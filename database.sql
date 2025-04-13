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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='求职者扩展表';



-- 工种表（建筑行业特有工种分类）
CREATE TABLE `construction_job_type` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '工种ID',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '工种名称',
  `category` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '工种类别（土建/装修/水电/钢筋等）',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '工种描述',
  `required_certificates` json DEFAULT NULL COMMENT '所需证书要求（JSON格式）',
  `average_daily_wage` decimal(10,2) DEFAULT NULL COMMENT '平均日薪（元）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_job_type_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='建筑工种分类表';

-- 建筑项目表
CREATE TABLE `construction_project` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '项目ID',
  `company_id` int unsigned NOT NULL COMMENT '关联企业ID',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目名称',
  `address` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目地址',
  `project_manager_id` bigint unsigned NOT NULL COMMENT '项目经理ID',
  `start_date` date NOT NULL COMMENT '开工日期',
  `expected_end_date` date NOT NULL COMMENT '预计竣工日期',
  `actual_end_date` date DEFAULT NULL COMMENT '实际竣工日期',
  `project_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目类型（住宅/商业/工业/市政等）',
  `project_scale` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目规模（小型/中型/大型）',
  `total_area` decimal(12,2) NOT NULL COMMENT '总建筑面积（平方米）',
  `budget` decimal(15,2) NOT NULL COMMENT '项目预算（元）',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '项目描述',
  `contract_number` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '合同编号',
  `status` enum('pending','in_progress','completed','paused','cancelled') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '项目状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_company` (`company_id`),
  KEY `idx_project_manager` (`project_manager_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `project_company_fk` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `project_manager_fk` FOREIGN KEY (`project_manager_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='建筑项目表';

-- 劳务需求表
CREATE TABLE `labor_demand` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '需求ID',
  `project_id` int unsigned NOT NULL COMMENT '关联项目ID',
  `job_type_id` int unsigned NOT NULL COMMENT '工种ID',
  `headcount` int unsigned NOT NULL COMMENT '需求人数',
  `daily_wage` decimal(10,2) NOT NULL COMMENT '日薪（元）',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date NOT NULL COMMENT '结束日期',
  `work_hours` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '8:00-18:00' COMMENT '工作时间',
  `requirements` text COLLATE utf8mb4_unicode_ci COMMENT '特殊要求',
  `accommodation` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否提供住宿（0否1是）',
  `meals` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否提供餐食（0否1是）',
  `status` enum('open','filled','cancelled','expired') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'open' COMMENT '需求状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_project` (`project_id`),
  KEY `idx_job_type` (`job_type_id`),
  KEY `idx_demand_status` (`status`),
  CONSTRAINT `demand_project_fk` FOREIGN KEY (`project_id`) REFERENCES `construction_project` (`id`) ON DELETE CASCADE,
  CONSTRAINT `demand_job_type_fk` FOREIGN KEY (`job_type_id`) REFERENCES `construction_job_type` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='劳务需求表';

-- 工作记录表
CREATE TABLE `work_record` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `worker_id` bigint unsigned NOT NULL COMMENT '工人用户ID',
  `project_id` int unsigned NOT NULL COMMENT '项目ID',
  `job_type_id` int unsigned NOT NULL COMMENT '工种ID',
  `daily_wage` decimal(10,2) NOT NULL COMMENT '约定日薪（元）',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date DEFAULT NULL COMMENT '结束日期',
  `status` enum('active','completed','terminated') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'active' COMMENT '记录状态',
  `evaluation_rating` tinyint unsigned DEFAULT NULL COMMENT '评价评分（1-5星）',
  `evaluation_comment` text COLLATE utf8mb4_unicode_ci COMMENT '评价内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_worker` (`worker_id`),
  KEY `idx_work_project` (`project_id`),
  KEY `idx_work_job_type` (`job_type_id`),
  CONSTRAINT `work_worker_fk` FOREIGN KEY (`worker_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `work_project_fk` FOREIGN KEY (`project_id`) REFERENCES `construction_project` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `work_job_type_fk` FOREIGN KEY (`job_type_id`) REFERENCES `construction_job_type` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工作记录表';

-- 工资结算表
CREATE TABLE `salary_settlement` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '结算ID',
  `worker_id` bigint unsigned NOT NULL COMMENT '工人用户ID',
  `project_id` int unsigned NOT NULL COMMENT '项目ID',
  `settlement_period_start` date NOT NULL COMMENT '结算周期开始',
  `settlement_period_end` date NOT NULL COMMENT '结算周期结束',
  `work_days` decimal(5,1) NOT NULL COMMENT '工作天数',
  `daily_wage` decimal(10,2) NOT NULL COMMENT '日薪（元）',
  `basic_salary` decimal(12,2) NOT NULL COMMENT '基本工资（元）',
  `overtime_pay` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '加班费（元）',
  `bonus` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '奖金（元）',
  `deductions` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '扣款（元）',
  `deduction_reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扣款原因',
  `total_amount` decimal(12,2) NOT NULL COMMENT '结算总额（元）',
  `payment_method` enum('cash','bank_transfer','digital_payment') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付方式',
  `payment_account` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '支付账号',
  `payment_status` enum('pending','paid','cancelled') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '支付状态',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `remarks` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_settlement_worker` (`worker_id`),
  KEY `idx_settlement_project` (`project_id`),
  KEY `idx_payment_status` (`payment_status`),
  CONSTRAINT `settlement_worker_fk` FOREIGN KEY (`worker_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `settlement_project_fk` FOREIGN KEY (`project_id`) REFERENCES `construction_project` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工资结算表';

-- 建筑行业证书分类表
CREATE TABLE `construction_certificate_type` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '证书类型ID',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '证书名称',
  `issuing_authority` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '发证机构',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '证书说明',
  `validity_period` int unsigned DEFAULT NULL COMMENT '有效期（月）',
  `certificate_level` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '证书等级',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_cert_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='建筑行业证书类型表';

-- 工人证书表
CREATE TABLE `worker_certificate` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `worker_id` bigint unsigned NOT NULL COMMENT '工人用户ID',
  `certificate_type_id` int unsigned NOT NULL COMMENT '证书类型ID',
  `certificate_number` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '证书编号',
  `issue_date` date NOT NULL COMMENT '发证日期',
  `expiry_date` date DEFAULT NULL COMMENT '到期日期',
  `certificate_level` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '证书等级/级别',
  `certificate_image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '证书图片URL',
  `verification_status` enum('pending','verified','rejected') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '验证状态',
  `verification_time` datetime DEFAULT NULL COMMENT '验证时间',
  `verified_by` bigint unsigned DEFAULT NULL COMMENT '验证人ID',
  `remarks` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_worker_cert` (`worker_id`),
  KEY `idx_cert_type` (`certificate_type_id`),
  KEY `idx_verification` (`verification_status`),
  CONSTRAINT `worker_cert_worker_fk` FOREIGN KEY (`worker_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `worker_cert_type_fk` FOREIGN KEY (`certificate_type_id`) REFERENCES `construction_certificate_type` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工人证书表';

-- 工人技能表
CREATE TABLE `worker_skill` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `worker_id` bigint unsigned NOT NULL COMMENT '工人用户ID',
  `job_type_id` int unsigned NOT NULL COMMENT '工种ID',
  `skill_level` enum('beginner','intermediate','skilled','expert','master') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '技能水平',
  `years_experience` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '从业年限',
  `is_primary` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否主要工种（0否1是）',
  `expected_daily_wage` decimal(10,2) DEFAULT NULL COMMENT '期望日薪（元）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_worker_job_type` (`worker_id`,`job_type_id`),
  KEY `idx_skill_worker` (`worker_id`),
  KEY `idx_skill_job_type` (`job_type_id`),
  CONSTRAINT `skill_worker_fk` FOREIGN KEY (`worker_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `skill_job_type_fk` FOREIGN KEY (`job_type_id`) REFERENCES `construction_job_type` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工人技能表';

-- 劳务申请表
CREATE TABLE `labor_application` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '申请ID',
  `demand_id` int unsigned NOT NULL COMMENT '需求ID',
  `worker_id` bigint unsigned NOT NULL COMMENT '工人用户ID',
  `expected_daily_wage` decimal(10,2) DEFAULT NULL COMMENT '期望日薪（元）',
  `status` enum('pending','approved','rejected','withdrawn') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '申请状态',
  `application_message` text COLLATE utf8mb4_unicode_ci COMMENT '申请留言',
  `response_message` text COLLATE utf8mb4_unicode_ci COMMENT '回复信息',
  `response_time` datetime DEFAULT NULL COMMENT '回复时间',
  `responded_by` bigint unsigned DEFAULT NULL COMMENT '回复人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_demand_worker` (`demand_id`,`worker_id`),
  KEY `idx_application_demand` (`demand_id`),
  KEY `idx_application_worker` (`worker_id`),
  KEY `idx_application_status` (`status`),
  CONSTRAINT `application_demand_fk` FOREIGN KEY (`demand_id`) REFERENCES `labor_demand` (`id`) ON DELETE CASCADE,
  CONSTRAINT `application_worker_fk` FOREIGN KEY (`worker_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='劳务申请表';

-- 劳务合同表
CREATE TABLE `labor_contract` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '合同ID',
  `project_id` int unsigned NOT NULL COMMENT '项目ID',
  `worker_id` bigint unsigned NOT NULL COMMENT '工人用户ID',
  `contract_number` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '合同编号',
  `job_type_id` int unsigned NOT NULL COMMENT '工种ID',
  `daily_wage` decimal(10,2) NOT NULL COMMENT '日薪（元）',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date NOT NULL COMMENT '结束日期',
  `work_content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '工作内容',
  `payment_terms` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '付款条款',
  `contract_terms` text COLLATE utf8mb4_unicode_ci COMMENT '合同条款',
  `contract_file_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '合同文件URL',
  `worker_signature_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '工人签名URL',
  `company_signature_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业签名URL',
  `status` enum('pending','active','completed','terminated','cancelled') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '合同状态',
  `termination_reason` text COLLATE utf8mb4_unicode_ci COMMENT '终止原因',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_contract_number` (`contract_number`),
  KEY `idx_contract_project` (`project_id`),
  KEY `idx_contract_worker` (`worker_id`),
  KEY `idx_contract_job_type` (`job_type_id`),
  KEY `idx_contract_status` (`status`),
  CONSTRAINT `contract_project_fk` FOREIGN KEY (`project_id`) REFERENCES `construction_project` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `contract_worker_fk` FOREIGN KEY (`worker_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `contract_job_type_fk` FOREIGN KEY (`job_type_id`) REFERENCES `construction_job_type` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='劳务合同表';

-- 考勤记录表
CREATE TABLE `attendance_record` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `worker_id` bigint unsigned NOT NULL COMMENT '工人用户ID',
  `project_id` int unsigned NOT NULL COMMENT '项目ID',
  `record_date` date NOT NULL COMMENT '记录日期',
  `check_in_time` time DEFAULT NULL COMMENT '签到时间',
  `check_out_time` time DEFAULT NULL COMMENT '签退时间',
  `work_hours` decimal(4,2) DEFAULT NULL COMMENT '工作时长',
  `attendance_status` enum('present','absent','late','leave','half_day') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '出勤状态',
  `overtime_hours` decimal(4,2) DEFAULT '0.00' COMMENT '加班时长',
  `location_check_in` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '签到位置',
  `location_check_out` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '签退位置',
  `remarks` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `recorded_by` bigint unsigned NOT NULL COMMENT '记录人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_worker_project_date` (`worker_id`,`project_id`,`record_date`),
  KEY `idx_attendance_worker` (`worker_id`),
  KEY `idx_attendance_project` (`project_id`),
  KEY `idx_attendance_date` (`record_date`),
  KEY `idx_attendance_status` (`attendance_status`),
  CONSTRAINT `attendance_worker_fk` FOREIGN KEY (`worker_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `attendance_project_fk` FOREIGN KEY (`project_id`) REFERENCES `construction_project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='考勤记录表';

-- 安全事故记录表
CREATE TABLE `safety_incident` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '事故ID',
  `project_id` int unsigned NOT NULL COMMENT '项目ID',
  `incident_date` datetime NOT NULL COMMENT '事故发生时间',
  `incident_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '事故类型',
  `incident_level` enum('minor','moderate','serious','critical','fatal') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '事故等级',
  `location` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '发生地点',
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '事故描述',
  `injured_workers` json DEFAULT NULL COMMENT '受伤工人（JSON格式）',
  `injury_details` text COLLATE utf8mb4_unicode_ci COMMENT '伤情详情',
  `cause_analysis` text COLLATE utf8mb4_unicode_ci COMMENT '原因分析',
  `preventive_measures` text COLLATE utf8mb4_unicode_ci COMMENT '预防措施',
  `reported_to_authorities` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否上报主管部门（0否1是）',
  `report_date` datetime DEFAULT NULL COMMENT '上报时间',
  `report_number` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '上报编号',
  `compensation_amount` decimal(12,2) DEFAULT NULL COMMENT '赔偿金额',
  `settlement_status` enum('pending','in_progress','settled','refused') COLLATE utf8mb4_unicode_ci DEFAULT 'pending' COMMENT '理赔状态',
  `reported_by` bigint unsigned NOT NULL COMMENT '报告人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_incident_project` (`project_id`),
  KEY `idx_incident_date` (`incident_date`),
  KEY `idx_incident_level` (`incident_level`),
  KEY `idx_settlement_status` (`settlement_status`),
  CONSTRAINT `incident_project_fk` FOREIGN KEY (`project_id`) REFERENCES `construction_project` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='安全事故记录表';

-- 工人评价表
CREATE TABLE `worker_evaluation` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `worker_id` bigint unsigned NOT NULL COMMENT '被评工人ID',
  `project_id` int unsigned NOT NULL COMMENT '项目ID',
  `evaluator_id` bigint unsigned NOT NULL COMMENT '评价人ID',
  `job_type_id` int unsigned NOT NULL COMMENT '工种ID',
  `work_quality_rating` tinyint unsigned NOT NULL COMMENT '工作质量评分（1-5星）',
  `work_efficiency_rating` tinyint unsigned NOT NULL COMMENT '工作效率评分（1-5星）',
  `attitude_rating` tinyint unsigned NOT NULL COMMENT '工作态度评分（1-5星）',
  `attendance_rating` tinyint unsigned NOT NULL COMMENT '出勤情况评分（1-5星）',
  `overall_rating` tinyint unsigned NOT NULL COMMENT '综合评分（1-5星）',
  `evaluation_comment` text COLLATE utf8mb4_unicode_ci COMMENT '评价内容',
  `reply_comment` text COLLATE utf8mb4_unicode_ci COMMENT '回复内容',
  `reply_time` datetime DEFAULT NULL COMMENT '回复时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_worker_project_evaluator` (`worker_id`,`project_id`,`evaluator_id`),
  KEY `idx_evaluation_worker` (`worker_id`),
  KEY `idx_evaluation_project` (`project_id`),
  KEY `idx_evaluation_evaluator` (`evaluator_id`),
  KEY `idx_evaluation_job_type` (`job_type_id`),
  KEY `idx_overall_rating` (`overall_rating`),
  CONSTRAINT `evaluation_worker_fk` FOREIGN KEY (`worker_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `evaluation_project_fk` FOREIGN KEY (`project_id`) REFERENCES `construction_project` (`id`) ON DELETE CASCADE,
  CONSTRAINT `evaluation_evaluator_fk` FOREIGN KEY (`evaluator_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `evaluation_job_type_fk` FOREIGN KEY (`job_type_id`) REFERENCES `construction_job_type` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工人评价表';

-- 班组表
CREATE TABLE `work_team` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '班组ID',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '班组名称',
  `project_id` int unsigned NOT NULL COMMENT '所属项目ID',
  `team_leader_id` bigint unsigned NOT NULL COMMENT '班组长ID',
  `job_type_id` int unsigned NOT NULL COMMENT '主要工种ID',
  `member_count` int unsigned NOT NULL DEFAULT '0' COMMENT '成员数量',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '班组描述',
  `status` enum('forming','active','disbanded') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'forming' COMMENT '班组状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_team_project` (`project_id`),
  KEY `idx_team_leader` (`team_leader_id`),
  KEY `idx_team_job_type` (`job_type_id`),
  CONSTRAINT `team_project_fk` FOREIGN KEY (`project_id`) REFERENCES `construction_project` (`id`) ON DELETE CASCADE,
  CONSTRAINT `team_leader_fk` FOREIGN KEY (`team_leader_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `team_job_type_fk` FOREIGN KEY (`job_type_id`) REFERENCES `construction_job_type` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='班组表';

-- 班组成员表
CREATE TABLE `work_team_member` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `team_id` int unsigned NOT NULL COMMENT '班组ID',
  `worker_id` bigint unsigned NOT NULL COMMENT '工人ID',
  `join_date` date NOT NULL COMMENT '加入日期',
  `leave_date` date DEFAULT NULL COMMENT '离开日期',
  `position` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '职位',
  `status` enum('active','left') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'active' COMMENT '状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_team_worker` (`team_id`,`worker_id`,`status`),
  KEY `idx_team_member_team` (`team_id`),
  KEY `idx_team_member_worker` (`worker_id`),
  CONSTRAINT `team_member_team_fk` FOREIGN KEY (`team_id`) REFERENCES `work_team` (`id`) ON DELETE CASCADE,
  CONSTRAINT `team_member_worker_fk` FOREIGN KEY (`worker_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='班组成员表';