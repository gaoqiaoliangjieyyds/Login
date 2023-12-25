package com.jia.demo.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jia.demo.state.ManagerStatus;
import com.jia.demo.user.controller.in.EditMenuParam;
import com.jia.demo.user.entity.CoreMenu;
import com.jia.demo.user.entity.CoreRoleMenu;
import com.jia.demo.user.mapper.CoreMenuMapper;
import com.jia.demo.user.service.CoreMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jia
 * @since 2023-11-09
 */
@Service
public class CoreMenuServiceImpl extends ServiceImpl<CoreMenuMapper, CoreMenu> implements CoreMenuService {

    @Autowired
    private CoreMenuMapper coreMenuMapper;

    /**
     * 菜单根节点id
     */
    Long MENU_ROOT_ID = 0L;

    @Override
    public List<CoreMenu> listOperMenu(Long operId) {
        List<CoreMenu> list = coreMenuMapper.listOperMenu(operId);
        return list;
    }

    @Override
    public List<Tree<Long>> menuListToTree(List<CoreMenu> menuList) {
        List<TreeNode<Long>> treeNodeList = menuList.stream().map(m->{
            TreeNode<Long> node = new TreeNode<>(m.getMenuId(), m.getPid(), m.getName(), m.getMenuId());
            node.setExtra(BeanUtil.beanToMap(m));
            return node;
        }).collect(Collectors.toList());
        new TreeNode<>(0L,"","顶层",0);
        return TreeUtil.build(treeNodeList,MENU_ROOT_ID);
    }

    @Override
    public List<Tree<Long>> tree(String name) {
        LambdaQueryWrapper<CoreMenu> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<CoreMenu> wrapper = queryWrapper.eq(CoreMenu::getStatus, ManagerStatus.ENABLE).like(CoreMenu::getName, !"null".equalsIgnoreCase(name)).orderByDesc(CoreMenu::getMenuId).select();
        List<CoreMenu> list = coreMenuMapper.selectList(wrapper);
        list.forEach(t->{
            if(ObjectUtil.isNotNull(t.getCreateTime())){
                t.setCreateTimeStr(new SimpleDateFormat("yyyy-MM-dd").format(t.getCreateTime()));
            }
        });
        return menuListToTree(list);
    }

//    @Override
//    public Long add(EditMenuParam param) {
//        param.setMenuId(null);
//        CoreMenu menu = new CoreMenu();
//        BeanUtil.copyProperties(param, menu);
//        menu.setMenuFlag(param.getIsmenu());
//        Long pid = param.getPid();
//        coreMenuMapper.selectOne(new)
//        CoreMenu parent = coreMenuDao.single(pid);
//        if (MENU_ROOT_ID.equals(pid)) {
//            parent = getMenuRoot();
//        }
//        Assert.isTrue(parent != null, "父菜单未找到");
//        menu.setLevels(parent.getLevels() + 1);
//
//        String pids = String.format("%s[%s],", parent.getPids(), parent.getMenuId());
//        menu.setPids(pids);
//
//        menu.setStatus(MenuStatus.ENABLE.getCode());
//        menu.setUpdateTime(null);
//        menu.setUpdateUser(null);
//        Long operId = HttpContext.getOperator().getOperId();
//        menu.setCreateUser(operId);
//        menu.setCreateTime(new Date());
//        coreMenuDao.insert(menu);
//
//        // 新增菜单时添加超级管理员权限
//        CoreRoleMenu rm = new CoreRoleMenu();
//        rm.setRoleId(Constant.ADMIN_ROLE_ID);
//        rm.setMenuId(menu.getMenuId());
//        coreRoleMenuDao.insert(rm);
//        return menu.getMenuId();
//    }
}
