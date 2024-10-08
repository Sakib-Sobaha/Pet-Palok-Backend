package dev.sabri.securityjwt.scopes.vetvisit;


import dev.sabri.securityjwt.entity.File;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@Document(collection = "vetVisit")

public class VetVisit {
    @Id
    private String id;
    private String vetId;
    private String userId;
    private String petId;


    private String description;

    private String medication;

    private Date visitDate;


    private boolean nextMeetUp;

    private File photo;

}
