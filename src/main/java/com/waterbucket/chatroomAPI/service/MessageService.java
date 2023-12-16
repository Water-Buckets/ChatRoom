package com.waterbucket.chatroomAPI.service;

import com.waterbucket.chatroomAPI.model.ChatRoom;
import com.waterbucket.chatroomAPI.model.Message;
import com.waterbucket.chatroomAPI.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final RetryService retryService;

    @Autowired
    public MessageService(MessageRepository messageRepository, RetryService retryService) {
        this.messageRepository = messageRepository;
        this.retryService = retryService;
    }

    public Message saveMessage(Message message) {
        return retryService.executeWithRetry(() -> messageRepository.save(message));
    }

    @Cacheable("messages")
    public List<Message> getMessagesByChatRoom(ChatRoom chatRoom) {
        return messageRepository.findByChatRoomOrderByCreateTime(chatRoom);
    }

    @CacheEvict("messages")
    public void deleteMessage(UUID id) {
        messageRepository.deleteById(id);
    }

}
