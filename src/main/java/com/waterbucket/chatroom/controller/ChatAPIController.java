package com.waterbucket.chatroom.controller;

import com.waterbucket.chatroom.model.ChatRoom;
import com.waterbucket.chatroom.model.Message;
import com.waterbucket.chatroom.service.ChatRoomService;
import com.waterbucket.chatroom.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/chat")
public class ChatAPIController {

    private final ChatRoomService chatRoomService;
    private final MessageService messageService;

    @Autowired
    public ChatAPIController(ChatRoomService chatRoomService, MessageService messageService) {
        this.chatRoomService = chatRoomService;
        this.messageService = messageService;
    }

    @PostMapping("/rooms")
    public Mono<ChatRoom> createChatRoom(@RequestBody Mono<ChatRoom> chatRoom) {
        return chatRoomService.saveChatRoom(chatRoom);
    }

    @GetMapping("/rooms/{id}")
    public Mono<ChatRoom> getChatRoom(@PathVariable UUID id) {
        return chatRoomService.getChatRoomById(id);
    }

    @GetMapping("/rooms")
    public Flux<ChatRoom> getAllChatRooms() {
        return chatRoomService.getAllChatRooms();
    }

    @PostMapping("/rooms/{id}/messages")
    public Mono<Message> createMessage(@PathVariable UUID id, @RequestBody Mono<Message> message) {
        return messageService.saveMessage(message, id);
    }

    @GetMapping("/rooms/{id}/messages")
    public Flux<Message> getMessagesByRoomId(@PathVariable UUID id) {
        return messageService.getMessagesByChatRoom(chatRoomService.getChatRoomById(id));
    }

    @DeleteMapping("/rooms/{roomId}")
    public Mono<Void> deleteChatRoom(@PathVariable UUID roomId) {
        return chatRoomService.deleteChatRoom(roomId);
    }
}
