package dev.sabri.securityjwt.scopes.vets;


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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/vet")
@AllArgsConstructor
public class VetController {
    private final VetService vetService;
    @Autowired
    private final VetRepository vetRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/getVets")
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

    record UpdateVetRequest(String firstname, String lastname, String phone, String clinic_name, String clinic_address, String address, String postOffice, String district, String country, Date dob, double rating_vetvisit, String about, String image, Gender gender, Role role){

    }

    @PostMapping
    public ResponseEntity<String> addVet(@RequestBody NewVetRequest newVetRequest){
        Vet vet = new Vet();
        vet.setEmail(newVetRequest.email);
        vet.setPassword(passwordEncoder.encode(newVetRequest.password));
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
        vet.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
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
        if (request.firstname() != null) {
            vet.setFirstname(request.firstname());
        }

        if (request.lastname() != null) {
            vet.setLastname(request.lastname());
        }
        if(request.phone() != null) {
            vet.setPhone(request.phone());
        }
        if(request.clinic_name() != null){
            vet.setClinic_name(request.clinic_name());
        }
        if(request.clinic_address() != null){
            vet.setClinic_address(request.clinic_address());
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
        if (request.dob() != null) {
            vet.setDob(request.dob());
        }
        if(request.rating_vetvisit() > 0){
            vet.setRating_vetvisit(request.rating_vetvisit());
        }
        if (request.about() != null) {
            vet.setAbout(request.about());
        }
        if(request.image() != null) {
            vet.setImage(request.image());
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
        System.out.println("Updated vet from DB: " + updatedVet);

        return ResponseEntity.ok("Vet updated successfully");
    }


}
