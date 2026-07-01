package com.example.aihelpdesk.service.model;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @Author wzh
 * @Date 2026/7/1 16:54
 */
@Component
public class FakeChatModelClient implements ChatModelClient {

    @Override
    public String chat(String prompt) {
        if (!StringUtils.hasText(prompt)) {
            throw new IllegalArgumentException("prompt 不能为空");
        }

        return "这是基于知识库召回内容生成的模拟回答。请根据返回的 citations 查看答案依据。";
    }
}
