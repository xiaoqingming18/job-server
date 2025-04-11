-- 企业表
CREATE TABLE `company` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '企业ID',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '企业名称',
  `license_number` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '营业执照编号',
  `address` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业地址',
  `legal_person` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '法人代表',
  `admin_id` bigint unsigned DEFAULT NULL COMMENT '企业管理员ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_license` (`license_number`),
  KEY `company_admin_fk` (`admin_id`),
  CONSTRAINT `company_admin_fk` FOREIGN KEY (`admin_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='企业信息表';

-- 用户表
CREATE TABLE `user` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名（唯一）',
  `password` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（明文）',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '电子邮箱',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像URL',
  `role` enum('system_admin','project_manager','job_seeker','company_admin') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户角色',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `account_status` enum('enabled','disabled','pending') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '账号状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`),
  UNIQUE KEY `idx_email` (`email`),
  UNIQUE KEY `idx_mobile` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户基础信息表';

-- 求职者扩展表
CREATE TABLE `job_seeker` (
  `user_id` bigint unsigned NOT NULL COMMENT '关联用户ID',
  `real_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '真实姓名',
  `gender` enum('male','female','other') COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `job_status` enum('seeking','employed','inactive') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'seeking' COMMENT '求职状态',
  `expect_position` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '期望职位',
  `work_years` tinyint unsigned DEFAULT NULL COMMENT '工作年限',
  `skill` text COLLATE utf8mb4_unicode_ci COMMENT '技能特长',
  `certificates` json DEFAULT NULL COMMENT '证书信息（JSON格式）',
  `resume_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简历存储路径',
  PRIMARY KEY (`user_id`),
  CONSTRAINT `job_seeker_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='求职者扩展表';

-- 项目经理扩展表
CREATE TABLE `project_manager` (
  `user_id` bigint unsigned NOT NULL COMMENT '关联用户ID',
  `company_id` int unsigned NOT NULL COMMENT '关联企业ID',
  `position` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '职位名称',
  PRIMARY KEY (`user_id`),
  KEY `company_id` (`company_id`),
  CONSTRAINT `project_manager_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `project_manager_ibfk_2` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAUL