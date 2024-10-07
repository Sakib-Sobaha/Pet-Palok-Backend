package dev.sabri.securityjwt.scopes.vetvisit.appointmentRequests;


import dev.sabri.securityjwt.scopes.pets.Pet;
import dev.sabri.securityjwt.scopes.user.User;
import dev.sabri.securityjwt.scopes.vets.Vet;
import dev.sabri.securityjwt.scopes.vetvisit.appointments.Appointment;
import dev.sabri.securityjwt.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record AppointmentRequestService(EmailService emailService) {

    public void sendAppointmentRequestStatusEmail(Optional<User> user, Optional<Vet> vet, Optional<Pet> pet, AppointmentRequest appointmentRequest, String update) {
        String subject = "Appointment Request Updates";
        String userName = user.get().getFirstname() + " " + user.get().getLastname();
        String vetName = vet.get().getFirstname() + " " + vet.get().getLastname();
        String petName = pet.get().getName();
        String appointmentRequestId = appointmentRequest.getId();

        String logoUrl = "https://github.com/user-attachments/assets/5db94c4d-497d-4b2d-ad86-f218c9e74a00";
        String gifUrl = "https://github.com/user-attachments/assets/81cf3a21-5558-4f5a-a344-515a41fb5c26";
        String htmlMessage = "<html>"
                + "<head>"
                + "<style>"
                // Define keyframes for the background color animation
                + "@keyframes bg-animation {"
                + "0% { background-color: #f5f5f5; }"
                + "50% { background-color: #e0e0e0; }"
                + "100% { background-color: #f5f5f5; }"
                + "}"
                // Apply the background animation to the body
                + "body { animation: bg-animation 5s infinite; padding: 20px; font-family: 'Cascadia Code'; }"
                // Style for the container to be square with purple background
                + ".email-container {"
                + "width: auto; height: auto; background-color: purple; border: 2px solid #007bff;"
                + "border-radius: 20px; padding: 20px; margin: 0 auto; box-shadow: 0 0 15px rgba(0,0,0,0.2);"
                + "display: flex; flex-direction: column; justify-content: center; align-items: center;"
                + "text-align: center;"
                + "}"
                // Style for the verification box with black background, white text
                + ".appointment-box {"
                + "background-color: black; padding: 20px; width: 300px; height: auto; border-radius: 20px;"
                + "display: flex; justify-content: center; align-items: center; text-align: center;"
                + "box-shadow: 0 0 10px rgba(0,0,0,0.1); color: white; margin: 20px auto;"
                + "}"
                // Style for the verification code text
                + ".appointment-code {"
                + "font-size: 18px; font-weight: bold; color: #007bff; font-family: 'Cascadia Code';"
                + "}"
                // Style for the 'Verification Code' label
                + ".appointment-label {"
                + "color: red; font-size: 20px; font-weight: bold; font-family: 'Cascadia Code';"
                + "}"
                // Style for the pet message
                + ".pet-message {"
                + "font-size: 24px; text-align: center; margin-top: 20px; color: #000000; font-family: 'Cascadia Code';"
                + "}"
                // Style for the GIF
                + ".pet-gif {"
                + "display: block; margin: 20px auto; max-width: 400px;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"email-container\">"
                + "<img src=\"" + logoUrl + "\" alt=\"Company Logo\" style=\"display:block; margin: 0 auto; max-width: 400px;\">"
                + "<h2 style=\"color: #fff; font-weight: bold; font-family: 'Cascadia Code'\">Welcome to our Pet-Palok app!  Thank you, " + userName +  " for making an appointment request with" + vetName + "for pet" + petName + "!</h2>"
                + "<p style=\"font-size: 24px; color: white; font-weight: bold; font-family: 'Cascadia Code'\">Here is the Updated Status of your appointment request: </p>"
                // The animated verification box with white text and red label in square shape
                + "<div class=\"appointment-box\">"
                + "<div>"
                + "<h3 class=\"appointment-label\">" + update + "Please follow the website to stay updated.</h3>"
                + "<p class=\"appointment-code\">" + "Your appointment request id: " + appointmentRequestId + "</p>"
                + "</div>"
                + "</div>"
                // Add the message and GIF below the verification box
                + "<p class=\"pet-message\">We are happy since you love pets!</p>"
                + "<img src=\"" + gifUrl + "\" alt=\"Happy Pet GIF\" class=\"pet-gif\"/>"
                + "</div>" // Close email-container
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.get().getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }

    }
}
