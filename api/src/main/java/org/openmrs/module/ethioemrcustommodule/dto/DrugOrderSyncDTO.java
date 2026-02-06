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

import java.util.List;

/**
 * DTO representing the drug orders sync payload
 */
public class DrugOrderSyncDTO {
	
	private List<DrugOrderDTO> drug_orders;
	
	public DrugOrderSyncDTO() {
	}
	
	public DrugOrderSyncDTO(List<DrugOrderDTO> drug_orders) {
		this.drug_orders = drug_orders;
	}
	
	public List<DrugOrderDTO> getDrug_orders() {
		return drug_orders;
	}
	
	public void setDrug_orders(List<DrugOrderDTO> drug_orders) {
		this.drug_orders = drug_orders;
	}
}
