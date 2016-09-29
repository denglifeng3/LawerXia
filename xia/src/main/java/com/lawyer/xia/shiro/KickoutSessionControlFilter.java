package com.lawyer.xia.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.*;

/**
 * Created by lindeng on 9/27/2016.
 * 同一个帐号 并发控制登录人数
 * Shiro对Servlet容器的FilterChain进行了代理，即ShiroFilter在继续Servlet容器的Filter链的执行之前，
 * 通过ProxiedFilterChain对Servlet容器的FilterChain进行了代理；即先走Shiro自己的Filter体系，
 * 然后才会委托给Servlet容器的FilterChain进行Servlet容器级别的Filter链执行
 * ；Shiro的ProxiedFilterChain执行流程：
 * 1、先执行Shiro自己的Filter链；2、再执行Servlet容器的Filter链（即原始的Filter）。
 * 而ProxiedFilterChain是通过FilterChainResolver根据配置文件中[urls]部分是否与请求的URL是否匹配解析得到的。
 */
public class KickoutSessionControlFilter extends AccessControlFilter{

    private String kickoutUrl="/login?again";//踢出后跳转到的地址
    private boolean kickoutAfter=false;//踢出之前登陆的/之后登陆的用户 默认踢出之前登录的用户
    private int maxSession=1;//统一账户最大会话数 默认1
    private SessionManager sessionManager;
    private Cache<String,Deque<Serializable>> cache;

    private Map<String,Subject> subjectMap= Collections.synchronizedMap(new LinkedHashMap<String, Subject>());

    public KickoutSessionControlFilter(SessionManager sessionManager,CacheManager cacheManager){
        this.sessionManager=sessionManager;
        this.cache=cacheManager.getCache("shiro-kickout-session");
        System.out.println("cache:"+this.cache);
    }

    /**
     * 表示是否允许访问；mappedValue就是[urls]配置中拦截器参数部分，如果允许访问返回true，否则false；
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    /**
     * 表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；如果返回false表示该拦截器实例已经处理了，将直接返回即可。
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if(!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，直接进行之后的流程
            return true;
        }

        Session session = subject.getSession();
        String username = (String) subject.getPrincipal();
        String sessionId = (String) session.getId();
        if(!subjectMap.containsKey(sessionId)){
            subjectMap.put(sessionId,subject);
            System.out.println("用户："+username+"被放入到缓存中："+subjectMap);
        }

        Subject outSubject=null;
        while (subjectMap.keySet().size()>maxSession){
            if(kickoutAfter) { //如果踢出后者
               outSubject=removeSubject(subjectMap, subjectMap.keySet().size()-1);
            } else { //否则踢出前者
                outSubject=removeSubject(subjectMap, 0);
            }
        }
        if(outSubject!=null){
            outSubject.logout();
            return false;
        }
        return true;
    }

    private Subject removeSubject(Map<String,Subject> subjectMap,int index){
        int i=0;
        Subject removeSubject=null;
        String  removeKey=null;
        for(String key:subjectMap.keySet()){
            if(i==index){
                removeSubject= (Subject) subjectMap.get(key);
                removeKey=key;
            }
            i++;
        }
        subjectMap.remove(removeKey);
        return removeSubject;
    }
}
