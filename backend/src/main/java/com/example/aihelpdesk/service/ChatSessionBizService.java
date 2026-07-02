package com.example.aihelpdesk.service;

import com.example.aihelpdesk.model.dto.AskResponse;
import com.example.aihelpdesk.model.dto.CreateChatSessionRequest;
import com.example.aihelpdesk.model.dto.SendChatMessageRequest;
import com.example.aihelpdesk.model.entity.ChatMessage;
import com.example.aihelpdesk.model.entity.ChatSession;

import java.util.List;

/**
 * @Author wzh
 * @Date 2026/7/2 18:32
 */
public interface ChatSessionBizService {

    ChatSession createSession(CreateChatSessionRequest request);

    AskResponse sendMessage(Long sessionId, SendChatMessageRequest request);

    List<ChatMessage> listMessages(Long sessionId);
}
