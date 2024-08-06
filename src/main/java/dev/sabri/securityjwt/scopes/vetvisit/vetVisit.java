package dev.sabri.securityjwt.scopes.vetvisit;


import dev.sabri.securityjwt.entity.File;
import dev.sabri.securityjwt.scopes.user.Gender;
import dev.sabri.securityjwt.scopes.user.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "vetVisit")
@Entity
public class vetVisit {
    @Id
    @SequenceGenerator(
            name = "vetVisit_id_sequence",
            sequenceName = "vetVisit_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "vetVisit_id_sequence"
    )

    @Setter
    @Getter
    private Integer vetId;
    private Integer userId;
    private Integer petId;

    @Setter
    @Getter
    private String description;
    @Setter
    @Getter
    private String medication;
    @Setter
    @Getter
    private LocalDate visitDate;
    @Setter
    @Getter
    private LocalDate visitEndDate;
    @Setter
    @Getter
    private boolean nextMeetUp;
    @OneToOne
    private File photo;

}
