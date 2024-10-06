package dev.sabri.securityjwt.scopes.pets.lost;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Data
@Document(collection = "_lostPosts")
public class LostPost {
    @Id
    private String id;
    private String userId;
    private String petId;

    private String lastSeenDate;
    private String lastSeenLocation;
    private String description;
    private String reward;


}
