package com.yioj.userservice.controller.inner;

import com.yioj.clientservice.service.UserFeignClient;
import com.yioj.model.model.entity.User;
import com.yioj.userservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {
    @Resource
    private UserService userService;

    @GetMapping("/get/id")
    public User getById(@RequestParam("userId") long userId) {
        return userService.getById(userId);
    }

    @GetMapping("/get/list")
    public List<User> listByIds(@RequestParam("isList") Collection<Long> isList) {
        return listByIds(isList);
    }

}
