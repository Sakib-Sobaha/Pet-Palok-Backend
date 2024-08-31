package dev.sabri.securityjwt.scopes.seller.marketItemComments;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MarketItemCommentRepository extends MongoRepository<MarketItemComment, String> {
    List<MarketItemComment> findByMarketItemId(String marketItemId);
    List<MarketItemComment> findByParent(String parent);
}
