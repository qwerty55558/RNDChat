package com.toyproject.apichat.config;

import com.toyproject.apichat.config.handler.SessionClosedHandler;
import com.toyproject.apichat.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**","/js/**","/images/**","/fonts/**","/login");
    }



    @Bean
    public SessionClosedHandler SessionClosedHandler(){
        return new SessionClosedHandler();
    }
}
