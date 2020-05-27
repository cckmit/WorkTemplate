package com.cc.manager.modules.jj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

/**
 * App配置
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-04 17:44
 */
@Data
@TableName(schema = "persie", value = "wx_config")
public class WxConfig implements BaseCrudEntity<WxConfig> {
    @TableId(value = "ddAppId", type = IdType.INPUT)
    private String id;
    @TableField(value = "ddAppSecret")
    private String ddAppSecret;
    @TableField(value = "product_name")
    private String productName;
    @TableField(value = "ddAppPlatform")
    private String ddAppPlatform;
    @TableField(value = "ddMchId")
    private String ddMchId;
    @TableField(value = "ddP12Password")
    private String ddP12Password;
    @TableField(value = "ddKey")
    private String ddKey;
    @TableField(value = "ddP12")
    private String ddP12;
    @TableField(value = "program_type")
    private Integer programType;
    @TableField(value = "origin_id")
    private String originId;
    @TableField(value = "ddAppSkipRes")
    private String ddAppSkipRes;
    @TableField(value = "origin_name")
    private String originName;
    @TableField(value = "add_id")
    private String addId;
    @TableField(value = "account")
    private String account;
    @TableField(value = "password")
    private String password;
    @TableField(value = "code_manager")
    private String codeManager;
    @TableField(value = "manager_wechat")
    private String managerWechat;
    @TableField(value = "clear_company")
    private String clearCompany;
    @TableField(value = "ddShareRes")
    private String ddShareRes;
    @TableField(value = "ddSubscribe")
    private String ddSubscribe;
    @TableField(value = "adminWechat")
    private String adminWechat;
    @TableField(value = "softWork")
    private String softWork;
    @TableField(value = "phoneNumber")
    private String phoneNumber;
    @TableField(value = "bindEmail")
    private String bindEmail;
    @TableField(value = "settleEmail")
    private String settleEmail;
    @TableField(value = "adSpaceId")
    private String adSpaceId;
    @TableField(value = "adName")
    private String adName;
    @TableField(value = "banner")
    private String banner;
    @TableField(value = "otherBanner")
    private String otherBanner;
    @TableField(value = "video")
    private String video;
    @TableField(value = "otherVideo")
    private String otherVideo;
    @TableField(value = "screen")
    private String screen;
    @TableField(value = "otherScreen")
    private String otherScreen;

    @TableField(exist = false)
    private String local;

    @TableField(exist = false)
    private String jumpDirect;

    /**
     * 展示名称
     */
    @TableField(exist = false)
    private String showName;

    @Override
    public String getCacheKey() {
        return this.id;
    }

    @Override
    public String getCacheValue() {
        return this.showName;
    }
}
