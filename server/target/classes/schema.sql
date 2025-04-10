CREATE DATABASE IF NOT EXISTS community_care DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE community_care;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `password` varchar(100) NOT NULL COMMENT '密码',
    `name` varchar(50) NOT NULL COMMENT '姓名',
    `phone` varchar(20) NOT NULL COMMENT '手机号',
    `role` varchar(20) DEFAULT 'USER' COMMENT '角色',
    `status` tinyint(4) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 分类表
CREATE TABLE IF NOT EXISTS `category` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` varchar(50) NOT NULL COMMENT '分类名称',
    `description` varchar(255) DEFAULT NULL COMMENT '分类描述',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- 人员表
CREATE TABLE IF NOT EXISTS `person` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` varchar(50) NOT NULL COMMENT '姓名',
    `gender` varchar(10) NOT NULL COMMENT '性别',
    `age` int(3) NOT NULL COMMENT '年龄',
    `id_card` varchar(18) NOT NULL COMMENT '身份证号',
    `phone` varchar(20) NOT NULL COMMENT '联系电话',
    `category` varchar(20) NOT NULL COMMENT '所属分类',
    `address` varchar(255) NOT NULL COMMENT '家庭住址',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `creator_id` bigint(20) NOT NULL COMMENT '创建者ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_creator` (`creator_id`),
    INDEX `idx_id_card` (`id_card`),
    INDEX `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人员基础信息表';

-- 创建关怀记录表
CREATE TABLE IF NOT EXISTS `record` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `person_id` bigint(20) NOT NULL COMMENT '关怀对象ID',
    `type` varchar(50) NOT NULL COMMENT '关怀类型',
    `care_time` datetime NOT NULL COMMENT '关怀时间',
    `content` text NOT NULL COMMENT '关怀内容',
    `remark` text COMMENT '备注',
    `created_by` bigint(20) NOT NULL COMMENT '创建人ID',
    `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` bigint(20) NOT NULL COMMENT '更新人ID',
    `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_person_id` (`person_id`),
    KEY `idx_type` (`type`),
    KEY `idx_care_time` (`care_time`),
    CONSTRAINT `fk_record_person` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='关怀记录表';

-- 插入管理员用户（密码：123456，使用BCrypt加密）
INSERT INTO `user` (`username`, `password`, `name`, `phone`, `role`, `status`)
VALUES ('admin', '$2a$10$F9U1ITY4/W3G.LfQejlTNuMSTvueYm0JD14UjwKdHpL4dnf7MpBBy', '管理员', '13800138000', 'ADMIN', 1)
ON DUPLICATE KEY UPDATE `update_time` = CURRENT_TIMESTAMP;

-- DROP TABLE IF EXISTS record;
-- DROP TABLE IF EXISTS person;
-- DROP TABLE IF EXISTS category;
-- DROP TABLE IF EXISTS user;