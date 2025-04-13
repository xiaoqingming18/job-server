package com.qingming.jobserver.controller;

import com.qingming.jobserver.common.BaseResponse;
import com.qingming.jobserver.common.CurrentUserUtils;
import com.qingming.jobserver.common.ErrorCode;
import com.qingming.jobserver.common.MinioUtil;
import com.qingming.jobserver.common.ResultUtils;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    private final MinioUtil minioUtil;
    private final UserService userService;

    @Value("${minio.endpoint}")
    private String minioEndpoint;

    @Value("${minio.bucketName}")
    private String bucketName;

    public FileController(MinioUtil minioUtil, UserService userService) {
        this.minioUtil = minioUtil;
        this.userService = userService;
    }

    /**
     * 上传用户头像
     * @param file 头像文件
     * @return 头像URL
     */
    @PostMapping("/upload/avatar")
    public BaseResponse<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "上传文件不能为空");
            }

            // 检查文件大小（2MB以内）
            if (file.getSize() > 2 * 1024 * 1024) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过2MB");
            }

            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "只能上传图片文件");
            }

            // 获取当前登录用户ID
            Long userId = CurrentUserUtils.getCurrentUserId();
            if (userId == null) {
                throw new BusinessException(ErrorCode.NOT_LOGIN, "用户未登录");
            }

            // 上传文件到MinIO，保存在avatars目录下
            String objectName = minioUtil.uploadFile(file, "avatars");
            
            // 生成可访问的URL (7天有效期)
            String avatarUrl = minioUtil.getPresignedUrl(objectName, 7 * 24 * 60 * 60);
            
            // 更新用户头像URL
            boolean updateResult = userService.updateUserAvatar(userId, avatarUrl);
            if (!updateResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新头像失败");
            }
            
            // 返回结果
            Map<String, String> result = new HashMap<>();
            result.put("url", avatarUrl);
            result.put("objectName", objectName);
            
            return ResultUtils.success(result);
        } catch (BusinessException e) {
            log.error("上传头像业务异常: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("上传头像系统异常", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传头像失败: " + e.getMessage());
        }
    }
}
