package com.qingming.jobserver.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qingming.jobserver.model.enums.GenderEnum;
import com.qingming.jobserver.model.enums.JobStatusEnum;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("job_seeker")
public class JobSeeker implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id")
    private Long userId;

    @TableField("real_name")
    private String realName;

    @TableField("gender")
    private GenderEnum gender;

    @TableField("birthday")
    private Date birthday;

    @TableField("job_status")
    private JobStatusEnum jobStatus;

    @TableField("expect_position")
    private String expectPosition;

    @TableField("work_years")
    private Integer workYears;

    @TableField("skill")
    private String skill;

    @TableField("certificates")
    private String certificates;

    @TableField("resume_url")
    private String resumeUrl;
}