package com.waterbucket.chatroomAPI.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ChatRoomDTO {
    private String name;
    private UUID id;
    private List<UUID> messagesID;
    private UUID creatorID;
    private UUID otherUserID;
}
