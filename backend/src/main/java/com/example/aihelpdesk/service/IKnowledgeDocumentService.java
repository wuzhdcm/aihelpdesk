package com.example.aihelpdesk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.aihelpdesk.model.entity.KnowledgeDocument;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author wzh
 * @Date 2026/6/5 02:26
 */
public interface IKnowledgeDocumentService extends IService<KnowledgeDocument> {

    KnowledgeDocument uploadDocument(Long knowledgeBaseId, MultipartFile file);

    List<KnowledgeDocument> listDocuments(Long knowledgeBaseId);
}
