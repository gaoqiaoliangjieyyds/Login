package com.jia.demo.user.entity;/**
 * @author ChenJia
 * @create 2023-12-19 10:12
 */

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jia.demo.exception.BusinessException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 *@ClassName Response
 *@Description TODO
 *@Author jia
 *@Date 2023/12/19 10:12
 *@Version 1.0
 **/
public class Response {
    private HttpsURLConnection https;
    private HttpURLConnection http;
    private int status;
    private InputStream is;
    private String responseAsString = null;
    private boolean streamConsumed = false;

    public Response() {
    }

    public Response(HttpsURLConnection https) throws IOException {
        this.https = https;
        this.status = https.getResponseCode();
        if (null == (is = https.getErrorStream())) {
            is = https.getInputStream();
        }
    }

    public Response(HttpURLConnection http) throws IOException {
        this.http = http;
        this.status = http.getResponseCode();
        if (null == (is = http.getErrorStream())) {
            is = http.getInputStream();
        }
    }

    /**
     * 转换为输出流
     *
     * @return 输出流
     */
    public InputStream asStream() {
        if (streamConsumed) {
            throw new IllegalStateException("Stream has already been consumed.");
        }
        return is;
    }

    /**
     * 将输出流转换为String字符串
     *
     * @return 输出内容
     */
    public String asString() {
        if (null == responseAsString) {
            BufferedReader br;
            try {
                InputStream stream = asStream();
                if (null == stream) {
                    return null;
                }
                br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                StringBuilder buf = new StringBuilder();
                String line;
                while (null != (line = br.readLine())) {
                    buf.append(line).append("\n");
                }
                this.responseAsString = buf.toString();
                stream.close();
                //输出流读取完毕，关闭连接
                if (https != null) {
                    https.disconnect();
                }
                //输出流读取完毕，关闭连接
                if (http != null) {
                    http.disconnect();
                }
                streamConsumed = true;
            } catch (NullPointerException | IOException e) {
                throw new BusinessException(e.getMessage());
            }
        }
        return responseAsString;
    }

    /**
     * 将输出流转换为JSON对象
     */
    public JSONObject asJSONObject() {
        return JSONUtil.parseObj(asString());
    }

    /**
     * 将输出流转换为JSON对象
     */
    public JSONArray asJSONArray() {
        return JSONUtil.parseArray(asString());
    }

    /**
     * 获取响应状态
     *
     * @return 响应状态
     */
    public int getStatus() {
        return status;
    }
}
