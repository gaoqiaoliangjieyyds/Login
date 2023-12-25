package com.jia.demo.user.controller;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import com.jia.demo.user.controller.in.EditMenuParam;
import com.jia.demo.user.service.CoreMenuService;
import com.jia.demo.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jia
 * @since 2023-11-09
 */
@RestController
@RequestMapping("/user/coreMenu")
public class CoreMenuController {
    private CoreMenuService menuService;

    @PostMapping("/list")
    public Result list(@RequestBody Map<String, Object> param) {
        String menuName = Convert.toStr(param.get("menuName"));
        List<Tree<Long>> menu = menuService.tree(menuName);
        return Result.ok(menu);
    }

//    @PostMapping("/add")
//    public Result add(@RequestBody EditMenuParam param) {
//        Long menuId = menuService.add(param);
//        return  Result.ok(menuId);
//    }
//
//    @PostMapping("/edit")
//    public CodeMsg edit(@RequestBody EditMenuParam param) {
//        Long menuId = menuService.edit(param);
//        return new CodeMsg(menuId);
//    }
//
//    @PostMapping("/del")
//    public CodeMsg del(@RequestBody Map<String,Long> menuId) {
//        menuService.del(menuId.get("menuId"));
//        return CodeMsg.success();
//    }

}

