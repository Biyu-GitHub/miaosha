-- 用户信息表
CREATE TABLE miaosha_user (
	id BIGINT ( 20 ) NOT NULL COMMENT '用户ID，手机号码',
	`nickname` VARCHAR ( 255 ) NOT NULL COMMENT '昵称',
	`password` VARCHAR ( 32 ) DEFAULT NULL COMMENT 'MD5（MD5（pass明文+固定salt） + salt）',
	`salt` VARCHAR ( 10 ) DEFAULT NULL,
	`head` VARCHAR ( 128 ) DEFAULT NULL COMMENT '头像，云存储的id',
	`register_data` datetime DEFAULT NULL COMMENT '注册时间',
	`last_login_data` datetime DEFAULT NULL COMMENT '上次登录时间',
	`login_count` INT ( 11 ) DEFAULT 0 COMMENT '登录次数',
PRIMARY KEY ( `id` )
) ENGINE = INNODB DEFAULT charset = utf8mb4;