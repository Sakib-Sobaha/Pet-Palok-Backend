package dev.sabri.securityjwt.scopes.vetvisit;


import dev.sabri.securityjwt.entity.File;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
@Setter
@Getter
@Document(collection = "vetVisit")

public class VetVisit {
    @Id



    private Integer vetId;
    private Integer userId;
    private Integer petId;


    private String description;

    private String medication;

    private LocalDate visitDate;

    private LocalDate visitEndDate;

    private boolean nextMeetUp;

    private File photo;

}
