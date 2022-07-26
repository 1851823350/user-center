package com.atwj.usercenterbackend.service;

import com.atwj.usercenterbackend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 * @description 针对表【user】的数据库操作Service
 * @createDate 2022-07-23 08:42:03
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPasswords 用户密码
     * @param checkPassword 校验密码
     * @param planetCode
     * @return long
     * @author 吴先森
     * @date 2022/7/23 9:41
     */
    long userRegister(String userAccount, String userPasswords, String checkPassword, String planetCode);

    /**
     * 用户登陆
     * @author 吴先森
     * @date 2022/7/23 14:32
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param request
     * @return com.atwj.usercenterbackend.model.domain.User
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 进行用户脱敏
     * @author 吴先森
     * @date 2022/7/24 15:49
     * @param originUser 脱敏对象
     * @return com.atwj.usercenterbackend.model.domain.User
     */
    User getSafetyUser(User originUser);

    /**
     * 注销用户
     * @author 吴先森
     * @date 2022/7/24 15:51
     * @return int
     */
    int userLogout(HttpServletRequest request);
}
