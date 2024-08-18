package dev.sabri.securityjwt.scopes.seller;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface SellerRepository extends MongoRepository<Seller, String> {
    Optional<Seller> findByEmail(String email);

    Seller findSellerById(String id);
}
