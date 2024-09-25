package dev.sabri.securityjwt.scopes.vetvisit.appointmentReview;


import com.azure.core.annotation.Post;
import dev.sabri.securityjwt.scopes.seller.MarketItems;
import dev.sabri.securityjwt.scopes.seller.review.Review;
import dev.sabri.securityjwt.scopes.user.User;
import dev.sabri.securityjwt.scopes.user.UserRepository;
import dev.sabri.securityjwt.scopes.vets.Vet;
import dev.sabri.securityjwt.scopes.vets.VetRepository;
import dev.sabri.securityjwt.scopes.vetvisit.pendingAppointmentReview.PendingAppointmentReviewRepository;
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
@RequestMapping("/api/v1/appointment/review")
public class AppointmentReviewController {
    @Autowired
    private final VetRepository vetRepository;
    @Autowired
    private final AppointmentReviewRepository appointmentReviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PendingAppointmentReviewRepository pendingAppointmentReviewRepository;

    @GetMapping("/getByVet/{vetId}")
    public ResponseEntity<List<AppointmentReview>> getReviewByItemId(@PathVariable String vetId) {
        Optional<Vet> vet = vetRepository.findById(vetId);

        if (vet.isPresent()) {
            return ResponseEntity.ok(appointmentReviewRepository.findByVetId(vetId));
        }

        return ResponseEntity.notFound().build();
    }

    record NewReview(String vetId, Integer vetRating, String vetReview, Boolean anonymous, String pendingAppointmentReviewId){}
    @PostMapping("/create")
    public ResponseEntity<AppointmentReview> create(@RequestBody NewReview newReview, Principal principal) {
        System.out.println("NEW appointment review created *****");
        String email = principal.getName();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            String uid = user.get().getId();

            AppointmentReview appointmentReview = new AppointmentReview();
            appointmentReview.setVetId(newReview.vetId);
            appointmentReview.setVetRating(newReview.vetRating);
            appointmentReview.setVetReview(newReview.vetReview);
            appointmentReview.setAnonymous(newReview.anonymous);
            appointmentReview.setUserId(uid);
            appointmentReview.setTimestamp(new Date());


            appointmentReviewRepository.save(appointmentReview);

            pendingAppointmentReviewRepository.deleteById(newReview.pendingAppointmentReviewId);
            updateVetRating(newReview.vetId);

            return ResponseEntity.ok(appointmentReview);
        }

        return ResponseEntity.notFound().build();
    }

    private void updateVetRating(String vetId) {
        // Fetch all reviews associated with the given vetId
        List<AppointmentReview> appointmentReviews = appointmentReviewRepository.findByVetId(vetId);

        // If there are no reviews, there's nothing to update
        if (appointmentReviews.isEmpty()) return;

        // Calculate the average vet rating based on all associated appointment reviews
        double averageVetRating = appointmentReviews.stream()
                .mapToInt(AppointmentReview::getVetRating)
                .average()
                .orElse(0.0);

        // Fetch vet and update their rating
        Optional<Vet> vetOptional = vetRepository.findById(vetId);
        if (vetOptional.isPresent()) {
            Vet vet = vetOptional.get();
            vet.setRating_vetvisit((float) averageVetRating); // Assuming this is the correct setter method
            vetRepository.save(vet);
        }
    }

}
