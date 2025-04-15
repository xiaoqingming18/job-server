-- 工种分类表
CREATE TABLE `occupation` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '工种ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '工种名称',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '工种类别（土建/装修/水电/钢筋等）',
  `icon` text COLLATE utf8mb4_unicode_ci,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '工种描述',
  `required_certificates` json DEFAULT NULL COMMENT '所需证书要求（JSON格式）',
  `average_daily_wage` decimal(10,2) DEFAULT NULL COMMENT '平均日薪（元）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_job_type_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='建筑工种分类表';

-- 用户表
CREATE TABLE `user` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名（唯一）',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（明文）',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '电子邮箱',
  `avatar` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '头像URL',
  `role` enum('system_admin','project_manager','job_seeker','company_admin') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户角色',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `account_status` enum('enabled','disabled','pending') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '账号状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`),
  UNIQUE KEY `idx_email` (`email`),
  UNIQUE KEY `idx_mobile` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户基础信息表';

-- 企业信息表
CREATE TABLE `company` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '企业ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '企业名称',
  `license_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '营业执照编号',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业地址',
  `legal_person` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '法人代表',
  `admin_id` bigint unsigned DEFAULT NULL COMMENT '企业管理员ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_license` (`license_number`),
  KEY `company_admin_fk` (`admin_id`),
  CONSTRAINT `company_admin_fk` FOREIGN KEY (`admin_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='企业信息表';

-- 建筑项目表
CREATE TABLE `construction_project` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '项目ID',
  `company_id` int unsigned NOT NULL COMMENT '关联企业ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目名称',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目地址',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '省份',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '城市',
  `district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '区县',
  `project_manager_id` bigint unsigned NOT NULL COMMENT '项目经理ID',
  `start_date` date NOT NULL COMMENT '开工日期',
  `expected_end_date` date NOT NULL COMMENT '预计竣工日期',
  `project_type` enum('residence','commerce','industry','municipal') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目类型（住宅/商业/工业/市政等）',
  `project_scale` enum('small','medium','big') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目规模（小型/中型/大型）',
  `total_area` decimal(12,2) NOT NULL COMMENT '总建筑面积（平方米）',
  `budget` decimal(15,2) NOT NULL COMMENT '项目预算（元）',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '项目描述',
  `status` enum('pending','in_progress','completed','paused','cancelled') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '项目状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_company` (`company_id`),
  KEY `idx_project_manager` (`project_manager_id`),
  KEY `idx_status` (`status`),
  KEY `idx_location` (`province`,`city`,`district`) COMMENT '地理位置索引',
  CONSTRAINT `project_company_fk` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `project_manager_fk` FOREIGN KEY (`project_manager_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='建筑项目表';

-- 求职者扩展表
CREATE TABLE `job_seeker` (
  `user_id` bigint unsigned NOT NULL COMMENT '关联用户ID',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '真实姓名',
  `gender` enum('male','female','other') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `job_status` enum('seeking','employed','inactive') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'seeking' COMMENT '求职状态',
  `expect_position` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '期望职位',
  `work_years` tinyint unsigned DEFAULT NULL COMMENT '工作年限',
  `skill` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '技能特长',
  `certificates` json DEFAULT NULL COMMENT '证书信息（JSON格式）',
  `resume_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简历存储路径',
  PRIMARY KEY (`user_id`),
  CONSTRAINT `job_seeker_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='求职者扩展表';

-- 劳务需求表
CREATE TABLE `labor_demand` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '需求ID',
  `project_id` int unsigned NOT NULL COMMENT '关联项目ID',
  `job_type_id` int unsigned NOT NULL COMMENT '工种ID',
  `headcount` int unsigned NOT NULL COMMENT '需求人数',
  `daily_wage` decimal(10,2) NOT NULL COMMENT '日薪（元）',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date NOT NULL COMMENT '结束日期',
  `work_hours` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '8:00-18:00' COMMENT '工作时间',
  `requirements` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '特殊要求',
  `accommodation` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否提供住宿（0否1是）',
  `meals` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否提供餐食（0否1是）',
  `status` enum('open','filled','cancelled','expired') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'open' COMMENT '需求状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_project` (`project_id`),
  KEY `idx_job_type` (`job_type_id`),
  KEY `idx_demand_status` (`status`),
  CONSTRAINT `demand_job_type_fk` FOREIGN KEY (`job_type_id`) REFERENCES `occupation` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `demand_project_fk` FOREIGN KEY (`project_id`) REFERENCES `construction_project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='劳务需求表';

-- 项目经理扩展表
CREATE TABLE `project_manager` (
  `user_id` bigint unsigned NOT NULL COMMENT '关联用户ID',
  `company_id` int unsigned NOT NULL COMMENT '关联企业ID',
  `position` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '职位名称',
  PRIMARY KEY (`user_id`),
  KEY `company_id` (`company_id`),
  CONSTRAINT `project_manager_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `project_manager_ibfk_2` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目经理扩展表';


-- 求职申请表
CREATE TABLE `job_application` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '申请ID',
  `labor_demand_id` int unsigned NOT NULL COMMENT '关联的劳务需求ID',
  `job_seeker_id` bigint unsigned NOT NULL COMMENT '求职者用户ID',
  `project_id` int unsigned NOT NULL COMMENT '关联的项目ID',
  `status` enum('pending','interview','rejected','accepted','cancelled','employed') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '申请状态（待处理/面试中/已拒绝/已接受/已取消/已入职）',
  `expected_salary` decimal(10,2) DEFAULT NULL COMMENT '期望薪资',
  `available_start_date` date DEFAULT NULL COMMENT '最早到岗日期',
  `application_letter` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '申请说明',
  `reject_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '拒绝原因',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_job_seeker` (`job_seeker_id`),
  KEY `idx_labor_demand` (`labor_demand_id`),
  KEY `idx_project` (`project_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `application_job_seeker_fk` FOREIGN KEY (`job_seeker_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `application_labor_demand_fk` FOREIGN KEY (`labor_demand_id`) REFERENCES `labor_demand` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `application_project_fk` FOREIGN KEY (`project_id`) REFERENCES `construction_project` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='求职申请表';

-- 面试记录表
CREATE TABLE `interview` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '面试ID',
  `application_id` int unsigned NOT NULL COMMENT '关联的申请ID',
  `interviewer_id` bigint unsigned NOT NULL COMMENT '面试官用户ID（项目经理）',
  `interview_time` datetime NOT NULL COMMENT '面试时间',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '面试地点',
  `status` enum('scheduled','completed','cancelled','absent') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'scheduled' COMMENT '面试状态（已安排/已完成/已取消/未到场）',
  `interview_notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '面试记录',
  `evaluation` enum('pass','fail','pending') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '面试评价（通过/不通过/待定）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_application` (`application_id`),
  KEY `idx_interviewer` (`interviewer_id`),
  KEY `idx_interview_time` (`interview_time`),
  CONSTRAINT `interview_application_fk` FOREIGN KEY (`application_id`) REFERENCES `job_application` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `interview_interviewer_fk` FOREIGN KEY (`interviewer_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='面试记录表';

-- 入职记录表
CREATE TABLE `onboarding` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '入职记录ID',
  `application_id` int unsigned NOT NULL COMMENT '关联的申请ID',
  `actual_salary` decimal(10,2) NOT NULL COMMENT '实际薪资',
  `start_date` date NOT NULL COMMENT '入职日期',
  `contract_period` int NOT NULL COMMENT '合同期限（月）',
  `status` enum('pending','active','terminated','completed') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '入职状态（待入职/在职/已终止/已完成）',
  `termination_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '终止原因',
  `termination_date` date DEFAULT NULL COMMENT '终止日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_application` (`application_id`),
  CONSTRAINT `onboarding_application_fk` FOREIGN KEY (`application_id`) REFERENCES `job_application` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='入职记录表';