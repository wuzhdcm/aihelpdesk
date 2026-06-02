package com.example.aihelpdesk.model.dto;

/**
 * @Author wzh
 * @Date 2026/6/3 03:06
 */
public record LoginResponse(
        String token,
        LoginUserInfo userInfo
) {
}
