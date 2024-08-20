package dev.sabri.securityjwt.scopes.pets;


import dev.sabri.securityjwt.entity.File;
import dev.sabri.securityjwt.scopes.user.Gender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(collection = "_pet")

public class Pet {

    @Id
//    @SequenceGenerator(
//            name = "pet_id_sequence",
//            sequenceName = "pet_id_sequence",
//            allocationSize = 1
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "pet_id_sequence"
//    )


    private String id;

    private String ownerId;


    private String name;

//    private int age;

    private String type;

    private String breed;

    private String description;

    private Date dob;

    Gender gender;

    private boolean vetVerified;


    private List<String> images;

    public Pet(String name, String type, String breed, String description, Gender gender, boolean vetVerified) {
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.description = description;
        this.gender = gender;
        this.vetVerified = vetVerified;

    }



    public Pet(String ownerId, String name, Date dob,String type, String breed, String description, Gender gender, boolean vetVerified, List<String> images) {
        this.ownerId = ownerId;
        this.name = name;
        this.dob = dob;
        this.type = type;
        this.breed = breed;
        this.description = description;
        this.gender = gender;
        this.vetVerified = vetVerified;
        this.images = images;

    }


    @Override
    public String toString() {
        return "Pet{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", breed='" + breed + '\'' +
                ", dob=" + dob +
                ", gender='" + gender + '\'' +
                ", description='" + description + '\'' +
                '}';

    }



}
