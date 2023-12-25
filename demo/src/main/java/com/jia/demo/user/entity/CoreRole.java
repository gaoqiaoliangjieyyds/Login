package com.jia.demo.user.entity;

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
 * 
 * </p>
 *
 * @author jia
 * @since 2023-11-08
 */
@Getter
@Setter
@TableName("core_role")
@ApiModel(value = "CoreRole对象", description = "")
public class CoreRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId("ROLE_ID")
    private Long roleId;

    @ApiModelProperty("序号")
    @TableField("SORT")
    private Integer sort;

    @ApiModelProperty("乐观锁")
    @TableField("VERSION")
    private Integer version;

    @ApiModelProperty("状态")
    @TableField("STATUS")
    private String status;

    @ApiModelProperty("创建人")
    @TableField("CREATE_USER")
    private Long createUser;

    @ApiModelProperty("提示")
    @TableField("DESCRIPTION")
    private String description;

    @ApiModelProperty("角色名称")
    @TableField("NAME")
    private String name;

    @ApiModelProperty("更新人")
    @TableField("UPDATE_USER")
    private Long updateUser;

    @ApiModelProperty("创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;


}
