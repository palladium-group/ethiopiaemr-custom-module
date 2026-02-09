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

import org.springframework.http.ResponseEntity;

/**
 * Service for making HTTP requests with enterprise-grade features.
 */
public interface HttpClientService {
	
	/**
	 * Makes a POST request with JSON payload and returns the response.
	 * 
	 * @param url the endpoint URL
	 * @param request the request body object
	 * @param responseType the expected response type
	 * @param <T> the response type
	 * @return ResponseEntity containing the response
	 * @throws org.openmrs.api.APIException if the request fails
	 */
	<T> ResponseEntity<T> post(String url, Object request, Class<T> responseType);
	
	/**
	 * Makes a GET request and returns the response.
	 * 
	 * @param url the endpoint URL
	 * @param responseType the expected response type
	 * @param <T> the response type
	 * @return ResponseEntity containing the response
	 * @throws org.openmrs.api.APIException if the request fails
	 */
	<T> ResponseEntity<T> get(String url, Class<T> responseType);
}
