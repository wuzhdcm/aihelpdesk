package com.example.aihelpdesk.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author wzh
 * @Date 2026/7/1 13:53
 */
@Getter
@Setter
public class SearchChunkRequest {

    private String query;

    private Integer topK;
}
