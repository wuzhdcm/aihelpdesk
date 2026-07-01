package com.example.aihelpdesk.service.embedding;

/**
 * @Author wzh
 * @Date 2026/7/1 13:20
 */
public interface EmbeddingClient {

    String embedChunk(Long chunkId, String content);
}
