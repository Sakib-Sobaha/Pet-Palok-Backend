package dev.sabri.securityjwt.scopes.user;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("/whoami")
    public ResponseEntity<User> getLoggedInUser(Principal principal) {
        String email = principal.getName();  // Get the logged-in user's email
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    record NewUserRequest(String firstName, String lastName, String email, String password) {

    }

    record UpdateUserRequest(String firstName, String lastName, String password) {}

    @PostMapping
    public void addUser(@RequestBody NewUserRequest newUserRequest) {
        User user = new User();
        setUserDetails(newUserRequest, user);
    }

    @DeleteMapping("/delete/{userId}")
    public void deleteUser(@PathVariable("userId") String userId) {
        userRepository.deleteById(userId);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable("userId") String userId, @RequestBody UpdateUserRequest request) {
        User user = userService.findUserById(userId);
        if (user == null) {
            System.out.printf("User with id %s not found", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Log current user details before updating
        System.out.println("Before update: " + user);

        // Update only the fields that are present in the request
        if (request.firstName() != null) {
            user.setFirstname(request.firstName());
        }
        if (request.lastName() != null) {
            user.setLastname(request.lastName());
        }
        if (request.password() != null) {
            user.setPasswd(passwordEncoder.encode(request.password()));
        }

        // Log user details after updating
        System.out.println("After update: " + user);

        // Save the updated user
        userRepository.save(user);

        // Fetch the user again to verify if the update was applied
        User updatedUser = userService.findUserById(userId);
        System.out.println("Updated user from DB: " + updatedUser);

        return ResponseEntity.ok("User updated successfully");
    }


    public void setUserDetails(@RequestBody NewUserRequest newUserRequest, User user) {
        user.setFirstname(newUserRequest.firstName);
        user.setLastname(newUserRequest.lastName);
        user.setEmail(newUserRequest.email);
        user.setPasswd(newUserRequest.password);
    }
}
