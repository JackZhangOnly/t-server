package com.tstartup.tserver.common.response;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;


public class ApiResponse<T> {

    public static final String MSG_SUCCESS = "success";

    public static final HashMap<String, Map<Integer, String>> ERROR_MAP;

    static {
        ERROR_MAP = new HashMap<>();
        ERROR_MAP.put("en", ResultCodeEnum.ERROR_MAP);

    }

    private int retCode;

    private String msg;

    private T data;

    public static <T> ApiResponse<T> newSuccess() {
        return newSuccess(null);
    }

    public static <T> ApiResponse<T> newSuccess(T data) {
        ApiResponse<T> ret = new ApiResponse<>();
        ret.setRetCode(HttpURLConnection.HTTP_OK);
        ret.setMsg(MSG_SUCCESS);
        ret.setData(data);
        return ret;
    }


    public static <T> ApiResponse<T> newError(ResultCodeEnum codeEnum) {
        return result(codeEnum.getCode());
    }



    public static <T> ApiResponse<T> newError(int code) {
        return result(code);
    }


    public static <T> ApiResponse<T> newError(int code, String msg) {
        return result(code, msg);
    }

    /**
     * 接口请求参数错误
     */
    public static <T> ApiResponse<T> newParamError() {
        return result(ResultCodeEnum.INVALID_PARAM.getCode());
    }

    /**
     * 接口请求参数错误
     */
    public static <T> ApiResponse<T> newParamError(String msg) {
        return result(HttpURLConnection.HTTP_BAD_REQUEST, msg);
    }

    /**
     * 系统错误
     */
    public static <T> ApiResponse<T> newSysError() {
        return result(999);
    }


    /**
     * 系统错误
     */
    @Deprecated
    public static <T> ApiResponse<T> newDataError(String msg) {
        return result(998, msg);
    }

    private static <T> ApiResponse<T> result(int code, String msg) {

        ApiResponse<T> ret = new ApiResponse<>();
        ret.setRetCode(code);
        ret.setMsg(msg);
        return ret;
    }

    private static <T> ApiResponse<T> result(int code) {
        Map<Integer, String> errorMap = ERROR_MAP.get("en");

        ApiResponse<T> ret = new ApiResponse<>();
        ret.setRetCode(code);
        ret.setMsg(errorMap.getOrDefault(code, "server error"));
        return ret;
    }



    public boolean successChecked() {
        return retCode == HttpURLConnection.HTTP_OK;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
