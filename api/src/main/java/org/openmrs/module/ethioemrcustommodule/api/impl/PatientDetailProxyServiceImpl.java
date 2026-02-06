/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ethioemrcustommodule.api.impl;

import java.nio.charset.StandardCharsets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.APIException;
import org.openmrs.api.PatientService;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.ethioemrcustommodule.EthioEmrCustomModuleConstants;
import org.openmrs.module.ethioemrcustommodule.api.PatientDetailProxyService;
import org.openmrs.module.ethioemrcustommodule.dto.OpenFnPatientDetailResponseDTO;

/**
 * Implementation of PatientDetailProxyService for proxying patient detail requests to OpenFn.
 */
public class PatientDetailProxyServiceImpl extends BaseOpenmrsService implements PatientDetailProxyService {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private AdministrationService administrationService;
	
	private PatientService patientService;
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setAdministrationService(AdministrationService administrationService) {
		this.administrationService = administrationService;
	}
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setPatientService(PatientService patientService) {
		this.patientService = patientService;
	}
	
	@Override
	public OpenFnPatientDetailResponseDTO getPatientDetailsFromOpenFn(String patientUuid) throws APIException {
		if (patientUuid == null || patientUuid.trim().isEmpty()) {
			throw new APIException("Patient UUID cannot be null or empty");
		}
		
		// Get patient by UUID
		Patient patient = patientService.getPatientByUuid(patientUuid);
		if (patient == null) {
			throw new APIException("Patient with UUID " + patientUuid + " not found");
		}
		
		// Get healthId identifier type
		PatientIdentifierType healthIdType = patientService.getPatientIdentifierTypeByName("healthId");
		if (healthIdType == null) {
			throw new APIException("Patient identifier type 'healthId' not found in the system");
		}
		
		// Get patient's healthId identifier
		PatientIdentifier healthIdIdentifier = patient.getPatientIdentifier(healthIdType);
		if (healthIdIdentifier == null || healthIdIdentifier.getIdentifier() == null
		        || healthIdIdentifier.getIdentifier().trim().isEmpty()) {
			throw new APIException("Patient with UUID " + patientUuid + " does not have a healthId identifier");
		}
		
		String healthId = healthIdIdentifier.getIdentifier().trim();
		log.info("Proxying patient detail request for UUID: " + patientUuid + " with healthId: " + healthId);
		
		try {
			// Get OpenFn endpoint from global property
			String endpoint = getOpenFnEndpoint();
			
			// Create request payload with healthId
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode requestPayload = mapper.createObjectNode();
			requestPayload.put("healthId", healthId);
			String jsonPayload = mapper.writeValueAsString(requestPayload);
			
			// Create HTTP client with timeout configuration
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000) // 10 seconds
			        .setSocketTimeout(30000) // 30 seconds
			        .setConnectionRequestTimeout(10000) // 10 seconds
			        .build();
			
			HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
			
			// Create POST request
			HttpPost httpPost = new HttpPost(endpoint);
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setEntity(new StringEntity(jsonPayload, StandardCharsets.UTF_8));
			
			// Execute request
			log.debug("Sending POST request to OpenFn endpoint: " + endpoint);
			log.debug("Request payload: " + jsonPayload);
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			log.info("OpenFn response code: " + statusCode);
			
			// Read response body
			HttpEntity responseEntity = httpResponse.getEntity();
			String responseBody = null;
			if (responseEntity != null) {
				responseBody = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
				EntityUtils.consume(responseEntity); // Ensure entity is fully consumed
			}
			
			if (responseBody == null || responseBody.trim().isEmpty()) {
				throw new APIException("Empty response received from OpenFn");
			}
			
			log.debug("OpenFn response: " + responseBody);
			
			// Parse JSON response to DTO
			OpenFnPatientDetailResponseDTO responseDTO;
			try {
				responseDTO = mapper.readValue(responseBody, OpenFnPatientDetailResponseDTO.class);
			}
			catch (Exception e) {
				log.error("Error parsing OpenFn response JSON", e);
				throw new APIException("Error parsing response from OpenFn: " + e.getMessage(), e);
			}
			
			// Check if patient not found in MPI (404 error)
			if (responseDTO.isPatientNotFound()) {
				log.warn("Patient with healthId " + healthId + " not found in MPI system");
				throw new APIException("Patient not found in MPI system");
			}
			
			return responseDTO;
			
		}
		catch (APIException e) {
			throw e;
		}
		catch (Exception e) {
			log.error("Error proxying patient detail request to OpenFn", e);
			throw new APIException("Error retrieving patient details from OpenFn: " + e.getMessage(), e);
		}
	}
	
	/**
	 * Gets the OpenFn endpoint URL from global properties.
	 * 
	 * @return the OpenFn endpoint URL
	 */
	private String getOpenFnEndpoint() {
		String endpoint = administrationService
		        .getGlobalProperty(EthioEmrCustomModuleConstants.GP_OPENFN_PATIENT_DETAIL_ENDPOINT);
		if (endpoint == null || endpoint.trim().isEmpty()) {
			log.warn("OpenFn endpoint not configured, using default");
			return EthioEmrCustomModuleConstants.DEFAULT_OPENFN_PATIENT_DETAIL_ENDPOINT;
		}
		return endpoint.trim();
	}
}
