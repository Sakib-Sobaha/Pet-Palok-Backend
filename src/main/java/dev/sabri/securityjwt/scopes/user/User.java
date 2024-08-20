package dev.sabri.securityjwt.scopes.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;
import java.util.Collection;
@Setter
@Getter
@Data
@Document(collection = "_user")

public class User implements UserDetails {
    @Id
//    @SequenceGenerator(
//            name = "user_id_sequence",
//            sequenceName = "user_id_sequence",
//            allocationSize = 1
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "user_id_sequence"
//    )


    private String id;

    private String firstname;

    private String lastname;

    @Indexed(unique = true)
    private String email;

    private String phoneNumber;

    private String passwd;

    private String address;

    private String postOffice;

    private String district;

    private String country;

    private Date dob;

    private Integer ratingBuySellExchange;

    private Integer ratingPetKeeping;
    private Integer ratingVet;
    private String about;

    private String image;

    Gender gender;

    Role role = Role.USER;

    public User() {
    }

    public User(String id, String firstname, String lastname, String email, String passwd, Role role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.passwd = passwd;
        this.role = role;
    }

    public User(String id,String firstname, String lastname, String passwd) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.passwd = passwd;
    }

    public User(String id, String email, String passwd, Role role) {
        this.id = id;
        this.email = email;
        this.passwd = passwd;
        this.role = role;

    }





//    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(role.name());
        //return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return passwd;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstname + '\'' +
                ", lastName='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phoneNumber + '\'' +
                ", password='" + passwd + '\'' +
                ", address='" + address + '\'' +
                ", postOffice='" + postOffice + '\'' +
                ", district='" + district + '\'' +
                ", country='" + country + '\'' +
                ", DOB='" +  + '\'' +
                ", gender='" + gender + '\'' +
                ", about='" + about + '\'' +
                ", ratingBuySellExch=" + ratingBuySellExchange +
                ", ratingPetKeeping=" + ratingPetKeeping +
                ", ratingVet=" + ratingVet +
                '}';
    }


}
