package com.example.aihelpdesk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.aihelpdesk.common.CurrentUser;
import com.example.aihelpdesk.common.CurrentUserContext;
import com.example.aihelpdesk.mapper.KnowledgeDocumentMapper;
import com.example.aihelpdesk.model.entity.KnowledgeBase;
import com.example.aihelpdesk.model.entity.KnowledgeDocument;
import com.example.aihelpdesk.service.IKnowledgeBaseService;
import com.example.aihelpdesk.service.IKnowledgeDocumentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @Author wzh
 * @Date 2026/6/5 02:26
 */
@Service
public class IKnowledgeDocumentServiceImpl extends ServiceImpl<KnowledgeDocumentMapper,KnowledgeDocument> implements IKnowledgeDocumentService {
    private static final String PARSE_STATUS_UPLOADED = "UPLOADED";

    private final IKnowledgeBaseService knowledgeBaseService;
    private final Path documentRootDir;

    public IKnowledgeDocumentServiceImpl( IKnowledgeBaseService knowledgeBaseService,@Value("${app.upload.document-dir:uploads/documents}") Path documentRootDir) {
        this.knowledgeBaseService = knowledgeBaseService;
        this.documentRootDir = documentRootDir;
    }

    @Override
    public KnowledgeDocument uploadDocument(Long knowledgeBaseId, MultipartFile file) {
        CurrentUser currentUser = CurrentUserContext.getRequired();

        if ( file == null ||  file.isEmpty() ) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        checkKnowledgeBaseOwner(knowledgeBaseId,currentUser.id());

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (!StringUtils.hasText(fileName)) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        try{
            String fileType = getFileType(fileName);
            String storedFileName = UUID.randomUUID() + "_" + fileName;

            Path targetDir  = documentRootDir.resolve(String.valueOf(knowledgeBaseId));
            Files.createDirectories(targetDir);

            Path targetPath =  targetDir.resolve(storedFileName);
            file.transferTo(targetPath);

            LocalDateTime now = LocalDateTime.now();

            KnowledgeDocument document = new KnowledgeDocument();
            document.setKnowledgeBaseId(knowledgeBaseId);
            document.setFileName(fileName);
            document.setFileType(fileType);
            document.setFileSize(file.getSize());
            document.setStoragePath(targetPath.toString());
            document.setParseStatus(PARSE_STATUS_UPLOADED);
            document.setErrorMessage(null);
            document.setCreatedBy(currentUser.id());
            document.setCreateTime(now);
            document.setUpdateTime(now);
            document.setDeleted(false);

            save(document);

            return document;
        }catch (Exception ex){
            throw new IllegalArgumentException("文档上传失败");
        }


    }

    @Override
    public List<KnowledgeDocument> listDocuments(Long knowledgeBaseId) {
        CurrentUser currentUser =  CurrentUserContext.getRequired();

        checkKnowledgeBaseOwner(knowledgeBaseId,currentUser.id());

        return lambdaQuery().eq(KnowledgeDocument::getKnowledgeBaseId,knowledgeBaseId)
                .orderByAsc(KnowledgeDocument::getCreateTime)
                .list();
    }

    private void checkKnowledgeBaseOwner(Long knowledgeBaseId, Long currentUserId) {
        KnowledgeBase knowledgeBase = knowledgeBaseService.getById(knowledgeBaseId);
        if (knowledgeBase == null || !currentUserId.equals(knowledgeBase.getOwnerId())) {
            throw new IllegalArgumentException("知识库不存在或无权访问");
        }
    }

    private String getFileType(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index < 0 || index == fileName.length() - 1) {
            return "UNKNOWN";
        }
        return fileName.substring(index + 1).toUpperCase();
    }
}
