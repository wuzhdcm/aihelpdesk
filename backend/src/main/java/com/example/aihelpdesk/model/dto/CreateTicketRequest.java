package com.example.aihelpdesk.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @Author wzh
 * @Date 2026/6/5 00:12
 */
public record CreateTicketRequest(
        @NotBlank
        @Size(max = 100)
        String title,

        @NotBlank
        @Size(max = 2000)
        String description,

        @NotBlank
        @Size(max = 50)
        String category,

        @NotBlank
        @Size(max = 20)
        String priority
) {
}
