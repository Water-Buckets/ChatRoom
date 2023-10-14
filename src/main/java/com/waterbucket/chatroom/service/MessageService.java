package com.waterbucket.chatroom.service;

import com.waterbucket.chatroom.model.Message;
import com.waterbucket.chatroom.model.ChatRoom;
import com.waterbucket.chatroom.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByChatRoom(ChatRoom chatRoom) {
        return messageRepository.findByChatRoomOrderByTimestamp(chatRoom);
    }

    public Optional<Message> getReplyToMessage(UUID replyToId) {
        return messageRepository.findById(replyToId);
    }

    public void deleteMessage(UUID id) {
        messageRepository.deleteById(id);
    }

}
