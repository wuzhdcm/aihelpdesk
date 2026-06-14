package com.example.aihelpdesk.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author wzh
 * @Date 2026/6/14 18:40
 */
public interface StorageService {

    String saveDocument(Long knowledgeBaseId, String fileName, MultipartFile file);

    String readText(String storagePath);
}
