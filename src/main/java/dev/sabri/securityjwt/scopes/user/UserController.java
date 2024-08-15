package dev.sabri.securityjwt.scopes.user;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
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

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable("userId") String userId) {
        userRepository.deleteById(userId);
    }

    @PutMapping("{userId}")
    public ResponseEntity<String> updateUser(@PathVariable("userId") String userId, @RequestBody UpdateUserRequest request) {
        User user = userService.findUserById(userId);
        if(user == null) {
            System.out.printf("User with id %s not found", userId);
        }
//        setUserDetails(newUserRequest, user);
        assert user != null;
        final var user_ = new User(null,
                request.firstName(),
                request.lastName(),
                user.getEmail(),
                passwordEncoder.encode(request.password()),
                Role.USER);
        user_.setFirstname(request.firstName());
        user_.setLastname(request.lastName());
        userRepository.save(user_);
        System.out.println(user_);
        userRepository.deleteById(userId);
        return ResponseEntity.ok("User updated successfully");

    }


    public void setUserDetails(@RequestBody NewUserRequest newUserRequest, User user) {
        user.setFirstname(newUserRequest.firstName);
        user.setLastname(newUserRequest.lastName);
        user.setEmail(newUserRequest.email);
        user.setPasswd(newUserRequest.password);
    }
}
