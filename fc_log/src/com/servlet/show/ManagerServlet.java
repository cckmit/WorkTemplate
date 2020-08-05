package com.servlet.show;

import com.config.ReadConfig;
import com.service.ThirdSendService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 服务基础配置
 *
 * @author xuwei
 */
@WebServlet(urlPatterns = "/manager")
public class ManagerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String type = req.getParameter("type");
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");
        switch (type) {
            case "thread": {
                out.println(ThirdSendService.threadStatus());
                out.flush();
            }
            break;
            case "config": {
                out.println("暂不支持！");
                out.flush();
            }
            break;
            case "flush": {
                ReadConfig.init();
                out.println("刷新成功！");
                out.flush();
            }
            break;
            default:
                break;
        }
        out.close();
    }
}
