# 基于 Spring Boot 的企业级 AI Agent 知识库与工单助手

本项目定位为面向企业服务台场景的 Java AI Agent 业务系统。目标不是做一个简单聊天机器人，而是从 0 搭建一个接近真实企业开发流程的系统，覆盖知识库管理、文档解析、RAG 问答、工单流转、Agent 工具调用、权限控制、日志审计和部署交付。

## 项目定位

企业内部经常存在大量重复咨询和服务请求，例如制度咨询、账号权限申请、系统使用问题、IT 报障、工单进度查询等。本系统通过 AI Agent 和 RAG 知识库，把企业文档、服务台工单和业务工具整合到一个统一入口。

核心业务闭环：

```text
员工提问
-> AI 判断意图
-> 知识类问题走 RAG 检索企业文档
-> 工单类问题调用工单工具查询、创建或更新
-> 无法解决的问题转人工或创建工单
-> 管理员维护知识库、文档、模型配置和工单分类
```

## 推荐技术栈

后端：

```text
Java 21
Spring Boot 3.x
Spring Security 或 Sa-Token
MyBatis-Plus
PostgreSQL + pgvector
Redis
MinIO
LangChain4j 或 Spring AI
RabbitMQ 可选
Docker Compose
```

前端：

```text
Vue 3
Vite
TypeScript
Element Plus
Pinia
Axios
SSE 流式响应
Markdown 渲染
```

模型与 AI 能力：

```text
OpenAI API
DeepSeek
通义千问 / 百炼
Ollama 本地模型
Embedding 模型
RAG 检索增强生成
Tool Calling / Agent 工具调用
```

## 文档目录

```text
docs/
  00-项目开发流程说明书.md
  01-需求规格说明书.md
  02-系统架构设计.md
  03-数据库设计.md
  04-接口设计.md
  05-开发计划与里程碑.md
  06-简历与面试表达.md
```

## 开发原则

1. 先完成企业业务系统，再接入 AI 能力。
2. 先跑通最小业务闭环，再做复杂 Agent 编排。
3. 所有 Agent 工具调用必须经过后端业务服务和权限校验。
4. 文档解析、向量化、模型调用等耗时任务必须有状态追踪和异常处理。
5. 项目最终要做到可运行、可演示、可部署、可解释。

## MVP 范围

第一版建议完成：

```text
登录认证
用户与角色
知识库创建
文档上传
文档解析
文档切片
向量化入库
知识库问答
SSE 流式回答
工单创建
工单查询
工单状态流转
Agent 调用查询工单工具
问答记录
后台管理页面
Docker Compose 启动
```

## 推荐开发顺序

```text
1. 项目脚手架
2. 登录认证和权限
3. 工单基础 CRUD
4. 工单状态流转
5. 文件上传和 MinIO
6. 知识库和文档管理
7. 文档解析与切片
8. 向量化和向量检索
9. RAG 问答
10. SSE 流式响应
11. Agent 工具调用
12. 后台管理页面
13. 日志、限流、部署
```

