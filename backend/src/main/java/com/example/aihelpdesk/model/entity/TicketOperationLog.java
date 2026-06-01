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
 * <p>
 * 工单操作日志表
 * </p>
 *
 * @author wuzh
 * @since 2026-06-02
 */
@Getter
@Setter
@ToString
@TableName("ticket_operation_log")
public class TicketOperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工单ID
     */
    @TableField("ticket_id")
    private Long ticketId;

    /**
     * 操作人
     */
    @TableField("operator_id")
    private Long operatorId;

    /**
     * 操作类型
     */
    @TableField("operation_type")
    private String operationType;

    /**
     * 操作前状态
     */
    @TableField("before_status")
    private String beforeStatus;

    /**
     * 操作后状态
     */
    @TableField("after_status")
    private String afterStatus;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
