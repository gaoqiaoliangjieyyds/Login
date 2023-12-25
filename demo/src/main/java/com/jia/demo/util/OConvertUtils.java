package com.jia.demo.util;/**
 * @author ChenJia
 * @create 2023-11-09 8:54
 */

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 *@ClassName OConvertUtils
 *@Description TODO
 *@Author jia
 *@Date 2023/11/9 8:54
 *@Version 1.0
 **/
@Component
public class OConvertUtils {

    public static Field[] getAllFields(Object object){
        Class<?> aClass = object.getClass();
        return getAllFields(aClass);
    }
}
