package com.jia.demo.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2023-11-09
 */
@Getter
@Setter
@TableName("core_menu")
@ApiModel(value = "CoreMenu对象", description = "")
public class CoreMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    @ApiModelProperty("菜单层级")
    @TableField("levels")
    private Integer levels;

    @ApiModelProperty("菜单排序号")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty("创建人")
    @TableField("create_user")
    private Long createUser;

    @ApiModelProperty("备注")
    @TableField("description")
    private String description;

    @ApiModelProperty("菜单图标")
    @TableField("icon")
    private String icon;

    @ApiModelProperty("是否是菜单（字典）")
    @TableField("menu_flag")
    private String menuFlag;

    @ApiModelProperty("菜单名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("是否打开新页面的标识（字典）")
    @TableField("new_page_flag")
    private String newPageFlag;

    @ApiModelProperty("是否打开（字典）")
    @TableField("open_flag")
    private String openFlag;

    @ApiModelProperty("菜单父编号")
    @TableField("pid")
    private Long pid;

    @ApiModelProperty("当前菜单的所有父菜单编号")
    @TableField("pids")
    private String pids;

    @ApiModelProperty("菜单状态（字典）")
    @TableField("status")
    private String status;

    @ApiModelProperty("系统分类（字典）")
    @TableField("system_type")
    private String systemType;

    @ApiModelProperty("更新人")
    @TableField("update_user")
    private Long updateUser;

    @ApiModelProperty("url地址")
    @TableField("url")
    private String url;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField("update_time")
    private Date updateTime;


    private String createTimeStr;


}
