package com.waterbucket.chatroomAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ChatRoomAPIApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatRoomAPIApplication.class, args);
    }
}
