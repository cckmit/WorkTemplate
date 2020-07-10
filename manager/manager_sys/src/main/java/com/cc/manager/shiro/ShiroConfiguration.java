package com.cc.manager.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Shiro权限验证
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-20 17:08
 */
@Configuration
public class ShiroConfiguration implements EnvironmentAware {

    private String host;
    private int port;
    private String password;
    private int timeout;
    private int database;

    @Override
    public void setEnvironment(Environment environment) {
        this.host = Objects.requireNonNull(environment.getProperty("spring.redis.host"));
        this.port = Integer.parseInt(Objects.requireNonNull(environment.getProperty("spring.redis.port")));
        this.password = environment.getProperty("spring.redis.password");
        this.timeout = Integer.parseInt(Objects.requireNonNull(environment.getProperty("spring.redis.timeout")));
        this.database = Integer.parseInt(Objects.requireNonNull(environment.getProperty("spring.redis.database")));
    }

    /**
     * shiro 中配置 redis 缓存，crazycake实现
     *
     * @return RedisManager
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host + ":" + port);
        if (StringUtils.isNotBlank(password)) {
            redisManager.setPassword(password);
        }
        redisManager.setTimeout(timeout);
        redisManager.setDatabase(database);
        return redisManager;
    }

    @Bean
    public JavaUuidSessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    @Bean
    public RedisSessionDAO sessionDAO() {
        // crazycake 实现
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        sessionDAO.setRedisManager(redisManager());
        // Session ID 生成器
        sessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return sessionDAO;
    }

    @Bean
    public SimpleCookie cookie() {
        // cookie的name,对应的默认是 JSESSIONID
        SimpleCookie cookie = new SimpleCookie("JSESSIONID");
        cookie.setHttpOnly(false);
        // path为 / 用于多个系统共享JSESSIONID
        cookie.setPath("/");
        return cookie;
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        AccountWebSessionManager sessionManager = new AccountWebSessionManager();
        // 设置session超时
        sessionManager.setGlobalSessionTimeout(timeout * 1000L);
        // 删除无效session
        sessionManager.setDeleteInvalidSessions(true);
        // 设置JSESSIONID
        sessionManager.setSessionIdCookie(cookie());
        // 设置sessionDAO
        sessionManager.setSessionDAO(sessionDAO());

        Collection<SessionListener> listeners = new ArrayList<>();
        listeners.add(new ShiroSessionListener());
        sessionManager.setSessionListeners(listeners);

        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    /**
     * 1. 配置SecurityManager
     *
     * @return
     */
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 配置 SecurityManager，并注入 shiroRealm
        securityManager.setRealm(realm());

        // 配置 shiro session管理器
        securityManager.setSessionManager(sessionManager());
        // 配置 缓存管理类 cacheManager
        securityManager.setCacheManager(redisCacheManager());
        return securityManager;
    }

    /**
     * 2. 配置缓存
     *
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager() {
        // crazycake 实现
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setRedisManager(redisManager());
        return cacheManager;
    }

    /**
     * 3. 配置Realm
     *
     * @return
     */
    @Bean
    public AuthorizingRealm realm() {
        AccountRealm realm = new AccountRealm();
        realm.setCredentialsMatcher(new AccountMatcher());
        return realm;
    }

    /**
     * 4. 配置LifecycleBeanPostProcessor，可以来自动的调用配置在Spring IOC容器中 Shiro Bean 的生命周期方法
     *
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 5. 启用IOC容器中使用Shiro的注解，但是必须配置第四步才可以使用
     *
     * @return
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 6. 配置ShiroFilter
     *
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        // 配置SecurityManager
        factoryBean.setSecurityManager(securityManager());

        // 配置权限路径
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("/open/**", "anon");
        map.put("/sys/**", "authc");
        map.put("/authc/loginCheck", "authc");
        map.put("/authc/getMenuTree", "authc");
        map.put("/**", "anon");

        // 配置无权限路径
        factoryBean.setFilterChainDefinitionMap(map);
        Map<String, Filter> filters = factoryBean.getFilters();
        filters.put("authc", new AccountUserFilter());
        factoryBean.setFilters(filters);
        return factoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
