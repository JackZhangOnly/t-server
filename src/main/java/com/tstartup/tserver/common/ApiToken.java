package com.tstartup.tserver.common;

public class ApiToken {

    public static final byte STATS_INIT = 0;
    public static final byte STATS_BAN= 1;

    private String value;
    private String userId;
    private byte status;

    public ApiToken() {
    }

    public ApiToken(String value, String userId) {
        this.value = value;
        this.userId = userId;
        this.status = STATS_INIT;
    }

    public ApiToken(String value, String userId, byte status) {
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