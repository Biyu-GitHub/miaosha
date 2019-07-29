DROP TABLE IF EXISTS `miaosha_goods`;
CREATE TABLE `miaosha_goods` (
	`id` BIGINT ( 20 ) NOT NULL AUTO_INCREMENT COMMENT '秒杀的商品表',
	`goods_id` BIGINT ( 20 ) DEFAULT NULL COMMENT '商品Id',
	`miaosha_price` DECIMAL ( 10, 2 ) DEFAULT '0.00' COMMENT '秒杀价',
	`stock_count` INT ( 11 ) DEFAULT NULL COMMENT '库存数量',
	`start_date` datetime DEFAULT NULL COMMENT '秒杀开始时间',
	`end_date` datetime DEFAULT NULL COMMENT '秒杀结束时间',
PRIMARY KEY ( `id` )
) ENGINE = INNODB AUTO_INCREMENT = 5 DEFAULT CHARSET = utf8mb4;


INSERT INTO `miaosha_goods` VALUES ('1', '1', '0.01', '9', '2017-12-04 21:51:23', '2017-12-31 21:51:27');
INSERT INTO `miaosha_goods` VALUES ('2', '2', '0.01', '9', '2017-12-04 21:40:14', '2017-12-31 14:00:24');
INSERT INTO `miaosha_goods` VALUES ('3', '3', '0.01', '9', '2017-12-04 21:40:14', '2017-12-31 14:00:24');
INSERT INTO `miaosha_goods` VALUES ('4', '4', '0.01', '9', '2017-12-04 21:40:14', '2017-12-31 14:00:24');
