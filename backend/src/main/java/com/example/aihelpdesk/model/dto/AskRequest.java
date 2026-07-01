package com.example.aihelpdesk.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author wzh
 * @Date 2026/7/1 16:52
 */
@Getter
@Setter
public class AskRequest {

    private Long knowledgeBaseId;

    private String question;

    private Integer topK;
}
