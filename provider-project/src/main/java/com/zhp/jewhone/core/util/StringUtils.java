package com.zhp.jewhone.core.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @title 字符串操作
 */
public class StringUtils {

    /**
     * 将以逗号分隔的字符串转换成字符串数组
     *
     * @param valStr
     * @return String[]
     */
    public static String[] StrList(String valStr) {
        int i = 0;
        String TempStr = valStr;
        String[] returnStr = new String[valStr.length() + 1 - TempStr.replace(",", "").length()];
        valStr = valStr + ",";
        while (valStr.indexOf(',') > 0) {
            returnStr[i] = valStr.substring(0, valStr.indexOf(','));
            valStr = valStr.substring(valStr.indexOf(',') + 1, valStr.length());

            i++;
        }
        return returnStr;
    }

    /**
     * 获取字符串编码
     *
     * @param str
     * @return String
     */
    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (UnsupportedEncodingException ignored) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (UnsupportedEncodingException ignored) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (UnsupportedEncodingException ignored) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (UnsupportedEncodingException ignored) {
        }
        return "";
    }

    /**
     * 字符串编码转换函数，用于将指定编码的字符串转换为标准（Unicode）字符串
     */
    public static String getStr(Object _strSrc) {
        if (isEmpty(_strSrc)) {
            return "";
        }
        return _strSrc.toString().trim();
    }

    /**
     * 获取字符串
     *
     * @param _strSrc    当前字符串
     * @param defaultStr 默认字符串
     */
    public static String getStrByDefault(Object _strSrc, String defaultStr) {
        if (isEmpty(_strSrc)) {
            return defaultStr;
        }
        return getStr(_strSrc.toString());
    }

    /**
     * 获取int
     *
     * @param _strSrc
     */
    public static int getInt(Object _strSrc) {
        if (isEmpty(_strSrc)) {
            return 0;
        }
        try {
            return Integer.parseInt(_strSrc.toString().trim());
        } catch (Exception ignored) {
        }
        return 0;
    }

    public static long getLong(Object _strSrc) {
        if (isEmpty(_strSrc)) {
            return 0;
        }
        try {
            return Long.parseLong(_strSrc.toString());
        } catch (Exception e) {
        }
        return 0;
    }

    public static int getIntByDefault(Object _strSrc, int defaultInt) {
        if (isEmpty(_strSrc)) {
            return defaultInt;
        }
        try {
            return Integer.parseInt(_strSrc.toString());
        } catch (NumberFormatException ignored) {
        }
        return 0;
    }

    /**
     * 获取double
     *
     * @param _strSrc
     * @return double
     */
    public static double getDouble(Object _strSrc) {
        if (isEmpty(_strSrc)) {
            return 0;
        }
        try {
            return Double.parseDouble(_strSrc.toString());
        } catch (NumberFormatException ignored) {
        }
        return 0;
    }

    /**
     * 判断指定字符串是否为空
     *
     * @param _string 指定的字符串
     * @return 若字符串为空对象（_string==null）或空串（长度为0），则返回true；否则，返回false.
     */
    public static boolean isEmpty(Object _string) {
        return (("null".equals(_string)) || (_string == null) || (_string.toString().trim().length() == 0));
    }

    /**
     * 判定非空
     *
     * @param _string
     * @return String
     */
    public static boolean isNotEmpty(Object _string) {
        return (!isEmpty(_string));
    }

    /**
     * list转换为逗号分隔的字符串
     *
     * @param list
     * @return
     */
    public static String listToStr(List<?> list) {
        StringBuilder str = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (Object ob : list) {
                str.append(",").append(ob);
            }
            str = new StringBuilder(str.substring(1));
        }
        return str.toString();
    }

    /**
     * 获取注册ip
     *
     * @param request
     * @return String
     */
    public static String getIpAddr(HttpServletRequest request) {
//		String ip = request.getHeader("x-forwarded-for");
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (!isEmpty(ip)) {
            String[] ips = ip.split(",");
            if (ips.length > 1)
                ip = ips[0];
        }
        return ip;
    }

    /**
     * 格式化double
     *
     * @param dividend     被除数
     * @param total        除数
     * @param decimalCount 保留小数位数
     * @return String
     */
    public static String formatDouble(double dividend, double total, int decimalCount) {
        double doubleNum = (dividend * 1.0) / total;
        return formatDouble(doubleNum,decimalCount)+"";
    }

    /**
     * 格式化数字
     *
     * @param numericalOb
     * @param decimalCount
     * @return String
     */
    public static String formatNumerical(Object numericalOb, int decimalCount) {
        double numerical = 0;
        if (!isEmpty(numericalOb)) {
            numerical = Double.parseDouble(numericalOb.toString()) * 100;
        }
        return formatDouble(numerical,decimalCount)+"";
    }

    /**
     * 格式化double
     *
     * @param doubleNum    被格式化的数
     * @param decimalCount 保留位数
     * @return
     */
    public static String formatDouble(double doubleNum, int decimalCount) {
        String doubleNumStr = "";

        switch (decimalCount) {
            case 0:
                DecimalFormat df0 = new DecimalFormat("######0");
                return df0.format(doubleNum);
            case 1:
                DecimalFormat df1 = new DecimalFormat("######0.0");
                return df1.format(doubleNum);
            case 2:
                DecimalFormat df2 = new DecimalFormat("######0.00");
                return df2.format(doubleNum);
            case 3:
                DecimalFormat df3 = new DecimalFormat("######0.000");
                return df3.format(doubleNum);
            default:
                break;
        }
        return doubleNumStr;
    }

    /**
     * 时间戳转换字符串
     *
     * @param mill
     * @return
     */
    public static String dateMillconvertStr(long mill) {
        Date date = new Date(mill);
        String strs = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            strs = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }

    public static String getOrderCode() {
        return new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date()) + RandomStringUtil.random(5);
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 获取运行类包名 方法名 行数
     *
     * @return String
     */
    public static String getTraceInfo() {
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stacks = new Throwable().getStackTrace();
//        int stacksLen = stacks.length;    
        sb.append("class:").append(stacks[1].getClassName()).append(";method:").append(stacks[1].getMethodName()).append(";number:").append(stacks[1].getLineNumber());
        return sb.toString();
    }

    /**
     * 获取项目路径
     *
     * @return String
     */
    public static String getPorjectPath() {
        String nowpath = "";
        nowpath = System.getProperty("user.dir") + "/";

        return nowpath;
    }

    /**
     * 字符串显示处理函数：若为空对象，则返回指定的字符串
     *
     * @param _sValue         指定的字符串
     * @param _sReplaceIfNull 当_sValue==null时的替换显示字符串；可选参数，缺省值为空字符串（""）
     * @return 处理后的字符串
     */
    public static String showNull(String _sValue, String _sReplaceIfNull) {
        return (_sValue == null ? _sReplaceIfNull : _sValue);
    }

    /**
     * 字符串替换函数：用于将指定字符串中指定的字符串替换为新的字符串。
     *
     * @param _strSrc 源字符串。
     * @param _strOld 被替换的旧字符串
     * @param _strNew 用来替换旧字符串的新字符串
     * @return 替换处理后的字符串
     */
    public static String replaceStr(String _strSrc, String _strOld,
                                    String _strNew) {
        if (_strSrc == null || _strNew == null || _strOld == null)
            return "";

        // 提取源字符串对应的字符数组
        char[] srcBuff = _strSrc.toCharArray();
        int nSrcLen = srcBuff.length;
        if (nSrcLen == 0)
            return "";

        // 提取旧字符串对应的字符数组
        char[] oldStrBuff = _strOld.toCharArray();
        int nOldStrLen = oldStrBuff.length;
        if (nOldStrLen == 0 || nOldStrLen > nSrcLen)
            return _strSrc;

        StringBuilder retBuff = new StringBuilder(
                (nSrcLen * (1 + _strNew.length() / nOldStrLen)));

        int i, j, nSkipTo;
        boolean bIsFound;

        i = 0;
        while (i < nSrcLen) {
            bIsFound = false;

            // 判断是否遇到要找的字符串
            if (srcBuff[i] == oldStrBuff[0]) {
                for (j = 1; j < nOldStrLen; j++) {
                    if (i + j >= nSrcLen)
                        break;
                    if (srcBuff[i + j] != oldStrBuff[j])
                        break;
                }
                bIsFound = (j == nOldStrLen);
            }

            // 若找到则替换，否则跳过
            if (bIsFound) { // 找到
                retBuff.append(_strNew);
                i += nOldStrLen;
            } else { // 没有找到
                if (i + nOldStrLen >= nSrcLen) {
                    nSkipTo = nSrcLen - 1;
                } else {
                    nSkipTo = i;
                }
                for (; i <= nSkipTo; i++) {
                    retBuff.append(srcBuff[i]);
                }
            }
        }// end while
        return retBuff.toString();
    }

    /**
     * 正则匹配参数 获取参数
     *
     * @param exp     正则表达式
     * @param content 参数
     * @return String
     */
    public static String getRegExp(String exp, String content) {
        Pattern pattern = Pattern.compile(exp); // 正则匹配
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    /**
     * 生成8位随机码（数字0~9+大写字母A~Z）
     */
    public static String getRandom() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int a = Math.abs((new Random()).nextInt(32));// 0~9, A~Z
            if (a <= 9) {// 将0~9转为char的0~9
                sb.append((char) (a + 48));
            } else if (a < 33) {// 将10~33转为char的A~Z
                if ((a + 55) == 79 || (a + 55) == 73) {
                    sb.append((char) (a + 63));
                } else {
                    sb.append((char) (a + 55));
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
	/*	DecimalFormat df2 = new DecimalFormat("######0.00");
		System.out.println(df2.format(1.168));
		System.out.println(formatNumber(0));*/
        BigDecimal one = new BigDecimal("1");
        System.out.println(one.add(new BigDecimal(-1)));
    }

    /**
     * 格式化html文本
     *
     * @param content
     * @return String
     */
    public static String htmlDocument(String content) {
        content = content + "";
        Document doc = Jsoup.parse(content);
        Element body = doc.body();
        content = body.text();
        return content;
    }

}
