package com.jia.demo.user.controller;/**
 * @author ChenJia
 * @create 2023-12-18 14:45
 */

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.EscapeUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jia.demo.exception.BusinessException;
import com.jia.demo.util.HttpCommUtils;
import com.jia.demo.util.RSAUtil;
import com.jia.demo.util.RSAUtils;
import com.jia.demo.util.WechatConst;
import com.jia.demo.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.prefs.BackingStoreException;


/**
 *@ClassName WxController
 *@Description TODO
 *@Author jia
 *@Date 2023/12/18 14:45
 *@Version 1.0
 **/

@RestController
@RequestMapping("/wx")
@Slf4j
public class WxController {

    @Autowired
    private WechatConst wechatConst;


    //微信静默登录，获取授权码
    @GetMapping("/home")
    public void home(@RequestParam Map<String,Object> param, HttpServletResponse response){
        try {
            String baseUrl = wechatConst.getBaseUrlKey();
            String oauth2AuthorizeUrl = wechatConst.getOauth2AuthorizeUrl()
                    + "?appid="+wechatConst.getAppId()
                    + "&redirect_uri=" + URLEncoder.encode(baseUrl,"GBK")
                    + "&response_type=code"
                    + "&scope=snsapi_base"
                    + "$state=" + URLEncoder.encode(JSONUtil.toJsonStr(param),"GBK")
                    + "&#wechat_redirect";
            log.info("进入home: " +oauth2AuthorizeUrl);
            response.sendRedirect(oauth2AuthorizeUrl);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }

    //利用code获取openid
    @GetMapping("/user/key")
    public void getUserKey(String code, String state, HttpServletResponse response, HttpServletRequest request){
        JSONObject params = JSONUtil.parseObj(EscapeUtil.unescapeHtml4(state));
        String key = (String)params.getOrDefault("key", "");
        String openId = Convert.toStr(params.get("openId"), "");
        //要是前端没传openId，则根据用户的code获取openid
        if(null == openId || StringUtils.isBlank(openId)){
            String oauth2AccessTokenUrl = wechatConst.getOauth2AccessTokenUrl()
                    + "?appId="+wechatConst.getAppId()
                    + "&secret="+ wechatConst.getSecret()
                    + "&code="+code
                    + "&grant_type = authorization_code";
            log.info("获取code"+code);
            String resp = HttpCommUtils.get(oauth2AccessTokenUrl, null);
            JSONObject jsonObject = JSONUtil.parseObj(resp);
            openId = jsonObject.getStr("openid");

            //将拿到的openid存入redis

        }else {
            openId = openId.replaceAll(" ","+");
            log.info("跳过验证"+openId);
            //调试使用openId传参，RSA解密后获取openId
            try {
                openId = RSAUtils.decrypt(openId);
            }catch (Exception e){
                log.error("传入的openid解密失败");
                throw new BusinessException("传入的openId解密失败");
            }
        }
    }

    //微信自动登录，生成token并且返回前端
    @GetMapping("/autoLogin")
    @ResponseBody
    public Result autoLogin(){
        //从redis中获取openid
        return null;
    }

}
