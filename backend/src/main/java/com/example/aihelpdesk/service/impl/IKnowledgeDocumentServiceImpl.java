package com.example.aihelpdesk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.aihelpdesk.common.CurrentUser;
import com.example.aihelpdesk.common.CurrentUserContext;
import com.example.aihelpdesk.mapper.KnowledgeDocumentMapper;
import com.example.aihelpdesk.model.entity.DocumentChunk;
import com.example.aihelpdesk.model.entity.EmbeddingTask;
import com.example.aihelpdesk.model.entity.KnowledgeBase;
import com.example.aihelpdesk.model.entity.KnowledgeDocument;
import com.example.aihelpdesk.service.IDocumentChunkService;
import com.example.aihelpdesk.service.IEmbeddingTaskService;
import com.example.aihelpdesk.service.IKnowledgeBaseService;
import com.example.aihelpdesk.service.IKnowledgeDocumentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author wzh
 * @Date 2026/6/5 02:26
 */
@Service
public class IKnowledgeDocumentServiceImpl extends ServiceImpl<KnowledgeDocumentMapper,KnowledgeDocument> implements IKnowledgeDocumentService {
    private static final String PARSE_STATUS_UPLOADED = "UPLOADED";
    private static final String PARSE_STATUS_PARSING = "PARSING";
    private static final String PARSE_STATUS_PARSED = "PARSED";
    private static final String PARSE_STATUS_FAILED = "FAILED";
    private static final int CHUNK_SIZE = 500;

    private static final String TASK_TYPE_PARSE = "PARSE";
    private static final String TASK_STATUS_PENDING = "PENDING";
    private static final String TASK_STATUS_RUNNING = "RUNNING";
    private static final String TASK_STATUS_SUCCESS = "SUCCESS";
    private static final String TASK_STATUS_FAILED = "FAILED";

    private final IEmbeddingTaskService embeddingTaskService;

    private final IDocumentChunkService documentChunkService;

    private final IKnowledgeBaseService knowledgeBaseService;
    private final Path documentRootDir;

    public IKnowledgeDocumentServiceImpl(IEmbeddingTaskService embeddingTaskService, IDocumentChunkService documentChunkService, IKnowledgeBaseService knowledgeBaseService, @Value("${app.upload.document-dir:uploads/documents}") Path documentRootDir) {
        this.embeddingTaskService = embeddingTaskService;
        this.documentChunkService = documentChunkService;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KnowledgeDocument parseDocument(Long documentId) {
        CurrentUser currentUser =  CurrentUserContext.getRequired();
        //1. 先查文档记录
        KnowledgeDocument document = getById(documentId);
        if (document == null) {
            throw new IllegalArgumentException("文档不存在");
        }
        // 2. 通过文档的 knowledgeBaseId 校验当前用户是否拥有该知识库
        checkKnowledgeBaseOwner(document.getKnowledgeBaseId(),currentUser.id());

        EmbeddingTask task = createParseTask(document, currentUser.id());

        // 3. 当前最小闭环只支持 TXT，先不要混入 PDF/DOCX

        if (!"TXT".equalsIgnoreCase(document.getFileType())) {
            throw new IllegalArgumentException("当前阶段只支持 TXT 文档解析");
        }
        try {
            // 核心思路：
            // 1. 先把任务置为 RUNNING，让任务表能反映“正在处理”。
            // 2. 再把文档状态置为 PARSING，表示文档级别的处理也开始了。
            // 3. 后面每一步失败，都回写 task 和 document，方便排查。
            task.setTaskStatus(TASK_STATUS_RUNNING);
            task.setStartedTime(LocalDateTime.now());
            task.setUpdateTime(LocalDateTime.now());
            embeddingTaskService.updateById(task);

            // 4. 状态先改成 PARSING，表示开始处理
            document.setParseStatus(PARSE_STATUS_PARSING);
            document.setErrorMessage(null);
            document.setUpdateTime(LocalDateTime.now());
            updateById(document);

            // 5. 从 storagePath 读取本地文件内容
            String content = Files.readString(Path.of(document.getStoragePath()));
            if (!StringUtils.hasText(content)) {
                throw new IllegalArgumentException("文档内容为空");
            }
            // 6. 重复解析时，先删除旧分片，避免同一文档重复生成 chunk
            documentChunkService.lambdaUpdate().eq(DocumentChunk::getDocumentId, document.getId()).remove();
            // 7. 按固定长度切片，生成 document_chunk 记录
            List<DocumentChunk> chunks = buildChunks(document, content);
            documentChunkService.saveBatch(chunks);

            task.setTaskStatus(TASK_STATUS_SUCCESS);
            task.setTotalChunks(chunks.size());
            task.setSuccessChunks(chunks.size());
            task.setFailReason(null);
            task.setFinishedTime(LocalDateTime.now());
            task.setUpdateTime(LocalDateTime.now());
            embeddingTaskService.updateById(task);

            // 8. 解析成功后，状态改成 PARSED
            document.setParseStatus(PARSE_STATUS_PARSED);
            document.setErrorMessage(null);
            document.setUpdateTime(LocalDateTime.now());
            updateById(document);
            return document;

        }catch (Exception ex){
            task.setTaskStatus(TASK_STATUS_FAILED);
            task.setFailReason(ex.getMessage());
            task.setFinishedTime(LocalDateTime.now());
            task.setUpdateTime(LocalDateTime.now());
            embeddingTaskService.updateById(task);

            // 9. 解析失败要记录状态和错误原因，方便排查
            document.setParseStatus(PARSE_STATUS_FAILED);
            document.setErrorMessage(ex.getMessage());
            document.setUpdateTime(LocalDateTime.now());
            updateById(document);

            throw new IllegalArgumentException("文档解析失败：" + ex.getMessage());

        }
    }

    @Override
    public List<DocumentChunk> listDocumentChunks(Long documentId) {
        CurrentUser  currentUser =  CurrentUserContext.getRequired();
        // 核心思路：
        // 1. 查询分片前必须先查 document，因为权限边界在 document 所属的 knowledgeBase 上。
        // 2. Controller 不接收 userId，避免前端伪造用户身份。
        // 3. 只有确认当前用户拥有该知识库后，才允许查询 document_chunk。
        // 4. 返回时按 chunk_index 排序，保证前端看到的文本顺序和原文一致。
        KnowledgeDocument document = getById(documentId);
        if (document == null) {
            throw new IllegalArgumentException("文档不存在");
        }

        checkKnowledgeBaseOwner(document.getKnowledgeBaseId(),currentUser.id());

        return  documentChunkService.lambdaQuery().eq(DocumentChunk::getDocumentId,documentId)
                .orderByAsc(DocumentChunk::getChunkIndex)
                .list();
    }

    @Override
    public List<EmbeddingTask> listDocumentTasks(Long documentId) {
        CurrentUser currentUser =  CurrentUserContext.getRequired();
        // 核心思路：
        // 1. 查询任务前先查 document，权限边界仍然在 knowledgeBase 上。
        // 2. Controller 不接收 userId，避免前端伪造身份。
        // 3. 只有确认当前用户有权访问该知识库，才允许看任务记录。
        KnowledgeDocument document = getById(documentId);
        if (document == null) {
            throw new IllegalArgumentException("文档不存在");
        }
        checkKnowledgeBaseOwner(document.getKnowledgeBaseId(),currentUser.id());


        return embeddingTaskService.lambdaQuery()
                .eq(EmbeddingTask::getDocumentId,document.getId())
                .orderByDesc(EmbeddingTask::getCreateTime)
                .list();
    }


    private List<DocumentChunk> buildChunks(KnowledgeDocument document, String content) {
        List<DocumentChunk> documentChunks = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        int chunkIndex= 0;

        // 固定每 500 个字符切一段；当前先简单可验证，后面再优化为 token/语义切分
        for (int start = 0; start < content.length()    ; start+=CHUNK_SIZE) {
            int end = Math.min(start + CHUNK_SIZE, content.length());
            String chunkContent = content.substring(start, end);

            DocumentChunk chunk = new DocumentChunk();
            chunk.setContent(chunkContent);
            chunk.setDocumentId(document.getId());
            chunk.setChunkIndex(chunkIndex++);
            chunk.setKnowledgeBaseId(document.getKnowledgeBaseId());

            // 当前先用字符数粗略估算 token 数，后续接模型 tokenizer 再优化
            chunk.setTokenCount(chunkContent.length());

            // 还没向量化，所以 vectorId 先为空
            chunk.setVectorId(null);

            //metadata 先放空 JSON，后续可写页码、标题、文件名等信息
            chunk.setMetadata("{}");
            chunk.setCreateTime(now);

            documentChunks.add(chunk);
        }
        return documentChunks;
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

    // 核心思路：
// 1. 文档解析不是“只改文档状态”，还要生成一条可追踪的任务记录。
// 2. 任务记录放在 Service 层创建，因为它和权限校验、文档状态流转是同一个业务闭环。
// 3. 先落一条 PENDING 任务，后面再根据解析过程更新 RUNNING / SUCCESS / FAILED。
// 4. 这样用户查任务时，能看到任务在哪一步卡住，失败原因也能回放。
    private EmbeddingTask createParseTask(KnowledgeDocument document, Long userId) {
        LocalDateTime  now = LocalDateTime.now();
        EmbeddingTask task = new EmbeddingTask();

        task.setDocumentId(document.getId());
        task.setKnowledgeBaseId(document.getKnowledgeBaseId());
        task.setTaskType(TASK_TYPE_PARSE);
        task.setTaskStatus(TASK_STATUS_PENDING);
        task.setTotalChunks(0);
        task.setSuccessChunks(0);
        task.setFailReason(null);
        task.setStartedTime(null);
        task.setFinishedTime(null);
        task.setCreatedBy(userId);
        task.setCreateTime(now);
        task.setUpdateTime(now);
        task.setDeleted(false);

        embeddingTaskService.save(task);
        return task;
    }
}
