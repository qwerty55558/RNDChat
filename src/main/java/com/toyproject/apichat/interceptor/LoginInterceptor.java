package com.toyproject.apichat.interceptor;

import com.toyproject.apichat.constants.CharConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;


@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    private static final String LOGID = CharConstants.LOG_ID.getValue();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(LOGID) == null) {
            if (!request.getRequestURI().equals("/home")){
                response.sendRedirect(request.getContextPath() + "/home");
                return false;
            }
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        if (request.getRequestURI().equals("/error")) {
            response.sendRedirect(request.getContextPath() + "/home");
            return false;
        }

        return true;
    }
}
