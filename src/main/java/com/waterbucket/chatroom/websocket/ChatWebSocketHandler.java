package com.waterbucket.chatroom.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // todo handel received text messages
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // todo handling after establishing connection
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // todo handling after connection closed
    }
}
