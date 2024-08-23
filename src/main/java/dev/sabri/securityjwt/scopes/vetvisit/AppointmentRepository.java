package dev.sabri.securityjwt.scopes.vetvisit;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

@EnableMongoRepositories(basePackageClasses = AppointmentRepository.class)
public interface AppointmentRepository extends MongoRepository<AppointmentRequest, String> {
    List<AppointmentRequest> findByUserId(String userId);
    List<AppointmentRequest> findByUserIdAndAppointmentId(String userId, String appointmentId);
    List<AppointmentRequest> findByAppointmentId(String appointmentId);
    List<AppointmentRequest> findByVetId(String vetId);
    List<AppointmentRequest> findByVetIdAndAppointmentId(String vetId, String appointmentId);

    List<AppointmentRequest> findByVetIdAndUserId(String vetId, String userId);
}
