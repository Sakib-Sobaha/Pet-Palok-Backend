package dev.sabri.securityjwt.controller.dto;

public record VerifyUser(String email, String verificationCode) {

}
