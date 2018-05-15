package com.zhp.jewhone.core.constant;

public interface Const {

    /**
     * 系统配置
     */
    String SPRING_PROFILES_ACTIVE_DEV = "dev";
    String SPRING_PROFILES_ACTIVE_ZHP = "zhp";
    String SYS_CONF_PATH = "sysconf.properties";
    String ERROR_MSG = "您的网络开了小差,请检查网络.";// 错误消息提示

    /**
     * 加密key
     */
    String SALT = "jewhone56key";

    /**
     * 爬虫平台
     */
    String BAIDU_SHARE_CONTENT = "百度平台分享";
    String ZHIHU_SHARE_CONTENT = "知乎平台分享";
    String WECHAT_SHARE_CONTENT = "微信平台分享";
    String IFENG_SHARE_CONTENT = "微信平台分享";

    /**
     * es
     */
    String ES_MOBILE_LOG = "";
    String DIARY_ELASTICSEARCH_INDEX_ID = "diary_elasticsearch_index_id";//diary index
    String HOTWORD_ELASTICSEARCH_INDEX_ID = "hotword_elasticsearch_index_id";//hotword index


    /**
     * 通用配置
     */
    public static String URL_REGEXP = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
    int[] DAY_ARR = new int[]{20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22};// 星座日期界限
    String REGEX_MOBILE = "^((14[0-9])|(17[0-9])|(13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";// 手机号验证正则表达式
    String[] CONSTELLATION_ARR = new String[]{"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
            "天蝎座", "射手座", "摩羯座"};// 12星座

    /**
     * 极光推送消息
     */
    String JPUSH_CUSTOM_MSG_PUSH_KEY = "jpush_custom_msg_push_key";// 极光推送自定义消息推送key
    String JPUSH_CUSTOM_MSG_PUSH_KEY_BUSINESS_TYPE = "jpush_custom_msg_push_key_business_type";// 极光推送自定义消息推送业务类型key

    String JPUSH_NOTIFICATION_MSG_PUSH_KEY = "jpush_notification_msg_push_key";// 极光推送自定义消息推送key
    String JPUSH_NOTIFICATION_MSG_PUSH_KEY_BUSINESS_TYPE = "jpush_notification_msg_push_key_business_type";// 极光推送自定义消息推送业务类型key

    String SYS_MSG_PUSH_CMENT_KEY = "msg_content";// 消息推送内容key
    String DEFAULT_MSG_PUSH_KEY = "default_msg_push_key";// 默认消息推送KEY类型

    /**
     * 文件编码
     */
    String FILE_WRITING_ENCODING = "UTF-8";
    String FILE_ISO_ENCODING="ISO-8859-1";

    String CONNECT_PWD = "jewhone56key";

    public static final String API_CODE_MSG_PREFIX_KEY = "api_code_msg_";
}
