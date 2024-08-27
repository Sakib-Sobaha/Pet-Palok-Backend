package dev.sabri.securityjwt.scopes.user.cart;


import dev.sabri.securityjwt.scopes.user.User;
import dev.sabri.securityjwt.scopes.user.UserController;
import dev.sabri.securityjwt.scopes.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/cart")
@AllArgsConstructor

public class CartItemController {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getCartItems")
    public ResponseEntity<Optional<List<CartItem>>> getCartItemsByUserId(Principal principal) {
        String email = principal.getName();

        // Fetch the user based on the email
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            // If the user is not found, return a 404 Not Found response
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartItemRepository.findByUserId(user.getId()));
    }


    record NewCartItem(String userId,  String itemId, Integer count) {

    }

    @PostMapping("/incrementCount/{cartItemId}")
    public ResponseEntity<Optional<CartItem>> incrementCount(@PathVariable String cartItemId) {
        System.out.println("Increment count rcv: " + cartItemId);
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.isPresent()) {
            // set the count to n + 1
            cartItem.get().setCount(cartItem.get().getCount() + 1);
            cartItemRepository.save(cartItem.get());

            System.out.println("count updated: " + cartItem.get().getCount());
            return ResponseEntity.ok(cartItem);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/decrementCount/{cartItemId}")
    public ResponseEntity<Optional<CartItem>> decrementCount(@PathVariable String cartItemId) {
        System.out.println("receive decrement count: " + cartItemId);
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.isPresent()) {

            cartItem.get().setCount(cartItem.get().getCount() - 1);
            cartItemRepository.save(cartItem.get());
            System.out.println("count updated: " + cartItem.get().getCount());
            return ResponseEntity.ok(cartItem);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/removeItem/{itemId}")
    public ResponseEntity<String> removeItem(@PathVariable String itemId) {

        Optional<CartItem> cartItem = cartItemRepository.findById(itemId);

        if (cartItem.isPresent()) {
            String itemString = cartItem.toString();
            cartItemRepository.deleteById(itemId);
            return ResponseEntity.ok("Item deleted successfully:"+itemString);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/clearCart")
    public ResponseEntity<String> clearCart(Principal principal) {

        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        assert user != null;
        Optional<List<CartItem>> list =  cartItemRepository.findByUserId(user.getId());
        if (list.isPresent()) {
            for(CartItem cartItem : list.get()) {
                System.out.println("deleting cartItem: " + cartItem.getId());
                cartItemRepository.deleteById(cartItem.getId());

            }
            System.out.println("cart items deleted successfully");
            return ResponseEntity.ok("Cart cleared successfully");
        }
        return ResponseEntity.notFound().build();
    }




    @PostMapping("/addToCart")
    public ResponseEntity<Optional<CartItem>> addToCart(@RequestBody NewCartItem newCartItem, Principal principal) {
        System.out.println("Received request for add to cart:");

        // Retrieve the logged-in user's email
        String email = principal.getName();

        // Fetch the user based on the email
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            // If the user is not found, return a 404 Not Found response
            return ResponseEntity.notFound().build();
        }

        // already cart e amn kisu ase kina
        List<CartItem> check = cartItemRepository.findByItemIdAndUserId(newCartItem.itemId, user.getId());
//        if (check != null) {
            if(check.size() == 1)
            {
                System.out.println("check size is 1");
                CartItem cartItem = check.get(0);
                cartItem.setCount(Math.min(cartItem.getCount() + newCartItem.count, 5));

                cartItemRepository.save(cartItem);
                System.out.println("count updated: " + cartItem.getCount());
                return ResponseEntity.ok(Optional.of(cartItem));
            }
            else if(check.size() >1){
                System.out.println("check size is " + check.size());
                System.out.println("items: ");
                for (CartItem cartItem : check) {
                    System.out.println(cartItem);
                }
                return ResponseEntity.ok(Optional.of(check.get(0)));
//            }
            // max 5ta order kora jabe

        }

        // Create a new CartItem and set the userId from the authenticated user
        CartItem cartItem = new CartItem();
        cartItem.setUserId(user.getId()); // Automatically set userId from the logged-in user
        cartItem.setItemId(newCartItem.itemId); // Assuming getItemId() method exists in NewCartItem
        cartItem.setCount(newCartItem.count); // Assuming getCount() method exists in NewCartItem

        // Save the new cart item to the repository
        cartItemRepository.save(cartItem);
        System.out.println("Item added to cart: " + cartItem);

        // Return the saved cart item wrapped in an Optional
        return ResponseEntity.ok(Optional.of(cartItem));
    }
}
