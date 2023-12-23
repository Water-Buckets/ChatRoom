package com.waterbucket.chatroom.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ChatRoomDTO {
    private String name;
    private UUID id;
    private UUID creatorID;
    private UUID otherUserID;
    private List<UUID> messagesID;
}
