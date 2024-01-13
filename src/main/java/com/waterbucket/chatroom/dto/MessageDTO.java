package com.waterbucket.chatroom.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class MessageDTO implements Serializable {
    private String content;
    private UUID id;
    private UUID senderID;
    private UUID receiverID;
    private UUID roomID;
}
