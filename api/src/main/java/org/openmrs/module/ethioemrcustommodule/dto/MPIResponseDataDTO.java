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
 * DTO representing the data section of MPI response. This can contain either success data (patient
 * info) or error data.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MPIResponseDataDTO {
	
	// Success response fields
	@JsonProperty("source")
	private String source;
	
	@JsonProperty("patient")
	private Object patient; // Using Object to handle dynamic structure
	
	@JsonProperty("identifiers")
	private Object identifiers;
	
	@JsonProperty("matchConfidence")
	private Double matchConfidence;
	
	@JsonProperty("medicalConditions")
	private Object medicalConditions;
	
	// Error response fields
	@JsonProperty("data")
	private String errorData; // The error message like "404 page not found\n"
	
	@JsonProperty("references")
	private Object references;
	
	@JsonProperty("request")
	private Object request;
	
	@JsonProperty("response")
	private MPIErrorResponseDTO response;
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public Object getPatient() {
		return patient;
	}
	
	public void setPatient(Object patient) {
		this.patient = patient;
	}
	
	public Object getIdentifiers() {
		return identifiers;
	}
	
	public void setIdentifiers(Object identifiers) {
		this.identifiers = identifiers;
	}
	
	public Double getMatchConfidence() {
		return matchConfidence;
	}
	
	public void setMatchConfidence(Double matchConfidence) {
		this.matchConfidence = matchConfidence;
	}
	
	public Object getMedicalConditions() {
		return medicalConditions;
	}
	
	public void setMedicalConditions(Object medicalConditions) {
		this.medicalConditions = medicalConditions;
	}
	
	public String getErrorData() {
		return errorData;
	}
	
	public void setErrorData(String errorData) {
		this.errorData = errorData;
	}
	
	public Object getReferences() {
		return references;
	}
	
	public void setReferences(Object references) {
		this.references = references;
	}
	
	public Object getRequest() {
		return request;
	}
	
	public void setRequest(Object request) {
		this.request = request;
	}
	
	public MPIErrorResponseDTO getResponse() {
		return response;
	}
	
	public void setResponse(MPIErrorResponseDTO response) {
		this.response = response;
	}
	
	/**
	 * Checks if this is a success response (has patient data).
	 * 
	 * @return true if success response, false if error response
	 */
	public boolean isSuccessResponse() {
		return patient != null || source != null;
	}
}
