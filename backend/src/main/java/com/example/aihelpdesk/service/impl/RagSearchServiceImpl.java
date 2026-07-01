package com.example.aihelpdesk.service.impl;

import com.example.aihelpdesk.common.CurrentUser;
import com.example.aihelpdesk.common.CurrentUserContext;
import com.example.aihelpdesk.model.dto.SearchChunkRequest;
import com.example.aihelpdesk.model.dto.SearchChunkResult;
import com.example.aihelpdesk.model.entity.DocumentChunk;
import com.example.aihelpdesk.model.entity.KnowledgeBase;
import com.example.aihelpdesk.service.IDocumentChunkService;
import com.example.aihelpdesk.service.IKnowledgeBaseService;
import com.example.aihelpdesk.service.RagSearchService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;

/**
 * @Author wzh
 * @Date 2026/7/1 13:55
 */
@Service
public class RagSearchServiceImpl implements RagSearchService {

    private static final int DEFAULT_TOP_K = 5;
    private static final int MAX_TOP_K = 20;

    private final IDocumentChunkService documentChunkService;

    private final IKnowledgeBaseService knowledgeBaseService;

    public RagSearchServiceImpl(IDocumentChunkService documentChunkService,
                                IKnowledgeBaseService knowledgeBaseService) {
        this.documentChunkService = documentChunkService;
        this.knowledgeBaseService = knowledgeBaseService;
    }

    @Override
    public List<SearchChunkResult> searchChunks(Long knowledgeBaseId, SearchChunkRequest request) {
        CurrentUser currentUser = CurrentUserContext.getRequired();

        // 核心思路：
        // 1. 检索入口先校验知识库归属，避免用户检索别人的文档分片。
        // 2. 当前阶段还没有真实向量库，所以先只查 document_chunk 表中已向量化的分片。
        // 3. 用简单关键词命中计算 score，先跑通“问题 -> 召回 chunk -> 返回来源”的 RAG 前半段。
        // 4. 后续接 pgvector/Milvus 时，只替换打分和召回逻辑，Controller 和权限规则不变。
        checkKnowledgeBaseOwner(knowledgeBaseId, currentUser.id());

        if (request == null || !StringUtils.hasText(request.getQuery())) {
            throw new IllegalArgumentException("query 不能为空");
        }

        int topK = normalizeTopK(request.getTopK());
        String query = request.getQuery().trim();

        List<DocumentChunk> chunks = documentChunkService.lambdaQuery()
                .eq(DocumentChunk::getKnowledgeBaseId, knowledgeBaseId)
                .isNotNull(DocumentChunk::getVectorId)
                .list();
        return chunks.stream()
                .map(chunk -> toSearchResult(chunk, calculateScore(query, chunk.getContent())))
                .filter(result ->result.getScore() >0)
                .sorted(Comparator.comparing(SearchChunkResult ::getScore).reversed())
                .limit(topK)
                .toList();

    }

    private double calculateScore(String query, String content) {
        if (!StringUtils.hasText(content)) {
            return 0;
        }

        String normalizedQuery = query.toLowerCase();
        String normalizedContent = content.toLowerCase();

        if (normalizedContent.contains(normalizedQuery)) {
            return 1.0;
        }

        String[] words = normalizedQuery.split("\\s+");
        int hitCount = 0;
        for (String word : words) {
            if (StringUtils.hasText(word) && normalizedContent.contains(word)) {
                hitCount++;
            }
        }

        if (hitCount == 0) {
            return 0;
        }

        return (double) hitCount / words.length;
    }

    private int normalizeTopK(Integer topK) {
        if (topK == null || topK <= 0) {
            return DEFAULT_TOP_K;
        }
        return Math.min(topK, MAX_TOP_K);
    }


    private SearchChunkResult toSearchResult(DocumentChunk chunk, double score) {
        SearchChunkResult result = new SearchChunkResult();
        result.setChunkId(chunk.getId());
        result.setDocumentId(chunk.getDocumentId());
        result.setKnowledgeBaseId(chunk.getKnowledgeBaseId());
        result.setChunkIndex(chunk.getChunkIndex());
        result.setContent(chunk.getContent());
        result.setVectorId(chunk.getVectorId());
        result.setScore(score);
        return result;
    }

    private void checkKnowledgeBaseOwner(Long knowledgeBaseId, Long currentUserId) {
        KnowledgeBase knowledgeBase = knowledgeBaseService.getById(knowledgeBaseId);
        if (knowledgeBase == null || !currentUserId.equals(knowledgeBase.getOwnerId())) {
            throw new IllegalArgumentException("知识库不存在或无权访问");
        }
    }
}
