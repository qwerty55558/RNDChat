package com.toyproject.apichat.service;

import com.toyproject.apichat.dao.User;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    User save(User user);
    User findById(Integer id);
    User findByUsername(String username);
    User findByUuid(String uuid);
    void deleteById(Integer id);
    void deleteByUsername(String username);
}
