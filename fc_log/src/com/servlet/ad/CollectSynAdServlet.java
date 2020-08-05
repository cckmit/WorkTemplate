package com.servlet.ad;

import com.alibaba.fastjson.JSONObject;
import com.service.ThirdSendService;
import com.servlet.CmServletMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @author xuwei
 */
@WebServlet(urlPatterns = {"/collect/ad", "/log/third"})
public class CollectSynAdServlet extends CmServletMain {

    private static final Logger AdLog = LoggerFactory.getLogger("third");

    @Override
    protected JSONObject handle(HttpServletRequest requestObject, JSONObject data) {
        // 记录广告提交日志
        data.put("serverTime", System.currentTimeMillis());
        AdLog.info(data.toJSONString());
        ThirdSendService.addQueue(data);
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
