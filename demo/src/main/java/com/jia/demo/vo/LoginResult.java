package com.jia.demo.vo;/**
 * @author ChenJia
 * @create 2023-11-07 15:55
 */

import cn.hutool.core.lang.tree.Tree;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.jia.demo.annotation.Dict;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *@ClassName LoginResult
 *@Description 登录信息结果返回类
 *@Author jia
 *@Date 2023/11/7 15:55
 *@Version 1.0
 **/
@Data
public class LoginResult implements Serializable {
    private static final long serialVersionUID = 1468507592559090758L;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long operId;

    private String account;

    private String name;
    private String deptName;

    @Dict(dictCode = "SEX")
    private String sex;

    private String phone;

    private String email;

    private String avatar;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;


    private Long deptId;

    private Long deptPid;


    private String status;

    private List<Long> roleIds;

    private List<RoleResult> roles;

    private List<Tree<Long>> menus;


}
