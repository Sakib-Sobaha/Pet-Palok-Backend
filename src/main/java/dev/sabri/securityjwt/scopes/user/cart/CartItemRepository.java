package dev.sabri.securityjwt.scopes.user.cart;

import dev.sabri.securityjwt.scopes.seller.MarketItems;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends MongoRepository<CartItem, String> {
    Optional<List<CartItem>> findByUserId(String userId);
    List<CartItem> findByItemIdAndUserId(String marketItemId, String userId);
}
