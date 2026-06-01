package com.example.aihelpdesk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.aihelpdesk.model.entity.Ticket;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 工单表 Mapper 接口
 * </p>
 *
 * @author wuzh
 * @since 2026-06-02
 */
@Mapper
public interface TicketMapper extends BaseMapper<Ticket> {

}
