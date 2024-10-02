package com.tstartup.tserver.common.response;


import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum ResultCodeEnum {

    ERROR_PWD(304, "Wrong Password"),

    INVALID_PARAM(306, "Invalid Parameter")

    ,SYSTEM_EXCEPTION(990, "System exception")

    ,DATA_ERROR(999, "data error")

    //处理异常
    ,HANDLING_EXCEPTIONS(997, "Handling Exceptions")

    //处理中
    ,IN_PROGRESS(996, "In progress")


    ;

    private final int code;

    private final String errorMessage;

    ResultCodeEnum(int code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return this.code;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * 所有的错误信息
     */
    public static final Map<Integer, String> ERROR_MAP;

    static {
        ERROR_MAP = Arrays.stream(ResultCodeEnum.values())
                .collect(Collectors.toMap(ResultCodeEnum::getCode, ResultCodeEnum::getErrorMessage));
    }
}
