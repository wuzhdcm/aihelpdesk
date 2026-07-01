package com.example.aihelpdesk.controller;

import com.example.aihelpdesk.common.Result;
import com.example.aihelpdesk.model.entity.DocumentChunk;
import com.example.aihelpdesk.model.entity.EmbeddingTask;
import com.example.aihelpdesk.model.entity.KnowledgeDocument;
import com.example.aihelpdesk.service.IKnowledgeDocumentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author wzh
 * @Date 2026/6/6 02:25
 */
@RequestMapping("/api/documents")
@RestController
public class DocumentController {

    private final IKnowledgeDocumentService knowledgeDocumentService;

    public DocumentController( IKnowledgeDocumentService knowledgeDocumentService) {
        this.knowledgeDocumentService = knowledgeDocumentService;
    }

    @PostMapping("/{id}/parse")
    public Result<KnowledgeDocument> parseDocument(@PathVariable Long id) {
        return Result.success(knowledgeDocumentService.parseDocument(id));
    }

    @GetMapping("/{id}/chunks")
    public Result<List<DocumentChunk>> listDocumentChunks(@PathVariable Long id) {
        return Result.success(knowledgeDocumentService.listDocumentChunks(id));
    }

    @GetMapping("/{id}/tasks")
    public Result<List<EmbeddingTask>> listDocumentTasks(@PathVariable Long id) {
        return Result.success(knowledgeDocumentService.listDocumentTasks(id));
    }

    @PostMapping("/{id}/embed")
    public Result<List<DocumentChunk>> embedDocument(@PathVariable Long id) {
        return Result.success(knowledgeDocumentService.embedDocument(id));
    }
}
