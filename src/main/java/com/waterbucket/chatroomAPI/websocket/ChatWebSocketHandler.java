package com.waterbucket.chatroomAPI.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@ServerEndpoint("/chat")
public class ChatWebSocketHandler {

    private static final Set<ChatWebSocketHandler> clients = new CopyOnWriteArraySet<>();
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        clients.add(this);
        log.info("WebSocket connection opened: {}", session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("Received message from {}: {}", session.getId(), message);
        broadcast(message);
    }

    @OnClose
    public void onClose() {
        clients.remove(this);
        log.info("WebSocket connection closed: {}", session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("WebSocket error occurred on session: {}", session.getId(), throwable);
    }

    private void broadcast(String message) {
        for (ChatWebSocketHandler client : clients) {
            try {
                if (client.session.isOpen()) {
                    client.session.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                log.error("Error broadcasting message to client: {}", e.getMessage());
            }
        }
    }
}
