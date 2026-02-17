package org.openmrs.module.ethiopiaemrcustommodule.task;

import org.openmrs.Encounter;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.ethiopiaemrcustommodule.EthiopiaEmrCustomModuleConstants;
import org.openmrs.module.ethiopiaemrcustommodule.PrescriptionOutbox;
import org.openmrs.module.ethiopiaemrcustommodule.PrescriptionOutboxStatus;
import org.openmrs.module.ethiopiaemrcustommodule.api.HttpClientService;
import org.openmrs.module.ethiopiaemrcustommodule.api.PrescriptionOutboxService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.scheduler.tasks.AbstractTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class EaptsPrescriptionSyncTask extends AbstractTask {
	
	private static final Logger log = LoggerFactory.getLogger(EaptsPrescriptionSyncTask.class);
	
	@Override
	public void execute() {
		log.info("Starting Prescription Transmission Task...");
		
		PrescriptionOutboxService service = Context.getService(PrescriptionOutboxService.class);
		List<PrescriptionOutbox> pendingList = service.getPendingPrescriptions(10);
		
		if (pendingList == null || pendingList.isEmpty()) {
			return;
		}
		
		for (PrescriptionOutbox outbox : pendingList) {
			try {
				processPrescription(outbox);
			}
			catch (Exception e) {
				log.error("Failed to process outbox record: " + outbox.getOutboxId(), e);
			}
		}
	}
	
	private void processPrescription(PrescriptionOutbox outbox) {
		Encounter encounter = outbox.getEncounter();
		PrescriptionOutboxService outboxService = Context.getService(PrescriptionOutboxService.class);
		HttpClientService httpClientService = Context.getService(HttpClientService.class);
		AdministrationService administrationService = Context.getAdministrationService();
		
		try {
			SimpleObject representation = (SimpleObject) ConversionUtil.convertToRepresentation(encounter,
			    Representation.DEFAULT);
			
			String endpoint = administrationService
			        .getGlobalProperty(EthiopiaEmrCustomModuleConstants.GP_EAPTS_PRESCRIPTION_SYNC_ENDPOINT);
			if (endpoint == null || endpoint.trim().isEmpty()) {
				throw new APIException("EAPTS prescription sync endpoint global property is not configured.");
			}
			// Optional: Fetch API Key if required by the external system
			// Map<String, String> headers = new HashMap<>();
			// headers.put("X-API-KEY", administrationService.getGlobalProperty("ethiopiaemrcustommodule.eaptsPrescriptionSync_api_key"));
			
			// Send the POST request
			ResponseEntity<String> response = httpClientService.post(endpoint, representation, String.class);
			
			if (response.getStatusCode().is2xxSuccessful()) {
				outbox.setStatus(PrescriptionOutboxStatus.SENT);
				outbox.setLastError(null);
				log.info("Successfully sent prescription for encounter: " + encounter.getUuid());
			} else {
				updateOutboxAsFailed(outbox, "Server returned: " + response.getStatusCodeValue());
			}
			
		}
		catch (Exception e) {
			log.error("Error transmitting prescription: " + encounter.getUuid(), e);
			updateOutboxAsFailed(outbox, e.getMessage());
		}
		finally {
			outboxService.savePrescriptionOutbox(outbox);
		}
	}
	
	private void updateOutboxAsFailed(PrescriptionOutbox outbox, String error) {
		outbox.setStatus(PrescriptionOutboxStatus.FAILED);
		outbox.setRetryCount(outbox.getRetryCount() + 1);
		outbox.setLastError(error);
	}
}
