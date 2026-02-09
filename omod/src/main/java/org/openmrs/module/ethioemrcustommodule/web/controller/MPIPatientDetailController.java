package org.openmrs.module.ethioemrcustommodule.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.module.ethioemrcustommodule.api.MPIPatientDetailProxyService;
import org.openmrs.module.ethioemrcustommodule.dto.MPIPatientDetailResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MPIPatientDetailController {
	
	@Autowired
	private MPIPatientDetailProxyService MPIPatientDetailProxyService;
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(method = RequestMethod.GET, value = "/rest/**/ethioemrcustommodule/mpi/patient/{patientUuid}")
	@ResponseBody
	public ResponseEntity<MPIPatientDetailResponseDTO> getPatientDetailsFromMPI(HttpServletRequest request,
	        HttpServletResponse response, @PathVariable("patientUuid") String patientUuid) {
		
		try {
			log.info("Received request for MPI patient details for UUID: " + patientUuid);
			
			// Validate patient UUID
			if (patientUuid == null || patientUuid.trim().isEmpty()) {
				log.warn("Patient UUID is required");
				return new ResponseEntity<MPIPatientDetailResponseDTO>(HttpStatus.BAD_REQUEST);
			}
			
			patientUuid = patientUuid.trim();
			
			// Get patient details from MPI via service
			MPIPatientDetailResponseDTO responseDTO = MPIPatientDetailProxyService.getPatientDetailsFromMPI(patientUuid);
			
			// Set content type
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			return new ResponseEntity<MPIPatientDetailResponseDTO>(responseDTO, HttpStatus.OK);
			
		}
		catch (APIException e) {
			log.error("API error retrieving patient details from MPI for UUID: " + patientUuid, e);
			HttpStatus status = e.getMessage().contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
			return new ResponseEntity<MPIPatientDetailResponseDTO>(HttpStatus.valueOf(status.value()));
		}
		catch (Exception e) {
			log.error("Unexpected error retrieving patient details from MPI for UUID: " + patientUuid, e);
			return new ResponseEntity<MPIPatientDetailResponseDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
