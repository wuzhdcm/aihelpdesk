package com.example.aihelpdesk.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author wzh
 * @Date 2026/7/1 16:52
 */
@Getter
@Setter
public class CitationResult {

    private Long chunkId;

    private Long documentId;

    private Long knowledgeBaseId;

    private Integer chunkIndex;

    private Double score;

    private String contentSnapshot;
}
