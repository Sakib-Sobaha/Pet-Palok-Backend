package dev.sabri.securityjwt.scopes.community.communityPostComment;

import dev.sabri.securityjwt.scopes.community.communityPost.CommunityPost;
import dev.sabri.securityjwt.scopes.community.communityPost.CommunityPostRepository;
import dev.sabri.securityjwt.scopes.seller.Seller;
import dev.sabri.securityjwt.scopes.seller.SellerRepository;
import dev.sabri.securityjwt.scopes.seller.marketItemComments.MarketItemComment;
import dev.sabri.securityjwt.scopes.seller.marketItemComments.MarketItemCommentController;
import dev.sabri.securityjwt.scopes.user.User;
import dev.sabri.securityjwt.scopes.user.UserRepository;
import dev.sabri.securityjwt.scopes.vets.Vet;
import dev.sabri.securityjwt.scopes.vets.VetRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/communityPostComment")
public class CommunityPostCommentController {
    @Autowired
    private CommunityPostCommentRepository communityPostCommentRepository;
    @Autowired
    private CommunityPostRepository communityPostRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private VetRepository vetRepository;

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommunityPostComment>> getCommunityPostComment(@PathVariable String postId) {
        Optional<CommunityPost> post = communityPostRepository.findById(postId);
        if (post.isPresent()) {
            System.out.println("Post found.. fetching comments:");
            List<CommunityPostComment> comments = communityPostCommentRepository.findByPostId(postId);

            List<CommunityPostComment> filteredComments = comments.stream()
                    .filter(comment -> comment.getParent() == null)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(filteredComments);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/getByParent/{parentId}")
    public ResponseEntity<List<CommunityPostComment>> getCommunityPostCommentByParent(@PathVariable String parentId) {
        System.out.println("Fetching nested comments for commentID: " + parentId);
        Optional<CommunityPostComment> comment = communityPostCommentRepository.findById(parentId);
        if(comment.isPresent()) {
            return ResponseEntity.ok(communityPostCommentRepository.findByParent(parentId));
        }
        return ResponseEntity.notFound().build();
    }

    record NewComment(String text, String postId, String userType, boolean anonymous) {
    }
    @PostMapping("/create")
    public ResponseEntity<CommunityPostComment> createComment(@RequestBody NewComment newComment, Principal principal) {
        System.out.println("Creating new comment for postId: " + newComment.postId);
        String email = principal.getName();
        if(newComment.userType.equalsIgnoreCase("user"))
        {
            Optional<User> user = userRepository.findByEmail(email);

            if(user.isPresent()) {
                CommunityPostComment comment = new CommunityPostComment();
                comment.setParent(null);
                comment.setText(newComment.text);
                comment.setPostId(newComment.postId);
                comment.setAuthorId(user.get().getId());
                comment.setTimestamp(new Date());
                comment.setUserType(newComment.userType.toLowerCase());
                comment.setAnonymous(newComment.anonymous);

                communityPostCommentRepository.save(comment);
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
                CommunityPostComment comment = new CommunityPostComment();
                comment.setParent(null);
                comment.setText(newComment.text);
                comment.setPostId(newComment.postId);
                comment.setAuthorId(seller.get().getId());
                comment.setTimestamp(new Date());
                comment.setUserType(newComment.userType.toLowerCase());
                comment.setAnonymous(newComment.anonymous);

                communityPostCommentRepository.save(comment);
                System.out.println("new comment created (seller)");
                return ResponseEntity.ok(comment);

            }
            else
                return ResponseEntity.notFound().build();
        }
        else if(newComment.userType.equalsIgnoreCase("vet"))
        {
            Optional<Vet> vet = vetRepository.findByEmail(email);
            if(vet.isPresent()) {
                CommunityPostComment comment = new CommunityPostComment();
                comment.setParent(null);
                comment.setText(newComment.text);
                comment.setPostId(newComment.postId);
                comment.setAuthorId(vet.get().getId());
                comment.setTimestamp(new Date());
                comment.setUserType(newComment.userType.toLowerCase());
                comment.setAnonymous(newComment.anonymous);

                communityPostCommentRepository.save(comment);
                System.out.println("new comment created (vet)");
                return ResponseEntity.ok(comment);

            }
            else
                return ResponseEntity.notFound().build();
        }
        else
            return ResponseEntity.notFound().build();
    }

    record NewReply(String text, String postId, String userType, String parent, boolean anonymous) {

    }

    @PostMapping("/reply")
    public ResponseEntity<CommunityPostComment> reply(@RequestBody NewReply newReply, Principal principal) {
        System.out.println("Creating new reply for post: " + newReply.postId);
        String email = principal.getName();
        if(newReply.userType.equalsIgnoreCase("user"))
        {
            Optional<User> user = userRepository.findByEmail(email);

            if(user.isPresent()) {
                CommunityPostComment comment = new CommunityPostComment();
                comment.setParent(newReply.parent);
                comment.setText(newReply.text);
                comment.setPostId(newReply.postId);
                comment.setAuthorId(user.get().getId());
                comment.setTimestamp(new Date());
                comment.setUserType(newReply.userType.toLowerCase());
                comment.setAnonymous(newReply.anonymous);

                communityPostCommentRepository.save(comment);
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
                CommunityPostComment comment = new CommunityPostComment();
                comment.setParent(newReply.parent);
                comment.setText(newReply.text);
                comment.setPostId(newReply.postId);
                comment.setAuthorId(seller.get().getId());
                comment.setTimestamp(new Date());
                comment.setUserType(newReply.userType.toLowerCase());
                comment.setAnonymous(newReply.anonymous);

                communityPostCommentRepository.save(comment);
                System.out.println("new comment created (seller)");
                return ResponseEntity.ok(comment);

            }
            else
                return ResponseEntity.notFound().build();
        }
        else if(newReply.userType.equalsIgnoreCase("vet"))
        {
            Optional<Vet> vet = vetRepository.findByEmail(email);
            if(vet.isPresent()) {
                CommunityPostComment comment = new CommunityPostComment();
                comment.setParent(newReply.parent);
                comment.setText(newReply.text);
                comment.setPostId(newReply.postId);
                comment.setAuthorId(vet.get().getId());
                comment.setTimestamp(new Date());
                comment.setUserType(newReply.userType.toLowerCase());
                comment.setAnonymous(newReply.anonymous);

                communityPostCommentRepository.save(comment);
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
    public ResponseEntity<List<CommunityPostComment>> getReplies(@PathVariable String commentId) {
        System.out.println("fetching replies for commentId: " + commentId);
        Optional<CommunityPostComment> comment = communityPostCommentRepository.findById(commentId);
        if(!comment.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        System.out.println("fetching reply of commentID: " + commentId);

        return ResponseEntity.ok(communityPostCommentRepository.findByParent(commentId));
    }
}
