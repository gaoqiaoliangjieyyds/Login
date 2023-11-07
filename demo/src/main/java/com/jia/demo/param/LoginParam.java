package com.jia.demo.param;/**
 * @author ChenJia
 * @create 2023-11-07 14:29
 */

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 *@ClassName LoginParam
 *@Description TODO
 *@Author jia
 *@Date 2023/11/7 14:29
 *@Version 1.0
 **/
@Data
@ApiModel("登录参数请求类")
public class LoginParam {
    private String username;

    private String password;

    private String verifyCode;
}
