package com.jia.demo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author bzzz
 * @date 2020/8/25 16:21
 * All rights Reserved, Designed By https://github.com/cnbzzz
 */
@Data
public class RoleResult implements Serializable {

    private static final long serialVersionUID = -7837746356625315288L;

    private Long roleId;

    private String roleName;

}
