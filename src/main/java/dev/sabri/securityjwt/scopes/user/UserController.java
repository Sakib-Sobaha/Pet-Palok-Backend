package dev.sabri.securityjwt.scopes.user;


import dev.sabri.securityjwt.controller.dto.UpdatePasswordRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;

import java.util.Date;
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

    @GetMapping("/getUserById/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(userRepository.findUserById(userId));
    }

    record NewUserRequest(String firstName, String lastName, String email, String password) {

    }

    record UpdateUserRequest(String firstName, String lastName, String phoneNumber,  String address, String postOffice, String district, String country, Date dob,  String about, String image, String gender) {

    }



    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody NewUserRequest newUserRequest) {
        User user = new User();
        setUserDetails(newUserRequest, user);
        return ResponseEntity.ok("User added successfully");
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId) {
        userRepository.deleteById(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/updatePassword/{userId}")
    public ResponseEntity<String> updatePassword(@PathVariable("userId") String userId, @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Check whether the old password is correct
        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect");
        }

        // Encode and update the new password
        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully");
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
        if (request.phoneNumber() != null) {
            user.setPhoneNumber(request.phoneNumber());
        }
        if (request.address() != null) {
            user.setAddress(request.address());
        }
        if (request.postOffice() != null) {
            user.setPostOffice(request.postOffice());
        }
        if (request.district() != null) {
            user.setDistrict(request.district());
        }
        if (request.country() != null) {
            user.setCountry(request.country());
        }
        if (request.dob() != null) {
            user.setDob(request.dob());
        }
        if (request.about() != null) {
            user.setAbout(request.about());
        }
        if (request.image() != null) {
            user.setImage(request.image());
            System.out.println("New image :) ");
        }
        if (request.gender() != null) {
            if(request.gender().equalsIgnoreCase("male")) {
                user.setGender( Gender.male);
            }
            else if(request.gender().equalsIgnoreCase("female")) {
                user.setGender( Gender.female);
            }
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
        user.setPassword(newUserRequest.password);
    }
}
