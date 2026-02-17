package org.openmrs.module.ethiopiaemrcustommodule;

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.Encounter;
import java.util.Date;

public class PrescriptionOutbox extends BaseOpenmrsObject {
	
	private Integer outboxId;
	
	private Encounter encounter;
	
	private PrescriptionOutboxStatus status;
	
	private Integer retryCount;
	
	private String lastError;
	
	private Date dateCreated;
	
	private Date dateChanged;
	
	public PrescriptionOutbox() {
	}
	
	// This is required by OpenMRS to identify the object
	@Override
	public Integer getId() {
		return getOutboxId();
	}
	
	@Override
	public void setId(Integer id) {
		setOutboxId(id);
	}
	
	public Integer getOutboxId() {
		return outboxId;
	}
	
	public void setOutboxId(Integer outboxId) {
		this.outboxId = outboxId;
	}
	
	public Encounter getEncounter() {
		return encounter;
	}
	
	public void setEncounter(Encounter encounter) {
		this.encounter = encounter;
	}
	
	public PrescriptionOutboxStatus getStatus() {
		return status;
	}
	
	public void setStatus(PrescriptionOutboxStatus status) {
		this.status = status;
	}
	
	public Integer getRetryCount() {
		return retryCount;
	}
	
	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}
	
	public String getLastError() {
		return lastError;
	}
	
	public void setLastError(String lastError) {
		this.lastError = lastError;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public Date getDateChanged() {
		return dateChanged;
	}
	
	public void setDateChanged(Date dateChanged) {
		this.dateChanged = dateChanged;
	}
}
