package com.waterbucket.chatroomAPI.repository;

import com.waterbucket.chatroomAPI.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {
    Optional<ChatRoom> findByName(String name);
}
