package com.qingming.jobserver.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 劳务需求实体类
 */
@Data
@TableName("labor_demand")
public class LaborDemand {
    
    /**
     * 需求ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    /**
     * 需求标题
     */
    @TableField("title")
    private String title;
    
    /**
     * 关联项目ID
     */
    @TableField("project_id")
    private Integer projectId;
    
    /**
     * 工种ID
     */
    @TableField("job_type_id")
    private Integer jobTypeId;
    
    /**
     * 需求人数
     */
    @TableField("headcount")
    private Integer headcount;
    
    /**
     * 日薪（元）
     */
    @TableField("daily_wage")
    private BigDecimal dailyWage;
    
    /**
     * 开始日期
     */
    @TableField("start_date")
    private Date startDate;
    
    /**
     * 结束日期
     */
    @TableField("end_date")
    private Date endDate;
    
    /**
     * 工作时间
     */
    @TableField("work_hours")
    private String workHours;
    
    /**
     * 特殊要求
     */
    @TableField("requirements")
    private String requirements;
    
    /**
     * 是否提供住宿
     */
    @TableField("accommodation")
    private Boolean accommodation;
    
    /**
     * 是否提供餐食
     */
    @TableField("meals")
    private Boolean meals;
    
    /**
     * 需求状态（open-开放中、filled-已满、cancelled-已取消、expired-已过期）
     */
    @TableField("status")
    private String status;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
}
