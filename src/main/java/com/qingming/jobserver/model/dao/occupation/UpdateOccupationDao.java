package com.qingming.jobserver.model.dao.occupation;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 更新工种请求对象
 */
@Data
public class UpdateOccupationDao {
    /**
     * 工种ID
     */
    @NotNull(message = "工种ID不能为空")
    private Integer id;

    /**
     * 工种名称
     */
    @Size(max = 50, message = "工种名称长度不能超过50个字符")
    private String name;

    /**
     * 工种类别
     */
    @Size(max = 50, message = "工种类别长度不能超过50个字符")
    private String category;

    /**
     * 工种描述
     */
    @Size(max = 1000, message = "工种描述长度不能超过1000个字符")
    private String description;

    /**
     * 所需证书要求（JSON格式）
     */
    private String requiredCertificates;

    /**
     * 平均日薪（元）
     */
    private BigDecimal averageDailyWage;
}