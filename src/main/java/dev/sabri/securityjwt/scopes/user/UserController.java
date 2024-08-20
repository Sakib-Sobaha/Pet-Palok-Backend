package dev.sabri.securityjwt.scopes.user;


import com.azure.core.annotation.Put;
import dev.sabri.securityjwt.controller.dto.UpdatePasswordRequest;
import dev.sabri.securityjwt.scopes.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;

import java.time.LocalDateTime;
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
    public ResponseEntity<UserDTO> getLoggedInUser(Principal principal) {
        String email = principal.getName();  // Get the logged-in user's email
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

//        return ResponseEntity.ok(user);
        // Map User entity to UserDTO
        // Map User entity to UserDTO
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getPasswd(),  // Password
                user.getPostOffice(),
                user.getDistrict(),
                user.getCountry(),
                user.getDateOfBirth(),  // Date of Birth
                user.getRatingBuySellExchange(),
                user.getRatingPetKeeping(),
                user.getRatingVet(),
                user.getAbout(),
                user.getImage(),  // Image URL or path
                user.getGender(),
                user.getRole()
        );

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/getUserById/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") String userId) {

        return ResponseEntity.ok(userRepository.findUserById(userId));
    }

    record NewUserRequest(String firstName, String lastName, String email, String password) {

    }

    record UpdateUserRequest(String firstName, String lastName, String phoneNumber, String password, String address, String postOffice, String district, String country, Date dob, Integer ratingBuySellExchange, Integer ratingPetKeeping, Integer ratingVet, String about) {

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
        user.setPasswd(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
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
        if (request.password() != null) {
            user.setPasswd(passwordEncoder.encode(request.password()));
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
            user.setDateOfBirth(request.dob());
        }
        if (request.ratingBuySellExchange() != null) {
            user.setRatingBuySellExchange(request.ratingBuySellExchange());
        }
        if (request.ratingPetKeeping() != null) {
            user.setRatingPetKeeping(request.ratingPetKeeping());
        }
        if (request.ratingVet() != null) {
            user.setRatingVet(request.ratingVet());
        }
        if (request.about() != null) {
            user.setAbout(request.about());
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
