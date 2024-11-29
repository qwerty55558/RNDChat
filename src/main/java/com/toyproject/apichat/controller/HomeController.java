package com.toyproject.apichat.controller;

import com.toyproject.apichat.constants.CharConstants;
import com.toyproject.apichat.dao.User;
import com.toyproject.apichat.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    private final String LOGID = CharConstants.LOG_ID.getValue();

    @Value("${server.ip}")
    private String serverIp;

    @Value("${server.web.port}")
    private String serverWebPort;

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {



        HttpSession session = request.getSession(false);
        Integer uuid = Integer.parseInt(session.getAttribute(LOGID).toString());
        User byUuid = userService.findById(uuid);

        model.addAttribute("user", byUuid);
        model.addAttribute("serverIp", serverIp);
        model.addAttribute("serverWebPort", serverWebPort);

        return "home";
    }
}
