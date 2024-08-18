package dev.sabri.securityjwt.scopes.pets.dto;

import dev.sabri.securityjwt.scopes.user.Gender;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class NewPetRequest {
    public String name;
    public LocalDate date;
    public String type;
    public String breed;
    public String description;
    public Gender gender;
    public boolean vetVerified;
    public List<String> images;

    public NewPetRequest() {

    }

    public NewPetRequest(String name, LocalDateTime dateTime, String type, String breed, String description, Gender gender, boolean vetVerified, List<String> images) {
        this.name = name;
        this.date = dateTime.toLocalDate();
        this.type = type;
        this.breed = breed;
        this.description = description;
        this.gender = gender;
        this.vetVerified = vetVerified;
        this.images = images;
    }
}
