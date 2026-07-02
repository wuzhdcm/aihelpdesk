package com.example.aihelpdesk.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @Author wzh
 * @Date 2026/7/2 18:27
 */
@Getter
@Setter
@ToString
@TableName("chat_session")
public class ChatSession {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("title")
    private String title;

    @TableField("knowledge_base_id")
    private Long knowledgeBaseId;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Boolean deleted;
}
