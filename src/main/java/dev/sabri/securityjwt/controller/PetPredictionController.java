package dev.sabri.securityjwt.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pet-predictor")
public class PetPredictionController {

    @PostMapping("/predict")
    public ResponseEntity<String> predictPet(@RequestBody Map<String, String> requestBody) {
        RestTemplate restTemplate = new RestTemplate();

        // Extract image link from the request
        String imageUrl = requestBody.get("imageUrl");

        // Prepare the data to send to the Python API
        Map<String, String> modelInput = new HashMap<>();
        modelInput.put("imageUrl", imageUrl);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build the request entity with headers and image data
        HttpEntity<Map<String, String>> request = new HttpEntity<>(modelInput, headers);

        // Make the POST request to Python API
        String pythonApiUrl = "http://localhost:5000/predict-pet";  // Flask API URL
        String response = restTemplate.postForObject(pythonApiUrl, request, String.class);

        // Return the Flask API response
        return ResponseEntity.ok(response);
    }
}
