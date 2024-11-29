package com.toyproject.apichat.controller;

import com.toyproject.apichat.constants.CharConstants;
import com.toyproject.apichat.constants.NumConstants;
import com.toyproject.apichat.dao.User;
import com.toyproject.apichat.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    private final static String LOGID = CharConstants.LOG_ID.getValue();
    private final static int REMAIN_INTERVAL = NumConstants.SESSION_REMAIN_INTERVAL.getValue();

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute User user, HttpServletRequest request, HttpServletResponse response) {
        if (request.getSession(false) == null) {
            user.setUuid(UUID.randomUUID().toString());
            User save = userService.save(user);
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(REMAIN_INTERVAL);
            session.setAttribute(LOGID, save.getUserId());
            log.info("sessionAttribute = {}", session.getAttribute(LOGID).toString());
            log.info("save = {}",save);
        }
        log.info("user = {}", user);
        return "redirect:/home";
    }
}
