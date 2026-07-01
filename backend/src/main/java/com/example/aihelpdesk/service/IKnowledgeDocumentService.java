package com.example.aihelpdesk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.aihelpdesk.model.entity.DocumentChunk;
import com.example.aihelpdesk.model.entity.EmbeddingTask;
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

    /**
     * 解析文档：
     * 1. 校验当前用户是否有权限操作该文档
     * 2. 读取本地 TXT 文件
     * 3. 生成 document_chunk
     * 4. 更新文档解析状态
     */
    KnowledgeDocument parseDocument(Long documentId);

    List<DocumentChunk> listDocumentChunks(Long documentId);

    List<EmbeddingTask> listDocumentTasks(Long documentId);

    List<DocumentChunk> embedDocument(Long documentId);
}
