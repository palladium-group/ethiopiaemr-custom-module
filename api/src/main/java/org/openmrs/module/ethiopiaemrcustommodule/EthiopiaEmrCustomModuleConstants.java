/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ethiopiaemrcustommodule;

/**
 * Constants for EthiopiaEMR Custom Module
 */
public class EthiopiaEmrCustomModuleConstants {
	
	/**
	 * Global property for the eAPTS prescription endpoint URL
	 */
	public static final String GP_EAPTS_PRESCRIPTION_SYNC_ENDPOINT = "ethiopiaemrcustommodule.eaptsPrescriptionSyncEndpoint";
	
	/**
	 * Global property for the drug order encounter type uuid
	 */
	public static final String GP_DRUG_ORDER_ENCOUNTER_TYPE_UUID = "ethiopiaemrcustommodule.drugOrderEncounterTYpeUuid";
	
	/**
	 * Global property for the MPI patient detail endpoint URL
	 */
	public static final String GP_MPI_PATIENT_DETAIL_ENDPOINT = "ethiopiaemrcustommodule.MPIPatientDetailEndpoint";
	
	/**
	 * HTTP connection timeout in milliseconds
	 */
	public static final int HTTP_CONNECT_TIMEOUT = 10000;
	
	/**
	 * HTTP socket timeout in milliseconds
	 */
	public static final int HTTP_SOCKET_TIMEOUT = 30000;
	
	/**
	 * HTTP connection request timeout in milliseconds
	 */
	public static final int HTTP_CONNECTION_REQUEST_TIMEOUT = 5000;
	
	/**
	 * HTTP maximum total connections
	 */
	public static final int HTTP_MAX_TOTAL_CONNECTIONS = 50;
	
	/**
	 * HTTP maximum connections per route
	 */
	public static final int HTTP_MAX_CONNECTIONS_PER_ROUTE = 20;
	
}
