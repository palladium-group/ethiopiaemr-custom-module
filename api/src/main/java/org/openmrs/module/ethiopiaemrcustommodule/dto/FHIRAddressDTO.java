/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ethiopiaemrcustommodule.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * DTO representing the FHIR address data from MPI response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FHIRAddressDTO {
	
	@JsonProperty("country")
	private String country;
	
	@JsonProperty("address1")
	private String address1;
	
	@JsonProperty("address4")
	private String address4;
	
	@JsonProperty("countyDistrict")
	private String countyDistrict;
	
	@JsonProperty("stateProvince")
	private String stateProvince;
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getAddress1() {
		return address1;
	}
	
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	
	public String getAddress4() {
		return address4;
	}
	
	public void setAddress4(String address4) {
		this.address4 = address4;
	}
	
	public String getCountyDistrict() {
		return countyDistrict;
	}
	
	public void setCountyDistrict(String countyDistrict) {
		this.countyDistrict = countyDistrict;
	}
	
	public String getStateProvince() {
		return stateProvince;
	}
	
	public void setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
	}
}
