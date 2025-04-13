package com.qingming.jobserver.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 建筑项目实体类
 */
@Data
@TableName("construction_project")
public class ConstructionProject implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Integer id;
    
    @TableField("company_id")
    private Integer companyId;
    
    @TableField("name")
    private String name;
    
    @TableField("address")
    private String address;
    
    @TableField("project_manager_id")
    private Long projectManagerId;
    
    @TableField("start_date")
    private Date startDate;
    
    @TableField("expected_end_date")
    private Date expectedEndDate;
    
    @TableField("project_type")
    private String projectType;
    
    @TableField("project_scale")
    private String projectScale;
    
    @TableField("total_area")
    private BigDecimal totalArea;
    
    @TableField("budget")
    private BigDecimal budget;
    
    @TableField("description")
    private String description;
    
    @TableField("status")
    private String status;
    
    @TableField("create_time")
    private Date createTime;
    
    @TableField("update_time")
    private Date updateTime;
}
