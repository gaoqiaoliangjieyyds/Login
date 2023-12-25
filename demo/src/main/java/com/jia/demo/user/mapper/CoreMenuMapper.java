package com.jia.demo.user.mapper;

import com.jia.demo.user.entity.CoreMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jia
 * @since 2023-11-09
 */
@Mapper
public interface CoreMenuMapper extends BaseMapper<CoreMenu> {

    List<CoreMenu> listOperMenu(@Param("operId") Long operId);
}
