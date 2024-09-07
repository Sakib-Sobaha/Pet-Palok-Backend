package dev.sabri.securityjwt.scopes.vetvisit.appointmentRequests;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Setter
@Getter
@Data
@Document(collection = "_appointmentRequests")

public class AppointmentRequest {
    @Id
    private String id;
    private String vetId;
    private String userId;
    private String petId;
    private Date timestamp;
    private Date bookingTime;
    private String description;
    private boolean online;
}
