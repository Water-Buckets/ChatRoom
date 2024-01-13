package com.waterbucket.chatroom.repository;

import com.waterbucket.chatroom.model.ChatRoom;
import com.waterbucket.chatroom.model.Message;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface MessageRepository extends R2dbcRepository<Message, UUID> {
    Flux<Message> findByChatRoomOrderByCreateTime(ChatRoom chatRoom);
}
