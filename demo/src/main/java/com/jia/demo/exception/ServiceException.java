package com.jia.demo.exception;/**
 * @author ChenJia
 * @create 2023-11-07 16:20
 */

/**
 *@ClassName ServiceException
 *@Description TODO
 *@Author jia
 *@Date 2023/11/7 16:20
 *@Version 1.0
 **/
public class ServiceException extends RuntimeException implements IKinthException{
    private Integer code;
    private String errorMessage;

    public ServiceException(Integer code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
