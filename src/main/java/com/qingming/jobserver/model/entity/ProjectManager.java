package com.qingming.jobserver.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("project_manager")
public class ProjectManager implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableField("user_id")
    private Long userId;

    @TableField("company_id")
    private Integer companyId;

    @TableField("position")
    private String position;
}