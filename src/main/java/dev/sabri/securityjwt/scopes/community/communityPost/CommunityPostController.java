package dev.sabri.securityjwt.scopes.community.communityPost;

import dev.sabri.securityjwt.scopes.community.Community;
import dev.sabri.securityjwt.scopes.community.CommunityRepository;
import dev.sabri.securityjwt.scopes.seller.Seller;
import dev.sabri.securityjwt.scopes.seller.SellerRepository;
import dev.sabri.securityjwt.scopes.user.User;
import dev.sabri.securityjwt.scopes.user.UserRepository;
import dev.sabri.securityjwt.scopes.vets.Vet;
import dev.sabri.securityjwt.scopes.vets.VetRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/communityPost")
public class CommunityPostController {
    @Autowired
    private CommunityPostRepository communityPostRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private VetRepository vetRepository;
    @Autowired
    private CommunityRepository communityRepository;

    @GetMapping("/{communityId}")
    public ResponseEntity<List<CommunityPost>> getCommunityPost(@PathVariable String communityId) {
        return ResponseEntity.ok(communityPostRepository.findByCommunityId(communityId));
    }

    record NewPostRequest(String title, String text, List<String> images, String userType, List<String> topics,
                          String communityId, boolean anonymous) {
    }

    @PostMapping("/create")
    public ResponseEntity<CommunityPost> createCommunityPost(@RequestBody NewPostRequest newPostRequest, Principal principal) {
        String email = principal.getName();
        CommunityPost communityPost = new CommunityPost();
        communityPost.setTitle(newPostRequest.title);
        communityPost.setText(newPostRequest.text);
        communityPost.setImages(newPostRequest.images);
        communityPost.setTopics(newPostRequest.topics);
        Optional<Community> community = communityRepository.findById(newPostRequest.communityId);
        if(community.isPresent()) {
            for(String s : newPostRequest.topics) {
                if(!community.get().getTopics().contains(s))
                {
                    community.get().getTopics().add(s);
                }
            }
        }
        else
            return ResponseEntity.notFound().build();
        communityPost.setTimestamp(new Date());
        communityPost.setReactList(new ArrayList<>());
        communityPost.setUserType(newPostRequest.userType);
        communityPost.setCommunityId(newPostRequest.communityId);
        communityPost.setAnonymous(newPostRequest.anonymous);

        if (newPostRequest.userType.equalsIgnoreCase("user")) {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                communityPost.setAuthor(user.get().getId());
                communityPostRepository.save(communityPost);
                communityRepository.save(community.get());
                return ResponseEntity.ok(communityPost);
            }
            return ResponseEntity.noContent().build();
        } else if (newPostRequest.userType.equalsIgnoreCase("seller")) {
            Optional<Seller> seller = sellerRepository.findByEmail(email);
            if (seller.isPresent()) {
                communityPost.setAuthor(seller.get().getId());
                communityPostRepository.save(communityPost);
                communityRepository.save(community.get());
                return ResponseEntity.ok(communityPost);
            }
            return ResponseEntity.noContent().build();
        } else if (newPostRequest.userType.equalsIgnoreCase("vet")) {
            Optional<Vet> vet = vetRepository.findByEmail(email);
            if (vet.isPresent()) {
                communityPost.setAuthor(vet.get().getId());
                communityPostRepository.save(communityPost);
                communityRepository.save(community.get());
                return ResponseEntity.ok(communityPost);
            }
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.noContent().build();
    }

    record ReactToPost(String userType) {
    }

    @PostMapping("/react/{postId}")
    public ResponseEntity<CommunityPost> reactToPost(Principal principal, @PathVariable String postId, @RequestBody ReactToPost reactToPost) {
        Optional<CommunityPost> communityPost = communityPostRepository.findById(postId);
        String email = principal.getName();
        System.out.println("reactToPost");
        if (communityPost.isPresent()) {
            System.out.println("post found");
            if (reactToPost.userType.equalsIgnoreCase("user")) {
                Optional<User> user = userRepository.findByEmail(email);
                if (!communityPost.get().getReactList().contains(user.get().getId()))
                    communityPost.get().getReactList().add(user.get().getId());
                communityPostRepository.save(communityPost.get());
                return ResponseEntity.ok(communityPost.get());
            } else if (reactToPost.userType.equalsIgnoreCase("seller")) {
                Optional<Seller> seller = sellerRepository.findByEmail(email);
                if (!communityPost.get().getReactList().contains(seller.get().getId()))
                    communityPost.get().getReactList().add(seller.get().getId());
                communityPostRepository.save(communityPost.get());
                return ResponseEntity.ok(communityPost.get());
            } else if (reactToPost.userType.equalsIgnoreCase("vet")) {
                Optional<Vet> vet = vetRepository.findByEmail(email);
                if (!communityPost.get().getReactList().contains(vet.get().getId()))
                    communityPost.get().getReactList().add(vet.get().getId());
                communityPostRepository.save(communityPost.get());
                return ResponseEntity.ok(communityPost.get());
            }

            ResponseEntity.notFound().build();
        }

        return ResponseEntity.notFound().build();


    }

    record RemoveReact(String userType) {
    }

    @PostMapping("/removeReact/{postId}")
    public ResponseEntity<CommunityPost> reactToPost(Principal principal, @PathVariable String postId, @RequestBody RemoveReact reactToPost) {
        Optional<CommunityPost> communityPost = communityPostRepository.findById(postId);
        String email = principal.getName();
        System.out.println("remove react");
        if (communityPost.isPresent()) {
            System.out.println("post found");
            if (reactToPost.userType.equalsIgnoreCase("user")) {
                Optional<User> user = userRepository.findByEmail(email);
                communityPost.get().getReactList().remove(user.get().getId());
                communityPostRepository.save(communityPost.get());
                return ResponseEntity.ok(communityPost.get());
            } else if (reactToPost.userType.equalsIgnoreCase("seller")) {
                Optional<Seller> seller = sellerRepository.findByEmail(email);
                communityPost.get().getReactList().remove(seller.get().getId());
                communityPostRepository.save(communityPost.get());
                return ResponseEntity.ok(communityPost.get());
            } else if (reactToPost.userType.equalsIgnoreCase("vet")) {
                Optional<Vet> vet = vetRepository.findByEmail(email);
                communityPost.get().getReactList().remove(vet.get().getId());
                communityPostRepository.save(communityPost.get());
                return ResponseEntity.ok(communityPost.get());
            }

            ResponseEntity.notFound().build();
        }

        return ResponseEntity.notFound().build();
    }
}
