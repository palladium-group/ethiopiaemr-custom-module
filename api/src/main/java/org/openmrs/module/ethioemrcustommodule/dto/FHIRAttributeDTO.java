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
 * DTO representing the FHIR attribute data from MPI response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FHIRAttributeDTO {
	
	@JsonProperty("value")
	private Object value;
	
	@JsonProperty("attributeType")
	private String attributeType;
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public String getAttributeType() {
		return attributeType;
	}
	
	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}
}
