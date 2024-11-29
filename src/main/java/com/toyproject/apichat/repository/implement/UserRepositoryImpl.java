package com.toyproject.apichat.repository.implement;

import com.toyproject.apichat.dao.User;
import com.toyproject.apichat.repository.JpaUserRepository;
import com.toyproject.apichat.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserService {

    private final JpaUserRepository jpaUserRepository;

    @Override
    @Transactional
    public User save(User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public User findById(Integer id) {
        return jpaUserRepository.findById(id.longValue()).orElse(null);
    }

    @Override
    public User findByUuid(String uuid) {
        return jpaUserRepository.findByUuid(uuid).orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        return jpaUserRepository.findByUserName(username).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        jpaUserRepository.deleteById(id.longValue());
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        jpaUserRepository.deleteByUserName(username);
    }


}
