package com.blaze.common.config;

import com.alibaba.fastjson.JSON;
import com.blaze.common.PostResult;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Controller统一日志接口
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-17 21:14
 */
@Aspect
@Component
public class WebLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger("Web");

    /**
     * 两个..代表所有子目录，最后括号里的两个..代表所有参数
     */
    @Pointcut("execution(* com.blaze.logic.controller..*.*(..))")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public PostResult doAround(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        PostResult postResult = new PostResult();
        try {
            postResult = (PostResult) pjp.proceed(pjp.getArgs());
            LOGGER.info(request.getMethod() + " -> " + request.getRequestURI() + " | " + (System.currentTimeMillis() - startTime) + "\n\t [Q] -> " + Arrays.toString(pjp.getArgs()) + "\n\t [S] -> " + JSON.toJSONString(postResult));
        } catch (RuntimeException e) {
            LOGGER.error(request.getMethod() + " -> " + request.getRequestURI() + " | " + (System.currentTimeMillis() - startTime) + "\n\t [Q] -> " + Arrays.toString(pjp.getArgs()) + "\n\t [E] -> " + ExceptionUtils.getStackTrace(e));
            postResult.updateMsg(0, "error");
        }
        return postResult;
    }

}
