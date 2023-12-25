package com.jia.demo.annotation;

import io.swagger.annotations.ApiModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ChenJia
 * @create 2023-11-08 10:36
 */
@ApiModel("数据字典注解")
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {
    /**
     * 字典类型
     * @return
     */
    String dictCode();

    /**
     * 返回属性名
     * @return
     */
    String dictText() default "";
}
