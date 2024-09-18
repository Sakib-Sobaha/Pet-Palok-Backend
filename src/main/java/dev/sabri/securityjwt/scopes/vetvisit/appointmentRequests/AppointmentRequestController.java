package dev.sabri.securityjwt.scopes.vetvisit.appointmentRequests;


import dev.sabri.securityjwt.scopes.notifications.Notification;
import dev.sabri.securityjwt.scopes.notifications.NotificationRepository;
import dev.sabri.securityjwt.scopes.notifications.NotificationType;
import dev.sabri.securityjwt.scopes.pets.PetRepository;
import dev.sabri.securityjwt.scopes.user.User;
import dev.sabri.securityjwt.scopes.user.UserRepository;
import dev.sabri.securityjwt.scopes.vets.Vet;
import dev.sabri.securityjwt.scopes.vets.VetRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private NotificationRepository notificationRepository;

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
            appointmentRequest.setState(AppointmentRequestState.PENDING);
            appointmentRequestRepository.save(appointmentRequest);

            Notification notification = new Notification();
            notification.setType(NotificationType.NEW_APPOINTMENT_REQUEST);

            // Build the notification text
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("New appointment request received from ");
            stringBuilder.append(user.get().getFirstname() + " " + user.get().getLastname() + " for ");
            stringBuilder.append("pet: "+petRepository.findPetById(appointmentRequest.getPetId()).getName());

            // Set the notification text with item details
            notification.setText(stringBuilder.toString());

            notification.setTimestamp(new Date());
            notification.setReceiver(appointmentRequest.getVetId());
            notification.setMainContextId(null);
            notification.setUnread(true);
            notificationRepository.save(notification);

            System.out.println("New Appointment Request created");
            return ResponseEntity.ok(appointmentRequest);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<AppointmentRequest> rejectAppointmentRequest(@PathVariable String id, Principal principal) {
        AppointmentRequest appointmentRequest = appointmentRequestRepository.findById(id).orElse(null);
        String email = principal.getName();
        Optional<Vet> vet = vetRepository.findByEmail(email);
        if(vet.isPresent() && appointmentRequest != null) {
            String vetId = vet.get().getId();
            if(appointmentRequest.getVetId().equals(vetId)) {
                appointmentRequest.setState(AppointmentRequestState.REJECTED);

                Notification notification = new Notification();
                notification.setType(NotificationType.APPOINTMENT_REJECTED);

                // Build the notification text
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Appointment Rejected with ");
                stringBuilder.append(vet.get().getFirstname() + " " + vet.get().getLastname() + " for your");
                stringBuilder.append("pet: "+petRepository.findPetById(appointmentRequest.getPetId()).getName());

                // Set the notification text with item details
                notification.setText(stringBuilder.toString());

                notification.setTimestamp(new Date());
                notification.setReceiver(appointmentRequest.getUserId());
                notification.setMainContextId(null);
                notification.setUnread(true);
                notificationRepository.save(notification);
                appointmentRequestRepository.deleteById(appointmentRequest.getId());
                return ResponseEntity.ok(appointmentRequest);
            }
            else
                return ResponseEntity.notFound().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<AppointmentRequest> cancelAppointmentRequest(@PathVariable String id, Principal principal) {
        AppointmentRequest appointmentRequest = appointmentRequestRepository.findById(id).orElse(null);
        String email = principal.getName();
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent() && appointmentRequest != null) {
            String userId = user.get().getId();
            if(appointmentRequest.getUserId().equals(userId)) {
                appointmentRequest.setState(AppointmentRequestState.CANCELLED);

                Notification notification = new Notification();
                notification.setType(NotificationType.APPOINTMENT_CANCELLED);

                // Build the notification text
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Appointment cancelled with ");
                stringBuilder.append(user.get().getFirstname() + " " + user.get().getLastname() + " for ");
                stringBuilder.append("pet: "+petRepository.findPetById(appointmentRequest.getPetId()).getName());

                // Set the notification text with item details
                notification.setText(stringBuilder.toString());

                notification.setTimestamp(new Date());
                notification.setReceiver(appointmentRequest.getVetId());
                notification.setMainContextId(null);
                notification.setUnread(true);
                notificationRepository.save(notification);
                appointmentRequestRepository.deleteById(appointmentRequest.getId());
                return ResponseEntity.ok(appointmentRequest);
            }
            else
                return ResponseEntity.notFound().build();
        }

        return ResponseEntity.notFound().build();
    }

    record AcceptReqBody(Date timestamp){}
    @PostMapping("/accept/{id}")
    public ResponseEntity<AppointmentRequest> acceptAppointmentRequest(@PathVariable String id, Principal principal,@RequestBody AcceptReqBody body) {
        System.out.println("Accept vet appointment req");
        System.out.println("timestamp: " + body.timestamp);
        if(body.timestamp == null)
            return ResponseEntity.notFound().build();

        AppointmentRequest appointmentRequest = appointmentRequestRepository.findById(id).orElse(null);
        String email = principal.getName();
        Optional<Vet> vet = vetRepository.findByEmail(email);
        if(vet.isPresent() && appointmentRequest != null) {
            String vetId = vet.get().getId();
            if(appointmentRequest.getVetId().equals(vetId)) {
                appointmentRequest.setBookingTime(body.timestamp);
                appointmentRequest.setState(AppointmentRequestState.WAITING_FOR_PAYMENT);
                appointmentRequestRepository.save(appointmentRequest);
                System.out.println("Appointment Request accepted");

                Notification notification = new Notification();
                notification.setType(NotificationType.APPOINTMENT_REQUEST_ACCEPTED);

                // Build the notification text
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Appointment accepted by ");
                stringBuilder.append(vet.get().getFirstname() + " " + vet.get().getLastname() + " for ");
                stringBuilder.append("pet: "+petRepository.findPetById(appointmentRequest.getPetId()).getName());

                // Set the notification text with item details
                notification.setText(stringBuilder.toString());

                notification.setTimestamp(new Date());
                notification.setReceiver(appointmentRequest.getUserId());
                notification.setMainContextId(appointmentRequest.getId());
                notification.setUnread(true);
                notificationRepository.save(notification);

                return ResponseEntity.ok(appointmentRequest);
            }
            else
                return ResponseEntity.notFound().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/confirmPayment/{id}")
    public ResponseEntity<AppointmentRequest> confirmPayment(@PathVariable String id, Principal principal) {
        String email = principal.getName();
        Optional<Vet> vet = vetRepository.findByEmail(email);
        if(!vet.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Optional<AppointmentRequest> appointmentRequest = appointmentRequestRepository.findById(id);

        if(appointmentRequest.isPresent() && appointmentRequest.get().getVetId().equals(vet.get().getId())) {
            if(appointmentRequest.get().getVetId().equals(vet.get().getId())) {
                AppointmentRequest newAppointmentRequest = appointmentRequest.get();
                newAppointmentRequest.setState(AppointmentRequestState.CONFIRMED);
                appointmentRequestRepository.save(newAppointmentRequest);

                Notification notification = new Notification();
                notification.setType(NotificationType.APPOINTMENT_REQUEST_ACCEPTED);

                // Build the notification text
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Appointment payment confirmed by ");
                stringBuilder.append(vet.get().getFirstname() + " " + vet.get().getLastname() + " for ");
                stringBuilder.append("pet: "+petRepository.findPetById(newAppointmentRequest.getPetId()).getName());

                // Set the notification text with item details
                notification.setText(stringBuilder.toString());

                notification.setTimestamp(new Date());
                notification.setReceiver(newAppointmentRequest.getUserId());
                notification.setMainContextId(newAppointmentRequest.getId());
                notification.setUnread(true);
                notificationRepository.save(notification);

                return ResponseEntity.ok(newAppointmentRequest);
            }
        }
        return ResponseEntity.notFound().build();
    }

    record TimeChangeRequest(Date newTime){}
    @PostMapping("/requestTimeChange/{id}")
    public ResponseEntity<AppointmentRequest> requestTimeChange(@RequestBody TimeChangeRequest timeChangeReq, Principal principal, @PathVariable String id) {
        String email = principal.getName();
        System.out.println("time change req received");
        System.out.println(timeChangeReq.newTime);
        Optional<User> user = userRepository.findByEmail(email);
        Optional<AppointmentRequest> appointmentRequest = appointmentRequestRepository.findById(id);
        if(appointmentRequest.isPresent()) {
            if(user.isPresent()) {
                if(appointmentRequest.get().getUserId().equals(user.get().getId())) {
                    AppointmentRequest ar = appointmentRequest.get();
                    ar.setBookingTime(timeChangeReq.newTime);
                    ar.setState(AppointmentRequestState.TIME_CHANGE_REQUESTED);

                    Notification notification = new Notification();
                    notification.setType(NotificationType.APPOINTMENT_TIME_CHANGE_REQUESTED);

                    // Build the notification text
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Appointment time change requested by ");
                    stringBuilder.append(user.get().getFirstname() + " " + user.get().getLastname() + " for ");
                    stringBuilder.append("pet: "+petRepository.findPetById(ar.getPetId()).getName());

                    // Set the notification text with item details
                    notification.setText(stringBuilder.toString());

                    notification.setTimestamp(new Date());
                    notification.setReceiver(ar.getVetId());
                    notification.setMainContextId(ar.getId());
                    notification.setUnread(true);
                    notificationRepository.save(notification);

                    appointmentRequestRepository.save(ar);
                    return ResponseEntity.ok(ar);
                }
            }
//            Optional<Vet> vet = vetRepository.findByEmail(email);
//            if(vet.isPresent()) {
//                if(appointmentRequest.get().getVetId().equals(vet.get().getId())) {
//                    AppointmentRequest ar = appointmentRequest.get();
//                    ar.setTimestamp(timeChangeReq.newTime);
//                    ar.setState(AppointmentRequestState.TIME_CHANGE_REQUESTED);
//                    appointmentRequestRepository.save(ar);
//                    return ResponseEntity.ok(ar);
//                }
//            }
        }
        return ResponseEntity.notFound().build();
    }
}
