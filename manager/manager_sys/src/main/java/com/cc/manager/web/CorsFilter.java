//package com.cc.manager.web;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.apache.shiro.web.util.WebUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * 登录拦截器
// *
// * @author CC ccheng0725@outlook.com
// * @date 2020-05-18 23:44
// */
//@WebFilter(urlPatterns = "/*", filterName = "CorsFilter")
//public class CorsFilter implements Filter {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(CorsFilter.class);
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
//        try {
//            HttpServletRequest request = (HttpServletRequest) servletRequest;
//            HttpServletResponse response = WebUtils.toHttp(servletResponse);
//            String sessionId = request.getHeader("JSESSIONID");
//            String method = request.getMethod();
//            String uri = request.getRequestURI();
//            System.out.println("AA " + method + " -> " + uri + " | " + sessionId);
//            response.setHeader("Access-Control-Allow-Credentials", "true");
//            response.setHeader("Access-control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Methods", "HEAD, POST, GET, OPTIONS, DELETE, PUT");
//            response.setHeader("Access-Control-Max-Age", "3600");
//            response.setHeader("Access-Control-Allow-Headers", "*");
//            // OPTIONS 请求直接返回
//            if (StringUtils.equals(request.getMethod(), RequestMethod.OPTIONS.name())) {
//                response.setStatus(HttpStatus.OK.value());
//            } else {
//                filterChain.doFilter(servletRequest, servletResponse);
//            }
//        } catch (ServletException | IOException e) {
//            LOGGER.error(ExceptionUtils.getStackTrace(e));
//        }
//    }
//
//    @Override
//    public void destroy() {
//    }
//
//}
