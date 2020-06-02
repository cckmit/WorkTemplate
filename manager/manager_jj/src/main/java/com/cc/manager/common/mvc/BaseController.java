package com.cc.manager.common.mvc;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.result.PostResult;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 建议所有的Controller都实现此接口，实现了权限验证封装，当然你也可以自己写
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-26 12:13
 */
public interface BaseController {

    Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    /**
     * 权限异常统一处理接口
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    default void authorizationException(HttpServletRequest request, HttpServletResponse response) {
        PostResult postResult = new PostResult();
        postResult.setCode(-2);
        postResult.setMsg("操作失败：您无权执行此操作，请联系管理员！");

        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setContentType("application/json; charset=UTF-8");
        ServletUtil.write(response, JSONObject.toJSONString(postResult), CharsetUtil.UTF_8);
    }

}
