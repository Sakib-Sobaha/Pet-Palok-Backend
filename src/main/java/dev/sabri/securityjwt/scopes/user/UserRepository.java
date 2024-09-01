package dev.sabri.securityjwt.scopes.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    //    User findByEmail(String email);
    User findUserById(String id);

    Optional<User> findByVerificationCode(String verificationCode);

    @Transactional
    @Query("{ 'email' :  ?0 }")
    int enableUser(String email);
}
