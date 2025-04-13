package com.qingming.jobserver.common;

import com.qingming.jobserver.exception.BusinessException;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * MinIO 操作工具类
 * 提供文件上传、下载、删除等操作
 */
@Component
public class MinioUtil {
    private static final Logger log = LoggerFactory.getLogger(MinioUtil.class);

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * 允许的内容类型白名单
     */
    private static final Set<String> ALLOWED_CONTENT_TYPES = new HashSet<>(Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp",
            "application/pdf",
            "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "text/plain"
    ));

    private final Tika tika = new Tika();

    /**
     * 上传文件
     *
     * @param file 文件
     * @param directory 目录
     * @return 文件访问路径
     */
    public String uploadFile(MultipartFile file, String directory) {
        try {
            if (file.isEmpty()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件不能为空");
            }
            
            // 检查文件类型
            String contentType = detectContentType(file.getInputStream());
            if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "不支持的文件类型: " + contentType);
            }
            
            // 构建文件路径
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String objectName = generateObjectName(directory, fileExtension);
            
            // 上传文件到 MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            
            log.info("文件上传成功: {}", objectName);
            return objectName;
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 上传Base64编码的图片
     *
     * @param base64Image Base64编码的图片（不包含前缀）
     * @param directory 目录
     * @return 文件访问路径
     */
    public String uploadBase64Image(String base64Image, String directory) {
        try {
            // 解码Base64内容
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            
            // 检查文件类型
            String contentType = detectContentType(inputStream);
            if (!contentType.startsWith("image/")) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "不是有效的图片格式");
            }
            
            // 重置输入流
            inputStream.reset();
            
            // 构建文件路径
            String fileExtension = contentType.replace("image/", ".");
            if (fileExtension.equals(".jpeg")) {
                fileExtension = ".jpg";
            }
            
            String objectName = generateObjectName(directory, fileExtension);
            
            // 上传文件到 MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, imageBytes.length, -1)
                            .contentType(contentType)
                            .build()
            );
            
            log.info("Base64图片上传成功: {}", objectName);
            return objectName;
        } catch (Exception e) {
            log.error("Base64图片上传失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "图片上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取文件预签名URL（用于前端直接访问）
     *
     * @param objectName 对象名称
     * @param expiry 过期时间（秒）
     * @return 预签名URL
     */
    public String getPresignedUrl(String objectName, int expiry) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .method(Method.GET)
                            .expiry(expiry, TimeUnit.SECONDS)
                            .build()
            );
        } catch (Exception e) {
            log.error("获取文件预签名URL失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "获取文件访问路径失败");
        }
    }

    /**
     * 删除单个文件
     *
     * @param objectName 对象名称
     */
    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            log.info("文件删除成功: {}", objectName);
        } catch (Exception e) {
            log.error("文件删除失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件删除失败");
        }
    }
    
    /**
     * 批量删除文件
     *
     * @param objectNames 对象名称列表
     * @return 删除失败的文件列表
     */
    public List<String> deleteFiles(List<String> objectNames) {
        List<DeleteObject> objects = objectNames.stream()
                .map(DeleteObject::new)
                .toList();
        
        List<String> failedFiles = new ArrayList<>();
        try {
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(
                    RemoveObjectsArgs.builder()
                            .bucket(bucketName)
                            .objects(objects)
                            .build()
            );
            
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                failedFiles.add(error.objectName());
                log.error("删除文件失败: {}, 错误信息: {}", error.objectName(), error.message());
            }
        } catch (Exception e) {
            log.error("批量删除文件失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "批量删除文件失败");
        }
        
        return failedFiles;
    }

    /**
     * 检查文件是否存在
     *
     * @param objectName 对象名称
     * @return 是否存在
     */
    public boolean isFileExist(String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 下载文件
     *
     * @param objectName 对象名称
     * @return 输入流
     */
    public InputStream downloadFile(String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.error("文件下载失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件下载失败");
        }
    }
    
    /**
     * 检测文件的实际内容类型
     *
     * @param inputStream 输入流
     * @return 内容类型
     * @throws IOException 如果发生I/O错误
     */
    private String detectContentType(InputStream inputStream) throws IOException {
        return tika.detect(inputStream);
    }
    
    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return 扩展名（带点）
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return ".bin"; // 默认二进制文件扩展名
        }
        return filename.substring(filename.lastIndexOf("."));
    }
    
    /**
     * 生成对象名称
     *
     * @param directory 目录
     * @param fileExtension 文件扩展名（带点）
     * @return 对象名称
     */
    private String generateObjectName(String directory, String fileExtension) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String path = directory;
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path + uuid + fileExtension;
    }
}
