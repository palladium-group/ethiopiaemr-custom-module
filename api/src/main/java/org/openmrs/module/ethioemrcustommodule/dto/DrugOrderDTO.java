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
 * DTO representing a single drug order
 */
public class DrugOrderDTO {
	
	private DrugDTO drug;
	
	private String type;
	
	private RouteDTO route;
	
	private OrdererDTO orderer;
	
	private PatientDTO patient;
	
	private Double quantity;
	
	private FrequencyDTO frequency;
	
	public DrugOrderDTO() {
	}
	
	public DrugDTO getDrug() {
		return drug;
	}
	
	public void setDrug(DrugDTO drug) {
		this.drug = drug;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public RouteDTO getRoute() {
		return route;
	}
	
	public void setRoute(RouteDTO route) {
		this.route = route;
	}
	
	public OrdererDTO getOrderer() {
		return orderer;
	}
	
	public void setOrderer(OrdererDTO orderer) {
		this.orderer = orderer;
	}
	
	public PatientDTO getPatient() {
		return patient;
	}
	
	public void setPatient(PatientDTO patient) {
		this.patient = patient;
	}
	
	public Double getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	
	public FrequencyDTO getFrequency() {
		return frequency;
	}
	
	public void setFrequency(FrequencyDTO frequency) {
		this.frequency = frequency;
	}
}
