package com.blazefire.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
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
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.addHeader("Access-Control-Allow-Origin", "*");
        filterChain.doFilter(servletRequest, httpResponse);
    }

    /**
     * 销毁的方法
     */
    @Override
    public void destroy() {
    }

}
