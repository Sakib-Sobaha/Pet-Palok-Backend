package dev.sabri.securityjwt.scopes.notifications;

import com.azure.core.annotation.Patch;
import dev.sabri.securityjwt.scopes.seller.Seller;
import dev.sabri.securityjwt.scopes.seller.SellerRepository;
import dev.sabri.securityjwt.scopes.user.User;
import dev.sabri.securityjwt.scopes.user.UserRepository;
import dev.sabri.securityjwt.scopes.vets.Vet;
import dev.sabri.securityjwt.scopes.vets.VetRepository;
import dev.sabri.securityjwt.scopes.vetvisit.appointments.Appointment;
import dev.sabri.securityjwt.scopes.vetvisit.appointments.AppointmentRepository;
import dev.sabri.securityjwt.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VetRepository vetRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
//    @Autowired
//    private UserRepository userRepository;

    @GetMapping("/user/fetch")
    public ResponseEntity<List<Notification>> getAll(Principal principal) {
        String email = principal.getName();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            String id = user.get().getId();
            List<Notification> list = notificationRepository.findByReceiver(id);

            // Sort notifications by unread status and timestamp
            list.sort(Comparator.comparing(Notification::getUnread, Comparator.reverseOrder()) // Unread first
                    .thenComparing(Notification::getTimestamp, Comparator.reverseOrder())); // Newest first

            return ResponseEntity.ok(list);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/user/clearAll")
    public ResponseEntity<List<Notification>> userClearAll(Principal principal) {
        String email = principal.getName();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            String id = user.get().getId();
            notificationRepository.deleteByReceiver(id);


            return ResponseEntity.ok(new ArrayList<>());
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/vet/fetch")
    public ResponseEntity<List<Notification>> getAllVet(Principal principal) {
        String email = principal.getName();
        Optional<Vet> vet = vetRepository.findByEmail(email);
        if (vet.isPresent()) {
            String id = vet.get().getId();
            return ResponseEntity.ok(notificationRepository.findByReceiver(id));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/vet/clearAll")
    public ResponseEntity<List<Notification>> vetClearAll(Principal principal) {
        String email = principal.getName();
        Optional<Vet> vet = vetRepository.findByEmail(email);
        if (vet.isPresent()) {
            String id = vet.get().getId();
            notificationRepository.deleteByReceiver(id);


            return ResponseEntity.ok(new ArrayList<>());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/seller/fetch")
    public ResponseEntity<List<Notification>> getAllSeller(Principal principal) {
        String email = principal.getName();
        Optional<Seller> user = sellerRepository.findByEmail(email);
        if (user.isPresent()) {
            String id = user.get().getId();
            return ResponseEntity.ok(notificationRepository.findByReceiver(id));
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/user/markAllAsRead")
    public ResponseEntity<Void> markAllAsRead(Principal principal) {
        System.out.println("received user mark all as read = noti");
        String email = principal.getName();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            String id = user.get().getId();

            // for each notification where receever == id, we set unread = false for them
            List<Notification> notifications = notificationRepository.findByReceiver(id);
            for (Notification notification : notifications) {
                notification.setUnread(false);
                notificationRepository.save(notification);
            }
            return ResponseEntity.ok().build();
        }
        System.out.println("User not found (NOTI)");
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/seller/markAllAsRead")
    public ResponseEntity<Void> markAllAsReadS(Principal principal) {
        String email = principal.getName();
        Optional<Seller> seller = sellerRepository.findByEmail(email);
        if (seller.isPresent()) {
            String id = seller.get().getId();

            // for each notification where receever == id, we set unread = false for them
            List<Notification> notifications = notificationRepository.findByReceiver(id);
            for (Notification notification : notifications) {
                notification.setUnread(false);
                notificationRepository.save(notification);
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/vet/markAllAsRead")
    public ResponseEntity<Void> markAllAsReadV(Principal principal) {
        System.out.println("Mark all as read vet noti");
        String email = principal.getName();
        Optional<Vet> vet = vetRepository.findByEmail(email);
        if (vet.isPresent()) {
            String id = vet.get().getId();

            // for each notification where receever == id, we set unread = false for them
            List<Notification> notifications = notificationRepository.findByReceiver(id);
            for (Notification notification : notifications) {
                notification.setUnread(false);
                notificationRepository.save(notification);
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    record MeetingLinkHolder(String appointmentId, String meetingLink) {
    }

    @PostMapping("/meetingStarted")
    public ResponseEntity<Void> meetingStarted(Principal principal, @RequestBody MeetingLinkHolder meetingLinkHolder) {
        System.out.println("Received request for meeting link send notification");
        String email = principal.getName();
        Optional<Appointment> appointment = appointmentRepository.findById(meetingLinkHolder.appointmentId);
        if (appointment.isPresent()) {

            System.out.println("appointment found");
            Notification notification = new Notification();
            notification.setType(NotificationType.APPOINTMENT_LINK_HOLDER);

            // Build the notification text
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Appointment meeting started. Room ID: ");

            String str = new String(meetingLinkHolder.meetingLink);
            String lastFourChars = "";

            // Check if the string is long enough to get the last 4 characters
            if (str.length() >= 4) {
                lastFourChars = str.substring(str.length() - 4);
                System.out.println("Last 4 characters: " + lastFourChars);
            } else {
                System.out.println("String is shorter than 4 characters: " + str);
                return ResponseEntity.badRequest().build();
            }

            stringBuilder.append(lastFourChars);





            notification.setText(stringBuilder.toString());
//
            notification.setTimestamp(new Date());
            notification.setReceiver(appointment.get().getUserId());
            System.out.println(meetingLinkHolder.meetingLink);
            notification.setMainContextId(meetingLinkHolder.meetingLink);
            notification.setUnread(true);
            notificationRepository.save(notification);

            return ResponseEntity.ok().build();

        }

        return ResponseEntity.notFound().build();
    }

}
