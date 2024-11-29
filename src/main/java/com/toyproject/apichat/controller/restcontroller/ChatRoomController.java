package com.toyproject.apichat.controller.restcontroller;

import com.toyproject.apichat.dao.Messages;
import com.toyproject.apichat.dto.ChatRoom;
import com.toyproject.apichat.service.ChatService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {
    private final ChatService chatService;

    @GetMapping("/search")
    public ResponseEntity<List<Long>> search(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            List<Long> chatRoomBySessionId = chatService.findChatRoomBySessionId(session.getId());
            List<Long> chatRoomIdList = new ArrayList<>();
            for (Long l : chatRoomBySessionId) {
                ChatRoom chatRoomById = chatService.findChatRoomById(l);
                chatRoomIdList.add(chatRoomById.getRoomId());
            }
            return ResponseEntity.ok(chatRoomIdList);
        }

        return ResponseEntity.notFound().build();
    }
}
