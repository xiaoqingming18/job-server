package com.qingming.jobserver.model.dao.company;

import lombok.Data;

/**
 * 企业查询参数
 */
@Data
public class CompanyQueryDao {
    
    /**
     * 企业名称（模糊查询）
     */
    private String name;
    
    /**
     * 当前页码，默认为1
     */
    private Integer pageNum = 1;
    
    /**
     * 每页记录数，默认为10
     */
    private Integer pageSize = 10;
} 