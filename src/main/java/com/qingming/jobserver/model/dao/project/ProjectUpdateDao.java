package com.qingming.jobserver.model.dao.project;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 更新项目接口入参数据对象
 */
@Data
public class ProjectUpdateDao {

    /**
     * 项目ID（必填）
     */
    @NotNull(message = "项目ID不能为空")
    private Integer id;

    /**
     * 项目名称（选填）
     */
    @Size(max = 100, message = "项目名称最长100个字符")
    private String name;

    /**
     * 项目地址（选填）
     */
    @Size(max = 255, message = "项目地址最长255个字符")
    private String address;

    /**
     * 省份（选填）
     */
    @Size(max = 50, message = "省份最长50个字符")
    private String province;

    /**
     * 城市（选填）
     */
    @Size(max = 50, message = "城市最长50个字符")
    private String city;

    /**
     * 区县（选填）
     */
    @Size(max = 50, message = "区县最长50个字符")
    private String district;

    /**
     * 开工日期（选填）
     */
    private Date startDate;

    /**
     * 预计竣工日期（选填）
     */
    private Date expectedEndDate;

    /**
     * 项目类型（选填）
     * 住宅/商业/工业/市政等
     */
    @Pattern(regexp = "^(residence|commerce|industry|municipal)$", message = "项目类型必须为：residence、commerce、industry或municipal")
    private String projectType;

    /**
     * 项目规模（小型/中型/大型）（选填）
     */
    @Pattern(regexp = "^(small|medium|big)$", message = "项目规模必须为：small、medium或big")
    private String projectScale;

    /**
     * 总建筑面积（平方米）（选填）
     */
    @DecimalMin(value = "0.01", message = "总建筑面积必须大于0")
    private BigDecimal totalArea;

    /**
     * 项目预算（元）（选填）
     */
    @DecimalMin(value = "0.01", message = "项目预算必须大于0")
    private BigDecimal budget;

    /**
     * 项目描述（选填）
     */
    private String description;
}