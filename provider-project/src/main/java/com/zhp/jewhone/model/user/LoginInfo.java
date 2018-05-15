package com.zhp.jewhone.model.user;

import java.io.Serializable;

public class LoginInfo implements Serializable {
    private static final long serialVersionUID = -1770232478301689194L;
    private String account;// 用户账号
    private String password;// 密码

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
