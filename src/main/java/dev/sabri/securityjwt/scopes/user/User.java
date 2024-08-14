package dev.sabri.securityjwt.scopes.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.mongodb.core.index.Indexed;

import java.lang.annotation.Documented;
import java.util.Collection;
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

    @Setter
    @Getter
    private String id;
    @Setter
    @Getter
    private String firstname;
    @Setter
    @Getter
    private String lastname;
    @Setter
    @Getter
    @Indexed(unique = true)
    private String email;
    @Setter
    @Getter
    private String phoneNumber;
    @Setter
    private String passwd;
    @Setter
    @Getter
    private String address;
    @Setter
    @Getter
    private String postOffice;
    @Setter
    @Getter
    private String district;
    @Setter
    @Getter
    private String country;
    @Setter
    @Getter
    private String dateOfBirth;
    @Setter
    @Getter
    private Integer ratingBuySellExchange;
    @Setter
    @Getter
    private Integer ratingPetKeeping;
    private Integer ratingVet;
    private String about;

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



    @JsonIgnore
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
