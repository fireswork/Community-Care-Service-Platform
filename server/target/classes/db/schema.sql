-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
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

-- 人员基础信息表
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

-- 插入管理员用户（密码：admin123，使用BCrypt加密）
INSERT INTO `user` (`username`, `password`, `name`, `phone`, `role`, `status`)
VALUES ('admin', '$2a$10$X/hX6J8eP9yWjqWF4TTGPuXGJYe.3v1YkgGHxFGZLOXFHUkEa.IHi', '管理员', '13800138000', 'ADMIN', 1)
ON DUPLICATE KEY UPDATE `update_time` = CURRENT_TIMESTAMP;

-- 移除了预设的分类数据 

DROP TABLE IF EXISTS record;
DROP TABLE IF EXISTS persons;
DROP TABLE IF EXISTS user; 