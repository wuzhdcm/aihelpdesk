package com.example.aihelpdesk.model.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * @Author wzh
 * @Date 2026/6/3 03:03
 */
public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {
}
