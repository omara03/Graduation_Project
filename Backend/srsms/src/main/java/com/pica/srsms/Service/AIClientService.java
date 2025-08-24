package com.pica.srsms.Service;


import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIClientService {
    @Autowired
    private RestTemplate restTemplate;
    private final String aiServiceBaseUrl = "http://AIMS/api/Ai/generate";
    @Autowired
    private RestTemplate externalRestTemplate; // Non-load-balanced for external services

//    @Autowired
//    private final String aimsUrl;  // Injected from ExternalServicesConfig
//    @Autowired
//    private final String krokiUrl; // Injected from ExternalServicesConfig

    public String generateContent(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> request = new HashMap<>();
        request.put("prompt", prompt);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                aiServiceBaseUrl, entity, Map.class);

        return (String) response.getBody().get("content");
    }

//    public String generateContent(String prompt) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        Map<String, String> request = new HashMap<>();
//        request.put("prompt", prompt);
//
//        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
//
//        try {
//            ResponseEntity<Map> response = restTemplate.postForEntity(
//                    aimsUrl , entity, Map.class);
//
//            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//                return (String) response.getBody().get("content");
//            } else {
//                throw new RuntimeException("AIMS service returned an invalid response");
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to generate content: " + e.getMessage());
//        }
//    }


    public String generateCodeClassDiagram() {
        try {
            System.out.println("Generating code class diagram using AIMS service at: " + aiServiceBaseUrl);
            ResponseEntity<String> response = restTemplate.getForEntity(
                    aiServiceBaseUrl + "/generateCodeClassDiagram",
                    String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate class diagram: " + e.getMessage());
        }
    }


    public String generateCodeStateDiagram() {
        try {
            System.out.println("Generating code class diagram using AIMS service at: " + aiServiceBaseUrl);

            ResponseEntity<String> response = restTemplate.getForEntity(
                    aiServiceBaseUrl + "/generateCodeStateDiagram",
                    String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate State diagram: " + e.getMessage());
        }
    }

    public String generateCodeUseCaseDiagram() {
        try {
            System.out.println("Generating code class diagram using AIMS service at: " + aiServiceBaseUrl);

            ResponseEntity<String> response = restTemplate.getForEntity(
                    aiServiceBaseUrl + "/generateCodeUseCaseDiagram",
                    String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate UseCase diagram: " + e.getMessage());
        }
    }

    public String generateCodeSequenceDiagram() {
        try {
            System.out.println("Generating code class diagram using AIMS service at: " + aiServiceBaseUrl);

            ResponseEntity<String> response = restTemplate.getForEntity(
                    aiServiceBaseUrl + "/generateCodeSequenceDiagram",
                    String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Sequence diagram: " + e.getMessage());
        }
    }


//    @Retry(name = "krokiServiceRetry", fallbackMethod = "fallbackGenerateTraditionalDiagram")
    public byte[] generateTraditionalDiagram(String codeDiagram) throws IOException {
        String url = "https://kroki.io/plantuml/png";
        // Create JSON payload for Kroki

        Map<String, String> payload = new HashMap<>();
        payload.put("diagram_source", codeDiagram);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

        System.out.println("Sending request to Kroki service at: " + url);
        ResponseEntity<byte[]> response = externalRestTemplate.postForEntity(url, request, byte[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody(); // PNG image bytes
        } else {
            throw new IOException("Failed to generate diagram: " + response.getStatusCode());
        }
    }
//    public byte[] fallbackGenerateTraditionalDiagram(String codeClassDiagram, Throwable throwable) {
//        System.err.println("Fallback triggered due to: " + throwable.getMessage());
//        return new byte[0]; // Return an empty byte array or a default response
//    }
}