package com.example.aihelpdesk.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author wzh
 * @Date 2026/6/2 01:54
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}

