package dev.sabri.securityjwt.scopes.vetvisit.appointments;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    public List<Appointment> findByUserId(String userId);
    public List<Appointment> findByVetId(String vetId);
}
