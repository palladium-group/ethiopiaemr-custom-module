package org.openmrs.module.ethiopiaemrcustommodule.api;

import org.openmrs.Encounter;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.ethiopiaemrcustommodule.PrescriptionOutbox;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface PrescriptionOutboxService extends OpenmrsService {
	
	PrescriptionOutbox savePrescriptionOutbox(PrescriptionOutbox outbox);
	
	@Transactional(readOnly = true)
	PrescriptionOutbox getPrescriptionOutboxByUuid(String uuid);
	
	@Transactional(readOnly = true)
	List<PrescriptionOutbox> getPendingPrescriptions(Integer limit);
	
	@Transactional(readOnly = true)
	PrescriptionOutbox getLatestOutboxByEncounter(Encounter encounter);
}
