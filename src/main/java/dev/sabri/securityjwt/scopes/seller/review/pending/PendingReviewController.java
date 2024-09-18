package dev.sabri.securityjwt.scopes.seller.review.pending;


import dev.sabri.securityjwt.scopes.seller.review.Review;
import dev.sabri.securityjwt.scopes.user.User;
import dev.sabri.securityjwt.scopes.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/review/pending")
public class PendingReviewController {
    @Autowired
    private PendingReviewRepository pendingReviewRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/fetchByUser")
    public ResponseEntity<List<PendingReview>> fetchByUser(Principal principal) {
        System.out.println("fetch review by user: " + principal.getName());
        String email = principal.getName();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            String uid = user.get().getId();
            List<PendingReview> list = pendingReviewRepository.findByUserId(uid);
            return ResponseEntity.ok(list);
        }

        return ResponseEntity.notFound().build();
    }

    record NewPendingReview(String userId, String marketItemId){}

    @PostMapping("/create")
    public ResponseEntity<PendingReview> create(@RequestBody NewPendingReview newPendingReview) {
        PendingReview pendingReview = new PendingReview();

        pendingReview.setUserId(newPendingReview.userId);
        pendingReview.setMarketItemId(newPendingReview.marketItemId);

        pendingReviewRepository.save(pendingReview);
        return ResponseEntity.ok(pendingReview);
    }
}
