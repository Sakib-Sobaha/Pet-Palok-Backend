package dev.sabri.securityjwt.scopes.seller.review;


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
@Document (collection = "_reviews")
public class Review {
    @Id
    private String id;

    private String marketItemId;
    private String userId;
    private Integer itemRating;
    private Integer sellerRating;
    private String itemReview;
    private String sellerReview;
    private Date timestamp;
    private boolean anonymous;
}
