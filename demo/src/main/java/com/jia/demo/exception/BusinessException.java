package com.jia.demo.exception;/**
 * @author ChenJia
 * @create 2023-12-19 10:26
 */

/**
 *@ClassName BusinessException
 *@Description TODO
 *@Author jia
 *@Date 2023/12/19 10:26
 *@Version 1.0
 **/
public class BusinessException extends RuntimeException{
    /**
     * 错误码
     */
    private final int code;



    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
    public BusinessException(String error) {
        super(error);
        this.code = 500;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
