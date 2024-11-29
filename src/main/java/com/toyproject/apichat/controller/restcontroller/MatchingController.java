package com.toyproject.apichat.controller.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyproject.apichat.constants.CharConstants;
import com.toyproject.apichat.constants.NumConstants;
import com.toyproject.apichat.dao.User;
import com.toyproject.apichat.service.MatchService;
import com.toyproject.apichat.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MatchingController {

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final MatchService matchService;

    private final long INTERVAL = NumConstants.REQUEST_INTERVAL_LIMIT.getValue();
    private final String LOGID = CharConstants.LOG_ID.getValue();

    @CrossOrigin(origins = "*")
    @GetMapping("/matching")
    public ResponseEntity<User> matching(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);

            if (session == null || session.getAttribute(LOGID) == null) {
                throw new IllegalStateException("No valid session found.");
            }


            String userId = session.getAttribute(LOGID).toString();

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Long lastRequestTime = (Long) session.getAttribute("lastRequestTime");
            long currentTime = System.currentTimeMillis();

            if (lastRequestTime != null && (currentTime - lastRequestTime) < INTERVAL) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
            }

            session.setAttribute("lastRequestTime", currentTime);

            User user = userService.findById(Integer.parseInt(userId));
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // 예외 처리
            log.error("Error occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
