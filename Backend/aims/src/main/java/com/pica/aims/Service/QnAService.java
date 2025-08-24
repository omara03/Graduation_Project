//package com.pica.aims.Service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.Map;
//
//@Service
//public class QnAService {
//
//    // Access to APIKey and URL [Gemini]
//    @Value("${gemini.api.url}")
//    private String geminiApiUrl;
//    @Value("${gemini.api.key}")
//    private String geminiApiKey;
//
//    private final WebClient webClient;
//
//    public QnAService(WebClient.Builder webClient) {
//        this.webClient = webClient.build();
//    }
//
//    public String getAnswer(String question) {
//        // Construct the request payload
//        // this is a map of string to object
//        Map<String, Object> requestBody = Map.of(
//                "contents", new Object[]{
//                        Map.of(
//                                "parts", new Object[]{
//                                        Map.of("text", question)
//                                })
//                }
//        );
////{
////  "contents": [{
////    "parts":[{"text": "Explain how AI works"}]
////    }]
////   }
//        // Make API call
//        // request and response in the same function here
//        String response = webClient.post()
//                .uri(geminiApiUrl + geminiApiKey)
//                .bodyValue(requestBody)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        // Return response
//        return extractResponseContent(response);
//    }
//
//    private String extractResponseContent(String response) {
//        try{
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode rootNode = mapper.readTree(response);
//            return rootNode.path("candidates")
//                    .get(0)
//                    .path("content")
//                    .path("parts")
//                    .get(0)
//                    .path("text")
//                    .asText();
//        } catch (Exception e) {
//            // Handle parsing error
//            return "Error parsing response: " + e.getMessage();
//        }
//    }
//}
//
//




























//package com.pica.aims.Service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.Map;
//import java.util.concurrent.Semaphore;
//
//@Service
//public class QnAService {
//
//    // Access to APIKey and URL [Gemini]
//    @Value("${gemini.api.url}")
//    private String geminiApiUrl;
//    @Value("${gemini.api.key}")
//    private String geminiApiKey;
//
//    private final WebClient webClient;
//
//    // Allow up to 5 concurrent Gemini requests at a time (you can tune this)
//    private final Semaphore semaphore = new Semaphore(5);
//
//
//    public QnAService(WebClient.Builder webClient) {
//        this.webClient = webClient.build();
//    }
//
//    public String getAnswer(String question) {
//        // Construct the request payload
//        // this is a map of string to object
//        try {
//            semaphore.acquire();
//
//            Map<String, Object> requestBody = Map.of(
//                    "contents", new Object[]{
//                            Map.of(
//                                    "parts", new Object[]{
//                                            Map.of("text", question)
//                                    })
//                    }
//            );
////{
////  "contents": [{
////    "parts":[{"text": "Explain how AI works"}]
////    }]
////   }
//            // Make API call
//            // request and response in the same function here
//            String response = webClient.post()
//                    .uri(geminiApiUrl + geminiApiKey)
//                    .bodyValue(requestBody)
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .block();
//
//            // Return response
//            return extractResponseContent(response);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            return "Interrupted while waiting to access Gemini API";
//        } catch (Exception ex) {
//            return "Gemini request failed: " + ex.getMessage();
//        } finally {
//            semaphore.release(); // Always release
//        }
//
//    }
//
//    private String extractResponseContent(String response) {
//        try{
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode rootNode = mapper.readTree(response);
//            return rootNode.path("candidates")
//                    .get(0)
//                    .path("content")
//                    .path("parts")
//                    .get(0)
//                    .path("text")
//                    .asText();
//        } catch (Exception e) {
//            // Handle parsing error
//            return "Error parsing response: " + e.getMessage();
//        }
//    }
//}


































package com.pica.aims.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;
import java.util.concurrent.Semaphore;

@Service
public class QnAService {

    // Access to APIKey and URL [Gemini]
    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;

    // Allow up to 5 concurrent Gemini requests at a time (you can tune this)
    private final Semaphore semaphore = new Semaphore(5);

    public QnAService(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    public String getAnswer(String question) {
        int retries = 3;
        int delay = 1500; // start with 1 second

        for (int attempt = 0; attempt < retries; attempt++) {
            try {
                semaphore.acquire();

                Map<String, Object> requestBody = Map.of(
                        "contents", new Object[]{
                                Map.of(
                                        "parts", new Object[]{
                                                Map.of("text", question)
                                        })
                        }
                );

                String response = webClient.post()
                        .uri(geminiApiUrl + geminiApiKey)
                        .bodyValue(requestBody)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                return extractResponseContent(response);

            } catch (WebClientResponseException.TooManyRequests e) {
                // Retry on 429 Too Many Requests
                System.out.println("Gemini 429 error, retrying... (" + (attempt + 1) + "/" + retries + ")");
                try {
                    Thread.sleep(delay);
                    delay *= 3; // exponential backoff
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return "Interrupted while backing off after 429";
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Interrupted while waiting to access Gemini API";

            } catch (Exception ex) {
                return "Gemini request failed: " + ex.getMessage();

            } finally {
                semaphore.release(); // Always release
            }
        }

        return "Gemini API failed after multiple retries";
    }

    private String extractResponseContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            // Handle parsing error
            return "Error parsing response: " + e.getMessage();
        }
    }
}
















//
//
//package com.pica.aims.Service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.reactive.function.client.WebClientResponseException;
//
//import java.util.Map;
//import java.util.concurrent.Semaphore;
//
//@Service
//public class QnAService {
//
//    @Value("${gemini.api.url}")
//    private String geminiApiUrl;
//
//    @Value("${gemini.api.key}")
//    private String geminiApiKey;
//
//    private final WebClient webClient;
//
//    private final Semaphore semaphore = new Semaphore(5);
//
//    // Global lock object to pause all threads when a retry is happening
//    private static final Object RETRY_LOCK = new Object();
//
//    public QnAService(WebClient.Builder webClient) {
//        this.webClient = webClient.build();
//    }
//
//    public String getAnswer(String question) {
//        int retries = 3;
//        int delay = 1500; // 1.5 seconds
//
//        for (int attempt = 0; attempt < retries; attempt++) {
//            try {
//                semaphore.acquire();
//
//                Map<String, Object> requestBody = Map.of(
//                        "contents", new Object[]{
//                                Map.of(
//                                        "parts", new Object[]{
//                                                Map.of("text", question)
//                                        })
//                        }
//                );
//
//                String response = webClient.post()
//                        .uri(geminiApiUrl + geminiApiKey)
//                        .bodyValue(requestBody)
//                        .retrieve()
//                        .bodyToMono(String.class)
//                        .block();
//
//                return extractResponseContent(response);
//
//            } catch (WebClientResponseException.TooManyRequests e) {
//                System.out.println("Gemini 429 error, global delay... (" + (attempt + 1) + "/" + retries + ")");
//
//                // Lock all other requests until the delay + retry is done
//                synchronized (RETRY_LOCK) {
//                    try {
//                        Thread.sleep(delay);
//                        delay *= 3; // more patient backoff
//                    } catch (InterruptedException ie) {
//                        Thread.currentThread().interrupt();
//                        return "Interrupted while backing off after 429";
//                    }
//                }
//
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                return "Interrupted while waiting to access Gemini API";
//
//            } catch (Exception ex) {
//                return "Gemini request failed: " + ex.getMessage();
//
//            } finally {
//                semaphore.release();
//            }
//        }
//
//        return "[This section could not be generated due to Gemini rate limits. Try again later.]";
//    }
//
//    private String extractResponseContent(String response) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode rootNode = mapper.readTree(response);
//            return rootNode.path("candidates")
//                    .get(0)
//                    .path("content")
//                    .path("parts")
//                    .get(0)
//                    .path("text")
//                    .asText();
//        } catch (Exception e) {
//            return "Error parsing response: " + e.getMessage();
//        }
//    }
//}