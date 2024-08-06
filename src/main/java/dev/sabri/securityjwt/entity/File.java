package dev.sabri.securityjwt.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String type;
    @Getter
    @Setter
    @Lob
    private byte[] data;

    // Getters and setters
    // Constructors
}
