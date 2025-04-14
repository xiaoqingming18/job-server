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
    
    @Value("${minio.endpoint}")
    private String endpoint;
    
    @Value("${minio.public-ip}")
    private String publicIp;

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
     * 将内部IP替换为公网IP
     *
     * @param objectName 对象名称
     * @param expiry 过期时间（秒）
     * @return 预签名URL
     */
    public String getPresignedUrl(String objectName, int expiry) {
        try {
            // 使用预签名方式生成URL - 此URL不需要认证即可访问
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .method(Method.GET)
                            .expiry(expiry, TimeUnit.SECONDS)
                            .build()
            );
            
            log.info("Generated presigned URL before replacement: {}", url);
            
            // 替换URL中的主机地址为公网IP
            String modifiedUrl = replaceHostWithPublicIp(url);
            log.info("Modified URL with public IP: {}", modifiedUrl);
            
            return modifiedUrl;
        } catch (Exception e) {
            log.error("获取文件预签名URL失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "获取文件访问路径失败");
        }
    }
    
    /**
     * 获取简化的文件URL（只保留到文件后缀名，不包含查询参数）
     *
     * @param objectName 对象名称
     * @return 简化的文件URL
     */
    public String getSimplifiedUrl(String objectName) {
        try {
            if (objectName == null || objectName.isEmpty()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件名不能为空");
            }
            
            // 构建简化URL，格式为：http://publicIp/bucketName/objectName
            String protocol = "http";
            if (endpoint.startsWith("https")) {
                protocol = "https";
            }
            
            // 检查publicIp是否已包含协议部分
            String publicAddress;
            if (publicIp.startsWith("http://") || publicIp.startsWith("https://")) {
                // 如果已包含协议，直接使用publicIp
                publicAddress = publicIp;
            } else {
                // 如果不包含协议，添加协议部分
                publicAddress = protocol + "://" + publicIp;
            }
            
            String url = publicAddress + "/" + bucketName + "/" + objectName;
            log.info("生成简化URL: {}", url);
            
            return url;
        } catch (Exception e) {
            log.error("生成简化URL失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成文件URL失败");
        }
    }
    
    /**
     * 将URL中的主机地址替换为公网IP
     *
     * @param url 原始URL
     * @return 替换后的URL
     */
    private String replaceHostWithPublicIp(String url) {
        if (url == null || url.isEmpty() || publicIp == null || publicIp.isEmpty()) {
            return url;
        }
        
        try {
            // 从endpoint中提取主机名部分，格式可能是http://hostname:port或https://hostname:port
            String protocol = endpoint.split("://")[0]; // 提取协议部分，如http或https
            String hostToReplace = endpoint.split("://")[1].split("/")[0];
            
            // 检查公网IP是否已包含协议部分
            String publicAddress;
            if (publicIp.startsWith("http://") || publicIp.startsWith("https://")) {
                publicAddress = publicIp;
            } else {
                // 使用原始endpoint的协议
                publicAddress = protocol + "://" + publicIp;
            }
            
            // 替换URL中的主机部分为公网IP
            String fullHostToReplace = protocol + "://" + hostToReplace;
            if (url.contains(fullHostToReplace)) {
                return url.replace(fullHostToReplace, publicAddress);
            }
            
            return url;
        } catch (Exception e) {
            log.error("替换主机地址失败: {}", e.getMessage(), e);
            return url; // 出现异常时返回原始URL
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
