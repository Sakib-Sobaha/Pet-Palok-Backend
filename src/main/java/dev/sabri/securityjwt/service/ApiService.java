package dev.sabri.securityjwt.service;

import dev.sabri.securityjwt.controller.dto.RegisterRequest;
import dev.sabri.securityjwt.scopes.user.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import dev.sabri.securityjwt.controller.dto.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class ApiService {

    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080/api/v1/auth";
    private final String baseUrl_ = "http://localhost:8080/api/v1";

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String registerUser(RegisterRequest registerRequest) {
        String registerUrl = baseUrl + "/register";
        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(registerUrl, registerRequest, AuthenticationResponse.class);

        if(response.getStatusCode() == HttpStatus.OK){
            return getJwtToken(registerRequest.email(), registerRequest.password());
        }else{
            throw new RuntimeException("Registration failed");
        }

    }

    public String getJwtToken(String email, String password) {
        String authUrl = baseUrl + "/authenticate";
        AuthenticationRequest authRequest = new AuthenticationRequest(email, password);
        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(authUrl, authRequest, AuthenticationResponse.class);
        return Objects.requireNonNull(response.getBody()).getJwt_token();
    }

    public List<User> getAllUsers(String token) {
        String url = baseUrl_ + "/users";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<User[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, User[].class);
        return Arrays.asList(Objects.requireNonNull(response.getBody()));

    }
}
