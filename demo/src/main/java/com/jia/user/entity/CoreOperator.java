package com.jia.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 操作员
 * </p>
 *
 * @author jia
 * @since 2023-11-07
 */
@Getter
@Setter
@TableName("core_operator")
@ApiModel(value = "CoreOperator对象", description = "操作员")
public class CoreOperator implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId("OPER_ID")
    private Long operId;

    @ApiModelProperty("头像")
    @TableField("AVATAR")
    private String avatar;

    @ApiModelProperty("账号")
    @TableField("ACCOUNT")
    private String account;

    @ApiModelProperty("密码")
    @TableField("PASSWORD")
    private String password;

    @ApiModelProperty("md5密码盐")
    @TableField("SALT")
    private String salt;

    @ApiModelProperty("名字")
    @TableField("NAME")
    private String name;

    @ApiModelProperty("生日")
    @TableField("BIRTHDAY")
    private Date birthday;

    @ApiModelProperty("性别(字典)")
    @TableField("SEX")
    private String sex;

    @ApiModelProperty("电子邮件")
    @TableField("EMAIL")
    private String email;

    @ApiModelProperty("电话")
    @TableField("PHONE")
    private String phone;

    @ApiModelProperty("部门id")
    @TableField("DEPT_ID")
    private Long deptId;

    @ApiModelProperty("状态(字典)")
    @TableField("STATUS")
    private String status;

    @ApiModelProperty("创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty("创建人")
    @TableField("CREATE_USER")
    private Long createUser;

    @ApiModelProperty("更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @ApiModelProperty("更新人")
    @TableField("UPDATE_USER")
    private Long updateUser;

    @ApiModelProperty("乐观锁")
    @TableField("VERSION")
    private Integer version;


}
