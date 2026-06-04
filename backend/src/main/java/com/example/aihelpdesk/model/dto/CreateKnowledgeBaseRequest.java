package com.example.aihelpdesk.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @Author wzh
 * @Date 2026/6/5 01:46
 */
public record CreateKnowledgeBaseRequest(
        @NotBlank
        @Size(max = 128)
        String name,

        @Size(max = 2000)
        String description,

        @NotBlank
        @Size(max = 20)
        String visibility
) {
}
