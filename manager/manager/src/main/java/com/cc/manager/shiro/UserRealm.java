package com.cc.manager.shiro;

import com.cc.manager.modules.sys.entity.User;
import com.cc.manager.modules.sys.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户权限校验
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-23 23:07
 */
public class UserRealm extends AuthorizingRealm {

    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        if (user != null) {
            Set<String> roles = new HashSet<>();
            Set<String> permissions = new HashSet<>();
            roles.add(user.getRoleIds());
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
            info.setStringPermissions(permissions);
            return info;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = this.userService.login(token.getUsername());
        if (user == null) {
            throw new AccountException("账户不存在!");
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }

    /**
     * 重写shiro的密码验证，让shiro用我自己的验证
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        setCredentialsMatcher(new UsernamePasswordMatcher());
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
