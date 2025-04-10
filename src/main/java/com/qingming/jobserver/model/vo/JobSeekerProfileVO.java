package com.qingming.jobserver.model.vo;

import com.qingming.jobserver.model.enums.AccountStatusEnum;
import com.qingming.jobserver.model.enums.GenderEnum;
import com.qingming.jobserver.model.enums.JobStatusEnum;
import com.qingming.jobserver.model.enums.UserRoleEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 求职者完整资料VO
 * 包含用户基本信息和求职者详细信息
 */
@Data
public class JobSeekerProfileVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // 用户基本信息 (user表)
    private Long id;
    private String username;
    private String mobile;
    private String email;
    private String avatar;
    private UserRoleEnum role;
    private Date createTime;
    private AccountStatusEnum accountStatus;
    
    // 求职者详细信息 (job_seeker表)
    private String realName;
    private GenderEnum gender;
    private Date birthday;
    private JobStatusEnum jobStatus;
    private String expectPosition;
    private Integer workYears;
    private String skill;
    private String certificates;
    private String resumeUrl;
}