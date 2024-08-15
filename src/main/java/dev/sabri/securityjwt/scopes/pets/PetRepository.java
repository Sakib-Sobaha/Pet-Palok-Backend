package dev.sabri.securityjwt.scopes.pets;

import dev.sabri.securityjwt.scopes.pets.Pet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;
import java.util.Optional;

@EnableMongoRepositories(basePackageClasses = PetRepository.class)
public interface PetRepository extends MongoRepository<Pet, String> {
    Optional<Pet> findByName(String name);
    Pet findPetById(String id);

    Optional<List<Pet>> findPetByOwnerId(String ownerId);

}
