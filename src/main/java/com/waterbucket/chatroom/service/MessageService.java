package com.waterbucket.chatroom.service;

import com.waterbucket.chatroom.model.ChatRoom;
import com.waterbucket.chatroom.model.Message;
import com.waterbucket.chatroom.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomService chatRoomService;

    @Autowired
    public MessageService(MessageRepository messageRepository, ChatRoomService chatRoomService) {
        this.messageRepository = messageRepository;
        this.chatRoomService = chatRoomService;
    }

    public Mono<Message> saveMessage(Mono<Message> message, UUID roomId) {
        return message.flatMap(m -> {
            //noinspection CallingSubscribeInNonBlockingScope
            chatRoomService.getChatRoomById(roomId).map(chatRoom -> {
                m.setChatRoom(chatRoom);
                m.setCreateTime(LocalDateTime.now());
                return chatRoom;
            }).subscribe();
            return messageRepository.save(m);
        });
    }

    public Flux<Message> getMessagesByChatRoom(Mono<ChatRoom> chatRoom) {
        return chatRoom.flatMapMany(messageRepository::findByChatRoomOrderByCreateTime);
    }

    public Mono<Void> deleteMessage(UUID id) {
        return messageRepository.deleteById(id);
    }

}
