package com.atwj.usercenterbackend.common;

/**
 * @author 吴先森
 * @description: 返回码
 * @create 2022-07-25 8:37
 */
public enum ReturnCode {

    SUCCESS(0,"OK",""),
    PARAM_ERROR(4000, "请求参数异常", ""),
    NULL_ERROR(4001, "请求数据为空", ""),
    NOT_LOGIN(40100, "未登录", ""),
    NOT_AUTH(40101, "无权限", ""),
    SYSTEM_ERROR(5000,"系统内部异常","");

    //状态码
    private final int code;

    //状态码信息
    private final String message;

    //状态码描述（详情）
    private final String description;

    ReturnCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
