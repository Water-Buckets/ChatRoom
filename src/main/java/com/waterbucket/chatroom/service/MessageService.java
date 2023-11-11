package com.waterbucket.chatroom.service;

import com.waterbucket.chatroom.model.ChatRoom;
import com.waterbucket.chatroom.model.Message;
import com.waterbucket.chatroom.repository.MessageRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message saveMessage(Message message) {
        final int MAX_RETRY_ATTEMPTS = 2;
        for (int i = 0; i < MAX_RETRY_ATTEMPTS; ) {
            try {
                @SuppressWarnings("UnnecessaryLocalVariable") var savedMessage  = messageRepository.save(message);
                return savedMessage;
            } catch (Exception e) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e1) {
                    logger.error("Thread sleep interrupted");
                    throw new RuntimeException(e1);
                }
                logger.warn("Optimistic lock exception occurred. Retry attempt: {}", i);
                ++i;
            }
        }
        throw new RuntimeException("Optimistic lock exception occurred.");
    }

    public List<Message> getMessagesByChatRoom(ChatRoom chatRoom) {
        return messageRepository.findByChatRoomOrderByCreateTime(chatRoom);
    }

    public Optional<Message> getReplyToMessage(UUID replyToId) {
        return messageRepository.findById(replyToId);
    }

    public void deleteMessage(UUID id) {
        messageRepository.deleteById(id);
    }

}
