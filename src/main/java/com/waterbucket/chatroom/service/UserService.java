package com.waterbucket.chatroom.service;

import com.waterbucket.chatroom.controller.UserController;
import com.waterbucket.chatroom.model.User;
import com.waterbucket.chatroom.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        final int MAX_RETRY_ATTEMPTS = 2;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        for (int i = 0; i < MAX_RETRY_ATTEMPTS; ) {
            try {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                @SuppressWarnings("UnnecessaryLocalVariable") var savedUser = userRepository.save(user);
                return savedUser;
            } catch (Exception e) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e1) {
                    logger.error("Thread sleep interrupted");
                    throw new RuntimeException(e1);
                }
                logger.info("Optimistic lock exception occurred. Retry attempt: {}", i);
                ++i;
            }
        }
        throw new RuntimeException("Optimistic lock exception occurred.");
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}

