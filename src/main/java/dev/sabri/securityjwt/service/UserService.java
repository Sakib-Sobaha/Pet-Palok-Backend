package dev.sabri.securityjwt.service;


import dev.sabri.securityjwt.repo.UserRepository;
import dev.sabri.securityjwt.scopes.user.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

//    public User findUserByEmail(String email){
//        return userRepository.findByEmail(email);
//    }

    public User findUserById(String id){
        return userRepository.findUserById(id);
    }
}
