package com.atwj.usercenterbackend.common;

/**
 * @author 吴先森
 * @description: 返回同一对象工具类
 * @create 2022-07-25 8:18
 */
public class ResultUtils {
    //成功
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(200, data, "OK");
    }

    //失败
    public static BaseResponse error(ReturnCode returnCode) {
        return new BaseResponse<>(returnCode);
    }

    /**
     * 失败
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse(code, null, message, description);
    }

    /**
     * 失败
     * @param returnCode
     * @return
     */
    public static BaseResponse error(ReturnCode returnCode, String message, String description) {
        return new BaseResponse(returnCode.getCode(), null, message, description);
    }

    /**
     * 失败
     * @param returnCode
     * @return
     */
    public static BaseResponse error(ReturnCode returnCode, String description) {
        return new BaseResponse(returnCode.getCode(), returnCode.getMessage(), description);
    }
}
