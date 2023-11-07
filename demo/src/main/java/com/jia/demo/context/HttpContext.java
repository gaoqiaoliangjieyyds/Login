package com.jia.demo.context;/**
 * @author ChenJia
 * @create 2023-11-07 14:34
 */

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
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getRequest();
    }

    public static HttpSession getSession() {
        return getRequest() == null ? null : getRequest().getSession();
    }
}
