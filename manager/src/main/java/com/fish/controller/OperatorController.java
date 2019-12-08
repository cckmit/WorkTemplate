package com.fish.controller;

import com.fish.dao.second.model.NoticeWithBLOBs;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.OperatorNoticeService;
import com.fish.service.OperatorSystemEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/operator")
public class OperatorController
{
    @Autowired
    OperatorSystemEmailService systemEmailService;
    @Autowired
    OperatorNoticeService noticeService;

    @ResponseBody
    @GetMapping("/email")
    public GetResult selectEmail(GetParameter parameter)
    {
        switch (parameter.getDatagrid())
        {
            case "system":
                return systemEmailService.findAll(parameter);
            case "notice":
                return noticeService.findAll(parameter);
            default:
                break;

        }
        GetResult result = new GetResult();
        result.setMsg("未知查询");
        result.setCode(200);
        return result;
    }

    @ResponseBody
    @PostMapping(value = {"/email/new", "/email/edit"})
    public PostResult wxInput(@RequestBody NoticeWithBLOBs notice)
    {
        PostResult result = new PostResult();
        noticeService.saveNotice(notice);
        result.setCode(200);
        result.setMsg("操作成功");
        return result;
    }
}
