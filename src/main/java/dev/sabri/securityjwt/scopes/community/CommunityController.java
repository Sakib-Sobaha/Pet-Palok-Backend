package dev.sabri.securityjwt.scopes.community;


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
@RequestMapping("api/v1/communities")
@AllArgsConstructor
public class CommunityController {
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private VetRepository vetRepository;

    @GetMapping("/all")
    public List<Community> getAllCommunities() {
        System.out.println("Fetch all communities");
        return communityRepository.findAll();
    }

    @GetMapping("/{communityId}")
    public Optional<Community> getCommunity(@PathVariable String communityId) {
        System.out.println("Fetch community with id: " + communityId);
        return communityRepository.findById(communityId);
    }

    record NewCommunityRequest(String name, String description, String image, List<String> topics, String userType) {}

    @PostMapping("/create")
    public ResponseEntity<Optional<Community>> createCommunity(@RequestBody NewCommunityRequest newCommunityRequest, Principal principal) {
        Community community = new Community();

        community.setName(newCommunityRequest.name);
        community.setDescription(newCommunityRequest.description);
        community.setImage(newCommunityRequest.image);
        community.setTopics(newCommunityRequest.topics);
        community.setDateCreated(new Date());

        String email = principal.getName();
        if(newCommunityRequest.userType.equalsIgnoreCase("user")){
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {

                List<String> userList = new ArrayList<>();
                userList.add(user.get().getId());

                community.setAdmins(userList);
                community.setMembers(userList);

                communityRepository.save(community);
                return ResponseEntity.ok(Optional.of(community));
            }
            else {
                return ResponseEntity.notFound().build();
            }
        }
        else if(newCommunityRequest.userType.equalsIgnoreCase("seller")){
            Optional<Seller> seller = sellerRepository.findByEmail(email);
            if (seller.isPresent()) {

                List<String> userList = new ArrayList<>();
                userList.add(seller.get().getId());

                community.setAdmins(userList);
                community.setMembers(userList);

                communityRepository.save(community);
                return ResponseEntity.ok(Optional.of(community));
            }
            else {
                return ResponseEntity.notFound().build();
            }
        }
        else if(newCommunityRequest.userType.equalsIgnoreCase("vet")){
            Optional<Vet> vet = vetRepository.findByEmail(email);
            if (vet.isPresent()) {

                List<String> userList = new ArrayList<>();
                userList.add(vet.get().getId());

                community.setAdmins(userList);
                community.setMembers(userList);

                communityRepository.save(community);
                return ResponseEntity.ok(Optional.of(community));
            }
            else {
                return ResponseEntity.notFound().build();
            }
        }
        else {
            return ResponseEntity.notFound().build();
        }

    }
}
