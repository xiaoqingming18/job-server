package com.qingming.jobserver.model.dao.company;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 添加项目经理接口入参数据对象
 */
@Data
public class AddProjectManagerDao {
    /**
     * 公司ID（必填）
     */
    @NotNull(message = "企业ID不能为空")
    private Integer companyId;

    /**
     * 项目经理用户名（必填）
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 50, message = "用户名长度为4-50位")
    private String username;

    /**
     * 项目经理密码（必填）
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度为6-20位")
    private String password;
    
    /**
     * 项目经理邮箱（必填）
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    /**
     * 项目经理职位名称
     */
    @Size(max = 50, message = "职位名称最长50字符")
    private String position;
}