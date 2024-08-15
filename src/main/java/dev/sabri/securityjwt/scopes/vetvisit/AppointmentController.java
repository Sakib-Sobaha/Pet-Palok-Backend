package dev.sabri.securityjwt.scopes.vetvisit;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users/appointment")
public class AppointmentController {
    private final AppointmentRepository appointmentRepository;


    public AppointmentController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping
    public List<appointmentRequest> getAppointments() {
        return appointmentRepository.findAll();
    }

    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<appointmentRequest>> getAppointmentsByUserId(@PathVariable String userId) {
        List<appointmentRequest> appointments = appointmentRepository.findByUserId(userId);
        if (appointments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/byVet/{vetId}")
    public ResponseEntity<List<appointmentRequest>> getAppointmentsByVetId(@PathVariable String vetId) {
        List<appointmentRequest> appointments = appointmentRepository.findByVetId(vetId);
        if (appointments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/byVetAndUser")
    public ResponseEntity<List<appointmentRequest>> getAppointmentsByVetIdAndUserId(
            @RequestParam String vetId, @RequestParam String userId) {
        List<appointmentRequest> appointments = appointmentRepository.findByVetIdAndUserId(vetId, userId);
        if (appointments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appointments);
    }




}
