package dev.sabri.securityjwt.scopes.community;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommunityRepository extends MongoRepository<Community,String> {

}
