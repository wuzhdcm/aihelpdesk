package com.example.aihelpdesk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.aihelpdesk.model.dto.CreateKnowledgeBaseRequest;
import com.example.aihelpdesk.model.entity.KnowledgeBase;

import java.util.List;

/**
 * @Author wzh
 * @Date 2026/6/5 01:48
 */
public interface IKnowledgeBaseService extends IService<KnowledgeBase> {

    KnowledgeBase createKnowledgeBase(CreateKnowledgeBaseRequest request);

    List<KnowledgeBase> listMyKnowledgeBases();
}
