package com.toyproject.apichat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyproject.apichat.dao.Messages;
import com.toyproject.apichat.dto.AckMessage;
import com.toyproject.apichat.dto.ChatRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatService {
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<Long,ChatRoom> chatRooms;
    private final AtomicLong count = new AtomicLong(0);

    @Autowired
    public ChatService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.chatRooms = new ConcurrentHashMap<>();
    }

    public boolean removeChatRoomById(Long roomId, WebSocketSession session) {
        ChatRoom chatRoomById = findChatRoomById(roomId);
        chatRoomById.deleteSession(session);
        try {
            session.close();
        } catch (IOException e) {
            log.info("session close");
        }
        if (chatRoomById.getSessions().size() <= 1) {
            return true; // 인원 수가 적으니 방을 삭제
        }
        return false; // 삭제하지 않음
    }

    public ChatRoom findChatRoomById(Long id){
        return chatRooms.get(id);
    }

    public ChatRoom createChatRoom(WebSocketSession ... sessions){
        Long id = count.getAndIncrement();
        ChatRoom newChatRoom = ChatRoom.of(id, sessions);
        chatRooms.put(id, newChatRoom);
        return newChatRoom;
    }

    public List<Long> findChatRoomBySessionId(String sessionId) {
        return chatRooms.entrySet().stream()
                .filter(entry -> entry.getValue().getSessions().stream()
                        .anyMatch(session -> session.getId().equals(sessionId)))  // sessionId와 일치하는 세션이 있는지 확인
                .map(Map.Entry::getKey)  // 해당 chatRoom의 key 값 가져오기
                .collect(Collectors.toList());  // key 값들을 리스트로 수집
    }


    public void sendMessage(WebSocketSession session, Messages messages){
        try{
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(messages)));
        }catch (Exception e){
            log.info("send msg error");
            if (!session.isOpen()) {
                long chatRoomId = Long.parseLong(messages.getChatroomId());
                boolean isEmptyRoom = removeChatRoomById(chatRoomId, session);
                if (isEmptyRoom) {
                    ChatRoom chatRoomById = findChatRoomById(chatRoomId);
                    AckMessage deleteRoom = new AckMessage();
                    deleteRoom.setAckMessage("deleteRoom");
                    deleteRoom.setRoomId(String.valueOf(chatRoomId));
                    chatRoomById.getSessions().stream().forEach(c -> {
                        try {
                            c.sendMessage(new TextMessage(objectMapper.writeValueAsString(deleteRoom)));
                        } catch (IOException ex) {
                            log.info("delete room error");
                        }
                    });
                }

            }
        }
    }


}
