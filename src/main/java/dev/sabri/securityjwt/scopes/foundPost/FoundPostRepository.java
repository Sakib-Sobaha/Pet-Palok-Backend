package dev.sabri.securityjwt.scopes.foundPost;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;
import java.util.Optional;


public interface FoundPostRepository {
    Optional<FoundPost> findById(String id);
    Optional<List<FoundPost>> findAll();
}
