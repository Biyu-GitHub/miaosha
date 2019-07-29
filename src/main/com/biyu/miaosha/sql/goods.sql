DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
	`id` BIGINT ( 20 ) NOT NULL auto_increment COMMENT '商品ID',
	`goods_name` VARCHAR ( 16 ) DEFAULT NULL COMMENT '商品名称',
	`goods_title` VARCHAR ( 64 ) DEFAULT NULL COMMENT '商品标题',
	`goods_img` VARCHAR ( 64 ) DEFAULT NULL COMMENT '商品图片',
	`goods_detail` LONGTEXT COMMENT '商品详情介绍',
	`goods_price` DECIMAL ( 10, 2 ) DEFAULT 0.00 COMMENT '商品单价',
	`goods_stock` INT ( 11 ) DEFAULT 0 COMMENT '商品库存，-1代表没有限制',
PRIMARY KEY ( `id` ) 
) ENGINE = INNODB auto_increment = 3 DEFAULT charset utf8mb4;


INSERT INTO `goods` VALUES ('1', 'iphoneX', 'Apple iPhone X (A1865) 64GB 银色 移动联通电信4G手机', '/img/iphonex.png', 'Apple iPhone X (A1865) 64GB 银色 移动联通电信4G手机', '8765.00', '10000');
INSERT INTO `goods` VALUES ('2', '华为Meta10', '华为 Mate 9 4GB+32GB版 月光银 移动联通电信4G手机 双卡双待', '/img/meta10.png', '华为 Mate 9 4GB+32GB版 月光银 移动联通电信4G手机 双卡双待', '3212.00', '-1');


INSERT INTO `goods` VALUES ('3', 'iphone8', 'Apple iPhone 8 (A1865) 64GB 银色 移动联通电信4G手机', '/img/iphone8.png', 'Apple iPhone 8 (A1865) 64GB 银色 移动联通电信4G手机', '5589.00', '10000');
INSERT INTO `goods` VALUES ('4', '小米6', '小米6 4GB+32GB版 月光银 移动联通电信4G手机 双卡双待', '/img/mi6.png', '小米6 4GB+32GB版 月光银 移动联通电信4G手机 双卡双待', '3212.00', '10000');
