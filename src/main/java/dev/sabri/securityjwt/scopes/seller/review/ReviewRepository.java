package dev.sabri.securityjwt.scopes.seller.review;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    public List<Review> findByMarketItemId(String marketItemId);
}
