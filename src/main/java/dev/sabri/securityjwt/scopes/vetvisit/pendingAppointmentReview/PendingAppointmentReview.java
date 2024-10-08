package dev.sabri.securityjwt.scopes.vetvisit.pendingAppointmentReview;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "_pendingAppointmentReviews")
public class PendingAppointmentReview {
    @Id
    private String id;
    private String userId;
    private String appointmentId;
}