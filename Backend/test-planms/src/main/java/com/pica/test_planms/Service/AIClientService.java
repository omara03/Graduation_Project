package com.pica.test_planms.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIClientService {
    @Autowired
    private final RestTemplate restTemplate;
    private final String aiServiceBaseUrl = "http://AIMS/api/Ai/generate";
    public String generateContent(String prompt){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> request = new HashMap<>();
        request.put("prompt", prompt);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                aiServiceBaseUrl, entity, Map.class);

        return (String) response.getBody().get("content");
    }
}
