package com.qingming.jobserver.model.dao.occupation;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 添加工种接口入参数据对象
 */
@Data
public class OccupationAddDao {
    
    /**
     * 工种名称（必填）
     */
    @NotBlank(message = "工种名称不能为空")
    @Size(max = 50, message = "工种名称最长50字符")
    private String name;

    /**
     * 工种类别（必填）
     */
    @NotBlank(message = "工种类别不能为空")
    @Size(max = 50, message = "工种类别最长50字符")
    private String category;

    /**
     * 工种描述
     */
    @Size(max = 1000, message = "工种描述最长1000字符")
    private String description;

    /**
     * 所需证书要求（JSON格式）
     */
    private String requiredCertificates;

    /**
     * 平均日薪（元）
     */
    @DecimalMin(value = "0.0", message = "平均日薪不能为负数")
    private BigDecimal averageDailyWage;
}