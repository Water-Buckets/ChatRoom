package com.waterbucket.chatroom.controller;

import com.waterbucket.chatroom.dto.UserDTO;
import com.waterbucket.chatroom.model.User;
import com.waterbucket.chatroom.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/users", produces = "application/json")
public class UserAPIController {

    private final UserService userService;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserAPIController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok(userService.getDTOFromUser(registeredUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "/{id}", params = "user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUser(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getUserDTO(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserDTO userDTO = userService.getDTOFromUser(user);
        userDTO.setPassword("");
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping(params = "name")
    public ResponseEntity<UserDTO> getUserByName(@RequestParam("name") String name) {
        User user = userService.getUserByUsername(name);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.getDTOFromUser(user));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUserDTOs().stream().peek(userDTO -> userDTO.setPassword("")).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
