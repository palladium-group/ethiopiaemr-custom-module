package org.openmrs.module.ethiopiaemrcustommodule.api;

import org.openmrs.api.OpenmrsService;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public interface HttpClientService {
	
	// Standard POST
	<T> ResponseEntity<T> post(String url, Object request, Class<T> responseType);
	
	// POST with custom headers (e.g., API Keys, Auth tokens)
	<T> ResponseEntity<T> post(String url, Object request, Class<T> responseType, Map<String, String> extraHeaders);
	
	// Standard GET
	<T> ResponseEntity<T> get(String url, Class<T> responseType);
	
	// GET with custom headers
	<T> ResponseEntity<T> get(String url, Class<T> responseType, Map<String, String> extraHeaders);
}
