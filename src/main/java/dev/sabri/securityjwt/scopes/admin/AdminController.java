package dev.sabri.securityjwt.scopes.admin;


import dev.sabri.securityjwt.controller.dto.UpdatePasswordRequest;
import dev.sabri.securityjwt.scopes.seller.Seller;
import dev.sabri.securityjwt.scopes.seller.SellerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/v1/admin")
@AllArgsConstructor
public class AdminController {
    private final AdminService adminService;
    @Autowired
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private SellerRepository sellerRepository;


    @GetMapping("/getAdmins")
    public List<Admin> getAdmins() {
        return adminRepository.findAll();
    }

    @GetMapping("/getAdminById/{adminId}")
    public ResponseEntity<Admin> getAdminById(@PathVariable("adminId") String adminId) {
        return ResponseEntity.ok(adminRepository.findById(adminId).orElse(null));
    }

    @GetMapping("/whoami")
    public ResponseEntity<Admin> getLoggedInAdmin(Principal principal) {
        String email = principal.getName();
        Admin admin = adminRepository.findByEmail(email).orElse(null);

        if (admin == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(admin);
    }

    record NewAdminRequest(String email, String password) {

    }

    record UpdateAdminRequest(String name, String password) {}

    @PostMapping
    public ResponseEntity<String> addAdmin(@RequestBody NewAdminRequest newAdminRequest) {
        Admin admin = new Admin();
        admin.setEmail(newAdminRequest.email);
        admin.setPassword(passwordEncoder.encode(newAdminRequest.password));
        admin = adminRepository.save(admin);
        return ResponseEntity.ok("Admin added successfully");
    }

    @DeleteMapping("/delete/{adminId}")
    public ResponseEntity<String> deleteAdmin(@PathVariable("adminId") String adminId) {
        adminRepository.deleteById(adminId);
        return ResponseEntity.ok("Admin deleted successfully");
    }

    @PutMapping("/updatePassword/{adminId}")
    public ResponseEntity<String> updatePassword(@PathVariable("adminId") String adminId, @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        Admin admin = adminRepository.findById(adminId).orElse(null);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
        }

        // Check whether the old password is correct
        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), admin.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect");
        }

        // Encode and update the new password
        admin.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        adminRepository.save(admin);

        return ResponseEntity.ok("Password updated successfully");
    }


    @PutMapping("/update/{adminId}")
    public ResponseEntity<String> updateAdmin(@PathVariable("adminId") String adminId, @RequestBody UpdateAdminRequest updateAdminRequest) {
        Admin admin = adminRepository.findById(adminId).orElse(null);
        if (admin == null) {
            return ResponseEntity.notFound().build();
        }

        // Log current user details before updating
        System.out.println("Before update: " + admin);

        // Update only the fields that are present in the request
        if (updateAdminRequest.name() != null) {
            admin.setName(updateAdminRequest.name());
        }
        if (updateAdminRequest.password() != null) {
            admin.setPassword(passwordEncoder.encode(updateAdminRequest.password()));
        }

        // Log user details after updating
        System.out.println("After update: " + admin);

        // Save the updated user
        adminRepository.save(admin);

        // Fetch the user again to verify if the update was applied
        Admin updatedAdmin = adminService.findAdminById(adminId);
        System.out.println("Updated user from DB: " + updatedAdmin);

        return ResponseEntity.ok("Admin updated successfully");
    }

}
