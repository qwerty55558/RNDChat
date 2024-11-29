package com.toyproject.apichat.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyproject.apichat.constants.CharConstants;
import com.toyproject.apichat.dao.Messages;
import com.toyproject.apichat.dto.AckMessage;
import com.toyproject.apichat.dto.ChatRoom;
import com.toyproject.apichat.repository.JpaMessagesRepository;
import com.toyproject.apichat.service.ChatService;
import com.toyproject.apichat.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Optional;


@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final MatchService matchService;
    private final ChatService chatService;
    private final JpaMessagesRepository jpaMessagesRepository;

    private final static String ACK_MESSAGE = CharConstants.MATCHING_ACKMESSAGE.getValue();
    private final static String DELETE_MESSAGE = CharConstants.DELETEROOM_ACKMESSAGE.getValue();
    private final static String EXIT_MESSAGE = CharConstants.EXITROOM_ACKMESSAGE.getValue();

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Connection closed");
        super.afterConnectionClosed(session, status);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        Messages originalMsg = objectMapper.readValue(payload, Messages.class);
        Messages messages = jpaMessagesRepository.save(originalMsg);
        log.info("message = {}",messages);

        if (messages.getMessageType().equals("0")) {
            ChatRoom chatRoomById = chatService.findChatRoomById(Long.parseLong(messages.getChatroomId()));
            try{
                chatRoomById.sendMessage(messages, chatService);
            }catch (Exception e){
                log.info("msg sending issue");
            }
        }

        if (messages.getMessageType().equals("1")) {
            matchService.onQueue(session);
            Optional<ChatRoom> chatRoom = matchService.matchUsers();
            if (chatRoom.isPresent()) {
                log.info("match success");
                AckMessage ackMessage = new AckMessage();
                ackMessage.setAckMessage(ACK_MESSAGE);
                ackMessage.setRoomId(String.valueOf(chatRoom.get().getRoomId()));
                chatRoom.get().getSessions().stream().forEach(c -> {
                    try {
                        c.sendMessage(new TextMessage(objectMapper.writeValueAsString(ackMessage)));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }

        if (messages.getMessageType().equals("2")) {
            log.info("exitChatRoom");
            AckMessage ackMessage = new AckMessage();
            ackMessage.setAckMessage(EXIT_MESSAGE);
            ackMessage.setRoomId(messages.getChatroomId());
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(ackMessage)));
            boolean b = chatService.removeChatRoomById(Long.parseLong(messages.getChatroomId()), session);
            if (b) {
                ChatRoom chatRoomById = chatService.findChatRoomById(Long.valueOf(messages.getChatroomId()));
                chatRoomById.getSessions().stream().forEach(c -> {
                    try {
                        ackMessage.setAckMessage(DELETE_MESSAGE);
                        c.sendMessage(new TextMessage(objectMapper.writeValueAsString(ackMessage)));
                        c.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }



    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connection established ");
    }
}
