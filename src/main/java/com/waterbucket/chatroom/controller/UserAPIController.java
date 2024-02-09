package com.waterbucket.chatroom.controller;

import com.waterbucket.chatroom.dto.UserDTO;
import com.waterbucket.chatroom.model.User;
import com.waterbucket.chatroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/users")
public class UserAPIController {
    private final UserService userService;

    @Autowired
    public UserAPIController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    public Mono<UserDTO> registerUser(@RequestBody Mono<UserDTO> user) {
        return userService.registerUser(user).flatMap(userService::getDTOFromUser);
    }

    @GetMapping(value = "/{id}", params = "user")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<User> getUser(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @GetMapping(value = "/{id}")
    protected Mono<UserDTO> getUserDTO(@PathVariable UUID id) {
        return userService.getUserById(id).flatMap(userService::getDTOFromUser).mapNotNull(this::userDTODesensitisation);
    }

    @GetMapping(params = "name")
    public Mono<UserDTO> getUserByName(@RequestParam("name") String name) {
        return userService.getUserByUsername(name).flatMap(userService::getDTOFromUser).mapNotNull(this::userDTODesensitisation);
    }

    @GetMapping
    public Flux<UserDTO> getAllUsers() {
        return userService.getAllUserDTOs().mapNotNull(this::userDTODesensitisation);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser(@PathVariable UUID id) {
        return userService.deleteUser(id);
    }

    private UserDTO userDTODesensitisation(UserDTO userDTO) {
        userDTO.setPassword("");
        return userDTO;
    }
}
