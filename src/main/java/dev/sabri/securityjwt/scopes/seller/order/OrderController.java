package dev.sabri.securityjwt.scopes.seller.order;

import java.util.Date;
import dev.sabri.securityjwt.scopes.seller.MarketItems;
import dev.sabri.securityjwt.scopes.seller.MarketItemsRepository;
import dev.sabri.securityjwt.scopes.seller.Seller;
import dev.sabri.securityjwt.scopes.seller.SellerRepository;
import dev.sabri.securityjwt.scopes.user.User;
import dev.sabri.securityjwt.scopes.user.UserRepository;
import dev.sabri.securityjwt.scopes.user.cart.CartItem;
import dev.sabri.securityjwt.scopes.user.cart.CartItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/order")
@AllArgsConstructor

public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MarketItemsRepository marketItemsRepository;

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @GetMapping("/getOrdersByUserId")
    public ResponseEntity<Optional<List<Order>>> getOrdersByUserId(Principal principal) {
        String email = principal.getName();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            String userId = user.get().getId();

            List<Order> orders = orderRepository.findByUserId(userId);

            // Filter orders where any item in the order's itemCountMap belongs to the given sellerId


            return ResponseEntity.ok(Optional.of(orders));
        }
        else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/getOrdersBySellerId")
    public ResponseEntity<Optional<List<Order>>> getOrdersBySellerId(Principal principal) {
        String email = principal.getName();
        Optional<Seller> seller = sellerRepository.findByEmail(email);
        if (seller.isPresent()) {
            String sellerId = seller.get().getId();
            // Fetch all orders
            List<Order> allOrders = orderRepository.findAll();

            // Filter orders where any item in the order's itemCountMap belongs to the given sellerId
            List<Order> filteredOrders = allOrders.stream()
                    .filter(order -> order.getItemCountMap().keySet().stream()
                            .anyMatch(itemId -> {
                                Optional<MarketItems> marketItem = marketItemsRepository.findById(itemId);
                                return marketItem.isPresent() && sellerId.equals(marketItem.get().getSellerId());
                            }))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Optional.of(filteredOrders));
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    record NewOrder (List<String> cartItemIds, String name, String email, String phone, String alternatePhone, String address, String postOffice, String district, String country, Float deliveryFee){

    }

    @PostMapping("/createOrder")
    public ResponseEntity<Order> createOrder(@RequestBody NewOrder newOrder, Principal principal) {
        System.out.println("Received create order request");
        System.out.println(newOrder.toString());
        // Retrieve the logged-in user's email
        String email = principal.getName();

        // Fetch the user based on the email
        User user = userRepository.findByEmail(email).orElse(null);

        if(user == null){
            System.out.println("user not found");
            return ResponseEntity.notFound().build();
        }
        else
        {
            // check valid uid or not
            List<String> cartItemIdsCheck = newOrder.cartItemIds;
            CartItem cartItem = cartItemRepository.findById(cartItemIdsCheck.get(0)).orElse(null);
            if(cartItem == null){
                System.out.println("cartItem not found");
                return ResponseEntity.notFound().build();
            }else if(!cartItem.getUserId().equals(user.getId())){
                System.out.println("userId: " + user.getId());
                System.out.println("CartItem UserID: " + cartItem.getUserId());
                System.out.println("cannot place order for other user huh :3");
                return ResponseEntity.badRequest().build();
            }else {
                System.out.println("All ok. initiating new order");
                Order order = new Order();

                order.setUserId(user.getId());
//                order.setCartItemIds(newOrder.cartItemIds);
                order.setName(newOrder.name);
                order.setEmail(newOrder.email);
                order.setPhone(newOrder.phone);
                order.setAlternatePhone(newOrder.alternatePhone);
                order.setAddress(newOrder.address);
                order.setPostOffice(newOrder.postOffice);
                order.setDistrict(newOrder.district);
                order.setCountry(newOrder.country);
                order.setDeliveryFee(newOrder.deliveryFee);

                // set the current time as ordered time and date
                Date date = new Date(new java.util.Date().getTime());
                System.out.println("ordering on : "+date);
                order.setOrderedOn(date);

                HashMap<String, Integer> itemCountMap = new HashMap<>();
                for(String id : newOrder.cartItemIds){
                    CartItem cartItemTemp = cartItemRepository.findById(id).orElse(null);
                    if(cartItemTemp == null){
                        System.out.println("cart item not found");
                        return ResponseEntity.notFound().build();
                    }
                    else
                    {
                        String marketItemId = cartItemTemp.getItemId();
                        Integer count = cartItemTemp.getCount();

                        itemCountMap.put(marketItemId, count);

                    }
                }

                order.setItemCountMap(itemCountMap);

                orderRepository.save(order);

                System.out.println("order created: "+ order);
                // time to delete the cartItems

//                cartItemRepository.deleteAllById(newOrder.cartItemIds);

                return ResponseEntity.ok(order);
            }

        }
    }

}
