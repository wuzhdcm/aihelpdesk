package com.example.aihelpdesk.service.impl;

import com.example.aihelpdesk.service.StorageService;
import io.minio.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @Author wzh
 * @Date 2026/6/14 19:05
 */
@Service
@ConditionalOnProperty(name = "app.storage.type", havingValue = "minio")
public class MinioStorageServiceImpl implements StorageService{

    private final MinioClient minioClient;
    private final String bucketName;

    public MinioStorageServiceImpl(MinioClient minioClient, @Value("${app.storage.minio.bucket}") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    @PostConstruct
    public void initBucket() {
        // 核心思路：
        // 1. MinIO bucket 是对象存储的基础边界，先确保 bucket 存在。
        // 2. 这一步放在存储适配层，不放在文档业务 Service。
        // 3. 失败时直接暴露配置或权限问题，排查会更直接。
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception ex) {
            throw new IllegalStateException("MinIO bucket 初始化失败: " + ex.getMessage(), ex);
        }
    }


    @Override
    public String saveDocument(Long knowledgeBaseId, String fileName, MultipartFile file) {
        // 核心思路：
        // 1. 这里只负责对象存储，不做权限判断。
        // 2. 对象 key 按知识库隔离，方便后续排查和清理。
        // 3. 返回 bucket/objectKey，业务层只保存这个路径即可。
        String objectKey = knowledgeBaseId + "/" + UUID.randomUUID() + "_" + fileName;

        try {
            String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectKey)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(contentType)
                            .build());

            return bucketName + "/" + objectKey;
        } catch (Exception ex) {
            throw new IllegalArgumentException("文件保存失败：" + ex.getMessage());
        }
    }

    @Override
    public String readText(String storagePath) {
        // 核心思路：
        // 1. 文档解析 Service 不应该知道文件来自本地磁盘还是 MinIO。
        // 2. storagePath 里保存 bucket/objectKey，读取时再拆开。
        // 3. 如果这里失败，优先查 bucket、对象 key、访问凭证和网络。
        try {
            int splitIndex = storagePath.indexOf('/');
            if (splitIndex <= 0 || splitIndex == storagePath.length() - 1) {
                throw new IllegalArgumentException("非法存储路径");
            }

            String bucket = storagePath.substring(0, splitIndex);
            String objectKey = storagePath.substring(splitIndex + 1);

            try (InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .build());
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                inputStream.transferTo(outputStream);
                return outputStream.toString(StandardCharsets.UTF_8);
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("文件读取失败：" + ex.getMessage());
        }
    }
}

