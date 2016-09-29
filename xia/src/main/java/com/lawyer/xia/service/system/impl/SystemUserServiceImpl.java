package com.lawyer.xia.service.system.impl;

import com.lawyer.xia.domain.system.SystemPermission;
import com.lawyer.xia.domain.system.SystemRole;
import com.lawyer.xia.domain.system.SystemUser;
import com.lawyer.xia.repositories.system.SystemUserRepository;
import com.lawyer.xia.service.system.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lindeng on 9/29/2016.
 */
@Service
public class SystemUserServiceImpl implements SystemUserService{
    @Autowired
    private SystemUserRepository systemUserRepository;

    @Override
    public Set<SystemPermission> getUserMenu(String userName) {
        SystemUser systemUser=systemUserRepository.findByUserName(userName);
        Set<SystemRole> roles=systemUser.getSysDepartment().getRoles();
        roles.addAll(systemUser.getRoles());
        Set<SystemPermission> systemPermissions=new HashSet<SystemPermission>();
        for(SystemRole systemRole:roles){
            systemPermissions.addAll(systemRole.getMenus());
        }
        for(SystemPermission systemPermission:systemPermissions){
            if(systemPermission.getResourceType().equals("button")){
                systemPermissions.remove(systemPermission);
            }
        }
        return  systemPermissions;
    }

    private Map<String,Object> returnParentAndChildMenu(Set<SystemPermission> menus,Map<String,Object> menusMap){

    }
}
