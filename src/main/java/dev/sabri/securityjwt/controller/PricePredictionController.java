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
@RequestMapping("/api/v1/price-predictor")
public class PricePredictionController {

    @PostMapping("/predict")
    public ResponseEntity<String> predictPrice(@RequestBody Map<String, Object> itemData) {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare the data for the model as expected
        Map<String, Object> modelInput = new HashMap<>();
        modelInput.put("name", itemData.get("name").toString());
        modelInput.put("description", itemData.get("description").toString());
        modelInput.put("quantity", Integer.parseInt(itemData.get("quantity").toString()));
        modelInput.put("type", itemData.get("type").toString());
        modelInput.put("petType", itemData.get("petType").toString());

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build the request entity with headers and prepared data
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(modelInput, headers);

        // Make the POST request to Python API
        String pythonApiUrl = "http://localhost:5000/predict";  // URL of Flask API
        String response = restTemplate.postForObject(pythonApiUrl, request, String.class);

        return ResponseEntity.ok(response);
    }
}

