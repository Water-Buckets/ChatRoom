package com.waterbucket.chatroomAPI.service;

import com.waterbucket.chatroomAPI.model.ChatRoom;
import com.waterbucket.chatroomAPI.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final RetryService retryService;

    @Autowired
    public ChatRoomService(ChatRoomRepository chatRoomRepository, RetryService retryService) {
        this.chatRoomRepository = chatRoomRepository;
        this.retryService = retryService;
    }

    public ChatRoom saveChatRoom(ChatRoom chatRoom) {
        return retryService.executeWithRetry(() -> chatRoomRepository.save(chatRoom));
    }

    @Cacheable("chatroom")
    public ChatRoom getChatRoomByName(String name) {
        return chatRoomRepository.findByName(name).orElse(null);
    }

    @Cacheable("chatroom")
    public ChatRoom getChatRoomById(UUID id) {
        return chatRoomRepository.findById(id).orElse(null);
    }

    @Cacheable("chatroom")
    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    @CacheEvict("chatroom")
    public void deleteChatRoom(UUID id) {
        chatRoomRepository.deleteById(id);
    }
}
