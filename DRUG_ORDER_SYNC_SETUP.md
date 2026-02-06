# Drug Order Sync Scheduler - Setup and Testing Guide

## Overview
This feature syncs drug orders to an external endpoint using a scheduled task that runs at configurable intervals.

## Step 1: Configure Global Properties

Go to **Administration → System Administration → Global Properties** and configure the following:

### 1. Last Processed Drug Order Date
- **Property**: `ethioemrcustommodule.lastProcessedDrugOrderDate`
- **Value**: Leave empty for first run (will default to 24 hours ago)
- **Format**: `yyyy-MM-dd HH:mm:ss` (e.g., `2026-02-05 10:30:00`)
- **Description**: Automatically updated after each successful sync. This marks the last processed order date.

### 2. Scheduler Interval
- **Property**: `ethioemrcustommodule.drugOrderSyncInterval`
- **Value**: `300` (default: 5 minutes in seconds)
- **Description**: How often the scheduler should run (in seconds)
- **Examples**:
  - `60` = 1 minute
  - `300` = 5 minutes
  - `600` = 10 minutes
  - `3600` = 1 hour

### 3. Batch Size
- **Property**: `ethioemrcustommodule.drugOrderSyncBatchSize`
- **Value**: `100` (default)
- **Description**: Maximum number of drug orders to process per run

### 4. Endpoint URL
- **Property**: `ethioemrcustommodule.drugOrderSyncEndpoint`
- **Value**: `http://localhost:3000` (or your actual endpoint)
- **Description**: The external API endpoint where drug orders will be sent

## Step 2: Register the Scheduler Task

### Option A: Via OpenMRS Admin UI (Recommended)

1. Go to **Administration → System Administration → Manage Scheduler**
2. Click **"Create Task"**
3. Fill in the form:
   - **Name**: `Drug Order Sync Task`
   - **Description**: `Syncs drug orders to external endpoint`
   - **Task Class**: `org.openmrs.module.ethioemrcustommodule.task.DrugOrderSyncTask`
   - **Repeat Interval**: Enter the interval in seconds (e.g., `300` for 5 minutes)
   - **Start on Startup**: Check this box to start automatically
   - **Start Time**: Leave empty or set a specific start time
4. Click **"Save Task"**
5. Click **"Schedule Task"** to start it immediately

### Option B: Via SQL (Alternative)

If you prefer to register via SQL, you can run:

```sql
INSERT INTO scheduler_task_config (
    name, 
    description, 
    schedulable_class, 
    repeat_interval, 
    start_on_startup, 
    started, 
    created_by, 
    date_created, 
    changed_by, 
    date_changed, 
    uuid
) VALUES (
    'Drug Order Sync Task',
    'Syncs drug orders to external endpoint',
    'org.openmrs.module.ethioemrcustommodule.task.DrugOrderSyncTask',
    300,
    1,
    0,
    1,
    NOW(),
    NULL,
    NULL,
    UUID()
);
```

Then start it via UI or:
```sql
UPDATE scheduler_task_config 
SET started = 1 
WHERE name = 'Drug Order Sync Task';
```

## Step 3: Testing the Feature

### Test 1: Manual Service Call (Quick Test)

1. Go to **Administration → System Administration → Module Management**
2. Find your module and ensure it's started
3. Use the Groovy Console or create a test script:

```groovy
import org.openmrs.api.context.Context
import org.openmrs.module.ethioemrcustommodule.api.DrugOrderSyncService

DrugOrderSyncService syncService = Context.getService(DrugOrderSyncService.class)
syncService.syncDrugOrders()
```

### Test 2: Create Test Drug Orders

1. Create a patient encounter
2. Add one or more drug orders to the encounter
3. Save the encounter
4. Wait for the scheduler to run (or trigger it manually)
5. Check the logs for sync activity

### Test 3: Verify Scheduler Execution

1. Go to **Administration → System Administration → Manage Scheduler**
2. Find "Drug Order Sync Task"
3. Check the **"Last Execution Time"** and **"Next Execution Time"**
4. Click **"Run Task Now"** to trigger immediately

### Test 4: Check Logs

Check the OpenMRS logs for:
- `INFO - Starting drug order sync`
- `INFO - Found X drug orders to sync`
- `INFO - Successfully synced X drug orders`
- `INFO - Updated last processed date to: ...`

Look for errors if sync fails:
- `ERROR - Error during drug order sync`
- `ERROR - Error sending drug orders to endpoint`

### Test 5: Verify Endpoint Receives Data

1. Start your endpoint server (e.g., `http://localhost:3000`)
2. Monitor the endpoint logs/console
3. Trigger the sync manually or wait for scheduled run
4. Verify the POST request is received with JSON payload:
```json
{
  "drug_orders": [
    {
      "drug": {
        "name": "...",
        "uuid": "...",
        "dagu_id": ...,
        "drugReferenceMaps": null
      },
      "type": "drugorder",
      "route": {...},
      "orderer": {...},
      "patient": {...},
      "quantity": ...,
      "frequency": {...}
    }
  ]
}
```

## Step 4: Monitor and Troubleshoot

### Check Scheduler Status
- **Administration → System Administration → Manage Scheduler**
- Look for "Drug Order Sync Task" status
- Check if it's running, stopped, or has errors

### Check Global Properties
- Verify all 4 global properties are set correctly
- Check `lastProcessedDrugOrderDate` is being updated after successful syncs

### Common Issues

1. **Scheduler not running**
   - Check if task is scheduled and started
   - Verify task class name is correct
   - Check module is started

2. **No drug orders found**
   - Check `lastProcessedDrugOrderDate` - might be too recent
   - Verify drug orders exist with `dateActivated` after the last processed date
   - Check if orders are voided (voided orders are excluded)

3. **Endpoint connection failed**
   - Verify endpoint URL is correct
   - Check if endpoint server is running
   - Verify network connectivity
   - Check endpoint logs for errors

4. **JSON serialization errors**
   - Ensure Jackson dependency is available
   - Check logs for serialization errors

## Step 5: Verify Data Flow

1. **Create a drug order** in OpenMRS
2. **Wait for scheduler** to run (or trigger manually)
3. **Check logs** for sync activity
4. **Verify endpoint** receives the data
5. **Check global property** `lastProcessedDrugOrderDate` is updated
6. **Create another drug order** and verify it's picked up in next run

## Testing Checklist

- [ ] Global properties configured
- [ ] Scheduler task registered
- [ ] Scheduler task started
- [ ] Test endpoint server running
- [ ] Created test drug order
- [ ] Triggered sync manually
- [ ] Verified logs show sync activity
- [ ] Verified endpoint received data
- [ ] Verified `lastProcessedDrugOrderDate` updated
- [ ] Verified scheduler runs automatically

## Notes

- The scheduler processes orders by `dateActivated` (not `dateCreated`)
- Only non-voided orders are processed
- Orders are ordered by `dateActivated` ascending
- The `lastProcessedDrugOrderDate` is updated to the latest processed order's date
- If sync fails, the date is NOT updated (so failed orders will be retried)

