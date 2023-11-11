package com.waterbucket.chatroom.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String content;
    private LocalDateTime createTime;

    @ManyToOne
    private User sender;

    @ManyToOne
    @Setter
    private ChatRoom chatRoom;
}