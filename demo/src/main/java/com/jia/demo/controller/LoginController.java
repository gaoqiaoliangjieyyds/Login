package com.jia.demo.controller;/**
 * @author ChenJia
 * @create 2023-11-07 10:59
 */

import com.jia.demo.util.VerifyCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.RequestWrapper;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *@ClassName LoginController
 *@Description 手机短信登录，手机账号登录，手机验证码登录
 *@Author jia
 *@Date 2023/11/7 10:59
 *@Version 1.0
 **/
@RequestMapping("/jia")
@RestController
@Api(tags = "登录管理")
public class LoginController {
    public static final String VERIFY_CODE = "verifyCode";

    //发送验证码
    @ApiOperation(value = "生成验证码",notes = "生成验证码")
    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        int width = 200;
        int height = 100;
        BufferedImage verifyImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        String randomText = VerifyCodeUtil.drawRandomText(width, height, verifyImg);
        HttpSession session = request.getSession();
        session.setAttribute(VERIFY_CODE, randomText);
        try (ServletOutputStream outputStream = resp.getOutputStream()) {
            ImageIO.write(verifyImg, "png", outputStream);
        }
    }
}
