package com.atwj.usercenterbackend.service.Impl;


import com.atwj.usercenterbackend.common.ReturnCode;
import com.atwj.usercenterbackend.exception.BusinessException;
import com.atwj.usercenterbackend.model.domain.User;
import com.atwj.usercenterbackend.service.UserService;
import com.atwj.usercenterbackend.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.atwj.usercenterbackend.contant.UserContant.USER_LOGIN_STATE;

/**
 * 用户服务实现
 *
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2022-07-23 08:42:03
 */
@Service
@Slf4j //日志，记录信息，可以查错
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    //盐值，用于加密
    private static final String SALT = "yupi";

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ReturnCode.NULL_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ReturnCode.PARAM_ERROR, "用户账号太短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ReturnCode.PARAM_ERROR, "密码或者确认密码太短");
        }
        if (planetCode.length() > 5) {
            throw new BusinessException(ReturnCode.PARAM_ERROR, "星球编号错误");
        }

        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\\\\\[\\\\\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ReturnCode.PARAM_ERROR,"账户含有非法字符");
        }
        // 密码和校验密码不相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ReturnCode.PARAM_ERROR,"两次密码不同");
        }

        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ReturnCode.PARAM_ERROR,"账户重复");
        }

        // 星球编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ReturnCode.PARAM_ERROR,"星球编号重复");
        }
        // 2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ReturnCode.PARAM_ERROR);
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ReturnCode.PARAM_ERROR,"用户账号或者密码输入为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ReturnCode.PARAM_ERROR,"用户账号输入不合法");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ReturnCode.PARAM_ERROR,"密码输入不合法");
        }

        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\\\\\[\\\\\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ReturnCode.PARAM_ERROR,"用户账户含有非法字符");
        }

        // 2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 查找用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) { //如果用户不存在
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ReturnCode.NULL_ERROR,"用户账号或者密码输入为空");
        }
        //3. 脱敏
        User safeUser = getSafetyUser(user);
        //4. 记录用户登陆状态
        request.getSession().setAttribute(USER_LOGIN_STATE, safeUser);
        return safeUser;
    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            throw new BusinessException(ReturnCode.PARAM_ERROR);
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        return safetyUser;
    }

    // 注销用户
    @Override
    public int userLogout(HttpServletRequest request) {
        //移出登陆状态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        throw new BusinessException(ReturnCode.SUCCESS);
    }
}




