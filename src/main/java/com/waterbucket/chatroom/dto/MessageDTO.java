package com.waterbucket.chatroom.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MessageDTO {
    private String content;
    private UUID id;
    private UUID senderID;
    private UUID receiverID;
    private UUID roomID;
}
