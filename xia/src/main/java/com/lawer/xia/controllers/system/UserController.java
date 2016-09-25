package com.lawer.xia.controllers.system;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Administrator on 2016/9/24.
 */
@Controller
@RequestMapping(value = "lawer/system/user")
public class UserController {

    private final String userPath="/lawer/system/user/";

    @RequestMapping(value="/list")
    @RequiresPermissions(value = "lawer/system/user:view")
    public String userList(){
        return userPath+"userInfo";
    }

    @RequestMapping(value="/add")
    @RequiresPermissions(value = "lawer/system/user:add")
    public String userAdd(){
        return userPath+"userAdd";
    }

    @RequestMapping(value="/delete")
    @RequiresPermissions(value = "lawer/system/user:delete")
    public String userDelete(){
        return userPath+"userDelete";
    }
}
