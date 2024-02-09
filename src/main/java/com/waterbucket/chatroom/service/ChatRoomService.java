package com.waterbucket.chatroom.service;

import com.waterbucket.chatroom.model.ChatRoom;
import com.waterbucket.chatroom.repository.ChatRoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public Mono<ChatRoom> saveChatRoom(Mono<ChatRoom> chatRoom) {
        return chatRoom.flatMap(cr -> {
            log.info("ChatRoom {} with name: {} has been created", cr.getId(), cr.getName());
            return chatRoomRepository.save(cr);
        });
    }

    public Mono<ChatRoom> getChatRoomByName(String name) {
        return chatRoomRepository.findByName(name);
    }

    public Mono<ChatRoom> getChatRoomById(UUID id) {
        return chatRoomRepository.findById(id);
    }

    public Flux<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    public Mono<Void> deleteChatRoom(UUID id) {
        return chatRoomRepository.deleteById(id);
    }
}
