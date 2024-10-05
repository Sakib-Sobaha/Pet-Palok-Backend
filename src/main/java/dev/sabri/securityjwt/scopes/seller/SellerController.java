package dev.sabri.securityjwt.scopes.seller;


import dev.sabri.securityjwt.controller.dto.StatusUpdateRequest;
import dev.sabri.securityjwt.controller.dto.UpdatePasswordRequest;
import dev.sabri.securityjwt.scopes.user.Gender;
import dev.sabri.securityjwt.scopes.user.Role;
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

    @GetMapping("/getSellerById/{sellerId}")
    public ResponseEntity<Seller> getSellerById(@PathVariable("sellerId") String sellerId) {
        System.out.println("Get seller by id req rcv: sellerID-> " + sellerId);
        return ResponseEntity.ok(sellerRepository.findById(sellerId).orElse(null));
    }

    record NewSellerRequest(String email, String password) {

    }



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
        if (!passwordEncoder.matches(updatePasswordRequest.getCurrentPassword(), seller.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect");
        }

        // Encode and update the new password
        seller.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        sellerRepository.save(seller);

        return ResponseEntity.ok("Password updated successfully");
    }


    record UpdateSellerRequest(String name, String storeName, String storeAddress, String slogan, String phone, String address, String postOffice, String district, String country, String about, Date dob, String image,Role role, String gender) {}

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

        if(request.storeAddress() != null) {
            seller.setStoreAddress(request.storeAddress());
        }

        if (request.slogan() != null) {
            seller.setSlogan(request.slogan());
        }

        if(request.phone() != null) {
            seller.setPhone(request.phone());
        }
        if(request.address() != null) {
            seller.setAddress(request.address());
        }
        if(request.postOffice() != null) {
            seller.setPostOffice(request.postOffice());
        }
        if(request.district() != null) {
            seller.setDistrict(request.district());
        }
        if(request.country() != null) {
            seller.setCountry(request.country());
        }

        if(request.about() != null) {
            seller.setAbout(request.about());
        }
        if(request.dob() != null) {
            seller.setDob(request.dob());
        }
        if(request.image() != null){
            seller.setImage(request.image());
        }

        if(request.role() != null) {
            seller.setRole(request.role());
        }
        if (request.gender() != null) {
            if(request.gender().equalsIgnoreCase("male")) {
                seller.setGender( Gender.male);
            }
            else if(request.gender().equalsIgnoreCase("female")) {
                seller.setGender( Gender.female);
            }
        }


        // Log user details after updating
        System.out.println("After update: " + seller);

        // Save the updated user
        sellerRepository.save(seller);

        // Fetch the user again to verify if the update was applied
        Seller updatedSeller = sellerService.findSellerById(sellerId);
        System.out.println("Updated seller from DB: " + updatedSeller);

        return ResponseEntity.ok("Seller updated successfully");
    }

    @PatchMapping("/update-status/{sellerId}")
    public ResponseEntity<String> updateSellerStatus(@PathVariable("sellerId") String sellerId,@RequestBody StatusUpdateRequest statusUpdateRequest) {
        sellerService.updateSellerStatus(sellerId, statusUpdateRequest.status());
        return ResponseEntity.ok("Seller Status updated successfully");
    }






}
