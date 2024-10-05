package dev.sabri.securityjwt.scopes.community;


import dev.sabri.securityjwt.scopes.seller.Seller;
import dev.sabri.securityjwt.scopes.seller.SellerRepository;
import dev.sabri.securityjwt.scopes.user.User;
import dev.sabri.securityjwt.scopes.user.UserRepository;
import dev.sabri.securityjwt.scopes.vets.Vet;
import dev.sabri.securityjwt.scopes.vets.VetRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

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
    public ResponseEntity<Community> createCommunity(@RequestBody NewCommunityRequest newCommunityRequest, Principal principal) {
        Community community = new Community();

        community.setName(newCommunityRequest.name);
        community.setDescription(newCommunityRequest.description);

        if(newCommunityRequest.image.equalsIgnoreCase(""))
        {
            community.setImage("https://media.istockphoto.com/id/1357830750/vector/geometric-illustration-of-multi-coloured-human-figures.jpg?s=612x612&w=0&k=20&c=2uvkAa8B9pyBcMbMUoE6zQVXPrNAz8Tdysdfq8G3oKM=");
        }
        else
            community.setImage(newCommunityRequest.image);

        community.setTopics(newCommunityRequest.topics);
        community.setTimeStamp(new Date());

        System.out.println(newCommunityRequest.userType);
        String email = principal.getName();
        if(newCommunityRequest.userType.equalsIgnoreCase("user")){
            System.out.println("user");
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {

                community.getAdminUsers().add(user.get().getId());
                community.getUserList().add(user.get().getId());
                System.out.println(community == null);
                communityRepository.save(community);
                return ResponseEntity.ok((community));
            }
            else {
                return ResponseEntity.notFound().build();
            }
        }
        else if(newCommunityRequest.userType.equalsIgnoreCase("seller")){
            Optional<Seller> seller = sellerRepository.findByEmail(email);
            if (seller.isPresent()) {



                community.getAdminSellers().add(seller.get().getId());
                community.getSellerList().add(seller.get().getId());

                communityRepository.save(community);
                return ResponseEntity.ok((community));
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

                community.getAdminVets().add(vet.get().getId());
                community.getVetList().add(vet.get().getId());

                communityRepository.save(community);
                return ResponseEntity.ok((community));
            }
            else {
                return ResponseEntity.notFound().build();
            }
        }
        else {
            return ResponseEntity.notFound().build();
        }

    }

    record JoinCommunityRequest(String userType){}
    @PostMapping("/join/{communityId}")
    public ResponseEntity<Community> joinCommunity(@PathVariable String communityId, Principal principal, @RequestBody JoinCommunityRequest joinCommunityRequest) {
        System.out.println("JOIN REQ received");
        System.out.println("body: "+joinCommunityRequest.userType);
        Optional<Community> community = communityRepository.findById(communityId);
        if(community.isPresent())
        {
            if(joinCommunityRequest.userType.equalsIgnoreCase("user"))
            {
                Optional<User> user = userRepository.findByEmail(principal.getName());
                if(user.isPresent()) {
                    community.get().getUserList().add(user.get().getId());
                    communityRepository.save(community.get());
                    return ResponseEntity.ok(community.get());
                }
                return ResponseEntity.notFound().build();
            }
            else if(joinCommunityRequest.userType.equalsIgnoreCase("seller"))
            {
                Optional<Seller> seller = sellerRepository.findByEmail(principal.getName());
                if(seller.isPresent()) {
                    community.get().getSellerList().add(seller.get().getId());
                    communityRepository.save(community.get());
                    return ResponseEntity.ok(community.get());
                }
                return ResponseEntity.notFound().build();
            }
            else if(joinCommunityRequest.userType.equalsIgnoreCase("vet")){
                Optional<Vet> vet = vetRepository.findByEmail(principal.getName());
                if(vet.isPresent()) {
                    community.get().getVetList().add(vet.get().getId());
                    communityRepository.save(community.get());
                    return ResponseEntity.ok(community.get());
                }
                return ResponseEntity.notFound().build();
            }

        }
        return ResponseEntity.notFound().build();
    }

    record LeaveCommunityReq(String userType){}
    @PostMapping("/leave/{communityId}")
    public ResponseEntity<Community> leaveCommunity(@PathVariable String communityId, Principal principal, @RequestBody LeaveCommunityReq leaveCommunityReq) {
        Optional<Community> community = communityRepository.findById(communityId);
        if(community.isPresent())
        {
            if(leaveCommunityReq.userType.equalsIgnoreCase("user"))
            {
                Optional<User> user = userRepository.findByEmail(principal.getName());
                if(user.isPresent()) {
                    if(community.get().getAdminUsers().size() == 1 && community.get().getAdminUsers().contains(user.get().getId()))
                    {
                        // i am the last admin standing
                        return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(community.get());
                    }

                    community.get().getUserList().remove(user.get().getId());
                    community.get().getAdminUsers().remove(user.get().getId());
                    communityRepository.save(community.get());
                    return ResponseEntity.ok(community.get());
                }
                return ResponseEntity.notFound().build();
            }
            else if(leaveCommunityReq.userType.equalsIgnoreCase("seller"))
            {
                Optional<Seller> seller = sellerRepository.findByEmail(principal.getName());
                if(seller.isPresent()) {
                    if(community.get().getAdminSellers().size() == 1 && community.get().getAdminSellers().contains(seller.get().getId()))
                    {
                        // i am the last admin standing
                        return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(community.get());
                    }

                    community.get().getSellerList().remove(seller.get().getId());
                    community.get().getAdminSellers().remove(seller.get().getId());
                    communityRepository.save(community.get());
                    return ResponseEntity.ok(community.get());
                }
                return ResponseEntity.notFound().build();
            }
            else if(leaveCommunityReq.userType.equalsIgnoreCase("vet")){
                Optional<Vet> vet = vetRepository.findByEmail(principal.getName());
                if(vet.isPresent()) {
                    if(community.get().getAdminVets().size() == 1 && community.get().getAdminVets().contains(vet.get().getId()))
                    {
                        // i am the last admin standing
                        return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(community.get());
                    }

                    community.get().getVetList().remove(vet.get().getId());
                    community.get().getAdminVets().remove(vet.get().getId());
                    communityRepository.save(community.get());
                    return ResponseEntity.ok(community.get());
                }
                return ResponseEntity.notFound().build();
            }

        }
        return ResponseEntity.notFound().build();
    }

    record AddAdminRequest(List<String> users, String userType){}
    @PostMapping("/addAdmin/user/{communityId}")
    ResponseEntity<Community> addAdminU(@PathVariable String communityId, @RequestBody AddAdminRequest addAdminRequest, Principal principal) {
        Optional<Community> community = communityRepository.findById(communityId);
        if(community.isPresent()) {
            if(addAdminRequest.userType.equalsIgnoreCase("user"))
            {
                Optional<User> user = userRepository.findByEmail(principal.getName());
                if(user.isPresent()) {
                    String uid = user.get().getId();
                    if(community.get().getAdminUsers().contains(uid))
                    {
                        community.get().getAdminUsers().addAll(addAdminRequest.users);
                        communityRepository.save(community.get());
                        return ResponseEntity.ok(community.get());
                    }
                }
                return ResponseEntity.notFound().build();
            }
            else if(addAdminRequest.userType.equalsIgnoreCase("seller"))
            {
                Optional<Seller> seller = sellerRepository.findByEmail(principal.getName());
                if(seller.isPresent()) {
                    String uid = seller.get().getId();
                    if(community.get().getAdminSellers().contains(uid))
                    {
                        community.get().getAdminUsers().addAll(addAdminRequest.users);
                        communityRepository.save(community.get());
                        return ResponseEntity.ok(community.get());
                    }
                }
            }
            else if(addAdminRequest.userType.equalsIgnoreCase("vet"))
            {
                Optional<Vet> vet = vetRepository.findByEmail(principal.getName());
                if(vet.isPresent()) {
                    String uid = vet.get().getId();
                    if(community.get().getAdminVets().contains(uid))
                    {
                        community.get().getAdminUsers().addAll(addAdminRequest.users);
                        communityRepository.save(community.get());
                        return ResponseEntity.ok(community.get());
                    }
                }
            }
            return ResponseEntity.notFound().build();

        }
        return ResponseEntity.notFound().build();
    }

    record AddAdminRequestV(List<String> vets, String userType){}
    @PostMapping("/addAdmin/vet/{communityId}")
    ResponseEntity<Community> addAdminV(@PathVariable String communityId, @RequestBody AddAdminRequestV addAdminRequest, Principal principal) {
        Optional<Community> community = communityRepository.findById(communityId);
        if(community.isPresent()) {
            if(addAdminRequest.userType.equalsIgnoreCase("user"))
            {
                Optional<User> user = userRepository.findByEmail(principal.getName());
                if(user.isPresent()) {
                    String uid = user.get().getId();
                    if(community.get().getAdminUsers().contains(uid))
                    {
                        community.get().getAdminVets().addAll(addAdminRequest.vets);
                        communityRepository.save(community.get());
                        return ResponseEntity.ok(community.get());
                    }
                }
                return ResponseEntity.notFound().build();
            }
            else if(addAdminRequest.userType.equalsIgnoreCase("seller"))
            {
                Optional<Seller> seller = sellerRepository.findByEmail(principal.getName());
                if(seller.isPresent()) {
                    String uid = seller.get().getId();
                    if(community.get().getAdminSellers().contains(uid))
                    {
                        community.get().getAdminVets().addAll(addAdminRequest.vets);
                        communityRepository.save(community.get());
                        return ResponseEntity.ok(community.get());
                    }
                }
            }
            else if(addAdminRequest.userType.equalsIgnoreCase("vet"))
            {
                Optional<Vet> vet = vetRepository.findByEmail(principal.getName());
                if(vet.isPresent()) {
                    String uid = vet.get().getId();
                    if(community.get().getAdminVets().contains(uid))
                    {
                        community.get().getAdminVets().addAll(addAdminRequest.vets);
                        communityRepository.save(community.get());
                        return ResponseEntity.ok(community.get());
                    }
                }
            }
            return ResponseEntity.notFound().build();

        }
        return ResponseEntity.notFound().build();
    }

    record AddAdminRequestS(List<String> sellers, String userType){}
    @PostMapping("/addAdmin/seller/{communityId}")
    ResponseEntity<Community> addAdminS(@PathVariable String communityId, @RequestBody AddAdminRequestS addAdminRequest, Principal principal) {
        Optional<Community> community = communityRepository.findById(communityId);
        if(community.isPresent()) {
            if(addAdminRequest.userType.equalsIgnoreCase("user"))
            {
                Optional<User> user = userRepository.findByEmail(principal.getName());
                if(user.isPresent()) {
                    String uid = user.get().getId();
                    if(community.get().getAdminUsers().contains(uid))
                    {
                        community.get().getAdminSellers().addAll(addAdminRequest.sellers);
                        communityRepository.save(community.get());
                        return ResponseEntity.ok(community.get());
                    }
                }
                return ResponseEntity.notFound().build();
            }
            else if(addAdminRequest.userType.equalsIgnoreCase("seller"))
            {
                Optional<Seller> seller = sellerRepository.findByEmail(principal.getName());
                if(seller.isPresent()) {
                    String uid = seller.get().getId();
                    if(community.get().getAdminSellers().contains(uid))
                    {
                        community.get().getAdminSellers().addAll(addAdminRequest.sellers);
                        communityRepository.save(community.get());
                        return ResponseEntity.ok(community.get());
                    }
                }
            }
            else if(addAdminRequest.userType.equalsIgnoreCase("vet"))
            {
                Optional<Vet> vet = vetRepository.findByEmail(principal.getName());
                if(vet.isPresent()) {
                    String uid = vet.get().getId();
                    if(community.get().getAdminVets().contains(uid))
                    {
                        community.get().getAdminSellers().addAll(addAdminRequest.sellers);
                        communityRepository.save(community.get());
                        return ResponseEntity.ok(community.get());
                    }
                }
            }
            return ResponseEntity.notFound().build();

        }
        return ResponseEntity.notFound().build();
    }

}
