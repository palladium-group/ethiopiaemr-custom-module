package org.openmrs.module.ethioemrcustommodule.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.api.APIException;
import org.openmrs.module.ethioemrcustommodule.api.PatientDetailProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class OpenFnPatientDetailController {
	
	@Autowired
	private PatientDetailProxyService patientDetailProxyService;
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(method = RequestMethod.GET, value = "/rest/**/ethioemrcustommodule/openfn/patient/{patientUuid}")
	@ResponseBody
	public ResponseEntity<String> getPatientDetailsFromOpenFn(HttpServletRequest request, HttpServletResponse response,
	        @PathVariable("patientUuid") String patientUuid) {
		
		try {
			log.info("Received request for OpenFn patient details for UUID: " + patientUuid);
			
			// Validate patient UUID
			if (patientUuid == null || patientUuid.trim().isEmpty()) {
				Map<String, String> error = new HashMap<String, String>();
				error.put("error", "Patient UUID is required");
				return new ResponseEntity<String>(convertToJson(error), HttpStatus.BAD_REQUEST);
			}
			
			patientUuid = patientUuid.trim();
			
			// Get patient details from OpenFn via service
			String patientDetailsJson = patientDetailProxyService.getPatientDetailsFromOpenFn(patientUuid);
			
			// Set content type
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			return new ResponseEntity<String>(patientDetailsJson, HttpStatus.OK);
			
		}
		catch (APIException e) {
			log.error("API error retrieving patient details from OpenFn for UUID: " + patientUuid, e);
			Map<String, String> error = new HashMap<String, String>();
			error.put("error", e.getMessage());
			HttpStatus status = e.getMessage().contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
			return new ResponseEntity<String>(convertToJson(error), status);
		}
		catch (Exception e) {
			log.error("Unexpected error retrieving patient details from OpenFn for UUID: " + patientUuid, e);
			Map<String, String> error = new HashMap<String, String>();
			error.put("error", "Internal server error: " + e.getMessage());
			return new ResponseEntity<String>(convertToJson(error), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private String convertToJson(Map<String, String> map) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(map);
		}
		catch (Exception e) {
			log.error("Error converting map to JSON", e);
			return "{\"error\":\"Internal error\"}";
		}
	}
}
