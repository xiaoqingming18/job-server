package com.qingming.jobserver.model.dao.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 企业注册接口入参数据对象
 */
@Data
public class CompanyRegisterDao {
    /**
     * 企业名称（必填）
     */
    @NotBlank(message = "企业名称不能为空")
    @Size(max = 100, message = "企业名称最长100字符")
    private String name;

    /**
     * 营业执照编号（必填）
     */
    @NotBlank(message = "营业执照编号不能为空")
    @Size(max = 50, message = "营业执照编号最长50字符")
    @Pattern(regexp = "^[0-9A-Z]{18}$", message = "营业执照编号格式不正确")
    private String licenseNumber;

    /**
     * 企业地址
     */
    @Size(max = 200, message = "企业地址最长200字符")
    private String address;

    /**
     * 法人代表（必填）
     */
    @NotBlank(message = "法人代表不能为空")
    @Size(max = 50, message = "法人代表最长50字符")
    private String legalPerson;

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
    @jakarta.validation.constraints.Email(message = "邮箱格式不正确")
    private String email;
    
    /**
     * 项目经理职位名称
     */
    @Size(max = 50, message = "职位名称最长50字符")
    private String position;
}
