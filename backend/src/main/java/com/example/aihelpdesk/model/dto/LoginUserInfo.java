package com.example.aihelpdesk.model.dto;

import java.util.List;

/**
 * @Author wzh
 * @Date 2026/6/3 03:07
 */
public record LoginUserInfo(
        Long id,
        String username,
        String nickname,
        List<String> roles
) {
}
