package dev.sabri.securityjwt.scopes.vetvisit.appointmentRequests;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppointmentRequestRepository extends MongoRepository<AppointmentRequest, String> {
    public List<AppointmentRequest> findByUserId(String userId);
    public List<AppointmentRequest> findByVetId(String vetId);
}
