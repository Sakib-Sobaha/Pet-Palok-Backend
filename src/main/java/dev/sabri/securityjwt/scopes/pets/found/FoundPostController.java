package dev.sabri.securityjwt.scopes.pets.found;


import com.azure.core.annotation.Get;
import dev.sabri.securityjwt.scopes.notifications.NotificationRepository;
import dev.sabri.securityjwt.scopes.pets.PetRepository;
import dev.sabri.securityjwt.scopes.pets.PetType;
import dev.sabri.securityjwt.scopes.user.User;
import dev.sabri.securityjwt.scopes.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/foundPost")
public class FoundPostController {
    @Autowired
    private FoundPostRepository foundPostRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PetRepository petRepository;



    @GetMapping("/fetchAll")
    public ResponseEntity<List<FoundPost>> fetchAll() {
        List<FoundPost> foundPosts = foundPostRepository.findAll();
        return ResponseEntity.ok(foundPosts);
    }

    @GetMapping("/{foundPostId}")
    public ResponseEntity<FoundPost> fetchFoundPostById(@PathVariable String foundPostId) {
        Optional<FoundPost> foundPost = foundPostRepository.findById(foundPostId);
        if (foundPost.isPresent()) {
            return ResponseEntity.ok(foundPost.get());
        }
        return ResponseEntity.notFound().build();
    }


    record NewFoundPostRequest(String title, Date date_found, String pet_type, String location_found, String description, List<String> images, String condition, String contactNo, String location_return, String appearance_breed, String confirmationStatus) {}
    @PostMapping("/create")
    public ResponseEntity<FoundPost> create(@RequestBody NewFoundPostRequest foundPostRequest, Principal principal) {
        String email = principal.getName();

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            FoundPost post = new FoundPost();
            if(foundPostRequest.title != null) {
                post.setTitle(foundPostRequest.title);
            }
            if(foundPostRequest.date_found != null) {
                post.setDate_found(foundPostRequest.date_found);
            }

            if(foundPostRequest.location_found != null) {
                post.setLocation_found(foundPostRequest.location_found);
            }
            if(foundPostRequest.description != null) {
                post.setDescription(foundPostRequest.description);
            }
            if(foundPostRequest.images != null) {
                post.setImages(foundPostRequest.images);
            }
            if(foundPostRequest.appearance_breed != null) {
                post.setAppearance_breed(foundPostRequest.appearance_breed);
            }
            if(foundPostRequest.condition != null) {
                post.setCondition(foundPostRequest.condition);
            }
            if(foundPostRequest.contactNo != null) {
                post.setContactNo(foundPostRequest.contactNo);
            }

            if(foundPostRequest.appearance_breed != null) {
                post.setAppearance_breed(foundPostRequest.appearance_breed);
            }
            if(foundPostRequest.pet_type.equalsIgnoreCase("animal")){
                post.setPet_type(PetType.ANIMAL);
            }
            else if(foundPostRequest.pet_type.equalsIgnoreCase("bird")){
                post.setPet_type(PetType.BIRD);
            }
            else if(foundPostRequest.pet_type.equalsIgnoreCase("fish")){
                post.setPet_type(PetType.FISH);

            }
            else post.setPet_type(PetType.OTHERS);


            if(foundPostRequest.confirmationStatus.equalsIgnoreCase("pending")){
                post.setConfirmation_status(ConfirmationStatus.pending);
            }
            else if(foundPostRequest.confirmationStatus.equalsIgnoreCase("confirmed")){
                post.setConfirmation_status(ConfirmationStatus.confirmed);
            }
            else if(foundPostRequest.confirmationStatus.equalsIgnoreCase("denied")){
                post.setConfirmation_status(ConfirmationStatus.denied);
            }
            else post.setConfirmation_status(ConfirmationStatus.others);


            post.setAuthorId(user.get().getId());




            post.setLocation_return(foundPostRequest.location_return);

            foundPostRepository.save(post);
            return ResponseEntity.ok(post);
        }

        return ResponseEntity.notFound().build();



    }
}
