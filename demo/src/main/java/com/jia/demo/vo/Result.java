package com.jia.demo.vo;/**
 * @author ChenJia
 * @create 2023-11-07 14:24
 */

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *@ClassName Result
 *@Description TODO
 *@Author jia
 *@Date 2023/11/7 14:24
 *@Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("统一结果返回类")
public class Result {
    /**
     * 是否成功
     **/
    private Boolean isSuccess;
    /**
     * 错误信息
     **/
    private String errorMsg;
    /**
     * 请求状态 200-成功 400-失败
     **/
    private Integer status;
    /**
     * 当前时间戳
     **/
    private Long timestamp;
    /**
     * 返回结果
     **/
    private Object data;

    public static Result ok() {
        return new Result(true, null, 200, System.currentTimeMillis(),null);
    }

    public static Result ok(Object data) {
        return new Result(true, null, 200,System.currentTimeMillis(),data);
    }

    public static Result ok(List<?> data) {
        return new Result(true, null, 200,System.currentTimeMillis(),data);
    }

    public static Result fail(String errorMsg) {
        return new Result(false, errorMsg, 400,System.currentTimeMillis(),null);
    }

}
