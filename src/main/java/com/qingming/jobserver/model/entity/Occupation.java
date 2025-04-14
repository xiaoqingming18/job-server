package com.qingming.jobserver.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 工种实体类
 */
@Data
@TableName("occupation")
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
     * 工种类别
     */
    private String category;

    /**
     * 工种描述
     */
    private String description;

    /**
     * 所需证书要求（JSON格式）
     */
    private String requiredCertificates;

    /**
     * 平均日薪（元）
     */
    private BigDecimal averageDailyWage;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}