package com.servlet;

import com.alibaba.fastjson.JSONObject;
import com.tool.CmTool;
import com.tool.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;

/**
 * @author feng
 */
public class CmServletMain extends HttpServlet {
    protected static final Logger SERVLET_LOG = LoggerFactory.getLogger(CmServletMain.class);
    private static final long serialVersionUID = -712623596818241269L;

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
        out.println("finish Fc Log!");
        out.flush();
        out.close();
    }

    /**
     * 需要子类实现的处理逻辑方法
     *
     * @param requestObject  请求的对象
     * @param requestPackage 请求的包体
     * @return 响应的包体
     */
    protected JSONObject handle(HttpServletRequest requestObject, JSONObject requestPackage) {
        return null;
    }

    /**
     * 需要子类实现的处理逻辑方法
     *
     * @param requestObject  请求的对象
     * @param requestPackage 请求的包体
     * @return 响应的包体
     */
    protected String handle(HttpServletRequest requestObject, String requestPackage) {
        return null;
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
        //
        // 数据库资源句柄
        //
        long current = System.currentTimeMillis();
        try {
            //
            // 获取并解密相关数据
            //
            byte[] requestBytes = CmTool.parseServletInputStream(request);
            //
            // 根据字节流内容得到解析包
            //
            String requestContent = new String(requestBytes);
            String result = null;
            switch (getContextType()) {
                case "json": {
                    JSONObject requestObject = JSONObject.parseObject(requestContent);
                    if (null == requestObject) {
                        break;
                    }
                    // 进行具体的响应操作
                    //
                    JSONObject responsePackage = handle(request, requestObject);
                    if (null != responsePackage) {
                        result = responsePackage.toJSONString();
                        //进行转义buffer
                        byte[] bytes = encodeBuffer(request, responsePackage);
                        CmTool.encodeServletOutputStream(response, bytes);
                    }
                }
                break;
                case "xml":
                case "text": {
                    result = handle(request, requestContent);
                    ServletOutputStream out = response.getOutputStream();
                    out.write(result.getBytes(StandardCharsets.UTF_8));
                    out.flush();
                }
                break;
                default:
                    break;
            }
            if (isSaveLog()) {
                SERVLET_LOG.info(this.getClass().getName() + ",get:" + requestContent + ",post=" + result);
            }

        } catch (Exception e) {
            SERVLET_LOG.error(Log4j.getExceptionInfo(e));
        } finally {
            SERVLET_LOG.warn(this.getClass().getName() + ",耗时:" + (System.currentTimeMillis() - current) + "ms!");
        }
    }

    /**
     * 进行数据byte处理
     *
     * @param request         请求节点
     * @param responsePackage 下发内容
     * @return bytes
     */
    private byte[] encodeBuffer(HttpServletRequest request, JSONObject responsePackage) {

        byte[] bytes = responsePackage.toJSONString().getBytes(StandardCharsets.UTF_8);
        String deflate = request.getHeader("deflate");
        if (!Boolean.parseBoolean(deflate)) {
            return bytes;
        }
        Deflater deflater = new Deflater(9, true);
        deflater.setInput(bytes);
        deflater.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length);
        byte[] buf = new byte[1024];
        while (!deflater.finished()) {
            int i = deflater.deflate(buf);
            bos.write(buf, 0, i);
        }
        return bos.toByteArray();
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
