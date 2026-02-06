/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ethioemrcustommodule.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.ethioemrcustommodule.api.DrugOrderSyncService;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * Scheduled task to sync drug orders to external endpoint
 * 
 * @see org.openmrs.module.ethioemrcustommodule.api.DrugOrderSyncService#syncDrugOrders()
 */
public class DrugOrderSyncTask extends AbstractTask {
	
	private static final Log log = LogFactory.getLog(DrugOrderSyncTask.class);
	
	@Override
	public void execute() {
		try {
			Context.openSession();
			DrugOrderSyncService syncService = Context.getService(DrugOrderSyncService.class);
			syncService.syncDrugOrders();
		}
		catch (Exception e) {
			log.error("Error executing drug order sync task", e);
		}
		finally {
			Context.closeSession();
		}
	}
}
