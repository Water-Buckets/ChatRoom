package com.waterbucket.chatroom.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private LocalDateTime createTime;

    @OneToMany(mappedBy = "chatRoom")
    private List<Message> messages;

    @ManyToMany
    private List<User> users;
}