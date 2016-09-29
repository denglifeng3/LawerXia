package com.lawyer.xia.shiro;

import com.lawyer.xia.domain.system.SystemRole;
import com.lawyer.xia.domain.system.SystemUser;
import com.lawyer.xia.repositories.system.SystemUserRepository;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by lindeng on 9/21/2016.
 */
public class ShiroRealmImpl extends AuthorizingRealm {

    @Autowired
    private SystemUserRepository systemUserRepository;

    /**
     * 权限认证，为当前登录的Subject授予角色和权限
     * 经测试：本例中该方法的调用时机为需授权资源被访问时
     * 经测试：并且每次访问需授权资源时都会执行该方法中的逻辑，这表明本例中默认并未启用AuthorizationCache
     * 经测试：如果连续访问同一个URL（比如刷新），该方法不会被重复调用，Shiro有一个时间间隔（也就是cache时间，在ehcache-shiro.xml中配置），超过这个时间间隔再刷新页面，该方法会被执行
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("权限配置-->ShiroRealmImpl.doGetAuthorizationInfo()");
        //获取当前登陆输入的用户名，等价于(String) principalCollection.fromRealm(getName()).iterator().next();
        String loginName= (String) super.getAvailablePrincipal(principalCollection);
        //到数据库查是否有此对象
        // 实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        SystemUser user=systemUserRepository.findByUserName(loginName);
        if(user!=null){
            //权限信息对象INFO，用来存放查出的用户的所有的角色（ROLE）及权限（permission）
            SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
            //用户的角色集合
            info.setRoles(user.getRolesName());
            //用户的角色对应的所有权限，如果只使用角色定义访问权限，下面的四行可以不要
            List<SystemRole> rolesList=user.getRolesList();
            for (SystemRole role : rolesList) {
                info.addStringPermissions(role.getPermissionsName());
            }
            return info;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //UsernamePasswordToken对象用来存放提交的登录信息
        UsernamePasswordToken token= (UsernamePasswordToken) authenticationToken;
        //查出是否有此用户
        SystemUser user=systemUserRepository.findByUserName(token.getUsername());
        if(user!=null){
            //若存在，将此用户存放到登录认证info中，无需自己做密码对比，Shiro会为我们进行密码对比校验
            return new SimpleAuthenticationInfo(user.getUserName(),user.getPassWord(), ByteSource.Util.bytes(user.getSalt()),
                    getName());
        }
        return null;
    }
}
