package com.waterbucket.chatroom.controller;

import com.waterbucket.chatroom.model.ChatRoom;
import com.waterbucket.chatroom.model.Message;
import com.waterbucket.chatroom.service.ChatRoomService;
import com.waterbucket.chatroom.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
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
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody ChatRoom chatRoom) {
        log.info("Creating chatroom: {}", chatRoom.getName());
        try {
            ChatRoom createdChatRoom = chatRoomService.saveChatRoom(chatRoom);
            log.info("Chatroom created successfully.");
            return ResponseEntity.ok(createdChatRoom);
        } catch (Exception e) {
            log.error("Error while creating chatroom: {}", chatRoom.getName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<ChatRoom> getChatRoom(@PathVariable UUID roomId) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(roomId);
        if (chatRoom == null) {
            log.warn("Chatroom not found with id: {}", roomId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoom>> getAllChatRooms() {
        return ResponseEntity.ok(chatRoomService.getAllChatRooms());
    }

    @PostMapping("/rooms/{roomId}/messages")
    public ResponseEntity<Message> createMessage(@PathVariable UUID roomId, @RequestBody Message message) {
        message.setChatRoom(chatRoomService.getChatRoomById(roomId));
        message.setCreateTime(LocalDateTime.now());
        log.info("Creating message in chatroom: {}", roomId);
        try {
            Message createdMessage = messageService.saveMessage(message);
            log.info("Message created successfully.");
            return ResponseEntity.ok(createdMessage);
        } catch (Exception e) {
            log.error("Error while creating message: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<Message>> getMessagesByRoomId(@PathVariable UUID roomId) {
        return ResponseEntity.ok(messageService.getMessagesByChatRoom(chatRoomService.getChatRoomById(roomId)));
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable UUID roomId) {
        chatRoomService.deleteChatRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}
