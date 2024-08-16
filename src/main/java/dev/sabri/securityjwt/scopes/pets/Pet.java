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

    private int age;

    private String type;

    private String breed;

    private String description;

    private LocalDateTime dob;

    Gender gender;

    private boolean vetVerified;


    private List<String> imageUrls;

    public Pet(String name, int age, String type, String breed, String description, Gender gender, boolean vetVerified) {
        this.name = name;
        this.age = age;
        this.type = type;
        this.breed = breed;
        this.description = description;
        this.gender = gender;
        this.vetVerified = vetVerified;

    }

    @Override
    public String toString() {
        return "Pet{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", type='" + type + '\'' +
                ", breed='" + breed + '\'' +
                ", dob=" + dob +
                ", gender='" + gender + '\'' +
                ", description='" + description + '\'' +
                '}';

    }



}
