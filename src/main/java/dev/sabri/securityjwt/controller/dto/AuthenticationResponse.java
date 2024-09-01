package dev.sabri.securityjwt.controller.dto;

public record AuthenticationResponse(String token, long expiresIn) {
    public String getJwt_token() {
        return token;
    }

}
