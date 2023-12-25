package com.jia.demo.util;/**
 * @author ChenJia
 * @create 2023-12-19 10:06
 */

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jia.demo.user.controller.in.AgentEntity;
import com.jia.demo.user.entity.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 *@ClassName HttpProxyUtils
 *@Description TODO
 *@Author jia
 *@Date 2023/12/19 10:06
 *@Version 1.0
 **/
@Slf4j
@Configuration
public class HttpProxyUtils {
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String GET = "GET";
    private static final String POST = "POST";


    private static final String proxyUrl = "http://10.20.112.162:8091/yun/app/proxy/pull";


    /**
     * @Description:代理get请求
     * @Author: chf
     * @Date: 2021/10/26
     */
    public static String get(String url, Map<String, String> headers) {
        Response response = proxyHttpsRequest(url, GET, null, headers);
        if (null != response) {
            return response.asString();
        } else {
            log.error(url + "请求失败！");
            return "";
        }

    }

    /**
     * @Description:代理post请求
     * @Author: chf
     * @Date: 2021/10/26
     */
    public static String post(String url, JSONObject postData) {
        return post(url, postData, null);
    }

    public static String post(String url, JSONObject postData, Map<String, String> headers) {
        Response response = proxyHttpsRequest(url, POST, null == postData ? null : postData.toString(), headers);
        if (null == response) {
            log.error("发送请求失败");
            return "";
        }
        log.info("proxy result:{}", response.asString());
        return response.asString();
    }

    public static String postBody(String url, String body, Map<String, String> headers) {
        Response response = proxyHttpsRequest(url, POST, body, headers);
        if (null == response) {
            log.error("发送请求失败");
            return "";
        }
        log.info("proxy result:{}", response.asString());
        return response.asString();
    }


    /**
     * @Description:生产内网服务器调用外网请求转发代理
     * @Author: chf
     * @Date: 2021/8/5
     */
    private static Response proxyHttpsRequest(String url, String method, String postData, Map<String, String> headers) {
        log.info("proxy url={}, method={}, data={}, headers={}", url, method, postData, headers);
        HttpURLConnection conn = null;
        BufferedWriter wr = null;
        try {
            AgentEntity metaData = new AgentEntity();
            metaData.setTargetUrl(url);
            if (method.equals(POST)) {
                metaData.setHeadParams(headers);
                metaData.setHttpMethod("httpPost");
                metaData.setObjectString(postData);
            } else {
                metaData.setHttpMethod("httpGet");
            }

            URL urlObj = new URL(proxyUrl);
            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "utf-8");

            wr = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), DEFAULT_CHARSET));
            ObjectMapper mapper = new ObjectMapper();
            wr.write(mapper.writeValueAsString(metaData));
            wr.flush();

            return new Response(conn);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (wr != null) {
                try {
                    wr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
