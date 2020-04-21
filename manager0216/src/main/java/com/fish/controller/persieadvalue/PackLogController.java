package com.fish.controller.persieadvalue;

import com.fish.protocols.GetResult;
import com.fish.service.persieadvalue.PackLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
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
    public GetResult getPackLog(HttpServletRequest request, HttpServletResponse response) {

        String logPath = request.getParameter("logPath");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        return this.packLogService.packLog(logPath, startTime, endTime,startDate,endDate);
    }
}
