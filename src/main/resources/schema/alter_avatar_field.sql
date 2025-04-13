-- 修改user表中的avatar字段类型为TEXT，以支持存储更长的URL
ALTER TABLE `user` MODIFY `avatar` TEXT COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像URL';
