package org.openmrs.module.ethiopiaemrcustommodule.api.impl;

import org.openmrs.Encounter;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.ethiopiaemrcustommodule.PrescriptionOutbox;
import org.openmrs.module.ethiopiaemrcustommodule.api.PrescriptionOutboxService;
import org.openmrs.module.ethiopiaemrcustommodule.api.dao.PrescriptionOutboxDao;

import java.util.List;

public class PrescriptionOutboxServiceImpl extends BaseOpenmrsService implements PrescriptionOutboxService {
	
	private PrescriptionOutboxDao dao;
	
	public void setDao(PrescriptionOutboxDao dao) {
		this.dao = dao;
	}
	
	@Override
	public PrescriptionOutbox savePrescriptionOutbox(PrescriptionOutbox outbox) {
		return dao.savePrescriptionOutbox(outbox);
	}
	
	@Override
	public PrescriptionOutbox getPrescriptionOutboxByUuid(String uuid) {
		return dao.getPrescriptionOutboxByUuid(uuid);
	}
	
	@Override
	public List<PrescriptionOutbox> getPendingPrescriptions(Integer limit) {
		return dao.getPendingPrescriptions(limit);
	}
	
	@Override
	public PrescriptionOutbox getLatestOutboxByEncounter(Encounter encounter) {
		return dao.getLatestOutboxByEncounter(encounter);
	}
}
