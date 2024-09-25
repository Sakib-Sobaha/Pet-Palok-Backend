package dev.sabri.securityjwt.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/openai")
public class AIController {

    @Value("${GROQ_API_KEY}")
    private String groqApiKey;
//    private static final String groqApiKey = "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";

    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";

    @PostMapping
    public ResponseEntity<String> sendChatRequest(@RequestBody Map<String, Object> payload) {

        RestTemplate restTemplate = new RestTemplate();

        List<Map<String, Object>> messages = (List<Map<String, Object>>) payload.get("messages");

        // Set the headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        // Create the request body
        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama3-8b-8192");
        body.put("messages", messages);

        // Build the request entity
        org.springframework.http.HttpEntity<Map<String, Object>> entity =
                new org.springframework.http.HttpEntity<>(body, headers);

        // Make the POST request to the Groq API
        String response = restTemplate.postForObject(GROQ_API_URL, entity, String.class);

        return ResponseEntity.ok(response);
    }
}