package dev.sabri.securityjwt.scopes.user;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

//    public User findUserByEmail(String email){
//        return userRepository.findByEmail(email);
//    }

    public User findUserById(String id){
        return userRepository.findUserById(id);
    }

    public int enableUser(String email){
        return userRepository.enableUser(email);
    }

    public boolean updateUserStatusByEmail(String email, String status){
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setStatus(status);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public void updateUserStatus(String userId, String status){
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setStatus(status);
            userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("User not found with id " + userId);
        }
    }

    public List<User> findConnectedUsers() {
        return userRepository.findAllByStatus("online");
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void disconnectUser(User user) {
        var storedUser = userRepository.findUserById(user.getId());
        if(storedUser != null){
            storedUser.setStatus("offline");
            userRepository.save(storedUser);
        }
    }
}
