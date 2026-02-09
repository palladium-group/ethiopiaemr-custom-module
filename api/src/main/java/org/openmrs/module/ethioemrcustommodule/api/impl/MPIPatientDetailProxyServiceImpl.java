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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.APIException;
import org.openmrs.api.PatientService;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.ethioemrcustommodule.EthioEmrCustomModuleConstants;
import org.openmrs.module.ethioemrcustommodule.api.HttpClientService;
import org.openmrs.module.ethioemrcustommodule.api.MPIPatientDetailProxyService;
import org.openmrs.module.ethioemrcustommodule.dto.FHIRPatientResponseDTO;
import org.springframework.http.ResponseEntity;

/**
 * Implementation of MPIPatientDetailProxyService for proxying patient detail requests to MPI.
 */
public class MPIPatientDetailProxyServiceImpl extends BaseOpenmrsService implements MPIPatientDetailProxyService {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private AdministrationService administrationService;
	
	private PatientService patientService;
	
	private HttpClientService httpClientService;
	
	/**
	 * Injected via moduleApplicationContext.xml
	 */
	public void setAdministrationService(AdministrationService administrationService) {
		this.administrationService = administrationService;
	}
	
	/**
	 * Injected via moduleApplicationContext.xml
	 */
	public void setPatientService(PatientService patientService) {
		this.patientService = patientService;
	}
	
	/**
	 * Injected via moduleApplicationContext.xml
	 */
	public void setHttpClientService(HttpClientService httpClientService) {
		this.httpClientService = httpClientService;
	}
	
	@Override
	public FHIRPatientResponseDTO getPatientDetailsFromMPI(String patientUuid) throws APIException {
		if (patientUuid == null || patientUuid.trim().isEmpty()) {
			throw new APIException("ethioemrcustommodule.error.patientUuidRequired");
		}

		Patient patient = patientService.getPatientByUuid(patientUuid);
		if (patient == null) {
			throw new APIException("ethioemrcustommodule.error.patientNotFound");
		}

		String healthIdIdentifierTypeUuid = getHealthIdIdentifierTypeUuid();
		PatientIdentifierType healthIdType = patientService.getPatientIdentifierTypeByUuid(healthIdIdentifierTypeUuid);
		if (healthIdType == null) {
			throw new APIException("ethioemrcustommodule.error.identifierTypeNotFound");
		}

		PatientIdentifier healthIdIdentifier = patient.getPatientIdentifier(healthIdType);
		if (healthIdIdentifier == null || healthIdIdentifier.getIdentifier() == null) {
			throw new APIException("ethioemrcustommodule.error.missingHealthId");
		}

		String healthId = healthIdIdentifier.getIdentifier().trim();
		log.info("Requesting MPI details for healthId: " + healthId);

		try {
			String endpoint = getMPIEndpoint();

			Map<String, String> requestPayload = new HashMap<>();
			requestPayload.put("healthId", healthId);

			ResponseEntity<FHIRPatientResponseDTO> response = httpClientService.post(endpoint, requestPayload,
					FHIRPatientResponseDTO.class);

			FHIRPatientResponseDTO responseDTO = response.getBody();

			if (responseDTO == null || responseDTO.isPatientNotFound()) {
				log.info("Patient with healthId " + healthId + " not found in MPI.");
				return null;
			}

			return responseDTO;

		}
		catch (APIException e) {
			throw e;
		}
		catch (Exception e) {
			log.error("Failed to communicate with MPI for healthId: " + healthId, e);
			throw new APIException("ethioemrcustommodule.error.mpiCommunicationError", e);
		}
	}
	
	private String getMPIEndpoint() {
		String endpoint = administrationService
		        .getGlobalProperty(EthioEmrCustomModuleConstants.GP_MPI_PATIENT_DETAIL_ENDPOINT);
		if (endpoint == null || endpoint.trim().isEmpty()) {
			throw new APIException("MPI endpoint global property is not configured.");
		}
		return endpoint.trim();
	}
	
	private String getHealthIdIdentifierTypeUuid() {
		String uuid = administrationService
		        .getGlobalProperty(EthioEmrCustomModuleConstants.GP_HEALTH_ID_IDENTIFIER_TYPE_UUID);
		if (uuid == null || uuid.trim().isEmpty()) {
			throw new APIException("HealthId identifier type UUID global property is not configured.");
		}
		return uuid.trim();
	}
}
