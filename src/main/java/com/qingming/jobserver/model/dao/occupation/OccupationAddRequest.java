package com.qingming.jobserver.model.dao.occupation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 添加工种请求
 */
@Data
public class OccupationAddRequest {
    
    /**
     * 工种名称
     */
    @NotBlank(message = "工种名称不能为空")
    @Size(max = 50, message = "工种名称长度不能超过50个字符")
    private String name;
    
    /**
     * 所属类别ID
     */
    @NotNull(message = "所属类别ID不能为空")
    private Integer categoryId;
    
    /**
     * 工种图标
     */
    private String icon;
    
    /**
     * 工种描述
     */
    private String description;
    
    /**
     * 所需证书要求（JSON格式）
     */
    private List<String> requiredCertificates;
    
    /**
     * 平均日薪（元）
     */
    private BigDecimal averageDailyWage;
    
    /**
     * 难度等级(1-5)
     */
    private Integer difficultyLevel;
} 