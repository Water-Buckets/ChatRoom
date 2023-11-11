package com.waterbucket.chatroom.service;

import com.waterbucket.chatroom.model.ChatRoom;
import com.waterbucket.chatroom.repository.ChatRoomRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CharSequence.class);

    @Autowired
    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoom saveChatRoom(ChatRoom chatRoom) {
        final int MAX_RETRY_ATTEMPTS = 2;
        for (int i = 0; i < MAX_RETRY_ATTEMPTS; ) {
            try {
                @SuppressWarnings("UnnecessaryLocalVariable") var savedChatRoom = chatRoomRepository.save(chatRoom);
                return savedChatRoom;
            } catch (Exception e) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e1) {
                    logger.error("Thread sleep interrupted");
                    throw new RuntimeException(e1);
                }
                logger.info("Optimistic lock exception occurred. Retry attempt: {}", i);
                ++i;
            }
        }
        throw new RuntimeException("Optimistic lock exception occurred.");
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
