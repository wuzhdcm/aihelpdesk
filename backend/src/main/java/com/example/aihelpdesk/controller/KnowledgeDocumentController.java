package com.example.aihelpdesk.controller;

import com.example.aihelpdesk.common.Result;
import com.example.aihelpdesk.model.entity.KnowledgeDocument;
import com.example.aihelpdesk.service.IKnowledgeDocumentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author wzh
 * @Date 2026/6/5 02:41
 */
@RestController
@RequestMapping("/api/knowledge-bases/{knowledgeBaseId}/documents")
public class KnowledgeDocumentController {

    private final IKnowledgeDocumentService knowledgeDocumentService;

    public KnowledgeDocumentController(IKnowledgeDocumentService knowledgeDocumentService) {
        this.knowledgeDocumentService = knowledgeDocumentService;
    }

    @PostMapping("/upload")
    public Result<KnowledgeDocument> uploadDocument( @PathVariable Long knowledgeBaseId,
                                                     @RequestParam("file") MultipartFile file) {
        return Result.success(knowledgeDocumentService.uploadDocument(knowledgeBaseId,file));
    }

    @GetMapping
    public Result<List<KnowledgeDocument>> listDocuments(@PathVariable Long knowledgeBaseId) {
        return Result.success(knowledgeDocumentService.listDocuments(knowledgeBaseId));
    }

}
