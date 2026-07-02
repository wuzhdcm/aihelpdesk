package com.example.aihelpdesk.service.impl;

import com.example.aihelpdesk.common.CurrentUser;
import com.example.aihelpdesk.common.CurrentUserContext;
import com.example.aihelpdesk.model.dto.AskRequest;
import com.example.aihelpdesk.model.dto.AskResponse;
import com.example.aihelpdesk.model.dto.CreateChatSessionRequest;
import com.example.aihelpdesk.model.dto.SendChatMessageRequest;
import com.example.aihelpdesk.model.entity.ChatCitation;
import com.example.aihelpdesk.model.entity.ChatMessage;
import com.example.aihelpdesk.model.entity.ChatSession;
import com.example.aihelpdesk.model.entity.KnowledgeBase;
import com.example.aihelpdesk.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author wzh
 * @Date 2026/7/2 18:32
 */
@Service
public class ChatSessionBizServiceImpl implements ChatSessionBizService {
    private static final String ROLE_USER = "USER";
    private static final String ROLE_ASSISTANT = "ASSISTANT";
    private static final String MESSAGE_TYPE_CHAT = "CHAT";

    private final IChatSessionService chatSessionService;
    private final IChatMessageService chatMessageService;
    private final IChatCitationService chatCitationService;
    private final IKnowledgeBaseService knowledgeBaseService;
    private final RagAskService ragAskService;

    public ChatSessionBizServiceImpl(IChatSessionService chatSessionService,
                                     IChatMessageService chatMessageService,
                                     IChatCitationService chatCitationService,
                                     IKnowledgeBaseService knowledgeBaseService,
                                     RagAskService ragAskService) {
        this.chatSessionService = chatSessionService;
        this.chatMessageService = chatMessageService;
        this.chatCitationService = chatCitationService;
        this.knowledgeBaseService = knowledgeBaseService;
        this.ragAskService = ragAskService;


    }

    @Override
    public ChatSession createSession(CreateChatSessionRequest request) {
        CurrentUser currentUser = CurrentUserContext.getRequired();

        if (request == null || request.getKnowledgeBaseId() == null) {
            throw new IllegalArgumentException("knowledgeBaseId 不能为空");
        }
        checkKnowledgeBaseOwner(request.getKnowledgeBaseId(), currentUser.id());

        LocalDateTime now = LocalDateTime.now();

        ChatSession session = new ChatSession();
        session.setUserId(currentUser.id());
        session.setKnowledgeBaseId(request.getKnowledgeBaseId());
        session.setTitle(StringUtils.hasText(request.getTitle()) ? request.getTitle() : "新的知识库问答");
        session.setCreateTime(now);
        session.setUpdateTime(now);
        session.setDeleted(false);

        chatSessionService.save(session);
        return session;
    }

    @Override
    @Transactional
    public AskResponse sendMessage(Long sessionId, SendChatMessageRequest request) {
        CurrentUser currentUser = CurrentUserContext.getRequired();

        // 核心思路：
        // 1. 发送消息前先校验 session 是否属于当前用户，避免用户往别人的会话里写消息。
        // 2. 先保存 USER 消息，再调用 RagAskService 生成回答，保证用户提问可追溯。
        // 3. 模型回答保存为 ASSISTANT 消息，citations 绑定到这条回答消息上。
        // 4. 这一步先做非流式落库闭环，后续 SSE 只是在返回方式上升级，不改变存储模型。
        if (request == null || !StringUtils.hasText(request.getContent())) {
            throw new IllegalArgumentException("content 不能为空");
        }

        ChatSession session = getOwnedSession(sessionId, currentUser.id());
        LocalDateTime now = LocalDateTime.now();

        ChatMessage userMessage = new ChatMessage();
        userMessage.setSessionId(session.getId());
        userMessage.setUserId(currentUser.id());
        userMessage.setRole(ROLE_USER);
        userMessage.setContent(request.getContent());
        userMessage.setMessageType(MESSAGE_TYPE_CHAT);
        userMessage.setCreateTime(now);
        chatMessageService.save(userMessage);

        AskRequest askRequest = new AskRequest();
        askRequest.setKnowledgeBaseId(session.getKnowledgeBaseId());
        askRequest.setQuestion(request.getContent());
        askRequest.setTopK(request.getTopK());

        AskResponse askResponse = ragAskService.ask(askRequest);

        ChatMessage assistantMessage = new ChatMessage();
        assistantMessage.setSessionId(session.getId());
        assistantMessage.setUserId(currentUser.id());
        assistantMessage.setRole(ROLE_ASSISTANT);
        assistantMessage.setContent(askResponse.getAnswer());
        assistantMessage.setMessageType(MESSAGE_TYPE_CHAT);
        assistantMessage.setCreateTime(LocalDateTime.now());
        chatMessageService.save(assistantMessage);

        List<ChatCitation> citations = askResponse.getCitations().stream()
                .map(citation -> {
                    ChatCitation entity = new ChatCitation();
                    entity.setMessageId(assistantMessage.getId());
                    entity.setDocumentId(citation.getDocumentId());
                    entity.setChunkId(citation.getChunkId());
                    entity.setScore(BigDecimal.valueOf(citation.getScore()));
                    entity.setContentSnapshot(citation.getContentSnapshot());
                    return entity;
                })
                .toList();

        if (!citations.isEmpty()) {
            chatCitationService.saveBatch(citations);
        }

        session.setUpdateTime(LocalDateTime.now());
        chatSessionService.updateById(session);

        return askResponse;
    }

    @Override
    public List<ChatMessage> listMessages(Long sessionId) {
        CurrentUser currentUser = CurrentUserContext.getRequired();

        getOwnedSession(sessionId, currentUser.id());

        return chatMessageService.lambdaQuery()
                .eq(ChatMessage::getSessionId, sessionId)
                .orderByAsc(ChatMessage::getCreateTime)
                .list();
    }

    private ChatSession getOwnedSession(Long sessionId, Long currentUserId) {
        if (sessionId == null) {
            throw new IllegalArgumentException("sessionId 不能为空");
        }

        ChatSession session = chatSessionService.getById(sessionId);
        if (session == null || !currentUserId.equals(session.getUserId())) {
            throw new IllegalArgumentException("会话不存在或无权访问");
        }
        return session;
    }

    private void checkKnowledgeBaseOwner(Long knowledgeBaseId, Long currentUserId) {
        KnowledgeBase knowledgeBase = knowledgeBaseService.getById(knowledgeBaseId);
        if (knowledgeBase == null || !currentUserId.equals(knowledgeBase.getOwnerId())) {
            throw new IllegalArgumentException("知识库不存在或无权访问");
        }
    }
}
