package com.jia.demo.context;/**
 * @author ChenJia
 * @create 2023-11-07 14:34
 */

import com.jia.demo.vo.LoginResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *@ClassName HttpContext
 *@Description TODO
 *@Author jia
 *@Date 2023/11/7 14:34
 *@Version 1.0
 **/
public class HttpContext {
    public static final String CURR_OPER = "CURR_OPER";
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getRequest();
    }

    public static void setOperator(LoginResult operator){
        HttpSession session = getSession();
        if(session == null){
            return;
        }
        session.setAttribute(CURR_OPER,operator);
        session.setMaxInactiveInterval(4*60*60);
    }

    public static HttpSession getSession() {
        return getRequest() == null ? null : getRequest().getSession();
    }
}
