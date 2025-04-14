package com.qingming.jobserver.model.vo;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目详细信息VO
 */
@Data
public class ProjectInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // 项目基本信息
    private Integer id;
    private Integer companyId;
    private String name;
    private String address;
    private String province;
    private String city;
    private String district;
    private Long projectManagerId;
    private String projectManagerName; // 项目经理姓名
    private Date startDate;
    private Date expectedEndDate;
    private String projectType;
    private String projectScale;
    private BigDecimal totalArea;
    private BigDecimal budget;
    private String description;
    private String status;
    private Date createTime;
    private Date updateTime;
}
