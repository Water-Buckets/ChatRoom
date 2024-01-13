package com.waterbucket.chatroom.service;

import com.waterbucket.chatroom.dto.UserDTO;
import com.waterbucket.chatroom.model.User;
import com.waterbucket.chatroom.repository.UserRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
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

    public Mono<User> registerUser(@NonNull Mono<UserDTO> user) {
        return user.map(userDTO -> {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            return userDTO;
        }).flatMap(dto -> {
            Mono<Boolean> usernameExists = userRepository.findByUsername(dto.getUsername())
                    .hasElement();
            Mono<Boolean> idExists = userRepository.findById(dto.getId())
                    .hasElement();

            return Mono.zip(usernameExists, idExists)
                    .flatMap(tuple -> {
                        if (tuple.getT1() || tuple.getT2()) {
                            return Mono.error(new IllegalArgumentException("User already exists."));
                        } else {
                            User registered = new User(dto.getId(), dto.getUsername(), dto.getPassword(), new ArrayList<>());
                            return userRepository.save(registered)
                                    .doOnSuccess(saved -> log.info("User {} with name {} registered.", saved.getId(), saved.getUsername()));
                        }
                    });
        });
    }

    @Cacheable(cacheNames = "UserService - getUserFromDTO", key = "#userDTO.id", cacheManager = "cacheManager")
    public Mono<User> getUserFromDTO(@NonNull UserDTO userDTO) {
        return userRepository.findById(userDTO.getId())
                .mapNotNull(user -> {
                    if (user.getId().equals(userDTO.getId()) || passwordEncoder.matches(user.getPassword(), userDTO.getPassword())) {
                        return user;
                    } else {
                        return null;
                    }
                });
    }


    @Cacheable(cacheNames = "UserService - getDTOFromUser", key = "#user.id", cacheManager = "cacheManager")
    public Mono<UserDTO> getDTOFromUser(User user) {
        return Mono.just(new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getChatRooms()));
    }

    @Cacheable(cacheNames = "UserService - getUserById", key = "#id", cacheManager = "cacheManager")
    public Mono<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Mono<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Flux<UserDTO> getAllUserDTOs() {
        return this.getAllUsers().flatMap(this::getDTOFromUser);
    }

    @CacheEvict(cacheNames = {"UserService - getUserFromDTO", "UserService - getDTOFromUser", "UserService - getUserById"}, key = "#id")
    public Mono<Void> deleteUser(UUID id) {
        return Mono.fromRunnable(() -> {
            //noinspection CallingSubscribeInNonBlockingScope
            userRepository.deleteById(id).subscribe();
            log.info("User {} has been deleted.", id);
        });
    }
}

