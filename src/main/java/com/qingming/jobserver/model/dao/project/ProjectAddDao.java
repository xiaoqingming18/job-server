package com.qingming.jobserver.model.dao.project;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 添加项目接口入参数据对象
 */
@Data
public class ProjectAddDao {

    /**
     * 关联企业ID（必填）
     */
    @NotNull(message = "企业ID不能为空")
    private Integer companyId;

    /**
     * 项目名称（必填）
     */
    @NotBlank(message = "项目名称不能为空")
    @Size(max = 100, message = "项目名称最长100个字符")
    private String name;

    /**
     * 项目地址（必填）
     */
    @NotBlank(message = "项目地址不能为空")
    @Size(max = 255, message = "项目地址最长255个字符")
    private String address;

    /**
     * 开工日期（必填）
     */
    @NotNull(message = "开工日期不能为空")
    private Date startDate;

    /**
     * 预计竣工日期（必填）
     */
    @NotNull(message = "预计竣工日期不能为空")
    private Date expectedEndDate;

    /**
     * 项目类型（必填）
     * 住宅/商业/工业/市政等
     */
    @NotBlank(message = "项目类型不能为空")
    @Pattern(regexp = "^(residence|commerce|industry|municipal)$", message = "项目类型必须为：residence、commerce、industry或municipal")
    private String projectType;

    /**
     * 项目规模（小型/中型/大型）（必填）
     */
    @NotBlank(message = "项目规模不能为空")
    @Pattern(regexp = "^(small|medium|big)$", message = "项目规模必须为：small、medium或big")
    private String projectScale;

    /**
     * 总建筑面积（平方米）（必填）
     */
    @NotNull(message = "总建筑面积不能为空")
    @DecimalMin(value = "0.01", message = "总建筑面积必须大于0")
    private BigDecimal totalArea;

    /**
     * 项目预算（元）（必填）
     */
    @NotNull(message = "项目预算不能为空")
    @DecimalMin(value = "0.01", message = "项目预算必须大于0")
    private BigDecimal budget;

    /**
     * 项目描述（选填）
     */
    private String description;
}
