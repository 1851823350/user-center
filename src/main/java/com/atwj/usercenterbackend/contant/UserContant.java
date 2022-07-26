package com.atwj.usercenterbackend.contant;

/**
 * @author 吴先森
 * @description: 定义用户常量
 * @create 2022-07-23 17:00
 */
public interface UserContant {
    //用户登陆状态
    String USER_LOGIN_STATE = "userLoginState";

    // ------- 权限 --------
    /**
     * 默认权限
     */
    int DEFAULT_ROLE = 0;

    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;
}
