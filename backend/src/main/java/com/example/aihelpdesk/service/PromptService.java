package com.example.aihelpdesk.service;

import com.example.aihelpdesk.model.dto.SearchChunkResult;

import java.util.List;

/**
 * @Author wzh
 * @Date 2026/7/1 16:55
 */
public interface PromptService {
    String buildRagPrompt(String question, List<SearchChunkResult> chunks);
}
