package dev.sabri.securityjwt.scopes.seller;



import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.sabri.securityjwt.scopes.user.Gender;
import dev.sabri.securityjwt.scopes.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "_seller")

public class Seller implements UserDetails {

    @Id
//    @SequenceGenerator(
//            name = "seller_id_sequence",
//            sequenceName = "seller_id_sequence",
//            allocationSize = 1
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "seller_id_sequence"
//    )


    private String id;
    private String name;
    @Indexed(unique = true)
    private String email;
    private String storeName;
    private String storeAddress;
    private String slogan;
    private String password;
    private String phone;
    private String address;
    private String postOffice;
    private String district;
    private String country;
    private String about;
    private Date dob;
    private String image;
    Role role;
    Gender gender;
//    List<String> items = new ArrayList<String>();

    public Seller(String id, String email, String passwd, Role role) {
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




}
