package com.qingming.jobserver.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 工种类别实体类
 */
@Data
@TableName("occupation_category")
public class OccupationCategory {
    
    /**
     * 类别ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    /**
     * 类别名称
     */
    private String name;
    
    /**
     * 类别图标
     */
    private String icon;
    
    /**
     * 类别描述
     */
    private String description;
    
    /**
     * 排序序号
     */
    private Integer sortOrder;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
} 