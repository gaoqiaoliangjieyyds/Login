package com.jia.demo.util;/**
 * @author ChenJia
 * @create 2023-12-19 9:04
 */

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *@ClassName WechatConst
 *@Description TODO
 *@Author jia
 *@Date 2023/12/19 9:04
 *@Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class WechatConst extends RedirectMappingConst{

    private String baseUrlKey;

    private String oauth2AuthorizeUrl;

    private String appId;

    private String oauth2AccessTokenUrl;

    private String secret;
}
