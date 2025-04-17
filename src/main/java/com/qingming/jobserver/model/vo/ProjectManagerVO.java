package com.qingming.jobserver.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * 项目经理信息VO
 */
@Data
public class ProjectManagerVO {
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 手机号
     */
    private String mobile;
    
    /**
     * 电子邮箱
     */
    private String email;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 职位名称
     */
    private String position;
    
    /**
     * 企业ID
     */
    private Integer companyId;
    
    /**
     * 企业名称
     */
    private String companyName;
    
    /**
     * 注册时间
     */
    private Date createTime;
    
    /**
     * 账号状态
     */
    private String accountStatus;
} 