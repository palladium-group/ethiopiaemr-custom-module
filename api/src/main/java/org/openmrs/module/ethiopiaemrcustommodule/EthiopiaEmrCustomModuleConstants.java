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
	 * Global property for the last processed drug order date
	 */
	public static final String GP_LAST_PROCESSED_DRUG_ORDER_DATE = "ethioemrcustommodule.lastProcessedDrugOrderDate";
	
	/**
	 * Global property for the scheduler interval in seconds
	 */
	public static final String GP_DRUG_ORDER_SYNC_INTERVAL = "ethiopiaemrcustommodule.drugOrderSyncInterval";
	
	/**
	 * Global property for the maximum number of drug orders to process per run
	 */
	public static final String GP_DRUG_ORDER_SYNC_BATCH_SIZE = "ethiopiaemrcustommodule.drugOrderSyncBatchSize";
	
	/**
	 * Global property for the external endpoint URL
	 */
	public static final String GP_DRUG_ORDER_SYNC_ENDPOINT = "ethiopiaemrcustommodule.drugOrderSyncEndpoint";
	
	/**
	 * Default scheduler interval in seconds (5 minutes)
	 */
	public static final int DEFAULT_SYNC_INTERVAL = 300;
	
	/**
	 * Default batch size
	 */
	public static final int DEFAULT_BATCH_SIZE = 100;
	
	/**
	 * Default endpoint URL
	 */
	public static final String DEFAULT_ENDPOINT = "http://localhost:3000";
	
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
