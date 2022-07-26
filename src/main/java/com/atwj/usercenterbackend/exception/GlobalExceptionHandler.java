package com.atwj.usercenterbackend.exception;

import com.atwj.usercenterbackend.common.BaseResponse;
import com.atwj.usercenterbackend.common.ResultUtils;
import com.atwj.usercenterbackend.common.ReturnCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 吴先森
 * @description:
 * @create 2022-07-25 9:52
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ReturnCode.SYSTEM_ERROR, e.getMessage(), "");
    }
}
