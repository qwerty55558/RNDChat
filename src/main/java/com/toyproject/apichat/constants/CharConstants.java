package com.toyproject.apichat.constants;

public enum CharConstants {
    LOG_ID("logId"),
    MATCHING_ACKMESSAGE("ackMessage"),
    DELETEROOM_ACKMESSAGE("deleteRoom"),
    EXITROOM_ACKMESSAGE("exitChatRoom");


    private String value;

    CharConstants(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
