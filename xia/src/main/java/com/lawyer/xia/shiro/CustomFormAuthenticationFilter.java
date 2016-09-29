package com.lawyer.xia.shiro;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by lindeng on 9/26/2016.
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter{
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //在这里进行验证码校验
        HttpServletRequest httpServletRequest= (HttpServletRequest) request;
        HttpSession session=httpServletRequest.getSession();
        //取出验证码
        String validateCode= (String) session.getAttribute("validateCode");
        //取出页面的验证和session中的验证码进行比对
        String randomCode=httpServletRequest.getParameter("randomCode");
        if(randomCode!=null&&validateCode!=null&&
                !randomCode.toLowerCase().equals(validateCode.toLowerCase())){
            // 如果校验失败，将验证码错误失败信息，通过shiroLoginFailure设置到request中
            //throw new KeyAlreadyExistsException("验证码错误");
            httpServletRequest.setAttribute("shiroLoginFailure","kaptchaValidateFailed");
            // 拒绝访问，不再校验账号和密码
            return true;
        }
        return super.onAccessDenied(request, response);
    }
}
