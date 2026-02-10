/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ethioemrcustommodule.api.impl;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.Provider;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.APIException;
import org.openmrs.api.OrderService;
import org.openmrs.api.PatientService;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.ethioemrcustommodule.EthioEmrCustomModuleConstants;
import org.openmrs.module.ethioemrcustommodule.api.DrugOrderSyncService;
import org.openmrs.module.ethioemrcustommodule.api.dao.DrugOrderSyncDao;
import org.openmrs.module.ethioemrcustommodule.dto.DrugOrderDTO;
import org.openmrs.module.ethioemrcustommodule.dto.DrugOrderSyncDTO;
import org.openmrs.module.ethioemrcustommodule.dto.DrugDTO;
import org.openmrs.module.ethioemrcustommodule.dto.FrequencyDTO;
import org.openmrs.module.ethioemrcustommodule.dto.IdentifierDTO;
import org.openmrs.module.ethioemrcustommodule.dto.OrdererDTO;
import org.openmrs.module.ethioemrcustommodule.dto.PatientDTO;
import org.openmrs.module.ethioemrcustommodule.dto.PersonDTO;
import org.openmrs.module.ethioemrcustommodule.dto.PersonDisplayDTO;
import org.openmrs.module.ethioemrcustommodule.dto.PreferredNameDTO;
import org.openmrs.module.ethioemrcustommodule.dto.RouteDTO;

/**
 * Implementation of DrugOrderSyncService
 */
public class DrugOrderSyncServiceImpl extends BaseOpenmrsService implements DrugOrderSyncService {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private DrugOrderSyncDao dao;
	
	private AdministrationService administrationService;
	
	private OrderService orderService;
	
	private PatientService patientService;
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(DrugOrderSyncDao dao) {
		this.dao = dao;
	}
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setAdministrationService(AdministrationService administrationService) {
		this.administrationService = administrationService;
	}
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setPatientService(PatientService patientService) {
		this.patientService = patientService;
	}
	
	@Override
	public void syncDrugOrders() throws APIException {
		log.info("Starting drug order sync");
		
		try {
			// Get configuration
			Date lastProcessedDate = getLastProcessedDate();
			int batchSize = getBatchSize();
			String endpoint = getEndpoint();
			
			// Query drug orders
			List<DrugOrder> drugOrders = dao.getDrugOrdersActivatedAfter(lastProcessedDate, batchSize);
			
			if (drugOrders.isEmpty()) {
				log.info("No new drug orders to sync");
				return;
			}
			
			log.info("Found " + drugOrders.size() + " drug orders to sync");
			
			// Convert to DTOs
			List<DrugOrderDTO> drugOrderDTOs = new ArrayList<DrugOrderDTO>();
			for (DrugOrder drugOrder : drugOrders) {
				DrugOrderDTO dto = convertToDTO(drugOrder);
				if (dto != null) {
					drugOrderDTOs.add(dto);
				}
			}
			
			if (drugOrderDTOs.isEmpty()) {
				log.warn("No valid drug orders to sync after conversion");
				return;
			}
			
			// Create sync payload
			DrugOrderSyncDTO syncDTO = new DrugOrderSyncDTO(drugOrderDTOs);
			
			// Send to endpoint
			boolean success = sendToEndpoint(syncDTO, endpoint);
			
			if (success) {
				// Update last processed date to the latest order's date
				Date latestDate = drugOrders.get(drugOrders.size() - 1).getDateActivated();
				updateLastProcessedDate(latestDate);
				log.info("Successfully synced " + drugOrderDTOs.size() + " drug orders");
			} else {
				log.error("Failed to sync drug orders to endpoint");
			}
			
		}
		catch (Exception e) {
			log.error("Error during drug order sync", e);
			throw new APIException("Error syncing drug orders", e);
		}
	}
	
	private Date getLastProcessedDate() {
		String dateStr = administrationService
		        .getGlobalProperty(EthioEmrCustomModuleConstants.GP_LAST_PROCESSED_DRUG_ORDER_DATE);
		if (dateStr == null || dateStr.isEmpty()) {
			// Default to 24 hours ago if not set
			Date defaultDate = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
			log.info("No last processed date found, using default: " + defaultDate);
			return defaultDate;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.parse(dateStr);
		}
		catch (Exception e) {
			log.warn("Error parsing last processed date: " + dateStr + ", using default", e);
			return new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
		}
	}
	
	private void updateLastProcessedDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(date);
		administrationService.setGlobalProperty(EthioEmrCustomModuleConstants.GP_LAST_PROCESSED_DRUG_ORDER_DATE, dateStr);
		log.info("Updated last processed date to: " + dateStr);
	}
	
	private int getBatchSize() {
		String batchSizeStr = administrationService
		        .getGlobalProperty(EthioEmrCustomModuleConstants.GP_DRUG_ORDER_SYNC_BATCH_SIZE);
		if (batchSizeStr == null || batchSizeStr.isEmpty()) {
			return EthioEmrCustomModuleConstants.DEFAULT_BATCH_SIZE;
		}
		try {
			return Integer.parseInt(batchSizeStr);
		}
		catch (Exception e) {
			log.warn("Error parsing batch size: " + batchSizeStr + ", using default", e);
			return EthioEmrCustomModuleConstants.DEFAULT_BATCH_SIZE;
		}
	}
	
	private String getEndpoint() {
		String endpoint = administrationService.getGlobalProperty(EthioEmrCustomModuleConstants.GP_DRUG_ORDER_SYNC_ENDPOINT);
		if (endpoint == null || endpoint.isEmpty()) {
			return EthioEmrCustomModuleConstants.DEFAULT_ENDPOINT;
		}
		return endpoint;
	}
	
	private DrugOrderDTO convertToDTO(DrugOrder drugOrder) {
		try {
			DrugOrderDTO dto = new DrugOrderDTO();
			
			// Drug
			if (drugOrder.getDrug() != null) {
				DrugDTO drugDTO = new DrugDTO();
				drugDTO.setName(drugOrder.getDrug().getName());
				drugDTO.setUuid(drugOrder.getDrug().getUuid());
				drugDTO.setDagu_id(drugOrder.getDrug().getId());
				drugDTO.setDrugReferenceMaps(null);
				dto.setDrug(drugDTO);
			}
			
			// Type
			dto.setType("drugorder");
			
			// Route
			if (drugOrder.getRoute() != null) {
				RouteDTO routeDTO = new RouteDTO();
				routeDTO.setUuid(drugOrder.getRoute().getUuid());
				routeDTO.setDisplay(drugOrder.getRoute().getDisplayString());
				dto.setRoute(routeDTO);
			}
			
			// Orderer
			if (drugOrder.getOrderer() != null) {
				Provider orderer = drugOrder.getOrderer();
				OrdererDTO ordererDTO = new OrdererDTO();
				if (orderer.getPerson() != null) {
					PersonDisplayDTO personDisplay = new PersonDisplayDTO();
					personDisplay.setDisplay(orderer.getPerson().getPersonName().getFullName());
					ordererDTO.setPerson(personDisplay);
				}
				ordererDTO.setDagu_id(orderer.getId());
				dto.setOrderer(ordererDTO);
			}
			
			// Patient
			if (drugOrder.getPatient() != null) {
				Patient patient = drugOrder.getPatient();
				PatientDTO patientDTO = convertPatientToDTO(patient);
				dto.setPatient(patientDTO);
			}
			
			// Quantity
			dto.setQuantity(drugOrder.getQuantity());
			
			// Frequency
			if (drugOrder.getFrequency() != null) {
				FrequencyDTO frequencyDTO = new FrequencyDTO();
				frequencyDTO.setName(drugOrder.getFrequency().getName());
				dto.setFrequency(frequencyDTO);
			}
			
			return dto;
		}
		catch (Exception e) {
			log.error("Error converting drug order to DTO: " + drugOrder.getUuid(), e);
			return null;
		}
	}
	
	private PatientDTO convertPatientToDTO(Patient patient) {
		PatientDTO patientDTO = new PatientDTO();
		patientDTO.setUuid(patient.getUuid());
		patientDTO.setVoided(patient.getVoided());
		patientDTO.setDagu_id(patient.getId());
		patientDTO.setDisplay(patient.getPersonName().getFullName());
		
		// OpenMRS ID
		PatientIdentifier openmrsId = patient.getPatientIdentifier();
		if (openmrsId != null) {
			patientDTO.setOpenmrsID(openmrsId.getIdentifier());
		}
		
		// Identifiers
		List<IdentifierDTO> identifiers = new ArrayList<IdentifierDTO>();
		for (PatientIdentifier identifier : patient.getIdentifiers()) {
			if (!identifier.getVoided()) {
				IdentifierDTO identifierDTO = new IdentifierDTO();
				identifierDTO.setUuid(identifier.getUuid());
				identifierDTO.setDisplay(identifier.getIdentifierType().getName() + " = " + identifier.getIdentifier());
				identifiers.add(identifierDTO);
			}
		}
		patientDTO.setIdentifiers(identifiers);
		patientDTO.setResourceVersion("1.8");
		
		// Person
		Person person = patient.getPerson();
		if (person != null) {
			PersonDTO personDTO = convertPersonToDTO(person);
			patientDTO.setPerson(personDTO);
		}
		
		return patientDTO;
	}
	
	private PersonDTO convertPersonToDTO(Person person) {
		PersonDTO personDTO = new PersonDTO();
		personDTO.setAge(person.getAge());
		personDTO.setDead(person.getDead());
		personDTO.setUuid(person.getUuid());
		personDTO.setGender(person.getGender());
		personDTO.setVoided(person.getVoided());
		personDTO.setDisplay(person.getPersonName().getFullName());
		personDTO.setBirthdate(person.getBirthdate());
		personDTO.setBirthtime(person.getBirthtime());
		personDTO.setDeathDate(person.getDeathDate());
		personDTO.setAttributes(new ArrayList<Object>());
		personDTO.setCauseOfDeath(null);
		personDTO.setResourceVersion("1.11");
		personDTO.setPreferredAddress(null);
		personDTO.setBirthdateEstimated(person.getBirthdateEstimated());
		personDTO.setDeathdateEstimated(person.getDeathdateEstimated());
		
		// Preferred Name
		PersonName preferredName = person.getPersonName();
		if (preferredName != null) {
			PreferredNameDTO preferredNameDTO = new PreferredNameDTO();
			preferredNameDTO.setUuid(preferredName.getUuid());
			preferredNameDTO.setDisplay(preferredName.getFullName());
			personDTO.setPreferredName(preferredNameDTO);
		}
		
		return personDTO;
	}
	
	private boolean sendToEndpoint(DrugOrderSyncDTO syncDTO, String endpoint) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(syncDTO);
			log.info("Sending drug orders to endpoint: " + endpoint);
			log.debug("Payload: " + jsonData);
			
			URL url = new URL(endpoint);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			
			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = jsonData.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			
			int responseCode = connection.getResponseCode();
			log.info("Response Code: " + responseCode);
			
			connection.disconnect();
			
			return responseCode >= 200 && responseCode < 300;
		} catch (Exception e) {
			log.error("Error sending drug orders to endpoint", e);
			return false;
		}
	}
}
