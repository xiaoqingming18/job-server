package com.qingming.jobserver.model.dao.occupation;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 更新工种接口入参数据对象
 */
@Data
public class OccupationUpdateDao {
    
    /**
     * 工种ID（必填）
     */
    @NotNull(message = "工种ID不能为空")
    private Integer id;
    
    /**
     * 工种名称
     */
    @Size(max = 50, message = "工种名称最长50字符")
    private String name;

    /**
     * 工种类别
     */
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