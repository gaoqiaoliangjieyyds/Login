package com.jia.demo.util;/**
 * @author ChenJia
 * @create 2023-12-19 9:43
 */

import cn.hutool.http.HttpUtil;
import com.jia.demo.config.SpringContextHolder;

import java.util.Map;

/**
 *@ClassName HttpCommUtils
 *@Description TODO
 *@Author jia
 *@Date 2023/12/19 9:43
 *@Version 1.0
 **/
public class HttpCommUtils {

    public static String get(String urlString, Map<String,String> header){
        String result = null;
        if(SpringContextHolder.isOnline()){
            result = HttpProxyUtils.get(urlString,header);
        }else {
            result = HttpUtil.get(urlString);
        }

        return result;
    }
}
