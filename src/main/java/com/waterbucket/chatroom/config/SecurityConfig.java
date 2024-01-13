package com.waterbucket.chatroom.config;

import com.waterbucket.chatroom.model.User;
import com.waterbucket.chatroom.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isPresent()) return user.get();
            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //noinspection Convert2MethodRef
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().permitAll()
                ).csrf(csrf -> csrf.disable());
        return http.build();
    }
}
