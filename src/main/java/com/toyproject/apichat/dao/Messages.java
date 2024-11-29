package com.toyproject.apichat.dao;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "messages")
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    String messageId;
    @Column(name = "sender")
    Integer sender;
    @Column(name = "message")
    String message;
    @Column(name = "chatroom_id")
    String chatroomId;
    @Column(name = "message_type")
    String messageType;
}
