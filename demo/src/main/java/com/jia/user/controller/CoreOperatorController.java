package com.jia.user.controller;


import com.jia.demo.util.VerifyCodeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * <p>
 * 操作员 前端控制器
 * </p>
 *
 * @author jia
 * @since 2023-11-07
 */
@RestController
@RequestMapping("/user/coreOperator")
public class CoreOperatorController {
    public static final String VERIFY_CODE = "verifyCode";

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

