package dev.sabri.securityjwt.scopes.vetvisit;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user/appointment")
public class AppointmentController {
    private final AppointmentRepository appointmentRepository;


    public AppointmentController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping("/getAppointments")
    public List<AppointmentRequest> getAppointments() {
        return appointmentRepository.findAll();
    }

    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<AppointmentRequest>> getAppointmentsByUserId(@PathVariable String userId) {
        List<AppointmentRequest> appointments = appointmentRepository.findByUserId(userId);
        if (appointments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/byVet/{vetId}")
    public ResponseEntity<List<AppointmentRequest>> getAppointmentsByVetId(@PathVariable String vetId) {
        List<AppointmentRequest> appointments = appointmentRepository.findByVetId(vetId);
        if (appointments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/byVetAndUser")
    public ResponseEntity<List<AppointmentRequest>> getAppointmentsByVetIdAndUserId(
            @RequestParam String vetId, @RequestParam String userId) {
        List<AppointmentRequest> appointments = appointmentRepository.findByVetIdAndUserId(vetId, userId);
        if (appointments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appointments);
    }




}
