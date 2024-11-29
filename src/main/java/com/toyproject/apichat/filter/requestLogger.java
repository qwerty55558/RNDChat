package com.toyproject.apichat.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
//@WebFilter(urlPatterns = "/login")
public class requestLogger implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        ContentCachingRequestWrapper httpRequest = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        ContentCachingResponseWrapper httpResponse = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

        filterChain.doFilter(servletRequest, servletResponse);

        String requestURI = httpRequest.getRequestURI();
        String contentAsString = httpRequest.getContentAsString();

        log.info("uri = {}, content = {}", requestURI, contentAsString);
        int status = httpResponse.getStatus();
        String s = new String(httpResponse.getContentAsByteArray());
        log.info("status = {}, content = {}", status, s);



    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
