package com.jia.demo.user.controller.in;/**
 * @author ChenJia
 * @create 2023-12-19 10:28
 */

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Map;

/**
 *@ClassName AgentEntity
 *@Description TODO
 *@Author jia
 *@Date 2023/12/19 10:28
 *@Version 1.0
 **/
@Data
@ApiModel("http请求的入参")
public class AgentEntity {
    private String httpMethod;

    private String targetUrl;

    private String objectString;

    private Map<String, String> headParams;
}
