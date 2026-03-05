package org.openmrs.module.ethiopiaemrcustommodule.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * DTO for the inner "data" wrapper in the MPI response.  format: { "data": { "data":
 * &lt;patient&gt;, "message": "...", "success": true } }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MPIDataWrapperDTO {
	
	@JsonProperty("data")
	private FHIRPatientDataDTO data;
	
	@JsonProperty("message")
	private String message;
	
	@JsonProperty("success")
	private Boolean success;
	
	public FHIRPatientDataDTO getData() {
		return data;
	}
	
	public void setData(FHIRPatientDataDTO data) {
		this.data = data;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Boolean getSuccess() {
		return success;
	}
	
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	public boolean isSuccess() {
		return success != null && success;
	}
}
