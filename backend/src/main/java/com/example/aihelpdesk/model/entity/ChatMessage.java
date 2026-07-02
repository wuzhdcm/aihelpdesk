package com.example.aihelpdesk.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @Author wzh
 * @Date 2026/7/2 18:28
 */
@Getter
@Setter
@ToString
@TableName("chat_message")
public class ChatMessage {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("session_id")
    private Long sessionId;

    @TableField("user_id")
    private Long userId;

    @TableField("role")
    private String role;

    @TableField("content")
    private String content;

    @TableField("message_type")
    private String messageType;

    @TableField("create_time")
    private LocalDateTime createTime;
}
