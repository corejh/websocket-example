package com.example.wsdemo.socket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SimpleWebSocketHandler extends TextWebSocketHandler {

    // Store connected clients
    private static final Set<WebSocketSession> sessions =
            ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {

        sessions.add(session);

        System.out.println("Client connected: " + session.getId());

        session.sendMessage(
                new TextMessage("Connected to server")
        );
    }

    @Override
    protected void handleTextMessage(
            WebSocketSession session,
            TextMessage message
    ) throws Exception {

        String payload = message.getPayload();

        System.out.println("Received: " + payload);

        // Broadcast message to all connected clients
        for (WebSocketSession s : sessions) {

            if (s.isOpen()) {
                s.sendMessage(
                        new TextMessage("Server received: " + payload)
                );
            }
        }
    }

    @Override
    public void afterConnectionClosed(
            WebSocketSession session,
            CloseStatus status
    ) throws Exception {

        sessions.remove(session);

        System.out.println("Client disconnected: " + session.getId());
    }
}