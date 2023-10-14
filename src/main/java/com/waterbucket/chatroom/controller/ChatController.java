package com.waterbucket.chatroom.controller;

import com.waterbucket.chatroom.model.ChatRoom;
import com.waterbucket.chatroom.model.Message;
import com.waterbucket.chatroom.service.ChatRoomService;
import com.waterbucket.chatroom.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final MessageService messageService;

    @Autowired
    public ChatController(ChatRoomService chatRoomService, MessageService messageService) {
        this.chatRoomService = chatRoomService;
        this.messageService = messageService;
    }

    @PostMapping("/rooms")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody ChatRoom chatRoom) {
        return ResponseEntity.ok(chatRoomService.saveChatRoom(chatRoom));
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<ChatRoom> getChatRoom(@PathVariable UUID id) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(id);
        if (chatRoom == null) {
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
        return ResponseEntity.ok(messageService.saveMessage(message));
    }

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<Message>> getMessagesByRoomId(@PathVariable UUID roomId) {
        return ResponseEntity.ok(messageService.getMessagesByChatRoom(chatRoomService.getChatRoomById(roomId)));
    }
}
