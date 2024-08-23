package dev.sabri.securityjwt.scopes.user;

import dev.sabri.securityjwt.scopes.user.*;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Optional;


public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    //    User findByEmail(String email);
    User findUserById(String id);
}
