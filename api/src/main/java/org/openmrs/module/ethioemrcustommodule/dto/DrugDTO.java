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

/**
 * DTO representing a drug
 */
public class DrugDTO {
	
	private String name;
	
	private String uuid;
	
	private Integer dagu_id;
	
	private Object drugReferenceMaps;
	
	public DrugDTO() {
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public Integer getDagu_id() {
		return dagu_id;
	}
	
	public void setDagu_id(Integer dagu_id) {
		this.dagu_id = dagu_id;
	}
	
	public Object getDrugReferenceMaps() {
		return drugReferenceMaps;
	}
	
	public void setDrugReferenceMaps(Object drugReferenceMaps) {
		this.drugReferenceMaps = drugReferenceMaps;
	}
}
