package com.jia.demo.state;

import io.swagger.annotations.ApiModel;

/**
 * @author ChenJia
 * @create 2023-11-07 16:13
 */
@ApiModel("响应码的类别")
public interface CodeConstant {

    Integer ERROR = 500;
    Integer SUCCESS = 200;

}
