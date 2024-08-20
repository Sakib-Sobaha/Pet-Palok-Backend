package dev.sabri.securityjwt.scopes.pets.dto;

import dev.sabri.securityjwt.scopes.user.Gender;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class NewPetRequest {
    public String ownerId;
    public String name;
    public Date dob;
    public String type;
    public String breed;
    public String description;
    public Gender gender;
    public boolean vetVerified;
    public List<String> images;

    public NewPetRequest() {

    }

    public NewPetRequest(String ownerId, String name, Date dob, String type, String breed, String description, Gender gender, boolean vetVerified, List<String> images) {
        this.name = name;
        this.dob = dob;
        this.type = type;
        this.breed = breed;
        this.description = description;
        this.gender = gender;
        this.vetVerified = vetVerified;
        this.images = images;
    }
}
