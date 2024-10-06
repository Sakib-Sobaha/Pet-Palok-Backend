package dev.sabri.securityjwt.scopes.pets.found;


import dev.sabri.securityjwt.model.Status;
import dev.sabri.securityjwt.scopes.pets.PetType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Data
@Document(collection = "_foundPosts")

public class FoundPost {
    @Id
    private String id;
    private String authorId;
    private String title;
    private Date date_found;
    private PetType pet_type;
    private String location_found;
    private String description;
    private List<String> images;
    private String condition;
    private String contactNo;
    private String location_return;
    private String appearance_breed;
    private ConfirmationStatus confirmation_status;


}
