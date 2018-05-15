package com.zhp.jewhone.core.constant.enums;

import com.zhp.jewhone.core.constant.Const;
import com.zhp.jewhone.core.util.StringUtils;

public enum SourceTypeEnum {
    /**
     * 来自html
     */
    FROM_NETEASE(2, "网易"),
    FROM_TOUTIAO(3, "头条"),
    FROM_EASYBOOK(4, "简书"),
    FROM_ZHIHU(5, "知乎"),
    FROM_36KR(7, "36kr"),
    FROM_PAPER(12, "澎湃"),
    FROM_TENCENT(17, "腾讯"),
    FROM_SOHU(18, "搜狐");

    private Integer sourceType;
    private String sourceTypeStr;

    SourceTypeEnum(Integer sourceType, String sourceTypeStr) {
        this.sourceType = sourceType;
        this.sourceTypeStr = sourceTypeStr;
    }

    /**
     * 根据类型获取枚举实例
     *
     * @param sourceType
     */
    public static SourceTypeEnum getByType(Integer sourceType) {
        for (SourceTypeEnum e : values()) {
            if (e.getSourceType().equals(sourceType)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 根据类型获取枚举int
     *
     * @param sourceTypeStr
     * @return
     */
    public static Integer getByType(String sourceTypeStr) {
        for (SourceTypeEnum e : values()) {
            if (e.getSourceTypeStr().equals(sourceTypeStr)) {
                return e.getSourceType();
            }
        }
        return null;
    }

    /**
     * 根据字符串获取枚举实例
     *
     * @param str
     */
    public static SourceTypeEnum getEnumByStr(String str) {
        if (str == null) {
            return null;
        } else if (str.indexOf("163.com/news") > 0) { // 网易
            return FROM_NETEASE;
        } else if (str.indexOf("iid=") > 0 && str.indexOf("&app=news_article") > 0) { // 头条
            return FROM_TOUTIAO;
        } else if (str.indexOf("www.jianshu.com/p/") > 0) { // 简书
            return FROM_EASYBOOK;
        } else if (str.indexOf("zhuanlan.zhihu.com/p/") > 0) { // 知乎
            return FROM_ZHIHU;
        }
        // else if (str.indexOf("guancha.cn/toutiao") > 0) { // 观察者网
        // return FROM_TOUTIAO;
        // } else if (str.indexOf("youth.cn/transfer") > 0) { // 中国青年网
        // return FROM_TOUTIAO;
        // } else if (str.indexOf("m.gmw.cn/") > 0) { // 光明网
        // return FROM_TOUTIAO;
        // } else if (str.indexOf("m.huanqiu.com/") > 0) { // 环球网
        // return FROM_TOUTIAO;
        // } else if (str.indexOf("m.ce.cn/") > 0) { // 中国经济网
        // return FROM_TOUTIAO;
        // } else if (str.indexOf("m.k618.cn/jrtt/") > 0) { // 未来网
        // return FROM_TOUTIAO;
        // } else if (str.indexOf("snssdk.com/") > 0) { // 会生活
        // return FROM_TOUTIAO;
        // }
        return null;
    }


    /**
     * 根据字符串获取字符串中的url
     *
     * @param str
     */
    public static String getUrlByStr(String str) {
        String url = "";
        try {
            SourceTypeEnum sourceTypeEnum = SourceTypeEnum.getEnumByStr(str);
            switch (sourceTypeEnum) {
                case FROM_NETEASE:
                    String[] strs = str.split("\n");
                    return getUrl(str, strs);
                case FROM_TOUTIAO:
                    String[] arr = str.split("\n");
                    if (arr.length == 1) {
                        url = arr[0];
                    } else {
                        url = arr[1];
                    }
                    return url;
                case FROM_EASYBOOK:
                    return StringUtils.getRegExp(Const.URL_REGEXP, str);
                case FROM_ZHIHU:
                    return StringUtils.getRegExp(Const.URL_REGEXP, str);
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String getUrl(String str, String[] arr1) {
        String url;
        if (arr1.length == 1) {
            url = arr1[0];
        } else if (arr1.length == 5 && str.indexOf("https://c.m.163.com/news/") > 0) {
            url = arr1[2];
        } else {
            url = "https://c.m.163.com/news/a/" + arr1[2]
                    + ".html?spss=newsapp&spsw=1";
        }
        return url;
    }

    public static String getUrlForDb(String str, Integer type, Integer urlFromType) {
        String url = null;
        try {
            switch (type) {
                case 1:
                    switch (urlFromType) {
                        case 2:
                            String[] arr1 = str.split("\n");
                            return getUrl(str, arr1);
                        case 3:
                            String[] arr = str.split("\n");
                            String url1;
                            if (arr.length == 1) {
                                url1 = arr[0];
                            } else {
                                url1 = arr[1];
                            }

                            return url1;
                        case 4:
                            return StringUtils.getRegExp(Const.URL_REGEXP, str);
                        case 5:
                            return StringUtils.getRegExp(Const.URL_REGEXP, str);
                        case 6:
                            return StringUtils.getRegExp(Const.URL_REGEXP, str);
                        case 7:
                            return StringUtils.getRegExp(Const.URL_REGEXP, str);
                        case 8:
                            return StringUtils.getRegExp(Const.URL_REGEXP, str);
                        case 9:
                            return StringUtils.getRegExp(Const.URL_REGEXP, str);
                        case 10:
                            return StringUtils.getRegExp(Const.URL_REGEXP, str);
                        case 11:
                            return StringUtils.getRegExp(Const.URL_REGEXP, str);
                        case 16:
                            return StringUtils.getRegExp(Const.URL_REGEXP, str);
                        case 17:
                            return StringUtils.getRegExp(Const.URL_REGEXP, str);
                        default:
                            break;
                    }
                    break;
                case 2:
                    return str;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceTypeStr() {
        return sourceTypeStr;
    }

    public void setSourceTypeStr(String sourceTypeStr) {
        this.sourceTypeStr = sourceTypeStr;
    }

}
