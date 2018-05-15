package com.zhp.jewhone.model.user;

import java.io.Serializable;
import java.util.Date;

public class RegCondition implements Serializable {
    private static final long serialVersionUID = 1335185172061380027L;
    private String username;//用户名称
    private String account; //手机号或者是第三方登录的 openId
    private String registerPwd; //密码
    private String confirmPassword;//确认密码
    private String email;//用户邮箱
    private Integer accountType; // 1.手机号注册 2.微信注册 3.qq注册

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRegisterPwd() {
        return registerPwd;
    }

    public void setRegisterPwd(String registerPwd) {
        this.registerPwd = registerPwd;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

