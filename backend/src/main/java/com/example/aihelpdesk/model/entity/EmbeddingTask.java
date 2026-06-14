package com.example.aihelpdesk.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author wzh
 * @Date 2026/6/14 18:04
 */
@Getter
@Setter
@ToString
@TableName("embedding_task")
public class EmbeddingTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("document_id")
    private Long documentId;

    @TableField("knowledge_base_id")
    private Long knowledgeBaseId;

    @TableField("task_type")
    private String taskType;

    @TableField("task_status")
    private String taskStatus;

    @TableField("total_chunks")
    private Integer totalChunks;

    @TableField("success_chunks")
    private Integer successChunks;

    @TableField("fail_reason")
    private String failReason;

    @TableField("started_time")
    private LocalDateTime startedTime;

    @TableField("finished_time")
    private LocalDateTime finishedTime;

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
