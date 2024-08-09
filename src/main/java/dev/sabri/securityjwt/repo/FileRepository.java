package dev.sabri.securityjwt.repo;

import dev.sabri.securityjwt.entity.File;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepository extends MongoRepository<File, Long> {

}
