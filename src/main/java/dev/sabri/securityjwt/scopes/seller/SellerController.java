package dev.sabri.securityjwt.scopes.seller;


import dev.sabri.securityjwt.scopes.user.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    public ResponseEntity<Seller> getLoggedInUser(Principal principal) {
        String email = principal.getName();
        Seller seller = sellerRepository.findByEmail(email).orElse(null);

        if (seller == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(seller);
    }

    record NewSellerRequest(String email, String password) {

    }

    record UpdateSellerRequest(String name, String password, String storeName) {}

    @PostMapping
    public ResponseEntity<String> addSeller(@RequestBody NewSellerRequest newSellerRequest) {
        Seller seller = new Seller();
        seller.setEmail(newSellerRequest.email());
        seller.setPasswd(passwordEncoder.encode(newSellerRequest.password()));
        sellerRepository.save(seller);
        return ResponseEntity.ok("Seller account registered successfully");
    }

    @DeleteMapping("/delete/{sellerId}")
    public ResponseEntity<String> deleteSeller(@PathVariable("sellerId") String sellerId) {
        sellerRepository.deleteById(sellerId);
        return ResponseEntity.ok("Seller account deleted successfully");
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
        if (request.password() != null) {
            seller.setPasswd(passwordEncoder.encode(request.password()));
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
