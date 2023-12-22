package com.waterbucket.chatroomAPI.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MessageDTO {
    private String content;
    private UUID id;
    private UUID roomID;
    private UUID senderID;
    private UUID receiverID;
}
