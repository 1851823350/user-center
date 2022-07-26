package com.atwj.usercenterbackend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 吴先森
 * @description: 用戶注冊請求躰
 * @create 2022-07-23 15:30
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 19124171637120793L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;
}
