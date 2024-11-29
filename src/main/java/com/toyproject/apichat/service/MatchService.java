package com.toyproject.apichat.service;

import com.toyproject.apichat.dto.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchService {

    private final ChatService chatService;

    private ConcurrentLinkedQueue<WebSocketSession> waitingQueue = new ConcurrentLinkedQueue<WebSocketSession>();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void onQueue(WebSocketSession session){
        waitingQueue.offer(session);
        scheduler.schedule(() -> {
            if (waitingQueue.contains(session)) {
                waitingQueue.remove(session);
                try {
                    session.close();
                } catch (IOException e) {
                    log.info("matching Error", e);
                    throw new RuntimeException(e);
                }
            }
        }, 20, TimeUnit.SECONDS);
        log.info("matching Session = {}", session);
        log.info("waitingQueue = {}", waitingQueue);
    }

    public Optional<ChatRoom> matchUsers() {
        WebSocketSession session1 = waitingQueue.poll();
        WebSocketSession session2 = waitingQueue.poll();

        if (session1 != null && session1.isOpen() && session2 != null && session2.isOpen()) {
            ChatRoom chatRoom = chatService.createChatRoom(session1, session2);
            return Optional.of(chatRoom);
        }

        // 유효하지 않은 세션은 매칭하지 않도록 처리
        if (session1 != null && session1.isOpen()) waitingQueue.offer(session1);
        if (session2 != null && session2.isOpen()) waitingQueue.offer(session2);

        return Optional.empty();
    }





}
