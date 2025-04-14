package com.qingming.jobserver.model.dao.labordemand;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 更新劳务需求请求参数
 */
@Data
public class LaborDemandUpdateDao {
    
    /**
     * 需求ID
     */
    @NotNull(message = "需求ID不能为空")
    private Integer id;
    
    /**
     * 项目ID
     */
    private Integer projectId;
    
    /**
     * 工种ID
     */
    private Integer jobTypeId;
    
    /**
     * 需求人数
     */
    @Min(value = 1, message = "需求人数必须大于0")
    private Integer headcount;
    
    /**
     * 日薪（元）
     */
    @Min(value = 0, message = "日薪不能为负数")
    private BigDecimal dailyWage;
    
    /**
     * 开始日期
     */
    private Date startDate;
    
    /**
     * 结束日期
     */
    private Date endDate;
    
    /**
     * 工作时间
     */
    private String workHours;
    
    /**
     * 特殊要求
     */
    private String requirements;
    
    /**
     * 是否提供住宿
     */
    private Boolean accommodation;
    
    /**
     * 是否提供餐食
     */
    private Boolean meals;
}
