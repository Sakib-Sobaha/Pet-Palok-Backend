package dev.sabri.securityjwt.service;

import dev.sabri.securityjwt.controller.dto.AuthenticationRequest;
import dev.sabri.securityjwt.controller.dto.AuthenticationResponse;
import dev.sabri.securityjwt.controller.dto.RegisterRequest;
import dev.sabri.securityjwt.controller.dto.VerifyUser;
import dev.sabri.securityjwt.scopes.admin.Admin;
import dev.sabri.securityjwt.scopes.admin.AdminRepository;
import dev.sabri.securityjwt.scopes.admin.dto.AdminRegisterRequest;
import dev.sabri.securityjwt.scopes.seller.Seller;
import dev.sabri.securityjwt.scopes.seller.SellerRepository;
import dev.sabri.securityjwt.scopes.seller.dto.SellerRegisterRequest;
import dev.sabri.securityjwt.scopes.user.*;
import dev.sabri.securityjwt.scopes.user.dto.UserRegisterRequest;
import dev.sabri.securityjwt.scopes.vets.Vet;
import dev.sabri.securityjwt.scopes.vets.VetRepository;
import dev.sabri.securityjwt.scopes.vets.dto.VetRegisterRequest;
import dev.sabri.securityjwt.utils.JwtService;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public record AuthenticationService(UserRepository userRepository,
                                    AdminRepository adminRepository,
                                    SellerRepository sellerRepository,
                                    VetRepository vetRepository,
                                    PasswordEncoder passwordEncoder,
                                    AuthenticationManager authenticationManager,
                                    EmailValidator emailValidator,
                                    EmailService emailService,
                                    JwtService jwtService
                                    ) {
    public User register(RegisterRequest request) {
        final var user = new User(null,
                request.firstname(),
                request.lastname(),
                request.email(),
                passwordEncoder.encode(request.password()),
                Role.USER);
        userRepository.save(user);
        final var token = JwtService.generateToken(user);
        return user;
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        final var user = userRepository.findByEmail(request.email()).orElseThrow();
        final var token = JwtService.generateToken(user);
        return new AuthenticationResponse(token, jwtService.getJwtExpiration());

    }



    public Admin adminRegister(AdminRegisterRequest request) {
        boolean adminExists = adminRepository.findByEmail(request.email()).isPresent();
        if (adminExists) {
            throw new IllegalStateException("email already taken");
        }

        boolean isValidEmail = emailValidator().test(request.email());
        if (!isValidEmail) {
            throw new IllegalStateException("invalid email");
        }


        final var admin = new Admin(null,
                request.email(),
                passwordEncoder.encode(request.password()),
                Role.ADMIN);
        adminRepository.save(admin);
        final var token = JwtService.generateToken(admin);


        return admin;
    }

    public AuthenticationResponse adminAuthenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        final var admin = adminRepository.findByEmail(request.email()).orElseThrow();
        final var token = JwtService.generateToken(admin);
        admin.setStatus("online");
        adminRepository.save(admin);
        return new AuthenticationResponse(token, jwtService().getJwtExpiration());

    }



    public User userRegister(UserRegisterRequest request) {
        boolean userExists = userRepository.findByEmail(request.email()).isPresent();
        if (userExists) {
            throw new IllegalStateException("email already taken");
        }

        boolean isValidEmail = emailValidator().test(request.email());
        if (!isValidEmail) {
            throw new IllegalStateException("invalid email");
        }

        final var user = new User(null,
                request.email(),
                passwordEncoder.encode(request.password()),
                Role.USER);

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(30));
        user.setEnabled(false);
        sendVerificationEmail(user);

        userRepository.save(user);
        final var token = JwtService.generateToken(user);


        return user;
    }



    public AuthenticationResponse userAuthenticate(AuthenticationRequest request) {


        final var user = userRepository.findByEmail(request.email()).orElseThrow();
        if(!user.isEnabled()){
            throw new RuntimeException("Account not verified. Please verify your account");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        final var token = JwtService.generateToken(user);
        user.setStatus("online");
        userRepository.save(user);
        return new AuthenticationResponse(token,jwtService.getJwtExpiration());

    }

    public void verifyUser(VerifyUser input){
        Optional<User> optionalUser = userRepository.findByEmail(input.email());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())){
                throw new RuntimeException("Verification code has expired");
            }
            if(user.getVerificationCode().equals(input.verificationCode())){
                user.setStatus("online");
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    private void sendVerificationEmail(User user) { //TODO: Update with company logo
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }



    public Vet vetRegister(VetRegisterRequest request) {
        final var vet = new Vet(null,
                request.email(),
                passwordEncoder.encode(request.password()),
                Role.VET);
        vetRepository.save(vet);
        final var token = JwtService.generateToken(vet);
        return vet;
    }

    public AuthenticationResponse vetAuthenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        final var vet = vetRepository.findByEmail(request.email()).orElseThrow();
        final var token = JwtService.generateToken(vet);
        vet.setStatus("online");
        vetRepository.save(vet);
        return new AuthenticationResponse(token, jwtService.getJwtExpiration());

    }

    public Seller sellerRegister(SellerRegisterRequest request) {
        boolean sellerExists = sellerRepository.findByEmail(request.email()).isPresent();
        if (sellerExists) {
            throw new IllegalStateException("email already taken");
        }


        final var seller = new Seller(null,
                request.email(),
                passwordEncoder.encode(request.password()),
                Role.SELLER);
        sellerRepository.save(seller);
        final var token = JwtService.generateToken(seller);
        return seller;
    }

    public AuthenticationResponse sellerAuthenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        final var seller = sellerRepository.findByEmail(request.email()).orElseThrow();
        final var token = JwtService.generateToken(seller);
        seller.setStatus("online");
        sellerRepository.save(seller);
        return new AuthenticationResponse(token, jwtService.getJwtExpiration());

    }


}
