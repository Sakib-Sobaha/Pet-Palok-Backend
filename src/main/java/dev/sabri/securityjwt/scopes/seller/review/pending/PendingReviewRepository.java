package dev.sabri.securityjwt.scopes.seller.review.pending;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PendingReviewRepository extends MongoRepository<PendingReview,String> {
    List<PendingReview> findByUserId(String userId);

//    boolean deleteByI(String marketItemId);
}
