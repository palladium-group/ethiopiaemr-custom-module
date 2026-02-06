/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ethioemrcustommodule;

/**
 * Constants for EthioEMR Custom Module
 */
public class EthioEmrCustomModuleConstants {
	
	/**
	 * Global property for the last processed drug order date
	 */
	public static final String GP_LAST_PROCESSED_DRUG_ORDER_DATE = "ethioemrcustommodule.lastProcessedDrugOrderDate";
	
	/**
	 * Global property for the scheduler interval in seconds
	 */
	public static final String GP_DRUG_ORDER_SYNC_INTERVAL = "ethioemrcustommodule.drugOrderSyncInterval";
	
	/**
	 * Global property for the maximum number of drug orders to process per run
	 */
	public static final String GP_DRUG_ORDER_SYNC_BATCH_SIZE = "ethioemrcustommodule.drugOrderSyncBatchSize";
	
	/**
	 * Global property for the external endpoint URL
	 */
	public static final String GP_DRUG_ORDER_SYNC_ENDPOINT = "ethioemrcustommodule.drugOrderSyncEndpoint";
	
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
	 * Global property for the OpenFn patient detail endpoint URL
	 */
	public static final String GP_OPENFN_PATIENT_DETAIL_ENDPOINT = "ethioemrcustommodule.openfnPatientDetailEndpoint";
	
	/**
	 * Default OpenFn patient detail endpoint URL
	 */
	public static final String DEFAULT_OPENFN_PATIENT_DETAIL_ENDPOINT = "https://app.openfn.org/i/e1efbe93-8784-4a16-8f56-fcff42b9c4cc";
}
