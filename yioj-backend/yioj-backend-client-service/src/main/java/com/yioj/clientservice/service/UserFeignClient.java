package com.yioj.clientservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yioj.common.common.ErrorCode;
import com.yioj.common.constant.UserConstant;
import com.yioj.common.exception.BusinessException;
import com.yioj.model.model.dto.user.UserQueryRequest;
import com.yioj.model.model.entity.User;
import com.yioj.model.model.enums.UserRoleEnum;
import com.yioj.model.model.vo.LoginUserVO;
import com.yioj.model.model.vo.UserRecordVO;
import com.yioj.model.model.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * 用户服务
 */
@FeignClient(name = "yioj-backend-user-service", path = "/api/user/inner")
public interface UserFeignClient {

    @GetMapping("/get/id")
    User getById(@RequestParam("userId") long userId);

    @GetMapping("/get/list")
    List<User> listByIds(@RequestParam("isList") Collection<Long> isList);

    default User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        //log.info(userObj.toString());
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    default boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }


    default boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    default UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    default User getLoginUserPermitNull(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        return currentUser;
    }
}
