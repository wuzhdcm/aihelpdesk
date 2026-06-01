package com.example.aihelpdesk.service.impl;

import com.example.aihelpdesk.model.entity.TicketOperationLog;
import com.example.aihelpdesk.mapper.TicketOperationLogMapper;
import com.example.aihelpdesk.service.ITicketOperationLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工单操作日志表 服务实现类
 * </p>
 *
 * @author wuzh
 * @since 2026-06-02
 */
@Service
public class TicketOperationLogServiceImpl extends ServiceImpl<TicketOperationLogMapper, TicketOperationLog> implements ITicketOperationLogService {

}
