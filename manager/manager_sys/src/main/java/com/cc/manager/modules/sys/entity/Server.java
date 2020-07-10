package com.cc.manager.modules.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-02 12:11
 */
@Data
@TableName(schema = "manager_system", value = "sys_server")
public class Server implements BaseCrudEntity<Server> {

    @TableId(type = IdType.AUTO)
    private int id;

    private String serverName;

    private String serverKey;

    private String serverDir;

    private String serverUrl;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    private String updateTime;

}
