package com.example.aihelpdesk.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author wzh
 * @Date 2026/6/5 02:24
 */
@Getter
@Setter
@ToString
@TableName("knowledge_document")
public class KnowledgeDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("knowledge_base_id")
    private Long knowledgeBaseId;

    @TableField("file_name")
    private String fileName;

    @TableField("file_type")
    private String fileType;

    @TableField("file_size")
    private Long fileSize;

    @TableField("storage_path")
    private String storagePath;

    @TableField("parse_status")
    private String parseStatus;

    @TableField("error_message")
    private String errorMessage;

    @TableField("created_by")
    private Long createdBy;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Boolean deleted;
}
