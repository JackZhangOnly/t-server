package com.tstartup.tserver.common;

public class ApiAuthToken {

    public static final byte STATS_INIT = 0;
    private String value;
    private String userId;
    private byte status;

    public ApiAuthToken() {
    }

    public ApiAuthToken(String value, String userId) {
        this.value = value;
        this.userId = userId;
        this.status = STATS_INIT;
    }

    public ApiAuthToken(String value, String userId, byte status) {
        this.value = value;
        this.userId = userId;
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public void resetValue() {
        this.value = "";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}