package com.example.aihelpdesk.controller;

import com.example.aihelpdesk.common.Result;
import com.example.aihelpdesk.model.dto.CreateKnowledgeBaseRequest;
import com.example.aihelpdesk.model.entity.KnowledgeBase;
import com.example.aihelpdesk.service.IKnowledgeBaseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author wzh
 * @Date 2026/6/5 01:55
 */
@RestController
@RequestMapping("/api/knowledge-bases")
public class KnowledgeBaseController {

    private final IKnowledgeBaseService knowledgeBaseService;

    public KnowledgeBaseController(IKnowledgeBaseService knowledgeBaseService) {
        this.knowledgeBaseService = knowledgeBaseService;
    }

    @PostMapping
    public Result<KnowledgeBase> createKnowledgeBase(@Valid @RequestBody CreateKnowledgeBaseRequest request) {
        return Result.success(knowledgeBaseService.createKnowledgeBase(request));
    }

    @GetMapping("/my")
    public Result<List<KnowledgeBase>> my() {
        return Result.success(knowledgeBaseService.listMyKnowledgeBases());
    }
}
