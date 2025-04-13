package com.qingming.jobserver.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO 配置类
 * 用于初始化 MinIO 客户端和存储桶
 */
@Configuration
public class MinioConfig {
    private static final Logger log = LoggerFactory.getLogger(MinioConfig.class);

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucketName}")
    private String bucketName;
    
    private final MinioClient minioClient;
    
    public MinioConfig() {
        this.minioClient = null; // 将在afterPropertiesSet中初始化
    }

    /**
     * 初始化 MinIO 客户端
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    /**
     * 在应用启动时检查 bucket 是否存在，不存在则创建
     */
    @PostConstruct
    public void init() {
        try {
            // 使用Bean方法创建一个临时客户端，而不是注入的Bean
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
            // 检查 bucket 是否已经存在
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());
            
            if (!bucketExists) {
                // 创建 bucket
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
                log.info("Bucket '{}' 创建成功", bucketName);
            } else {
                log.info("Bucket '{}' 已存在", bucketName);
            }
        } catch (Exception e) {
            log.error("MinIO 初始化失败: {}", e.getMessage(), e);
        }
    }
}
