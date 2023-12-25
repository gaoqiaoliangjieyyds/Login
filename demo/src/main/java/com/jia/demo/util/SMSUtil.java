package com.jia.demo.util;/**
 * @author ChenJia
 * @create 2023-12-25 15:43
 */

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static cn.hutool.http.HttpUtil.post;

/**
 *@ClassName SMSUtil
 *@Description TODO
 *@Author jia
 *@Date 2023/12/25 15:43
 *@Version 1.0
 **/
@Slf4j
public class SMSUtil {
    /**指定模版单发
     *  apikey 成功注册后登录云片官网,进入后台可查看
     * @param mobile 接收的手机号,仅支持单号码发送
     *  tpl_id 发送的模版id
     *  tpl_value  模版内容，变量名和变量值对
     * @return json格式字符串
     */

    public static String tplSingleSend(String mobile,String apikey,long tpl_id,String url) throws UnsupportedEncodingException {
        Map<String, Object> params = new HashMap<String, Object>();
        // 生成验证码
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int random = (int) (Math.random() * 10);
            builder.append(random);
        }
        String code = builder.toString();
        params.put("apikey", apikey);
        params.put("mobile", mobile);
        params.put("tpl_id", String.valueOf(tpl_id));
        params.put("tpl_value", URLEncoder.encode("#code#") + "=" + URLEncoder.encode(code) );
        log.info("手机验证码_{}",params);
        return post(url, params)+code;
    }
}
