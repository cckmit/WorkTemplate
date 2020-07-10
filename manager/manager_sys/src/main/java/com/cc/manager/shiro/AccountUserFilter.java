package com.cc.manager.shiro;

import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.result.PostResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户请求拦截器，未登录用户返回自定义信息
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-25 21:41
 */
public class AccountUserFilter extends UserFilter {

    /**
     * 集合shiro过滤器和跨域过滤器
     *
     * @param request  ServletRequest
     * @param response ServletResponse
     * @return 过滤结果
     * @throws Exception Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        String sessionId = httpServletRequest.getHeader("JSESSIONID");
        String method = httpServletRequest.getMethod();
        String uri = httpServletRequest.getRequestURI();
        System.out.println("CC " + method + " -> " + uri + " | " + sessionId);
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "HEAD, POST, GET, OPTIONS, DELETE, PUT");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
        // OPTIONS 请求直接返回
        if (StringUtils.equals(httpServletRequest.getMethod(), RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return true;
        } else {
            return super.onAccessDenied(request, response);
        }
    }

    /**
     * 未登录时返回自定义信息而不是跳转登录页面
     *
     * @param request  ServletRequest
     * @param response ServletResponse
     * @throws IOException IOException
     */
    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        PostResult postResult = new PostResult();
        postResult.setCode(-1);
        postResult.setMsg("您还未登录或登录已超时，请重新登录！");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(JSONObject.toJSONString(postResult));
    }

}
