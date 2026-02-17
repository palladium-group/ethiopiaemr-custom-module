package org.openmrs.module.ethiopiaemrcustommodule.api.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.ethiopiaemrcustommodule.EthiopiaEmrCustomModuleConstants;
import org.openmrs.module.ethiopiaemrcustommodule.api.HttpClientService;
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
import java.util.Map;

public class HttpClientServiceImpl extends BaseOpenmrsService implements HttpClientService {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private AdministrationService administrationService;
	
	private RestTemplate restTemplate;
	
	public void setAdministrationService(AdministrationService administrationService) {
		this.administrationService = administrationService;
	}
	
	public void initialize() {
		this.restTemplate = new RestTemplate(createRequestFactory());
		log.info("HttpClientService initialized with Pooled Connection Manager");
	}
	
	@Override
	public <T> ResponseEntity<T> post(String url, Object request, Class<T> responseType) {
		return post(url, request, responseType, null);
	}
	
	@Override
	public <T> ResponseEntity<T> post(String url, Object request, Class<T> responseType, Map<String, String> extraHeaders) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

			if (extraHeaders != null) {
				extraHeaders.forEach(headers::add);
			}

			HttpEntity<?> entity = new HttpEntity<>(request, headers);
			return restTemplate.postForEntity(url, entity, responseType);
		}
		catch (HttpStatusCodeException e) {
			log.error("External Server error: " + e.getRawStatusCode() + " URL: " + url + " Body: " + e.getResponseBodyAsString());
			throw new APIException("HTTP Post failed with status: " + e.getRawStatusCode(), e);
		}
		catch (Exception e) {
			log.error("Communication failure with URL: " + url, e);
			throw new APIException("Internal error during HTTP Post", e);
		}
	}
	
	@Override
	public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
		return get(url, responseType, null);
	}
	
	@Override
	public <T> ResponseEntity<T> get(String url, Class<T> responseType, Map<String, String> extraHeaders) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

			if (extraHeaders != null) {
				extraHeaders.forEach(headers::add);
			}

			HttpEntity<?> entity = new HttpEntity<>(headers);
			return restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
		}
		catch (HttpStatusCodeException e) {
			log.error("External Server GET error: " + e.getRawStatusCode() + " URL: " + url);
			throw new APIException("HTTP Get failed with status: " + e.getRawStatusCode(), e);
		}
		catch (Exception e) {
			log.error("Communication failure with URL: " + url, e);
			throw new APIException("Internal error during HTTP Get", e);
		}
	}
	
	private ClientHttpRequestFactory createRequestFactory() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(EthiopiaEmrCustomModuleConstants.HTTP_MAX_TOTAL_CONNECTIONS);
		connectionManager.setDefaultMaxPerRoute(EthiopiaEmrCustomModuleConstants.HTTP_MAX_CONNECTIONS_PER_ROUTE);
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connectionManager).build();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
		
		factory.setConnectTimeout(EthiopiaEmrCustomModuleConstants.HTTP_CONNECT_TIMEOUT);
		factory.setReadTimeout(EthiopiaEmrCustomModuleConstants.HTTP_SOCKET_TIMEOUT);
		factory.setConnectionRequestTimeout(EthiopiaEmrCustomModuleConstants.HTTP_CONNECTION_REQUEST_TIMEOUT);
		
		return factory;
	}
	
	@Override
	public String getOpenfnApiKey() {
		// Check OS Environment Variable (Best for Docker/Production)
		String apiKey = System.getenv("OPENFN_API_KEY");
		
		// Fallback to OpenMRS Runtime Properties (Best for SDK/Development)
		if (apiKey == null || apiKey.isEmpty()) {
			apiKey = org.openmrs.api.context.Context.getRuntimeProperties().getProperty(
			    "ethiopiaemrcustommodule.openfnApiKey");
		}
		
		if (apiKey == null || apiKey.isEmpty()) {
			log.warn("OpenFn API Key not found in Environment Variables or Runtime Properties!");
		}
		
		return apiKey;
	}
}
