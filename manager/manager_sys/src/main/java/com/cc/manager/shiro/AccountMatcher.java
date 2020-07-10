package com.cc.manager.shiro;

import cn.hutool.crypto.digest.DigestUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * 用户名密码校验
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-23 23:02
 */
public class AccountMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        Object tokenCredentials = DigestUtil.md5Hex(
                usernamePasswordToken.getUsername() + String.valueOf(usernamePasswordToken.getPassword()));
        Object accountCredentials = getCredentials(info);
        return accountCredentials.equals(tokenCredentials);
    }

}
