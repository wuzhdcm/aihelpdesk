package com.example.aihelpdesk.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 工单表
 * </p>
 *
 * @author wuzh
 * @since 2026-06-02
 */
@Getter
@Setter
@ToString
@TableName("ticket")
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工单编号
     */
    @TableField("ticket_no")
    private String ticketNo;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 分类
     */
    @TableField("category")
    private String category;

    /**
     * LOW / MEDIUM / HIGH / URGENT
     */
    @TableField("priority")
    private String priority;

    /**
     * PENDING / PROCESSING / WAITING_CONFIRM / RESOLVED / CLOSED
     */
    @TableField("status")
    private String status;

    /**
     * 创建人
     */
    @TableField("creator_id")
    private Long creatorId;

    /**
     * 负责人
     */
    @TableField("assignee_id")
    private Long assigneeId;

    /**
     * 是否AI创建
     */
    @TableField("ai_generated")
    private Boolean aiGenerated;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 关闭时间
     */
    @TableField("closed_time")
    private LocalDateTime closedTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField("deleted")
    private Boolean deleted;
}
