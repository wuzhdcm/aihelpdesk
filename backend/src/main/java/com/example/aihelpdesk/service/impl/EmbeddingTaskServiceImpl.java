package com.example.aihelpdesk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.aihelpdesk.mapper.EmbeddingTaskMapper;
import com.example.aihelpdesk.model.entity.EmbeddingTask;
import com.example.aihelpdesk.service.IEmbeddingTaskService;
import org.springframework.stereotype.Service;

/**
 * @Author wzh
 * @Date 2026/6/14 18:05
 */
@Service
public class EmbeddingTaskServiceImpl extends ServiceImpl<EmbeddingTaskMapper, EmbeddingTask> implements IEmbeddingTaskService {
}
