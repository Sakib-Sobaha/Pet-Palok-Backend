package dev.sabri.securityjwt.scopes.vetvisit.pendingAppointmentReview;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PendingAppointmentReviewRepository extends MongoRepository<PendingAppointmentReview, String> {
    public List<PendingAppointmentReview> findByUserId(String userId);
}
