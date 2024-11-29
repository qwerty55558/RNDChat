package com.toyproject.apichat.dto;

import com.toyproject.apichat.dao.Messages;
import com.toyproject.apichat.service.ChatService;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ChatRoom {
    @Getter
    private Long roomId;

    @Getter
    private Set<WebSocketSession> sessions = new HashSet<WebSocketSession>();

    private ChatRoom(Long roomId, WebSocketSession[] user) {
        this.roomId = roomId;
        sessions.addAll(Arrays.asList(user));
    }

    public void sendMessage(Messages message, ChatService chatService) {
        sessions
                .parallelStream()
                .forEach(session -> chatService.sendMessage(session, message));
    }

    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void deleteSession(WebSocketSession session) {
        Optional<WebSocketSession> first = sessions.stream().filter(c -> c.getId().equals(session.getId())).findFirst();
        if (first.isPresent()) {
            sessions.remove(session);
        }
    }

    public static ChatRoom of(Long roomId,WebSocketSession ... user) {
        return new ChatRoom(roomId,user);
    }

}
