package com.qingming.jobserver.model.dao.user;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 项目经理登录接口入参数据对象
 */
@Data
public class ProjectManagerLoginDao {
    /**
     * 用户名（必填）
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码（必填）
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}