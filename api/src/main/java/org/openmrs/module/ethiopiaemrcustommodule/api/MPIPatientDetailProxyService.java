/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ethiopiaemrcustommodule.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.ethiopiaemrcustommodule.EthiopiaEmrCustomModuleConfig;
import org.openmrs.module.ethiopiaemrcustommodule.dto.FHIRPatientResponseDTO;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for proxying patient detail requests to MPI server.
 */
public interface MPIPatientDetailProxyService extends OpenmrsService {
	
	/**
	 * Retrieves patient details from MPI through MPI server using the patient healthId.
	 * 
	 * @param patientHealthId the healthId of the patient
	 * @return DTO containing patient details from MPI
	 * @throws APIException if the request fails, patient healthId is invalid, or patient not found
	 *             in MPI
	 */
	@Authorized(EthiopiaEmrCustomModuleConfig.MODULE_PRIVILEGE)
	@Transactional(readOnly = true)
	FHIRPatientResponseDTO getPatientDetailsFromMPIByHealthId(String patientHealthId) throws APIException;
}
