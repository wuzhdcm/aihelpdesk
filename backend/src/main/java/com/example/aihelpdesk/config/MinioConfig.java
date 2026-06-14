package com.example.aihelpdesk.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author wzh
 * @Date 2026/6/14 19:01
 */
@Configuration
@ConditionalOnProperty(name = "app.storage.type", havingValue = "minio")
public class MinioConfig {

    @Bean
    public MinioClient minioClient(
            @Value("${app.storage.minio.endpoint}") String endpoint,
            @Value("${app.storage.minio.access-key}") String accessKey,
            @Value("${app.storage.minio.secret-key}") String secretKey,
            @Value("${app.storage.minio.secure:false}") boolean secure) {
        // 核心思路：
        // 1. 这里专门负责创建 MinioClient，不掺杂业务逻辑。
        // 2. 业务层只关心“怎么存、怎么读”，不关心 client 怎么初始化。
        // 3. 后续替换成其他对象存储时，只改这个配置层。
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
