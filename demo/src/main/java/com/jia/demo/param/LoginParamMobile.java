package com.jia.demo.param;/**
 * @author ChenJia
 * @create 2023-12-25 14:40
 */

import lombok.Data;

import java.io.Serializable;

/**
 *@ClassName LoginParamMobile
 *@Description TODO
 *@Author jia
 *@Date 2023/12/25 14:40
 *@Version 1.0
 **/
@Data
public class LoginParamMobile implements Serializable {

    private String phone;

    private String smsVerifyCode;

    private String verifyCode;

}
