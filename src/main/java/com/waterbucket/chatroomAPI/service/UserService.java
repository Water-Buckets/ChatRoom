package com.waterbucket.chatroomAPI.service;

import com.waterbucket.chatroomAPI.model.User;
import com.waterbucket.chatroomAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RetryService retryService;

    @Autowired
    public UserService(UserRepository userRepository, RetryService retryService) {
        this.userRepository = userRepository;
        this.retryService = retryService;
    }

    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent() || userRepository.findById(user.getId()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        user.setId(UUID.fromString(user.getId().toString().toUpperCase()));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return retryService.executeWithRetry(() -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        });
    }

    @Cacheable("users")
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Cacheable("users")
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Cacheable("users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @CacheEvict("users")
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}

