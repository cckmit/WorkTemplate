package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.model.PublicCentre;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.PublicCentreService;
import com.fish.utils.BaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 公众号配置中心
 * PublicCentreController
 *
 * @author
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class PublicCentreController {

    @Autowired
    PublicCentreService publicCentreService;
    @Autowired
    BaseConfig baseConfig;

    /**
     * 查询公众号配置中心信息
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/public")
    public GetResult getPublicCentre(GetParameter parameter) {
        return publicCentreService.findAll(parameter);
    }

    /**
     * 新增公众号配置
     *
     * @param productInfo
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/public/new")
    public PostResult insertPublicCentre(@RequestBody PublicCentre productInfo) {
        PostResult result = new PostResult();
        int count = publicCentreService.insert(productInfo);
        if (count == 1) {
            result.setMsg("操作成功");
            return result;
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
    }

    /**
     * 修改公众号配置
     *
     * @param productInfo
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/public/edit")
    public PostResult modifyPublicCentre(@RequestBody PublicCentre productInfo) {
        PostResult result = new PostResult();
        int count = publicCentreService.updateByPrimaryKeySelective(productInfo);
        if (count != 0) {
            result.setMsg("操作成功");
            return result;
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
    }

    /**
     * 修改完毕公众号中心内容提交JSON文件到客户端
     *
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/public/submitJson")
    public PostResult submitPublicCentre() {
        PostResult result = new PostResult();
        try {
            publicCentreService.selectAllForJson();
            result.setMsg("更新JSON成功");
        } catch (Exception e) {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;

    }

    /**
     * 删除选中的公众号配置内容
     *
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/public/delete")
    public PostResult deleteBuyPay(@RequestBody JSONObject jsonObject) {
        return this.publicCentreService.deleteSelective(jsonObject);
    }

    /**
     * 更新展示顺序
     *
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/public/updateShowId")
    public PostResult updateShowId(@RequestBody JSONObject jsonObject) {
        return this.publicCentreService.updateShowId(jsonObject);
    }

}
