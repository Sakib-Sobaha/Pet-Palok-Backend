package dev.sabri.securityjwt.scopes.seller;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "_seller")
@Entity
public class Seller {

    @Id
    @SequenceGenerator(
            name = "seller_id_sequence",
            sequenceName = "seller_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seller_id_sequence"
    )


    private Integer id;
    private String name;
    @Column(unique = true)
    private String email;
    private String storeName;
    private String slogan;
    private String passwd;
    private String phone;
    private String address;
    private String info;
    private LocalDateTime dob;
//    List<String> items = new ArrayList<String>();




}
