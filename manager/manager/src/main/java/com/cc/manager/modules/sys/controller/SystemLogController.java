package com.cc.manager.modules.sys.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cc.manager.config.BaseConfig;
import com.cc.manager.modules.sys.entity.User;
import com.cc.manager.modules.sys.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 系统登录登出接口
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-23 21:18
 */
@CrossOrigin
@RestController
@RequestMapping
public class SystemLogController {

    private BaseConfig baseConfig;

    private RoleService roleService;

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public JSONObject login(@RequestParam("username") String username, @RequestParam("password") String password) {
        JSONObject loginObject = new JSONObject();
        // 默认code=2，表示失败，这里是为了同步layui的弹出样式
        loginObject.put("code", 2);
        Subject subject = SecurityUtils.getSubject();
        subject.getSession().setTimeout(this.baseConfig.getTimeout());
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            if (subject.isAuthenticated()) {
                loginObject.put("code", 1);
                loginObject.put("msg", "登录成功");
            } else {
                token.clear();
                loginObject.put("msg", "登录失败");
            }
        } catch (UnknownAccountException uae) {
            loginObject.put("msg", "未知账户");
        } catch (IncorrectCredentialsException ice) {
            loginObject.put("msg", "密码不正确");
        } catch (LockedAccountException lae) {
            loginObject.put("msg", "账户已锁定");
        } catch (ExcessiveAttemptsException eae) {
            loginObject.put("msg", "用户名或密码错误次数过多");
        } catch (AuthenticationException ae) {
            loginObject.put("msg", "用户名或密码不正确");
        }
        return loginObject;
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    @GetMapping(value = "/userInfo")
    public JSONObject getUserInfo() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        JSONObject userObject = new JSONObject();
        userObject.put("userName", user.getUsername());
        userObject.put("nickName", user.getNickName());
        return userObject;
    }

    /**
     * 获取已授权菜单
     *
     * @return 授权菜单列表
     */
    @GetMapping(value = "/getAuthorizedMenu")
    public JSONArray getAuthorizedMenu() {
        // 获取用户及用户权限信息
        Set<String> authorizedPages = null;
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null || StringUtils.isBlank(user.getRoleIds())) {
            return new JSONArray();
        }
        return new JSONArray();
    }

    /**
     * 系统登出
     *
     * @return 登出提示
     */
    @GetMapping(value = "/logout")
    public JSONObject logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        JSONObject logoutObject = new JSONObject();
        logoutObject.put("code", 1);
        logoutObject.put("msg", "退出成功！");
        return logoutObject;
    }

    @Autowired
    public void setBaseConfig(BaseConfig baseConfig) {
        this.baseConfig = baseConfig;
    }

}
