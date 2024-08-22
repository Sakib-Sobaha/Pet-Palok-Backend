package dev.sabri.securityjwt.scopes.foundPost;

import dev.sabri.securityjwt.scopes.pets.PetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(collection = "_found_post")

public class FoundPost {
    @Id
    private String id;
    private String authorId; // user id who posted
    private PetType type;
    private Date date_found;
    private String location_found;
    private String description;
    private String [] images;
    private Condition condition;
    private String contactNo;
    private String location_return;
    private String appearance_breed;
    private ConfirmationStatus confirmationStatus;


    public FoundPost(String authorId, PetType type, Date date_found, String location_found, String description, String[] images, Condition condition, String contactNo, String location_return, String appearance_breed, ConfirmationStatus confirmationStatus) {
        this.authorId = authorId;
        this.type = type;
        this.date_found = date_found;
        this.location_found = location_found;
        this.description = description;
        this.images = images;
        this.condition = condition;
        this.contactNo = contactNo;
        this.location_return = location_return;
        this.appearance_breed = appearance_breed;
        this.confirmationStatus = confirmationStatus;
    }

    @Override
    public String toString() {
        return
                this.authorId + "\t" + this.type + "\t" + this.date_found + "\t" + this.location_found + "\t"
                + this.description + "\t" + this.images + "\t" + this.condition + "\t"
        ;
    }
}


