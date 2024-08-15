package dev.sabri.securityjwt.entity;

import dev.sabri.securityjwt.entity.File;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepository extends MongoRepository<File, Long> {

}
