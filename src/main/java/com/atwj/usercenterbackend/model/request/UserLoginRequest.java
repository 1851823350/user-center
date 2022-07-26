package com.atwj.usercenterbackend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 吴先森
 * @description:
 * @create 2022-07-23 15:44
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 19124171637120793L;

    private String userAccount;

    private String userPassword;
}
