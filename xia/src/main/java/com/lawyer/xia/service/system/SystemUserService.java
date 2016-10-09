package com.lawyer.xia.service.system;

import com.lawyer.xia.domain.system.SystemPermission;
import com.lawyer.xia.domain.system.SystemUser;

import java.util.Map;

/**
 * Created by lindeng on 9/29/2016.
 */

public interface SystemUserService {
    Map<SystemPermission, Object> getUserMenu(SystemUser systemUser);

}
