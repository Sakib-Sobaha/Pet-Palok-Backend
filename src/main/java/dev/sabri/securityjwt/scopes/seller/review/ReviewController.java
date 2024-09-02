package dev.sabri.securityjwt.scopes.seller.review;

import dev.sabri.securityjwt.scopes.seller.MarketItems;
import dev.sabri.securityjwt.scopes.seller.MarketItemsRepository;
import dev.sabri.securityjwt.scopes.seller.Seller;
import dev.sabri.securityjwt.scopes.seller.SellerRepository;
import dev.sabri.securityjwt.scopes.seller.review.pending.PendingReviewRepository;
import dev.sabri.securityjwt.scopes.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import dev.sabri.securityjwt.scopes.user.User;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {
    @Autowired
    private MarketItemsRepository marketItemsRepository;

    @Autowired
    private PendingReviewRepository pendingReviewRepository;

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellerRepository sellerRepository;

    @GetMapping("/getByItem/{marketItemId}")
    public ResponseEntity<List<Review>> getReviewByItemId(@PathVariable String marketItemId) {
        Optional<MarketItems> item = marketItemsRepository.findById(marketItemId);

        if (item.isPresent()) {
            return ResponseEntity.ok(reviewRepository.findByMarketItemId(marketItemId));
        }

        return ResponseEntity.notFound().build();
    }

    record NewReview(String marketItemId,
                     Integer itemRating,
                     Integer sellerRating,
                     String itemReview,
                     String sellerReview,
                     boolean anonymous,
                     String pendingReviewId
    ) {
    }

    @PostMapping("/create")
    public ResponseEntity<Review> createReview(@RequestBody NewReview newReview, Principal principal) {
        String email = principal.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            Review review = new Review();

            review.setUserId(user.get().getId());
            review.setMarketItemId(newReview.marketItemId);
            review.setItemRating(newReview.itemRating);
            review.setSellerRating(newReview.sellerRating);
            review.setItemReview(newReview.itemReview);
            review.setSellerReview(newReview.sellerReview);
            review.setTimestamp(new Date());
            review.setAnonymous(newReview.anonymous);

            // Save the new review
            reviewRepository.save(review);

            // Calculate the new average rating for the market item
            updateMarketItemRating(newReview.marketItemId);

            // Get the sellerId from MarketItem and update seller's rating
            Optional<MarketItems> marketItemOptional = marketItemsRepository.findById(newReview.marketItemId);
            if (marketItemOptional.isPresent()) {
                String sellerId = marketItemOptional.get().getSellerId();
                updateSellerRating(sellerId);
                System.out.println("pending review id: " + newReview.pendingReviewId);
                pendingReviewRepository.deleteById(newReview.pendingReviewId);
                return ResponseEntity.ok(review);

            }

            return ResponseEntity.notFound().build();


        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Method to update the market item rating
    private void updateMarketItemRating(String marketItemId) {
        List<Review> reviews = reviewRepository.findByMarketItemId(marketItemId);
        double averageRating = reviews.stream()
                .mapToInt(Review::getItemRating)
                .average()
                .orElse(0.0);

        // Fetch market item and update its rating
        Optional<MarketItems> marketItemOptional = marketItemsRepository.findById(marketItemId);
        if (marketItemOptional.isPresent()) {
            MarketItems marketItem = marketItemOptional.get();
            marketItem.setRating((float) averageRating);
            marketItemsRepository.save(marketItem);
        }
    }

    // Method to update the seller rating
    private void updateSellerRating(String sellerId) {
        List<MarketItems> marketItems = marketItemsRepository.findBySellerId(sellerId);
        if (marketItems.isEmpty()) return;

        // Calculate the average seller rating based on all associated market items
        List<Review> allReviews = new ArrayList<>();
        for (MarketItems item : marketItems) {
            allReviews.addAll(reviewRepository.findByMarketItemId(item.getId()));
        }

        double averageSellerRating = allReviews.stream()
                .mapToInt(Review::getSellerRating)
                .average()
                .orElse(0.0);

        // Fetch seller and update their rating
        Optional<Seller> sellerOptional = sellerRepository.findById(sellerId);
        if (sellerOptional.isPresent()) {
            Seller seller = sellerOptional.get();
            seller.setRating((float) averageSellerRating);
            sellerRepository.save(seller);
        }
    }

}
