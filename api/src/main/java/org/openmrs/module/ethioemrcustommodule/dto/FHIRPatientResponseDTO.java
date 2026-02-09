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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * DTO representing the FHIR patient response from MPI.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FHIRPatientResponseDTO {
	
	@JsonProperty("fhir")
	private FHIRPatientDataDTO fhir;
	
	@JsonProperty("message")
	private String message;
	
	@JsonProperty("success")
	private Boolean success;

	@JsonProperty("patientNotFound")
	private Boolean patientNotFound;
	
	public FHIRPatientDataDTO getFhir() {
		return fhir;
	}
	
	public void setFhir(FHIRPatientDataDTO fhir) {
		this.fhir = fhir;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Boolean getSuccess() {
		return success;
	}
	
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	/**
	 * Checks if the response indicates patient not found.
	 * 
	 * @return true if patient not found, false otherwise
	 */
	public boolean isPatientNotFound() {
		return patientNotFound;
	}
}
