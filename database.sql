-- 用户基础表（公共信息）
CREATE TABLE `user` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名（唯一）',
  `password` VARCHAR(50) NOT NULL COMMENT '密码（明文）',  -- 改为明文存储
  `mobile` VARCHAR(20) NULL COMMENT '手机号',
  `email` VARCHAR(100) NOT NULL COMMENT '电子邮箱',
  `avatar` VARCHAR(255) COMMENT '头像URL',
  `role` ENUM('system_admin', 'project_manager', 'job_seeker') NOT NULL COMMENT '用户角色',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `account_status` ENUM('enabled', 'disabled', 'pending') NOT NULL DEFAULT 'pending' COMMENT '账号状态',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idx_username` (`username`),
  UNIQUE INDEX `idx_mobile` (`mobile`),
  UNIQUE INDEX `idx_email` (`email`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='用户基础信息表';

-- 新增企业信息表
CREATE TABLE `company` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '企业ID',
  `name` VARCHAR(100) NOT NULL COMMENT '企业名称',
  `license_number` VARCHAR(50) NOT NULL COMMENT '营业执照编号',
  `address` VARCHAR(200) COMMENT '企业地址',
  `legal_person` VARCHAR(50) COMMENT '法人代表',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idx_license` (`license_number`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='企业信息表';

-- 项目管理员扩展表（关联企业）
CREATE TABLE `project_manager` (
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '关联用户ID',
  `company_id` INT UNSIGNED NOT NULL COMMENT '关联企业ID',
  `position` VARCHAR(50) NOT NULL COMMENT '职位名称',
  PRIMARY KEY (`user_id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE RESTRICT
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='项目管理员扩展表';

-- 求职者扩展表（保留原结构）
CREATE TABLE `job_seeker` (
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '关联用户ID',
  `real_name` VARCHAR(50) COMMENT '真实姓名',
  `gender` ENUM('male', 'female', 'other') COMMENT '性别',
  `birthday` DATE COMMENT '出生日期',
  `job_status` ENUM('seeking', 'employed', 'inactive') NOT NULL DEFAULT 'seeking' COMMENT '求职状态',
  `expect_position` VARCHAR(100) COMMENT '期望职位',
  `work_years` TINYINT UNSIGNED COMMENT '工作年限',
  `skill` TEXT COMMENT '技能特长',
  `certificates` JSON COMMENT '证书信息（JSON格式）',
  `resume_url` VARCHAR(255) COMMENT '简历存储路径',
  PRIMARY KEY (`user_id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT='求职者扩展表';