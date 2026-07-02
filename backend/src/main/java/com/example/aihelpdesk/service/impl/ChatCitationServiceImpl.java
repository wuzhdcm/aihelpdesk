package com.example.aihelpdesk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.aihelpdesk.mapper.ChatCitationMapper;
import com.example.aihelpdesk.model.entity.ChatCitation;
import com.example.aihelpdesk.service.IChatCitationService;
import org.springframework.stereotype.Service;

/**
 * @Author wzh
 * @Date 2026/7/2 18:31
 */
@Service
public class ChatCitationServiceImpl extends ServiceImpl<ChatCitationMapper, ChatCitation>
        implements IChatCitationService {
}
