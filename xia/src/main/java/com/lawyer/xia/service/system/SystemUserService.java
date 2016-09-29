package com.lawyer.xia.service.system;

import com.lawyer.xia.domain.system.SystemPermission;

import java.util.List;
import java.util.Set;

/**
 * Created by lindeng on 9/29/2016.
 */

public interface SystemUserService {
    Set<SystemPermission> getUserMenu(String userName);

}
