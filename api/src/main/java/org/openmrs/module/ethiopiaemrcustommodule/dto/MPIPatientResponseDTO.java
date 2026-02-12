package org.openmrs.module.ethiopiaemrcustommodule.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * DTO representing the complete MPI API response wrapper.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MPIPatientResponseDTO {
	
	@JsonProperty("data")
	private FHIRPatientResponseDTO data;
	
	@JsonProperty("meta")
	private MPIMetaDTO meta;
	
	public FHIRPatientResponseDTO getData() {
		return data;
	}
	
	public void setData(FHIRPatientResponseDTO data) {
		this.data = data;
	}
	
	public MPIMetaDTO getMeta() {
		return meta;
	}
	
	public void setMeta(MPIMetaDTO meta) {
		this.meta = meta;
	}
	
}
