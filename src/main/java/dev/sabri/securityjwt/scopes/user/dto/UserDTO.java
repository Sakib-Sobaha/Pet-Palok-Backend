package dev.sabri.securityjwt.scopes.user.dto;


import dev.sabri.securityjwt.scopes.user.Gender;
import dev.sabri.securityjwt.scopes.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String address;
    private String password;
    private String postOffice;
    private String district;
    private String country;
    private Date dateOfBirth;
    private Integer ratingBuySellExchange;

    private Integer ratingPetKeeping;
    private Integer ratingVet;
    private String about;

    private String image;
    Gender gender;
    Role role;
}
