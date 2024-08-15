package dev.sabri.securityjwt;

import dev.sabri.securityjwt.controller.dto.RegisterRequest;
import dev.sabri.securityjwt.scopes.user.Role;
import dev.sabri.securityjwt.scopes.user.User;
import dev.sabri.securityjwt.service.ApiService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication

public class SecurityJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityJwtApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

//	@Bean
//	public CommandLineRunner run(ApiService apiService) {
//		return args -> {
//			String email = "sobaha@gmail.com";
//			String password = "123456";
//			RegisterRequest request = new RegisterRequest("Suvro", "Gazi", email, password);
//
//			String token = apiService.registerUser(request);
//
//			System.out.println(token);
//
//			List<User> users = apiService.getAllUsers(token);
//			users.forEach(user_ ->
//				System.out.printf(String.valueOf(user_)));
//		};
//	}



}
