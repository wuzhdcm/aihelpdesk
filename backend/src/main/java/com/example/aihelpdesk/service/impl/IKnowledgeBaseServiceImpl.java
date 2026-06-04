package com.example.aihelpdesk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.aihelpdesk.common.CurrentUser;
import com.example.aihelpdesk.common.CurrentUserContext;
import com.example.aihelpdesk.mapper.KnowledgeBaseMapper;
import com.example.aihelpdesk.model.dto.CreateKnowledgeBaseRequest;
import com.example.aihelpdesk.model.entity.KnowledgeBase;
import com.example.aihelpdesk.service.IKnowledgeBaseService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author wzh
 * @Date 2026/6/5 01:49
 */
@Service
public class IKnowledgeBaseServiceImpl extends ServiceImpl<KnowledgeBaseMapper, KnowledgeBase> implements IKnowledgeBaseService {

    private static final String STATUS_ENABLED = "ENABLED";

    @Override
    public KnowledgeBase createKnowledgeBase(CreateKnowledgeBaseRequest request) {

        CurrentUser currentUser = CurrentUserContext.getRequired();

        LocalDateTime now = LocalDateTime.now();

        KnowledgeBase knowledgeBase = new KnowledgeBase();
        knowledgeBase.setCreateTime(now);
        knowledgeBase.setUpdateTime(now);
        knowledgeBase.setStatus(STATUS_ENABLED);
        knowledgeBase.setName(request.name());
        knowledgeBase.setDescription(request.description());
        knowledgeBase.setOwnerId(currentUser.id());
        knowledgeBase.setVisibility(request.visibility());
        knowledgeBase.setDeleted(false);

        save(knowledgeBase);
        return knowledgeBase;
    }

    @Override
    public List<KnowledgeBase> listMyKnowledgeBases() {
        CurrentUser currentUser = CurrentUserContext.getRequired();
        return lambdaQuery()
                .eq(KnowledgeBase::getId, currentUser.id())
                .orderByDesc(KnowledgeBase::getUpdateTime)
                .list();
    }
}
