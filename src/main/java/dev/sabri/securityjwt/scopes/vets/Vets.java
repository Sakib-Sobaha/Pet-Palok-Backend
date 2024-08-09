package dev.sabri.securityjwt.scopes.vets;

import dev.sabri.securityjwt.scopes.user.Gender;
import dev.sabri.securityjwt.scopes.user.Role;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Collection;

@Document(collection = "_vet")

public class Vets implements UserDetails {
    @Id


    @Setter
    @Getter
    private Integer id;
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
    private String about;
    @Setter
    @Getter
    private String clinic;

    Gender gender;

    Role role;

    public Vets() {
    }

    public Vets(Integer id, String firstname, String lastname, String email, String passwd, Role role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
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
