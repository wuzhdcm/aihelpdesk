package com.example.aihelpdesk.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author wzh
 * @Date 2026/7/2 18:31
 */
@Getter
@Setter
public class CreateChatSessionRequest {

    private Long knowledgeBaseId;

    private String title;
}
