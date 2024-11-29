package com.toyproject.apichat.constants;

public enum NumConstants {
    REQUEST_INTERVAL_LIMIT(20000),
    SESSION_REMAIN_INTERVAL(1800);

    private final int value;

    NumConstants(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

}
