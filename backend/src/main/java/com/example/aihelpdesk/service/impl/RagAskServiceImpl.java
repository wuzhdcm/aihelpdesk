package com.example.aihelpdesk.service.impl;

import com.example.aihelpdesk.model.dto.*;
import com.example.aihelpdesk.service.PromptService;
import com.example.aihelpdesk.service.RagAskService;
import com.example.aihelpdesk.service.RagSearchService;
import com.example.aihelpdesk.service.model.ChatModelClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author wzh
 * @Date 2026/7/1 17:16
 */
@Service
public class RagAskServiceImpl implements RagAskService {

    private static final int DEFAULT_TOP_K = 5;

    private final RagSearchService ragSearchService;

    private final PromptService promptService;

    private final ChatModelClient chatModelClient;

    public RagAskServiceImpl(RagSearchService ragSearchService,
                             PromptService promptService,
                             ChatModelClient chatModelClient) {
        this.ragSearchService = ragSearchService;
        this.promptService = promptService;
        this.chatModelClient = chatModelClient;
    }
    @Override
    public AskResponse ask(AskRequest request) {
        // 核心思路：
        // 1. RAG 问答不直接查 document_chunk，而是复用 RagSearchService，保证权限校验和召回规则一致。
        // 2. 先召回 TopK 分片，再由 PromptService 统一拼 Prompt，避免提示词散落在 Controller。
        // 3. 当前阶段使用 FakeChatModelClient，先跑通“检索 -> Prompt -> 回答 -> 引用”的业务闭环。
        // 4. 后续接真实大模型时，只替换 ChatModelClient 实现，不改 RAG 问答主流程。
        if (request == null) {
            throw new IllegalArgumentException("请求不能为空");
        }
        if (request.getKnowledgeBaseId() == null) {
            throw new IllegalArgumentException("knowledgeBaseId 不能为空");
        }
        if (!StringUtils.hasText(request.getQuestion())) {
            throw new IllegalArgumentException("question 不能为空");
        }

        SearchChunkRequest searchRequest = new SearchChunkRequest();
        searchRequest.setQuery(request.getQuestion());
        searchRequest.setTopK(request.getTopK() == null ? DEFAULT_TOP_K : request.getTopK());

        List<SearchChunkResult> chunks = ragSearchService.searchChunks(request.getKnowledgeBaseId(), searchRequest);
        if (chunks.isEmpty()) {
            throw new IllegalArgumentException("知识库中没有检索到相关内容");
        }
        String prompt = promptService.buildRagPrompt(request.getQuestion(), chunks);
        String answer = chatModelClient.chat(prompt);

        AskResponse response = new AskResponse();
        response.setAnswer(answer);
        response.setCitations(toCitations(chunks));

        return response;
    }
    private List<CitationResult> toCitations(List<SearchChunkResult> chunks) {
        return chunks.stream()
                .map(chunk -> {
                    CitationResult citation = new CitationResult();
                    citation.setChunkId(chunk.getChunkId());
                    citation.setDocumentId(chunk.getDocumentId());
                    citation.setKnowledgeBaseId(chunk.getKnowledgeBaseId());
                    citation.setChunkIndex(chunk.getChunkIndex());
                    citation.setScore(chunk.getScore());
                    citation.setContentSnapshot(chunk.getContent());
                    return citation;
                })
                .toList();
    }
}
