package dev.sabri.securityjwt.service;

import dev.sabri.securityjwt.controller.dto.AuthenticationRequest;
import dev.sabri.securityjwt.controller.dto.AuthenticationResponse;
import dev.sabri.securityjwt.controller.dto.RegisterRequest;
import dev.sabri.securityjwt.email.EmailSender;
import dev.sabri.securityjwt.email.EmailService;
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
import dev.sabri.securityjwt.utils.JwtTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public record AuthenticationService(UserRepository userRepository,
                                    AdminRepository adminRepository,
                                    SellerRepository sellerRepository,
                                    VetRepository vetRepository,
                                    PasswordEncoder passwordEncoder,
                                    AuthenticationManager authenticationManager,
                                    EmailSender emailSender,
                                    EmailValidator emailValidator,
                                    JwtTokenService jwtTokenService) {
    public AuthenticationResponse register(RegisterRequest request) {
        final var user = new User(null,
                request.firstname(),
                request.lastname(),
                request.email(),
                passwordEncoder.encode(request.password()),
                Role.USER);
        userRepository.save(user);
        final var token = JwtService.generateToken(user);
        return new AuthenticationResponse(token);
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
        return new AuthenticationResponse(token);

    }



    public AuthenticationResponse adminRegister(AdminRegisterRequest request) {
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

//        String link = "http://localhost:8080/api/v1/admin/register/confirm?token=" + token;
//        emailSender.send(
//                request.email(),
//                jwtTokenService().buildEmail(request.email(), link));
        return new AuthenticationResponse(token);
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
        return new AuthenticationResponse(token);

    }



    public AuthenticationResponse userRegister(UserRegisterRequest request) {
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
        userRepository.save(user);
        final var token = JwtService.generateToken(user);

//        String link = "http://localhost:8080/api/v1/user/register/confirm?token=" + token;
//        emailSender.send(
//                request.email(),
//                jwtTokenService().buildEmail(request.email(), link));

        return new AuthenticationResponse(token);
    }



    public AuthenticationResponse userAuthenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        final var user = userRepository.findByEmail(request.email()).orElseThrow();
        final var token = JwtService.generateToken(user);
        user.setStatus("online");
        userRepository.save(user);
        return new AuthenticationResponse(token);

    }

    public AuthenticationResponse vetRegister(VetRegisterRequest request) {
        final var vet = new Vet(null,
                request.email(),
                passwordEncoder.encode(request.password()),
                Role.VET);
        vetRepository.save(vet);
        final var token = JwtService.generateToken(vet);
        return new AuthenticationResponse(token);
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
        return new AuthenticationResponse(token);

    }

    public AuthenticationResponse sellerRegister(SellerRegisterRequest request) {
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
        return new AuthenticationResponse(token);
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
        return new AuthenticationResponse(token);

    }


}
