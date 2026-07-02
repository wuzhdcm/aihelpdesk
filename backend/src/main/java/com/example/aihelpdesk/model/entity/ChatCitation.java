package com.example.aihelpdesk.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Author wzh
 * @Date 2026/7/2 18:28
 */
@Getter
@Setter
@ToString
@TableName("chat_citation")
public class ChatCitation {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("message_id")
    private Long messageId;

    @TableField("document_id")
    private Long documentId;

    @TableField("chunk_id")
    private Long chunkId;

    @TableField("score")
    private BigDecimal score;

    @TableField("content_snapshot")
    private String contentSnapshot;
}
