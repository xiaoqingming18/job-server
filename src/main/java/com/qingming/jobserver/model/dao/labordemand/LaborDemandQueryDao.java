package com.qingming.jobserver.model.dao.labordemand;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 劳务需求查询参数
 */
@Data
public class LaborDemandQueryDao {
    
    /**
     * 项目ID
     */
    private Integer projectId;
    
    /**
     * 工种ID
     */
    private Integer jobTypeId;
    
    /**
     * 最小日薪
     */
    private BigDecimal minDailyWage;
    
    /**
     * 最大日薪
     */
    private BigDecimal maxDailyWage;
    
    /**
     * 开始日期
     */
    private Date startDate;
    
    /**
     * 结束日期
     */
    private Date endDate;
    
    /**
     * 是否提供住宿
     */
    private Boolean accommodation;
    
    /**
     * 是否提供餐食
     */
    private Boolean meals;
    
    /**
     * 需求状态
     */
    private String status;
    
    /**
     * 当前页码，默认为1
     */
    private Integer pageNum = 1;
    
    /**
     * 每页记录数，默认为10
     */
    private Integer pageSize = 10;
}
