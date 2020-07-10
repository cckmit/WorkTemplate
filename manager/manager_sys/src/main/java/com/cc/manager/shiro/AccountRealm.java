package com.cc.manager.shiro;

import com.alibaba.fastjson.JSONObject;
import com.cc.manager.modules.sys.entity.Menu;
import com.cc.manager.modules.sys.entity.Role;
import com.cc.manager.modules.sys.service.MenuService;
import com.cc.manager.modules.sys.service.RoleService;
import com.cc.manager.modules.sys.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义shiro 权限验证对象
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-19 12:01
 */
@Component
public class AccountRealm extends AuthorizingRealm {

    private final Logger LOGGER = LoggerFactory.getLogger(AccountRealm.class);

    private UserService userService;
    private RoleService roleService;
    private MenuService menuService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        LOGGER.info("----------------> 开始执行Shiro凭证认证 ~");
        AccountUser user = (AccountUser) principalCollection.getPrimaryPrincipal();
        if (user != null) {
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            // 获取用户角色集
            if (StringUtils.isNotBlank(user.getRoleIds())) {
                List<Role> roleList = this.roleService.listByIds(
                        JSONObject.parseArray(user.getRoleIds(), String.class));
                Set<Integer> roleIdSet = new HashSet<>();
                Set<String> roleNameSet = new HashSet<>();
                roleList.forEach(role -> {
                    roleIdSet.add(role.getId());
                    roleNameSet.add(role.getRoleName());
                });
                // 当前项目权限都统一按照菜单注解配置，所以这里并不生效
                simpleAuthorizationInfo.setRoles(roleNameSet);

                // 获取用户权限集
                List<Menu> menuList;
                // 1表示管理员
                if (roleIdSet.contains(1)) {
                    menuList = this.menuService.list();
                } else {
                    menuList = this.menuService.listByIds(roleIdSet);
                }
                Set<String> permissionSet = new HashSet<>();
                menuList.forEach(menu -> {
                    if (StringUtils.isNotBlank(menu.getPerms())) {
                        permissionSet.add(menu.getPerms());
                    }
                });
                // 每个登录成功的用户都给一个拉取菜单的权限，加上这个注解是为了让用户执行一次当前方法
                permissionSet.add("authc:getMenuJson");

                simpleAuthorizationInfo.setStringPermissions(permissionSet);
                return simpleAuthorizationInfo;
            }
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        AccountUser accountUser = this.userService.login(token.getUsername());
        if (accountUser != null) {
            if (!accountUser.isStatus()) {
                throw new LockedAccountException("账号已被锁定,请联系管理员！");
            }
        } else {
            throw new AccountException("账号不存在!");
        }
        // 登录成功将当前用户放入redis
        return new SimpleAuthenticationInfo(accountUser, accountUser.getPassword(), getName());
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

}