package com.zhp.jewhone.intercepter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class WebIntercepter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
       /* String uri = request.getRequestURI();
        if (uri.contains("login")) {
            return true;
        }
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("USER_SESSION");
        if (user != null) {
            return true;
        }
        request.setAttribute("msg", "您还没有登录，请先登录！");
        request.getRequestDispatcher("/static/login.html").forward(request, response);
        return false;*/
        /*String uri = request.getRequestURI(); //请求路径
        String ctx = request.getContextPath();
        String path = uri.replace(ctx,"");

        if(path.startsWith("/person")){
            SysUser member=SysUserUtils.getSessionLoginUser();
            if (member == null || member == null) { // 转到登陆页面
                response.sendRedirect(ctx + "/front/login");
                return false;
            } else{
                return true;
            }
        }
        if(path.startsWith("/wap1/person")){
            SysUser member=SysUserUtils.getSessionLoginUser();
            if (member == null || member == null) { // 转到登陆页面
                response.sendRedirect(ctx + "/wap1/login");
                return false;
            } else{
                return true;
            }
        }
        if(path.startsWith("/web1/person")){
            SysUser member=SysUserUtils.getSessionLoginUser();
            if (member == null || member == null) { // 转到登陆页面
                response.sendRedirect(ctx + "/web1/login");
                return false;
            } else{
                return true;
            }
        }
        if(path.startsWith("/front")||path.startsWith("/web1")||path.startsWith("/wap1") ){
            return true;
        }
        //if(!ignorePath.contains(path)){
        //获得session中的登陆用户
        SysUser sessionUser = SysUserUtils.getSessionLoginUser();

        //获得缓存中的登陆用户
        SysUser cacheUser = SysUserUtils.getCacheLoginUser();

        if (sessionUser == null || cacheUser == null) { // 转到登陆页面
            //   response.sendRedirect(ctx + "/notlogin");
            return true;
        } else {

            Map<String, SysResource> allRes = BeetlUtils
                    .getBeetlSharedVars(Constant.CACHE_ALL_RESOURCE);
            String perPath = path.substring(1);
            SysResource sysResource = allRes.get(perPath);
            //判断如果url不在数据库中，则默认都有权限访问
            if (sysResource == null
                    || Constant.RESOURCE_COMMON.equals(sysResource.getCommon())) {
                return true;
            }

            //实时的权限验证,检测用户认证是否改变，如果认证改变则重置，否则不进行任何操作
            SysUserUtils.setUserAuth();

            //从缓存中的用户权限 map<url:resource>
            Map<String, SysResource> userRes = SysUserUtils.getUserResources();
            if (userRes.containsKey(perPath)) { //有权限则过
                return true;
            } else { //没有权限跳到未有权限
                response.sendRedirect(ctx + "/notauth");
                return false;
            }
        }*/

        System.out.println("拦截器启动");
       return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
