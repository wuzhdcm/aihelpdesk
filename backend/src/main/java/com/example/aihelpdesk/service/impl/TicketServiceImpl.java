package com.example.aihelpdesk.service.impl;

import com.example.aihelpdesk.common.CurrentUser;
import com.example.aihelpdesk.common.CurrentUserContext;
import com.example.aihelpdesk.model.dto.CreateTicketRequest;
import com.example.aihelpdesk.model.entity.Ticket;
import com.example.aihelpdesk.mapper.TicketMapper;
import com.example.aihelpdesk.service.ITicketService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    @Override
    public Ticket createTicket(CreateTicketRequest request) {
        CurrentUser currentUser = CurrentUserContext.getRequired();

        LocalDateTime now = LocalDateTime.now();

        Ticket ticket = new Ticket();

        ticket.setCreatorId(currentUser.id());
        ticket.setTicketNo(generateTicketNo(now));
        ticket.setCategory(request.category());
        ticket.setDescription(request.description());
        ticket.setCreateTime(now);
        ticket.setUpdateTime(now);
        ticket.setAiGenerated(false);
        ticket.setDeleted(false);
        ticket.setPriority(request.priority());
        ticket.setStatus("PENDING");
        ticket.setTitle(request.title());

        save(ticket);
        return ticket;
    }

    @Override
    public List<Ticket> listMyTickets() {
        CurrentUser currentUser = CurrentUserContext.getRequired();
        return lambdaQuery().eq(Ticket::getCreatorId, currentUser.id())
                .orderByDesc(Ticket::getCreateTime)
                .list();
    }

    private String generateTicketNo(LocalDateTime now){
        return "T" + now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    };
}
