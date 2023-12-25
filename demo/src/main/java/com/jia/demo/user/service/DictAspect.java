package com.jia.demo.user.service;/**
 * @author ChenJia
 * @create 2023-11-08 17:24
 */

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jia.demo.annotation.Dict;
import com.jia.demo.util.OConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *@ClassName DictAspect
 *@Description TODO
 *@Author jia
 *@Date 2023/11/8 17:24
 *@Version 1.0
 **/
@Aspect
@Component
@Slf4j
public class DictAspect {

    @Autowired
    private RedisTemplate redisTemplate;

    private static String DICT_SUFFIX="_dictText";

    private static final String SYS_DICT = "sysDict";

    @Autowired
    private OConvertUtils oConvertUtils;

    public JSONObject parseDetail(Object record) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{}";
        try {
            // 解决@JsonFormat注解解析不了的问题详见SysAnnouncement类的@JsonFormat
            json = mapper.writeValueAsString(record);
        } catch (JsonProcessingException e) {
            log.error("Json解析失败：" + e);
        }
        JSONObject item = JSONObject.parseObject(json);

        for (Field field : oConvertUtils.getAllFields(record)) {
            //如果该属性上面有@Dict注解，则进行翻译
            if (ObjectUtil.isNotNull(field.getAnnotation(Dict.class))) {
                String dictCode = field.getAnnotation(Dict.class).dictCode();
                String dictText = field.getAnnotation(Dict.class).dictText();
                //获取当前待翻译的值
                String key = String.valueOf(item.get(field.getName()));
                //翻译字典值对应的text值
                String textValue = translateDictValue(dictCode, key);

                log.debug("字典Val：" + textValue);
                log.debug(("翻译字典字段：" + field.getName() + DICT_SUFFIX) + ":" + textValue);

                item.put(field.getName() + DICT_SUFFIX, (ObjectUtil.isEmpty(textValue) && !"null".equals(key))?key:textValue);

                //date类型默认转换string格式化日期
                if ("java.util.Date".equals(field.getType().getName()) && field.getAnnotation(JsonFormat.class) == null && item.get(field.getName()) != null) {
                    SimpleDateFormat aDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    item.put(field.getName(), aDate.format(new Date((Long) item.get(field.getName()))));
                }
            }
        }
        return item;
    }

    private String translateDictValue(String dictCode,String key){
        if(ObjectUtil.isEmpty(key) || "null".equals(key) || ObjectUtil.isEmpty(dictCode)){
            return "";
        }

        StringBuffer textValues = new StringBuffer();
        String[] keys = key.split(",");

        for (String k : keys
             ) {
            if (k.trim().length() == 0){
                continue;
            }
            Object o = redisTemplate.opsForValue().get((dictCode + "&&") + k);
            String dictName = "";
            if(ObjectUtil.isNotEmpty(o)){
                dictName = o.toString();
            }
            if(!"".equals(textValues.toString())){
                textValues.append(",");
            }
            if(ObjectUtil.isEmpty(dictName)){
                log.info("查询不到字典值_dictCode:"+dictCode+",k:"+k.trim());
                textValues.append(k);
            }else {
                textValues.append(dictName);
            }
        }
        return textValues.toString();
    }
}
