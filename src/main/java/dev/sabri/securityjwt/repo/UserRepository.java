package dev.sabri.securityjwt.repo;

import dev.sabri.securityjwt.scopes.user.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
//    User findByEmail(String email);
    User findUserById(Integer id);
}
