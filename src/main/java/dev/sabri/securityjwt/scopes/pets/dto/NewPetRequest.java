package dev.sabri.securityjwt.scopes.pets.dto;

import dev.sabri.securityjwt.scopes.user.Gender;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class NewPetRequest {
    public String name;
    public int age;
    public String type;
    public String breed;
    public String description;
    public Gender gender;
    public boolean vetVerified;
    public List<MultipartFile> images;

    public NewPetRequest() {

    }

    public NewPetRequest(String name, int age, String type, String breed, String description, Gender gender, boolean vetVerified, List<MultipartFile> images) {
        this.name = name;
        this.age = age;
        this.type = type;
        this.breed = breed;
        this.description = description;
        this.gender = gender;
        this.vetVerified = vetVerified;
        this.images = images;
    }
}
