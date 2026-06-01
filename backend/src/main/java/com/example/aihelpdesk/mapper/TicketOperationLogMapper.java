package com.example.aihelpdesk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.aihelpdesk.model.entity.TicketOperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 工单操作日志表 Mapper 接口
 * </p>
 *
 * @author wuzh
 * @since 2026-06-02
 */
@Mapper
public interface TicketOperationLogMapper extends BaseMapper<TicketOperationLog> {

}
