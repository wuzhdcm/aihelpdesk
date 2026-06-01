package com.example.aihelpdesk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.aihelpdesk.model.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author wuzh
 * @since 2026-06-02
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}
