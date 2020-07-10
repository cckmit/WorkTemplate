package com.cc.manager.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;

/**
 * 修改默认获取Sessionf方式
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-22 21:41
 */
@Configuration
public class AccountWebSessionManager extends DefaultWebSessionManager {

    private static final String REFERENCED_SESSION_ID_SOURCE = "JSESSIONID";

    private ShiroConfiguration shiroConfiguration;

    public AccountWebSessionManager() {
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        String sessionId = httpServletRequest.getHeader("JSESSIONID");
        String method = httpServletRequest.getMethod();
        String uri = httpServletRequest.getRequestURI();
        System.out.println("BB " + method + " -> " + uri + " | " + sessionId);
        //如果请求头中有 Authorization （前端请求头中设置的名字）则其值为sessionId
        if (!StringUtils.isEmpty(sessionId)) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sessionId);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            printSessionCache(sessionId);
            return sessionId;
        } else {
            //否则按默认规则从cookie取sessionId
            return super.getSessionId(request, response);
        }
    }

    private void printSessionCache(String id) {
        SimpleSession session = (SimpleSession) shiroConfiguration.sessionDAO().readSession(id);
        System.out.println(session);
        Map<Object, Object> attributes = session.getAttributes();
        if (attributes != null) {
            attributes.forEach((key, value) -> {
                System.out.println(key + " -> " + value);
            });
        } else {
            System.out.println("attributes -> null");
        }
    }

    @Autowired
    public void setShiroConfiguration(ShiroConfiguration shiroConfiguration) {
        this.shiroConfiguration = shiroConfiguration;
    }

}
