package com.example.aihelpdesk.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author wzh
 * @Date 2026/7/1 13:54
 */
@Getter
@Setter
public class SearchChunkResult {

    private Long chunkId;

    private Long documentId;

    private Long knowledgeBaseId;

    private Integer chunkIndex;

    private String content;

    private String vectorId;

    private Double score;
}
