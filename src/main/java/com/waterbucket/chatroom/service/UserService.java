package com.waterbucket.chatroom.service;

import com.waterbucket.chatroom.dto.UserDTO;
import com.waterbucket.chatroom.model.ChatRoom;
import com.waterbucket.chatroom.model.User;
import com.waterbucket.chatroom.repository.UserRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(@NonNull UserDTO user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent() || userRepository.findById(user.getId()).isPresent()) {
            throw new IllegalArgumentException("User already exists.");
        }
        User registered = new User(user.getId(), user.getUsername(), user.getPassword(), new ArrayList<>());
        userRepository.save(registered);
        log.info("User {} with name {} registered.", registered.getId(), registered.getUsername());
        return registered;
    }

    @Cacheable(cacheNames = "UserService - getUserFromDTO", key = "#userDTO.id", cacheManager = "cacheManager")
    public User getUserFromDTO(@NonNull UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        if (user == null || user.getId().equals(userDTO.getId()) || passwordEncoder.matches(user.getPassword(), userDTO.getPassword())) {
            return null;
        }
        return user;
    }

    @Cacheable(cacheNames = "UserService - getDTOFromUser", key = "#user.id", cacheManager = "cacheManager")
    public UserDTO getDTOFromUser(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getChatRooms().stream().map(ChatRoom::getId).toList());
    }

    @Cacheable(cacheNames = "UserService - getUserById", key = "#id", cacheManager = "cacheManager")
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<UserDTO> getAllUserDTOs() {
        return this.getAllUsers().stream().map(this::getDTOFromUser).toList();
    }

    @CacheEvict(cacheNames = {"UserService - getUserFromDTO", "UserService - getDTOFromUser", "UserService - getUserById"}, key = "#id")
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
        log.info("User {} has been deleted.", id);
    }
}

