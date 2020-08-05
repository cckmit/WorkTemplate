package com.servlet.ad;

import com.alibaba.fastjson.JSONObject;
import com.servlet.CmServletMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 广告日志记录接口
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-01 19:02
 */
@WebServlet(urlPatterns = {"/logAdsThird", "/log/third-ad"})
public class AdLogServletThird extends CmServletMain implements Serializable {

    private static final Logger AdLog = LoggerFactory.getLogger("AdLog");

    @Override
    protected JSONObject handle(HttpServletRequest requestObject, JSONObject requestPackage) {
        // 记录广告提交日志
        AdLog.info("[THIRD]" + requestPackage.toJSONString());

        // 通知客户端数据记录成功
        JSONObject resultObject = new JSONObject();
        resultObject.put("result", "success");
        return resultObject;
    }

    /**
     * 是否保存日志
     *
     * @return 存储检测
     */
    @Override
    protected boolean isSaveLog() {
        return false;
    }
}
