package com.toyproject.apichat.repository;

import com.toyproject.apichat.dao.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMessagesRepository extends JpaRepository<Messages, Long> {
}
