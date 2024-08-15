package dev.sabri.securityjwt.scopes.vetvisit;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@Data
@Document(collection = "_appointment")

public class appointmentRequest {
    @Id
    private String id;
    private String vetId;
    private String userId;
    private Date request_date;
    private Date booking_date;
    private LocalDateTime booking_time;
    private String description;
    private String status;
    private String medium;

    @Indexed(unique = true)
    private String appointmentId;


}
