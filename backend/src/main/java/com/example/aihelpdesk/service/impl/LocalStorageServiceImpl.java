package com.example.aihelpdesk.service.impl;

import com.example.aihelpdesk.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * @Author wzh
 * @Date 2026/6/14 18:40
 */
@Service
@ConditionalOnProperty(name = "app.storage.type", havingValue = "local", matchIfMissing = true)
public class LocalStorageServiceImpl implements StorageService {

    private final Path documentRootDir;

    public LocalStorageServiceImpl(@Value("${app.upload.document-dir:uploads/documents}")Path documentRootDir) {
        this.documentRootDir = documentRootDir;
    }

    @Override
    public String saveDocument(Long knowledgeBaseId, String fileName, MultipartFile file) {
        // 核心思路：
        // 1. StorageService 只负责“文件怎么存”，不判断用户有没有知识库权限。
        // 2. 权限、文档状态、数据库写入仍然放在 KnowledgeDocumentService。
        // 3. 返回 storagePath 给业务层保存，后续换 MinIO 时也保持这个返回约定。
        try{
            String storedFileName = UUID.randomUUID() + "_" + fileName;

            Path targetDir  = documentRootDir.resolve(String.valueOf(knowledgeBaseId));
            Files.createDirectories(targetDir);

            Path targetPath = targetDir.resolve(storedFileName);
            file.transferTo(targetPath);

            return targetPath.toString();
        }catch (Exception ex){
            throw new IllegalArgumentException("文件保存失败：" + ex.getMessage());
        }
    }

    @Override
    public String readText(String storagePath) {
        // 核心思路：
        // 1. 文档解析 Service 不直接关心文件来自本地磁盘还是 MinIO。
        // 2. 当前实现从本地路径读取文本；后续 MinIO 实现可以用同一个方法签名下载对象内容。
        // 3. 读取失败时抛出业务异常，让解析任务能记录 failReason。
        try {
            return Files.readString(Path.of(storagePath));
        } catch (Exception ex) {
            throw new IllegalArgumentException("文件读取失败：" + ex.getMessage());
        }
    }
}
