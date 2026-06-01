package com.example.aihelpdesk.service.impl;

import com.example.aihelpdesk.model.entity.Ticket;
import com.example.aihelpdesk.mapper.TicketMapper;
import com.example.aihelpdesk.service.ITicketService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工单表 服务实现类
 * </p>
 *
 * @author wuzh
 * @since 2026-06-02
 */
@Service
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket> implements ITicketService {

}
