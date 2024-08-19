package dev.sabri.securityjwt.scopes.vets;

import dev.sabri.securityjwt.scopes.user.Gender;
import dev.sabri.securityjwt.scopes.user.Role;

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
import java.util.Collection;

@Setter
@Getter
@Data
@Document(collection = "_vet")

public class Vet implements UserDetails {
    @Id



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

    private LocalDateTime dateOfBirth;

    private Integer ratingBuySellExchange;

    private String about;

    private String clinic;

    Gender gender;

    Role role;

    public Vet() {
    }

    public Vet(String id, String firstname, String lastname, String email, String passwd, Role role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.passwd = passwd;
        this.role = role;
    }

    public Vet(String id, String email, String passwd, Role role) {
        this.id = id;
        this.email = email;
        this.passwd = passwd;
        this.role = role;
    }




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
                ", clinic=" + clinic +
                ", ratingVet=" +
                '}';
    }


}
