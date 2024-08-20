package dev.sabri.securityjwt.scopes.vets;


import com.azure.core.annotation.Get;
import dev.sabri.securityjwt.controller.dto.UpdatePasswordRequest;
import dev.sabri.securityjwt.scopes.user.Gender;
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
@RequestMapping("api/v1/vet")
@AllArgsConstructor
public class VetController {
    private final VetService vetService;
    @Autowired
    private final VetRepository vetRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Vet> getAllVets(){
        return vetService.getAll();
    }

    @GetMapping("/whoami")
    public ResponseEntity<Vet> getLoggedInVet(Principal principal){
        String email = principal.getName();
        Vet vet = vetRepository.findByEmail(email).orElse(null);

        if(vet == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vet);

    }

    @GetMapping("/getVetById/{vetId}")
    public ResponseEntity<Vet> getVetById(@PathVariable String vetId){
        return ResponseEntity.ok(vetRepository.findById(vetId).orElse(null));
    }

    record NewVetRequest(String email, String password){}

    record UpdateVetRequest(String firstName, String lastName, String phoneNumber, String password, String address, String postOffice, String district, String country, LocalDateTime dateOfBirth, String about, String clinic, Gender gender, Role role){

    }

    @PostMapping
    public ResponseEntity<String> addVet(@RequestBody NewVetRequest newVetRequest){
        Vet vet = new Vet();
        vet.setEmail(newVetRequest.email);
        vet.setPasswd(passwordEncoder.encode(newVetRequest.password));
        vetRepository.save(vet);
        return ResponseEntity.ok("Vet added successfully");
    }

    @DeleteMapping("/delete/{vetId}")
    public ResponseEntity<String> deleteVet(@PathVariable("vetId") String vetId){
        vetRepository.deleteById(vetId);
        return ResponseEntity.ok("Vet deleted successfully");
    }

    @PutMapping("/updatePassword/{vetId}")
    public ResponseEntity<String> updatePassword(@PathVariable("vetId") String vetId, @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        Vet vet = vetRepository.findById(vetId).orElse(null);
        if (vet == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vet not found");
        }

        // Check whether the old password is correct
        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), vet.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect");
        }

        // Encode and update the new password
        vet.setPasswd(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        vetRepository.save(vet);

        return ResponseEntity.ok("Password updated successfully");
    }

    @PutMapping("/update/{vetId}")
    public ResponseEntity<String> updateVet(@PathVariable("vetId") String vetId, @RequestBody UpdateVetRequest request){
        Vet vet = vetRepository.findById(vetId).orElse(null);
        if(vet == null){
            return ResponseEntity.notFound().build();
        }

        // Log current user details before updating
        System.out.println("Before update: " + vet);

        // Update only the fields that are present in the request
        if (request.firstName() != null) {
            vet.setFirstname(request.firstName());
        }

        if (request.lastName() != null) {
            vet.setLastname(request.lastName());
        }
        if(request.phoneNumber() != null) {
            vet.setPhoneNumber(request.phoneNumber());
        }

        if (request.password() != null) {
            vet.setPasswd(passwordEncoder.encode(request.password()));
        }
        if (request.address() != null) {
            vet.setAddress(request.address());
        }
        if (request.postOffice() != null) {
            vet.setPostOffice(request.postOffice());
        }
        if (request.district() != null) {
            vet.setDistrict(request.district());
        }
        if (request.country() != null) {
            vet.setCountry(request.country());
        }
        if (request.dateOfBirth() != null) {
            vet.setDateOfBirth(request.dateOfBirth());
        }
        if (request.about() != null) {
            vet.setAbout(request.about());
        }
        if (request.clinic() != null) {
            vet.setClinic(request.clinic());
        }
        if(request.gender() != null){
            vet.setGender(request.gender());
        }
        if(request.role() != null){
            vet.setRole(request.role());
        }

        // Log user details after updating
        System.out.println("After update: " + vet);

        // Save the updated user
        vetRepository.save(vet);

        // Fetch the user again to verify if the update was applied
        Vet updatedVet = vetRepository.findById(vetId).orElse(null);
        System.out.println("Updated user from DB: " + updatedVet);

        return ResponseEntity.ok("Vet updated successfully");
    }


}
