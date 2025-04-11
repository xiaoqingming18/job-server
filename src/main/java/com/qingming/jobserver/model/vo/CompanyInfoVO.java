package com.qingming.jobserver.model.vo;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 企业详细信息VO
 */
@Data
public class CompanyInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // 企业基本信息
    private Integer id;
    private String name;
    private String licenseNumber;
    private String address;
    private String legalPerson;
    private Date createTime;
}