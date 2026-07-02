package com.example.aihelpdesk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.aihelpdesk.mapper.ChatMessageMapper;
import com.example.aihelpdesk.model.entity.ChatMessage;
import com.example.aihelpdesk.service.IChatMessageService;
import org.springframework.stereotype.Service;

/**
 * @Author wzh
 * @Date 2026/7/2 18:31
 */
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage>
        implements IChatMessageService {
}
