package com.lawer.xia.shiro;

import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Map;

/**
 * Created by lindeng on 9/21/2016.
 */
@Configuration
public class ShiroConfiguration {
    Map<String,String> filterChainDefinitionMap= Collections.synchronizedMap(new HashedMap());

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     *  所以我们需要修改下doGetAuthenticationInfo中的代码;
     * ）
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = //new HashedCredentialsMatcher();
                new RetryLimitHashedCredentialsMatcher(getEhCacheManager());

        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
        //hashedCredentialsMatcher.set
        return hashedCredentialsMatcher;
    }

    /**
     * 身份认证realm; (这个需要自己写，账号密码校验；权限等)
     *
     * @return
     */
    @Bean(name = "ShiroRealmImpl")
    public ShiroRealmImpl getShiroRealm() {
        ShiroRealmImpl shiroRealm= new ShiroRealmImpl();
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRealm;
    }

    @Bean(name = "shiroEhcacheManager")
    public EhCacheManager getEhCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return em;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean
    public SimpleCookie rememberMeCookie(){

        System.out.println("ShiroConfiguration.rememberMeCookie()");
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }
    /**
     * cookie管理对象;
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(){

        System.out.println("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(getShiroRealm());
        securityManager.setCacheManager(getEhCacheManager());
        //注入记住我管理器;
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    /**
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(getDefaultWebSecurityManager());
        return new AuthorizationAttributeSourceAdvisor();
    }



    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 必须设置SecuritManager
        shiroFilterFactoryBean.setSecurityManager(getDefaultWebSecurityManager());

        // 拦截器
        //Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        // 配置退出过滤器,其中的具体代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");

        //配置记住我或认证通过可以访问的地址

        filterChainDefinitionMap.put("/index", "user");
        filterChainDefinitionMap.put("/", "user");

        // <!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/servlet/safecode", "anon");
        filterChainDefinitionMap.put("/**", "authc");

        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        // 未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        shiroFilterFactoryBean
                .setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
}
