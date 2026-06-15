package com.example.aihelpdesk.service.parser;

import com.example.aihelpdesk.model.entity.KnowledgeDocument;
import org.springframework.stereotype.Component;

/**
 * @Author wzh
 * @Date 2026/6/16 00:03
 */
@Component
public class MarkdownDocumentParser implements DocumentParser {

    @Override
    public boolean supports(String fileType) {
        return "MD".equalsIgnoreCase(fileType)
                || "MARKDOWN".equalsIgnoreCase(fileType);
    }

    @Override
    public String parse(KnowledgeDocument document, String rawContent) {
        // 核心思路：
        // 1. Markdown 本质上是文本，当前阶段先保留标题、列表、代码块等原始结构。
        // 2. 这样切片后仍能看到文档层级，后续做 RAG 时标题信息也有价值。
        // 3. 这里只做换行标准化，不提前引入复杂 Markdown 解析库。
        return rawContent
                .replace("\r\n", "\n")
                .replace("\r", "\n")
                .trim();
    }
}
