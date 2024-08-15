package dev.sabri.securityjwt.scopes.vets;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VetRepository extends MongoRepository<Vet, String> {
    Optional<Vet> findByEmail(String email);
}
