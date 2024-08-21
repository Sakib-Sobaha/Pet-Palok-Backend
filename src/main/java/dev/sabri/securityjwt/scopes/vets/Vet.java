package dev.sabri.securityjwt.scopes.vets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.sabri.securityjwt.scopes.user.Gender;
import dev.sabri.securityjwt.scopes.user.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Collection;
import java.util.Date;

@Setter
@Getter
@Data
@Document(collection = "_vet")
@AllArgsConstructor
public class Vet implements UserDetails {
    @Id



    private String id;

    private String firstname;

    private String lastname;

    @Indexed(unique = true)
    private String email;

    private String phone;
    private String password;

    private String clinic_name;

    private String clinic_address;

    private String address;

    private String postOffice;

    private String district;

    private String country;

    private Date dob;

    private double rating_vetvisit;

    private String about;



    private String image;

    Gender gender;

    Role role;

    public Vet() {
    }

    public Vet(String id, String firstname, String lastname, String email, String passwd, Role role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = passwd;
        this.role = role;
    }

    public Vet(String id, String email, String passwd, Role role) {
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

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", address='" + clinic_address + '\'' +
                ", postOffice='" + postOffice + '\'' +
                ", district='" + district + '\'' +
                ", country='" + country + '\'' +
                ", DOB='" +  + '\'' +
                ", gender='" + gender + '\'' +
                ", about='" + about + '\'' +
                ", ratingBuySellExch=" + rating_vetvisit +
                ", clinic=" + clinic_name +
                ", ratingVet=" +
                '}';
    }


}
