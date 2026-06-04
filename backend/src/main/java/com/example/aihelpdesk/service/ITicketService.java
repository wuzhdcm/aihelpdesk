package com.example.aihelpdesk.service;

import com.example.aihelpdesk.model.dto.CreateTicketRequest;
import com.example.aihelpdesk.model.entity.Ticket;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 工单表 服务类
 * </p>
 *
 * @author wuzh
 * @since 2026-06-02
 */
public interface ITicketService extends IService<Ticket> {

    Ticket createTicket(CreateTicketRequest request);
}
