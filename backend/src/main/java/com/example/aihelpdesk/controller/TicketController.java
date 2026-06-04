package com.example.aihelpdesk.controller;

import com.example.aihelpdesk.common.Result;
import com.example.aihelpdesk.model.dto.CreateTicketRequest;
import com.example.aihelpdesk.model.entity.Ticket;
import com.example.aihelpdesk.service.ISysUserService;
import com.example.aihelpdesk.service.ITicketService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 工单表 前端控制器
 * </p>
 *
 * @author wuzh
 * @since 2026-06-02
 */
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final ITicketService iTicketService;

    public TicketController(ITicketService iTicketService) {
        this.iTicketService = iTicketService;
    }


    @PostMapping
    public Result<Ticket> createTicket(@Valid @RequestBody CreateTicketRequest createTicketRequest) {

        return  Result.success(iTicketService.createTicket(createTicketRequest));
    }
}
