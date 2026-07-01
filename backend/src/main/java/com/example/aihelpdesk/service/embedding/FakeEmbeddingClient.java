package com.example.aihelpdesk.service.embedding;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @Author wzh
 * @Date 2026/7/1 13:21
 */
@Component
public class FakeEmbeddingClient implements EmbeddingClient {

    @Override
    public String embedChunk(Long chunkId, String content) {
        if (chunkId == null) {
            throw new IllegalArgumentException("chunkId 不能为空");
        }
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("分片内容不能为空");
        }

        return "fake-vector-" + chunkId + "-" + Math.abs(content.hashCode());
    }
}
