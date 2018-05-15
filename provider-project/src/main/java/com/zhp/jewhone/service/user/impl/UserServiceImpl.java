package com.zhp.jewhone.service.user.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhp.jewhone.core.Result;
import com.zhp.jewhone.core.constant.Const;
import com.zhp.jewhone.core.constant.enums.ResponseEnum;
import com.zhp.jewhone.core.util.MD5Util;
import com.zhp.jewhone.core.util.StringUtils;
import com.zhp.jewhone.dao.user.UserMapper;
import com.zhp.jewhone.model.user.LoginInfo;
import com.zhp.jewhone.model.user.RegCondition;
import com.zhp.jewhone.model.user.User;
import com.zhp.jewhone.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional(propagation = Propagation.REQUIRED)//事务管理
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Result queryAllUsers(int pageNumber, int pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        List<User> userList = userMapper.selectAll();
        return new Result(ResponseEnum.SUCCESS, new PageInfo<>(userList));
    }

    @Override
    public Result addUser(RegCondition regCondition) {
        if (StringUtils.getInt(regCondition.getAccountType()) == 1) {//手机号注册
            User user = userMapper.queryUserByAccount(regCondition.getAccount());
            if (user != null) {
                return new Result(ResponseEnum.ACCOUNT_REGISTER);
            }
            if (!regCondition.getRegisterPwd().equals(regCondition.getConfirmPassword())){
                return new Result(ResponseEnum.PASSWORD_NOT_SAME);
            }
            MD5Util md5Util = MD5Util.getInstance();
            String password = md5Util.getMD5Code(regCondition.getRegisterPwd() + Const.SALT);//密码加密
            regCondition.setRegisterPwd(password);
        }
        userMapper.addUser(regCondition);
        return new Result(ResponseEnum.SUCCESS);
    }

    @Override
    public Result checkLogin(LoginInfo info) {
        User user = userMapper.queryUserByAccount(info.getAccount());
        if (user == null) {
            return new Result(ResponseEnum.ACCOUNT_NOT_EXIST);
        }
        if (user.getState() == 0) {
            return new Result(ResponseEnum.ACCOUNT_IS_LOCKED);
        }
        MD5Util md5Util = MD5Util.getInstance();
        String password = md5Util.getMD5Code(info.getPassword() + Const.SALT);
        if (!password.equals(user.getPassword())) {
            return new Result(ResponseEnum.ACCOUNT_PASSWORD_ERROR);
        }
        return new Result(ResponseEnum.SUCCESS);
    }
}
