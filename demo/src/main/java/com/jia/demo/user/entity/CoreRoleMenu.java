package com.jia.demo.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("core_role_menu")
@ApiModel(value = "CoreRoleMenu对象", description = "")
public class CoreRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("关联id")
    @TableId(value = "relation_id", type = IdType.AUTO)
    private Long relationId;

    @ApiModelProperty("菜单表id")
    @TableField("menu_id")
    private Long menuId;

    @ApiModelProperty("角色表id")
    @TableField("role_id")
    private Long roleId;


}
