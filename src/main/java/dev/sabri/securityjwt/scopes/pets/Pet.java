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


    private Integer id;


    private String name;

    private int age;

    private String type;

    private String breed;
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "video_id")
//    @Setter
//    @Getter
//    private File photos;
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "video_id")
//    @Setter
//    @Getter
//    private File videos;

    private String description;

    private LocalDateTime dob;

    Gender gender;

    private boolean vetVerified;

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
