-- 体彩投注记录管理系统数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS sports_lottery DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sports_lottery;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 推荐记录表
CREATE TABLE IF NOT EXISTS `recommend_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `recommend_date` DATE NOT NULL COMMENT '推荐日期',
    `match_desc` VARCHAR(255) NOT NULL COMMENT '比赛信息',
    `recommendation` VARCHAR(100) NOT NULL COMMENT '推荐结果',
    `recommender` VARCHAR(100) NOT NULL COMMENT '推荐人',
    `result` VARCHAR(10) DEFAULT NULL COMMENT '最终结果（1：成功 0：失败 2：比赛中断）',
    `create_by` VARCHAR(255) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_by` VARCHAR(255) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_recommend_user_date` (`user_id`, `recommend_date`),
    CONSTRAINT `fk_recommend_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='推荐记录表';

-- 投注记录表
CREATE TABLE IF NOT EXISTS `betting_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `bet_date` DATE NOT NULL COMMENT '投注日期',
    `sport_type` VARCHAR(255) DEFAULT NULL COMMENT '运动类型：football-足球，basketball-篮球',
    `league` VARCHAR(100) NOT NULL COMMENT '联赛名称',
    `home_team` VARCHAR(100) NOT NULL COMMENT '主队',
    `away_team` VARCHAR(100) NOT NULL COMMENT '客队',
    `bet_type` VARCHAR(50) NOT NULL COMMENT '投注类型',
    `bet_content` VARCHAR(200) NOT NULL COMMENT '投注内容',
    `odds` DECIMAL(10,2) NOT NULL COMMENT '赔率',
    `bet_amount` DECIMAL(10,2) NOT NULL COMMENT '投注金额',
    `result` TINYINT DEFAULT NULL COMMENT '投注结果：1-中奖，2-未中奖',
    `win_amount` DECIMAL(10,2) DEFAULT NULL COMMENT '实际奖金',
    `profit_loss` DECIMAL(10,2) DEFAULT NULL COMMENT '盈亏金额',
    `notes` TEXT COMMENT '备注',
    `create_by` VARCHAR(255) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(255) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_match_date` (`bet_date`),
    KEY `idx_league` (`league`),
    KEY `idx_bet_type` (`bet_type`),
    KEY `idx_result` (`result`),
    KEY `idx_create_time` (`create_time`),
    CONSTRAINT `fk_betting_record_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投注记录表';

-- 字典类型表
CREATE TABLE IF NOT EXISTS `sys_dict_type` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `dict_type` VARCHAR(100) NOT NULL COMMENT '字典类型编码',
    `dict_name` VARCHAR(100) NOT NULL COMMENT '字典类型名称',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-停用',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典类型表';

-- 字典数据表
CREATE TABLE IF NOT EXISTS `sys_dict_data` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `dict_type` VARCHAR(100) NOT NULL COMMENT '字典类型编码',
    `dict_code` VARCHAR(50) NOT NULL COMMENT '字典值',
    `dict_label` VARCHAR(100) NOT NULL COMMENT '字典标签',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-停用',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_type_code` (`dict_type`, `dict_code`),
    KEY `idx_dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典数据表';

-- 插入测试用户数据
INSERT INTO `user` (`username`, `password`, `email`, `nickname`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'admin@example.com', '管理员', 1),
('testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'test@example.com', '测试用户', 1)
ON DUPLICATE KEY UPDATE `username` = VALUES(`username`);

-- 插入字典类型数据
INSERT INTO `sys_dict_type` (`dict_type`, `dict_name`, `status`, `remark`) VALUES
('recommend_result', '推荐结果', 1, '推荐记录结果：成功、失败、比赛中断'),
('football_league', '足球联赛', 1, '足球联赛字典')
ON DUPLICATE KEY UPDATE `dict_name` = VALUES(`dict_name`);

-- 插入推荐结果字典数据
INSERT INTO `sys_dict_data` (`dict_type`, `dict_code`, `dict_label`, `sort_order`, `status`, `remark`) VALUES
('recommend_result', '1', '成功', 1, 1, '推荐成功'),
('recommend_result', '0', '失败', 2, 1, '推荐失败'),
('recommend_result', '2', '比赛中断', 3, 1, '比赛中断'),
('football_league', 'EPL', '英超', 1, 1, 'England Premier League'),
('football_league', 'LALIGA', '西甲', 2, 1, 'La Liga'),
('football_league', 'SERIE_A', '意甲', 3, 1, 'Serie A'),
('football_league', 'BUNDESLIGA', '德甲', 4, 1, 'Bundesliga'),
('football_league', 'LIGUE_1', '法甲', 5, 1, 'Ligue 1'),
('football_league', 'EREDIVISIE', '荷甲', 6, 1, 'Netherlands'),
('football_league', 'PRIMEIRA', '葡超', 7, 1, 'Portugal'),
('football_league', 'SUPER_LIG', '土超', 8, 1, 'Turkey'),
('football_league', 'BELGIUM', '比甲', 9, 1, 'Belgium'),
('football_league', 'SWISS', '瑞士超', 10, 1, 'Switzerland'),
('football_league', 'AUSTRIA', '奥甲', 11, 1, 'Austria'),
('football_league', 'SCOTLAND', '苏超', 12, 1, 'Scotland'),
('football_league', 'DENMARK', '丹超', 13, 1, 'Denmark'),
('football_league', 'NORWAY', '挪超', 14, 1, 'Norway'),
('football_league', 'SWEDEN', '瑞典超', 15, 1, 'Sweden'),
('football_league', 'POLAND', '波兰甲', 16, 1, 'Poland'),
('football_league', 'CZECH', '捷甲', 17, 1, 'Czech Republic'),
('football_league', 'CSL', '中超', 18, 1, 'China'),
('football_league', 'J1', '日职联', 19, 1, 'Japan'),
('football_league', 'K_LEAGUE', '韩K联', 20, 1, 'Korea'),
('football_league', 'A_LEAGUE', '澳超', 21, 1, 'Australia'),
('football_league', 'MLS', '美职联', 22, 1, 'USA'),
('football_league', 'LIGA_MX', '墨超', 23, 1, 'Mexico'),
('football_league', 'BRA_SERIE_A', '巴甲', 24, 1, 'Brazil'),
('football_league', 'ARG_PRIMERA', '阿甲', 25, 1, 'Argentina'),
('football_league', 'UEFA_CL', '欧冠', 26, 1, 'UEFA Champions League'),
('football_league', 'UEFA_EL', '欧联杯', 27, 1, 'UEFA Europa League'),
('football_league', 'UEFA_ECL', '欧协联', 28, 1, 'UEFA Conference League'),
('football_league', 'WORLD_CUP', '世界杯', 29, 1, 'FIFA World Cup'),
('football_league', 'EURO_CUP', '欧洲杯', 30, 1, 'UEFA European Championship'),
('football_league', 'COPA_AMERICA', '美洲杯', 31, 1, 'Copa America'),
('football_league', 'AFCON', '非洲杯', 32, 1, 'Africa Cup of Nations'),
('football_league', 'ASIAN_CUP', '亚洲杯', 33, 1, 'AFC Asian Cup')
ON DUPLICATE KEY UPDATE `dict_label` = VALUES(`dict_label`), `sort_order` = VALUES(`sort_order`);

-- 插入测试投注记录数据（result：1-中奖，2-未中奖）
INSERT INTO `betting_record` (`user_id`, `bet_date`, `sport_type`, `league`, `home_team`, `away_team`, `bet_type`, `bet_content`, `odds`, `bet_amount`, `result`, `win_amount`, `profit_loss`, `notes`, `create_by`) VALUES
(1, '2024-01-15', 'football', '英超', '曼城', '利物浦', '胜负', '主胜', 2.10, 100.00, 1, 210.00, 110.00, '看好曼城主场优势', '1'),
(1, '2024-01-16', 'football', '西甲', '皇马', '巴萨', '让球', '主队-1', 1.85, 200.00, 2, 0.00, -200.00, '国家德比，激烈对抗', '1'),
(1, '2024-01-17', 'football', '德甲', '拜仁', '多特', '大小球', '大2.5球', 1.75, 150.00, 1, 262.50, 112.50, '两队攻击力强', '1'),
(1, '2024-01-18', 'football', '意甲', '尤文', '米兰', '胜负', '客胜', 3.20, 80.00, 2, 0.00, -80.00, '平局收场', '1'),
(1, '2024-01-19', 'football', '法甲', '巴黎', '马赛', '让球', '主队-1.5', 2.05, 120.00, 1, 246.00, 126.00, '巴黎实力占优', '1')
ON DUPLICATE KEY UPDATE `id` = VALUES(`id`);
