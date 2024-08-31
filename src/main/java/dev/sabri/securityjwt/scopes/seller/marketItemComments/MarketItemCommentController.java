package dev.sabri.securityjwt.scopes.seller.marketItemComments;

import dev.sabri.securityjwt.scopes.seller.MarketItems;
import dev.sabri.securityjwt.scopes.seller.MarketItemsRepository;
import dev.sabri.securityjwt.scopes.seller.Seller;
import dev.sabri.securityjwt.scopes.seller.SellerRepository;
import dev.sabri.securityjwt.scopes.user.User;
import dev.sabri.securityjwt.scopes.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/marketplace/item/comment")
public class MarketItemCommentController {
    @Autowired
    private MarketItemCommentRepository marketItemCommentRepository;

    @Autowired
    private MarketItemsRepository marketItemsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellerRepository sellerRepository;


    @GetMapping("/fetchCommentByItem/{itemID}")
    public ResponseEntity<List<MarketItemComment>> findById(@PathVariable String itemID) {
        System.out.println("Fetching all comments for marketItemID: " + itemID);
        Optional<MarketItems> marketItem = marketItemsRepository.findById(itemID);

        if (marketItem.isPresent()) {
            List<MarketItemComment> comments = marketItemCommentRepository.findByMarketItemId(itemID);

            // Filter comments where parent is null
            List<MarketItemComment> filteredComments = comments.stream()
                    .filter(comment -> comment.getParent() == null)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(filteredComments);
        }

        return ResponseEntity.notFound().build();
    }


    @GetMapping("/{commentId}")
    public ResponseEntity<List<MarketItemComment>> findByCommentId(@PathVariable String commentId) {
        System.out.println("Fetching nested comments for commentID: " + commentId);
        Optional<MarketItemComment> comment = marketItemCommentRepository.findById(commentId);
        if(comment.isPresent()) {
            return ResponseEntity.ok(marketItemCommentRepository.findByParent(commentId));
        }

        return ResponseEntity.notFound().build();
    }

    record NewComment(String text, String marketItemId, String userType, boolean anonymous) {

    }

    @PostMapping("/create")
    public ResponseEntity<MarketItemComment> createComment(@RequestBody NewComment newComment, Principal principal) {
        System.out.println("Creating new comment for marketItemID: " + newComment.marketItemId);
        String email = principal.getName();
        if(newComment.userType.equalsIgnoreCase("user"))
        {
            Optional<User> user = userRepository.findByEmail(email);

            if(user.isPresent()) {
                MarketItemComment comment = new MarketItemComment();
                comment.setParent(null);
                comment.setText(newComment.text);
                comment.setMarketItemId(newComment.marketItemId);
                comment.setCommenterId(user.get().getId());
                comment.setTimeStamp(new Date());
                comment.setUserType(newComment.userType.toLowerCase());
                comment.setAnonymous(newComment.anonymous);

                marketItemCommentRepository.save(comment);
                System.out.println("new comment created (user)");
                return ResponseEntity.ok(comment);
            }
            else
                return ResponseEntity.notFound().build();
        }
        else if(newComment.userType.equalsIgnoreCase("seller"))
        {
            Optional<Seller> seller = sellerRepository.findByEmail(email);
            if(seller.isPresent()) {
                MarketItemComment comment = new MarketItemComment();
                comment.setParent(null);
                comment.setText(newComment.text);
                comment.setMarketItemId(newComment.marketItemId);
                comment.setCommenterId(seller.get().getId());
                comment.setTimeStamp(new Date());
                comment.setUserType(newComment.userType.toLowerCase());
                comment.setAnonymous(newComment.anonymous);

                marketItemCommentRepository.save(comment);
                System.out.println("new comment created (seller)");
                return ResponseEntity.ok(comment);

            }
            else
                return ResponseEntity.notFound().build();
        }
        else
            return ResponseEntity.notFound().build();
        // vet ra comment korte parbena, dekhteo parbena market item :3 hehee
    }

    record NewReply(String text, String marketItemId, String userType, String parent, boolean anonymous) {

    }

    @PostMapping("/reply")
    public ResponseEntity<MarketItemComment> reply(@RequestBody NewReply newReply, Principal principal) {
        System.out.println("Creating new comment for marketItemID: " + newReply.marketItemId);
        String email = principal.getName();
        if(newReply.userType.equalsIgnoreCase("user"))
        {
            Optional<User> user = userRepository.findByEmail(email);

            if(user.isPresent()) {
                MarketItemComment comment = new MarketItemComment();
                comment.setParent(newReply.parent);
                comment.setText(newReply.text);
                comment.setMarketItemId(newReply.marketItemId);
                comment.setCommenterId(user.get().getId());
                comment.setTimeStamp(new Date());
                comment.setUserType(newReply.userType.toLowerCase());
                comment.setAnonymous(newReply.anonymous);

                marketItemCommentRepository.save(comment);
                System.out.println("new comment created (user)");
                return ResponseEntity.ok(comment);
            }
            else
                return ResponseEntity.notFound().build();
        }
        else if(newReply.userType.equalsIgnoreCase("seller"))
        {
            Optional<Seller> seller = sellerRepository.findByEmail(email);
            if(seller.isPresent()) {
                MarketItemComment comment = new MarketItemComment();
                comment.setParent(newReply.parent);
                comment.setText(newReply.text);
                comment.setMarketItemId(newReply.marketItemId);
                comment.setCommenterId(seller.get().getId());
                comment.setTimeStamp(new Date());
                comment.setUserType(newReply.userType.toLowerCase());
                comment.setAnonymous(newReply.anonymous);

                marketItemCommentRepository.save(comment);
                System.out.println("new comment created (seller)");
                return ResponseEntity.ok(comment);

            }
            else
                return ResponseEntity.notFound().build();
        }
        else
            return ResponseEntity.notFound().build();
        // vet ra comment korte parbena, dekhteo parbena market item :3 hehee
    }

    @GetMapping("/getReplies/{commentId}")
    public ResponseEntity<List<MarketItemComment>> getReplies(@PathVariable String commentId) {
        System.out.println("fetching replies for commentId: " + commentId);
        Optional<MarketItemComment> comment = marketItemCommentRepository.findById(commentId);
        if(!comment.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        System.out.println("fetching reply of commentID: " + commentId);

        return ResponseEntity.ok(marketItemCommentRepository.findByParent(commentId));
    }

}
