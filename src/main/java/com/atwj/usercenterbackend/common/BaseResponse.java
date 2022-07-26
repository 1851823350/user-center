package com.atwj.usercenterbackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 吴先森
 * @description: 通用返回对象
 * @create 2022-07-25 8:08
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(ReturnCode returnCode) {
        this(returnCode.getCode(), null, returnCode.getMessage(), returnCode.getDescription());
    }
}
