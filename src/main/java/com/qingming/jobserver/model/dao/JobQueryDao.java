package com.qingming.jobserver.model.dao;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class JobQueryDao {
    /**
     * 工种ID
     */
    private Integer jobTypeId;

    /**
     * 工种类别
     */
    private String jobTypeCategory;

    /**
     * 项目类型
     */
    private String projectType;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 最低日薪
     */
    private BigDecimal minDailyWage;

    /**
     * 最高日薪
     */
    private BigDecimal maxDailyWage;

    /**
     * 是否提供住宿
     */
    private Boolean accommodation;

    /**
     * 是否提供餐食
     */
    private Boolean meals;

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页记录数
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方式（asc/desc）
     */
    private String sortOrder;
} 