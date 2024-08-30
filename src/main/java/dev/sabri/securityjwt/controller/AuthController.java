package dev.sabri.securityjwt.controller;

import dev.sabri.securityjwt.controller.dto.AuthenticationRequest;
import dev.sabri.securityjwt.controller.dto.AuthenticationResponse;
import dev.sabri.securityjwt.controller.dto.RegisterRequest;
import dev.sabri.securityjwt.scopes.admin.dto.AdminRegisterRequest;
import dev.sabri.securityjwt.scopes.seller.dto.SellerRegisterRequest;
import dev.sabri.securityjwt.scopes.user.dto.UserRegisterRequest;
import dev.sabri.securityjwt.scopes.vets.dto.VetRegisterRequest;
import dev.sabri.securityjwt.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public record AuthController(AuthenticationService authenticationService) {


    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/auth/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request) {
        System.out.println("hello request received");
        System.out.println(request.email() + " " + request.password());
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/admin/register")
    public ResponseEntity<AuthenticationResponse> adminRegister(@RequestBody AdminRegisterRequest request) {
        return ResponseEntity.ok(authenticationService.adminRegister(request));
    }

    @PostMapping("/admin/authenticate")
    public ResponseEntity<AuthenticationResponse> adminAuthenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.adminAuthenticate(request));
    }

    @PostMapping("/user/register")
    public ResponseEntity<AuthenticationResponse> userRegister(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.ok(authenticationService.userRegister(request));
    }

    @PostMapping("/user/authenticate")
    public ResponseEntity<AuthenticationResponse> userAuthenticate(@RequestBody AuthenticationRequest request) {
        System.out.println("authenticate request received");
        System.out.println(request.email() + " " + request.password());
        return ResponseEntity.ok(authenticationService.userAuthenticate(request));
    }

    @PostMapping("/vet/register")
    public ResponseEntity<AuthenticationResponse> vetRegister(@RequestBody VetRegisterRequest request) {
        return ResponseEntity.ok(authenticationService.vetRegister(request));
    }

    @PostMapping("/vet/authenticate")
    public ResponseEntity<AuthenticationResponse> vetAuthenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.vetAuthenticate(request));
    }

    @PostMapping("/seller/register")
    public ResponseEntity<AuthenticationResponse> sellerRegister(@RequestBody SellerRegisterRequest request) {
        return ResponseEntity.ok(authenticationService.sellerRegister(request));
    }

    @PostMapping("/seller/authenticate")
    public ResponseEntity<AuthenticationResponse> sellerAuthenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.sellerAuthenticate(request));
    }

    





}
