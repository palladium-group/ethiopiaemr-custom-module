package org.openmrs.module.ethioemrcustommodule;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.MapMessage;
import javax.jms.Message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.DrugOrder;
import org.openmrs.Order;
import org.openmrs.api.APIException;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.Daemon;
import org.openmrs.event.EventListener;
import org.openmrs.module.DaemonToken;

/**
 * Listens for drug order created and updated events, logs the drug information to the terminal
 */
public class DrugPrescriptionCreatedListener implements EventListener {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private DaemonToken daemonToken;
	
	public DrugPrescriptionCreatedListener(DaemonToken token) {
		daemonToken = token;
	}
	
	/**
	 * @see EventListener#onMessage(javax.jms.Message)
	 * @param message
	 */
	@Override
	public void onMessage(final Message message) {
		Daemon.runInDaemonThread(new Runnable() {
			
			@Override
			public void run() {
				try {
					processMessage(message);
				}
				catch (Exception e) {
					log.warn("Failed to process drug order event", e);
				}
			}
		}, daemonToken);
	}
	
	/**
	 * Processes the specified jms message
	 * 
	 * @should log drug order information when drug order is created
	 * @should log drug order information when drug order is updated
	 */
	public void processMessage(Message message) throws Exception {
		MapMessage mapMessage = (MapMessage) message;
		
		String orderUuid = mapMessage.getString("uuid");
		
		if (orderUuid == null) {
			log.warn("Received drug order event with null uuid");
			return;
		}
		
		OrderService orderService = Context.getOrderService();
		Order order = orderService.getOrderByUuid(orderUuid);
		
		if (order == null) {
			throw new APIException("failed to find an order with uuid:" + orderUuid);
		}
		
		if (!(order instanceof DrugOrder)) {
			log.warn("Received order event for non-drug order: " + orderUuid);
			return;
		}
		
		DrugOrder drugOrder = (DrugOrder) order;
		
		log.warn("*************************************************");
		log.warn("EthioEMR Custom Module! Drug Order Event caught!");
		log.warn("Drug Order ID: " + drugOrder.getOrderId());
		log.warn("Drug Order UUID: " + drugOrder.getUuid());
		if (drugOrder.getDrug() != null) {
			log.warn("Drug: " + drugOrder.getDrug().getName());
		}
		if (drugOrder.getPatient() != null) {
			log.warn("Patient: " + drugOrder.getPatient().getPatientId() + " - " + drugOrder.getPatient().getPersonName());
		}
		if (drugOrder.getOrderer() != null) {
			log.warn("Orderer: " + drugOrder.getOrderer().getName());
		}
		log.warn("Dose: " + drugOrder.getDose());
		log.warn("Dose Units: " + (drugOrder.getDoseUnits() != null ? drugOrder.getDoseUnits().getDisplayString() : "N/A"));
		log.warn("Frequency: " + (drugOrder.getFrequency() != null ? drugOrder.getFrequency().getName() : "N/A"));
		log.warn("Route: " + (drugOrder.getRoute() != null ? drugOrder.getRoute().getDisplayString() : "N/A"));
		log.warn("Quantity: " + drugOrder.getQuantity());
		log.warn("Quantity Units: "
		        + (drugOrder.getQuantityUnits() != null ? drugOrder.getQuantityUnits().getDisplayString() : "N/A"));
		log.warn("Duration: " + drugOrder.getDuration());
		log.warn("Duration Units: "
		        + (drugOrder.getDurationUnits() != null ? drugOrder.getDurationUnits().getDisplayString() : "N/A"));
		log.warn("Instructions: " + drugOrder.getInstructions());
		log.warn("*************************************************");
		
		// Send drug order data to external endpoint
		sendDrugOrderToEndpoint(drugOrder);
	}
	
	/**
	 * Sends drug order data to external endpoint
	 * 
	 * @param drugOrder the drug order to send
	 */
	private void sendDrugOrderToEndpoint(DrugOrder drugOrder) {
		try {
			String jsonData = buildDrugOrderJson(drugOrder);
			log.warn("Sending drug order data to endpoint: " + jsonData);
			
			URL url = new URL("http://localhost:3000");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			
			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = jsonData.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			
			int responseCode = connection.getResponseCode();
			log.warn("Response Code: " + responseCode);
			
			if (responseCode >= 200 && responseCode < 300) {
				log.warn("Successfully sent drug order data to endpoint");
			} else {
				log.warn("Failed to send drug order data. Response code: " + responseCode);
			}
			
			connection.disconnect();
		} catch (Exception e) {
			log.error("Error sending drug order data to endpoint", e);
		}
	}
	
	/**
	 * Builds JSON string from DrugOrder using OpenMRS field names
	 * 
	 * @param drugOrder the drug order to convert
	 * @return JSON string
	 */
	private String buildDrugOrderJson(DrugOrder drugOrder) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		// orderer -> providerUuid
		if (drugOrder.getOrderer() != null) {
			data.put("ordererUuid", drugOrder.getOrderer().getUuid());
		} else {
			data.put("ordererUuid", null);
		}
		
		// dateActivated
		if (drugOrder.getDateActivated() != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			data.put("dateActivated", dateFormat.format(drugOrder.getDateActivated()));
		} else {
			data.put("dateActivated", null);
		}
		
		// orderNumber
		if (drugOrder.getOrderNumber() != null) {
			data.put("orderNumber", drugOrder.getOrderNumber());
		} else if (drugOrder.getOrderId() != null) {
			data.put("orderNumber", drugOrder.getOrderId().toString());
		} else {
			data.put("orderNumber", null);
		}
		
		// diagnoses -> empty array (not directly available in DrugOrder)
		data.put("diagnoses", new ArrayList<Object>());
		
		// compoundCost -> null (not available in DrugOrder)
		data.put("compoundCost", null);
		
		// orderDetail -> drug order details using OpenMRS field names
		Map<String, Object> orderDetail = new HashMap<String, Object>();
		if (drugOrder.getDrug() != null) {
			orderDetail.put("drugUuid", drugOrder.getDrug().getUuid());
		} else {
			orderDetail.put("drugUuid", null);
		}
		if (drugOrder.getQuantityUnits() != null) {
			orderDetail.put("quantityUnitsUuid", drugOrder.getQuantityUnits().getUuid());
		} else {
			orderDetail.put("quantityUnitsUuid", null);
		}
		orderDetail.put("quantity", drugOrder.getQuantity());
		if (drugOrder.getRoute() != null) {
			orderDetail.put("routeUuid", drugOrder.getRoute().getUuid());
		} else {
			orderDetail.put("routeUuid", null);
		}
		orderDetail.put("duration", drugOrder.getDuration());
		if (drugOrder.getFrequency() != null) {
			orderDetail.put("frequencyUuid", drugOrder.getFrequency().getUuid());
		} else {
			orderDetail.put("frequencyUuid", null);
		}
		data.put("orderDetail", orderDetail);
		
		// newDiagnosis, newCategory, newAdditionalInfo, newDiagnosisEdit, newAdditionalInfoEdit -> null
		data.put("newDiagnosis", null);
		data.put("newCategory", null);
		data.put("newAdditionalInfo", null);
		data.put("newDiagnosisEdit", null);
		data.put("newAdditionalInfoEdit", null);
		
		// patient -> patientUuid
		if (drugOrder.getPatient() != null) {
			data.put("patientUuid", drugOrder.getPatient().getUuid());
		} else {
			data.put("patientUuid", null);
		}
		
		// orderDetails -> array with drug order details using OpenMRS field names
		List<Map<String, Object>> orderDetails = new ArrayList<Map<String, Object>>();
		Map<String, Object> orderDetailItem = new HashMap<String, Object>();
		if (drugOrder.getQuantityUnits() != null) {
			orderDetailItem.put("quantityUnitsUuid", drugOrder.getQuantityUnits().getUuid());
		} else {
			orderDetailItem.put("quantityUnitsUuid", null);
		}
		orderDetailItem.put("quantity", drugOrder.getQuantity());
		if (drugOrder.getRoute() != null) {
			orderDetailItem.put("routeUuid", drugOrder.getRoute().getUuid());
		} else {
			orderDetailItem.put("routeUuid", null);
		}
		orderDetailItem.put("duration", drugOrder.getDuration());
		if (drugOrder.getFrequency() != null) {
			orderDetailItem.put("frequencyUuid", drugOrder.getFrequency().getUuid());
		} else {
			orderDetailItem.put("frequencyUuid", null);
		}
		orderDetailItem.put("isActive", drugOrder.getAction() != null && drugOrder.getAction() != Order.Action.DISCONTINUE);
		orderDetails.add(orderDetailItem);
		data.put("orderDetails", orderDetails);
		
		return mapToJson(data);
	}
	
	/**
	 * Converts a Map to JSON string
	 * 
	 * @param map the map to convert
	 * @return JSON string
	 */
	private String mapToJson(Map<String, Object> map) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		boolean first = true;
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (!first) {
				json.append(",");
			}
			first = false;
			json.append("\"").append(escapeJson(entry.getKey())).append("\":");
			json.append(valueToJson(entry.getValue()));
		}
		json.append("}");
		return json.toString();
	}
	
	/**
	 * Converts a value to JSON string
	 * 
	 * @param value the value to convert
	 * @return JSON string representation
	 */
	private String valueToJson(Object value) {
		if (value == null) {
			return "null";
		} else if (value instanceof String) {
			return "\"" + escapeJson((String) value) + "\"";
		} else if (value instanceof Number || value instanceof Boolean) {
			return value.toString();
		} else if (value instanceof List) {
			StringBuilder json = new StringBuilder();
			json.append("[");
			boolean first = true;
			for (Object item : (List<?>) value) {
				if (!first) {
					json.append(",");
				}
				first = false;
				json.append(valueToJson(item));
			}
			json.append("]");
			return json.toString();
		} else if (value instanceof Map) {
			return mapToJson((Map<String, Object>) value);
		} else {
			return "\"" + escapeJson(value.toString()) + "\"";
		}
	}
	
	/**
	 * Escapes special characters in JSON strings
	 * 
	 * @param str the string to escape
	 * @return escaped string
	 */
	private String escapeJson(String str) {
		if (str == null) {
			return "";
		}
		return str.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r")
		        .replace("\t", "\\t");
	}
}
