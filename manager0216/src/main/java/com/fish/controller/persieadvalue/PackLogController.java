package com.fish.controller.persieadvalue;

import com.fish.service.persieadvalue.PackLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * WxAdPosController
 *
 * @author
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class PackLogController {

    @Autowired
    PackLogService packLogService;

    /**
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/packLog")
    public void getPackLog(HttpServletRequest request, HttpServletResponse response) {

        String logPath = request.getParameter("logPath");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        System.out.println(logPath + "-" + startTime + "==" + endTime);

      //  packLogService.packLog(logPath,startTime,endTime);
    }
}
