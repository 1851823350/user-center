package com.atwj.usercenterbackend.exception;

import com.atwj.usercenterbackend.common.ReturnCode;

/**
 * @author 吴先森
 * @description:
 * @create 2022-07-25 9:21
 */
public class BusinessException extends RuntimeException {

    private final int code;

    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ReturnCode returnCode) {
        super(returnCode.getMessage());
        this.code = returnCode.getCode();
        this.description = returnCode.getDescription();
    }

    public BusinessException(ReturnCode returnCode, String description) {
        super(returnCode.getMessage());
        this.code = returnCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
