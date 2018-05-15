package com.zhp.jewhone.core.constant;

/**
 * 状态码
 *
 * @author ZHP.
 *
 */
public interface ResponseStatus {
    // 请求成功
    int SUCCESS = 1;
    // 请求失败
    int ERROR = 0;
    /**
     * http status 状态码
     */
    int API_SECRET_PAST_DUE_STATUS = 5002;// 过期状态
    int API_SIGN_ILLEGAL_STATUS = 5003;// 签名不合法
    int API_HTTP_OK_STATUS = 200;// 正常
}
