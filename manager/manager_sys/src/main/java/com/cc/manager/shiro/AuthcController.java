package com.cc.manager.shiro;

import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseController;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.sys.service.MenuService;
import com.cc.manager.modules.sys.service.ServerService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 系统登录登出接口
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-23 21:18
 */
@RestController
@RequestMapping("/authc")
public class AuthcController implements BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthcController.class);

    private MenuService menuService;
    private ServerService serverService;

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @PostMapping(value = "/login")
    public JSONObject login(@RequestParam("username") String username, @RequestParam("password") String password) {
        JSONObject loginObject = new JSONObject();
        // 默认code=2，表示失败，这里是为了同步layui的弹出样式
        loginObject.put("code", 2);
        Subject subject = SecurityUtils.getSubject();
        try {
            Session session = subject.getSession();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            token.setRememberMe(true);
            subject.login(token);
            if (subject.isAuthenticated()) {
                loginObject.put("code", 1);
                AccountUser user = (AccountUser) subject.getPrincipal();
                loginObject.put("nickName", user.getNickName());
                loginObject.put("JSESSIONID", session.getId());
                loginObject.put("msg", "登录成功！");
            } else {
                token.clear();
                loginObject.put("msg", "登录失败！");
            }
        } catch (UnknownAccountException e) {
            loginObject.put("msg", "未知账户！");
        } catch (IncorrectCredentialsException e) {
            loginObject.put("msg", "密码不正确！");
        } catch (LockedAccountException e) {
            loginObject.put("msg", "账户已锁定！");
        } catch (ExcessiveAttemptsException e) {
            loginObject.put("msg", "用户名或密码错误次数过多！");
        } catch (AuthenticationException e) {
            loginObject.put("msg", "用户名或密码不正确！");
        } catch (Exception e) {
            loginObject.put("msg", "登录服务器其它异常！");
        }
        return loginObject;
    }

    /**
     * 获取用户授权菜单，将这个方法防在这里，是为了让用户执行一次权限验证
     *
     * @return 授权菜单
     */
    @GetMapping(value = "/getMenuJson")
    @RequiresPermissions("authc:getMenuJson")
    public JSONObject getMenuJson() {
        Subject subject = SecurityUtils.getSubject();
        AccountUser accountUser = (AccountUser) subject.getPrincipal();
        return this.menuService.getMenuJson(accountUser.getId());
    }

    @GetMapping(value = "/getServerJson")
    public JSONObject getServerJson(){
        return this.serverService.getServerJson();
    }

    /**
     * 是否登录检测，由于分布式redis问题，其它模块用当前接口验证用户是否登录
     * @return 登录结果
     */
    @GetMapping(value = "/loginCheck")
    public PostResult loginCheck(){
        return new PostResult();
    }

    /**
     * 刷新缓存接口
     *
     * @return 刷新结果
     */
    @GetMapping(value = "/clearCache")
    public PostResult clearCache() {
        return new PostResult();
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
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    @Autowired
    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }

}
