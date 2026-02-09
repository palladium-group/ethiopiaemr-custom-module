package org.openmrs.module.ethioemrcustommodule.api.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.ethioemrcustommodule.EthioEmrCustomModuleConstants;
import org.openmrs.module.ethioemrcustommodule.api.HttpClientService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class HttpClientServiceImpl extends BaseOpenmrsService implements HttpClientService {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private AdministrationService administrationService;
	
	private RestTemplate restTemplate;
	
	public void setAdministrationService(AdministrationService administrationService) {
		this.administrationService = administrationService;
	}
	
	/**
	 * Called via init-method in moduleApplicationContext.xml
	 */
	public void initialize() {
		this.restTemplate = new RestTemplate(createRequestFactory());
		log.info("HttpClientService initialized with Pooled Connection Manager");
	}
	
	@Override
	public <T> ResponseEntity<T> post(String url, Object request, Class<T> responseType) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

			HttpEntity<?> entity = new HttpEntity<>(request, headers);
			return restTemplate.postForEntity(url, entity, responseType);
		}
		catch (HttpStatusCodeException e) {
			log.error("MPI Server returned error: " + e.getRawStatusCode() + " Body: " + e.getResponseBodyAsString());
			throw new APIException("MPI service error: " + e.getStatusText(), e);
		}
		catch (Exception e) {
			log.error("Communication failure with URL: " + url, e);
			throw new APIException("ethioemrcustommodule.error.httpFailure", e);
		}
	}
	
	@Override
	public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			HttpEntity<?> entity = new HttpEntity<>(headers);
			return restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
		}
		catch (HttpStatusCodeException e) {
			log.error("MPI GET error: " + e.getRawStatusCode() + " Body: " + e.getResponseBodyAsString());
			throw new APIException("MPI service error", e);
		}
		catch (Exception e) {
			throw new APIException("HTTP GET request failed", e);
		}
	}
	
	private ClientHttpRequestFactory createRequestFactory() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		
		connectionManager.setMaxTotal(getGlobalInt(EthioEmrCustomModuleConstants.GP_HTTP_MAX_TOTAL_CONNECTIONS, 100));
		connectionManager.setDefaultMaxPerRoute(getGlobalInt(
		    EthioEmrCustomModuleConstants.GP_HTTP_MAX_CONNECTIONS_PER_ROUTE, 20));
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connectionManager).build();
		
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
		
		factory.setConnectTimeout(getGlobalInt(EthioEmrCustomModuleConstants.GP_HTTP_CONNECT_TIMEOUT, 5000));
		factory.setReadTimeout(getGlobalInt(EthioEmrCustomModuleConstants.GP_HTTP_SOCKET_TIMEOUT, 30000));
		factory.setConnectionRequestTimeout(getGlobalInt(EthioEmrCustomModuleConstants.GP_HTTP_CONNECTION_REQUEST_TIMEOUT,
		    5000));
		
		return factory;
	}
	
	private int getGlobalInt(String property, int defaultValue) {
		String val = administrationService.getGlobalProperty(property);
		try {
			return (val != null && !val.isEmpty()) ? Integer.parseInt(val.trim()) : defaultValue;
		}
		catch (NumberFormatException e) {
			log.warn("Invalid value for " + property + ": " + val + ". Using default: " + defaultValue);
			return defaultValue;
		}
	}
}
