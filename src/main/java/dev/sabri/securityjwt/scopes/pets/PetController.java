package dev.sabri.securityjwt.scopes.pets;


import dev.sabri.securityjwt.client.imagestorage.AzureImageStorageClient;
import dev.sabri.securityjwt.scopes.pets.dto.NewPetRequest;
import dev.sabri.securityjwt.scopes.user.User;
import dev.sabri.securityjwt.scopes.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/pets")
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class PetController {
    private final PetService petService;
    private final PetRepository petRepository;
    private final AzureImageStorageClient azureImageStorageClient;
    private final UserRepository userRepository;


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

    @GetMapping("/myPets")
    public ResponseEntity<Optional<List<Pet>>> getMyPets(Principal principal) {
        String email = principal.getName();
        Optional<User> user_ = userRepository.findByEmail(email);

        if (user_.isPresent()) {
            return ResponseEntity.ok(petRepository.findPetByOwnerId(user_.get().getId()));
        } else
            return ResponseEntity.notFound().build();

    }


//    record NewPetRequest(String name, int age, String type, String breed, String description, Gender, boolean vetVerified) {
//
//    }

    @PostMapping("/create-pet")
    public ResponseEntity<String> createPet(@RequestBody NewPetRequest newPetRequest) {
        Pet pet = new Pet(newPetRequest.ownerId, newPetRequest.name, newPetRequest.dob, newPetRequest.type, newPetRequest.breed, newPetRequest.description, newPetRequest.gender, newPetRequest.vetVerified, newPetRequest.images);
        System.out.println("Received new pet request: ");
        System.out.println(newPetRequest.name + " " + newPetRequest.description + "imageUrl" + newPetRequest.dob);
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

    record Images(List<String> newImageUrls){}
    @PostMapping("/imageUpload/{petId}")
    public ResponseEntity<Pet> uploadImage(@RequestBody Images images, @PathVariable String petId) {
        Optional<Pet> pet = petRepository.findById(petId);
        if (pet.isPresent()) {
            Pet pet1 = pet.get();
            System.out.println("New image uploaded, urls:");
            for(String s  : images.newImageUrls)
                System.out.println(s);

            List<String> list = pet1.getImages();

            for(String s : images.newImageUrls)
            {
                list.add(s);
            }

            pet1.setImages(list);

            petRepository.save(pet1);

            return ResponseEntity.ok(pet1);

        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{petId}")
    public ResponseEntity<String> deletePet(@PathVariable("petId") String petId) {
        petRepository.deleteById(petId);
        return ResponseEntity.ok("Pet deleted successfully");
    }



}
