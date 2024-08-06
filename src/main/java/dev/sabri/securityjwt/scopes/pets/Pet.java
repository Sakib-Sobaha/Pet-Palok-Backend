package dev.sabri.securityjwt.scopes.pets;


import dev.sabri.securityjwt.entity.File;
import dev.sabri.securityjwt.scopes.user.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_pet")
@Entity
public class Pet {

    @Id
    @SequenceGenerator(
            name = "pet_id_sequence",
            sequenceName = "pet_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "pet_id_sequence"
    )

    @Setter
    @Getter
    private Integer id;

    @Setter
    @Getter
    private String name;
    @Setter
    @Getter
    private int age;
    @Setter
    @Getter
    private String type;
    @Setter
    @Getter
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
    @Setter
    @Getter
    private String description;
    @Setter
    @Getter
    private LocalDateTime dob;
    @Setter
    @Getter
    Gender gender;
    @Setter
    @Getter
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
