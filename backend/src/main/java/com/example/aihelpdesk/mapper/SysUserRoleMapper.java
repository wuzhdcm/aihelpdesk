package com.example.aihelpdesk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.aihelpdesk.model.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户角色关联表 Mapper 接口
 * </p>
 *
 * @author wuzh
 * @since 2026-06-02
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

}
