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
 * DTO representing the meta section of OpenFn response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenFnResponseMetaDTO {
	
	@JsonProperty("state")
	private String state;
	
	@JsonProperty("started_at")
	private String startedAt;
	
	@JsonProperty("error_type")
	private String errorType;
	
	@JsonProperty("inserted_at")
	private String insertedAt;
	
	@JsonProperty("work_order_id")
	private String workOrderId;
	
	@JsonProperty("finished_at")
	private String finishedAt;
	
	@JsonProperty("run_id")
	private String runId;
	
	@JsonProperty("claimed_at")
	private String claimedAt;
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getStartedAt() {
		return startedAt;
	}
	
	public void setStartedAt(String startedAt) {
		this.startedAt = startedAt;
	}
	
	public String getErrorType() {
		return errorType;
	}
	
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	
	public String getInsertedAt() {
		return insertedAt;
	}
	
	public void setInsertedAt(String insertedAt) {
		this.insertedAt = insertedAt;
	}
	
	public String getWorkOrderId() {
		return workOrderId;
	}
	
	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
	}
	
	public String getFinishedAt() {
		return finishedAt;
	}
	
	public void setFinishedAt(String finishedAt) {
		this.finishedAt = finishedAt;
	}
	
	public String getRunId() {
		return runId;
	}
	
	public void setRunId(String runId) {
		this.runId = runId;
	}
	
	public String getClaimedAt() {
		return claimedAt;
	}
	
	public void setClaimedAt(String claimedAt) {
		this.claimedAt = claimedAt;
	}
}
