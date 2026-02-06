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
 * DTO representing the OpenFn patient detail response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenFnPatientDetailResponseDTO {
	
	@JsonProperty("data")
	private OpenFnResponseDataDTO data;
	
	@JsonProperty("meta")
	private OpenFnResponseMetaDTO meta;
	
	public OpenFnResponseDataDTO getData() {
		return data;
	}
	
	public void setData(OpenFnResponseDataDTO data) {
		this.data = data;
	}
	
	public OpenFnResponseMetaDTO getMeta() {
		return meta;
	}
	
	public void setMeta(OpenFnResponseMetaDTO meta) {
		this.meta = meta;
	}
	
	/**
	 * Checks if the response indicates patient not found (404 error).
	 * 
	 * @return true if patient not found, false otherwise
	 */
	public boolean isPatientNotFound() {
		if (data != null && data.getResponse() != null) {
			Integer statusCode = data.getResponse().getStatusCode();
			return statusCode != null && statusCode == 404;
		}
		return false;
	}
}
