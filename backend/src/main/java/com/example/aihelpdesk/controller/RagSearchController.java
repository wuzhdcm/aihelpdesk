package com.example.aihelpdesk.controller;

import com.example.aihelpdesk.common.Result;
import com.example.aihelpdesk.model.dto.SearchChunkRequest;
import com.example.aihelpdesk.model.dto.SearchChunkResult;
import com.example.aihelpdesk.service.RagSearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author wzh
 * @Date 2026/7/1 14:08
 */
@RestController
@RequestMapping("/api/knowledge-bases")
public class RagSearchController {

    private final RagSearchService ragSearchService;

    public RagSearchController(RagSearchService ragSearchService) {
        this.ragSearchService = ragSearchService;
    }

    @PostMapping("/{knowledgeBaseId}/search")
    public Result<List<SearchChunkResult>> searchChunks(@PathVariable Long knowledgeBaseId,
                                                        @RequestBody SearchChunkRequest request) {
        return Result.success(ragSearchService.searchChunks(knowledgeBaseId, request));
    }
}
