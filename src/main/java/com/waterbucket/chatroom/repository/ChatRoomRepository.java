package com.waterbucket.chatroom.repository;

import com.waterbucket.chatroom.model.ChatRoom;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ChatRoomRepository extends R2dbcRepository<ChatRoom, UUID> {
    Mono<ChatRoom> findByName(String name);
}
