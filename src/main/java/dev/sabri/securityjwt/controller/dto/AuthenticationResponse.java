package dev.sabri.securityjwt.controller.dto;

public record AuthenticationResponse(String token) {
    public String getJwt_token() {
        return token;
    }

}
