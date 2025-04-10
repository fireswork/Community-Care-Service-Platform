CREATE TABLE IF NOT EXISTS `record` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `person_id` bigint(20) NOT NULL COMMENT '关怀对象ID',
    `type` varchar(50) NOT NULL COMMENT '关怀类型',
    `care_time` datetime NOT NULL COMMENT '关怀时间',
    `content` text NOT NULL COMMENT '关怀内容',
    `remark` text COMMENT '备注',
    `created_by` bigint(20) NOT NULL COMMENT '创建人ID',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `updated_by` bigint(20) NOT NULL COMMENT '更新人ID',
    `updated_time` datetime NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_person_id` (`person_id`),
    KEY `idx_type` (`type`),
    KEY `idx_care_time` (`care_time`),
    CONSTRAINT `fk_record_person` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='关怀记录表'; 