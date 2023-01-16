package com.example.fusionwallet.service;

import com.example.fusionwallet.model.User;
import com.example.fusionwallet.model.UserDto;
import com.example.fusionwallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createAcc(User userDetails){
        User newUser = new User();
        newUser.setFirstName(userDetails.getFirstName());
        newUser.setLastName(userDetails.getLastName());
        newUser.setPassword(userDetails.getPassword());
        newUser.setEmail(userDetails.getEmail());
        newUser.setUsername(userDetails.getUsername());
        return userRepository.save(newUser);
    }

    public Optional<User> findbyId(Long user_id){
        return userRepository.findById(user_id);
    }

    public Optional<User> findbyEmail(String email){
        return userRepository.findByEmail(email);
    }
}
