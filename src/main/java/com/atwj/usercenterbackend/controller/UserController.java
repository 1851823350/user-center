package com.atwj.usercenterbackend.controller;

import com.atwj.usercenterbackend.common.BaseResponse;
import com.atwj.usercenterbackend.common.ReturnCode;
import com.atwj.usercenterbackend.exception.BusinessException;
import com.atwj.usercenterbackend.model.domain.User;
import com.atwj.usercenterbackend.model.request.UserLoginRequest;
import com.atwj.usercenterbackend.model.request.UserRegisterRequest;
import com.atwj.usercenterbackend.service.UserService;
import com.atwj.usercenterbackend.common.ResultUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.atwj.usercenterbackend.contant.UserContant.ADMIN_ROLE;
import static com.atwj.usercenterbackend.contant.UserContant.USER_LOGIN_STATE;

/**
 * @author 吴先森
 * @description:
 * @create 2022-07-23 15:19
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    //注册用户
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ReturnCode.PARAM_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            return ResultUtils.error(ReturnCode.NULL_ERROR,"注册信息不能为空");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);
    }

    //用户登陆
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ReturnCode.PARAM_ERROR,"请正确输入登陆用户信息");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw  new BusinessException(ReturnCode.PARAM_ERROR,"请输入正确输入用户信息");
        }
        User resultUser = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(resultUser);
    }

    //获取登陆用户的信息
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw  new BusinessException(ReturnCode.PARAM_ERROR);
        }
        Long id = currentUser.getId();
        //查询用户最新的信息
        User resultUser = userService.getById(id);
        User safetyUser = userService.getSafetyUser(resultUser);
        return ResultUtils.success(safetyUser);
    }

    //管理员查询用户
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {

        //判断是否为管理员
        if (!isAdmin(request)) {
            throw  new BusinessException(ReturnCode.PARAM_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> resultUsers = userList.stream().map(user -> {
            user.setUserPassword(null);
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());
        return ResultUtils.success(resultUsers);
    }

    //删除用户
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw  new BusinessException(ReturnCode.PARAM_ERROR);
        }
        if (id <= 0) {
            throw  new BusinessException(ReturnCode.PARAM_ERROR);
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    //注销用户
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw  new BusinessException(ReturnCode.PARAM_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 判断是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        //判断是否为管理员
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }


}
