CREATE DATABASE IF NOT EXISTS community_care DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE community_care;

CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    role VARCHAR(20) NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 人员表
CREATE TABLE IF NOT EXISTS persons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '姓名',
    gender VARCHAR(10) COMMENT '性别',
    age INT COMMENT '年龄',
    id_card VARCHAR(18) COMMENT '身份证号',
    phone VARCHAR(20) COMMENT '联系电话',
    category VARCHAR(50) COMMENT '所属分类',
    address VARCHAR(255) COMMENT '地址',
    health_status VARCHAR(100) COMMENT '健康状态',
    notes TEXT COMMENT '备注信息',
    remark TEXT COMMENT '备注',
    created_at DATETIME COMMENT '创建时间',
    created_by BIGINT COMMENT '创建人ID',
    creator_id BIGINT COMMENT '创建者ID',
    updated_at DATETIME COMMENT '更新时间',
    updated_by BIGINT COMMENT '更新人ID',
    create_time DATETIME COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    INDEX idx_created_by (created_by),
    INDEX idx_creator_id (creator_id),
    INDEX idx_name (name),
    INDEX idx_phone (phone),
    INDEX idx_id_card (id_card)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人员信息表'; 