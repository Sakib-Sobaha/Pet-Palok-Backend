package dev.sabri.securityjwt.scopes.vetvisit;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

@EnableMongoRepositories(basePackageClasses = AppointmentRepository.class)
public interface AppointmentRepository extends MongoRepository<appointmentRequest, String> {
    List<appointmentRequest> findByUserId(String userId);
    List<appointmentRequest> findByUserIdAndAppointmentId(String userId, String appointmentId);
    List<appointmentRequest> findByAppointmentId(String appointmentId);
    List<appointmentRequest> findByVetId(String vetId);
    List<appointmentRequest> findByVetIdAndAppointmentId(String vetId, String appointmentId);

    List<appointmentRequest> findByVetIdAndUserId(String vetId, String userId);
}
