package com.example.aihelpdesk.service;

import com.example.aihelpdesk.model.dto.AskRequest;
import com.example.aihelpdesk.model.dto.AskResponse;

/**
 * @Author wzh
 * @Date 2026/7/1 17:16
 */
public interface RagAskService {

    AskResponse ask(AskRequest request);
}
