package com.example.aihelpdesk.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author wzh
 * @Date 2026/6/6 01:08
 */
@Getter
@Setter
@ToString
@TableName("document_chunk")
public class DocumentChunk implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("document_id")
    private Long documentId;

    @TableField("knowledge_base_id")
    private Long knowledgeBaseId;

    @TableField("chunk_index")
    private Integer chunkIndex;

    @TableField("content")
    private String content;

    @TableField("token_count")
    private Integer tokenCount;

    @TableField("vector_id")
    private String vectorId;

    @TableField("metadata")
    private String metadata;

    @TableField("create_time")
    private LocalDateTime createTime;
}
