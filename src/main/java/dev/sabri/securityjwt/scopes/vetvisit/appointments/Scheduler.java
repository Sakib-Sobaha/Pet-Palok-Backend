package dev.sabri.securityjwt.scopes.vetvisit.appointments;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Scheduled(fixedRate = 5000) // Run every 5 seconds
    public void printMessage() {
//        System.out.println("Scheduled task executed every 5 seconds");
    }
}
