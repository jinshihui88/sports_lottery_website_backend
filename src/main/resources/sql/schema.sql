-- 体彩投注记录管理系统数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS sports_lottery DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sports_lottery;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `email` VARCHAR(100) NOT NULL COMMENT '邮箱',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `avatar` VARCHAR(255) COMMENT '头像URL',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `last_login_time` DATETIME COMMENT '最后登录时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 推荐记录表
CREATE TABLE IF NOT EXISTS recommend_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    recommend_date DATE NOT NULL,
    match_desc VARCHAR(255) NOT NULL,
    recommendation VARCHAR(100) NOT NULL,
    recommender VARCHAR(100) NOT NULL,
    result VARCHAR(10) NULL,
    amount DECIMAL(10,2) NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    deleted TINYINT NOT NULL DEFAULT 0,
    INDEX idx_recommend_user_date (user_id, recommend_date),
    CONSTRAINT fk_recommend_user FOREIGN KEY (user_id) REFERENCES user(id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 投注记录表
CREATE TABLE IF NOT EXISTS `betting_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `match_date` DATE NOT NULL COMMENT '比赛日期',
    `league` VARCHAR(100) NOT NULL COMMENT '联赛名称',
    `home_team` VARCHAR(100) NOT NULL COMMENT '主队',
    `away_team` VARCHAR(100) NOT NULL COMMENT '客队',
    `bet_type` VARCHAR(50) NOT NULL COMMENT '投注类型',
    `bet_content` VARCHAR(200) NOT NULL COMMENT '投注内容',
    `odds` DECIMAL(10,2) NOT NULL COMMENT '赔率',
    `bet_amount` DECIMAL(10,2) NOT NULL COMMENT '投注金额',
    `result` VARCHAR(20) COMMENT '结果：WIN-赢，LOSE-输，DRAW-平',
    `win_amount` DECIMAL(10,2) COMMENT '盈利金额',
    `notes` TEXT COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_match_date` (`match_date`),
    KEY `idx_league` (`league`),
    KEY `idx_bet_type` (`bet_type`),
    KEY `idx_result` (`result`),
    KEY `idx_create_time` (`create_time`),
    CONSTRAINT `fk_betting_record_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投注记录表';

-- 插入测试用户数据
INSERT INTO `user` (`username`, `password`, `email`, `nickname`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'admin@example.com', '管理员', 1),
('testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'test@example.com', '测试用户', 1)
ON DUPLICATE KEY UPDATE `username` = VALUES(`username`);

-- 插入测试投注记录数据
INSERT INTO `betting_record` (`user_id`, `match_date`, `league`, `home_team`, `away_team`, `bet_type`, `bet_content`, `odds`, `bet_amount`, `result`, `win_amount`, `notes`) VALUES
(1, '2024-01-15', '英超', '曼城', '利物浦', '胜负', '主胜', 2.10, 100.00, 'WIN', 210.00, '看好曼城主场优势'),
(1, '2024-01-16', '西甲', '皇马', '巴萨', '让球', '主队-1', 1.85, 200.00, 'LOSE', 0.00, '国家德比，激烈对抗'),
(1, '2024-01-17', '德甲', '拜仁', '多特', '大小球', '大2.5球', 1.75, 150.00, 'WIN', 262.50, '两队攻击力强'),
(1, '2024-01-18', '意甲', '尤文', '米兰', '胜负', '客胜', 3.20, 80.00, 'DRAW', 0.00, '平局收场'),
(1, '2024-01-19', '法甲', '巴黎', '马赛', '让球', '主队-1.5', 2.05, 120.00, 'WIN', 246.00, '巴黎实力占优')
ON DUPLICATE KEY UPDATE `id` = VALUES(`id`);