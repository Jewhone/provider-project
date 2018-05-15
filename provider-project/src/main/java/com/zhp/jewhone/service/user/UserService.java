package com.zhp.jewhone.service.user;

import com.zhp.jewhone.core.Result;
import com.zhp.jewhone.model.user.LoginInfo;
import com.zhp.jewhone.model.user.RegCondition;

public interface UserService {
	/**
	 * 所有用户
	 * @return
	 */
	Result queryAllUsers(int page, int rows);

	/**
	 * 添加用户
	 * @param regCondition
	 * @return
	 */
	Result addUser(RegCondition regCondition);

	/**
	 * 登录验证
	 * @param info
	 * @return
	 */
    Result checkLogin(LoginInfo info);
}
