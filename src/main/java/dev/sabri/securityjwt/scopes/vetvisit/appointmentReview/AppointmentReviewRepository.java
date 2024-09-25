package dev.sabri.securityjwt.scopes.vetvisit.appointmentReview;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppointmentReviewRepository extends MongoRepository<AppointmentReview, String> {
    public List<AppointmentReview> findByVetId(String vetId);

}
