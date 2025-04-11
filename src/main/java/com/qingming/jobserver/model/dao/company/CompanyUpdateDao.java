package com.qingming.jobserver.model.dao.company;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 企业信息更新接口入参数据对象
 */
@Data
public class CompanyUpdateDao {
    /**
     * 企业ID（必填）
     */
    @NotNull(message = "企业ID不能为空")
    private Integer id;

    /**
     * 企业名称
     */
    @Size(max = 100, message = "企业名称最长100字符")
    private String name;

    /**
     * 企业地址
     */
    @Size(max = 200, message = "企业地址最长200字符")
    private String address;

    /**
     * 法人代表
     */
    @Size(max = 50, message = "法人代表最长50字符")
    private String legalPerson;
}