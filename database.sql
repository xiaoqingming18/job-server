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

-- 插入企业示例数据
INSERT INTO `company` (`id`, `name`, `license_number`, `address`, `legal_person`, `admin_id`, `create_time`) VALUES
(1, '建业建筑工程有限公司', 'JY91430121MA4L16TX9X', '湖南省长沙市岳麓区麓谷大道627号', '张建国', 1, '2024-01-15 10:00:00'),
(2, '鸿达建设集团有限公司', 'HD91110108MA01A4CC2B', '北京市海淀区西三环北路25号', '李鸿志', 2, '2024-01-20 09:30:00'),
(3, '宏远建筑劳务有限公司', 'HY914401017384716A8D', '广东省广州市天河区体育西路101号', '王宏远', 3, '2024-02-01 14:20:00');

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

-- 插入用户示例数据
INSERT INTO `user` (`id`, `username`, `password`, `mobile`, `email`, `avatar`, `role`, `create_time`, `account_status`) VALUES
(1, 'admin', 'admin123', '13800138000', 'admin@example.com', '/avatars/admin.jpg', 'system_admin', '2024-01-10 08:00:00', 'enabled'),
(2, 'zhangjg', 'pass123', '13911111111', 'zhang@jianye.com', '/avatars/zhang.jpg', 'company_admin', '2024-01-15 10:15:00', 'enabled'),
(3, 'lihongzhi', 'pass123', '13922222222', 'li@hongda.com', '/avatars/li.jpg', 'company_admin', '2024-01-20 09:45:00', 'enabled'),
(4, 'wangmanager', 'pass123', '13933333333', 'wang@hongda.com', '/avatars/wang.jpg', 'project_manager', '2024-02-05 11:30:00', 'enabled'),
(5, 'liumanager', 'pass123', '13944444444', 'liu@jianye.com', '/avatars/liu.jpg', 'project_manager', '2024-02-06 14:20:00', 'enabled'),
(6, 'zhangsan', 'pass123', '13955555555', 'zhangsan@qq.com', '/avatars/zhangsan.jpg', 'job_seeker', '2024-02-10 09:00:00', 'enabled'),
(7, 'lisi', 'pass123', '13966666666', 'lisi@qq.com', '/avatars/lisi.jpg', 'job_seeker', '2024-02-11 10:30:00', 'enabled'),
(8, 'wangwu', 'pass123', '13977777777', 'wangwu@qq.com', '/avatars/wangwu.jpg', 'job_seeker', '2024-02-12 15:45:00', 'enabled');

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
  `id_card_number` varchar(18) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '身份证号码',
  `health_certificate` tinyint(1) DEFAULT 0 COMMENT '是否有健康证(1:有,0:无)',
  `safety_certificate` tinyint(1) DEFAULT 0 COMMENT '是否有安全培训证(1:有,0:无)', 
  PRIMARY KEY (`user_id`),
  CONSTRAINT `job_seeker_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='求职者扩展表';

-- 插入求职者示例数据
INSERT INTO `job_seeker` (`user_id`, `real_name`, `gender`, `birthday`, `job_status`, `expect_position`, `work_years`, `skill`, `certificates`, `resume_url`, `id_card_number`, `health_certificate`, `safety_certificate`) VALUES
(6, '张三', 'male', '1985-06-15', 'seeking', '木工/瓦工', 8, '擅长木工、家具制作，掌握各种木工工具使用', '[{"name":"中级木工证","issue_date":"2018-05-20","number":"WG2018052001"}]', '/resumes/zhangsan.pdf', '430121198506150011', 1, 1),
(7, '李四', 'male', '1990-03-22', 'seeking', '电工', 5, '专业电工，熟悉建筑电路安装与维修', '[{"name":"电工操作证","issue_date":"2019-07-15","number":"DG2019071501"}]', '/resumes/lisi.pdf', '110108199003220857', 1, 1),
(8, '王五', 'male', '1988-11-10', 'employed', '钢筋工', 7, '专业钢筋工，熟悉钢筋绑扎、预制等工作', '[{"name":"钢筋工证书","issue_date":"2017-09-10","number":"GJ2017091022"}]', '/resumes/wangwu.pdf', '440101198811101234', 0, 1);

-- 项目经理扩展表
CREATE TABLE `project_manager` (
  `user_id` bigint unsigned NOT NULL COMMENT '关联用户ID',
  `company_id` int unsigned NOT NULL COMMENT '关联企业ID',
  `position` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '职位名称',
  PRIMARY KEY (`user_id`),
  KEY `company_id` (`company_id`),
  CONSTRAINT `project_manager_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `project_manager_ibfk_2` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目经理扩展表';

-- 插入项目经理示例数据
INSERT INTO `project_manager` (`user_id`, `company_id`, `position`) VALUES
(4, 2, '高级项目经理'),
(5, 1, '技术项目经理');

-- 工种分类表
CREATE TABLE `construction_trade` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '工种ID',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '工种名称',
  `category` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '工种类别',
  `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '工种描述',
  `required_certificates` json DEFAULT NULL COMMENT '所需证书(JSON格式)',
  `required_skills` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所需技能',
  `avg_salary_range` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '平均薪资范围',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name_category` (`name`,`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='建筑工种分类表';

-- 插入工种示例数据
INSERT INTO `construction_trade` (`id`, `name`, `category`, `description`, `required_certificates`, `required_skills`, `avg_salary_range`, `create_time`) VALUES
(1, '木工', '装修类', '从事建筑木工工作，包括模板安装、支撑、拆除等', '["木工证"]', '熟练使用各类木工工具，能够独立完成木工施工任务', '300-450元/天', '2024-01-20 10:00:00'),
(2, '瓦工', '装修类', '从事泥瓦工作，包括砖砌体、抹灰、贴砖等', '["瓦工证"]', '熟练掌握砌筑、抹灰、贴砖等技术', '350-500元/天', '2024-01-20 10:05:00'),
(3, '电工', '设备安装类', '从事建筑电气安装、维修工作', '["电工操作证"]', '了解电气图纸，熟悉电气设备安装、线路敷设等工作', '400-550元/天', '2024-01-20 10:10:00'),
(4, '钢筋工', '主体结构类', '从事钢筋加工、绑扎、预埋等工作', '["钢筋工证"]', '熟练掌握钢筋绑扎技术，会看施工图纸', '350-500元/天', '2024-01-20 10:15:00'),
(5, '混凝土工', '主体结构类', '从事混凝土浇筑、振捣、养护等工作', '["混凝土工证"]', '熟悉混凝土配比、浇筑、振捣等施工工艺', '300-450元/天', '2024-01-20 10:20:00');

-- 求职者工种技能表
CREATE TABLE `seeker_trade_skill` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint unsigned NOT NULL COMMENT '求职者ID',
  `trade_id` int unsigned NOT NULL COMMENT '工种ID',
  `skill_level` enum('junior','intermediate','senior','expert') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'junior' COMMENT '技能等级',
  `work_years` tinyint unsigned DEFAULT NULL COMMENT '该工种工作年限',
  `certificate_urls` json DEFAULT NULL COMMENT '相关证书URL(JSON格式)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_trade` (`user_id`,`trade_id`),
  KEY `trade_id` (`trade_id`),
  CONSTRAINT `seeker_trade_skill_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `job_seeker` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `seeker_trade_skill_ibfk_2` FOREIGN KEY (`trade_id`) REFERENCES `construction_trade` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='求职者工种技能表';

-- 插入求职者工种技能示例数据
INSERT INTO `seeker_trade_skill` (`id`, `user_id`, `trade_id`, `skill_level`, `work_years`, `certificate_urls`, `create_time`) VALUES
(1, 6, 1, 'senior', 8, '["http://example.com/certificates/zhangsan_wood.jpg"]', '2024-02-10 09:15:00'),
(2, 6, 2, 'intermediate', 3, '["http://example.com/certificates/zhangsan_tile.jpg"]', '2024-02-10 09:16:00'),
(3, 7, 3, 'senior', 5, '["http://example.com/certificates/lisi_electric.jpg"]', '2024-02-11 10:35:00'),
(4, 8, 4, 'expert', 7, '["http://example.com/certificates/wangwu_steel.jpg"]', '2024-02-12 16:00:00');

-- 建筑项目表
CREATE TABLE `construction_project` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '项目ID',
  `company_id` int unsigned NOT NULL COMMENT '所属企业ID',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目名称',
  `location` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目地点',
  `start_date` date NOT NULL COMMENT '项目开始日期',
  `end_date` date DEFAULT NULL COMMENT '项目计划结束日期',
  `status` enum('planning','recruiting','in_progress','completed','suspended') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'planning' COMMENT '项目状态',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '项目描述',
  `manager_id` bigint unsigned NOT NULL COMMENT '项目经理ID',
  `accommodation` tinyint(1) DEFAULT 0 COMMENT '是否提供住宿(1:是,0:否)',
  `meals` tinyint(1) DEFAULT 0 COMMENT '是否提供工餐(1:是,0:否)',
  `insurance` tinyint(1) DEFAULT 0 COMMENT '是否购买保险(1:是,0:否)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_company` (`company_id`),
  KEY `idx_manager` (`manager_id`),
  CONSTRAINT `project_company_fk` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `project_manager_fk` FOREIGN KEY (`manager_id`) REFERENCES `project_manager` (`user_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='建筑项目信息表';

-- 插入建筑项目示例数据
INSERT INTO `construction_project` (`id`, `company_id`, `name`, `location`, `start_date`, `end_date`, `status`, `description`, `manager_id`, `accommodation`, `meals`, `insurance`, `create_time`, `update_time`) VALUES
(1, 1, '阳光花园住宅小区', '湖南省长沙市岳麓区阳光大道123号', '2024-03-01', '2025-06-30', 'recruiting', '总建筑面积约8万平方米的住宅小区，包含12栋高层住宅楼及配套设施', 5, 1, 1, 1, '2024-02-15 14:00:00', '2024-02-15 14:00:00'),
(2, 2, '星河商业广场', '北京市海淀区中关村南大街15号', '2024-04-15', '2025-12-31', 'planning', '集商业、办公为一体的综合商业广场，总建筑面积约12万平方米', 4, 1, 0, 1, '2024-02-20 10:30:00', '2024-02-20 10:30:00'),
(3, 1, '城市中央公园改造项目', '湖南省长沙市天心区劳动西路299号', '2024-03-20', '2024-09-30', 'in_progress', '对现有公园进行改造升级，包括景观设计、园路铺装、绿化等工程', 5, 0, 1, 1, '2024-01-25 09:00:00', '2024-03-20 08:00:00');

-- 项目招工需求表
CREATE TABLE `project_labor_demand` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '需求ID',
  `project_id` bigint unsigned NOT NULL COMMENT '关联项目ID',
  `trade_id` int unsigned NOT NULL COMMENT '关联工种ID',
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '招工标题',
  `headcount` int unsigned NOT NULL DEFAULT 1 COMMENT '招工人数',
  `filled_count` int unsigned NOT NULL DEFAULT 0 COMMENT '已招人数',
  `daily_salary_min` decimal(10,2) NOT NULL COMMENT '日薪下限',
  `daily_salary_max` decimal(10,2) NOT NULL COMMENT '日薪上限',
  `work_hours_per_day` tinyint unsigned NOT NULL DEFAULT 8 COMMENT '每日工作时长',
  `payment_cycle` enum('daily','weekly','biweekly','monthly') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'monthly' COMMENT '结算周期',
  `required_exp` tinyint unsigned DEFAULT NULL COMMENT '要求工作年限',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '职位描述',
  `requirements` text COLLATE utf8mb4_unicode_ci COMMENT '岗位要求',
  `status` enum('open','closed','filled') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'open' COMMENT '招工状态',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `deadline` datetime DEFAULT NULL COMMENT '截止申请时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_project` (`project_id`),
  KEY `idx_trade` (`trade_id`),
  CONSTRAINT `labor_demand_project_fk` FOREIGN KEY (`project_id`) REFERENCES `construction_project` (`id`) ON DELETE CASCADE,
  CONSTRAINT `labor_demand_trade_fk` FOREIGN KEY (`trade_id`) REFERENCES `construction_trade` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目招工需求表';

-- 插入项目招工需求示例数据
INSERT INTO `project_labor_demand` (`id`, `project_id`, `trade_id`, `title`, `headcount`, `filled_count`, `daily_salary_min`, `daily_salary_max`, `work_hours_per_day`, `payment_cycle`, `required_exp`, `description`, `requirements`, `status`, `publish_time`, `deadline`, `create_time`, `update_time`) VALUES
(1, 1, 1, '阳光花园项目急招熟练木工10名', 10, 3, 350.00, 420.00, 8, 'biweekly', 3, '负责小区内部住宅的木门安装、木质吊顶等木工活', '有3年以上工作经验，持有木工证，能吃苦耐劳', 'open', '2024-02-16 09:00:00', '2024-03-20 18:00:00', '2024-02-16 09:00:00', '2024-02-25 16:30:00'),
(2, 1, 2, '阳光花园项目招聘瓦工师傅8名', 8, 2, 380.00, 450.00, 9, 'biweekly', 2, '负责小区内瓷砖铺贴、墙面抹灰等工作', '2年以上工作经验，有瓦工证者优先', 'open', '2024-02-16 09:05:00', '2024-03-20 18:00:00', '2024-02-16 09:05:00', '2024-02-23 14:20:00'),
(3, 2, 3, '星河广场项目招聘电工5名', 5, 0, 400.00, 500.00, 8, 'monthly', 3, '负责商业广场的强弱电安装工程', '必须持有电工证，有大型商业项目经验者优先', 'open', '2024-02-21 10:00:00', '2024-04-10 18:00:00', '2024-02-21 10:00:00', '2024-02-21 10:00:00'),
(4, 3, 4, '中央公园项目招聘钢筋工3名', 3, 3, 380.00, 450.00, 8, 'weekly', 2, '负责公园内部构筑物的钢筋绑扎工作', '熟练掌握钢筋绑扎技术，有园林景观项目经验者优先', 'filled', '2024-02-05 11:00:00', '2024-03-10 18:00:00', '2024-02-05 11:00:00', '2024-03-05 16:00:00');

-- 求职申请表
CREATE TABLE `labor_application` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '申请ID',
  `demand_id` bigint unsigned NOT NULL COMMENT '关联招工需求ID',
  `seeker_id` bigint unsigned NOT NULL COMMENT '申请人ID',
  `expect_salary` decimal(10,2) DEFAULT NULL COMMENT '期望日薪',
  `status` enum('pending','reviewing','interviewed','offered','rejected','accepted','withdrawn') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '申请状态',
  `notes` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '申请备注',
  `apply_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `interview_time` datetime DEFAULT NULL COMMENT '面试时间',
  `interview_location` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '面试地点',
  `feedback` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '面试反馈',
  `offer_salary` decimal(10,2) DEFAULT NULL COMMENT '录用薪资(日薪)',
  `offer_time` datetime DEFAULT NULL COMMENT '录用时间',
  `start_date` date DEFAULT NULL COMMENT '开始工作日期',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_demand_seeker` (`demand_id`,`seeker_id`),
  KEY `idx_seeker` (`seeker_id`),
  CONSTRAINT `application_demand_fk` FOREIGN KEY (`demand_id`) REFERENCES `project_labor_demand` (`id`) ON DELETE CASCADE,
  CONSTRAINT `application_seeker_fk` FOREIGN KEY (`seeker_id`) REFERENCES `job_seeker` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='求职申请表';

-- 插入求职申请示例数据
INSERT INTO `labor_application` (`id`, `demand_id`, `seeker_id`, `expect_salary`, `status`, `notes`, `apply_time`, `interview_time`, `interview_location`, `feedback`, `offer_salary`, `offer_time`, `start_date`, `update_time`) VALUES
(1, 1, 6, 400.00, 'accepted', '擅长木工，经验丰富', '2024-02-17 14:30:00', '2024-02-20 10:00:00', '湖南省长沙市岳麓区阳光大道123号项目部', '技能熟练，态度好，符合要求', 400.00, '2024-02-20 15:00:00', '2024-03-01', '2024-02-20 15:00:00'),
(2, 3, 7, 450.00, 'interviewed', '电工经验丰富，持有高级电工证', '2024-02-22 09:15:00', '2024-02-25 14:00:00', '北京市海淀区中关村南大街15号工地办公室', '技术专业，经验丰富，待确认录用', NULL, NULL, NULL, '2024-02-25 16:00:00'),
(3, 4, 8, 430.00, 'accepted', '钢筋工经验丰富，希望尽快入职', '2024-02-10 08:45:00', '2024-02-12 13:30:00', '湖南省长沙市天心区劳动西路299号现场', '技术娴熟，经验丰富', 450.00, '2024-02-12 16:00:00', '2024-03-20', '2024-02-12 16:00:00');

-- 工人聘用记录表
CREATE TABLE `labor_employment` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '聘用记录ID',
  `project_id` bigint unsigned NOT NULL COMMENT '关联项目ID',
  `demand_id` bigint unsigned NOT NULL COMMENT '关联招工需求ID',
  `worker_id` bigint unsigned NOT NULL COMMENT '工人用户ID',
  `daily_salary` decimal(10,2) NOT NULL COMMENT '日薪',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date DEFAULT NULL COMMENT '结束日期',
  `status` enum('active','completed','terminated') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'active' COMMENT '聘用状态',
  `attendance_days` int unsigned DEFAULT 0 COMMENT '考勤出勤天数',
  `total_salary` decimal(12,2) DEFAULT 0.00 COMMENT '累计薪资',
  `last_payment_date` date DEFAULT NULL COMMENT '最近结算日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_worker` (`project_id`,`worker_id`),
  KEY `idx_demand` (`demand_id`),
  KEY `idx_worker` (`worker_id`),
  CONSTRAINT `employment_project_fk` FOREIGN KEY (`project_id`) REFERENCES `construction_project` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `employment_demand_fk` FOREIGN KEY (`demand_id`) REFERENCES `project_labor_demand` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `employment_worker_fk` FOREIGN KEY (`worker_id`) REFERENCES `job_seeker` (`user_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工人聘用记录表';

-- 插入工人聘用记录示例数据
INSERT INTO `labor_employment` (`id`, `project_id`, `demand_id`, `worker_id`, `daily_salary`, `start_date`, `end_date`, `status`, `attendance_days`, `total_salary`, `last_payment_date`, `create_time`, `update_time`) VALUES
(1, 1, 1, 6, 400.00, '2024-03-01', NULL, 'active', 30, 12000.00, '2024-03-31', '2024-02-28 16:00:00', '2024-03-31 18:00:00'),
(2, 3, 4, 8, 450.00, '2024-03-20', NULL, 'active', 15, 6750.00, '2024-04-05', '2024-03-19 15:30:00', '2024-04-05 18:00:00');

-- 工人考勤表
CREATE TABLE `labor_attendance` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '考勤ID',
  `employment_id` bigint unsigned NOT NULL COMMENT '关联聘用记录ID',
  `attendance_date` date NOT NULL COMMENT '考勤日期',
  `status` enum('present','absent','late','leave') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '考勤状态',
  `work_hours` decimal(4,1) DEFAULT NULL COMMENT '工作时长',
  `remarks` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `checked_by` bigint unsigned DEFAULT NULL COMMENT '记录人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employment_date` (`employment_id`,`attendance_date`),
  KEY `idx_checked_by` (`checked_by`),
  CONSTRAINT `attendance_employment_fk` FOREIGN KEY (`employment_id`) REFERENCES `labor_employment` (`id`) ON DELETE CASCADE,
  CONSTRAINT `attendance_checker_fk` FOREIGN KEY (`checked_by`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工人考勤表';

-- 插入工人考勤示例数据
INSERT INTO `labor_attendance` (`id`, `employment_id`, `attendance_date`, `status`, `work_hours`, `remarks`, `checked_by`, `create_time`) VALUES
(1, 1, '2024-03-01', 'present', 8.0, '正常出勤', 5, '2024-03-01 18:00:00'),
(2, 1, '2024-03-02', 'present', 8.0, '正常出勤', 5, '2024-03-02 18:00:00'),
(3, 1, '2024-03-03', 'present', 8.0, '正常出勤', 5, '2024-03-03 18:00:00'),
(4, 1, '2024-03-04', 'late', 7.5, '迟到30分钟', 5, '2024-03-04 18:00:00'),
(5, 1, '2024-03-05', 'absent', 0.0, '请假一天', 5, '2024-03-05 18:00:00'),
(6, 2, '2024-03-20', 'present', 8.0, '正常出勤', 5, '2024-03-20 18:00:00'),
(7, 2, '2024-03-21', 'present', 8.0, '正常出勤', 5, '2024-03-21 18:00:00');

-- 工资结算记录表
CREATE TABLE `salary_payment` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '结算ID',
  `employment_id` bigint unsigned NOT NULL COMMENT '关联聘用记录ID',
  `payment_date` date NOT NULL COMMENT '结算日期',
  `start_date` date NOT NULL COMMENT '结算开始日期',
  `end_date` date NOT NULL COMMENT '结算结束日期',
  `work_days` decimal(5,1) NOT NULL COMMENT '工作天数',
  `daily_salary` decimal(10,2) NOT NULL COMMENT '日薪',
  `amount` decimal(12,2) NOT NULL COMMENT '结算金额',
  `deduction` decimal(10,2) DEFAULT 0.00 COMMENT '扣款金额',
  `deduction_reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扣款原因',
  `actual_amount` decimal(12,2) NOT NULL COMMENT '实发金额',
  `payment_method` enum('cash','bank_transfer','alipay','wechat') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付方式',
  `payment_status` enum('pending','paid','failed') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '支付状态',
  `transaction_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交易流水号',
  `remarks` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `paid_by` bigint unsigned DEFAULT NULL COMMENT '支付操作人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_employment` (`employment_id`),
  KEY `idx_paid_by` (`paid_by`),
  CONSTRAINT `payment_employment_fk` FOREIGN KEY (`employment_id`) REFERENCES `labor_employment` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `payment_payer_fk` FOREIGN KEY (`paid_by`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工资结算记录表';

-- 插入工资结算记录示例数据
INSERT INTO `salary_payment` (`id`, `employment_id`, `payment_date`, `start_date`, `end_date`, `work_days`, `daily_salary`, `amount`, `deduction`, `deduction_reason`, `actual_amount`, `payment_method`, `payment_status`, `transaction_id`, `remarks`, `paid_by`, `create_time`, `update_time`) VALUES
(1, 1, '2024-03-15', '2024-03-01', '2024-03-15', 13.5, 400.00, 5400.00, 200.00, '迟到1次，旷工1天', 5200.00, 'bank_transfer', 'paid', 'BK202403151234', '3月上半月工资', 5, '2024-03-15 15:00:00', '2024-03-15 15:30:00'),
(2, 1, '2024-03-31', '2024-03-16', '2024-03-31', 16.0, 400.00, 6400.00, 0.00, NULL, 6400.00, 'bank_transfer', 'paid', 'BK202403311235', '3月下半月工资', 5, '2024-03-31 16:00:00', '2024-03-31 16:30:00'),
(3, 2, '2024-04-05', '2024-03-20', '2024-04-05', 15.0, 450.00, 6750.00, 0.00, NULL, 6750.00, 'alipay', 'paid', 'ZFB202404051236', '首次工资结算', 5, '2024-04-05 14:00:00', '2024-04-05 14:30:00');

-- 投诉反馈表
CREATE TABLE `complaint` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '投诉ID',
  `user_id` bigint unsigned NOT NULL COMMENT '投诉人ID',
  `project_id` bigint unsigned DEFAULT NULL COMMENT '关联项目ID',
  `employment_id` bigint unsigned DEFAULT NULL COMMENT '关联聘用记录ID',
  `type` enum('salary','safety','contract','other') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '投诉类型',
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '投诉标题',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '投诉内容',
  `evidence_urls` json DEFAULT NULL COMMENT '证据材料URL(JSON格式)',
  `status` enum('pending','processing','resolved','rejected') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '处理状态',
  `handler_id` bigint unsigned DEFAULT NULL COMMENT '处理人ID',
  `handling_comments` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '处理意见',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`),
  KEY `idx_project` (`project_id`),
  KEY `idx_employment` (`employment_id`),
  KEY `idx_handler` (`handler_id`),
  CONSTRAINT `complaint_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `complaint_project_fk` FOREIGN KEY (`project_id`) REFERENCES `construction_project` (`id`) ON DELETE SET NULL,
  CONSTRAINT `complaint_employment_fk` FOREIGN KEY (`employment_id`) REFERENCES `labor_employment` (`id`) ON DELETE SET NULL,
  CONSTRAINT `complaint_handler_fk` FOREIGN KEY (`handler_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投诉反馈表';

-- 插入投诉反馈示例数据
INSERT INTO `complaint` (`id`, `user_id`, `project_id`, `employment_id`, `type`, `title`, `content`, `evidence_urls`, `status`, `handler_id`, `handling_comments`, `create_time`, `update_time`) VALUES
(1, 6, 1, 1, 'salary', '3月工资结算问题', '3月15日的工资结算有误，少计算了一天的工作时间', '["http://example.com/evidence/zhangsan_salary_record.jpg"]', 'resolved', 2, '经核实，确实少算一天，已补发工资', '2024-03-16 09:30:00', '2024-03-17 14:00:00'),
(2, 8, 3, 2, 'safety', '工地安全设施不完善', '工地高空作业区域缺少安全网和防护栏', '["http://example.com/evidence/safety_issue1.jpg", "http://example.com/evidence/safety_issue2.jpg"]', 'processing', 1, '已安排安全专员检查整改', '2024-03-25 11:20:00', '2024-03-26 10:00:00');