package com.waterbucket.chatroom.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class ChatRoomDTO implements Serializable {
    private String name;
    private UUID id;
    private UUID creatorID;
    private UUID otherUserID;
    private List<UUID> messagesID;
}
