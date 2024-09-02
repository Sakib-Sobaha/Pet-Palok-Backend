package dev.sabri.securityjwt.scopes.seller;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MarketItemsRepository extends MongoRepository<MarketItems, String> {
    List<MarketItems> findBySellerId(String sellerId);
}
