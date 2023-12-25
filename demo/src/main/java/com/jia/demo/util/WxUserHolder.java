package com.jia.demo.util;/**
 * @author ChenJia
 * @create 2023-12-19 10:53
 */

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jia.demo.context.HttpContext;
import com.jia.demo.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 *@ClassName WxUserHolder
 *@Description TODO
 *@Author jia
 *@Date 2023/12/19 10:53
 *@Version 1.0
 **/
@Slf4j
public class WxUserHolder {
    public static String tokenHeader;

    public static String debugToken;

    private static final String CUST = "cust";

    public static final String WECHAT_USER = "WECHAT_USER";

    public static final String YSF_MINIAPP_USER = "YSF_MINIAPP_USER";

    public static final String SESSION_REPORT = "session_report";




    @Autowired
    private WechatConst wechatConst;


    public static void setOpenId(String openId) {
        HttpSession session = HttpContext.getSession();
        log.info("设置sessionid：" + (session !=null?session.getId():"session 为空"));
        if (session == null) {

            return;
        }
        session.setAttribute(WECHAT_USER, openId);
    }

    public static void setMiniAppOpenId(String openId) {
        HttpSession session = HttpContext.getSession();
        log.info("设置sessionid：" + (session !=null?session.getId():"session 为空"));
        if (session == null) {
            return;
        }
        session.setAttribute(YSF_MINIAPP_USER, openId);
    }


    public static void generateToken(String sub){
        HttpServletResponse response = HttpContext.getResponse();
        String token = JwtUtil.generateToken(sub, CUST);
        response.setHeader(tokenHeader, token);
    }

    public static String currentopenId(){
        HttpServletRequest request = HttpContext.getRequest();
        String header = request.getHeader(tokenHeader);
        if (StrUtil.equals(debugToken, header)){
            return header;
        }
        String openId = null;
        try {
            openId = JwtUtil.getUsernameFromToken(header);
        }catch (ExpiredJwtException e){
            openId = e.getClaims().getSubject();
        }
        return openId;
    }


}
