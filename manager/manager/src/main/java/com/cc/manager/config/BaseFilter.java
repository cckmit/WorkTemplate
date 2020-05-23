package com.cc.manager.config;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HttpServlet 跨域过滤器
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-19 15:42
 */
@WebFilter(urlPatterns = "/*", filterName = "cors")
public class BaseFilter implements Filter {

    /**
     * 初始化的方法
     */
    @Override
    public void init(FilterConfig filterConfig) {
    }

    /**
     * 过滤的方法
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String origin = httpRequest.getHeader("Origin");
        httpResponse.setHeader("Access-Control-Allow-Origin", origin);
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
        httpResponse.setHeader("Access-Control-Max-Age", "7200");
        httpResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
    }

    /**
     * 销毁的方法
     */
    @Override
    public void destroy() {
    }

}
