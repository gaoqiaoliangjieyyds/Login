package com.jia.demo.user.service;

import cn.hutool.core.lang.tree.Tree;
import com.jia.demo.user.controller.in.EditMenuParam;
import com.jia.demo.user.controller.in.KinthAdd;
import com.jia.demo.user.entity.CoreMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jia
 * @since 2023-11-09
 */
public interface CoreMenuService extends IService<CoreMenu> {
    List<CoreMenu> listOperMenu(Long operId);

    List<Tree<Long>> menuListToTree(List<CoreMenu> menuList);

    List<Tree<Long>> tree(String name);

//    @Validated(KinthAdd.class)
//    Long add(@Valid EditMenuParam param);
}
