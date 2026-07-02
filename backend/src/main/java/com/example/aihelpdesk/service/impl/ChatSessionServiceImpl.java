package com.example.aihelpdesk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.aihelpdesk.mapper.ChatSessionMapper;
import com.example.aihelpdesk.model.entity.ChatSession;
import com.example.aihelpdesk.service.IChatSessionService;
import org.springframework.stereotype.Service;

/**
 * @Author wzh
 * @Date 2026/7/2 18:30
 */
@Service
public class ChatSessionServiceImpl extends ServiceImpl<ChatSessionMapper, ChatSession>
        implements IChatSessionService {
}
