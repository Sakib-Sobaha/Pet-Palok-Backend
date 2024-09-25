package dev.sabri.securityjwt.scopes.vetvisit.pendingAppointmentReview;

import dev.sabri.securityjwt.scopes.user.User;
import dev.sabri.securityjwt.scopes.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dev.sabri.securityjwt.scopes.vetvisit.pendingAppointmentReview.PendingAppointmentReview;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/appointments/pendingReviews")
public class PendingAppointmentReviewController {
    @Autowired
    private PendingAppointmentReviewRepository pendingAppointmentReviewRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/fetchByUser")
    public ResponseEntity<List<PendingAppointmentReview>> fetchPendingAppointmentReviews(Principal principal) {
        String email = principal.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if(user.isPresent()) {
            String id = user.get().getId();
            return ResponseEntity.ok(pendingAppointmentReviewRepository.findByUserId(id));
        }

        return ResponseEntity.notFound().build();
    }

    record NewPendingAppointmentReview(String appointmentId){}
    @PostMapping("/create")
    public ResponseEntity<PendingAppointmentReview> createPendingAppointmentReview(Principal principal, @RequestBody NewPendingAppointmentReview newPendingReview) {
        String email = principal.getName();

        Optional<User> user = userRepository.findByEmail(email);

        if(user.isPresent()) {
            String id = user.get().getId();
            PendingAppointmentReview pendingAppointmentReview = new PendingAppointmentReview();
            pendingAppointmentReview.setAppointmentId(newPendingReview.appointmentId);
            pendingAppointmentReview.setUserId(id);
            pendingAppointmentReviewRepository.save(pendingAppointmentReview);
            return ResponseEntity.ok(pendingAppointmentReview);
        }
        return ResponseEntity.notFound().build();
    }

}
