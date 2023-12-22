package com.waterbucket.chatroomAPI.repository;

import com.waterbucket.chatroomAPI.model.ChatRoom;
import com.waterbucket.chatroomAPI.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByChatRoomOrderByCreateTime(ChatRoom chatRoom);
}
