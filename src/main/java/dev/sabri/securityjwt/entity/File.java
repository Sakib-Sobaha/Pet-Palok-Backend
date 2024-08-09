package dev.sabri.securityjwt.entity;



import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;


@Setter
@Getter
public class File {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

//    @Lob
    private byte[] data;

    // Getters and setters
    // Constructors
}
