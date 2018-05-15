package com.zhp.jewhone.dao.user;

import com.zhp.jewhone.model.user.RegCondition;
import com.zhp.jewhone.model.user.User;

import java.util.List;

public interface UserMapper{
    /**
     * 添加用户
     * @param regCondition
     */
    void addUser(RegCondition regCondition);

    /**
     * 查询全部用户
     * @return
     */
    List<User> selectAll();

    /**
     * 根据帐号查询用户
     * @param account
     * @return
     */
    User queryUserByAccount(String account);
}