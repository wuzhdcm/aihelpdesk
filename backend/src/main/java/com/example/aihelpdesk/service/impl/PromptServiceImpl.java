package com.example.aihelpdesk.service.impl;

import com.example.aihelpdesk.model.dto.SearchChunkResult;
import com.example.aihelpdesk.service.PromptService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author wzh
 * @Date 2026/7/1 17:03
 */
@Service
public class PromptServiceImpl implements PromptService {
    @Override
    public String buildRagPrompt(String question, List<SearchChunkResult> chunks) {
        if (!StringUtils.hasText(question)) {
            throw new IllegalArgumentException("question 不能为空");
        }
        if (chunks == null || chunks.isEmpty()) {
            throw new IllegalArgumentException("没有可用的知识库上下文");
        }

        StringBuilder prompt = new StringBuilder();
        prompt.append("你是企业服务台知识库助手，请只根据给定资料回答问题。\n");
        prompt.append("如果资料中没有答案，请说明知识库中没有找到明确依据。\n\n");

        prompt.append("用户问题：\n");
        prompt.append(question).append("\n\n");

        for (int i = 0 ; i < chunks.size() ; i++) {
            SearchChunkResult chunk = chunks.get(i);
            prompt.append("[资料").append(i + 1).append("]\n");
            prompt.append("chunkId=").append(chunk.getChunkId()).append("\n");
            prompt.append(chunk.getContent()).append("\n");
        }
        return prompt.toString();
    }
}
