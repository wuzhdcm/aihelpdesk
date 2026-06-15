package com.example.aihelpdesk.service.parser;

import com.example.aihelpdesk.model.entity.KnowledgeDocument;
import org.springframework.stereotype.Component;

/**
 * @Author wzh
 * @Date 2026/6/16 00:03
 */
@Component
public class TxtDocumentParser implements DocumentParser {

    @Override
    public boolean supports(String fileType) {
        return "TXT".equalsIgnoreCase(fileType);
    }

    @Override
    public String parse(KnowledgeDocument document, String rawContent) {
        return rawContent;
    }
}
