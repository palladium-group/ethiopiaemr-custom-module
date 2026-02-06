/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ethioemrcustommodule.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.ethioemrcustommodule.EthioEmrCustomModuleConfig;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for proxying patient detail requests to OpenFn server.
 */
public interface PatientDetailProxyService extends OpenmrsService {
	
	/**
	 * Retrieves patient details from OpenFn server using the patient UUID.
	 * 
	 * @param patientUuid the UUID of the patient
	 * @return DTO containing patient details from OpenFn
	 * @throws APIException if the request fails, patient UUID is invalid, or patient not found in
	 *             MPI
	 */
	@Authorized(EthioEmrCustomModuleConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	org.openmrs.module.ethioemrcustommodule.dto.OpenFnPatientDetailResponseDTO getPatientDetailsFromOpenFn(String patientUuid)
	        throws APIException;
}
