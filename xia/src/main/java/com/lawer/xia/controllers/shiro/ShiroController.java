package com.lawer.xia.controllers.shiro;

import com.lawer.xia.domain.system.SystemUser;
import com.lawer.xia.repositories.system.SystemUserRepository;
import com.lawer.xia.shiro.KaptchaValidateFailedException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * Created by lindeng on 9/21/2016.
 */
@Controller
public class ShiroController {

    private static final Logger logger = LoggerFactory.getLogger(ShiroController.class);

    @Autowired
    private SystemUserRepository systemUserRepository;

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String loginGet(){
        return "login";
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@Valid SystemUser systemUser, HttpServletRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        String urlTogo="";
        if(bindingResult.hasErrors()){
            urlTogo= "login";
        }
        String username=systemUser.getUserName();
        UsernamePasswordToken token=new UsernamePasswordToken(systemUser.getUserName(),systemUser.getPassWord());
        //获取当前Subject
        Subject currentUser= SecurityUtils.getSubject();
        //System.out.println("密码是："+new Md5Hash("123456", "testuser", 2).toString());

        try {
            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            logger.info("对用户[" + username + "]进行登录验证..验证开始");
            currentUser.login(token);
            logger.info("对用户[" + username + "]进行登录验证..验证通过");
        }catch(UnknownAccountException uae){
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,未知账户");
            redirectAttributes.addFlashAttribute("message", "未知账户");
        }catch(IncorrectCredentialsException ice){
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,错误的凭证");
            redirectAttributes.addFlashAttribute("message", "密码不正确");
        }catch(LockedAccountException lae){
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,账户已锁定");
            redirectAttributes.addFlashAttribute("message", "账户已锁定");
        }catch(ExcessiveAttemptsException eae){
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,错误次数过多");
            redirectAttributes.addFlashAttribute("message", "用户名或密码错误次数过多");
        }
        catch(AuthenticationException ae){
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,堆栈轨迹如下");
            ae.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "用户名或密码不正确");
        }
        String exception = (String) request.getAttribute("shiroLoginFailure");
        if("kaptchaValidateFailed".equals(exception)){
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,验证码错误");
            urlTogo= "redirect:/login";
            redirectAttributes.addFlashAttribute("message", "验证码错误");
        }else{
            //验证是否登录成功
            if(currentUser.isAuthenticated()){
                logger.info("用户[" + username + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
                urlTogo= "redirect:/index";

            }else{
                token.clear();
                urlTogo= "redirect:/login";
            }
        }

        return urlTogo;
    }

    @RequestMapping(value = "/common/index",method = RequestMethod.GET)
    public String commonIndex(){
        return "commonIndex";
    }

    @RequestMapping(value="/logout",method=RequestMethod.GET)
    public String logout(RedirectAttributes redirectAttributes ){
        //使用权限管理工具进行用户的退出，跳出登录，给出提示信息
        SecurityUtils.getSubject().logout();
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        return "redirect:/login";
    }

    @RequestMapping("/403")
    public String unauthorizedRole(){
        logger.info("------没有权限-------");
        return "403";
    }

    @RequestMapping("/index")
    public String getUserList(Map<String, Object> model){
        model.put("userList", systemUserRepository.findAll());
        return "index";
    }

}
