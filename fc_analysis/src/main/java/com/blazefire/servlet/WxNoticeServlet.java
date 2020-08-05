package com.blazefire.servlet;

import com.blazefire.service.wx.WxNoticeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

/**
 * 接收微信公共
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-30 20:16
 */
@WebServlet(urlPatterns = "/wxNoticeServlet", name = "wxNoticeServlet")
public class WxNoticeServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxNoticeServlet.class);

    private WxNoticeService wxNoticeService;

    public static byte[] unzipBytes(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }

        byte[] b = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            GZIPInputStream gzip = new GZIPInputStream(bis);
            byte[] buf = new byte[1024];
            int num = -1;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((num = gzip.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, num);
            }
            b = baos.toByteArray();
            baos.flush();
            baos.close();
            gzip.close();
            bis.close();
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return b;
    }

    @Override
    public void init() {
        Objects.requireNonNull(WebApplicationContextUtils.getWebApplicationContext(
                getServletContext())).getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.println("finish!");
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = getPostZipParam(request);
        if (StringUtils.isNotBlank(param)) {
            try {
                LOGGER.debug(param);
                this.wxNoticeService.insert(param);
            } catch (Exception e) {
                LOGGER.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }

    private String getPostZipParam(HttpServletRequest request) {
        String postParam = null;
        try {
            request.setCharacterEncoding("UTF-8");
            ServletInputStream inputStream = request.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            do {
                byte[] bytes = new byte[1024];
                int bufferLength = inputStream.read(bytes);
                if (bufferLength < 0) {
                    break;
                }
                outputStream.write(bytes, 0, bufferLength);
            } while (true);
            byte[] byteArray = outputStream.toByteArray();
            postParam = new String(unzipBytes(byteArray), StandardCharsets.UTF_8);
            inputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.getMessage(e));
        }
        return postParam;
    }

    @Autowired
    public void setWxNoticeService(WxNoticeService wxNoticeService) {
        this.wxNoticeService = wxNoticeService;
    }
}
