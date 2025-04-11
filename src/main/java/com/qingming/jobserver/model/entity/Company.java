package com.qingming.jobserver.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("company")
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("license_number")
    private String licenseNumber;

    @TableField("address")
    private String address;

    @TableField("legal_person")
    private String legalPerson;

    @TableField("admin_id")
    private Long adminId;

    @TableField("create_time")
    private Date createTime;
}