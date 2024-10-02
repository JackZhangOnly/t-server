package com.tstartup.tserver.common.response;

import com.google.common.base.Strings;
import org.springframework.util.CollectionUtils;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;


public class ApiResult<T> {

    public static final String MSG_SUCCESS = "success";

    public static final HashMap<String, Map<Integer, String>> LANGUAGE_ERROR_MAP;

    static {
        LANGUAGE_ERROR_MAP = new HashMap<>();
        LANGUAGE_ERROR_MAP.put("en", ResultCodeEnum.ERROR_MAP);

    }

    private int retCode;

    private String msg;

    private T data;

    public static <T> ApiResult<T> newSuccess() {
        return newSuccess(null);
    }

    public static <T> ApiResult<T> newSuccess(T data) {
        ApiResult<T> ret = new ApiResult<>();
        ret.setRetCode(HttpURLConnection.HTTP_OK);
        ret.setMsg(MSG_SUCCESS);
        ret.setData(data);
        return ret;
    }

    public static <T> ApiResult<T> newSuccessWithMsg(String msgContent) {
        ApiResult<T> ret = new ApiResult<>();
        ret.setRetCode(HttpURLConnection.HTTP_OK);
        ret.setMsg(msgContent);
        return ret;
    }

    public static <T> ApiResult<T> newError(ResultCodeEnum codeEnum) {
        return result(codeEnum.getCode());
    }

    public static <T> ApiResult<T> newSuccessWithCodeEnum(ResultCodeEnum codeEnum) {
        ApiResult<T> ret = result(codeEnum.getCode());
        ret.setRetCode(HttpURLConnection.HTTP_OK);
        return ret;
    }


    public static <T> ApiResult<T> newError(int code) {
        return result(code);
    }

    public static <T> ApiResult<T> newError(ResultCodeEnum codeEnum, Object... elements) {
        Map<Integer, String> errorMap = LANGUAGE_ERROR_MAP.get("en");
        //}
        ApiResult<T> ret = new ApiResult<>();
        ret.setRetCode(codeEnum.getCode());
        ret.setMsg(String.format(errorMap.getOrDefault(codeEnum.getCode(), "server error"), elements));
        return ret;
    }

    public static <T> ApiResult<T> newDataError(ResultCodeEnum codeEnum, T data) {
        ApiResult<T> ret = new ApiResult<>();
        ret.setRetCode(codeEnum.getCode());
        Map<Integer, String> errorMap = LANGUAGE_ERROR_MAP.get("en");
        ret.setMsg(String.format(errorMap.getOrDefault(codeEnum.getCode(), "server error")));
        ret.setData(data);
        return ret;
    }

    @Deprecated
    public static <T> ApiResult<T> newError(int code, String msg) {
        return result(code, msg);
    }

    /**
     * 接口请求参数错误
     */
    public static <T> ApiResult<T> newParamError() {
        return result(ResultCodeEnum.INVALID_PARAM.getCode());
    }

    /**
     * 接口请求参数错误
     */
    public static <T> ApiResult<T> newParamError(String msg) {
        return result(HttpURLConnection.HTTP_BAD_REQUEST, msg);
    }

    /**
     * 系统错误
     */
    public static <T> ApiResult<T> newSysError() {
        return result(999);
    }

    /**
     * 系统错误
     */
    public static <T> ApiResult<T> newDataError() {
        return result(998);
    }

    /**
     * 系统错误
     */
    @Deprecated
    public static <T> ApiResult<T> newDataError(String msg) {
        return result(998, msg);
    }

    private static <T> ApiResult<T> result(int code, String msg) {

        ApiResult<T> ret = new ApiResult<>();
        ret.setRetCode(code);
        ret.setMsg(msg);
        return ret;
    }

    private static <T> ApiResult<T> result(int code) {
        Map<Integer, String> errorMap = LANGUAGE_ERROR_MAP.get("en");

        ApiResult<T> ret = new ApiResult<>();
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
