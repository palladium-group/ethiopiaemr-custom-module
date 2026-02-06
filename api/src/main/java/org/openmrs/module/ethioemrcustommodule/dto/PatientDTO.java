/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ethioemrcustommodule.dto;

import java.util.List;

/**
 * DTO representing a patient
 */
public class PatientDTO {
	
	private String uuid;
	
	private PersonDTO person;
	
	private Boolean voided;
	
	private Integer dagu_id;
	
	private String display;
	
	private String openmrsID;
	
	private List<IdentifierDTO> identifiers;
	
	private String resourceVersion;
	
	public PatientDTO() {
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public PersonDTO getPerson() {
		return person;
	}
	
	public void setPerson(PersonDTO person) {
		this.person = person;
	}
	
	public Boolean getVoided() {
		return voided;
	}
	
	public void setVoided(Boolean voided) {
		this.voided = voided;
	}
	
	public Integer getDagu_id() {
		return dagu_id;
	}
	
	public void setDagu_id(Integer dagu_id) {
		this.dagu_id = dagu_id;
	}
	
	public String getDisplay() {
		return display;
	}
	
	public void setDisplay(String display) {
		this.display = display;
	}
	
	public String getOpenmrsID() {
		return openmrsID;
	}
	
	public void setOpenmrsID(String openmrsID) {
		this.openmrsID = openmrsID;
	}
	
	public List<IdentifierDTO> getIdentifiers() {
		return identifiers;
	}
	
	public void setIdentifiers(List<IdentifierDTO> identifiers) {
		this.identifiers = identifiers;
	}
	
	public String getResourceVersion() {
		return resourceVersion;
	}
	
	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}
}
