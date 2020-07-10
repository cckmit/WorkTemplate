package com.cc.manager.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录用户，当前类的属性来源于User，使用当前类的目的是不必使用sys模块，保证代码完整性
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-01 17:40
 */
@Data
public class AccountUser implements Serializable {

    private int id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 用户角色列表ID字符串
     */
    private String roleIds;
    /**
     * 是否允许登录
     */
    private boolean status = false;

}
