package dev.sabri.securityjwt.controller;

import dev.sabri.securityjwt.controller.dto.AuthenticationRequest;
import dev.sabri.securityjwt.controller.dto.AuthenticationResponse;
import dev.sabri.securityjwt.controller.dto.RegisterRequest;
import dev.sabri.securityjwt.controller.dto.VerifyUser;
import dev.sabri.securityjwt.response.AuthResponse;
import dev.sabri.securityjwt.scopes.admin.Admin;
import dev.sabri.securityjwt.scopes.admin.dto.AdminRegisterRequest;
import dev.sabri.securityjwt.scopes.seller.Seller;
import dev.sabri.securityjwt.scopes.seller.dto.SellerRegisterRequest;
import dev.sabri.securityjwt.scopes.user.User;
import dev.sabri.securityjwt.scopes.user.dto.UserRegisterRequest;
import dev.sabri.securityjwt.scopes.vets.Vet;
import dev.sabri.securityjwt.scopes.vets.dto.VetRegisterRequest;
import dev.sabri.securityjwt.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public record AuthController(AuthenticationService authenticationService) {


    @PostMapping("/auth/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/auth/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request) {
        System.out.println("hello request received");
        System.out.println(request.email() + " " + request.password());
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/admin/register")
    public ResponseEntity<Admin> adminRegister(@RequestBody AdminRegisterRequest request) {
        return ResponseEntity.ok(authenticationService.adminRegister(request));
    }

    @PostMapping("/admin/authenticate")
    public ResponseEntity<AuthenticationResponse> adminAuthenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.adminAuthenticate(request));
    }

    @PostMapping("/user/register")
    public ResponseEntity<User> userRegister(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.ok(authenticationService.userRegister(request));
    }

    @PostMapping("/user/authenticate")
    public ResponseEntity<AuthenticationResponse> userAuthenticate(@RequestBody AuthenticationRequest request) {
        System.out.println("authenticate request received");
        System.out.println(request.email() + " " + request.password());
        return ResponseEntity.ok(authenticationService.userAuthenticate(request));
    }

    @PostMapping("/user/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUser verifyUser){
        try {
            authenticationService.verifyUser(verifyUser);
            return ResponseEntity.ok("Account verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/user/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email){
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code resent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/vet/register")
    public ResponseEntity<Vet> vetRegister(@RequestBody VetRegisterRequest request) {
        return ResponseEntity.ok(authenticationService.vetRegister(request));
    }

    @PostMapping("/vet/authenticate")
    public ResponseEntity<AuthenticationResponse> vetAuthenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.vetAuthenticate(request));
    }

    @PostMapping("/seller/register")
    public ResponseEntity<Seller> sellerRegister(@RequestBody SellerRegisterRequest request) {
        return ResponseEntity.ok(authenticationService.sellerRegister(request));
    }

    @PostMapping("/seller/authenticate")
    public ResponseEntity<AuthenticationResponse> sellerAuthenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.sellerAuthenticate(request));
    }

    





}
