package com.example.aihelpdesk.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author wzh
 * @Date 2026/7/1 16:53
 */
@Getter
@Setter
public class AskResponse {

    private String answer;

    private List<CitationResult> citations;
}
