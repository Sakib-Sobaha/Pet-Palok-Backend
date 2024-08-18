package dev.sabri.securityjwt.scopes.pets;


import dev.sabri.securityjwt.client.imagestorage.AzureImageStorageClient;
import dev.sabri.securityjwt.scopes.pets.dto.NewPetRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/pets")
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class PetController {
    private final PetService petService;
    private final PetRepository petRepository;
    private final AzureImageStorageClient azureImageStorageClient;


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

    @GetMapping("/petByOwnerId/{ownerId}")
    public ResponseEntity<List<Pet>> getPetByOwnerId(@PathVariable("ownerId") String ownerId) {
        return petRepository.findPetByOwnerId(ownerId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }



//    record NewPetRequest(String name, int age, String type, String breed, String description, Gender gender, boolean vetVerified) {
//
//    }

    @PostMapping("/create-pet")
    public ResponseEntity<String> createPet(@RequestBody NewPetRequest newPetRequest) {
        Pet pet = new Pet(newPetRequest.ownerId, newPetRequest.name, newPetRequest.type,newPetRequest.breed, newPetRequest.description, newPetRequest.gender, newPetRequest.vetVerified, newPetRequest.images);
//        List<String> imageUrls = new ArrayList<>();
//
//        //Upload each image and store the url
//        for(MultipartFile image: newPetRequest.images){
//            try{
//                String imageUrl = azureImageStorageClient.uploadImage("petpalok-image-container",
//                        Objects.requireNonNull(image.getOriginalFilename()), image.getInputStream(), image.getSize());
//                imageUrls.add(imageUrl);
//
//            }catch (IOException e){
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image" + image.getOriginalFilename());
//            }
//        }
//
//        pet.setImages(imageUrls);

        petRepository.save(pet);
        //handle multiple images
        return ResponseEntity.ok("Pet created Successfully");

    }


}
