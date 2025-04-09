package com.qingming.jobserver.model.dao.user;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

/**
 * 求职者注册接口入参数据对象
 */
@Data
public class JobSeekerRegisterDao {
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

    /**
     * 电子邮箱（必填）
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
}