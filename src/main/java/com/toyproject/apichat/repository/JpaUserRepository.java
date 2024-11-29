package com.toyproject.apichat.repository;

import com.toyproject.apichat.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);
    Optional<User> deleteByUserName(String email);
    Optional<User> findByUuid(String uuid);
}
