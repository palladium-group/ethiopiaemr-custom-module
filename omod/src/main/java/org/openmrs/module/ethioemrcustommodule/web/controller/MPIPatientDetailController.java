package org.openmrs.module.ethioemrcustommodule.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.module.ethioemrcustommodule.api.MPIPatientDetailProxyService;
import org.openmrs.module.ethioemrcustommodule.dto.FHIRPatientResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MPIPatientDetailController {
	
	@Autowired
	private MPIPatientDetailProxyService MPIPatientDetailProxyService;
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(method = RequestMethod.GET, value = "/rest/**/ethioemrcustommodule/mpi/patient")
	@ResponseBody
	public ResponseEntity<FHIRPatientResponseDTO> getPatientDetailsFromMPI(HttpServletRequest request,
	        HttpServletResponse response, @RequestParam("healthId") String healthId) {
		
		try {
			log.info("Received request for MPI patient details for healthId: " + healthId);
			
			// Validate patient healthId
			if (healthId == null || healthId.trim().isEmpty()) {
				log.warn("Patient healthId is required");
				return new ResponseEntity<FHIRPatientResponseDTO>(HttpStatus.BAD_REQUEST);
			}
			
			healthId = healthId.trim();
			
			FHIRPatientResponseDTO responseDTO = MPIPatientDetailProxyService.getPatientDetailsFromMPIByHealthId(healthId);

			if (responseDTO == null) {
				log.warn("Patient details not found in MPI for healthId: " + healthId);
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
			// Set content type
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			return new ResponseEntity<FHIRPatientResponseDTO>(responseDTO, HttpStatus.OK);
			
		}
		catch (APIException e) {
			log.error("API error retrieving patient details from MPI for Patient Health ID: " + healthId, e);
			HttpStatus status = e.getMessage().contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
			return new ResponseEntity<FHIRPatientResponseDTO>(HttpStatus.valueOf(status.value()));
		}
		catch (Exception e) {
			log.error("Unexpected error retrieving patient details from MPI for Patient Health ID: " + healthId, e);
			return new ResponseEntity<FHIRPatientResponseDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
