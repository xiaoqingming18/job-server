package com.qingming.jobserver.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 工种实体类
 */
@Data
@TableName(value = "occupation", autoResultMap = true)
public class Occupation {
    /**
     * 工种ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 工种名称
     */
    private String name;

    /**
     * 所属类别ID
     */
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
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> requiredCertificates;

    /**
     * 平均日薪（元）
     */
    private BigDecimal averageDailyWage;

    /**
     * 难度等级(1-5)
     */
    private Integer difficultyLevel;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 非数据库字段：类别名称
     */
    @TableField(exist = false)
    private String categoryName;
}