package dev.sabri.securityjwt.scopes.seller;


import dev.sabri.securityjwt.controller.dto.UpdatePasswordRequest;
import dev.sabri.securityjwt.scopes.user.Role;
import dev.sabri.securityjwt.scopes.user.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/seller")
@AllArgsConstructor
public class SellerController {
    private final SellerService sellerService;
    @Autowired
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/getSellers")
    public List<Seller> getSellers() {
        return sellerRepository.findAll();
    }

    @GetMapping("/whoami")
    public ResponseEntity<Seller> getLoggedInSeller(Principal principal) {
        String email = principal.getName();
        Seller seller = sellerRepository.findByEmail(email).orElse(null);

        if (seller == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(seller);
    }

    record NewSellerRequest(String email, String password) {

    }

    record UpdateSellerRequest(String name, String storeName, String slogan, String password, String phone, String address, String info, LocalDateTime dob, Role role) {}

    @PostMapping
    public ResponseEntity<String> addSeller(@RequestBody NewSellerRequest newSellerRequest) {
        Seller seller = new Seller();
        seller.setEmail(newSellerRequest.email());
        seller.setPassword(passwordEncoder.encode(newSellerRequest.password()));
        sellerRepository.save(seller);
        return ResponseEntity.ok("Seller account registered successfully");
    }

    @DeleteMapping("/delete/{sellerId}")
    public ResponseEntity<String> deleteSeller(@PathVariable("sellerId") String sellerId) {
        sellerRepository.deleteById(sellerId);
        return ResponseEntity.ok("Seller account deleted successfully");
    }

    @PutMapping("/updatePassword/{sellerId}")
    public ResponseEntity<String> updatePassword(@PathVariable("sellerId") String sellerId, @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        Seller seller = sellerRepository.findById(sellerId).orElse(null);
        if (seller == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller not found");
        }

        // Check whether the old password is correct
        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), seller.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect");
        }

        // Encode and update the new password
        seller.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        sellerRepository.save(seller);

        return ResponseEntity.ok("Password updated successfully");
    }

    @PutMapping("/update/{sellerId}")
    public ResponseEntity<String> updateSeller(@PathVariable("sellerId") String sellerId, @RequestBody UpdateSellerRequest request) {
        Seller seller = sellerRepository.findById(sellerId).orElse(null);
        if (seller == null) {
            return ResponseEntity.notFound().build();
        }

        // Log current user details before updating
        System.out.println("Before update: " + seller);

        // Update only the fields that are present in the request
        if (request.name() != null) {
            seller.setName(request.name());
        }
        if (request.storeName() != null) {
            seller.setStoreName(request.storeName());
        }
        if (request.slogan() != null) {
            seller.setSlogan(request.slogan());
        }
        if (request.password() != null) {
            seller.setPassword(passwordEncoder.encode(request.password()));
        }
        if(request.phone() != null) {
            seller.setPhone(request.phone());
        }
        if(request.address() != null) {
            seller.setAddress(request.address());
        }
        if(request.info() != null) {
            seller.setInfo(request.info());
        }


        // Log user details after updating
        System.out.println("After update: " + seller);

        // Save the updated user
        sellerRepository.save(seller);

        // Fetch the user again to verify if the update was applied
        Seller updatedSeller = sellerService.findSellerById(sellerId);
        System.out.println("Updated user from DB: " + updatedSeller);

        return ResponseEntity.ok("User updated successfully");
    }






}
