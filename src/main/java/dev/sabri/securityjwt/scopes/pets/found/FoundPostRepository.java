package dev.sabri.securityjwt.scopes.pets.found;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;
import java.util.Optional;

@EnableMongoRepositories(basePackageClasses = FoundPostRepository.class)
public interface FoundPostRepository extends MongoRepository<FoundPost, String> {
    Optional<FoundPost> findById(String id);
    List<FoundPost> findAll();

}
