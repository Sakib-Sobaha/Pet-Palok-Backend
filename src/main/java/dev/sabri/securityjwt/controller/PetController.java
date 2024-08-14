package dev.sabri.securityjwt.controller;


import dev.sabri.securityjwt.repository.PetRepository;
import dev.sabri.securityjwt.scopes.pets.Pet;
import dev.sabri.securityjwt.scopes.user.Gender;
import dev.sabri.securityjwt.service.PetService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/pets")
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class PetController {
    private final PetService petService;
    private final PetRepository petRepository;

    @GetMapping("/getAllPets")
    public List<Pet> getAllPets() {
        System.out.println("Pet aschena asbe ki??");
        return petService.getAll();
    }


    @GetMapping("/{petId}")
    public ResponseEntity<Pet> getPetById(@PathVariable("petId") String petId) {
        return petRepository.findById(petId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    record NewPetRequest(String name, int age, String type, String breed, String description, Gender gender, boolean vetVerified) {

    }

    @PostMapping
    public ResponseEntity<String> createPet(@RequestBody NewPetRequest newPetRequest) {
        Pet pet = new Pet(newPetRequest.name, newPetRequest.age, newPetRequest.type,newPetRequest.breed, newPetRequest.description, newPetRequest.gender, newPetRequest.vetVerified);
        petRepository.save(pet);
        return ResponseEntity.ok("Pet created");

    }


}
