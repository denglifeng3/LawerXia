package com.lawer.xia.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016/9/25.
 * CredentialsMatcher是shiro提供的用于加密密码和验证密码服务的接口，而HashedCredentialsMatcher正是CredentialsMatcher的一个实现类
 * 自定义RetryLimitHashedCredentialsMatcher继承HashedCredentialsMatcher
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher{
    private Cache<String,AtomicInteger> passWordRetryCache;

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager){
        passWordRetryCache=cacheManager.getCache("passWordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String userName= (String) token.getPrincipal();
        AtomicInteger retryCount=passWordRetryCache.get(userName);
        if(retryCount==null){
            retryCount=new AtomicInteger(0);
            passWordRetryCache.put(userName,retryCount);
        }
        if(retryCount.incrementAndGet()>5){
            throw  new ExcessiveAttemptsException();
        }
        boolean matchs=super.doCredentialsMatch(token,info);
        if(matchs){
            //清除输入次数
            passWordRetryCache.remove(userName);
        }
        return matchs;
    }
}
