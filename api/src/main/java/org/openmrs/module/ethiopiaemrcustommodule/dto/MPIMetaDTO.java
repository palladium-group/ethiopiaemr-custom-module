package org.openmrs.module.ethiopiaemrcustommodule.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * DTO representing the metadata section of MPI API response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MPIMetaDTO {
	
	@JsonProperty("state")
	private String state;
	
	@JsonProperty("started_at")
	private String startedAt;
	
	@JsonProperty("error_type")
	private String errorType;
	
	@JsonProperty("inserted_at")
	private String insertedAt;
	
	@JsonProperty("run_id")
	private String runId;
	
	@JsonProperty("work_order_id")
	private String workOrderId;
	
	@JsonProperty("finished_at")
	private String finishedAt;
	
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
	
	public String getRunId() {
		return runId;
	}
	
	public void setRunId(String runId) {
		this.runId = runId;
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
	
	public String getClaimedAt() {
		return claimedAt;
	}
	
	public void setClaimedAt(String claimedAt) {
		this.claimedAt = claimedAt;
	}
	
	/**
	 * Checks if the MPI request was successful based on state.
	 * 
	 * @return true if state is "success", false otherwise
	 */
	public boolean isSuccess() {
		return "success".equalsIgnoreCase(state);
	}
	
	/**
	 * Checks if there was an error processing the request.
	 * 
	 * @return true if error_type is not null, false otherwise
	 */
	public boolean hasError() {
		return errorType != null && !errorType.isEmpty();
	}
}
