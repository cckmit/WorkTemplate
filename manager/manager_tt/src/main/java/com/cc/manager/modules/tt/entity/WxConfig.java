package com.cc.manager.modules.tt.entity;

import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

/**
 * WxConfig配置
 *
 * @author CF
 * @date 2020-07-014
 */
@Data
public class WxConfig implements BaseCrudEntity<WxConfig> {

    private String id;

    private String productName;

    private String ddAppPlatform;

    private Integer programType;

}
