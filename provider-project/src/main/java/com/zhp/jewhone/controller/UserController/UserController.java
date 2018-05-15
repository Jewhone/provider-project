package com.zhp.jewhone.controller.UserController;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import com.zhp.jewhone.controller.BaseController;
import com.zhp.jewhone.core.Result;
import com.zhp.jewhone.core.util.annotation.Validation;
import com.zhp.jewhone.model.user.LoginInfo;
import com.zhp.jewhone.model.user.RegCondition;
import com.zhp.jewhone.service.user.UserService;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @GetMapping(value = "/getAll")
    public Result getUsers() {
        return userService.queryAllUsers(1, 15);
    }

    @PostMapping(value = "/register")
    @Validation(notNull = {"username", "phone"})
    public Result addUser(@RequestBody RegCondition regCondition) {
        return userService.addUser(regCondition);
    }

    @PostMapping(value = "/checkLogin")
    public Result checkLogin(@RequestBody LoginInfo info){
        return userService.checkLogin(info);
    }

}