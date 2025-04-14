package com.qingming.jobserver.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 劳务需求返回视图对象
 */
@Data
public class LaborDemandVO {
    
    /**
     * 需求ID
     */
    private Integer id;
    
    /**
     * 项目ID
     */
    private Integer projectId;
    
    /**
     * 项目名称
     */
    private String projectName;
    
    /**
     * 项目地址
     */
    private String projectAddress;
    
    /**
     * 工种ID
     */
    private Integer jobTypeId;
    
    /**
     * 工种名称
     */
    private String jobTypeName;
    
    /**
     * 工种类别
     */
    private String jobTypeCategory;
    
    /**
     * 需求人数
     */
    private Integer headcount;
    
    /**
     * 日薪（元）
     */
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
    
    /**
     * 需求状态（open-开放中、filled-已满、cancelled-已取消、expired-已过期）
     */
    private String status;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 企业名称
     */
    private String companyName;
}
