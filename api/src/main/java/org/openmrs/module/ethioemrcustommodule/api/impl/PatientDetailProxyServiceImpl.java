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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	public String getPatientDetailsFromOpenFn(String patientUuid) throws APIException {
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
			
			// Make request to OpenFn endpoint
			URL url = new URL(endpoint);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.setDoOutput(true);
			connection.setConnectTimeout(10000); // 10 seconds
			connection.setReadTimeout(30000); // 30 seconds
			
			// Send request body
			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}
			
			// Get response
			int responseCode = connection.getResponseCode();
			log.info("OpenFn response code: " + responseCode);
			
			// Read response body
			String responseBody;
			if (responseCode >= 200 && responseCode < 300) {
				// Success - read from input stream
				try (BufferedReader reader = new BufferedReader(
				        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
					StringBuilder responseBuilder = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						responseBuilder.append(line);
					}
					responseBody = responseBuilder.toString();
				}
			} else {
				// Error - read from error stream if available
				if (connection.getErrorStream() != null) {
					try (BufferedReader reader = new BufferedReader(
					        new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
						StringBuilder errorBuilder = new StringBuilder();
						String line;
						while ((line = reader.readLine()) != null) {
							errorBuilder.append(line);
						}
						responseBody = errorBuilder.toString();
					}
				} else {
					responseBody = "{\"error\": \"Request failed with status code: " + responseCode + "\"}";
				}
				
				connection.disconnect();
				throw new APIException("OpenFn request failed with status code: " + responseCode + ". Response: " + responseBody);
			}
			
			connection.disconnect();
			
			log.debug("OpenFn response: " + responseBody);
			return responseBody;
			
		} catch (APIException e) {
			throw e;
		} catch (Exception e) {
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
