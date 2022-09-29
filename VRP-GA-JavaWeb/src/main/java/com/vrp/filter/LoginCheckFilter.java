package com.vrp.filter;

import com.alibaba.fastjson.JSON;
import com.vrp.controller.utils.Res;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/13 12:08
 * @Description
 * @Since version-1.0
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    /**
     * 路径匹配工具
     */
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        /*获取本次请求url*/
        String requestURI = request.getRequestURI();
        /*定义不需要处理的请求url*/
        String[] urls = new String[]{
                "/user/**",
                "/page/**",
                "/js/**",
                "/css/**",
                "/imgs/**",
                "/element-ui/**"

        };

        /*判断本次请求是否需要处理*/
        boolean check = check(requestURI, urls);

        /*不需要处理直接放行*/
        if (check){
            filterChain.doFilter(request, response);
            return;
        }
        /*判断登录状态，去Session里找用户编号是否存在，如果已登录就直接放行*/
        if (request.getSession().getAttribute("user")!=null){
            filterChain.doFilter(request, response);
            return;
        }
        /*如果未登录就返回未登录结果*/
        Res res = new Res(false,"未登录");
        response.getWriter().write(JSON.toJSONString(res));
    }
    /**
     * 路径匹配，检查本次请求是否要放行
     * @param requestURL
     * @param urls
     * @return
     */
    public boolean check(String requestURL,String[] urls){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURL);
            if (match){
                return true;
            }
        }
        return false;
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }


    @Override
    public void destroy() {
        Filter.super.destroy();
    }


}
