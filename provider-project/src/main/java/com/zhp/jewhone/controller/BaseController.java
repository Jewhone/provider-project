package com.zhp.jewhone.controller;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    /**
     * 把浏览器参数转化放到Map集合中
     *
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    protected Map<String, Object> getParam(HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String method = request.getMethod();
        Enumeration<?> keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            if (key != null) {
                if (key instanceof String) {
                    String value = request.getParameter(key.toString());
                    if ("GET".equals(method)) {//前台encodeURIComponent('我们');转码后到后台还是ISO-8859-1，所以还需要转码
                        try {
                            value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    paramMap.put(key.toString(), value);
                }
            }
        }
        return paramMap;
    }

    //将数据刷新写回web端
    protected void flushResponse(HttpServletResponse response, String responseContent) {
        PrintWriter writer = null;
        try {
            response.setCharacterEncoding("GBK");
            // 针对ajax中页面编码为GBK的情况，一定要加上以下两句
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/json;charset=UTF-8");
            writer = response.getWriter();
            if (responseContent == null || "".equals(responseContent) || "null".equals(responseContent)) {
                writer.write("");
            } else {
                writer.write(responseContent);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }


    /**
     * 获取session对象
     *
     * @return
     */
    protected HttpSession getSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session;
    }

    /**
     * 日期格式化
     *
     * @param binder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
