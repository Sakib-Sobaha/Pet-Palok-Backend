package dev.sabri.securityjwt.controller;


import dev.sabri.securityjwt.service.UserService;
import dev.sabri.securityjwt.repository.UserRepository;
import dev.sabri.securityjwt.scopes.user.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    record NewUserRequest(String firstName, String lastName, String email, String password) {

    }

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
    public ResponseEntity<String> updateUser(@PathVariable("userId") String userId, @RequestBody NewUserRequest newUserRequest) {
        User user = userService.findUserById(userId);
        if(user == null) {
            System.out.printf("User with id %s not found", userId);
        }
//        setUserDetails(newUserRequest, user);
        assert user != null;
        userRepository.save(new User(user.getId(), newUserRequest.firstName, newUserRequest.lastName, newUserRequest.password));
        return ResponseEntity.ok("User updated successfully");

    }


    public void setUserDetails(@RequestBody NewUserRequest newUserRequest, User user) {
        user.setFirstname(newUserRequest.firstName);
        user.setLastname(newUserRequest.lastName);
        user.setEmail(newUserRequest.email);
        user.setPasswd(newUserRequest.password);
    }
}
