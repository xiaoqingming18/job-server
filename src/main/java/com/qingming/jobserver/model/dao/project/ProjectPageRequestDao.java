package com.qingming.jobserver.model.dao.project;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ProjectPageRequestDao {
    /**
     * 页码（从1开始）
     */
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    @Min(value = 1, message = "每页数量最小为1")
    @Max(value = 100, message = "每页数量最大为100")
    private Integer pageSize = 10;

    /**
     * 项目状态筛选
     */
    private String status;

    /**
     * 项目类型筛选
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
     * 排序字段（create_time/total_area/budget）
     */
    private String orderBy = "create_time";

    /**
     * 排序方向（asc/desc）
     */
    private String orderDirection = "desc";
} 