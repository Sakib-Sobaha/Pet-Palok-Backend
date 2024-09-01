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

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Collection;
@Setter
@Getter
@Data
@Document(collection = "_user")

public class User implements UserDetails {
    @Id


    private String id;

    private String firstname;

    private String lastname;

    @Indexed(unique = true)
    private String email;

    private String phoneNumber;

    private String password;

    private String address;

    private String postOffice;

    private String district;

    private String country;

    private Date dob;

    private double ratingBuySellExchange;

    private double ratingPetKeeping;
    private double ratingVet;
    private String about;

    private String image;

    private String status;

    private boolean enabled = true;

    private String verificationCode;

    private LocalDateTime verificationCodeExpiresAt;

    Gender gender;

    Role role = Role.USER;

    public User() {
    }

    public User(String id, String firstname, String lastname, String email, String passwd, Role role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = passwd;
        this.role = role;
    }

    public User(String id,String firstname, String lastname, String passwd) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = passwd;
    }

    public User(String id, String email, String passwd, Role role) {
        this.id = id;
        this.email = email;
        this.password = passwd;
        this.role = role;

    }





    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(role.name());
        //return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

//    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return enabled;
    }


    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstname + '\'' +
                ", lastName='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
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
