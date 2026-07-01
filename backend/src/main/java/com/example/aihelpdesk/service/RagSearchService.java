package com.example.aihelpdesk.service;

import com.example.aihelpdesk.model.dto.SearchChunkRequest;
import com.example.aihelpdesk.model.dto.SearchChunkResult;

import java.util.List;

/**
 * @Author wzh
 * @Date 2026/7/1 13:54
 */
public interface RagSearchService {

    List<SearchChunkResult> searchChunks(Long knowledgeBaseId, SearchChunkRequest request);
}
