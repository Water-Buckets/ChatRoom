package com.waterbucket.chatroom.service;

import com.waterbucket.chatroom.model.ChatRoom;
import com.waterbucket.chatroom.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoom saveChatRoom(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom getChatRoomByName(String name) {
        return chatRoomRepository.findByName(name).orElse(null);
    }

    public ChatRoom getChatRoomById(UUID id) {
        return chatRoomRepository.findById(id).orElse(null);
    }

    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    public void deleteChatRoom(UUID id) {
        chatRoomRepository.deleteById(id);
    }
}
