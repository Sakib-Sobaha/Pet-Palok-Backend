package dev.sabri.securityjwt.scopes.seller.marketItemComments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "_marketItemComments")
public class MarketItemComment {
    @Id
    private String id;
    private String marketItemId;
    private String parent; // parent null mane eta main comment, parent null na mane eita reply
    private String text;
    private String commenterId;
    private Date timeStamp;
    private String userType;
    private boolean anonymous;
}
