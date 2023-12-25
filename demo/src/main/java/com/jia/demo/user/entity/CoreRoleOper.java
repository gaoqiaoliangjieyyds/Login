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
 * @since 2023-11-08
 */
@Getter
@Setter
@TableName("core_role_oper")
@ApiModel(value = "CoreRoleOper对象", description = "")
public class CoreRoleOper implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("oper_id")
    private Long operId;

    @TableField("role_id")
    private Long roleId;


}
