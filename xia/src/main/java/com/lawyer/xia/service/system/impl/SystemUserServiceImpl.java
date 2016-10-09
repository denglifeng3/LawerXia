package com.lawyer.xia.service.system.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lawyer.xia.domain.system.SystemPermission;
import com.lawyer.xia.domain.system.SystemRole;
import com.lawyer.xia.domain.system.SystemUser;
import com.lawyer.xia.repositories.system.SystemUserRepository;
import com.lawyer.xia.service.system.SystemUserService;
import com.lawyer.xia.utils.PermissionType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by lindeng on 9/29/2016.
 */
@Service
public class SystemUserServiceImpl implements SystemUserService{
    @Autowired
    private SystemUserRepository systemUserRepository;

    @Override
    public Map<SystemPermission, Object> getUserMenu(SystemUser systemUser) {

        Set<SystemRole> roles=systemUser.getSysDepartment().getRoles();
        roles.addAll(systemUser.getRoles());
        Set<SystemPermission> systemPermissions=new HashSet<SystemPermission>();
        for(SystemRole systemRole:roles){
            systemPermissions.addAll(systemRole.getMenus());
        }
        return  returnParentAndChildMenu(systemPermissions);
    }

    private Map<SystemPermission,Object> returnParentAndChildMenu(Set<SystemPermission> systemPermissions) {
        Map<SystemPermission,Object> menusMap=new LinkedHashMap<SystemPermission,Object>();
        for(SystemPermission systemPermission:systemPermissions){
           if(systemPermission.getpId()==0L&&systemPermission.getResourceType().equals("menu")){
               menusMap.put(systemPermission,this.returnMenuByPid(systemPermissions,systemPermission.getId()));
           }
       }
       return menusMap;
    }

    private List<SystemPermission> returnMenuByPid(Set<SystemPermission> systemPermissions,Long pId){
        List<SystemPermission> systemPermissionList=new LinkedList<SystemPermission>();
        for(SystemPermission systemPermission:systemPermissions){
            if(systemPermission.getpId()==pId&&systemPermission.getResourceType().equals("menu")){
                systemPermissionList.add(systemPermission);
            }
        }
        return systemPermissionList;
    }

}
