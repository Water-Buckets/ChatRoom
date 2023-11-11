package com.waterbucket.chatroom.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/chat")
public class ChatWebSocketHandler {

    private static final Set<ChatWebSocketHandler> clients = new CopyOnWriteArraySet<>();
    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        clients.add(this);
        logger.info("WebSocket connection opened: {}", session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("Received message from {}: {}", session.getId(), message);
        broadcast(message);
    }

    @OnClose
    public void onClose() {
        clients.remove(this);
        logger.info("WebSocket connection closed: {}", session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("WebSocket error occurred on session: {}", session.getId(), throwable);
    }

    private void broadcast(String message) {
        for (ChatWebSocketHandler client : clients) {
            try {
                if (client.session.isOpen()) {
                    client.session.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                logger.error("Error broadcasting message to client: {}", e.getMessage());
            }
        }
    }
}
