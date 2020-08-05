package com.blazefire.servlet;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * HttpServlet 统一接口入口
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-19 15:42
 */
public abstract class BaseServlet extends HttpServlet {

    protected static final Logger SERVLET_LOG = LoggerFactory.getLogger(BaseServlet.class);

    @Override
    public void init() {
        Objects.requireNonNull(WebApplicationContextUtils.getWebApplicationContext(
                getServletContext())).getAutowireCapableBeanFactory().autowireBean(this);
    }

    /**
     * The doGet method of the servlet. <br>
     * <p>
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws IOException if an error occurred
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.println("finish!");
        out.flush();
        out.close();
    }

    /**
     * 获取格式内容
     */
    protected String getContextType() {
        return "json";
    }

    /**
     * The doPost method of the servlet. <br>
     * <p>
     * This method is called when a form has its tag value method equals to
     * post.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        long current = System.currentTimeMillis();
        try {
            String requestContent = ServletUtil.getBody(request);
            JSONObject responseObject = null;
            if ("json".equals(this.getContextType())) {
                JSONObject requestObject = JSONObject.parseObject(requestContent);
                if (Objects.nonNull(requestObject)) {
                    // 进行具体的响应操作
                    responseObject = handle(request, requestObject);

                }
            } else {
                responseObject = handle(request, requestContent);
            }

            String responseContent = "null";
            if (Objects.nonNull(responseObject)) {
                responseContent = responseObject.toJSONString();
                response.setCharacterEncoding(CharsetUtil.UTF_8);
                ServletUtil.write(response, responseContent, CharsetUtil.UTF_8);
            }
            if (isSaveLog()) {
                SERVLET_LOG.info(this.getClass().getName() + ",get:" + requestContent + ",post=" + responseContent);
            }
        } catch (Exception e) {
            SERVLET_LOG.error(ExceptionUtils.getStackTrace(e));
        } finally {
            SERVLET_LOG.warn(this.getClass().getName() + ",耗时:" + (System.currentTimeMillis() - current) + "ms!");
        }
    }

    /**
     * 需要子类实现的处理逻辑方法
     *
     * @param request       请求的对象
     * @param requestObject 请求的包体
     * @return 响应的包体
     */
    protected JSONObject handle(HttpServletRequest request, JSONObject requestObject) {
        return null;
    }

    /**
     * 需要子类实现的处理逻辑方法
     *
     * @param request    请求的对象
     * @param requestStr 请求的字符串
     * @return 响应的包体
     */
    protected JSONObject handle(HttpServletRequest request, String requestStr) {
        return null;
    }

    /**
     * 是否保存日志
     *
     * @return 存储检测
     */
    protected boolean isSaveLog() {
        return true;
    }

}
