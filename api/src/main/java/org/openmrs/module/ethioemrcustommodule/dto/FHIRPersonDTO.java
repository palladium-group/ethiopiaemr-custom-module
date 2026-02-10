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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * DTO representing the FHIR person data from MPI response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FHIRPersonDTO {
	
	@JsonProperty("names")
	private List<FHIRNameDTO> names;
	
	@JsonProperty("gender")
	private String gender;
	
	@JsonProperty("addresses")
	private List<FHIRAddressDTO> addresses;
	
	@JsonProperty("birthdate")
	private String birthdate;
	
	public List<FHIRNameDTO> getNames() {
		return names;
	}
	
	public void setNames(List<FHIRNameDTO> names) {
		this.names = names;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public List<FHIRAddressDTO> getAddresses() {
		return addresses;
	}
	
	public void setAddresses(List<FHIRAddressDTO> addresses) {
		this.addresses = addresses;
	}
	
	public String getBirthdate() {
		return birthdate;
	}
	
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
}
