/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ethiopiaemrcustommodule.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * DTO representing the FHIR patient data from MPI response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FHIRPatientDataDTO {
	
	@JsonProperty("uuid")
	private String uuid;
	
	@JsonProperty("person")
	private FHIRPersonDTO person;
	
	@JsonProperty("healthId")
	private String healthId;
	
	@JsonProperty("attributes")
	private List<FHIRAttributeDTO> attributes;
	
	@JsonProperty("identifiers")
	private List<Object> identifiers;
	
	@JsonProperty("allergies")
	private List<Object> allergies;
	
	@JsonProperty("bloodType")
	private String bloodType;
	
	@JsonProperty("chronicDiseases")
	private List<Object> chronicDiseases;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("nationality")
	private String nationality;
	
	@JsonProperty("phone")
	private String phone;
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public FHIRPersonDTO getPerson() {
		return person;
	}
	
	public void setPerson(FHIRPersonDTO person) {
		this.person = person;
	}
	
	public String getHealthId() {
		return healthId;
	}
	
	public void setHealthId(String healthId) {
		this.healthId = healthId;
	}
	
	public List<FHIRAttributeDTO> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(List<FHIRAttributeDTO> attributes) {
		this.attributes = attributes;
	}
	
	public List<Object> getIdentifiers() {
		return identifiers;
	}
	
	public void setIdentifiers(List<Object> identifiers) {
		this.identifiers = identifiers;
	}
	
	public List<Object> getAllergies() {
		return allergies;
	}
	
	public void setAllergies(List<Object> allergies) {
		this.allergies = allergies;
	}
	
	public String getBloodType() {
		return bloodType;
	}
	
	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}
	
	public List<Object> getChronicDiseases() {
		return chronicDiseases;
	}
	
	public void setChronicDiseases(List<Object> chronicDiseases) {
		this.chronicDiseases = chronicDiseases;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNationality() {
		return nationality;
	}
	
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
