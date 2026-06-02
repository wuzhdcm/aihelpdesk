package com.example.aihelpdesk.common;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @Author wzh
 * @Date 2026/6/3 03:28
 */
@Component
public class SimpleTokenService {
    public String generate(Long userId, String username) {
        String raw = userId + ":" + username + ":" + System.currentTimeMillis();
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }
}
