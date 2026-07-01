package com.example.aihelpdesk.controller;

import com.example.aihelpdesk.common.Result;
import com.example.aihelpdesk.model.dto.AskRequest;
import com.example.aihelpdesk.model.dto.AskResponse;
import com.example.aihelpdesk.service.RagAskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author wzh
 * @Date 2026/7/1 17:31
 */
@RestController
@RequestMapping("/api/rag")
public class RagAskController {

    private final RagAskService ragAskService;

    public RagAskController(RagAskService ragAskService) {
        this.ragAskService = ragAskService;
    }

    @PostMapping("/ask")
    public Result<AskResponse> ask(@RequestBody AskRequest request) {
        return Result.success(ragAskService.ask(request));
    }
}
