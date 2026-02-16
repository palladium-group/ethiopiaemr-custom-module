package org.openmrs.module.ethiopiaemrcustommodule.api.advice;

import org.openmrs.Encounter;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.ethiopiaemrcustommodule.EthiopiaEmrCustomModuleConstants;
import org.openmrs.module.ethiopiaemrcustommodule.PrescriptionOutbox;
import org.openmrs.module.ethiopiaemrcustommodule.PrescriptionOutboxStatus;
import org.openmrs.module.ethiopiaemrcustommodule.api.PrescriptionOutboxService;
import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;
import java.util.Date;

public class PrescriptionSaveAdvice implements AfterReturningAdvice {
	
	private AdministrationService administrationService;
	
	/**
	 * Injected via moduleApplicationContext.xml
	 */
	public void setAdministrationService(AdministrationService administrationService) {
		this.administrationService = administrationService;
	}
	
	@Override
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		
		// We only care about the saveEncounter method
		if (!method.getName().equals("saveEncounter")) {
			return;
		}
		String drugOrderEncounterTypeUuid = administrationService
		        .getGlobalProperty(EthiopiaEmrCustomModuleConstants.GP_DRUG_ORDER_ENCOUNTER_TYPE_UUID);
		if (drugOrderEncounterTypeUuid == null || drugOrderEncounterTypeUuid.trim().isEmpty()) {
			throw new APIException("Drug order encounter type UUID global property is not configured.");
		}
		Encounter encounter = (Encounter) returnValue;
		// Check if this is the right encounter type
		if (encounter != null && encounter.getEncounterType() != null
		        && drugOrderEncounterTypeUuid.equals(encounter.getEncounterType().getUuid())) {
			
			saveToOutbox(encounter);
		}
	}
	
	private void saveToOutbox(Encounter encounter) {
		PrescriptionOutboxService service = Context.getService(PrescriptionOutboxService.class);
		
		// Look for the most recent record for this encounter
		PrescriptionOutbox existing = service.getLatestOutboxByEncounter(encounter);
		
		if (existing != null && PrescriptionOutboxStatus.PENDING.equals(existing.getStatus())) {
			// There is already a record waiting for the scheduler.
			// We don't need to do anything. When the scheduler runs,
			// it will pull the LATEST state of the encounter from the DB.
			return;
		}
		
		// If there's no existing record OR the last one was already 'SENT' or 'FAILED',
		// create a new PENDING entry to trigger a new sync.
		PrescriptionOutbox outbox = new PrescriptionOutbox();
		outbox.setEncounter(encounter);
		outbox.setStatus(PrescriptionOutboxStatus.PENDING);
		outbox.setRetryCount(0);
		outbox.setDateCreated(new Date());
		
		service.savePrescriptionOutbox(outbox);
	}
	
}
