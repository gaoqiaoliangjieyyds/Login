package com.jia.demo.user.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.jia.demo.context.HttpContext;
import com.jia.demo.param.LoginParam;
import com.jia.demo.param.LoginParamMobile;
import com.jia.demo.user.entity.CoreOperator;
import com.jia.demo.util.SMSUtil;
import com.jia.demo.util.VerifyCodeUtil;
import com.jia.demo.vo.LoginResult;
import com.jia.demo.vo.Result;
import com.jia.demo.user.service.CoreOperatorService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 操作员 前端控制器
 * </p>
 *
 * @author jia
 * @since 2023-11-07
 */
@RestController
@Slf4j
@RequestMapping("/user/coreOperator")
public class CoreOperatorController {
    public static final String VERIFY_CODE = "verifyCode";

    private static final String SMS_OPER_NUM = "sms_oper_";

    private CoreOperatorService coreOperatorService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${sms.apikey}")
    public String apikey;
    @Value("${sms.tpl_id}")
    public long tpl_id;
    @Value("${sms.url}")
    public String url;

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


    @PostMapping("/login")
    @ResponseBody
    @ApiOperation(value = "登录-账号登录")
    public Result login(@RequestBody LoginParam param) {
        HttpSession session = HttpContext.getSession();
        try {
            String currCode = (String) session.getAttribute(VERIFY_CODE);
            LoginResult result = coreOperatorService.login(param, currCode);
            HttpContext.setOperator(result);
            //设置其他的用户信息到HttpContext
//            InfoPhysician info = infoPhysicianService.createLambdaQuery()
//                    .andEq(InfoPhysician::getOperId, result.getOperId())
//                    .single();
//            HttpContext.setOperatorPhysicianInfo(info);
//            if (ObjectUtil.isNotEmpty(info)) {
//                result.setPhysicianId(info.getId());
//            }
            return Result.ok(result);
        } finally {
            session.removeAttribute(VERIFY_CODE);
        }
    }

    @PostMapping("/loginMobile")
    @ResponseBody
    @ApiOperation(value = "登录-手机号登录")
    public Result loginMobile(@RequestBody LoginParamMobile param) {
        HttpSession session = HttpContext.getSession();
        try {
            String currCode = (String) session.getAttribute(VERIFY_CODE);

            LoginResult result = coreOperatorService.login(param, currCode);
            HttpContext.setOperator(result);
//            InfoPhysician info = infoPhysicianService.createLambdaQuery()
//                    .andEq(InfoPhysician::getOperId, result.getOperId())
//                    .single();
//            HttpContext.setOperatorPhysicianInfo(info);
//            if (ObjectUtil.isNotEmpty(info)) {
//                result.setPhysicianId(info.getId());
//            }
            return Result.ok(result);
        } finally {
            session.removeAttribute(VERIFY_CODE);
        }
    }



    @GetMapping("/sms")
    @ResponseBody
    @ApiOperation("发送手机验证码")
    public Result SmsVerifyCode() throws UnsupportedEncodingException {
        Object o = HttpContext.getSession().getAttribute("smsphone");
        Assert.isTrue(ObjectUtil.isNotNull(o),"未检测到手机号！");
        String smsmobile = o.toString();
        CoreOperator operator = coreOperatorService.getOperatorByPhone(smsmobile);
        Assert.isTrue(operator != null, "未找到用户");
        String smsStr = SMS_OPER_NUM + smsmobile;

        Integer sendNum = 0;

        if (redisTemplate.hasKey(smsStr)) {
            sendNum = Integer.valueOf(redisTemplate.opsForValue().get(smsStr).toString());
            Assert.isTrue(sendNum <15, "今日该号码发送短信验证码次数已达15次！");
        }
        String result = SMSUtil.tplSingleSend(smsmobile, apikey, tpl_id, url);
        int index = result.indexOf("}");
        String resultstr = result.substring(0, index + 1);
        JSONObject jsonstr = new JSONObject(resultstr);
        Integer code = jsonstr.getInt("code");
        if (code == 0) {
            try {
                if (redisTemplate.hasKey(smsStr)) {
                    sendNum = sendNum+1;
                    redisTemplate.opsForValue().set(smsStr,sendNum,1, TimeUnit.DAYS);
                }else{
                    sendNum = 1;
                    redisTemplate.opsForValue().set(smsStr,sendNum,1,TimeUnit.DAYS);

                }
                // 短信有效时长5分钟
                redisTemplate.opsForValue().set("sms" + smsmobile, result.substring(index + 1), 300, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.info("发送手机验证码_json={}", jsonstr);
                e.printStackTrace();
                Assert.isTrue(false, "发送验证码失败_报错！");
            }
            return Result.ok("发送手机验证码成功！");
        } else if (code == 53) {
            log.info("" + jsonstr);
            Assert.isTrue(false, "发送手机验证码失败_{}", jsonstr.get("msg"));

        }else if(code == -3){
            log.info(""+jsonstr);
            Assert.isTrue(false, "发送手机验证码失败_{}_{}",jsonstr.get("msg"),jsonstr.get("detail"));

        } else {
            log.info("" + jsonstr);
            Assert.isTrue(false, "发送手机验证码失败_{}", jsonstr.get("msg"));
        }
        return Result.fail("发送手机验证码失败!");

    }


}

