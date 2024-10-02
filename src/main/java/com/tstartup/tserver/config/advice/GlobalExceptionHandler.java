package com.tstartup.tserver.config.advice;

import com.tstartup.tserver.common.response.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@ControllerAdvice
public class GlobalExceptionHandler {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @ExceptionHandler(value = {Throwable.class})
    public Object onException(Exception e) {

        log.error(e.getMessage(), e);

        if (e instanceof MissingServletRequestParameterException) {
            return ApiResult.newParamError(e.getMessage());
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            return ApiResult.newParamError(e.getMessage());
        } else if (e instanceof HttpMessageNotReadableException) {
            return ApiResult.newParamError();
        }

        return ApiResult.newSysError();
    }

    /**
     * 统一参数绑定异常处理
     *
     * @param e 异常队形
     * @return 返回结果
     */
    @ResponseBody
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public Object validExceptionHandler(MethodArgumentNotValidException e) {
        StringBuilder message = new StringBuilder("参数错误: ");
        // 参数校验异常
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        for (ObjectError allError : allErrors) {
            FieldError fieldError = (FieldError) allError;
            message.append(fieldError.getField())
                    .append(" ")
                    .append(fieldError.getDefaultMessage())
                    .append(" !!! ");
        }
        return ApiResult.newParamError(message.toString());
    }
}
