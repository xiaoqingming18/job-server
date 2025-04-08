package com.qingming.jobserver.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qingming.jobserver.model.enums.AccountStatusEnum;
import com.qingming.jobserver.model.enums.UserRoleEnum;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("mobile")
    private String mobile;

    @TableField("email")
    private String email;

    @TableField("avatar")
    private String avatar;

    @TableField("role")
    private UserRoleEnum role;

    @TableField("create_time")
    private Date createTime;

    @TableField("account_status")
    private AccountStatusEnum accountStatus;
}