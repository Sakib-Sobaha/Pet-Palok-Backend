package dev.sabri.securityjwt.scopes.vetvisit.appointmentReview;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document (collection = "_appointmentReviews")
public class AppointmentReview {
    @Id
    private String id;

    private String vetId;
    private String userId;
    private Integer vetRating;
    private String vetReview;
    private Date timestamp;
    private boolean anonymous;
}