package com.toyproject.apichat.dto;

import lombok.Data;

@Data
public class AckMessage {
    private String ackMessage;
    private String roomId;
}
