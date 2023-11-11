package com.waterbucket.chatroom.controller;

import com.waterbucket.chatroom.model.ChatRoom;
import com.waterbucket.chatroom.model.Message;
import com.waterbucket.chatroom.service.ChatRoomService;
import com.waterbucket.chatroom.service.MessageService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final MessageService messageService;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    public ChatController(ChatRoomService chatRoomService, MessageService messageService) {
        this.chatRoomService = chatRoomService;
        this.messageService = messageService;
    }

    @PostMapping("/rooms")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody ChatRoom chatRoom) {
        logger.info("Creating user: {}", chatRoom.getName());
        try {
            ChatRoom createdChatRoom = chatRoomService.saveChatRoom(chatRoom);
            logger.info("User created successfully.");
            return ResponseEntity.ok(createdChatRoom);
        } catch (Exception e) {
            logger.error("Error while creating user: {}", chatRoom.getName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping( "/rooms/{roomId}")
    public ResponseEntity<ChatRoom> getChatRoom(@PathVariable UUID roomId) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(roomId);
        if (chatRoom == null) {
            logger.warn("Chat room not found with id: {}", roomId);
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
        logger.info("Creating message in room: {}", roomId);
        try {
            Message createdMessage = messageService.saveMessage(message);
            logger.info("Message created successfully.");
            return ResponseEntity.ok(createdMessage);
        } catch (Exception e) {
            logger.error("Error while creating message: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<Message>> getMessagesByRoomId(@PathVariable UUID roomId) {
        return ResponseEntity.ok(messageService.getMessagesByChatRoom(chatRoomService.getChatRoomById(roomId)));
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable UUID roomId){
        chatRoomService.deleteChatRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}
