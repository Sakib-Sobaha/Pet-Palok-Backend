package dev.sabri.securityjwt.scopes.vetvisit.appointmentRequests;


import dev.sabri.securityjwt.scopes.user.User;
import dev.sabri.securityjwt.scopes.user.UserRepository;
import dev.sabri.securityjwt.scopes.vets.Vet;
import dev.sabri.securityjwt.scopes.vets.VetRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/appointmentRequests")
public class AppointmentRequestController {
    @Autowired
    private AppointmentRequestRepository appointmentRequestRepository;
    @Autowired
    private VetRepository vetRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/vet/myRequests")
    public ResponseEntity<List<AppointmentRequest>> getMyRequests(Principal principal) {
        String email = principal.getName();

        Optional<Vet> vet = vetRepository.findByEmail(email);
        if(vet.isPresent()) {
            System.out.println("fetch app req vet");
            return ResponseEntity.ok(appointmentRequestRepository.findByVetId(vet.get().getId()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("user/myRequests")
    public ResponseEntity<List<AppointmentRequest>> getUserRequests(Principal principal) {
        String email = principal.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if(user.isPresent()) {
            System.out.println("fetch appointment req user");
            return ResponseEntity.ok(appointmentRequestRepository.findByUserId(user.get().getId()));
        }
        return ResponseEntity.notFound().build();
    }

    record NewAppointmentRequest(String vetId, String petId, boolean online, Date bookingTime, String description){}
    @PostMapping("/create")
    public ResponseEntity<AppointmentRequest> createAppointmentRequest(@RequestBody NewAppointmentRequest newAppointmentRequest, Principal principal) {
        String email = principal.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if(user.isPresent()) {
            String userId = user.get().getId();

            AppointmentRequest appointmentRequest = new AppointmentRequest();

            appointmentRequest.setUserId(userId);
            appointmentRequest.setVetId(newAppointmentRequest.vetId);
            appointmentRequest.setPetId(newAppointmentRequest.petId);
            appointmentRequest.setTimestamp(new Date());
            appointmentRequest.setDescription(newAppointmentRequest.description);
            appointmentRequest.setOnline(newAppointmentRequest.online);
            appointmentRequest.setBookingTime(newAppointmentRequest.bookingTime);
            appointmentRequestRepository.save(appointmentRequest);

            System.out.println("New Appointment Request created");
            return ResponseEntity.ok(appointmentRequest);
        }

        return ResponseEntity.notFound().build();
    }
}
