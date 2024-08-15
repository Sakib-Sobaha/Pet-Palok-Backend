package dev.sabri.securityjwt.scopes.seller;



import com.fasterxml.jackson.annotation.JsonIgnore;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private String slogan;
    private String passwd;
    private String phone;
    private String address;
    private String info;
    private LocalDateTime dob;
    Role role;
//    List<String> items = new ArrayList<String>();

    public Seller(String id, String email, String passwd, Role role) {
        this.id = id;
        this.email = email;
        this.passwd = passwd;
        this.role = role;

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




}