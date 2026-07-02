package com.example.aihelpdesk.controller;

import com.example.aihelpdesk.common.Result;
import com.example.aihelpdesk.model.dto.AskResponse;
import com.example.aihelpdesk.model.dto.CreateChatSessionRequest;
import com.example.aihelpdesk.model.dto.SendChatMessageRequest;
import com.example.aihelpdesk.model.entity.ChatMessage;
import com.example.aihelpdesk.model.entity.ChatSession;
import com.example.aihelpdesk.service.ChatSessionBizService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author wzh
 * @Date 2026/7/2 18:41
 */
@RestController
@RequestMapping("/api/chat/sessions")
public class ChatController {

    private final ChatSessionBizService chatSessionBizService;

    public ChatController(ChatSessionBizService chatSessionBizService) {
        this.chatSessionBizService = chatSessionBizService;
    }

    @PostMapping
    public Result<ChatSession> createSession(@RequestBody CreateChatSessionRequest request) {
        return Result.success(chatSessionBizService.createSession(request));
    }

    @PostMapping("/{sessionId}/messages")
    public Result<AskResponse> sendMessage(@PathVariable Long sessionId,
                                           @RequestBody SendChatMessageRequest request) {
        return Result.success(chatSessionBizService.sendMessage(sessionId, request));
    }

    @GetMapping("/{sessionId}/messages")
    public Result<List<ChatMessage>> listMessages(@PathVariable Long sessionId) {
        return Result.success(chatSessionBizService.listMessages(sessionId));
    }
}
