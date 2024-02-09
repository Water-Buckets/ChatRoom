package com.waterbucket.chatroom.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Table
@Entity
public class ChatRoom implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NonNull
    private String name;
    private LocalDateTime createTime;

    @OneToMany
    private List<Message> messages;

    @NonNull
    @ManyToOne
    private User user;

    @NonNull
    @ManyToOne
    private User otherUser;
}
