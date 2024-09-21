package dev.sabri.securityjwt.scopes.vetvisit.appointments;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Setter
@Getter
@Data
@Document(collection = "_appointments")

public class Appointment {
    @Id
    private String id;
    private String userId;
    private String vetId;
    private String petId;

    private Date bookingTime;
    private String description;
    private boolean online;

    private String prescription;
    private HashMap<String, String> medications;
    private List<String> tests;
    private AppointmentState state;

}
