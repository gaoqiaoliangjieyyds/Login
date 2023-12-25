package com.jia.demo.config;/**
 * @author ChenJia
 * @create 2023-12-19 9:46
 */

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *@ClassName SpringContextHolder
 *@Description 获取Spring的上下文信息
 *@Author jia
 *@Date 2023/12/19 9:46
 *@Version 1.0
 **/

public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public static boolean isOnline(){
        String[] activeProfile = getActiveProfile();
        for(String s : activeProfile){
            if("online".equals(s)){
                return true;
            }
        }
        return false;
    }

    //使用配置文件可以在不更改代码的情况下更改应用程序的行为
    public static String[] getActiveProfile(){
        return getApplicationContext().getEnvironment().getActiveProfiles();
    }

    public static ApplicationContext getApplicationContext(){
        assertApplicationContext();
        return applicationContext;
    }

    //确保SpringContextHolder已经被Spring框架正确初始化，并且ApplicationContext已经被注入
    private static void assertApplicationContext(){
        if(applicationContext == null){
            throw new RuntimeException("applicaitonContext属性为null,请检查是否注入了SpringContextHolder!");
        }
    }
}
