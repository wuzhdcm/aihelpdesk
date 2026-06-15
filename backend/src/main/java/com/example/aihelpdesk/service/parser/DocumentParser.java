package com.example.aihelpdesk.service.parser;

import com.example.aihelpdesk.model.entity.KnowledgeDocument;

/**
 * @Author wzh
 * @Date 2026/6/16 00:02
 */
public interface DocumentParser {

    boolean supports(String fileType);

    String parse(KnowledgeDocument document, String rawContent);
}
