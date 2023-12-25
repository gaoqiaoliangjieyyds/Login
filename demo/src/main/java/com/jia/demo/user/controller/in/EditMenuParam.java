package com.jia.demo.user.controller.in;/**
 * @author ChenJia
 * @create 2023-11-10 10:02
 */

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *@ClassName EditMenuParam
 *@Description TODO
 *@Author jia
 *@Date 2023/11/10 10:02
 *@Version 1.0
 **/
@Data
public class EditMenuParam {

    @NotNull(message="menuId不能为空", groups = KinthEdit.class)
    private Long menuId;

    @NotNull(message="pid不能为空", groups = KinthAdd.class)
    private Long pid;

    @NotBlank(message="name不能为空", groups = KinthAdd.class)
    @Length(min=1, max=32, message="name必须在1-32位之间", groups = {KinthAdd.class, KinthEdit.class})
    private String name;

    @NotBlank(message="ismenu不能为空", groups = KinthAdd.class)
    private String ismenu;

    private Integer sort;

    private String icon;

    @NotBlank(message="url不能为空", groups = KinthAdd.class)
    private String url;

    @Length(max=16, message="status最多16位", groups = {KinthEdit.class})
    private String status;

}

