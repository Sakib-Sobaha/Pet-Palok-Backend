package dev.sabri.securityjwt.scopes.vetvisit.appointments;

import dev.sabri.securityjwt.scopes.notifications.Notification;
import dev.sabri.securityjwt.scopes.notifications.NotificationRepository;
import dev.sabri.securityjwt.scopes.notifications.NotificationType;
import dev.sabri.securityjwt.scopes.pets.Pet;
import dev.sabri.securityjwt.scopes.pets.PetRepository;
import dev.sabri.securityjwt.scopes.user.User;
import dev.sabri.securityjwt.scopes.user.UserRepository;
import dev.sabri.securityjwt.scopes.vets.Vet;
import dev.sabri.securityjwt.scopes.vets.VetRepository;
import dev.sabri.securityjwt.scopes.vetvisit.appointmentRequests.AppointmentRequestRepository;
import lombok.AllArgsConstructor;
import org.bson.io.BsonOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VetRepository vetRepository;
    @Autowired
    private AppointmentRequestRepository appointmentRequestRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private NotificationRepository notificationRepository;


    @GetMapping("/user/fetchAll")
    public ResponseEntity<List<Appointment>> getAllAppointmentsUser(Principal principal) {
        String username = principal.getName();
        Optional<User> user = userRepository.findByEmail(username);

        if (user.isPresent()) {
            return ResponseEntity.ok(appointmentRepository.findByUserId(user.get().getId()));
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/vet/fetchAll")
    public ResponseEntity<List<Appointment>> getAllAppointmentsVet(Principal principal) {
        String username = principal.getName();
        Optional<Vet> vet = vetRepository.findByEmail(username);

        if (vet.isPresent()) {
            return ResponseEntity.ok(appointmentRepository.findByVetId(vet.get().getId()));
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable String appointmentId) {
        System.out.println("Fetch appointment by id: " + appointmentId);
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);

        if (appointment.isPresent()) {
            return ResponseEntity.ok(appointment.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/getAvailableTimeSlots/{vetId}/{day}")
    public ResponseEntity<List<Date>> getAvailableTimeSlots(@PathVariable String vetId, @PathVariable String day) {
        // Parse the input day to a Date object
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date requestedDate;
        try {
            requestedDate = dateFormat.parse(day);
        } catch (ParseException e) {
            // If the date format is incorrect, return a bad request response
            return ResponseEntity.badRequest().build();
        }

        // Fetch all appointments for the specified vet and day
        List<Appointment> appointments = appointmentRepository.findByVetId(vetId);
        List<Appointment> dayAppointments = new ArrayList<>();

        // Filter appointments for the given day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(requestedDate);
        int targetYear = calendar.get(Calendar.YEAR);
        int targetMonth = calendar.get(Calendar.MONTH);
        int targetDay = calendar.get(Calendar.DAY_OF_MONTH);

        for (Appointment appointment : appointments) {
            calendar.setTime(appointment.getBookingTime());
            if (calendar.get(Calendar.YEAR) == targetYear &&
                    calendar.get(Calendar.MONTH) == targetMonth &&
                    calendar.get(Calendar.DAY_OF_MONTH) == targetDay) {
                dayAppointments.add(appointment);
            }
        }

        // Create a boolean array to track occupied hours
        boolean[] occupiedSlots = new boolean[24];

        // Mark the occupied hours
        for (Appointment appointment : dayAppointments) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(appointment.getBookingTime());
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            occupiedSlots[hour] = true;
        }

        // Find available time slots and create Date objects for them
        List<Date> availableSlots = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(requestedDate); // Set to the start of the day

        for (int i = 0; i < 24; i++) {
            if (!occupiedSlots[i]) {
                cal.set(Calendar.HOUR_OF_DAY, i);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                availableSlots.add(cal.getTime());
            }
        }

        return ResponseEntity.ok(availableSlots);
    }

    @Scheduled(fixedRate = 5 * 60 * 1000) // Runs every hour
    public void updateState() {
        System.out.println("updateState called");

        // Get the current time
        Date currentTime = new Date();
        System.out.println("Current time: " + currentTime);

        // Retrieve all appointments from the database
        List<Appointment> appointments = appointmentRepository.findAll();

        for (Appointment appointment : appointments) {
            Date bookingTime = appointment.getBookingTime();
            System.out.println("Booking time for appointment ID " + appointment.getId() + ": " + bookingTime);

            // Add 1 hour to the booking time
            Calendar cal = Calendar.getInstance();
            cal.setTime(bookingTime);
            cal.add(Calendar.HOUR_OF_DAY, 1);
            Date bookingPlusOneHour = cal.getTime();
            System.out.println("Booking + 1 hour: " + bookingPlusOneHour);

            // Check the appointment state
            AppointmentState previousState = appointment.getState();

            if (currentTime.after(bookingTime) && currentTime.before(bookingPlusOneHour)) {
                appointment.setState(AppointmentState.ONGOING);
            } else if (currentTime.after(bookingPlusOneHour)) {
                appointment.setState(AppointmentState.COMPLETED);
            } else if (currentTime.before(bookingTime)) {
                appointment.setState(AppointmentState.SCHEDULED);
            }

            Vet vet = vetRepository.findById(appointment.getVetId()).orElse(null);
            User user = userRepository.findById(appointment.getUserId()).orElse(null);
            Pet pet = petRepository.findById(appointment.getPetId()).orElse(null);

            // Check if the state has changed and print a message if it has
            if (previousState != appointment.getState()) {
                System.out.println("State updated to: " + appointment.getState());

                if (appointment.getState() == AppointmentState.ONGOING) {
                    Notification notification = new Notification();
                    notification.setType(NotificationType.APPOINTMENT_STARTED);

                    // Build the notification text
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Appointment started with ");
                    stringBuilder.append(vet.getFirstname() + " " + vet.getLastname() + " for ");
                    stringBuilder.append("pet: " + pet.getName());

                    // Set the notification text with item details
                    notification.setText(stringBuilder.toString());

                    notification.setTimestamp(new Date());
                    notification.setReceiver(appointment.getUserId());
                    notification.setMainContextId(appointment.getId());
                    notification.setUnread(true);
                    notificationRepository.save(notification);

                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Appointment started with ");
                    stringBuilder.append(user.getFirstname() + " " + user.getLastname() + " for ");
                    stringBuilder.append("pet: " + pet.getName());

                    // Set the notification text with item details
                    notification.setText(stringBuilder.toString());

                    notification.setTimestamp(new Date());
                    notification.setReceiver(appointment.getVetId());
                    notification.setMainContextId(appointment.getId());
                    notification.setUnread(true);
                    notificationRepository.save(notification);
                } else if (appointment.getState() == AppointmentState.COMPLETED) {
                    Notification notification = new Notification();
                    notification.setType(NotificationType.APPOINTMENT_STARTED);

                    // Build the notification text
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Appointment completed with ");
                    stringBuilder.append(vet.getFirstname() + " " + vet.getLastname() + " for ");
                    stringBuilder.append("pet: " + pet.getName());

                    // Set the notification text with item details
                    notification.setText(stringBuilder.toString());

                    notification.setTimestamp(new Date());
                    notification.setReceiver(appointment.getUserId());
                    notification.setMainContextId(appointment.getId());
                    notification.setUnread(true);
                    notificationRepository.save(notification);

                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Appointment completed with ");
                    stringBuilder.append(user.getFirstname() + " " + user.getLastname() + " for ");
                    stringBuilder.append("pet: " + pet.getName());

                    // Set the notification text with item details
                    notification.setText(stringBuilder.toString());

                    notification.setTimestamp(new Date());
                    notification.setReceiver(appointment.getVetId());
                    notification.setMainContextId(appointment.getId());
                    notification.setUnread(true);
                    notificationRepository.save(notification);
                }
            }


            // Save the updated appointment state
            appointmentRepository.save(appointment);
            System.out.println("Appointment ID: " + appointment.getId() + " updated to state: " + appointment.getState());

        }

        System.out.println("Appointment states updated");
    }


//    @Scheduled(fixedRate = 5000) // Runs every 5 seconds
//    public void printMessage() {
//        System.out.println("Hello! This message prints every 5 seconds.");
//    }


    record NewAppUpdate(String prescription, HashMap<String, String> medications, List<String> tests,
                        String prescriptionFile) {
    }

    @PostMapping("/update/{appointmentId}")
    public ResponseEntity<Appointment> updateAppointment(Principal principal, @PathVariable String appointmentId, @RequestBody NewAppUpdate newAppUpdate) {
        System.out.println("Update appointment: " + appointmentId);
        System.out.println("prescription: " + newAppUpdate.prescription);
        System.out.println("medications: " + newAppUpdate.medications);
        System.out.println("tests: " + newAppUpdate.tests);
        System.out.println("prescriptionFile: " + newAppUpdate.prescriptionFile);

        String email = principal.getName();
        Optional<Vet> vet = vetRepository.findByEmail(email);
        if (vet.isPresent()) {
            Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
            if (appointment.isPresent()) {
                Appointment temp = appointment.get();
                temp.setTests(newAppUpdate.tests);
                temp.setMedications(newAppUpdate.medications);
                temp.setPrescription(newAppUpdate.prescription);
                temp.setPrescriptionFile(newAppUpdate.prescriptionFile);
                appointmentRepository.save(temp);
                return ResponseEntity.ok(temp);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/completed")
    public ResponseEntity<List<Appointment>> getCompletedAppointments(Principal principal) {
        System.out.println("fetch completed appointments");
        String username = principal.getName();
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            List<Appointment> appointments = appointmentRepository.findByUserId(user.get().getId());
            List<Appointment> completedAppointments = new ArrayList<>();

            for (Appointment appointment : appointments) {
                if (appointment.getState().equals(AppointmentState.COMPLETED)) {
                    completedAppointments.add(appointment);
                    System.out.println(appointment.getState());
                }
            }

            return ResponseEntity.ok(completedAppointments);
        } else {
            Optional<Vet> vet = vetRepository.findByEmail(username);
            if (vet.isPresent()) {
                List<Appointment> appointments = appointmentRepository.findByVetId(vet.get().getId());
                List<Appointment> completedAppointments = new ArrayList<>();

                for (Appointment appointment : appointments) {
                    if (appointment.getState().equals(AppointmentState.COMPLETED)) {
                        completedAppointments.add(appointment);
                    }
                }

                return ResponseEntity.ok(completedAppointments);
            }
        }

        return ResponseEntity.notFound().build();

    }

    @GetMapping("/petTimeline/{petId}")
    public ResponseEntity<List<Map<String, Object>>> getPetTimeline(@PathVariable String petId) {
        Optional<Pet> pet = petRepository.findById(petId);
        if (pet.isPresent()) {
            List<Appointment> appointments = appointmentRepository.findByPetId(pet.get().getId());

            // Create a list to hold the formatted appointment events
            List<Map<String, Object>> petTimeline = new ArrayList<>();

            // Map each appointment to the desired format
            for (int i = 0; i < appointments.size(); i++) {
                Appointment appointment = appointments.get(i);
                Map<String, Object> event = new HashMap<>();
                event.put("id", i + 1); // Serial number starting from 1
                event.put("date", appointment.getBookingTime().toLocaleString()); // Assuming bookingTime is a LocalDateTime
                event.put("event", appointment.getDescription());

                petTimeline.add(event);
            }

            return ResponseEntity.ok(petTimeline); // Return the timeline as response
        }

        return ResponseEntity.notFound().build(); // Pet not found
    }

}
