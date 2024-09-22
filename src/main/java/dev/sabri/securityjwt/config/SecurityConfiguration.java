package dev.sabri.securityjwt.config;

import dev.sabri.securityjwt.scopes.admin.AdminRepository;
import dev.sabri.securityjwt.scopes.seller.SellerRepository;
import dev.sabri.securityjwt.scopes.user.UserRepository;
import dev.sabri.securityjwt.scopes.vets.VetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    private final UserRepository userRepository;

    private final AdminRepository adminRepository;

    private final SellerRepository sellerRepository;

    private final VetRepository vetRepository;



    public SecurityConfiguration(UserRepository userRepository, AdminRepository adminRepository, SellerRepository sellerRepository, VetRepository vetRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.sellerRepository = sellerRepository;
        this.vetRepository = vetRepository;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.
                        requestMatchers(
                                new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/error"),
                                new AntPathRequestMatcher("/favicon.ico"),
                                new AntPathRequestMatcher("/actuator/*"),
                                new AntPathRequestMatcher("/api/v1/user/register"),
                                new AntPathRequestMatcher("/api/v1/user/authenticate"),
                                new AntPathRequestMatcher("/api/v1/user/verify"),
                                new AntPathRequestMatcher("/api/v1/user/resend"),
                                new AntPathRequestMatcher("/api/v1/admin/register"),
                                new AntPathRequestMatcher("/api/v1/admin/authenticate"),
                                new AntPathRequestMatcher("/api/v1/admin/verify"),
                                new AntPathRequestMatcher("/api/v1/admin/resend"),
                                new AntPathRequestMatcher("/api/v1/seller/register"),
                                new AntPathRequestMatcher("/api/v1/seller/authenticate"),
                                new AntPathRequestMatcher("/api/v1/seller/verify"),
                                new AntPathRequestMatcher("/api/v1/seller/resend"),
                                new AntPathRequestMatcher("/api/v1/vet/register"),
                                new AntPathRequestMatcher("/api/v1/vet/authenticate"),
                                new AntPathRequestMatcher("/api/v1/vet/verify"),
                                new AntPathRequestMatcher("/api/v1/vet/resend"),
                                new AntPathRequestMatcher("/ws/**"),
//                                new AntPathRequestMatcher("/api/**"),
//                                new AntPathRequestMatcher("/api/v1/**"),
//                                new AntPathRequestMatcher("/api/v1/auth/*"),
//                                new AntPathRequestMatcher("/api/v1/auth/**"),
                                new AntPathRequestMatcher("http://localhost:3000/**")
                        )
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(new JwtAuthenticationFilter(userDetailsService()), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> userRepository
//                .findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User Not Found !"));
//    }



    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {

            var user = userRepository.findByEmail(username);
            if(user.isPresent()) {
                return user.get();
            }

            var admin = adminRepository.findByEmail(username);
            if(admin.isPresent()) {
                return admin.get();
            }

            var seller = sellerRepository.findByEmail(username);
            if(seller.isPresent()) {
                return seller.get();
            }

            var vet = vetRepository.findByEmail(username);
            if(vet.isPresent()) {
                return vet.get();
            }

            throw new UsernameNotFoundException("User not found with email: " + username);
        };
    }




    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;

    }



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow all origins
//        configuration.setAllowedOrigins(List.of("*"));

        // Allow all HTTP methods
//        configuration.setAllowedMethods(List.of("*"));

        // Allow all headers
//        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000","https://pet-palok.vercel.app/")); // or your specific frontend host
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true); // Important for cookies, authorization headers with HTTPS

        // Allow credentials if needed (be cautious with this in production)
//        configuration.setAllowCredentials(true);

        // Create and register the CORS configuration for all endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();

    }


    @Bean
    ApplicationListener<AuthenticationSuccessEvent> authSuccess() {
        return event -> {
            var auth = event.getAuthentication();
            log.info("LOGIN SUCCESSFUL [{}] - {}", auth.getClass().getSimpleName(), auth.getName());
        };
    }
}
