package dev.sabri.securityjwt.scopes.seller.order;

import dev.sabri.securityjwt.scopes.user.cart.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository  extends MongoRepository<Order, String> {
    List<Order> findByUserId(String userId);

}
