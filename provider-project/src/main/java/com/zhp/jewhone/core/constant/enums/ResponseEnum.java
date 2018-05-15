package com.zhp.jewhone.core.constant.enums;

import com.zhp.jewhone.core.constant.Const;
import com.zhp.jewhone.core.constant.ResponseStatus;
import com.zhp.jewhone.core.util.PropertiesUtil;

public enum ResponseEnum {

    SUCCESS(ResponseStatus.SUCCESS),// 请求成功
    ERROR(ResponseStatus.ERROR),

    ACCOUNT_REGISTER(1000),//帐号已注册
    ACCOUNT_NOT_EXIST(1001),//帐号不存在
    ACCOUNT_IS_LOCKED(1002),//帐号被锁定
    ACCOUNT_PASSWORD_ERROR(1003),//密码错误
    PASSWORD_NOT_SAME(1004),//前后密码不一致

    ;// 请求失败


    /**
     * 编码
     */
    private int value;
    /**
     * 描述
     */
    private String desc;

    // 构造方法
    private ResponseEnum(int value) {
        this.value = value;
        this.desc = PropertiesUtil.getApiCodeMsgByKey(Const.API_CODE_MSG_PREFIX_KEY + value);
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 获取枚举
     *
     * @param value
     * @return
     */
    public static ResponseEnum getByValue(int value) {
        for (ResponseEnum e : values()) {
            if (e.getValue() == value) {
                return e;
            }
        }
        return null;
    }
    }
