# Host: 192.168.10.240  (Version 5.6.31)
# Date: 2016-08-24 16:37:42
# Generator: MySQL-Front 5.3  (Build 8.5)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "act_appearance"
#

DROP TABLE IF EXISTS `act_appearance`;
CREATE TABLE `act_appearance` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `realname` varchar(255) DEFAULT NULL,
  `id_card_image1` varchar(255) DEFAULT NULL,
  `id_card_image2` varchar(255) DEFAULT NULL,
  `image1` varchar(255) DEFAULT NULL,
  `image2` varchar(255) DEFAULT NULL,
  `image3` varchar(255) DEFAULT NULL,
  `image4` varchar(255) DEFAULT NULL,
  `image5` varchar(255) DEFAULT NULL,
  `image6` varchar(255) DEFAULT NULL,
  `confirm_status` int(2) DEFAULT NULL,
  `confirm_remark` varchar(255) DEFAULT NULL,
  `confirmed_time` datetime DEFAULT NULL,
  `applied_time` datetime DEFAULT NULL,
  `id_card_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "act_appearance"
#


#
# Structure for table "act_report"
#

DROP TABLE IF EXISTS `act_report`;
CREATE TABLE `act_report` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `text` varchar(2000) DEFAULT NULL,
  `image1` varchar(255) DEFAULT NULL,
  `image2` varchar(255) DEFAULT NULL,
  `image3` varchar(255) DEFAULT NULL,
  `image4` varchar(255) DEFAULT NULL,
  `image5` varchar(255) DEFAULT NULL,
  `image6` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "act_report"
#


#
# Structure for table "adm_admin"
#

DROP TABLE IF EXISTS `adm_admin`;
CREATE TABLE `adm_admin` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `role_names` varchar(255) DEFAULT NULL,
  `site_id` int(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

#
# Data for table "adm_admin"
#

INSERT INTO `adm_admin` VALUES (1,1,'超级管理员',NULL);

#
# Structure for table "adm_admin_role"
#

DROP TABLE IF EXISTS `adm_admin_role`;
CREATE TABLE `adm_admin_role` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `admin_id` int(20) DEFAULT NULL,
  `role_id` int(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

#
# Data for table "adm_admin_role"
#

INSERT INTO `adm_admin_role` VALUES (3,1,1);

#
# Structure for table "adm_role"
#

DROP TABLE IF EXISTS `adm_role`;
CREATE TABLE `adm_role` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

#
# Data for table "adm_role"
#

INSERT INTO `adm_role` VALUES (1,'超级管理员');

#
# Structure for table "adm_role_permission"
#

DROP TABLE IF EXISTS `adm_role_permission`;
CREATE TABLE `adm_role_permission` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `permission` varchar(255) DEFAULT NULL,
  `permission_name` varchar(255) DEFAULT NULL,
  `role_id` int(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=782 DEFAULT CHARSET=utf8mb4;

#
# Data for table "adm_role_permission"
#

INSERT INTO `adm_role_permission` VALUES (1,'admin:admin:*','管理员管理',NULL),(2,'lesson:lesson:*','课程管理',NULL),(3,'account:user:view','用户信息(查看)',NULL),(4,'sns:snsUser:*','用户管理',NULL),(5,'account:user:edit','用户信息(编辑)',NULL),(6,'admin:role:*','角色管理',NULL),(7,'admin:admin:*','管理员管理',NULL),(8,'account:user:view','用户信息(查看)',NULL),(9,'account:user:edit','用户信息(编辑)',NULL),(10,'admin:role:*','角色管理',NULL),(723,'agent:view','代理信息(查看)',1),(724,'area:*','区域管理',1),(725,'site:*','站点管理',1),(726,'account:deposit','账户充值',1),(727,'appearance:view','颜值认证查看',1),(728,'task:deliver','平台发货',1),(729,'platformLogistics:import','平台物流导入',1),(730,'mal:edit','商品编辑',1),(731,'appearance:confirm','颜值认证审核管理',1),(732,'platformLogistics:export','平台物流导出',1),(733,'agent:edit','代理信息(编辑)',1),(734,'admin:*','管理员管理',1),(735,'accountLog:export','流水导出',1),(736,'deposit:export','充值导出',1),(737,'shopInfo:confirm','商家店铺信息(审核)',1),(738,'feedback:view','申诉管理',1),(739,'task:confirm','任务审核',1),(740,'task:settleUp','结算',1),(741,'task:view','任务查看',1),(742,'shopInfo:view','商家店铺信息(查看)',1),(743,'user:view','用户信息(查看)',1),(744,'notice:view','公告管理',1),(745,'platformAccount:confirm','平台账号(审核)',1),(746,'agent:export','代理信息(导出)',1),(747,'agent:freeze','代理信息(冻结账号)',1),(748,'deposit:confirm','充值审核',1),(749,'withdraw:confirm','操作提现',1),(750,'agent:addVip','代理信息(加VIP)',1),(751,'bankCard:view','绑定银行信息(查看)',1),(752,'platformLogistics:view','平台物流查看',1),(753,'user:freeze','用户信息(冻结账号)',1),(754,'notice:edit','公告编辑',1),(755,'setting:*','系统设置',1),(756,'ticket:view','试客券管理',1),(757,'account:view','账户查看',1),(758,'mal:view','商品查看',1),(759,'user:export','用户信息(导出)',1),(760,'message:edit','消息编辑',1),(761,'tag:*','标签管理',1),(762,'order:view','订单查看',1),(763,'deposit:view','充值查看',1),(764,'article:view','文章查看',1),(765,'banner:edit','banner编辑',1),(766,'accountLog:view','流水查看',1),(767,'article:edit','文章编辑',1),(768,'user:addVip','用户信息(加VIP)',1),(769,'bankCard:confirm','绑定银行信息(审核)',1),(770,'help:edit','帮助中心编辑',1),(771,'withdraw:view','提现查看',1),(772,'platformAccount:view','平台账号信息(查看)',1),(773,'redPacket:view','红包管理',1),(774,'report:view','任务报告管理',1),(775,'role:*','角色管理',1),(776,'help:view','帮助中心管理',1),(777,'profit:view','收益查看',1),(778,'user:edit','用户信息(编辑)',1),(779,'pointProduct:view','积分商品管理',1),(780,'message:view','消息管理',1),(781,'banner:view','banner管理',1);

#
# Structure for table "cms_article"
#

DROP TABLE IF EXISTS `cms_article`;
CREATE TABLE `cms_article` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `brief` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `released_time` datetime DEFAULT NULL,
  `is_released` bit(1) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `content` longtext,
  `created_time` datetime DEFAULT NULL,
  `visit_count` int(20) DEFAULT NULL,
  `article_category_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8mb4;

#
# Data for table "cms_article"
#


#
# Structure for table "cms_article_category"
#

DROP TABLE IF EXISTS `cms_article_category`;
CREATE TABLE `cms_article_category` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `index_number` int(11) DEFAULT NULL,
  `is_visiable` bit(1) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `is_top_parent` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

#
# Data for table "cms_article_category"
#


#
# Structure for table "cms_banner"
#

DROP TABLE IF EXISTS `cms_banner`;
CREATE TABLE `cms_banner` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `is_released` bit(1) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `order_number` int(11) DEFAULT NULL,
  `banner_position` int(2) DEFAULT NULL,
  `is_open_blank` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

#
# Data for table "cms_banner"
#


#
# Structure for table "cms_feedback"
#

DROP TABLE IF EXISTS `cms_feedback`;
CREATE TABLE `cms_feedback` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `feedback_type` int(2) DEFAULT NULL,
  `feedback_status` int(2) DEFAULT NULL,
  `content` varchar(2000) DEFAULT NULL,
  `reply` varchar(2000) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `replied_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "cms_feedback"
#


#
# Structure for table "cms_help"
#

DROP TABLE IF EXISTS `cms_help`;
CREATE TABLE `cms_help` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(80) DEFAULT NULL,
  `content` longtext,
  `created_time` datetime DEFAULT NULL,
  `help_category_id` int(20) DEFAULT NULL,
  `index_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4;

#
# Data for table "cms_help"
#


#
# Structure for table "cms_help_category"
#

DROP TABLE IF EXISTS `cms_help_category`;
CREATE TABLE `cms_help_category` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `user_type` int(2) DEFAULT NULL,
  `index_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4;

#
# Data for table "cms_help_category"
#


#
# Structure for table "cms_notice"
#

DROP TABLE IF EXISTS `cms_notice`;
CREATE TABLE `cms_notice` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `content` longtext,
  `created_time` datetime DEFAULT NULL,
  `notice_type` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

#
# Data for table "cms_notice"
#


#
# Structure for table "fnc_account"
#

DROP TABLE IF EXISTS `fnc_account`;
CREATE TABLE `fnc_account` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `amount` decimal(19,2) DEFAULT NULL,
  `currency_type` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_id_currency_type` (`user_id`,`currency_type`)
) ENGINE=InnoDB AUTO_INCREMENT=258 DEFAULT CHARSET=utf8mb4;

#
# Data for table "fnc_account"
#

INSERT INTO `fnc_account` VALUES (2,1,0,0.00,0),(10,3,0,0.00,0),(16,3,0,0.00,2),(17,1,0,0.00,2),(19,3,0,0.00,1),(20,1,0,0.00,1),(189,2,0,0.00,0),(190,2,0,0.00,1),(191,2,0,0.00,2);

#
# Structure for table "fnc_account_log"
#

DROP TABLE IF EXISTS `fnc_account_log`;
CREATE TABLE `fnc_account_log` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `in_out` int(2) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `user_id` int(20) DEFAULT NULL,
  `account_log_type` int(2) DEFAULT NULL,
  `ref_id` int(20) DEFAULT NULL,
  `ref_sn` varchar(255) DEFAULT NULL,
  `currency_type` int(2) DEFAULT NULL,
  `trans_time` datetime DEFAULT NULL,
  `before_amount` decimal(19,2) DEFAULT NULL,
  `trans_amount` decimal(19,2) DEFAULT NULL,
  `after_amount` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4;

#
# Data for table "fnc_account_log"
#


#
# Structure for table "fnc_bank_card"
#

DROP TABLE IF EXISTS `fnc_bank_card`;
CREATE TABLE `fnc_bank_card` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `realname` varchar(255) DEFAULT NULL,
  `card_number` varchar(255) DEFAULT NULL,
  `bank_name` varchar(255) DEFAULT NULL,
  `bank_branch_name` varchar(255) DEFAULT NULL,
  `confirm_status` int(2) DEFAULT NULL,
  `confirm_remark` varchar(255) DEFAULT NULL,
  `confirmed_time` datetime DEFAULT NULL,
  `applied_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "fnc_bank_card"
#


#
# Structure for table "fnc_deposit"
#

DROP TABLE IF EXISTS `fnc_deposit`;
CREATE TABLE `fnc_deposit` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `payment_id` int(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `sn` varchar(60) DEFAULT NULL,
  `currency_type1` int(2) DEFAULT NULL,
  `amount1` decimal(19,2) DEFAULT NULL,
  `currency_type2` int(2) DEFAULT NULL,
  `amount2` decimal(19,2) DEFAULT NULL,
  `total_amount` decimal(19,2) DEFAULT NULL,
  `is_outer_created` bit(1) DEFAULT NULL,
  `outer_sn` varchar(255) DEFAULT NULL,
  `qr_code_url` varchar(255) DEFAULT NULL,
  `weixin_open_id` varchar(255) DEFAULT NULL,
  `pay_type` int(2) DEFAULT NULL,
  `paid_time` datetime DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `expired_time` datetime DEFAULT NULL,
  `deposit_status` int(2) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_sn` (`sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "fnc_deposit"
#


#
# Structure for table "fnc_payment"
#

DROP TABLE IF EXISTS `fnc_payment`;
CREATE TABLE `fnc_payment` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `sn` varchar(60) DEFAULT NULL,
  `user_id` int(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `pay_type` int(2) DEFAULT NULL,
  `biz_name` varchar(60) DEFAULT NULL,
  `biz_sn` varchar(60) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `expired_time` datetime DEFAULT NULL,
  `paid_time` datetime DEFAULT NULL,
  `payment_status` int(2) DEFAULT NULL,
  `currency_type1` int(2) DEFAULT NULL,
  `amount1` decimal(19,2) DEFAULT NULL,
  `currency_type2` int(2) DEFAULT NULL,
  `amount2` decimal(19,2) DEFAULT NULL,
  `refund1` decimal(19,2) DEFAULT NULL,
  `refund2` decimal(19,2) DEFAULT NULL,
  `refunded_time` datetime DEFAULT NULL,
  `refund_remark` varchar(255) DEFAULT NULL,
  `cancel_remark` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_biz_name_biz_sn` (`biz_name`,`biz_sn`),
  UNIQUE KEY `unique_sn` (`sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "fnc_payment"
#


#
# Structure for table "fnc_profit"
#

DROP TABLE IF EXISTS `fnc_profit`;
CREATE TABLE `fnc_profit` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `sn` varchar(60) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `currency_type` int(2) DEFAULT NULL,
  `amount` decimal(19,2) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `biz_name` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_sn` (`sn`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

#
# Data for table "fnc_profit"
#


#
# Structure for table "fnc_withdraw"
#

DROP TABLE IF EXISTS `fnc_withdraw`;
CREATE TABLE `fnc_withdraw` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `currency_type` int(2) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `sn` varchar(60) DEFAULT NULL,
  `amount` decimal(19,2) DEFAULT NULL,
  `fee_rate` decimal(19,2) DEFAULT NULL,
  `fee` decimal(19,2) DEFAULT NULL,
  `real_amount` decimal(19,2) DEFAULT NULL,
  `outer_fee` decimal(19,2) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `withdrawed_time` datetime DEFAULT NULL,
  `withdraw_status` int(2) DEFAULT NULL,
  `operator_user_id` int(20) DEFAULT NULL,
  `remark` varchar(2000) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `is_to_bank_card` bit(1) DEFAULT NULL,
  `bank_card_id` int(20) DEFAULT NULL,
  `open_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_sn` (`sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "fnc_withdraw"
#


#
# Structure for table "mal_delivery"
#

DROP TABLE IF EXISTS `mal_delivery`;
CREATE TABLE `mal_delivery` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `sn` varchar(255) DEFAULT NULL,
  `delivery_status` int(2) DEFAULT NULL,
  `sender_area_id` int(20) DEFAULT NULL,
  `sender_realname` varchar(255) DEFAULT NULL,
  `sender_phone` varchar(255) DEFAULT NULL,
  `sender_province` varchar(255) DEFAULT NULL,
  `sender_city` varchar(255) DEFAULT NULL,
  `sender_district` varchar(255) DEFAULT NULL,
  `sender_address` varchar(255) DEFAULT NULL,
  `receiver_area_id` int(20) DEFAULT NULL,
  `receiver_realname` varchar(255) DEFAULT NULL,
  `receiver_phone` varchar(255) DEFAULT NULL,
  `receiver_province` varchar(255) DEFAULT NULL,
  `receiver_city` varchar(255) DEFAULT NULL,
  `receiver_district` varchar(255) DEFAULT NULL,
  `receiver_address` varchar(255) DEFAULT NULL,
  `order_id` int(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "mal_delivery"
#


#
# Structure for table "mal_gift"
#

DROP TABLE IF EXISTS `mal_gift`;
CREATE TABLE `mal_gift` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `detail` longtext,
  `price` decimal(19,2) DEFAULT NULL,
  `market_price` decimal(19,2) DEFAULT NULL,
  `sku_code` varchar(255) DEFAULT NULL,
  `stock_quantity` int(20) DEFAULT NULL,
  `locked_count` int(20) DEFAULT NULL,
  `is_on` bit(1) DEFAULT NULL,
  `image1` varchar(255) DEFAULT NULL,
  `image2` varchar(255) DEFAULT NULL,
  `image3` varchar(255) DEFAULT NULL,
  `image4` varchar(255) DEFAULT NULL,
  `image5` varchar(255) DEFAULT NULL,
  `image6` varchar(255) DEFAULT NULL,
  `order_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8mb4;

#
# Data for table "mal_gift"
#


#
# Structure for table "mal_order"
#

DROP TABLE IF EXISTS `mal_order`;
CREATE TABLE `mal_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `sn` varchar(60) DEFAULT NULL,
  `user_id` int(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `expired_time` datetime DEFAULT NULL,
  `paid_time` datetime DEFAULT NULL,
  `refunded_time` datetime DEFAULT NULL,
  `order_status` int(2) DEFAULT NULL,
  `discount_fee` decimal(19,2) DEFAULT NULL,
  `amount` decimal(19,2) DEFAULT NULL,
  `refund` decimal(19,2) DEFAULT NULL,
  `refund_remark` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_sn` (`sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "mal_order"
#


#
# Structure for table "mal_order_item"
#

DROP TABLE IF EXISTS `mal_order_item`;
CREATE TABLE `mal_order_item` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `order_id` int(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `market_price` decimal(19,2) DEFAULT NULL,
  `price` decimal(19,2) DEFAULT NULL,
  `quantity` int(20) DEFAULT NULL,
  `amount` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "mal_order_item"
#


#
# Structure for table "mal_product"
#

DROP TABLE IF EXISTS `mal_product`;
CREATE TABLE `mal_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `created_time` datetime DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `detail` longtext,
  `price` decimal(19,2) DEFAULT NULL,
  `market_price` decimal(19,2) DEFAULT NULL,
  `sku_code` varchar(255) DEFAULT NULL,
  `stock_quantity` int(20) DEFAULT NULL,
  `locked_count` int(20) DEFAULT NULL,
  `is_on` bit(1) DEFAULT NULL,
  `image1` varchar(255) DEFAULT NULL,
  `image2` varchar(255) DEFAULT NULL,
  `image3` varchar(255) DEFAULT NULL,
  `image4` varchar(255) DEFAULT NULL,
  `image5` varchar(255) DEFAULT NULL,
  `image6` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "mal_product"
#


#
# Structure for table "sys_area"
#

DROP TABLE IF EXISTS `sys_area`;
CREATE TABLE `sys_area` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `area_type` int(2) DEFAULT NULL,
  `parent_id` int(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `order_number` int(11) DEFAULT NULL,
  `code` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3573 DEFAULT CHARSET=utf8mb4;

#
# Data for table "sys_area"
#

INSERT INTO `sys_area` VALUES (1,0,NULL,'北京市',110000,110000),(2,1,1,'市辖区',110100,110100),(3,2,2,'东城区',110101,110101),(4,2,2,'西城区',110102,110102),(5,2,2,'朝阳区',110105,110105),(6,2,2,'丰台区',110106,110106),(7,2,2,'石景山区',110107,110107),(8,2,2,'海淀区',110108,110108),(9,2,2,'门头沟区',110109,110109),(10,2,2,'房山区',110111,110111),(11,2,2,'通州区',110112,110112),(12,2,2,'顺义区',110113,110113),(13,2,2,'昌平区',110114,110114),(14,2,2,'大兴区',110115,110115),(15,2,2,'怀柔区',110116,110116),(16,2,2,'平谷区',110117,110117),(17,1,1,'县',110200,110200),(18,2,17,'密云县',110228,110228),(19,2,17,'延庆县',110229,110229),(20,0,NULL,'天津市',120000,120000),(21,1,20,'市辖区',120100,120100),(22,2,21,'和平区',120101,120101),(23,2,21,'河东区',120102,120102),(24,2,21,'河西区',120103,120103),(25,2,21,'南开区',120104,120104),(26,2,21,'河北区',120105,120105),(27,2,21,'红桥区',120106,120106),(28,2,21,'东丽区',120110,120110),(29,2,21,'西青区',120111,120111),(30,2,21,'津南区',120112,120112),(31,2,21,'北辰区',120113,120113),(32,2,21,'武清区',120114,120114),(33,2,21,'宝坻区',120115,120115),(34,2,21,'滨海新区',120116,120116),(35,1,20,'县',120200,120200),(36,2,35,'宁河县',120221,120221),(37,2,35,'静海县',120223,120223),(38,2,35,'蓟县',120225,120225),(39,0,NULL,'河北省',130000,130000),(40,1,39,'石家庄市',130100,130100),(41,2,40,'市辖区',130101,130101),(42,2,40,'长安区',130102,130102),(43,2,40,'桥西区',130104,130104),(44,2,40,'新华区',130105,130105),(45,2,40,'井陉矿区',130107,130107),(46,2,40,'裕华区',130108,130108),(47,2,40,'藁城区',130109,130109),(48,2,40,'鹿泉区',130110,130110),(49,2,40,'栾城区',130111,130111),(50,2,40,'井陉县',130121,130121),(51,2,40,'正定县',130123,130123),(52,2,40,'行唐县',130125,130125),(53,2,40,'灵寿县',130126,130126),(54,2,40,'高邑县',130127,130127),(55,2,40,'深泽县',130128,130128),(56,2,40,'赞皇县',130129,130129),(57,2,40,'无极县',130130,130130),(58,2,40,'平山县',130131,130131),(59,2,40,'元氏县',130132,130132),(60,2,40,'赵县',130133,130133),(61,2,40,'辛集市',130181,130181),(62,2,40,'晋州市',130183,130183),(63,2,40,'新乐市',130184,130184),(64,1,39,'唐山市',130200,130200),(65,2,64,'市辖区',130201,130201),(66,2,64,'路南区',130202,130202),(67,2,64,'路北区',130203,130203),(68,2,64,'古冶区',130204,130204),(69,2,64,'开平区',130205,130205),(70,2,64,'丰南区',130207,130207),(71,2,64,'丰润区',130208,130208),(72,2,64,'曹妃甸区',130209,130209),(73,2,64,'滦县',130223,130223),(74,2,64,'滦南县',130224,130224),(75,2,64,'乐亭县',130225,130225),(76,2,64,'迁西县',130227,130227),(77,2,64,'玉田县',130229,130229),(78,2,64,'遵化市',130281,130281),(79,2,64,'迁安市',130283,130283),(80,1,39,'秦皇岛市',130300,130300),(81,2,80,'市辖区',130301,130301),(82,2,80,'海港区',130302,130302),(83,2,80,'山海关区',130303,130303),(84,2,80,'北戴河区',130304,130304),(85,2,80,'青龙满族自治县',130321,130321),(86,2,80,'昌黎县',130322,130322),(87,2,80,'抚宁县',130323,130323),(88,2,80,'卢龙县',130324,130324),(89,1,39,'邯郸市',130400,130400),(90,2,89,'市辖区',130401,130401),(91,2,89,'邯山区',130402,130402),(92,2,89,'丛台区',130403,130403),(93,2,89,'复兴区',130404,130404),(94,2,89,'峰峰矿区',130406,130406),(95,2,89,'邯郸县',130421,130421),(96,2,89,'临漳县',130423,130423),(97,2,89,'成安县',130424,130424),(98,2,89,'大名县',130425,130425),(99,2,89,'涉县',130426,130426),(100,2,89,'磁县',130427,130427),(101,2,89,'肥乡县',130428,130428),(102,2,89,'永年县',130429,130429),(103,2,89,'邱县',130430,130430),(104,2,89,'鸡泽县',130431,130431),(105,2,89,'广平县',130432,130432),(106,2,89,'馆陶县',130433,130433),(107,2,89,'魏县',130434,130434),(108,2,89,'曲周县',130435,130435),(109,2,89,'武安市',130481,130481),(110,1,39,'邢台市',130500,130500),(111,2,110,'市辖区',130501,130501),(112,2,110,'桥东区',130502,130502),(113,2,110,'桥西区',130503,130503),(114,2,110,'邢台县',130521,130521),(115,2,110,'临城县',130522,130522),(116,2,110,'内丘县',130523,130523),(117,2,110,'柏乡县',130524,130524),(118,2,110,'隆尧县',130525,130525),(119,2,110,'任县',130526,130526),(120,2,110,'南和县',130527,130527),(121,2,110,'宁晋县',130528,130528),(122,2,110,'巨鹿县',130529,130529),(123,2,110,'新河县',130530,130530),(124,2,110,'广宗县',130531,130531),(125,2,110,'平乡县',130532,130532),(126,2,110,'威县',130533,130533),(127,2,110,'清河县',130534,130534),(128,2,110,'临西县',130535,130535),(129,2,110,'南宫市',130581,130581),(130,2,110,'沙河市',130582,130582),(131,1,39,'保定市',130600,130600),(132,2,131,'市辖区',130601,130601),(133,2,131,'新市区',130602,130602),(134,2,131,'北市区',130603,130603),(135,2,131,'南市区',130604,130604),(136,2,131,'满城县',130621,130621),(137,2,131,'清苑县',130622,130622),(138,2,131,'涞水县',130623,130623),(139,2,131,'阜平县',130624,130624),(140,2,131,'徐水县',130625,130625),(141,2,131,'定兴县',130626,130626),(142,2,131,'唐县',130627,130627),(143,2,131,'高阳县',130628,130628),(144,2,131,'容城县',130629,130629),(145,2,131,'涞源县',130630,130630),(146,2,131,'望都县',130631,130631),(147,2,131,'安新县',130632,130632),(148,2,131,'易县',130633,130633),(149,2,131,'曲阳县',130634,130634),(150,2,131,'蠡县',130635,130635),(151,2,131,'顺平县',130636,130636),(152,2,131,'博野县',130637,130637),(153,2,131,'雄县',130638,130638),(154,2,131,'涿州市',130681,130681),(155,2,131,'定州市',130682,130682),(156,2,131,'安国市',130683,130683),(157,2,131,'高碑店市',130684,130684),(158,1,39,'张家口市',130700,130700),(159,2,158,'市辖区',130701,130701),(160,2,158,'桥东区',130702,130702),(161,2,158,'桥西区',130703,130703),(162,2,158,'宣化区',130705,130705),(163,2,158,'下花园区',130706,130706),(164,2,158,'宣化县',130721,130721),(165,2,158,'张北县',130722,130722),(166,2,158,'康保县',130723,130723),(167,2,158,'沽源县',130724,130724),(168,2,158,'尚义县',130725,130725),(169,2,158,'蔚县',130726,130726),(170,2,158,'阳原县',130727,130727),(171,2,158,'怀安县',130728,130728),(172,2,158,'万全县',130729,130729),(173,2,158,'怀来县',130730,130730),(174,2,158,'涿鹿县',130731,130731),(175,2,158,'赤城县',130732,130732),(176,2,158,'崇礼县',130733,130733),(177,1,39,'承德市',130800,130800),(178,2,177,'市辖区',130801,130801),(179,2,177,'双桥区',130802,130802),(180,2,177,'双滦区',130803,130803),(181,2,177,'鹰手营子矿区',130804,130804),(182,2,177,'承德县',130821,130821),(183,2,177,'兴隆县',130822,130822),(184,2,177,'平泉县',130823,130823),(185,2,177,'滦平县',130824,130824),(186,2,177,'隆化县',130825,130825),(187,2,177,'丰宁满族自治县',130826,130826),(188,2,177,'宽城满族自治县',130827,130827),(189,2,177,'围场满族蒙古族自治县',130828,130828),(190,1,39,'沧州市',130900,130900),(191,2,190,'市辖区',130901,130901),(192,2,190,'新华区',130902,130902),(193,2,190,'运河区',130903,130903),(194,2,190,'沧县',130921,130921),(195,2,190,'青县',130922,130922),(196,2,190,'东光县',130923,130923),(197,2,190,'海兴县',130924,130924),(198,2,190,'盐山县',130925,130925),(199,2,190,'肃宁县',130926,130926),(200,2,190,'南皮县',130927,130927),(201,2,190,'吴桥县',130928,130928),(202,2,190,'献县',130929,130929),(203,2,190,'孟村回族自治县',130930,130930),(204,2,190,'泊头市',130981,130981),(205,2,190,'任丘市',130982,130982),(206,2,190,'黄骅市',130983,130983),(207,2,190,'河间市',130984,130984),(208,1,39,'廊坊市',131000,131000),(209,2,208,'市辖区',131001,131001),(210,2,208,'安次区',131002,131002),(211,2,208,'广阳区',131003,131003),(212,2,208,'固安县',131022,131022),(213,2,208,'永清县',131023,131023),(214,2,208,'香河县',131024,131024),(215,2,208,'大城县',131025,131025),(216,2,208,'文安县',131026,131026),(217,2,208,'大厂回族自治县',131028,131028),(218,2,208,'霸州市',131081,131081),(219,2,208,'三河市',131082,131082),(220,1,39,'衡水市',131100,131100),(221,2,220,'市辖区',131101,131101),(222,2,220,'桃城区',131102,131102),(223,2,220,'枣强县',131121,131121),(224,2,220,'武邑县',131122,131122),(225,2,220,'武强县',131123,131123),(226,2,220,'饶阳县',131124,131124),(227,2,220,'安平县',131125,131125),(228,2,220,'故城县',131126,131126),(229,2,220,'景县',131127,131127),(230,2,220,'阜城县',131128,131128),(231,2,220,'冀州市',131181,131181),(232,2,220,'深州市',131182,131182),(233,0,NULL,'山西省',140000,140000),(234,1,233,'太原市',140100,140100),(235,2,234,'市辖区',140101,140101),(236,2,234,'小店区',140105,140105),(237,2,234,'迎泽区',140106,140106),(238,2,234,'杏花岭区',140107,140107),(239,2,234,'尖草坪区',140108,140108),(240,2,234,'万柏林区',140109,140109),(241,2,234,'晋源区',140110,140110),(242,2,234,'清徐县',140121,140121),(243,2,234,'阳曲县',140122,140122),(244,2,234,'娄烦县',140123,140123),(245,2,234,'古交市',140181,140181),(246,1,233,'大同市',140200,140200),(247,2,246,'市辖区',140201,140201),(248,2,246,'城区',140202,140202),(249,2,246,'矿区',140203,140203),(250,2,246,'南郊区',140211,140211),(251,2,246,'新荣区',140212,140212),(252,2,246,'阳高县',140221,140221),(253,2,246,'天镇县',140222,140222),(254,2,246,'广灵县',140223,140223),(255,2,246,'灵丘县',140224,140224),(256,2,246,'浑源县',140225,140225),(257,2,246,'左云县',140226,140226),(258,2,246,'大同县',140227,140227),(259,1,233,'阳泉市',140300,140300),(260,2,259,'市辖区',140301,140301),(261,2,259,'城区',140302,140302),(262,2,259,'矿区',140303,140303),(263,2,259,'郊区',140311,140311),(264,2,259,'平定县',140321,140321),(265,2,259,'盂县',140322,140322),(266,1,233,'长治市',140400,140400),(267,2,266,'市辖区',140401,140401),(268,2,266,'城区',140402,140402),(269,2,266,'郊区',140411,140411),(270,2,266,'长治县',140421,140421),(271,2,266,'襄垣县',140423,140423),(272,2,266,'屯留县',140424,140424),(273,2,266,'平顺县',140425,140425),(274,2,266,'黎城县',140426,140426),(275,2,266,'壶关县',140427,140427),(276,2,266,'长子县',140428,140428),(277,2,266,'武乡县',140429,140429),(278,2,266,'沁县',140430,140430),(279,2,266,'沁源县',140431,140431),(280,2,266,'潞城市',140481,140481),(281,1,233,'晋城市',140500,140500),(282,2,281,'市辖区',140501,140501),(283,2,281,'城区',140502,140502),(284,2,281,'沁水县',140521,140521),(285,2,281,'阳城县',140522,140522),(286,2,281,'陵川县',140524,140524),(287,2,281,'泽州县',140525,140525),(288,2,281,'高平市',140581,140581),(289,1,233,'朔州市',140600,140600),(290,2,289,'市辖区',140601,140601),(291,2,289,'朔城区',140602,140602),(292,2,289,'平鲁区',140603,140603),(293,2,289,'山阴县',140621,140621),(294,2,289,'应县',140622,140622),(295,2,289,'右玉县',140623,140623),(296,2,289,'怀仁县',140624,140624),(297,1,233,'晋中市',140700,140700),(298,2,297,'市辖区',140701,140701),(299,2,297,'榆次区',140702,140702),(300,2,297,'榆社县',140721,140721),(301,2,297,'左权县',140722,140722),(302,2,297,'和顺县',140723,140723),(303,2,297,'昔阳县',140724,140724),(304,2,297,'寿阳县',140725,140725),(305,2,297,'太谷县',140726,140726),(306,2,297,'祁县',140727,140727),(307,2,297,'平遥县',140728,140728),(308,2,297,'灵石县',140729,140729),(309,2,297,'介休市',140781,140781),(310,1,233,'运城市',140800,140800),(311,2,310,'市辖区',140801,140801),(312,2,310,'盐湖区',140802,140802),(313,2,310,'临猗县',140821,140821),(314,2,310,'万荣县',140822,140822),(315,2,310,'闻喜县',140823,140823),(316,2,310,'稷山县',140824,140824),(317,2,310,'新绛县',140825,140825),(318,2,310,'绛县',140826,140826),(319,2,310,'垣曲县',140827,140827),(320,2,310,'夏县',140828,140828),(321,2,310,'平陆县',140829,140829),(322,2,310,'芮城县',140830,140830),(323,2,310,'永济市',140881,140881),(324,2,310,'河津市',140882,140882),(325,1,233,'忻州市',140900,140900),(326,2,325,'市辖区',140901,140901),(327,2,325,'忻府区',140902,140902),(328,2,325,'定襄县',140921,140921),(329,2,325,'五台县',140922,140922),(330,2,325,'代县',140923,140923),(331,2,325,'繁峙县',140924,140924),(332,2,325,'宁武县',140925,140925),(333,2,325,'静乐县',140926,140926),(334,2,325,'神池县',140927,140927),(335,2,325,'五寨县',140928,140928),(336,2,325,'岢岚县',140929,140929),(337,2,325,'河曲县',140930,140930),(338,2,325,'保德县',140931,140931),(339,2,325,'偏关县',140932,140932),(340,2,325,'原平市',140981,140981),(341,1,233,'临汾市',141000,141000),(342,2,341,'市辖区',141001,141001),(343,2,341,'尧都区',141002,141002),(344,2,341,'曲沃县',141021,141021),(345,2,341,'翼城县',141022,141022),(346,2,341,'襄汾县',141023,141023),(347,2,341,'洪洞县',141024,141024),(348,2,341,'古县',141025,141025),(349,2,341,'安泽县',141026,141026),(350,2,341,'浮山县',141027,141027),(351,2,341,'吉县',141028,141028),(352,2,341,'乡宁县',141029,141029),(353,2,341,'大宁县',141030,141030),(354,2,341,'隰县',141031,141031),(355,2,341,'永和县',141032,141032),(356,2,341,'蒲县',141033,141033),(357,2,341,'汾西县',141034,141034),(358,2,341,'侯马市',141081,141081),(359,2,341,'霍州市',141082,141082),(360,1,233,'吕梁市',141100,141100),(361,2,360,'市辖区',141101,141101),(362,2,360,'离石区',141102,141102),(363,2,360,'文水县',141121,141121),(364,2,360,'交城县',141122,141122),(365,2,360,'兴县',141123,141123),(366,2,360,'临县',141124,141124),(367,2,360,'柳林县',141125,141125),(368,2,360,'石楼县',141126,141126),(369,2,360,'岚县',141127,141127),(370,2,360,'方山县',141128,141128),(371,2,360,'中阳县',141129,141129),(372,2,360,'交口县',141130,141130),(373,2,360,'孝义市',141181,141181),(374,2,360,'汾阳市',141182,141182),(375,0,NULL,'内蒙古自治区',150000,150000),(376,1,375,'呼和浩特市',150100,150100),(377,2,376,'市辖区',150101,150101),(378,2,376,'新城区',150102,150102),(379,2,376,'回民区',150103,150103),(380,2,376,'玉泉区',150104,150104),(381,2,376,'赛罕区',150105,150105),(382,2,376,'土默特左旗',150121,150121),(383,2,376,'托克托县',150122,150122),(384,2,376,'和林格尔县',150123,150123),(385,2,376,'清水河县',150124,150124),(386,2,376,'武川县',150125,150125),(387,1,375,'包头市',150200,150200),(388,2,387,'市辖区',150201,150201),(389,2,387,'东河区',150202,150202),(390,2,387,'昆都仑区',150203,150203),(391,2,387,'青山区',150204,150204),(392,2,387,'石拐区',150205,150205),(393,2,387,'白云鄂博矿区',150206,150206),(394,2,387,'九原区',150207,150207),(395,2,387,'土默特右旗',150221,150221),(396,2,387,'固阳县',150222,150222),(397,2,387,'达尔罕茂明安联合旗',150223,150223),(398,1,375,'乌海市',150300,150300),(399,2,398,'市辖区',150301,150301),(400,2,398,'海勃湾区',150302,150302),(401,2,398,'海南区',150303,150303),(402,2,398,'乌达区',150304,150304),(403,1,375,'赤峰市',150400,150400),(404,2,403,'市辖区',150401,150401),(405,2,403,'红山区',150402,150402),(406,2,403,'元宝山区',150403,150403),(407,2,403,'松山区',150404,150404),(408,2,403,'阿鲁科尔沁旗',150421,150421),(409,2,403,'巴林左旗',150422,150422),(410,2,403,'巴林右旗',150423,150423),(411,2,403,'林西县',150424,150424),(412,2,403,'克什克腾旗',150425,150425),(413,2,403,'翁牛特旗',150426,150426),(414,2,403,'喀喇沁旗',150428,150428),(415,2,403,'宁城县',150429,150429),(416,2,403,'敖汉旗',150430,150430),(417,1,375,'通辽市',150500,150500),(418,2,417,'市辖区',150501,150501),(419,2,417,'科尔沁区',150502,150502),(420,2,417,'科尔沁左翼中旗',150521,150521),(421,2,417,'科尔沁左翼后旗',150522,150522),(422,2,417,'开鲁县',150523,150523),(423,2,417,'库伦旗',150524,150524),(424,2,417,'奈曼旗',150525,150525),(425,2,417,'扎鲁特旗',150526,150526),(426,2,417,'霍林郭勒市',150581,150581),(427,1,375,'鄂尔多斯市',150600,150600),(428,2,427,'市辖区',150601,150601),(429,2,427,'东胜区',150602,150602),(430,2,427,'达拉特旗',150621,150621),(431,2,427,'准格尔旗',150622,150622),(432,2,427,'鄂托克前旗',150623,150623),(433,2,427,'鄂托克旗',150624,150624),(434,2,427,'杭锦旗',150625,150625),(435,2,427,'乌审旗',150626,150626),(436,2,427,'伊金霍洛旗',150627,150627),(437,1,375,'呼伦贝尔市',150700,150700),(438,2,437,'市辖区',150701,150701),(439,2,437,'海拉尔区',150702,150702),(440,2,437,'扎赉诺尔区',150703,150703),(441,2,437,'阿荣旗',150721,150721),(442,2,437,'莫力达瓦达斡尔族自治旗',150722,150722),(443,2,437,'鄂伦春自治旗',150723,150723),(444,2,437,'鄂温克族自治旗',150724,150724),(445,2,437,'陈巴尔虎旗',150725,150725),(446,2,437,'新巴尔虎左旗',150726,150726),(447,2,437,'新巴尔虎右旗',150727,150727),(448,2,437,'满洲里市',150781,150781),(449,2,437,'牙克石市',150782,150782),(450,2,437,'扎兰屯市',150783,150783),(451,2,437,'额尔古纳市',150784,150784),(452,2,437,'根河市',150785,150785),(453,1,375,'巴彦淖尔市',150800,150800),(454,2,453,'市辖区',150801,150801),(455,2,453,'临河区',150802,150802),(456,2,453,'五原县',150821,150821),(457,2,453,'磴口县',150822,150822),(458,2,453,'乌拉特前旗',150823,150823),(459,2,453,'乌拉特中旗',150824,150824),(460,2,453,'乌拉特后旗',150825,150825),(461,2,453,'杭锦后旗',150826,150826),(462,1,375,'乌兰察布市',150900,150900),(463,2,462,'市辖区',150901,150901),(464,2,462,'集宁区',150902,150902),(465,2,462,'卓资县',150921,150921),(466,2,462,'化德县',150922,150922),(467,2,462,'商都县',150923,150923),(468,2,462,'兴和县',150924,150924),(469,2,462,'凉城县',150925,150925),(470,2,462,'察哈尔右翼前旗',150926,150926),(471,2,462,'察哈尔右翼中旗',150927,150927),(472,2,462,'察哈尔右翼后旗',150928,150928),(473,2,462,'四子王旗',150929,150929),(474,2,462,'丰镇市',150981,150981),(475,1,375,'兴安盟',152200,152200),(476,2,475,'乌兰浩特市',152201,152201),(477,2,475,'阿尔山市',152202,152202),(478,2,475,'科尔沁右翼前旗',152221,152221),(479,2,475,'科尔沁右翼中旗',152222,152222),(480,2,475,'扎赉特旗',152223,152223),(481,2,475,'突泉县',152224,152224),(482,1,375,'锡林郭勒盟',152500,152500),(483,2,482,'二连浩特市',152501,152501),(484,2,482,'锡林浩特市',152502,152502),(485,2,482,'阿巴嘎旗',152522,152522),(486,2,482,'苏尼特左旗',152523,152523),(487,2,482,'苏尼特右旗',152524,152524),(488,2,482,'东乌珠穆沁旗',152525,152525),(489,2,482,'西乌珠穆沁旗',152526,152526),(490,2,482,'太仆寺旗',152527,152527),(491,2,482,'镶黄旗',152528,152528),(492,2,482,'正镶白旗',152529,152529),(493,2,482,'正蓝旗',152530,152530),(494,2,482,'多伦县',152531,152531),(495,1,375,'阿拉善盟',152900,152900),(496,2,495,'阿拉善左旗',152921,152921),(497,2,495,'阿拉善右旗',152922,152922),(498,2,495,'额济纳旗',152923,152923),(499,0,NULL,'辽宁省',210000,210000),(500,1,499,'沈阳市',210100,210100),(501,2,500,'市辖区',210101,210101),(502,2,500,'和平区',210102,210102),(503,2,500,'沈河区',210103,210103),(504,2,500,'大东区',210104,210104),(505,2,500,'皇姑区',210105,210105),(506,2,500,'铁西区',210106,210106),(507,2,500,'苏家屯区',210111,210111),(508,2,500,'浑南区',210112,210112),(509,2,500,'沈北新区',210113,210113),(510,2,500,'于洪区',210114,210114),(511,2,500,'辽中县',210122,210122),(512,2,500,'康平县',210123,210123),(513,2,500,'法库县',210124,210124),(514,2,500,'新民市',210181,210181),(515,1,499,'大连市',210200,210200),(516,2,515,'市辖区',210201,210201),(517,2,515,'中山区',210202,210202),(518,2,515,'西岗区',210203,210203),(519,2,515,'沙河口区',210204,210204),(520,2,515,'甘井子区',210211,210211),(521,2,515,'旅顺口区',210212,210212),(522,2,515,'金州区',210213,210213),(523,2,515,'长海县',210224,210224),(524,2,515,'瓦房店市',210281,210281),(525,2,515,'普兰店市',210282,210282),(526,2,515,'庄河市',210283,210283),(527,1,499,'鞍山市',210300,210300),(528,2,527,'市辖区',210301,210301),(529,2,527,'铁东区',210302,210302),(530,2,527,'铁西区',210303,210303),(531,2,527,'立山区',210304,210304),(532,2,527,'千山区',210311,210311),(533,2,527,'台安县',210321,210321),(534,2,527,'岫岩满族自治县',210323,210323),(535,2,527,'海城市',210381,210381),(536,1,499,'抚顺市',210400,210400),(537,2,536,'市辖区',210401,210401),(538,2,536,'新抚区',210402,210402),(539,2,536,'东洲区',210403,210403),(540,2,536,'望花区',210404,210404),(541,2,536,'顺城区',210411,210411),(542,2,536,'抚顺县',210421,210421),(543,2,536,'新宾满族自治县',210422,210422),(544,2,536,'清原满族自治县',210423,210423),(545,1,499,'本溪市',210500,210500),(546,2,545,'市辖区',210501,210501),(547,2,545,'平山区',210502,210502),(548,2,545,'溪湖区',210503,210503),(549,2,545,'明山区',210504,210504),(550,2,545,'南芬区',210505,210505),(551,2,545,'本溪满族自治县',210521,210521),(552,2,545,'桓仁满族自治县',210522,210522),(553,1,499,'丹东市',210600,210600),(554,2,553,'市辖区',210601,210601),(555,2,553,'元宝区',210602,210602),(556,2,553,'振兴区',210603,210603),(557,2,553,'振安区',210604,210604),(558,2,553,'宽甸满族自治县',210624,210624),(559,2,553,'东港市',210681,210681),(560,2,553,'凤城市',210682,210682),(561,1,499,'锦州市',210700,210700),(562,2,561,'市辖区',210701,210701),(563,2,561,'古塔区',210702,210702),(564,2,561,'凌河区',210703,210703),(565,2,561,'太和区',210711,210711),(566,2,561,'黑山县',210726,210726),(567,2,561,'义县',210727,210727),(568,2,561,'凌海市',210781,210781),(569,2,561,'北镇市',210782,210782),(570,1,499,'营口市',210800,210800),(571,2,570,'市辖区',210801,210801),(572,2,570,'站前区',210802,210802),(573,2,570,'西市区',210803,210803),(574,2,570,'鲅鱼圈区',210804,210804),(575,2,570,'老边区',210811,210811),(576,2,570,'盖州市',210881,210881),(577,2,570,'大石桥市',210882,210882),(578,1,499,'阜新市',210900,210900),(579,2,578,'市辖区',210901,210901),(580,2,578,'海州区',210902,210902),(581,2,578,'新邱区',210903,210903),(582,2,578,'太平区',210904,210904),(583,2,578,'清河门区',210905,210905),(584,2,578,'细河区',210911,210911),(585,2,578,'阜新蒙古族自治县',210921,210921),(586,2,578,'彰武县',210922,210922),(587,1,499,'辽阳市',211000,211000),(588,2,587,'市辖区',211001,211001),(589,2,587,'白塔区',211002,211002),(590,2,587,'文圣区',211003,211003),(591,2,587,'宏伟区',211004,211004),(592,2,587,'弓长岭区',211005,211005),(593,2,587,'太子河区',211011,211011),(594,2,587,'辽阳县',211021,211021),(595,2,587,'灯塔市',211081,211081),(596,1,499,'盘锦市',211100,211100),(597,2,596,'市辖区',211101,211101),(598,2,596,'双台子区',211102,211102),(599,2,596,'兴隆台区',211103,211103),(600,2,596,'大洼县',211121,211121),(601,2,596,'盘山县',211122,211122),(602,1,499,'铁岭市',211200,211200),(603,2,602,'市辖区',211201,211201),(604,2,602,'银州区',211202,211202),(605,2,602,'清河区',211204,211204),(606,2,602,'铁岭县',211221,211221),(607,2,602,'西丰县',211223,211223),(608,2,602,'昌图县',211224,211224),(609,2,602,'调兵山市',211281,211281),(610,2,602,'开原市',211282,211282),(611,1,499,'朝阳市',211300,211300),(612,2,611,'市辖区',211301,211301),(613,2,611,'双塔区',211302,211302),(614,2,611,'龙城区',211303,211303),(615,2,611,'朝阳县',211321,211321),(616,2,611,'建平县',211322,211322),(617,2,611,'喀喇沁左翼蒙古族自治县',211324,211324),(618,2,611,'北票市',211381,211381),(619,2,611,'凌源市',211382,211382),(620,1,499,'葫芦岛市',211400,211400),(621,2,620,'市辖区',211401,211401),(622,2,620,'连山区',211402,211402),(623,2,620,'龙港区',211403,211403),(624,2,620,'南票区',211404,211404),(625,2,620,'绥中县',211421,211421),(626,2,620,'建昌县',211422,211422),(627,2,620,'兴城市',211481,211481),(628,0,NULL,'吉林省',220000,220000),(629,1,628,'长春市',220100,220100),(630,2,629,'市辖区',220101,220101),(631,2,629,'南关区',220102,220102),(632,2,629,'宽城区',220103,220103),(633,2,629,'朝阳区',220104,220104),(634,2,629,'二道区',220105,220105),(635,2,629,'绿园区',220106,220106),(636,2,629,'双阳区',220112,220112),(637,2,629,'九台区',220113,220113),(638,2,629,'农安县',220122,220122),(639,2,629,'榆树市',220182,220182),(640,2,629,'德惠市',220183,220183),(641,1,628,'吉林市',220200,220200),(642,2,641,'市辖区',220201,220201),(643,2,641,'昌邑区',220202,220202),(644,2,641,'龙潭区',220203,220203),(645,2,641,'船营区',220204,220204),(646,2,641,'丰满区',220211,220211),(647,2,641,'永吉县',220221,220221),(648,2,641,'蛟河市',220281,220281),(649,2,641,'桦甸市',220282,220282),(650,2,641,'舒兰市',220283,220283),(651,2,641,'磐石市',220284,220284),(652,1,628,'四平市',220300,220300),(653,2,652,'市辖区',220301,220301),(654,2,652,'铁西区',220302,220302),(655,2,652,'铁东区',220303,220303),(656,2,652,'梨树县',220322,220322),(657,2,652,'伊通满族自治县',220323,220323),(658,2,652,'公主岭市',220381,220381),(659,2,652,'双辽市',220382,220382),(660,1,628,'辽源市',220400,220400),(661,2,660,'市辖区',220401,220401),(662,2,660,'龙山区',220402,220402),(663,2,660,'西安区',220403,220403),(664,2,660,'东丰县',220421,220421),(665,2,660,'东辽县',220422,220422),(666,1,628,'通化市',220500,220500),(667,2,666,'市辖区',220501,220501),(668,2,666,'东昌区',220502,220502),(669,2,666,'二道江区',220503,220503),(670,2,666,'通化县',220521,220521),(671,2,666,'辉南县',220523,220523),(672,2,666,'柳河县',220524,220524),(673,2,666,'梅河口市',220581,220581),(674,2,666,'集安市',220582,220582),(675,1,628,'白山市',220600,220600),(676,2,675,'市辖区',220601,220601),(677,2,675,'浑江区',220602,220602),(678,2,675,'江源区',220605,220605),(679,2,675,'抚松县',220621,220621),(680,2,675,'靖宇县',220622,220622),(681,2,675,'长白朝鲜族自治县',220623,220623),(682,2,675,'临江市',220681,220681),(683,1,628,'松原市',220700,220700),(684,2,683,'市辖区',220701,220701),(685,2,683,'宁江区',220702,220702),(686,2,683,'前郭尔罗斯蒙古族自治县',220721,220721),(687,2,683,'长岭县',220722,220722),(688,2,683,'乾安县',220723,220723),(689,2,683,'扶余市',220781,220781),(690,1,628,'白城市',220800,220800),(691,2,690,'市辖区',220801,220801),(692,2,690,'洮北区',220802,220802),(693,2,690,'镇赉县',220821,220821),(694,2,690,'通榆县',220822,220822),(695,2,690,'洮南市',220881,220881),(696,2,690,'大安市',220882,220882),(697,1,628,'延边朝鲜族自治州',222400,222400),(698,2,697,'延吉市',222401,222401),(699,2,697,'图们市',222402,222402),(700,2,697,'敦化市',222403,222403),(701,2,697,'珲春市',222404,222404),(702,2,697,'龙井市',222405,222405),(703,2,697,'和龙市',222406,222406),(704,2,697,'汪清县',222424,222424),(705,2,697,'安图县',222426,222426),(706,0,NULL,'黑龙江省',230000,230000),(707,1,706,'哈尔滨市',230100,230100),(708,2,707,'市辖区',230101,230101),(709,2,707,'道里区',230102,230102),(710,2,707,'南岗区',230103,230103),(711,2,707,'道外区',230104,230104),(712,2,707,'平房区',230108,230108),(713,2,707,'松北区',230109,230109),(714,2,707,'香坊区',230110,230110),(715,2,707,'呼兰区',230111,230111),(716,2,707,'阿城区',230112,230112),(717,2,707,'依兰县',230123,230123),(718,2,707,'方正县',230124,230124),(719,2,707,'宾县',230125,230125),(720,2,707,'巴彦县',230126,230126),(721,2,707,'木兰县',230127,230127),(722,2,707,'通河县',230128,230128),(723,2,707,'延寿县',230129,230129),(724,2,707,'双城市',230182,230182),(725,2,707,'尚志市',230183,230183),(726,2,707,'五常市',230184,230184),(727,1,706,'齐齐哈尔市',230200,230200),(728,2,727,'市辖区',230201,230201),(729,2,727,'龙沙区',230202,230202),(730,2,727,'建华区',230203,230203),(731,2,727,'铁锋区',230204,230204),(732,2,727,'昂昂溪区',230205,230205),(733,2,727,'富拉尔基区',230206,230206),(734,2,727,'碾子山区',230207,230207),(735,2,727,'梅里斯达斡尔族区',230208,230208),(736,2,727,'龙江县',230221,230221),(737,2,727,'依安县',230223,230223),(738,2,727,'泰来县',230224,230224),(739,2,727,'甘南县',230225,230225),(740,2,727,'富裕县',230227,230227),(741,2,727,'克山县',230229,230229),(742,2,727,'克东县',230230,230230),(743,2,727,'拜泉县',230231,230231),(744,2,727,'讷河市',230281,230281),(745,1,706,'鸡西市',230300,230300),(746,2,745,'市辖区',230301,230301),(747,2,745,'鸡冠区',230302,230302),(748,2,745,'恒山区',230303,230303),(749,2,745,'滴道区',230304,230304),(750,2,745,'梨树区',230305,230305),(751,2,745,'城子河区',230306,230306),(752,2,745,'麻山区',230307,230307),(753,2,745,'鸡东县',230321,230321),(754,2,745,'虎林市',230381,230381),(755,2,745,'密山市',230382,230382),(756,1,706,'鹤岗市',230400,230400),(757,2,756,'市辖区',230401,230401),(758,2,756,'向阳区',230402,230402),(759,2,756,'工农区',230403,230403),(760,2,756,'南山区',230404,230404),(761,2,756,'兴安区',230405,230405),(762,2,756,'东山区',230406,230406),(763,2,756,'兴山区',230407,230407),(764,2,756,'萝北县',230421,230421),(765,2,756,'绥滨县',230422,230422),(766,1,706,'双鸭山市',230500,230500),(767,2,766,'市辖区',230501,230501),(768,2,766,'尖山区',230502,230502),(769,2,766,'岭东区',230503,230503),(770,2,766,'四方台区',230505,230505),(771,2,766,'宝山区',230506,230506),(772,2,766,'集贤县',230521,230521),(773,2,766,'友谊县',230522,230522),(774,2,766,'宝清县',230523,230523),(775,2,766,'饶河县',230524,230524),(776,1,706,'大庆市',230600,230600),(777,2,776,'市辖区',230601,230601),(778,2,776,'萨尔图区',230602,230602),(779,2,776,'龙凤区',230603,230603),(780,2,776,'让胡路区',230604,230604),(781,2,776,'红岗区',230605,230605),(782,2,776,'大同区',230606,230606),(783,2,776,'肇州县',230621,230621),(784,2,776,'肇源县',230622,230622),(785,2,776,'林甸县',230623,230623),(786,2,776,'杜尔伯特蒙古族自治县',230624,230624),(787,1,706,'伊春市',230700,230700),(788,2,787,'市辖区',230701,230701),(789,2,787,'伊春区',230702,230702),(790,2,787,'南岔区',230703,230703),(791,2,787,'友好区',230704,230704),(792,2,787,'西林区',230705,230705),(793,2,787,'翠峦区',230706,230706),(794,2,787,'新青区',230707,230707),(795,2,787,'美溪区',230708,230708),(796,2,787,'金山屯区',230709,230709),(797,2,787,'五营区',230710,230710),(798,2,787,'乌马河区',230711,230711),(799,2,787,'汤旺河区',230712,230712),(800,2,787,'带岭区',230713,230713),(801,2,787,'乌伊岭区',230714,230714),(802,2,787,'红星区',230715,230715),(803,2,787,'上甘岭区',230716,230716),(804,2,787,'嘉荫县',230722,230722),(805,2,787,'铁力市',230781,230781),(806,1,706,'佳木斯市',230800,230800),(807,2,806,'市辖区',230801,230801),(808,2,806,'向阳区',230803,230803),(809,2,806,'前进区',230804,230804),(810,2,806,'东风区',230805,230805),(811,2,806,'郊区',230811,230811),(812,2,806,'桦南县',230822,230822),(813,2,806,'桦川县',230826,230826),(814,2,806,'汤原县',230828,230828),(815,2,806,'抚远县',230833,230833),(816,2,806,'同江市',230881,230881),(817,2,806,'富锦市',230882,230882),(818,1,706,'七台河市',230900,230900),(819,2,818,'市辖区',230901,230901),(820,2,818,'新兴区',230902,230902),(821,2,818,'桃山区',230903,230903),(822,2,818,'茄子河区',230904,230904),(823,2,818,'勃利县',230921,230921),(824,1,706,'牡丹江市',231000,231000),(825,2,824,'市辖区',231001,231001),(826,2,824,'东安区',231002,231002),(827,2,824,'阳明区',231003,231003),(828,2,824,'爱民区',231004,231004),(829,2,824,'西安区',231005,231005),(830,2,824,'东宁县',231024,231024),(831,2,824,'林口县',231025,231025),(832,2,824,'绥芬河市',231081,231081),(833,2,824,'海林市',231083,231083),(834,2,824,'宁安市',231084,231084),(835,2,824,'穆棱市',231085,231085),(836,1,706,'黑河市',231100,231100),(837,2,836,'市辖区',231101,231101),(838,2,836,'爱辉区',231102,231102),(839,2,836,'嫩江县',231121,231121),(840,2,836,'逊克县',231123,231123),(841,2,836,'孙吴县',231124,231124),(842,2,836,'北安市',231181,231181),(843,2,836,'五大连池市',231182,231182),(844,1,706,'绥化市',231200,231200),(845,2,844,'市辖区',231201,231201),(846,2,844,'北林区',231202,231202),(847,2,844,'望奎县',231221,231221),(848,2,844,'兰西县',231222,231222),(849,2,844,'青冈县',231223,231223),(850,2,844,'庆安县',231224,231224),(851,2,844,'明水县',231225,231225),(852,2,844,'绥棱县',231226,231226),(853,2,844,'安达市',231281,231281),(854,2,844,'肇东市',231282,231282),(855,2,844,'海伦市',231283,231283),(856,1,706,'大兴安岭地区',232700,232700),(857,2,856,'呼玛县',232721,232721),(858,2,856,'塔河县',232722,232722),(859,2,856,'漠河县',232723,232723),(860,0,NULL,'上海市',310000,310000),(861,1,860,'市辖区',310100,310100),(862,2,861,'黄浦区',310101,310101),(863,2,861,'徐汇区',310104,310104),(864,2,861,'长宁区',310105,310105),(865,2,861,'静安区',310106,310106),(866,2,861,'普陀区',310107,310107),(867,2,861,'闸北区',310108,310108),(868,2,861,'虹口区',310109,310109),(869,2,861,'杨浦区',310110,310110),(870,2,861,'闵行区',310112,310112),(871,2,861,'宝山区',310113,310113),(872,2,861,'嘉定区',310114,310114),(873,2,861,'浦东新区',310115,310115),(874,2,861,'金山区',310116,310116),(875,2,861,'松江区',310117,310117),(876,2,861,'青浦区',310118,310118),(877,2,861,'奉贤区',310120,310120),(878,1,860,'县',310200,310200),(879,2,878,'崇明县',310230,310230),(880,0,NULL,'江苏省',320000,320000),(881,1,880,'南京市',320100,320100),(882,2,881,'市辖区',320101,320101),(883,2,881,'玄武区',320102,320102),(884,2,881,'秦淮区',320104,320104),(885,2,881,'建邺区',320105,320105),(886,2,881,'鼓楼区',320106,320106),(887,2,881,'浦口区',320111,320111),(888,2,881,'栖霞区',320113,320113),(889,2,881,'雨花台区',320114,320114),(890,2,881,'江宁区',320115,320115),(891,2,881,'六合区',320116,320116),(892,2,881,'溧水区',320117,320117),(893,2,881,'高淳区',320118,320118),(894,1,880,'无锡市',320200,320200),(895,2,894,'市辖区',320201,320201),(896,2,894,'崇安区',320202,320202),(897,2,894,'南长区',320203,320203),(898,2,894,'北塘区',320204,320204),(899,2,894,'锡山区',320205,320205),(900,2,894,'惠山区',320206,320206),(901,2,894,'滨湖区',320211,320211),(902,2,894,'江阴市',320281,320281),(903,2,894,'宜兴市',320282,320282),(904,1,880,'徐州市',320300,320300),(905,2,904,'市辖区',320301,320301),(906,2,904,'鼓楼区',320302,320302),(907,2,904,'云龙区',320303,320303),(908,2,904,'贾汪区',320305,320305),(909,2,904,'泉山区',320311,320311),(910,2,904,'铜山区',320312,320312),(911,2,904,'丰县',320321,320321),(912,2,904,'沛县',320322,320322),(913,2,904,'睢宁县',320324,320324),(914,2,904,'新沂市',320381,320381),(915,2,904,'邳州市',320382,320382),(916,1,880,'常州市',320400,320400),(917,2,916,'市辖区',320401,320401),(918,2,916,'天宁区',320402,320402),(919,2,916,'钟楼区',320404,320404),(920,2,916,'戚墅堰区',320405,320405),(921,2,916,'新北区',320411,320411),(922,2,916,'武进区',320412,320412),(923,2,916,'溧阳市',320481,320481),(924,2,916,'金坛市',320482,320482),(925,1,880,'苏州市',320500,320500),(926,2,925,'市辖区',320501,320501),(927,2,925,'虎丘区',320505,320505),(928,2,925,'吴中区',320506,320506),(929,2,925,'相城区',320507,320507),(930,2,925,'姑苏区',320508,320508),(931,2,925,'吴江区',320509,320509),(932,2,925,'常熟市',320581,320581),(933,2,925,'张家港市',320582,320582),(934,2,925,'昆山市',320583,320583),(935,2,925,'太仓市',320585,320585),(936,1,880,'南通市',320600,320600),(937,2,936,'市辖区',320601,320601),(938,2,936,'崇川区',320602,320602),(939,2,936,'港闸区',320611,320611),(940,2,936,'通州区',320612,320612),(941,2,936,'海安县',320621,320621),(942,2,936,'如东县',320623,320623),(943,2,936,'启东市',320681,320681),(944,2,936,'如皋市',320682,320682),(945,2,936,'海门市',320684,320684),(946,1,880,'连云港市',320700,320700),(947,2,946,'市辖区',320701,320701),(948,2,946,'连云区',320703,320703),(949,2,946,'海州区',320706,320706),(950,2,946,'赣榆区',320707,320707),(951,2,946,'东海县',320722,320722),(952,2,946,'灌云县',320723,320723),(953,2,946,'灌南县',320724,320724),(954,1,880,'淮安市',320800,320800),(955,2,954,'市辖区',320801,320801),(956,2,954,'清河区',320802,320802),(957,2,954,'淮安区',320803,320803),(958,2,954,'淮阴区',320804,320804),(959,2,954,'清浦区',320811,320811),(960,2,954,'涟水县',320826,320826),(961,2,954,'洪泽县',320829,320829),(962,2,954,'盱眙县',320830,320830),(963,2,954,'金湖县',320831,320831),(964,1,880,'盐城市',320900,320900),(965,2,964,'市辖区',320901,320901),(966,2,964,'亭湖区',320902,320902),(967,2,964,'盐都区',320903,320903),(968,2,964,'响水县',320921,320921),(969,2,964,'滨海县',320922,320922),(970,2,964,'阜宁县',320923,320923),(971,2,964,'射阳县',320924,320924),(972,2,964,'建湖县',320925,320925),(973,2,964,'东台市',320981,320981),(974,2,964,'大丰市',320982,320982),(975,1,880,'扬州市',321000,321000),(976,2,975,'市辖区',321001,321001),(977,2,975,'广陵区',321002,321002),(978,2,975,'邗江区',321003,321003),(979,2,975,'江都区',321012,321012),(980,2,975,'宝应县',321023,321023),(981,2,975,'仪征市',321081,321081),(982,2,975,'高邮市',321084,321084),(983,1,880,'镇江市',321100,321100),(984,2,983,'市辖区',321101,321101),(985,2,983,'京口区',321102,321102),(986,2,983,'润州区',321111,321111),(987,2,983,'丹徒区',321112,321112),(988,2,983,'丹阳市',321181,321181),(989,2,983,'扬中市',321182,321182),(990,2,983,'句容市',321183,321183),(991,1,880,'泰州市',321200,321200),(992,2,991,'市辖区',321201,321201),(993,2,991,'海陵区',321202,321202),(994,2,991,'高港区',321203,321203),(995,2,991,'姜堰区',321204,321204),(996,2,991,'兴化市',321281,321281),(997,2,991,'靖江市',321282,321282),(998,2,991,'泰兴市',321283,321283),(999,1,880,'宿迁市',321300,321300),(1000,2,999,'市辖区',321301,321301),(1001,2,999,'宿城区',321302,321302),(1002,2,999,'宿豫区',321311,321311),(1003,2,999,'沭阳县',321322,321322),(1004,2,999,'泗阳县',321323,321323),(1005,2,999,'泗洪县',321324,321324),(1006,0,NULL,'浙江省',330000,330000),(1007,1,1006,'杭州市',330100,330100),(1008,2,1007,'市辖区',330101,330101),(1009,2,1007,'上城区',330102,330102),(1010,2,1007,'下城区',330103,330103),(1011,2,1007,'江干区',330104,330104),(1012,2,1007,'拱墅区',330105,330105),(1013,2,1007,'西湖区',330106,330106),(1014,2,1007,'滨江区',330108,330108),(1015,2,1007,'萧山区',330109,330109),(1016,2,1007,'余杭区',330110,330110),(1017,2,1007,'桐庐县',330122,330122),(1018,2,1007,'淳安县',330127,330127),(1019,2,1007,'建德市',330182,330182),(1020,2,1007,'富阳市',330183,330183),(1021,2,1007,'临安市',330185,330185),(1022,1,1006,'宁波市',330200,330200),(1023,2,1022,'市辖区',330201,330201),(1024,2,1022,'海曙区',330203,330203),(1025,2,1022,'江东区',330204,330204),(1026,2,1022,'江北区',330205,330205),(1027,2,1022,'北仑区',330206,330206),(1028,2,1022,'镇海区',330211,330211),(1029,2,1022,'鄞州区',330212,330212),(1030,2,1022,'象山县',330225,330225),(1031,2,1022,'宁海县',330226,330226),(1032,2,1022,'余姚市',330281,330281),(1033,2,1022,'慈溪市',330282,330282),(1034,2,1022,'奉化市',330283,330283),(1035,1,1006,'温州市',330300,330300),(1036,2,1035,'市辖区',330301,330301),(1037,2,1035,'鹿城区',330302,330302),(1038,2,1035,'龙湾区',330303,330303),(1039,2,1035,'瓯海区',330304,330304),(1040,2,1035,'洞头县',330322,330322),(1041,2,1035,'永嘉县',330324,330324),(1042,2,1035,'平阳县',330326,330326),(1043,2,1035,'苍南县',330327,330327),(1044,2,1035,'文成县',330328,330328),(1045,2,1035,'泰顺县',330329,330329),(1046,2,1035,'瑞安市',330381,330381),(1047,2,1035,'乐清市',330382,330382),(1048,1,1006,'嘉兴市',330400,330400),(1049,2,1048,'市辖区',330401,330401),(1050,2,1048,'南湖区',330402,330402),(1051,2,1048,'秀洲区',330411,330411),(1052,2,1048,'嘉善县',330421,330421),(1053,2,1048,'海盐县',330424,330424),(1054,2,1048,'海宁市',330481,330481),(1055,2,1048,'平湖市',330482,330482),(1056,2,1048,'桐乡市',330483,330483),(1057,1,1006,'湖州市',330500,330500),(1058,2,1057,'市辖区',330501,330501),(1059,2,1057,'吴兴区',330502,330502),(1060,2,1057,'南浔区',330503,330503),(1061,2,1057,'德清县',330521,330521),(1062,2,1057,'长兴县',330522,330522),(1063,2,1057,'安吉县',330523,330523),(1064,1,1006,'绍兴市',330600,330600),(1065,2,1064,'市辖区',330601,330601),(1066,2,1064,'越城区',330602,330602),(1067,2,1064,'柯桥区',330603,330603),(1068,2,1064,'上虞区',330604,330604),(1069,2,1064,'新昌县',330624,330624),(1070,2,1064,'诸暨市',330681,330681),(1071,2,1064,'嵊州市',330683,330683),(1072,1,1006,'金华市',330700,330700),(1073,2,1072,'市辖区',330701,330701),(1074,2,1072,'婺城区',330702,330702),(1075,2,1072,'金东区',330703,330703),(1076,2,1072,'武义县',330723,330723),(1077,2,1072,'浦江县',330726,330726),(1078,2,1072,'磐安县',330727,330727),(1079,2,1072,'兰溪市',330781,330781),(1080,2,1072,'义乌市',330782,330782),(1081,2,1072,'东阳市',330783,330783),(1082,2,1072,'永康市',330784,330784),(1083,1,1006,'衢州市',330800,330800),(1084,2,1083,'市辖区',330801,330801),(1085,2,1083,'柯城区',330802,330802),(1086,2,1083,'衢江区',330803,330803),(1087,2,1083,'常山县',330822,330822),(1088,2,1083,'开化县',330824,330824),(1089,2,1083,'龙游县',330825,330825),(1090,2,1083,'江山市',330881,330881),(1091,1,1006,'舟山市',330900,330900),(1092,2,1091,'市辖区',330901,330901),(1093,2,1091,'定海区',330902,330902),(1094,2,1091,'普陀区',330903,330903),(1095,2,1091,'岱山县',330921,330921),(1096,2,1091,'嵊泗县',330922,330922),(1097,1,1006,'台州市',331000,331000),(1098,2,1097,'市辖区',331001,331001),(1099,2,1097,'椒江区',331002,331002),(1100,2,1097,'黄岩区',331003,331003),(1101,2,1097,'路桥区',331004,331004),(1102,2,1097,'玉环县',331021,331021),(1103,2,1097,'三门县',331022,331022),(1104,2,1097,'天台县',331023,331023),(1105,2,1097,'仙居县',331024,331024),(1106,2,1097,'温岭市',331081,331081),(1107,2,1097,'临海市',331082,331082),(1108,1,1006,'丽水市',331100,331100),(1109,2,1108,'市辖区',331101,331101),(1110,2,1108,'莲都区',331102,331102),(1111,2,1108,'青田县',331121,331121),(1112,2,1108,'缙云县',331122,331122),(1113,2,1108,'遂昌县',331123,331123),(1114,2,1108,'松阳县',331124,331124),(1115,2,1108,'云和县',331125,331125),(1116,2,1108,'庆元县',331126,331126),(1117,2,1108,'景宁畲族自治县',331127,331127),(1118,2,1108,'龙泉市',331181,331181),(1119,0,NULL,'安徽省',340000,340000),(1120,1,1119,'合肥市',340100,340100),(1121,2,1120,'市辖区',340101,340101),(1122,2,1120,'瑶海区',340102,340102),(1123,2,1120,'庐阳区',340103,340103),(1124,2,1120,'蜀山区',340104,340104),(1125,2,1120,'包河区',340111,340111),(1126,2,1120,'长丰县',340121,340121),(1127,2,1120,'肥东县',340122,340122),(1128,2,1120,'肥西县',340123,340123),(1129,2,1120,'庐江县',340124,340124),(1130,2,1120,'巢湖市',340181,340181),(1131,1,1119,'芜湖市',340200,340200),(1132,2,1131,'市辖区',340201,340201),(1133,2,1131,'镜湖区',340202,340202),(1134,2,1131,'弋江区',340203,340203),(1135,2,1131,'鸠江区',340207,340207),(1136,2,1131,'三山区',340208,340208),(1137,2,1131,'芜湖县',340221,340221),(1138,2,1131,'繁昌县',340222,340222),(1139,2,1131,'南陵县',340223,340223),(1140,2,1131,'无为县',340225,340225),(1141,1,1119,'蚌埠市',340300,340300),(1142,2,1141,'市辖区',340301,340301),(1143,2,1141,'龙子湖区',340302,340302),(1144,2,1141,'蚌山区',340303,340303),(1145,2,1141,'禹会区',340304,340304),(1146,2,1141,'淮上区',340311,340311),(1147,2,1141,'怀远县',340321,340321),(1148,2,1141,'五河县',340322,340322),(1149,2,1141,'固镇县',340323,340323),(1150,1,1119,'淮南市',340400,340400),(1151,2,1150,'市辖区',340401,340401),(1152,2,1150,'大通区',340402,340402),(1153,2,1150,'田家庵区',340403,340403),(1154,2,1150,'谢家集区',340404,340404),(1155,2,1150,'八公山区',340405,340405),(1156,2,1150,'潘集区',340406,340406),(1157,2,1150,'凤台县',340421,340421),(1158,1,1119,'马鞍山市',340500,340500),(1159,2,1158,'市辖区',340501,340501),(1160,2,1158,'花山区',340503,340503),(1161,2,1158,'雨山区',340504,340504),(1162,2,1158,'博望区',340506,340506),(1163,2,1158,'当涂县',340521,340521),(1164,2,1158,'含山县',340522,340522),(1165,2,1158,'和县',340523,340523),(1166,1,1119,'淮北市',340600,340600),(1167,2,1166,'市辖区',340601,340601),(1168,2,1166,'杜集区',340602,340602),(1169,2,1166,'相山区',340603,340603),(1170,2,1166,'烈山区',340604,340604),(1171,2,1166,'濉溪县',340621,340621),(1172,1,1119,'铜陵市',340700,340700),(1173,2,1172,'市辖区',340701,340701),(1174,2,1172,'铜官山区',340702,340702),(1175,2,1172,'狮子山区',340703,340703),(1176,2,1172,'郊区',340711,340711),(1177,2,1172,'铜陵县',340721,340721),(1178,1,1119,'安庆市',340800,340800),(1179,2,1178,'市辖区',340801,340801),(1180,2,1178,'迎江区',340802,340802),(1181,2,1178,'大观区',340803,340803),(1182,2,1178,'宜秀区',340811,340811),(1183,2,1178,'怀宁县',340822,340822),(1184,2,1178,'枞阳县',340823,340823),(1185,2,1178,'潜山县',340824,340824),(1186,2,1178,'太湖县',340825,340825),(1187,2,1178,'宿松县',340826,340826),(1188,2,1178,'望江县',340827,340827),(1189,2,1178,'岳西县',340828,340828),(1190,2,1178,'桐城市',340881,340881),(1191,1,1119,'黄山市',341000,341000),(1192,2,1191,'市辖区',341001,341001),(1193,2,1191,'屯溪区',341002,341002),(1194,2,1191,'黄山区',341003,341003),(1195,2,1191,'徽州区',341004,341004),(1196,2,1191,'歙县',341021,341021),(1197,2,1191,'休宁县',341022,341022),(1198,2,1191,'黟县',341023,341023),(1199,2,1191,'祁门县',341024,341024),(1200,1,1119,'滁州市',341100,341100),(1201,2,1200,'市辖区',341101,341101),(1202,2,1200,'琅琊区',341102,341102),(1203,2,1200,'南谯区',341103,341103),(1204,2,1200,'来安县',341122,341122),(1205,2,1200,'全椒县',341124,341124),(1206,2,1200,'定远县',341125,341125),(1207,2,1200,'凤阳县',341126,341126),(1208,2,1200,'天长市',341181,341181),(1209,2,1200,'明光市',341182,341182),(1210,1,1119,'阜阳市',341200,341200),(1211,2,1210,'市辖区',341201,341201),(1212,2,1210,'颍州区',341202,341202),(1213,2,1210,'颍东区',341203,341203),(1214,2,1210,'颍泉区',341204,341204),(1215,2,1210,'临泉县',341221,341221),(1216,2,1210,'太和县',341222,341222),(1217,2,1210,'阜南县',341225,341225),(1218,2,1210,'颍上县',341226,341226),(1219,2,1210,'界首市',341282,341282),(1220,1,1119,'宿州市',341300,341300),(1221,2,1220,'市辖区',341301,341301),(1222,2,1220,'埇桥区',341302,341302),(1223,2,1220,'砀山县',341321,341321),(1224,2,1220,'萧县',341322,341322),(1225,2,1220,'灵璧县',341323,341323),(1226,2,1220,'泗县',341324,341324),(1227,1,1119,'六安市',341500,341500),(1228,2,1227,'市辖区',341501,341501),(1229,2,1227,'金安区',341502,341502),(1230,2,1227,'裕安区',341503,341503),(1231,2,1227,'寿县',341521,341521),(1232,2,1227,'霍邱县',341522,341522),(1233,2,1227,'舒城县',341523,341523),(1234,2,1227,'金寨县',341524,341524),(1235,2,1227,'霍山县',341525,341525),(1236,1,1119,'亳州市',341600,341600),(1237,2,1236,'市辖区',341601,341601),(1238,2,1236,'谯城区',341602,341602),(1239,2,1236,'涡阳县',341621,341621),(1240,2,1236,'蒙城县',341622,341622),(1241,2,1236,'利辛县',341623,341623),(1242,1,1119,'池州市',341700,341700),(1243,2,1242,'市辖区',341701,341701),(1244,2,1242,'贵池区',341702,341702),(1245,2,1242,'东至县',341721,341721),(1246,2,1242,'石台县',341722,341722),(1247,2,1242,'青阳县',341723,341723),(1248,1,1119,'宣城市',341800,341800),(1249,2,1248,'市辖区',341801,341801),(1250,2,1248,'宣州区',341802,341802),(1251,2,1248,'郎溪县',341821,341821),(1252,2,1248,'广德县',341822,341822),(1253,2,1248,'泾县',341823,341823),(1254,2,1248,'绩溪县',341824,341824),(1255,2,1248,'旌德县',341825,341825),(1256,2,1248,'宁国市',341881,341881),(1257,0,NULL,'福建省',350000,350000),(1258,1,1257,'福州市',350100,350100),(1259,2,1258,'市辖区',350101,350101),(1260,2,1258,'鼓楼区',350102,350102),(1261,2,1258,'台江区',350103,350103),(1262,2,1258,'仓山区',350104,350104),(1263,2,1258,'马尾区',350105,350105),(1264,2,1258,'晋安区',350111,350111),(1265,2,1258,'闽侯县',350121,350121),(1266,2,1258,'连江县',350122,350122),(1267,2,1258,'罗源县',350123,350123),(1268,2,1258,'闽清县',350124,350124),(1269,2,1258,'永泰县',350125,350125),(1270,2,1258,'平潭县',350128,350128),(1271,2,1258,'福清市',350181,350181),(1272,2,1258,'长乐市',350182,350182),(1273,1,1257,'厦门市',350200,350200),(1274,2,1273,'市辖区',350201,350201),(1275,2,1273,'思明区',350203,350203),(1276,2,1273,'海沧区',350205,350205),(1277,2,1273,'湖里区',350206,350206),(1278,2,1273,'集美区',350211,350211),(1279,2,1273,'同安区',350212,350212),(1280,2,1273,'翔安区',350213,350213),(1281,1,1257,'莆田市',350300,350300),(1282,2,1281,'市辖区',350301,350301),(1283,2,1281,'城厢区',350302,350302),(1284,2,1281,'涵江区',350303,350303),(1285,2,1281,'荔城区',350304,350304),(1286,2,1281,'秀屿区',350305,350305),(1287,2,1281,'仙游县',350322,350322),(1288,1,1257,'三明市',350400,350400),(1289,2,1288,'市辖区',350401,350401),(1290,2,1288,'梅列区',350402,350402),(1291,2,1288,'三元区',350403,350403),(1292,2,1288,'明溪县',350421,350421),(1293,2,1288,'清流县',350423,350423),(1294,2,1288,'宁化县',350424,350424),(1295,2,1288,'大田县',350425,350425),(1296,2,1288,'尤溪县',350426,350426),(1297,2,1288,'沙县',350427,350427),(1298,2,1288,'将乐县',350428,350428),(1299,2,1288,'泰宁县',350429,350429),(1300,2,1288,'建宁县',350430,350430),(1301,2,1288,'永安市',350481,350481),(1302,1,1257,'泉州市',350500,350500),(1303,2,1302,'市辖区',350501,350501),(1304,2,1302,'鲤城区',350502,350502),(1305,2,1302,'丰泽区',350503,350503),(1306,2,1302,'洛江区',350504,350504),(1307,2,1302,'泉港区',350505,350505),(1308,2,1302,'惠安县',350521,350521),(1309,2,1302,'安溪县',350524,350524),(1310,2,1302,'永春县',350525,350525),(1311,2,1302,'德化县',350526,350526),(1312,2,1302,'金门县',350527,350527),(1313,2,1302,'石狮市',350581,350581),(1314,2,1302,'晋江市',350582,350582),(1315,2,1302,'南安市',350583,350583),(1316,1,1257,'漳州市',350600,350600),(1317,2,1316,'市辖区',350601,350601),(1318,2,1316,'芗城区',350602,350602),(1319,2,1316,'龙文区',350603,350603),(1320,2,1316,'云霄县',350622,350622),(1321,2,1316,'漳浦县',350623,350623),(1322,2,1316,'诏安县',350624,350624),(1323,2,1316,'长泰县',350625,350625),(1324,2,1316,'东山县',350626,350626),(1325,2,1316,'南靖县',350627,350627),(1326,2,1316,'平和县',350628,350628),(1327,2,1316,'华安县',350629,350629),(1328,2,1316,'龙海市',350681,350681),(1329,1,1257,'南平市',350700,350700),(1330,2,1329,'市辖区',350701,350701),(1331,2,1329,'延平区',350702,350702),(1332,2,1329,'顺昌县',350721,350721),(1333,2,1329,'浦城县',350722,350722),(1334,2,1329,'光泽县',350723,350723),(1335,2,1329,'松溪县',350724,350724),(1336,2,1329,'政和县',350725,350725),(1337,2,1329,'邵武市',350781,350781),(1338,2,1329,'武夷山市',350782,350782),(1339,2,1329,'建瓯市',350783,350783),(1340,2,1329,'建阳市',350784,350784),(1341,1,1257,'龙岩市',350800,350800),(1342,2,1341,'市辖区',350801,350801),(1343,2,1341,'新罗区',350802,350802),(1344,2,1341,'长汀县',350821,350821),(1345,2,1341,'永定县',350822,350822),(1346,2,1341,'上杭县',350823,350823),(1347,2,1341,'武平县',350824,350824),(1348,2,1341,'连城县',350825,350825),(1349,2,1341,'漳平市',350881,350881),(1350,1,1257,'宁德市',350900,350900),(1351,2,1350,'市辖区',350901,350901),(1352,2,1350,'蕉城区',350902,350902),(1353,2,1350,'霞浦县',350921,350921),(1354,2,1350,'古田县',350922,350922),(1355,2,1350,'屏南县',350923,350923),(1356,2,1350,'寿宁县',350924,350924),(1357,2,1350,'周宁县',350925,350925),(1358,2,1350,'柘荣县',350926,350926),(1359,2,1350,'福安市',350981,350981),(1360,2,1350,'福鼎市',350982,350982),(1361,0,NULL,'江西省',360000,360000),(1362,1,1361,'南昌市',360100,360100),(1363,2,1362,'市辖区',360101,360101),(1364,2,1362,'东湖区',360102,360102),(1365,2,1362,'西湖区',360103,360103),(1366,2,1362,'青云谱区',360104,360104),(1367,2,1362,'湾里区',360105,360105),(1368,2,1362,'青山湖区',360111,360111),(1369,2,1362,'南昌县',360121,360121),(1370,2,1362,'新建县',360122,360122),(1371,2,1362,'安义县',360123,360123),(1372,2,1362,'进贤县',360124,360124),(1373,1,1361,'景德镇市',360200,360200),(1374,2,1373,'市辖区',360201,360201),(1375,2,1373,'昌江区',360202,360202),(1376,2,1373,'珠山区',360203,360203),(1377,2,1373,'浮梁县',360222,360222),(1378,2,1373,'乐平市',360281,360281),(1379,1,1361,'萍乡市',360300,360300),(1380,2,1379,'市辖区',360301,360301),(1381,2,1379,'安源区',360302,360302),(1382,2,1379,'湘东区',360313,360313),(1383,2,1379,'莲花县',360321,360321),(1384,2,1379,'上栗县',360322,360322),(1385,2,1379,'芦溪县',360323,360323),(1386,1,1361,'九江市',360400,360400),(1387,2,1386,'市辖区',360401,360401),(1388,2,1386,'庐山区',360402,360402),(1389,2,1386,'浔阳区',360403,360403),(1390,2,1386,'九江县',360421,360421),(1391,2,1386,'武宁县',360423,360423),(1392,2,1386,'修水县',360424,360424),(1393,2,1386,'永修县',360425,360425),(1394,2,1386,'德安县',360426,360426),(1395,2,1386,'星子县',360427,360427),(1396,2,1386,'都昌县',360428,360428),(1397,2,1386,'湖口县',360429,360429),(1398,2,1386,'彭泽县',360430,360430),(1399,2,1386,'瑞昌市',360481,360481),(1400,2,1386,'共青城市',360482,360482),(1401,1,1361,'新余市',360500,360500),(1402,2,1401,'市辖区',360501,360501),(1403,2,1401,'渝水区',360502,360502),(1404,2,1401,'分宜县',360521,360521),(1405,1,1361,'鹰潭市',360600,360600),(1406,2,1405,'市辖区',360601,360601),(1407,2,1405,'月湖区',360602,360602),(1408,2,1405,'余江县',360622,360622),(1409,2,1405,'贵溪市',360681,360681),(1410,1,1361,'赣州市',360700,360700),(1411,2,1410,'市辖区',360701,360701),(1412,2,1410,'章贡区',360702,360702),(1413,2,1410,'南康区',360703,360703),(1414,2,1410,'赣县',360721,360721),(1415,2,1410,'信丰县',360722,360722),(1416,2,1410,'大余县',360723,360723),(1417,2,1410,'上犹县',360724,360724),(1418,2,1410,'崇义县',360725,360725),(1419,2,1410,'安远县',360726,360726),(1420,2,1410,'龙南县',360727,360727),(1421,2,1410,'定南县',360728,360728),(1422,2,1410,'全南县',360729,360729),(1423,2,1410,'宁都县',360730,360730),(1424,2,1410,'于都县',360731,360731),(1425,2,1410,'兴国县',360732,360732),(1426,2,1410,'会昌县',360733,360733),(1427,2,1410,'寻乌县',360734,360734),(1428,2,1410,'石城县',360735,360735),(1429,2,1410,'瑞金市',360781,360781),(1430,1,1361,'吉安市',360800,360800),(1431,2,1430,'市辖区',360801,360801),(1432,2,1430,'吉州区',360802,360802),(1433,2,1430,'青原区',360803,360803),(1434,2,1430,'吉安县',360821,360821),(1435,2,1430,'吉水县',360822,360822),(1436,2,1430,'峡江县',360823,360823),(1437,2,1430,'新干县',360824,360824),(1438,2,1430,'永丰县',360825,360825),(1439,2,1430,'泰和县',360826,360826),(1440,2,1430,'遂川县',360827,360827),(1441,2,1430,'万安县',360828,360828),(1442,2,1430,'安福县',360829,360829),(1443,2,1430,'永新县',360830,360830),(1444,2,1430,'井冈山市',360881,360881),(1445,1,1361,'宜春市',360900,360900),(1446,2,1445,'市辖区',360901,360901),(1447,2,1445,'袁州区',360902,360902),(1448,2,1445,'奉新县',360921,360921),(1449,2,1445,'万载县',360922,360922),(1450,2,1445,'上高县',360923,360923),(1451,2,1445,'宜丰县',360924,360924),(1452,2,1445,'靖安县',360925,360925),(1453,2,1445,'铜鼓县',360926,360926),(1454,2,1445,'丰城市',360981,360981),(1455,2,1445,'樟树市',360982,360982),(1456,2,1445,'高安市',360983,360983),(1457,1,1361,'抚州市',361000,361000),(1458,2,1457,'市辖区',361001,361001),(1459,2,1457,'临川区',361002,361002),(1460,2,1457,'南城县',361021,361021),(1461,2,1457,'黎川县',361022,361022),(1462,2,1457,'南丰县',361023,361023),(1463,2,1457,'崇仁县',361024,361024),(1464,2,1457,'乐安县',361025,361025),(1465,2,1457,'宜黄县',361026,361026),(1466,2,1457,'金溪县',361027,361027),(1467,2,1457,'资溪县',361028,361028),(1468,2,1457,'东乡县',361029,361029),(1469,2,1457,'广昌县',361030,361030),(1470,1,1361,'上饶市',361100,361100),(1471,2,1470,'市辖区',361101,361101),(1472,2,1470,'信州区',361102,361102),(1473,2,1470,'上饶县',361121,361121),(1474,2,1470,'广丰县',361122,361122),(1475,2,1470,'玉山县',361123,361123),(1476,2,1470,'铅山县',361124,361124),(1477,2,1470,'横峰县',361125,361125),(1478,2,1470,'弋阳县',361126,361126),(1479,2,1470,'余干县',361127,361127),(1480,2,1470,'鄱阳县',361128,361128),(1481,2,1470,'万年县',361129,361129),(1482,2,1470,'婺源县',361130,361130),(1483,2,1470,'德兴市',361181,361181),(1484,0,NULL,'山东省',370000,370000),(1485,1,1484,'济南市',370100,370100),(1486,2,1485,'市辖区',370101,370101),(1487,2,1485,'历下区',370102,370102),(1488,2,1485,'市中区',370103,370103),(1489,2,1485,'槐荫区',370104,370104),(1490,2,1485,'天桥区',370105,370105),(1491,2,1485,'历城区',370112,370112),(1492,2,1485,'长清区',370113,370113),(1493,2,1485,'平阴县',370124,370124),(1494,2,1485,'济阳县',370125,370125),(1495,2,1485,'商河县',370126,370126),(1496,2,1485,'章丘市',370181,370181),(1497,1,1484,'青岛市',370200,370200),(1498,2,1497,'市辖区',370201,370201),(1499,2,1497,'市南区',370202,370202),(1500,2,1497,'市北区',370203,370203),(1501,2,1497,'黄岛区',370211,370211),(1502,2,1497,'崂山区',370212,370212),(1503,2,1497,'李沧区',370213,370213),(1504,2,1497,'城阳区',370214,370214),(1505,2,1497,'胶州市',370281,370281),(1506,2,1497,'即墨市',370282,370282),(1507,2,1497,'平度市',370283,370283),(1508,2,1497,'莱西市',370285,370285),(1509,1,1484,'淄博市',370300,370300),(1510,2,1509,'市辖区',370301,370301),(1511,2,1509,'淄川区',370302,370302),(1512,2,1509,'张店区',370303,370303),(1513,2,1509,'博山区',370304,370304),(1514,2,1509,'临淄区',370305,370305),(1515,2,1509,'周村区',370306,370306),(1516,2,1509,'桓台县',370321,370321),(1517,2,1509,'高青县',370322,370322),(1518,2,1509,'沂源县',370323,370323),(1519,1,1484,'枣庄市',370400,370400),(1520,2,1519,'市辖区',370401,370401),(1521,2,1519,'市中区',370402,370402),(1522,2,1519,'薛城区',370403,370403),(1523,2,1519,'峄城区',370404,370404),(1524,2,1519,'台儿庄区',370405,370405),(1525,2,1519,'山亭区',370406,370406),(1526,2,1519,'滕州市',370481,370481),(1527,1,1484,'东营市',370500,370500),(1528,2,1527,'市辖区',370501,370501),(1529,2,1527,'东营区',370502,370502),(1530,2,1527,'河口区',370503,370503),(1531,2,1527,'垦利县',370521,370521),(1532,2,1527,'利津县',370522,370522),(1533,2,1527,'广饶县',370523,370523),(1534,1,1484,'烟台市',370600,370600),(1535,2,1534,'市辖区',370601,370601),(1536,2,1534,'芝罘区',370602,370602),(1537,2,1534,'福山区',370611,370611),(1538,2,1534,'牟平区',370612,370612),(1539,2,1534,'莱山区',370613,370613),(1540,2,1534,'长岛县',370634,370634),(1541,2,1534,'龙口市',370681,370681),(1542,2,1534,'莱阳市',370682,370682),(1543,2,1534,'莱州市',370683,370683),(1544,2,1534,'蓬莱市',370684,370684),(1545,2,1534,'招远市',370685,370685),(1546,2,1534,'栖霞市',370686,370686),(1547,2,1534,'海阳市',370687,370687),(1548,1,1484,'潍坊市',370700,370700),(1549,2,1548,'市辖区',370701,370701),(1550,2,1548,'潍城区',370702,370702),(1551,2,1548,'寒亭区',370703,370703),(1552,2,1548,'坊子区',370704,370704),(1553,2,1548,'奎文区',370705,370705),(1554,2,1548,'临朐县',370724,370724),(1555,2,1548,'昌乐县',370725,370725),(1556,2,1548,'青州市',370781,370781),(1557,2,1548,'诸城市',370782,370782),(1558,2,1548,'寿光市',370783,370783),(1559,2,1548,'安丘市',370784,370784),(1560,2,1548,'高密市',370785,370785),(1561,2,1548,'昌邑市',370786,370786),(1562,1,1484,'济宁市',370800,370800),(1563,2,1562,'市辖区',370801,370801),(1564,2,1562,'任城区',370811,370811),(1565,2,1562,'兖州区',370812,370812),(1566,2,1562,'微山县',370826,370826),(1567,2,1562,'鱼台县',370827,370827),(1568,2,1562,'金乡县',370828,370828),(1569,2,1562,'嘉祥县',370829,370829),(1570,2,1562,'汶上县',370830,370830),(1571,2,1562,'泗水县',370831,370831),(1572,2,1562,'梁山县',370832,370832),(1573,2,1562,'曲阜市',370881,370881),(1574,2,1562,'邹城市',370883,370883),(1575,1,1484,'泰安市',370900,370900),(1576,2,1575,'市辖区',370901,370901),(1577,2,1575,'泰山区',370902,370902),(1578,2,1575,'岱岳区',370911,370911),(1579,2,1575,'宁阳县',370921,370921),(1580,2,1575,'东平县',370923,370923),(1581,2,1575,'新泰市',370982,370982),(1582,2,1575,'肥城市',370983,370983),(1583,1,1484,'威海市',371000,371000),(1584,2,1583,'市辖区',371001,371001),(1585,2,1583,'环翠区',371002,371002),(1586,2,1583,'文登区',371003,371003),(1587,2,1583,'荣成市',371082,371082),(1588,2,1583,'乳山市',371083,371083),(1589,1,1484,'日照市',371100,371100),(1590,2,1589,'市辖区',371101,371101),(1591,2,1589,'东港区',371102,371102),(1592,2,1589,'岚山区',371103,371103),(1593,2,1589,'五莲县',371121,371121),(1594,2,1589,'莒县',371122,371122),(1595,1,1484,'莱芜市',371200,371200),(1596,2,1595,'市辖区',371201,371201),(1597,2,1595,'莱城区',371202,371202),(1598,2,1595,'钢城区',371203,371203),(1599,1,1484,'临沂市',371300,371300),(1600,2,1599,'市辖区',371301,371301),(1601,2,1599,'兰山区',371302,371302),(1602,2,1599,'罗庄区',371311,371311),(1603,2,1599,'河东区',371312,371312),(1604,2,1599,'沂南县',371321,371321),(1605,2,1599,'郯城县',371322,371322),(1606,2,1599,'沂水县',371323,371323),(1607,2,1599,'兰陵县',371324,371324),(1608,2,1599,'费县',371325,371325),(1609,2,1599,'平邑县',371326,371326),(1610,2,1599,'莒南县',371327,371327),(1611,2,1599,'蒙阴县',371328,371328),(1612,2,1599,'临沭县',371329,371329),(1613,1,1484,'德州市',371400,371400),(1614,2,1613,'市辖区',371401,371401),(1615,2,1613,'德城区',371402,371402),(1616,2,1613,'陵城区',371403,371403),(1617,2,1613,'宁津县',371422,371422),(1618,2,1613,'庆云县',371423,371423),(1619,2,1613,'临邑县',371424,371424),(1620,2,1613,'齐河县',371425,371425),(1621,2,1613,'平原县',371426,371426),(1622,2,1613,'夏津县',371427,371427),(1623,2,1613,'武城县',371428,371428),(1624,2,1613,'乐陵市',371481,371481),(1625,2,1613,'禹城市',371482,371482),(1626,1,1484,'聊城市',371500,371500),(1627,2,1626,'市辖区',371501,371501),(1628,2,1626,'东昌府区',371502,371502),(1629,2,1626,'阳谷县',371521,371521),(1630,2,1626,'莘县',371522,371522),(1631,2,1626,'茌平县',371523,371523),(1632,2,1626,'东阿县',371524,371524),(1633,2,1626,'冠县',371525,371525),(1634,2,1626,'高唐县',371526,371526),(1635,2,1626,'临清市',371581,371581),(1636,1,1484,'滨州市',371600,371600),(1637,2,1636,'市辖区',371601,371601),(1638,2,1636,'滨城区',371602,371602),(1639,2,1636,'沾化区',371603,371603),(1640,2,1636,'惠民县',371621,371621),(1641,2,1636,'阳信县',371622,371622),(1642,2,1636,'无棣县',371623,371623),(1643,2,1636,'博兴县',371625,371625),(1644,2,1636,'邹平县',371626,371626),(1645,1,1484,'菏泽市',371700,371700),(1646,2,1645,'市辖区',371701,371701),(1647,2,1645,'牡丹区',371702,371702),(1648,2,1645,'曹县',371721,371721),(1649,2,1645,'单县',371722,371722),(1650,2,1645,'成武县',371723,371723),(1651,2,1645,'巨野县',371724,371724),(1652,2,1645,'郓城县',371725,371725),(1653,2,1645,'鄄城县',371726,371726),(1654,2,1645,'定陶县',371727,371727),(1655,2,1645,'东明县',371728,371728),(1656,0,NULL,'河南省',410000,410000),(1657,1,1656,'郑州市',410100,410100),(1658,2,1657,'市辖区',410101,410101),(1659,2,1657,'中原区',410102,410102),(1660,2,1657,'二七区',410103,410103),(1661,2,1657,'管城回族区',410104,410104),(1662,2,1657,'金水区',410105,410105),(1663,2,1657,'上街区',410106,410106),(1664,2,1657,'惠济区',410108,410108),(1665,2,1657,'中牟县',410122,410122),(1666,2,1657,'巩义市',410181,410181),(1667,2,1657,'荥阳市',410182,410182),(1668,2,1657,'新密市',410183,410183),(1669,2,1657,'新郑市',410184,410184),(1670,2,1657,'登封市',410185,410185),(1671,1,1656,'开封市',410200,410200),(1672,2,1671,'市辖区',410201,410201),(1673,2,1671,'龙亭区',410202,410202),(1674,2,1671,'顺河回族区',410203,410203),(1675,2,1671,'鼓楼区',410204,410204),(1676,2,1671,'禹王台区',410205,410205),(1677,2,1671,'金明区',410211,410211),(1678,2,1671,'杞县',410221,410221),(1679,2,1671,'通许县',410222,410222),(1680,2,1671,'尉氏县',410223,410223),(1681,2,1671,'开封县',410224,410224),(1682,2,1671,'兰考县',410225,410225),(1683,1,1656,'洛阳市',410300,410300),(1684,2,1683,'市辖区',410301,410301),(1685,2,1683,'老城区',410302,410302),(1686,2,1683,'西工区',410303,410303),(1687,2,1683,'瀍河回族区',410304,410304),(1688,2,1683,'涧西区',410305,410305),(1689,2,1683,'吉利区',410306,410306),(1690,2,1683,'洛龙区',410311,410311),(1691,2,1683,'孟津县',410322,410322),(1692,2,1683,'新安县',410323,410323),(1693,2,1683,'栾川县',410324,410324),(1694,2,1683,'嵩县',410325,410325),(1695,2,1683,'汝阳县',410326,410326),(1696,2,1683,'宜阳县',410327,410327),(1697,2,1683,'洛宁县',410328,410328),(1698,2,1683,'伊川县',410329,410329),(1699,2,1683,'偃师市',410381,410381),(1700,1,1656,'平顶山市',410400,410400),(1701,2,1700,'市辖区',410401,410401),(1702,2,1700,'新华区',410402,410402),(1703,2,1700,'卫东区',410403,410403),(1704,2,1700,'石龙区',410404,410404),(1705,2,1700,'湛河区',410411,410411),(1706,2,1700,'宝丰县',410421,410421),(1707,2,1700,'叶县',410422,410422),(1708,2,1700,'鲁山县',410423,410423),(1709,2,1700,'郏县',410425,410425),(1710,2,1700,'舞钢市',410481,410481),(1711,2,1700,'汝州市',410482,410482),(1712,1,1656,'安阳市',410500,410500),(1713,2,1712,'市辖区',410501,410501),(1714,2,1712,'文峰区',410502,410502),(1715,2,1712,'北关区',410503,410503),(1716,2,1712,'殷都区',410505,410505),(1717,2,1712,'龙安区',410506,410506),(1718,2,1712,'安阳县',410522,410522),(1719,2,1712,'汤阴县',410523,410523),(1720,2,1712,'滑县',410526,410526),(1721,2,1712,'内黄县',410527,410527),(1722,2,1712,'林州市',410581,410581),(1723,1,1656,'鹤壁市',410600,410600),(1724,2,1723,'市辖区',410601,410601),(1725,2,1723,'鹤山区',410602,410602),(1726,2,1723,'山城区',410603,410603),(1727,2,1723,'淇滨区',410611,410611),(1728,2,1723,'浚县',410621,410621),(1729,2,1723,'淇县',410622,410622),(1730,1,1656,'新乡市',410700,410700),(1731,2,1730,'市辖区',410701,410701),(1732,2,1730,'红旗区',410702,410702),(1733,2,1730,'卫滨区',410703,410703),(1734,2,1730,'凤泉区',410704,410704),(1735,2,1730,'牧野区',410711,410711),(1736,2,1730,'新乡县',410721,410721),(1737,2,1730,'获嘉县',410724,410724),(1738,2,1730,'原阳县',410725,410725),(1739,2,1730,'延津县',410726,410726),(1740,2,1730,'封丘县',410727,410727),(1741,2,1730,'长垣县',410728,410728),(1742,2,1730,'卫辉市',410781,410781),(1743,2,1730,'辉县市',410782,410782),(1744,1,1656,'焦作市',410800,410800),(1745,2,1744,'市辖区',410801,410801),(1746,2,1744,'解放区',410802,410802),(1747,2,1744,'中站区',410803,410803),(1748,2,1744,'马村区',410804,410804),(1749,2,1744,'山阳区',410811,410811),(1750,2,1744,'修武县',410821,410821),(1751,2,1744,'博爱县',410822,410822),(1752,2,1744,'武陟县',410823,410823),(1753,2,1744,'温县',410825,410825),(1754,2,1744,'沁阳市',410882,410882),(1755,2,1744,'孟州市',410883,410883),(1756,1,1656,'濮阳市',410900,410900),(1757,2,1756,'市辖区',410901,410901),(1758,2,1756,'华龙区',410902,410902),(1759,2,1756,'清丰县',410922,410922),(1760,2,1756,'南乐县',410923,410923),(1761,2,1756,'范县',410926,410926),(1762,2,1756,'台前县',410927,410927),(1763,2,1756,'濮阳县',410928,410928),(1764,1,1656,'许昌市',411000,411000),(1765,2,1764,'市辖区',411001,411001),(1766,2,1764,'魏都区',411002,411002),(1767,2,1764,'许昌县',411023,411023),(1768,2,1764,'鄢陵县',411024,411024),(1769,2,1764,'襄城县',411025,411025),(1770,2,1764,'禹州市',411081,411081),(1771,2,1764,'长葛市',411082,411082),(1772,1,1656,'漯河市',411100,411100),(1773,2,1772,'市辖区',411101,411101),(1774,2,1772,'源汇区',411102,411102),(1775,2,1772,'郾城区',411103,411103),(1776,2,1772,'召陵区',411104,411104),(1777,2,1772,'舞阳县',411121,411121),(1778,2,1772,'临颍县',411122,411122),(1779,1,1656,'三门峡市',411200,411200),(1780,2,1779,'市辖区',411201,411201),(1781,2,1779,'湖滨区',411202,411202),(1782,2,1779,'渑池县',411221,411221),(1783,2,1779,'陕县',411222,411222),(1784,2,1779,'卢氏县',411224,411224),(1785,2,1779,'义马市',411281,411281),(1786,2,1779,'灵宝市',411282,411282),(1787,1,1656,'南阳市',411300,411300),(1788,2,1787,'市辖区',411301,411301),(1789,2,1787,'宛城区',411302,411302),(1790,2,1787,'卧龙区',411303,411303),(1791,2,1787,'南召县',411321,411321),(1792,2,1787,'方城县',411322,411322),(1793,2,1787,'西峡县',411323,411323),(1794,2,1787,'镇平县',411324,411324),(1795,2,1787,'内乡县',411325,411325),(1796,2,1787,'淅川县',411326,411326),(1797,2,1787,'社旗县',411327,411327),(1798,2,1787,'唐河县',411328,411328),(1799,2,1787,'新野县',411329,411329),(1800,2,1787,'桐柏县',411330,411330),(1801,2,1787,'邓州市',411381,411381),(1802,1,1656,'商丘市',411400,411400),(1803,2,1802,'市辖区',411401,411401),(1804,2,1802,'梁园区',411402,411402),(1805,2,1802,'睢阳区',411403,411403),(1806,2,1802,'民权县',411421,411421),(1807,2,1802,'睢县',411422,411422),(1808,2,1802,'宁陵县',411423,411423),(1809,2,1802,'柘城县',411424,411424),(1810,2,1802,'虞城县',411425,411425),(1811,2,1802,'夏邑县',411426,411426),(1812,2,1802,'永城市',411481,411481),(1813,1,1656,'信阳市',411500,411500),(1814,2,1813,'市辖区',411501,411501),(1815,2,1813,'浉河区',411502,411502),(1816,2,1813,'平桥区',411503,411503),(1817,2,1813,'罗山县',411521,411521),(1818,2,1813,'光山县',411522,411522),(1819,2,1813,'新县',411523,411523),(1820,2,1813,'商城县',411524,411524),(1821,2,1813,'固始县',411525,411525),(1822,2,1813,'潢川县',411526,411526),(1823,2,1813,'淮滨县',411527,411527),(1824,2,1813,'息县',411528,411528),(1825,1,1656,'周口市',411600,411600),(1826,2,1825,'市辖区',411601,411601),(1827,2,1825,'川汇区',411602,411602),(1828,2,1825,'扶沟县',411621,411621),(1829,2,1825,'西华县',411622,411622),(1830,2,1825,'商水县',411623,411623),(1831,2,1825,'沈丘县',411624,411624),(1832,2,1825,'郸城县',411625,411625),(1833,2,1825,'淮阳县',411626,411626),(1834,2,1825,'太康县',411627,411627),(1835,2,1825,'鹿邑县',411628,411628),(1836,2,1825,'项城市',411681,411681),(1837,1,1656,'驻马店市',411700,411700),(1838,2,1837,'市辖区',411701,411701),(1839,2,1837,'驿城区',411702,411702),(1840,2,1837,'西平县',411721,411721),(1841,2,1837,'上蔡县',411722,411722),(1842,2,1837,'平舆县',411723,411723),(1843,2,1837,'正阳县',411724,411724),(1844,2,1837,'确山县',411725,411725),(1845,2,1837,'泌阳县',411726,411726),(1846,2,1837,'汝南县',411727,411727),(1847,2,1837,'遂平县',411728,411728),(1848,2,1837,'新蔡县',411729,411729),(1849,1,1656,'省直辖县级行政区划',419000,419000),(1850,2,1849,'济源市',419001,419001),(1851,0,NULL,'湖北省',420000,420000),(1852,1,1851,'武汉市',420100,420100),(1853,2,1852,'市辖区',420101,420101),(1854,2,1852,'江岸区',420102,420102),(1855,2,1852,'江汉区',420103,420103),(1856,2,1852,'硚口区',420104,420104),(1857,2,1852,'汉阳区',420105,420105),(1858,2,1852,'武昌区',420106,420106),(1859,2,1852,'青山区',420107,420107),(1860,2,1852,'洪山区',420111,420111),(1861,2,1852,'东西湖区',420112,420112),(1862,2,1852,'汉南区',420113,420113),(1863,2,1852,'蔡甸区',420114,420114),(1864,2,1852,'江夏区',420115,420115),(1865,2,1852,'黄陂区',420116,420116),(1866,2,1852,'新洲区',420117,420117),(1867,1,1851,'黄石市',420200,420200),(1868,2,1867,'市辖区',420201,420201),(1869,2,1867,'黄石港区',420202,420202),(1870,2,1867,'西塞山区',420203,420203),(1871,2,1867,'下陆区',420204,420204),(1872,2,1867,'铁山区',420205,420205),(1873,2,1867,'阳新县',420222,420222),(1874,2,1867,'大冶市',420281,420281),(1875,1,1851,'十堰市',420300,420300),(1876,2,1875,'市辖区',420301,420301),(1877,2,1875,'茅箭区',420302,420302),(1878,2,1875,'张湾区',420303,420303),(1879,2,1875,'郧阳区',420304,420304),(1880,2,1875,'郧西县',420322,420322),(1881,2,1875,'竹山县',420323,420323),(1882,2,1875,'竹溪县',420324,420324),(1883,2,1875,'房县',420325,420325),(1884,2,1875,'丹江口市',420381,420381),(1885,1,1851,'宜昌市',420500,420500),(1886,2,1885,'市辖区',420501,420501),(1887,2,1885,'西陵区',420502,420502),(1888,2,1885,'伍家岗区',420503,420503),(1889,2,1885,'点军区',420504,420504),(1890,2,1885,'猇亭区',420505,420505),(1891,2,1885,'夷陵区',420506,420506),(1892,2,1885,'远安县',420525,420525),(1893,2,1885,'兴山县',420526,420526),(1894,2,1885,'秭归县',420527,420527),(1895,2,1885,'长阳土家族自治县',420528,420528),(1896,2,1885,'五峰土家族自治县',420529,420529),(1897,2,1885,'宜都市',420581,420581),(1898,2,1885,'当阳市',420582,420582),(1899,2,1885,'枝江市',420583,420583),(1900,1,1851,'襄阳市',420600,420600),(1901,2,1900,'市辖区',420601,420601),(1902,2,1900,'襄城区',420602,420602),(1903,2,1900,'樊城区',420606,420606),(1904,2,1900,'襄州区',420607,420607),(1905,2,1900,'南漳县',420624,420624),(1906,2,1900,'谷城县',420625,420625),(1907,2,1900,'保康县',420626,420626),(1908,2,1900,'老河口市',420682,420682),(1909,2,1900,'枣阳市',420683,420683),(1910,2,1900,'宜城市',420684,420684),(1911,1,1851,'鄂州市',420700,420700),(1912,2,1911,'市辖区',420701,420701),(1913,2,1911,'梁子湖区',420702,420702),(1914,2,1911,'华容区',420703,420703),(1915,2,1911,'鄂城区',420704,420704),(1916,1,1851,'荆门市',420800,420800),(1917,2,1916,'市辖区',420801,420801),(1918,2,1916,'东宝区',420802,420802),(1919,2,1916,'掇刀区',420804,420804),(1920,2,1916,'京山县',420821,420821),(1921,2,1916,'沙洋县',420822,420822),(1922,2,1916,'钟祥市',420881,420881),(1923,1,1851,'孝感市',420900,420900),(1924,2,1923,'市辖区',420901,420901),(1925,2,1923,'孝南区',420902,420902),(1926,2,1923,'孝昌县',420921,420921),(1927,2,1923,'大悟县',420922,420922),(1928,2,1923,'云梦县',420923,420923),(1929,2,1923,'应城市',420981,420981),(1930,2,1923,'安陆市',420982,420982),(1931,2,1923,'汉川市',420984,420984),(1932,1,1851,'荆州市',421000,421000),(1933,2,1932,'市辖区',421001,421001),(1934,2,1932,'沙市区',421002,421002),(1935,2,1932,'荆州区',421003,421003),(1936,2,1932,'公安县',421022,421022),(1937,2,1932,'监利县',421023,421023),(1938,2,1932,'江陵县',421024,421024),(1939,2,1932,'石首市',421081,421081),(1940,2,1932,'洪湖市',421083,421083),(1941,2,1932,'松滋市',421087,421087),(1942,1,1851,'黄冈市',421100,421100),(1943,2,1942,'市辖区',421101,421101),(1944,2,1942,'黄州区',421102,421102),(1945,2,1942,'团风县',421121,421121),(1946,2,1942,'红安县',421122,421122),(1947,2,1942,'罗田县',421123,421123),(1948,2,1942,'英山县',421124,421124),(1949,2,1942,'浠水县',421125,421125),(1950,2,1942,'蕲春县',421126,421126),(1951,2,1942,'黄梅县',421127,421127),(1952,2,1942,'麻城市',421181,421181),(1953,2,1942,'武穴市',421182,421182),(1954,1,1851,'咸宁市',421200,421200),(1955,2,1954,'市辖区',421201,421201),(1956,2,1954,'咸安区',421202,421202),(1957,2,1954,'嘉鱼县',421221,421221),(1958,2,1954,'通城县',421222,421222),(1959,2,1954,'崇阳县',421223,421223),(1960,2,1954,'通山县',421224,421224),(1961,2,1954,'赤壁市',421281,421281),(1962,1,1851,'随州市',421300,421300),(1963,2,1962,'市辖区',421301,421301),(1964,2,1962,'曾都区',421303,421303),(1965,2,1962,'随县',421321,421321),(1966,2,1962,'广水市',421381,421381),(1967,1,1851,'恩施土家族苗族自治州',422800,422800),(1968,2,1967,'恩施市',422801,422801),(1969,2,1967,'利川市',422802,422802),(1970,2,1967,'建始县',422822,422822),(1971,2,1967,'巴东县',422823,422823),(1972,2,1967,'宣恩县',422825,422825),(1973,2,1967,'咸丰县',422826,422826),(1974,2,1967,'来凤县',422827,422827),(1975,2,1967,'鹤峰县',422828,422828),(1976,1,1851,'省直辖县级行政区划',429000,429000),(1977,2,1976,'仙桃市',429004,429004),(1978,2,1976,'潜江市',429005,429005),(1979,2,1976,'天门市',429006,429006),(1980,2,1976,'神农架林区',429021,429021),(1981,0,NULL,'湖南省',430000,430000),(1982,1,1981,'长沙市',430100,430100),(1983,2,1982,'市辖区',430101,430101),(1984,2,1982,'芙蓉区',430102,430102),(1985,2,1982,'天心区',430103,430103),(1986,2,1982,'岳麓区',430104,430104),(1987,2,1982,'开福区',430105,430105),(1988,2,1982,'雨花区',430111,430111),(1989,2,1982,'望城区',430112,430112),(1990,2,1982,'长沙县',430121,430121),(1991,2,1982,'宁乡县',430124,430124),(1992,2,1982,'浏阳市',430181,430181),(1993,1,1981,'株洲市',430200,430200),(1994,2,1993,'市辖区',430201,430201),(1995,2,1993,'荷塘区',430202,430202),(1996,2,1993,'芦淞区',430203,430203),(1997,2,1993,'石峰区',430204,430204),(1998,2,1993,'天元区',430211,430211),(1999,2,1993,'株洲县',430221,430221),(2000,2,1993,'攸县',430223,430223),(2001,2,1993,'茶陵县',430224,430224),(2002,2,1993,'炎陵县',430225,430225),(2003,2,1993,'醴陵市',430281,430281),(2004,1,1981,'湘潭市',430300,430300),(2005,2,2004,'市辖区',430301,430301),(2006,2,2004,'雨湖区',430302,430302),(2007,2,2004,'岳塘区',430304,430304),(2008,2,2004,'湘潭县',430321,430321),(2009,2,2004,'湘乡市',430381,430381),(2010,2,2004,'韶山市',430382,430382),(2011,1,1981,'衡阳市',430400,430400),(2012,2,2011,'市辖区',430401,430401),(2013,2,2011,'珠晖区',430405,430405),(2014,2,2011,'雁峰区',430406,430406),(2015,2,2011,'石鼓区',430407,430407),(2016,2,2011,'蒸湘区',430408,430408),(2017,2,2011,'南岳区',430412,430412),(2018,2,2011,'衡阳县',430421,430421),(2019,2,2011,'衡南县',430422,430422),(2020,2,2011,'衡山县',430423,430423),(2021,2,2011,'衡东县',430424,430424),(2022,2,2011,'祁东县',430426,430426),(2023,2,2011,'耒阳市',430481,430481),(2024,2,2011,'常宁市',430482,430482),(2025,1,1981,'邵阳市',430500,430500),(2026,2,2025,'市辖区',430501,430501),(2027,2,2025,'双清区',430502,430502),(2028,2,2025,'大祥区',430503,430503),(2029,2,2025,'北塔区',430511,430511),(2030,2,2025,'邵东县',430521,430521),(2031,2,2025,'新邵县',430522,430522),(2032,2,2025,'邵阳县',430523,430523),(2033,2,2025,'隆回县',430524,430524),(2034,2,2025,'洞口县',430525,430525),(2035,2,2025,'绥宁县',430527,430527),(2036,2,2025,'新宁县',430528,430528),(2037,2,2025,'城步苗族自治县',430529,430529),(2038,2,2025,'武冈市',430581,430581),(2039,1,1981,'岳阳市',430600,430600),(2040,2,2039,'市辖区',430601,430601),(2041,2,2039,'岳阳楼区',430602,430602),(2042,2,2039,'云溪区',430603,430603),(2043,2,2039,'君山区',430611,430611),(2044,2,2039,'岳阳县',430621,430621),(2045,2,2039,'华容县',430623,430623),(2046,2,2039,'湘阴县',430624,430624),(2047,2,2039,'平江县',430626,430626),(2048,2,2039,'汨罗市',430681,430681),(2049,2,2039,'临湘市',430682,430682),(2050,1,1981,'常德市',430700,430700),(2051,2,2050,'市辖区',430701,430701),(2052,2,2050,'武陵区',430702,430702),(2053,2,2050,'鼎城区',430703,430703),(2054,2,2050,'安乡县',430721,430721),(2055,2,2050,'汉寿县',430722,430722),(2056,2,2050,'澧县',430723,430723),(2057,2,2050,'临澧县',430724,430724),(2058,2,2050,'桃源县',430725,430725),(2059,2,2050,'石门县',430726,430726),(2060,2,2050,'津市市',430781,430781),(2061,1,1981,'张家界市',430800,430800),(2062,2,2061,'市辖区',430801,430801),(2063,2,2061,'永定区',430802,430802),(2064,2,2061,'武陵源区',430811,430811),(2065,2,2061,'慈利县',430821,430821),(2066,2,2061,'桑植县',430822,430822),(2067,1,1981,'益阳市',430900,430900),(2068,2,2067,'市辖区',430901,430901),(2069,2,2067,'资阳区',430902,430902),(2070,2,2067,'赫山区',430903,430903),(2071,2,2067,'南县',430921,430921),(2072,2,2067,'桃江县',430922,430922),(2073,2,2067,'安化县',430923,430923),(2074,2,2067,'沅江市',430981,430981),(2075,1,1981,'郴州市',431000,431000),(2076,2,2075,'市辖区',431001,431001),(2077,2,2075,'北湖区',431002,431002),(2078,2,2075,'苏仙区',431003,431003),(2079,2,2075,'桂阳县',431021,431021),(2080,2,2075,'宜章县',431022,431022),(2081,2,2075,'永兴县',431023,431023),(2082,2,2075,'嘉禾县',431024,431024),(2083,2,2075,'临武县',431025,431025),(2084,2,2075,'汝城县',431026,431026),(2085,2,2075,'桂东县',431027,431027),(2086,2,2075,'安仁县',431028,431028),(2087,2,2075,'资兴市',431081,431081),(2088,1,1981,'永州市',431100,431100),(2089,2,2088,'市辖区',431101,431101),(2090,2,2088,'零陵区',431102,431102),(2091,2,2088,'冷水滩区',431103,431103),(2092,2,2088,'祁阳县',431121,431121),(2093,2,2088,'东安县',431122,431122),(2094,2,2088,'双牌县',431123,431123),(2095,2,2088,'道县',431124,431124),(2096,2,2088,'江永县',431125,431125),(2097,2,2088,'宁远县',431126,431126),(2098,2,2088,'蓝山县',431127,431127),(2099,2,2088,'新田县',431128,431128),(2100,2,2088,'江华瑶族自治县',431129,431129),(2101,1,1981,'怀化市',431200,431200),(2102,2,2101,'市辖区',431201,431201),(2103,2,2101,'鹤城区',431202,431202),(2104,2,2101,'中方县',431221,431221),(2105,2,2101,'沅陵县',431222,431222),(2106,2,2101,'辰溪县',431223,431223),(2107,2,2101,'溆浦县',431224,431224),(2108,2,2101,'会同县',431225,431225),(2109,2,2101,'麻阳苗族自治县',431226,431226),(2110,2,2101,'新晃侗族自治县',431227,431227),(2111,2,2101,'芷江侗族自治县',431228,431228),(2112,2,2101,'靖州苗族侗族自治县',431229,431229),(2113,2,2101,'通道侗族自治县',431230,431230),(2114,2,2101,'洪江市',431281,431281),(2115,1,1981,'娄底市',431300,431300),(2116,2,2115,'市辖区',431301,431301),(2117,2,2115,'娄星区',431302,431302),(2118,2,2115,'双峰县',431321,431321),(2119,2,2115,'新化县',431322,431322),(2120,2,2115,'冷水江市',431381,431381),(2121,2,2115,'涟源市',431382,431382),(2122,1,1981,'湘西土家族苗族自治州',433100,433100),(2123,2,2122,'吉首市',433101,433101),(2124,2,2122,'泸溪县',433122,433122),(2125,2,2122,'凤凰县',433123,433123),(2126,2,2122,'花垣县',433124,433124),(2127,2,2122,'保靖县',433125,433125),(2128,2,2122,'古丈县',433126,433126),(2129,2,2122,'永顺县',433127,433127),(2130,2,2122,'龙山县',433130,433130),(2131,0,NULL,'广东省',440000,440000),(2132,1,2131,'广州市',440100,440100),(2133,2,2132,'市辖区',440101,440101),(2134,2,2132,'荔湾区',440103,440103),(2135,2,2132,'越秀区',440104,440104),(2136,2,2132,'海珠区',440105,440105),(2137,2,2132,'天河区',440106,440106),(2138,2,2132,'白云区',440111,440111),(2139,2,2132,'黄埔区',440112,440112),(2140,2,2132,'番禺区',440113,440113),(2141,2,2132,'花都区',440114,440114),(2142,2,2132,'南沙区',440115,440115),(2143,2,2132,'萝岗区',440116,440116),(2144,2,2132,'从化区',440117,440117),(2145,2,2132,'增城区',440118,440118),(2146,1,2131,'韶关市',440200,440200),(2147,2,2146,'市辖区',440201,440201),(2148,2,2146,'武江区',440203,440203),(2149,2,2146,'浈江区',440204,440204),(2150,2,2146,'曲江区',440205,440205),(2151,2,2146,'始兴县',440222,440222),(2152,2,2146,'仁化县',440224,440224),(2153,2,2146,'翁源县',440229,440229),(2154,2,2146,'乳源瑶族自治县',440232,440232),(2155,2,2146,'新丰县',440233,440233),(2156,2,2146,'乐昌市',440281,440281),(2157,2,2146,'南雄市',440282,440282),(2158,1,2131,'深圳市',440300,440300),(2159,2,2158,'市辖区',440301,440301),(2160,2,2158,'罗湖区',440303,440303),(2161,2,2158,'福田区',440304,440304),(2162,2,2158,'南山区',440305,440305),(2163,2,2158,'宝安区',440306,440306),(2164,2,2158,'龙岗区',440307,440307),(2165,2,2158,'盐田区',440308,440308),(2166,1,2131,'珠海市',440400,440400),(2167,2,2166,'市辖区',440401,440401),(2168,2,2166,'香洲区',440402,440402),(2169,2,2166,'斗门区',440403,440403),(2170,2,2166,'金湾区',440404,440404),(2171,1,2131,'汕头市',440500,440500),(2172,2,2171,'市辖区',440501,440501),(2173,2,2171,'龙湖区',440507,440507),(2174,2,2171,'金平区',440511,440511),(2175,2,2171,'濠江区',440512,440512),(2176,2,2171,'潮阳区',440513,440513),(2177,2,2171,'潮南区',440514,440514),(2178,2,2171,'澄海区',440515,440515),(2179,2,2171,'南澳县',440523,440523),(2180,1,2131,'佛山市',440600,440600),(2181,2,2180,'市辖区',440601,440601),(2182,2,2180,'禅城区',440604,440604),(2183,2,2180,'南海区',440605,440605),(2184,2,2180,'顺德区',440606,440606),(2185,2,2180,'三水区',440607,440607),(2186,2,2180,'高明区',440608,440608),(2187,1,2131,'江门市',440700,440700),(2188,2,2187,'市辖区',440701,440701),(2189,2,2187,'蓬江区',440703,440703),(2190,2,2187,'江海区',440704,440704),(2191,2,2187,'新会区',440705,440705),(2192,2,2187,'台山市',440781,440781),(2193,2,2187,'开平市',440783,440783),(2194,2,2187,'鹤山市',440784,440784),(2195,2,2187,'恩平市',440785,440785),(2196,1,2131,'湛江市',440800,440800),(2197,2,2196,'市辖区',440801,440801),(2198,2,2196,'赤坎区',440802,440802),(2199,2,2196,'霞山区',440803,440803),(2200,2,2196,'坡头区',440804,440804),(2201,2,2196,'麻章区',440811,440811),(2202,2,2196,'遂溪县',440823,440823),(2203,2,2196,'徐闻县',440825,440825),(2204,2,2196,'廉江市',440881,440881),(2205,2,2196,'雷州市',440882,440882),(2206,2,2196,'吴川市',440883,440883),(2207,1,2131,'茂名市',440900,440900),(2208,2,2207,'市辖区',440901,440901),(2209,2,2207,'茂南区',440902,440902),(2210,2,2207,'电白区',440904,440904),(2211,2,2207,'高州市',440981,440981),(2212,2,2207,'化州市',440982,440982),(2213,2,2207,'信宜市',440983,440983),(2214,1,2131,'肇庆市',441200,441200),(2215,2,2214,'市辖区',441201,441201),(2216,2,2214,'端州区',441202,441202),(2217,2,2214,'鼎湖区',441203,441203),(2218,2,2214,'广宁县',441223,441223),(2219,2,2214,'怀集县',441224,441224),(2220,2,2214,'封开县',441225,441225),(2221,2,2214,'德庆县',441226,441226),(2222,2,2214,'高要市',441283,441283),(2223,2,2214,'四会市',441284,441284),(2224,1,2131,'惠州市',441300,441300),(2225,2,2224,'市辖区',441301,441301),(2226,2,2224,'惠城区',441302,441302),(2227,2,2224,'惠阳区',441303,441303),(2228,2,2224,'博罗县',441322,441322),(2229,2,2224,'惠东县',441323,441323),(2230,2,2224,'龙门县',441324,441324),(2231,1,2131,'梅州市',441400,441400),(2232,2,2231,'市辖区',441401,441401),(2233,2,2231,'梅江区',441402,441402),(2234,2,2231,'梅县区',441403,441403),(2235,2,2231,'大埔县',441422,441422),(2236,2,2231,'丰顺县',441423,441423),(2237,2,2231,'五华县',441424,441424),(2238,2,2231,'平远县',441426,441426),(2239,2,2231,'蕉岭县',441427,441427),(2240,2,2231,'兴宁市',441481,441481),(2241,1,2131,'汕尾市',441500,441500),(2242,2,2241,'市辖区',441501,441501),(2243,2,2241,'城区',441502,441502),(2244,2,2241,'海丰县',441521,441521),(2245,2,2241,'陆河县',441523,441523),(2246,2,2241,'陆丰市',441581,441581),(2247,1,2131,'河源市',441600,441600),(2248,2,2247,'市辖区',441601,441601),(2249,2,2247,'源城区',441602,441602),(2250,2,2247,'紫金县',441621,441621),(2251,2,2247,'龙川县',441622,441622),(2252,2,2247,'连平县',441623,441623),(2253,2,2247,'和平县',441624,441624),(2254,2,2247,'东源县',441625,441625),(2255,1,2131,'阳江市',441700,441700),(2256,2,2255,'市辖区',441701,441701),(2257,2,2255,'江城区',441702,441702),(2258,2,2255,'阳西县',441721,441721),(2259,2,2255,'阳东县',441723,441723),(2260,2,2255,'阳春市',441781,441781),(2261,1,2131,'清远市',441800,441800),(2262,2,2261,'市辖区',441801,441801),(2263,2,2261,'清城区',441802,441802),(2264,2,2261,'清新区',441803,441803),(2265,2,2261,'佛冈县',441821,441821),(2266,2,2261,'阳山县',441823,441823),(2267,2,2261,'连山壮族瑶族自治县',441825,441825),(2268,2,2261,'连南瑶族自治县',441826,441826),(2269,2,2261,'英德市',441881,441881),(2270,2,2261,'连州市',441882,441882),(2271,1,2131,'东莞市',441900,441900),(2272,1,2131,'中山市',442000,442000),(2273,1,2131,'潮州市',445100,445100),(2274,2,2273,'市辖区',445101,445101),(2275,2,2273,'湘桥区',445102,445102),(2276,2,2273,'潮安区',445103,445103),(2277,2,2273,'饶平县',445122,445122),(2278,1,2131,'揭阳市',445200,445200),(2279,2,2278,'市辖区',445201,445201),(2280,2,2278,'榕城区',445202,445202),(2281,2,2278,'揭东区',445203,445203),(2282,2,2278,'揭西县',445222,445222),(2283,2,2278,'惠来县',445224,445224),(2284,2,2278,'普宁市',445281,445281),(2285,1,2131,'云浮市',445300,445300),(2286,2,2285,'市辖区',445301,445301),(2287,2,2285,'云城区',445302,445302),(2288,2,2285,'云安区',445303,445303),(2289,2,2285,'新兴县',445321,445321),(2290,2,2285,'郁南县',445322,445322),(2291,2,2285,'罗定市',445381,445381),(2292,0,NULL,'广西壮族自治区',450000,450000),(2293,1,2292,'南宁市',450100,450100),(2294,2,2293,'市辖区',450101,450101),(2295,2,2293,'兴宁区',450102,450102),(2296,2,2293,'青秀区',450103,450103),(2297,2,2293,'江南区',450105,450105),(2298,2,2293,'西乡塘区',450107,450107),(2299,2,2293,'良庆区',450108,450108),(2300,2,2293,'邕宁区',450109,450109),(2301,2,2293,'武鸣县',450122,450122),(2302,2,2293,'隆安县',450123,450123),(2303,2,2293,'马山县',450124,450124),(2304,2,2293,'上林县',450125,450125),(2305,2,2293,'宾阳县',450126,450126),(2306,2,2293,'横县',450127,450127),(2307,1,2292,'柳州市',450200,450200),(2308,2,2307,'市辖区',450201,450201),(2309,2,2307,'城中区',450202,450202),(2310,2,2307,'鱼峰区',450203,450203),(2311,2,2307,'柳南区',450204,450204),(2312,2,2307,'柳北区',450205,450205),(2313,2,2307,'柳江县',450221,450221),(2314,2,2307,'柳城县',450222,450222),(2315,2,2307,'鹿寨县',450223,450223),(2316,2,2307,'融安县',450224,450224),(2317,2,2307,'融水苗族自治县',450225,450225),(2318,2,2307,'三江侗族自治县',450226,450226),(2319,1,2292,'桂林市',450300,450300),(2320,2,2319,'市辖区',450301,450301),(2321,2,2319,'秀峰区',450302,450302),(2322,2,2319,'叠彩区',450303,450303),(2323,2,2319,'象山区',450304,450304),(2324,2,2319,'七星区',450305,450305),(2325,2,2319,'雁山区',450311,450311),(2326,2,2319,'临桂区',450312,450312),(2327,2,2319,'阳朔县',450321,450321),(2328,2,2319,'灵川县',450323,450323),(2329,2,2319,'全州县',450324,450324),(2330,2,2319,'兴安县',450325,450325),(2331,2,2319,'永福县',450326,450326),(2332,2,2319,'灌阳县',450327,450327),(2333,2,2319,'龙胜各族自治县',450328,450328),(2334,2,2319,'资源县',450329,450329),(2335,2,2319,'平乐县',450330,450330),(2336,2,2319,'荔浦县',450331,450331),(2337,2,2319,'恭城瑶族自治县',450332,450332),(2338,1,2292,'梧州市',450400,450400),(2339,2,2338,'市辖区',450401,450401),(2340,2,2338,'万秀区',450403,450403),(2341,2,2338,'长洲区',450405,450405),(2342,2,2338,'龙圩区',450406,450406),(2343,2,2338,'苍梧县',450421,450421),(2344,2,2338,'藤县',450422,450422),(2345,2,2338,'蒙山县',450423,450423),(2346,2,2338,'岑溪市',450481,450481),(2347,1,2292,'北海市',450500,450500),(2348,2,2347,'市辖区',450501,450501),(2349,2,2347,'海城区',450502,450502),(2350,2,2347,'银海区',450503,450503),(2351,2,2347,'铁山港区',450512,450512),(2352,2,2347,'合浦县',450521,450521),(2353,1,2292,'防城港市',450600,450600),(2354,2,2353,'市辖区',450601,450601),(2355,2,2353,'港口区',450602,450602),(2356,2,2353,'防城区',450603,450603),(2357,2,2353,'上思县',450621,450621),(2358,2,2353,'东兴市',450681,450681),(2359,1,2292,'钦州市',450700,450700),(2360,2,2359,'市辖区',450701,450701),(2361,2,2359,'钦南区',450702,450702),(2362,2,2359,'钦北区',450703,450703),(2363,2,2359,'灵山县',450721,450721),(2364,2,2359,'浦北县',450722,450722),(2365,1,2292,'贵港市',450800,450800),(2366,2,2365,'市辖区',450801,450801),(2367,2,2365,'港北区',450802,450802),(2368,2,2365,'港南区',450803,450803),(2369,2,2365,'覃塘区',450804,450804),(2370,2,2365,'平南县',450821,450821),(2371,2,2365,'桂平市',450881,450881),(2372,1,2292,'玉林市',450900,450900),(2373,2,2372,'市辖区',450901,450901),(2374,2,2372,'玉州区',450902,450902),(2375,2,2372,'福绵区',450903,450903),(2376,2,2372,'容县',450921,450921),(2377,2,2372,'陆川县',450922,450922),(2378,2,2372,'博白县',450923,450923),(2379,2,2372,'兴业县',450924,450924),(2380,2,2372,'北流市',450981,450981),(2381,1,2292,'百色市',451000,451000),(2382,2,2381,'市辖区',451001,451001),(2383,2,2381,'右江区',451002,451002),(2384,2,2381,'田阳县',451021,451021),(2385,2,2381,'田东县',451022,451022),(2386,2,2381,'平果县',451023,451023),(2387,2,2381,'德保县',451024,451024),(2388,2,2381,'靖西县',451025,451025),(2389,2,2381,'那坡县',451026,451026),(2390,2,2381,'凌云县',451027,451027),(2391,2,2381,'乐业县',451028,451028),(2392,2,2381,'田林县',451029,451029),(2393,2,2381,'西林县',451030,451030),(2394,2,2381,'隆林各族自治县',451031,451031),(2395,1,2292,'贺州市',451100,451100),(2396,2,2395,'市辖区',451101,451101),(2397,2,2395,'八步区',451102,451102),(2398,2,2395,'昭平县',451121,451121),(2399,2,2395,'钟山县',451122,451122),(2400,2,2395,'富川瑶族自治县',451123,451123),(2401,1,2292,'河池市',451200,451200),(2402,2,2401,'市辖区',451201,451201),(2403,2,2401,'金城江区',451202,451202),(2404,2,2401,'南丹县',451221,451221),(2405,2,2401,'天峨县',451222,451222),(2406,2,2401,'凤山县',451223,451223),(2407,2,2401,'东兰县',451224,451224),(2408,2,2401,'罗城仫佬族自治县',451225,451225),(2409,2,2401,'环江毛南族自治县',451226,451226),(2410,2,2401,'巴马瑶族自治县',451227,451227),(2411,2,2401,'都安瑶族自治县',451228,451228),(2412,2,2401,'大化瑶族自治县',451229,451229),(2413,2,2401,'宜州市',451281,451281),(2414,1,2292,'来宾市',451300,451300),(2415,2,2414,'市辖区',451301,451301),(2416,2,2414,'兴宾区',451302,451302),(2417,2,2414,'忻城县',451321,451321),(2418,2,2414,'象州县',451322,451322),(2419,2,2414,'武宣县',451323,451323),(2420,2,2414,'金秀瑶族自治县',451324,451324),(2421,2,2414,'合山市',451381,451381),(2422,1,2292,'崇左市',451400,451400),(2423,2,2422,'市辖区',451401,451401),(2424,2,2422,'江州区',451402,451402),(2425,2,2422,'扶绥县',451421,451421),(2426,2,2422,'宁明县',451422,451422),(2427,2,2422,'龙州县',451423,451423),(2428,2,2422,'大新县',451424,451424),(2429,2,2422,'天等县',451425,451425),(2430,2,2422,'凭祥市',451481,451481),(2431,0,NULL,'海南省',460000,460000),(2432,1,2431,'海口市',460100,460100),(2433,2,2432,'市辖区',460101,460101),(2434,2,2432,'秀英区',460105,460105),(2435,2,2432,'龙华区',460106,460106),(2436,2,2432,'琼山区',460107,460107),(2437,2,2432,'美兰区',460108,460108),(2438,1,2431,'三亚市',460200,460200),(2439,2,2438,'市辖区',460201,460201),(2440,2,2438,'海棠区',460202,460202),(2441,2,2438,'吉阳区',460203,460203),(2442,2,2438,'天涯区',460204,460204),(2443,2,2438,'崖州区',460205,460205),(2444,1,2431,'三沙市',460300,460300),(2445,1,2431,'省直辖县级行政区划',469000,469000),(2446,2,2445,'五指山市',469001,469001),(2447,2,2445,'琼海市',469002,469002),(2448,2,2445,'儋州市',469003,469003),(2449,2,2445,'文昌市',469005,469005),(2450,2,2445,'万宁市',469006,469006),(2451,2,2445,'东方市',469007,469007),(2452,2,2445,'定安县',469021,469021),(2453,2,2445,'屯昌县',469022,469022),(2454,2,2445,'澄迈县',469023,469023),(2455,2,2445,'临高县',469024,469024),(2456,2,2445,'白沙黎族自治县',469025,469025),(2457,2,2445,'昌江黎族自治县',469026,469026),(2458,2,2445,'乐东黎族自治县',469027,469027),(2459,2,2445,'陵水黎族自治县',469028,469028),(2460,2,2445,'保亭黎族苗族自治县',469029,469029),(2461,2,2445,'琼中黎族苗族自治县',469030,469030),(2462,0,NULL,'重庆市',500000,500000),(2463,1,2462,'市辖区',500100,500100),(2464,2,2463,'万州区',500101,500101),(2465,2,2463,'涪陵区',500102,500102),(2466,2,2463,'渝中区',500103,500103),(2467,2,2463,'大渡口区',500104,500104),(2468,2,2463,'江北区',500105,500105),(2469,2,2463,'沙坪坝区',500106,500106),(2470,2,2463,'九龙坡区',500107,500107),(2471,2,2463,'南岸区',500108,500108),(2472,2,2463,'北碚区',500109,500109),(2473,2,2463,'綦江区',500110,500110),(2474,2,2463,'大足区',500111,500111),(2475,2,2463,'渝北区',500112,500112),(2476,2,2463,'巴南区',500113,500113),(2477,2,2463,'黔江区',500114,500114),(2478,2,2463,'长寿区',500115,500115),(2479,2,2463,'江津区',500116,500116),(2480,2,2463,'合川区',500117,500117),(2481,2,2463,'永川区',500118,500118),(2482,2,2463,'南川区',500119,500119),(2483,2,2463,'璧山区',500120,500120),(2484,2,2463,'铜梁区',500151,500151),(2485,1,2462,'县',500200,500200),(2486,2,2485,'潼南县',500223,500223),(2487,2,2485,'荣昌县',500226,500226),(2488,2,2485,'梁平县',500228,500228),(2489,2,2485,'城口县',500229,500229),(2490,2,2485,'丰都县',500230,500230),(2491,2,2485,'垫江县',500231,500231),(2492,2,2485,'武隆县',500232,500232),(2493,2,2485,'忠县',500233,500233),(2494,2,2485,'开县',500234,500234),(2495,2,2485,'云阳县',500235,500235),(2496,2,2485,'奉节县',500236,500236),(2497,2,2485,'巫山县',500237,500237),(2498,2,2485,'巫溪县',500238,500238),(2499,2,2485,'石柱土家族自治县',500240,500240),(2500,2,2485,'秀山土家族苗族自治县',500241,500241),(2501,2,2485,'酉阳土家族苗族自治县',500242,500242),(2502,2,2485,'彭水苗族土家族自治县',500243,500243),(2503,0,NULL,'四川省',510000,510000),(2504,1,2503,'成都市',510100,510100),(2505,2,2504,'市辖区',510101,510101),(2506,2,2504,'锦江区',510104,510104),(2507,2,2504,'青羊区',510105,510105),(2508,2,2504,'金牛区',510106,510106),(2509,2,2504,'武侯区',510107,510107),(2510,2,2504,'成华区',510108,510108),(2511,2,2504,'龙泉驿区',510112,510112),(2512,2,2504,'青白江区',510113,510113),(2513,2,2504,'新都区',510114,510114),(2514,2,2504,'温江区',510115,510115),(2515,2,2504,'金堂县',510121,510121),(2516,2,2504,'双流县',510122,510122),(2517,2,2504,'郫县',510124,510124),(2518,2,2504,'大邑县',510129,510129),(2519,2,2504,'蒲江县',510131,510131),(2520,2,2504,'新津县',510132,510132),(2521,2,2504,'都江堰市',510181,510181),(2522,2,2504,'彭州市',510182,510182),(2523,2,2504,'邛崃市',510183,510183),(2524,2,2504,'崇州市',510184,510184),(2525,1,2503,'自贡市',510300,510300),(2526,2,2525,'市辖区',510301,510301),(2527,2,2525,'自流井区',510302,510302),(2528,2,2525,'贡井区',510303,510303),(2529,2,2525,'大安区',510304,510304),(2530,2,2525,'沿滩区',510311,510311),(2531,2,2525,'荣县',510321,510321),(2532,2,2525,'富顺县',510322,510322),(2533,1,2503,'攀枝花市',510400,510400),(2534,2,2533,'市辖区',510401,510401),(2535,2,2533,'东区',510402,510402),(2536,2,2533,'西区',510403,510403),(2537,2,2533,'仁和区',510411,510411),(2538,2,2533,'米易县',510421,510421),(2539,2,2533,'盐边县',510422,510422),(2540,1,2503,'泸州市',510500,510500),(2541,2,2540,'市辖区',510501,510501),(2542,2,2540,'江阳区',510502,510502),(2543,2,2540,'纳溪区',510503,510503),(2544,2,2540,'龙马潭区',510504,510504),(2545,2,2540,'泸县',510521,510521),(2546,2,2540,'合江县',510522,510522),(2547,2,2540,'叙永县',510524,510524),(2548,2,2540,'古蔺县',510525,510525),(2549,1,2503,'德阳市',510600,510600),(2550,2,2549,'市辖区',510601,510601),(2551,2,2549,'旌阳区',510603,510603),(2552,2,2549,'中江县',510623,510623),(2553,2,2549,'罗江县',510626,510626),(2554,2,2549,'广汉市',510681,510681),(2555,2,2549,'什邡市',510682,510682),(2556,2,2549,'绵竹市',510683,510683),(2557,1,2503,'绵阳市',510700,510700),(2558,2,2557,'市辖区',510701,510701),(2559,2,2557,'涪城区',510703,510703),(2560,2,2557,'游仙区',510704,510704),(2561,2,2557,'三台县',510722,510722),(2562,2,2557,'盐亭县',510723,510723),(2563,2,2557,'安县',510724,510724),(2564,2,2557,'梓潼县',510725,510725),(2565,2,2557,'北川羌族自治县',510726,510726),(2566,2,2557,'平武县',510727,510727),(2567,2,2557,'江油市',510781,510781),(2568,1,2503,'广元市',510800,510800),(2569,2,2568,'市辖区',510801,510801),(2570,2,2568,'利州区',510802,510802),(2571,2,2568,'昭化区',510811,510811),(2572,2,2568,'朝天区',510812,510812),(2573,2,2568,'旺苍县',510821,510821),(2574,2,2568,'青川县',510822,510822),(2575,2,2568,'剑阁县',510823,510823),(2576,2,2568,'苍溪县',510824,510824),(2577,1,2503,'遂宁市',510900,510900),(2578,2,2577,'市辖区',510901,510901),(2579,2,2577,'船山区',510903,510903),(2580,2,2577,'安居区',510904,510904),(2581,2,2577,'蓬溪县',510921,510921),(2582,2,2577,'射洪县',510922,510922),(2583,2,2577,'大英县',510923,510923),(2584,1,2503,'内江市',511000,511000),(2585,2,2584,'市辖区',511001,511001),(2586,2,2584,'市中区',511002,511002),(2587,2,2584,'东兴区',511011,511011),(2588,2,2584,'威远县',511024,511024),(2589,2,2584,'资中县',511025,511025),(2590,2,2584,'隆昌县',511028,511028),(2591,1,2503,'乐山市',511100,511100),(2592,2,2591,'市辖区',511101,511101),(2593,2,2591,'市中区',511102,511102),(2594,2,2591,'沙湾区',511111,511111),(2595,2,2591,'五通桥区',511112,511112),(2596,2,2591,'金口河区',511113,511113),(2597,2,2591,'犍为县',511123,511123),(2598,2,2591,'井研县',511124,511124),(2599,2,2591,'夹江县',511126,511126),(2600,2,2591,'沐川县',511129,511129),(2601,2,2591,'峨边彝族自治县',511132,511132),(2602,2,2591,'马边彝族自治县',511133,511133),(2603,2,2591,'峨眉山市',511181,511181),(2604,1,2503,'南充市',511300,511300),(2605,2,2604,'市辖区',511301,511301),(2606,2,2604,'顺庆区',511302,511302),(2607,2,2604,'高坪区',511303,511303),(2608,2,2604,'嘉陵区',511304,511304),(2609,2,2604,'南部县',511321,511321),(2610,2,2604,'营山县',511322,511322),(2611,2,2604,'蓬安县',511323,511323),(2612,2,2604,'仪陇县',511324,511324),(2613,2,2604,'西充县',511325,511325),(2614,2,2604,'阆中市',511381,511381),(2615,1,2503,'眉山市',511400,511400),(2616,2,2615,'市辖区',511401,511401),(2617,2,2615,'东坡区',511402,511402),(2618,2,2615,'仁寿县',511421,511421),(2619,2,2615,'彭山县',511422,511422),(2620,2,2615,'洪雅县',511423,511423),(2621,2,2615,'丹棱县',511424,511424),(2622,2,2615,'青神县',511425,511425),(2623,1,2503,'宜宾市',511500,511500),(2624,2,2623,'市辖区',511501,511501),(2625,2,2623,'翠屏区',511502,511502),(2626,2,2623,'南溪区',511503,511503),(2627,2,2623,'宜宾县',511521,511521),(2628,2,2623,'江安县',511523,511523),(2629,2,2623,'长宁县',511524,511524),(2630,2,2623,'高县',511525,511525),(2631,2,2623,'珙县',511526,511526),(2632,2,2623,'筠连县',511527,511527),(2633,2,2623,'兴文县',511528,511528),(2634,2,2623,'屏山县',511529,511529),(2635,1,2503,'广安市',511600,511600),(2636,2,2635,'市辖区',511601,511601),(2637,2,2635,'广安区',511602,511602),(2638,2,2635,'前锋区',511603,511603),(2639,2,2635,'岳池县',511621,511621),(2640,2,2635,'武胜县',511622,511622),(2641,2,2635,'邻水县',511623,511623),(2642,2,2635,'华蓥市',511681,511681),(2643,1,2503,'达州市',511700,511700),(2644,2,2643,'市辖区',511701,511701),(2645,2,2643,'通川区',511702,511702),(2646,2,2643,'达川区',511703,511703),(2647,2,2643,'宣汉县',511722,511722),(2648,2,2643,'开江县',511723,511723),(2649,2,2643,'大竹县',511724,511724),(2650,2,2643,'渠县',511725,511725),(2651,2,2643,'万源市',511781,511781),(2652,1,2503,'雅安市',511800,511800),(2653,2,2652,'市辖区',511801,511801),(2654,2,2652,'雨城区',511802,511802),(2655,2,2652,'名山区',511803,511803),(2656,2,2652,'荥经县',511822,511822),(2657,2,2652,'汉源县',511823,511823),(2658,2,2652,'石棉县',511824,511824),(2659,2,2652,'天全县',511825,511825),(2660,2,2652,'芦山县',511826,511826),(2661,2,2652,'宝兴县',511827,511827),(2662,1,2503,'巴中市',511900,511900),(2663,2,2662,'市辖区',511901,511901),(2664,2,2662,'巴州区',511902,511902),(2665,2,2662,'恩阳区',511903,511903),(2666,2,2662,'通江县',511921,511921),(2667,2,2662,'南江县',511922,511922),(2668,2,2662,'平昌县',511923,511923),(2669,1,2503,'资阳市',512000,512000),(2670,2,2669,'市辖区',512001,512001),(2671,2,2669,'雁江区',512002,512002),(2672,2,2669,'安岳县',512021,512021),(2673,2,2669,'乐至县',512022,512022),(2674,2,2669,'简阳市',512081,512081),(2675,1,2503,'阿坝藏族羌族自治州',513200,513200),(2676,2,2675,'汶川县',513221,513221),(2677,2,2675,'理县',513222,513222),(2678,2,2675,'茂县',513223,513223),(2679,2,2675,'松潘县',513224,513224),(2680,2,2675,'九寨沟县',513225,513225),(2681,2,2675,'金川县',513226,513226),(2682,2,2675,'小金县',513227,513227),(2683,2,2675,'黑水县',513228,513228),(2684,2,2675,'马尔康县',513229,513229),(2685,2,2675,'壤塘县',513230,513230),(2686,2,2675,'阿坝县',513231,513231),(2687,2,2675,'若尔盖县',513232,513232),(2688,2,2675,'红原县',513233,513233),(2689,1,2503,'甘孜藏族自治州',513300,513300),(2690,2,2689,'康定县',513321,513321),(2691,2,2689,'泸定县',513322,513322),(2692,2,2689,'丹巴县',513323,513323),(2693,2,2689,'九龙县',513324,513324),(2694,2,2689,'雅江县',513325,513325),(2695,2,2689,'道孚县',513326,513326),(2696,2,2689,'炉霍县',513327,513327),(2697,2,2689,'甘孜县',513328,513328),(2698,2,2689,'新龙县',513329,513329),(2699,2,2689,'德格县',513330,513330),(2700,2,2689,'白玉县',513331,513331),(2701,2,2689,'石渠县',513332,513332),(2702,2,2689,'色达县',513333,513333),(2703,2,2689,'理塘县',513334,513334),(2704,2,2689,'巴塘县',513335,513335),(2705,2,2689,'乡城县',513336,513336),(2706,2,2689,'稻城县',513337,513337),(2707,2,2689,'得荣县',513338,513338),(2708,1,2503,'凉山彝族自治州',513400,513400),(2709,2,2708,'西昌市',513401,513401),(2710,2,2708,'木里藏族自治县',513422,513422),(2711,2,2708,'盐源县',513423,513423),(2712,2,2708,'德昌县',513424,513424),(2713,2,2708,'会理县',513425,513425),(2714,2,2708,'会东县',513426,513426),(2715,2,2708,'宁南县',513427,513427),(2716,2,2708,'普格县',513428,513428),(2717,2,2708,'布拖县',513429,513429),(2718,2,2708,'金阳县',513430,513430),(2719,2,2708,'昭觉县',513431,513431),(2720,2,2708,'喜德县',513432,513432),(2721,2,2708,'冕宁县',513433,513433),(2722,2,2708,'越西县',513434,513434),(2723,2,2708,'甘洛县',513435,513435),(2724,2,2708,'美姑县',513436,513436),(2725,2,2708,'雷波县',513437,513437),(2726,0,NULL,'贵州省',520000,520000),(2727,1,2726,'贵阳市',520100,520100),(2728,2,2727,'市辖区',520101,520101),(2729,2,2727,'南明区',520102,520102),(2730,2,2727,'云岩区',520103,520103),(2731,2,2727,'花溪区',520111,520111),(2732,2,2727,'乌当区',520112,520112),(2733,2,2727,'白云区',520113,520113),(2734,2,2727,'观山湖区',520115,520115),(2735,2,2727,'开阳县',520121,520121),(2736,2,2727,'息烽县',520122,520122),(2737,2,2727,'修文县',520123,520123),(2738,2,2727,'清镇市',520181,520181),(2739,1,2726,'六盘水市',520200,520200),(2740,2,2739,'钟山区',520201,520201),(2741,2,2739,'六枝特区',520203,520203),(2742,2,2739,'水城县',520221,520221),(2743,2,2739,'盘县',520222,520222),(2744,1,2726,'遵义市',520300,520300),(2745,2,2744,'市辖区',520301,520301),(2746,2,2744,'红花岗区',520302,520302),(2747,2,2744,'汇川区',520303,520303),(2748,2,2744,'遵义县',520321,520321),(2749,2,2744,'桐梓县',520322,520322),(2750,2,2744,'绥阳县',520323,520323),(2751,2,2744,'正安县',520324,520324),(2752,2,2744,'道真仡佬族苗族自治县',520325,520325),(2753,2,2744,'务川仡佬族苗族自治县',520326,520326),(2754,2,2744,'凤冈县',520327,520327),(2755,2,2744,'湄潭县',520328,520328),(2756,2,2744,'余庆县',520329,520329),(2757,2,2744,'习水县',520330,520330),(2758,2,2744,'赤水市',520381,520381),(2759,2,2744,'仁怀市',520382,520382),(2760,1,2726,'安顺市',520400,520400),(2761,2,2760,'市辖区',520401,520401),(2762,2,2760,'西秀区',520402,520402),(2763,2,2760,'平坝县',520421,520421),(2764,2,2760,'普定县',520422,520422),(2765,2,2760,'镇宁布依族苗族自治县',520423,520423),(2766,2,2760,'关岭布依族苗族自治县',520424,520424),(2767,2,2760,'紫云苗族布依族自治县',520425,520425),(2768,1,2726,'毕节市',520500,520500),(2769,2,2768,'市辖区',520501,520501),(2770,2,2768,'七星关区',520502,520502),(2771,2,2768,'大方县',520521,520521),(2772,2,2768,'黔西县',520522,520522),(2773,2,2768,'金沙县',520523,520523),(2774,2,2768,'织金县',520524,520524),(2775,2,2768,'纳雍县',520525,520525),(2776,2,2768,'威宁彝族回族苗族自治县',520526,520526),(2777,2,2768,'赫章县',520527,520527),(2778,1,2726,'铜仁市',520600,520600),(2779,2,2778,'市辖区',520601,520601),(2780,2,2778,'碧江区',520602,520602),(2781,2,2778,'万山区',520603,520603),(2782,2,2778,'江口县',520621,520621),(2783,2,2778,'玉屏侗族自治县',520622,520622),(2784,2,2778,'石阡县',520623,520623),(2785,2,2778,'思南县',520624,520624),(2786,2,2778,'印江土家族苗族自治县',520625,520625),(2787,2,2778,'德江县',520626,520626),(2788,2,2778,'沿河土家族自治县',520627,520627),(2789,2,2778,'松桃苗族自治县',520628,520628),(2790,1,2726,'黔西南布依族苗族自治州',522300,522300),(2791,2,2790,'兴义市',522301,522301),(2792,2,2790,'兴仁县',522322,522322),(2793,2,2790,'普安县',522323,522323),(2794,2,2790,'晴隆县',522324,522324),(2795,2,2790,'贞丰县',522325,522325),(2796,2,2790,'望谟县',522326,522326),(2797,2,2790,'册亨县',522327,522327),(2798,2,2790,'安龙县',522328,522328),(2799,1,2726,'黔东南苗族侗族自治州',522600,522600),(2800,2,2799,'凯里市',522601,522601),(2801,2,2799,'黄平县',522622,522622),(2802,2,2799,'施秉县',522623,522623),(2803,2,2799,'三穗县',522624,522624),(2804,2,2799,'镇远县',522625,522625),(2805,2,2799,'岑巩县',522626,522626),(2806,2,2799,'天柱县',522627,522627),(2807,2,2799,'锦屏县',522628,522628),(2808,2,2799,'剑河县',522629,522629),(2809,2,2799,'台江县',522630,522630),(2810,2,2799,'黎平县',522631,522631),(2811,2,2799,'榕江县',522632,522632),(2812,2,2799,'从江县',522633,522633),(2813,2,2799,'雷山县',522634,522634),(2814,2,2799,'麻江县',522635,522635),(2815,2,2799,'丹寨县',522636,522636),(2816,1,2726,'黔南布依族苗族自治州',522700,522700),(2817,2,2816,'都匀市',522701,522701),(2818,2,2816,'福泉市',522702,522702),(2819,2,2816,'荔波县',522722,522722),(2820,2,2816,'贵定县',522723,522723),(2821,2,2816,'瓮安县',522725,522725),(2822,2,2816,'独山县',522726,522726),(2823,2,2816,'平塘县',522727,522727),(2824,2,2816,'罗甸县',522728,522728),(2825,2,2816,'长顺县',522729,522729),(2826,2,2816,'龙里县',522730,522730),(2827,2,2816,'惠水县',522731,522731),(2828,2,2816,'三都水族自治县',522732,522732),(2829,0,NULL,'云南省',530000,530000),(2830,1,2829,'昆明市',530100,530100),(2831,2,2830,'市辖区',530101,530101),(2832,2,2830,'五华区',530102,530102),(2833,2,2830,'盘龙区',530103,530103),(2834,2,2830,'官渡区',530111,530111),(2835,2,2830,'西山区',530112,530112),(2836,2,2830,'东川区',530113,530113),(2837,2,2830,'呈贡区',530114,530114),(2838,2,2830,'晋宁县',530122,530122),(2839,2,2830,'富民县',530124,530124),(2840,2,2830,'宜良县',530125,530125),(2841,2,2830,'石林彝族自治县',530126,530126),(2842,2,2830,'嵩明县',530127,530127),(2843,2,2830,'禄劝彝族苗族自治县',530128,530128),(2844,2,2830,'寻甸回族彝族自治县',530129,530129),(2845,2,2830,'安宁市',530181,530181),(2846,1,2829,'曲靖市',530300,530300),(2847,2,2846,'市辖区',530301,530301),(2848,2,2846,'麒麟区',530302,530302),(2849,2,2846,'马龙县',530321,530321),(2850,2,2846,'陆良县',530322,530322),(2851,2,2846,'师宗县',530323,530323),(2852,2,2846,'罗平县',530324,530324),(2853,2,2846,'富源县',530325,530325),(2854,2,2846,'会泽县',530326,530326),(2855,2,2846,'沾益县',530328,530328),(2856,2,2846,'宣威市',530381,530381),(2857,1,2829,'玉溪市',530400,530400),(2858,2,2857,'市辖区',530401,530401),(2859,2,2857,'红塔区',530402,530402),(2860,2,2857,'江川县',530421,530421),(2861,2,2857,'澄江县',530422,530422),(2862,2,2857,'通海县',530423,530423),(2863,2,2857,'华宁县',530424,530424),(2864,2,2857,'易门县',530425,530425),(2865,2,2857,'峨山彝族自治县',530426,530426),(2866,2,2857,'新平彝族傣族自治县',530427,530427),(2867,2,2857,'元江哈尼族彝族傣族自治县',530428,530428),(2868,1,2829,'保山市',530500,530500),(2869,2,2868,'市辖区',530501,530501),(2870,2,2868,'隆阳区',530502,530502),(2871,2,2868,'施甸县',530521,530521),(2872,2,2868,'腾冲县',530522,530522),(2873,2,2868,'龙陵县',530523,530523),(2874,2,2868,'昌宁县',530524,530524),(2875,1,2829,'昭通市',530600,530600),(2876,2,2875,'市辖区',530601,530601),(2877,2,2875,'昭阳区',530602,530602),(2878,2,2875,'鲁甸县',530621,530621),(2879,2,2875,'巧家县',530622,530622),(2880,2,2875,'盐津县',530623,530623),(2881,2,2875,'大关县',530624,530624),(2882,2,2875,'永善县',530625,530625),(2883,2,2875,'绥江县',530626,530626),(2884,2,2875,'镇雄县',530627,530627),(2885,2,2875,'彝良县',530628,530628),(2886,2,2875,'威信县',530629,530629),(2887,2,2875,'水富县',530630,530630),(2888,1,2829,'丽江市',530700,530700),(2889,2,2888,'市辖区',530701,530701),(2890,2,2888,'古城区',530702,530702),(2891,2,2888,'玉龙纳西族自治县',530721,530721),(2892,2,2888,'永胜县',530722,530722),(2893,2,2888,'华坪县',530723,530723),(2894,2,2888,'宁蒗彝族自治县',530724,530724),(2895,1,2829,'普洱市',530800,530800),(2896,2,2895,'市辖区',530801,530801),(2897,2,2895,'思茅区',530802,530802),(2898,2,2895,'宁洱哈尼族彝族自治县',530821,530821),(2899,2,2895,'墨江哈尼族自治县',530822,530822),(2900,2,2895,'景东彝族自治县',530823,530823),(2901,2,2895,'景谷傣族彝族自治县',530824,530824),(2902,2,2895,'镇沅彝族哈尼族拉祜族自治县',530825,530825),(2903,2,2895,'江城哈尼族彝族自治县',530826,530826),(2904,2,2895,'孟连傣族拉祜族佤族自治县',530827,530827),(2905,2,2895,'澜沧拉祜族自治县',530828,530828),(2906,2,2895,'西盟佤族自治县',530829,530829),(2907,1,2829,'临沧市',530900,530900),(2908,2,2907,'市辖区',530901,530901),(2909,2,2907,'临翔区',530902,530902),(2910,2,2907,'凤庆县',530921,530921),(2911,2,2907,'云县',530922,530922),(2912,2,2907,'永德县',530923,530923),(2913,2,2907,'镇康县',530924,530924),(2914,2,2907,'双江拉祜族佤族布朗族傣族自治县',530925,530925),(2915,2,2907,'耿马傣族佤族自治县',530926,530926),(2916,2,2907,'沧源佤族自治县',530927,530927),(2917,1,2829,'楚雄彝族自治州',532300,532300),(2918,2,2917,'楚雄市',532301,532301),(2919,2,2917,'双柏县',532322,532322),(2920,2,2917,'牟定县',532323,532323),(2921,2,2917,'南华县',532324,532324),(2922,2,2917,'姚安县',532325,532325),(2923,2,2917,'大姚县',532326,532326),(2924,2,2917,'永仁县',532327,532327),(2925,2,2917,'元谋县',532328,532328),(2926,2,2917,'武定县',532329,532329),(2927,2,2917,'禄丰县',532331,532331),(2928,1,2829,'红河哈尼族彝族自治州',532500,532500),(2929,2,2928,'个旧市',532501,532501),(2930,2,2928,'开远市',532502,532502),(2931,2,2928,'蒙自市',532503,532503),(2932,2,2928,'弥勒市',532504,532504),(2933,2,2928,'屏边苗族自治县',532523,532523),(2934,2,2928,'建水县',532524,532524),(2935,2,2928,'石屏县',532525,532525),(2936,2,2928,'泸西县',532527,532527),(2937,2,2928,'元阳县',532528,532528),(2938,2,2928,'红河县',532529,532529),(2939,2,2928,'金平苗族瑶族傣族自治县',532530,532530),(2940,2,2928,'绿春县',532531,532531),(2941,2,2928,'河口瑶族自治县',532532,532532),(2942,1,2829,'文山壮族苗族自治州',532600,532600),(2943,2,2942,'文山市',532601,532601),(2944,2,2942,'砚山县',532622,532622),(2945,2,2942,'西畴县',532623,532623),(2946,2,2942,'麻栗坡县',532624,532624),(2947,2,2942,'马关县',532625,532625),(2948,2,2942,'丘北县',532626,532626),(2949,2,2942,'广南县',532627,532627),(2950,2,2942,'富宁县',532628,532628),(2951,1,2829,'西双版纳傣族自治州',532800,532800),(2952,2,2951,'景洪市',532801,532801),(2953,2,2951,'勐海县',532822,532822),(2954,2,2951,'勐腊县',532823,532823),(2955,1,2829,'大理白族自治州',532900,532900),(2956,2,2955,'大理市',532901,532901),(2957,2,2955,'漾濞彝族自治县',532922,532922),(2958,2,2955,'祥云县',532923,532923),(2959,2,2955,'宾川县',532924,532924),(2960,2,2955,'弥渡县',532925,532925),(2961,2,2955,'南涧彝族自治县',532926,532926),(2962,2,2955,'巍山彝族回族自治县',532927,532927),(2963,2,2955,'永平县',532928,532928),(2964,2,2955,'云龙县',532929,532929),(2965,2,2955,'洱源县',532930,532930),(2966,2,2955,'剑川县',532931,532931),(2967,2,2955,'鹤庆县',532932,532932),(2968,1,2829,'德宏傣族景颇族自治州',533100,533100),(2969,2,2968,'瑞丽市',533102,533102),(2970,2,2968,'芒市',533103,533103),(2971,2,2968,'梁河县',533122,533122),(2972,2,2968,'盈江县',533123,533123),(2973,2,2968,'陇川县',533124,533124),(2974,1,2829,'怒江傈僳族自治州',533300,533300),(2975,2,2974,'泸水县',533321,533321),(2976,2,2974,'福贡县',533323,533323),(2977,2,2974,'贡山独龙族怒族自治县',533324,533324),(2978,2,2974,'兰坪白族普米族自治县',533325,533325),(2979,1,2829,'迪庆藏族自治州',533400,533400),(2980,2,2979,'香格里拉县',533421,533421),(2981,2,2979,'德钦县',533422,533422),(2982,2,2979,'维西傈僳族自治县',533423,533423),(2983,0,NULL,'西藏自治区',540000,540000),(2984,1,2983,'拉萨市',540100,540100),(2985,2,2984,'市辖区',540101,540101),(2986,2,2984,'城关区',540102,540102),(2987,2,2984,'林周县',540121,540121),(2988,2,2984,'当雄县',540122,540122),(2989,2,2984,'尼木县',540123,540123),(2990,2,2984,'曲水县',540124,540124),(2991,2,2984,'堆龙德庆县',540125,540125),(2992,2,2984,'达孜县',540126,540126),(2993,2,2984,'墨竹工卡县',540127,540127),(2994,1,2983,'日喀则市',540200,540200),(2995,2,2994,'桑珠孜区',540202,540202),(2996,2,2994,'南木林县',540221,540221),(2997,2,2994,'江孜县',540222,540222),(2998,2,2994,'定日县',540223,540223),(2999,2,2994,'萨迦县',540224,540224),(3000,2,2994,'拉孜县',540225,540225),(3001,2,2994,'昂仁县',540226,540226),(3002,2,2994,'谢通门县',540227,540227),(3003,2,2994,'白朗县',540228,540228),(3004,2,2994,'仁布县',540229,540229),(3005,2,2994,'康马县',540230,540230),(3006,2,2994,'定结县',540231,540231),(3007,2,2994,'仲巴县',540232,540232),(3008,2,2994,'亚东县',540233,540233),(3009,2,2994,'吉隆县',540234,540234),(3010,2,2994,'聂拉木县',540235,540235),(3011,2,2994,'萨嘎县',540236,540236),(3012,2,2994,'岗巴县',540237,540237),(3013,1,2983,'昌都地区',542100,542100),(3014,2,3013,'昌都县',542121,542121),(3015,2,3013,'江达县',542122,542122),(3016,2,3013,'贡觉县',542123,542123),(3017,2,3013,'类乌齐县',542124,542124),(3018,2,3013,'丁青县',542125,542125),(3019,2,3013,'察雅县',542126,542126),(3020,2,3013,'八宿县',542127,542127),(3021,2,3013,'左贡县',542128,542128),(3022,2,3013,'芒康县',542129,542129),(3023,2,3013,'洛隆县',542132,542132),(3024,2,3013,'边坝县',542133,542133),(3025,1,2983,'山南地区',542200,542200),(3026,2,3025,'乃东县',542221,542221),(3027,2,3025,'扎囊县',542222,542222),(3028,2,3025,'贡嘎县',542223,542223),(3029,2,3025,'桑日县',542224,542224),(3030,2,3025,'琼结县',542225,542225),(3031,2,3025,'曲松县',542226,542226),(3032,2,3025,'措美县',542227,542227),(3033,2,3025,'洛扎县',542228,542228),(3034,2,3025,'加查县',542229,542229),(3035,2,3025,'隆子县',542231,542231),(3036,2,3025,'错那县',542232,542232),(3037,2,3025,'浪卡子县',542233,542233),(3038,1,2983,'那曲地区',542400,542400),(3039,2,3038,'那曲县',542421,542421),(3040,2,3038,'嘉黎县',542422,542422),(3041,2,3038,'比如县',542423,542423),(3042,2,3038,'聂荣县',542424,542424),(3043,2,3038,'安多县',542425,542425),(3044,2,3038,'申扎县',542426,542426),(3045,2,3038,'索县',542427,542427),(3046,2,3038,'班戈县',542428,542428),(3047,2,3038,'巴青县',542429,542429),(3048,2,3038,'尼玛县',542430,542430),(3049,2,3038,'双湖县',542431,542431),(3050,1,2983,'阿里地区',542500,542500),(3051,2,3050,'普兰县',542521,542521);
INSERT INTO `sys_area` VALUES (3052,2,3050,'札达县',542522,542522),(3053,2,3050,'噶尔县',542523,542523),(3054,2,3050,'日土县',542524,542524),(3055,2,3050,'革吉县',542525,542525),(3056,2,3050,'改则县',542526,542526),(3057,2,3050,'措勤县',542527,542527),(3058,1,2983,'林芝地区',542600,542600),(3059,2,3058,'林芝县',542621,542621),(3060,2,3058,'工布江达县',542622,542622),(3061,2,3058,'米林县',542623,542623),(3062,2,3058,'墨脱县',542624,542624),(3063,2,3058,'波密县',542625,542625),(3064,2,3058,'察隅县',542626,542626),(3065,2,3058,'朗县',542627,542627),(3066,0,NULL,'陕西省',610000,610000),(3067,1,3066,'西安市',610100,610100),(3068,2,3067,'市辖区',610101,610101),(3069,2,3067,'新城区',610102,610102),(3070,2,3067,'碑林区',610103,610103),(3071,2,3067,'莲湖区',610104,610104),(3072,2,3067,'灞桥区',610111,610111),(3073,2,3067,'未央区',610112,610112),(3074,2,3067,'雁塔区',610113,610113),(3075,2,3067,'阎良区',610114,610114),(3076,2,3067,'临潼区',610115,610115),(3077,2,3067,'长安区',610116,610116),(3078,2,3067,'蓝田县',610122,610122),(3079,2,3067,'周至县',610124,610124),(3080,2,3067,'户县',610125,610125),(3081,2,3067,'高陵县',610126,610126),(3082,1,3066,'铜川市',610200,610200),(3083,2,3082,'市辖区',610201,610201),(3084,2,3082,'王益区',610202,610202),(3085,2,3082,'印台区',610203,610203),(3086,2,3082,'耀州区',610204,610204),(3087,2,3082,'宜君县',610222,610222),(3088,1,3066,'宝鸡市',610300,610300),(3089,2,3088,'市辖区',610301,610301),(3090,2,3088,'渭滨区',610302,610302),(3091,2,3088,'金台区',610303,610303),(3092,2,3088,'陈仓区',610304,610304),(3093,2,3088,'凤翔县',610322,610322),(3094,2,3088,'岐山县',610323,610323),(3095,2,3088,'扶风县',610324,610324),(3096,2,3088,'眉县',610326,610326),(3097,2,3088,'陇县',610327,610327),(3098,2,3088,'千阳县',610328,610328),(3099,2,3088,'麟游县',610329,610329),(3100,2,3088,'凤县',610330,610330),(3101,2,3088,'太白县',610331,610331),(3102,1,3066,'咸阳市',610400,610400),(3103,2,3102,'市辖区',610401,610401),(3104,2,3102,'秦都区',610402,610402),(3105,2,3102,'杨陵区',610403,610403),(3106,2,3102,'渭城区',610404,610404),(3107,2,3102,'三原县',610422,610422),(3108,2,3102,'泾阳县',610423,610423),(3109,2,3102,'乾县',610424,610424),(3110,2,3102,'礼泉县',610425,610425),(3111,2,3102,'永寿县',610426,610426),(3112,2,3102,'彬县',610427,610427),(3113,2,3102,'长武县',610428,610428),(3114,2,3102,'旬邑县',610429,610429),(3115,2,3102,'淳化县',610430,610430),(3116,2,3102,'武功县',610431,610431),(3117,2,3102,'兴平市',610481,610481),(3118,1,3066,'渭南市',610500,610500),(3119,2,3118,'市辖区',610501,610501),(3120,2,3118,'临渭区',610502,610502),(3121,2,3118,'华县',610521,610521),(3122,2,3118,'潼关县',610522,610522),(3123,2,3118,'大荔县',610523,610523),(3124,2,3118,'合阳县',610524,610524),(3125,2,3118,'澄城县',610525,610525),(3126,2,3118,'蒲城县',610526,610526),(3127,2,3118,'白水县',610527,610527),(3128,2,3118,'富平县',610528,610528),(3129,2,3118,'韩城市',610581,610581),(3130,2,3118,'华阴市',610582,610582),(3131,1,3066,'延安市',610600,610600),(3132,2,3131,'市辖区',610601,610601),(3133,2,3131,'宝塔区',610602,610602),(3134,2,3131,'延长县',610621,610621),(3135,2,3131,'延川县',610622,610622),(3136,2,3131,'子长县',610623,610623),(3137,2,3131,'安塞县',610624,610624),(3138,2,3131,'志丹县',610625,610625),(3139,2,3131,'吴起县',610626,610626),(3140,2,3131,'甘泉县',610627,610627),(3141,2,3131,'富县',610628,610628),(3142,2,3131,'洛川县',610629,610629),(3143,2,3131,'宜川县',610630,610630),(3144,2,3131,'黄龙县',610631,610631),(3145,2,3131,'黄陵县',610632,610632),(3146,1,3066,'汉中市',610700,610700),(3147,2,3146,'市辖区',610701,610701),(3148,2,3146,'汉台区',610702,610702),(3149,2,3146,'南郑县',610721,610721),(3150,2,3146,'城固县',610722,610722),(3151,2,3146,'洋县',610723,610723),(3152,2,3146,'西乡县',610724,610724),(3153,2,3146,'勉县',610725,610725),(3154,2,3146,'宁强县',610726,610726),(3155,2,3146,'略阳县',610727,610727),(3156,2,3146,'镇巴县',610728,610728),(3157,2,3146,'留坝县',610729,610729),(3158,2,3146,'佛坪县',610730,610730),(3159,1,3066,'榆林市',610800,610800),(3160,2,3159,'市辖区',610801,610801),(3161,2,3159,'榆阳区',610802,610802),(3162,2,3159,'神木县',610821,610821),(3163,2,3159,'府谷县',610822,610822),(3164,2,3159,'横山县',610823,610823),(3165,2,3159,'靖边县',610824,610824),(3166,2,3159,'定边县',610825,610825),(3167,2,3159,'绥德县',610826,610826),(3168,2,3159,'米脂县',610827,610827),(3169,2,3159,'佳县',610828,610828),(3170,2,3159,'吴堡县',610829,610829),(3171,2,3159,'清涧县',610830,610830),(3172,2,3159,'子洲县',610831,610831),(3173,1,3066,'安康市',610900,610900),(3174,2,3173,'市辖区',610901,610901),(3175,2,3173,'汉滨区',610902,610902),(3176,2,3173,'汉阴县',610921,610921),(3177,2,3173,'石泉县',610922,610922),(3178,2,3173,'宁陕县',610923,610923),(3179,2,3173,'紫阳县',610924,610924),(3180,2,3173,'岚皋县',610925,610925),(3181,2,3173,'平利县',610926,610926),(3182,2,3173,'镇坪县',610927,610927),(3183,2,3173,'旬阳县',610928,610928),(3184,2,3173,'白河县',610929,610929),(3185,1,3066,'商洛市',611000,611000),(3186,2,3185,'市辖区',611001,611001),(3187,2,3185,'商州区',611002,611002),(3188,2,3185,'洛南县',611021,611021),(3189,2,3185,'丹凤县',611022,611022),(3190,2,3185,'商南县',611023,611023),(3191,2,3185,'山阳县',611024,611024),(3192,2,3185,'镇安县',611025,611025),(3193,2,3185,'柞水县',611026,611026),(3194,0,NULL,'甘肃省',620000,620000),(3195,1,3194,'兰州市',620100,620100),(3196,2,3195,'市辖区',620101,620101),(3197,2,3195,'城关区',620102,620102),(3198,2,3195,'七里河区',620103,620103),(3199,2,3195,'西固区',620104,620104),(3200,2,3195,'安宁区',620105,620105),(3201,2,3195,'红古区',620111,620111),(3202,2,3195,'永登县',620121,620121),(3203,2,3195,'皋兰县',620122,620122),(3204,2,3195,'榆中县',620123,620123),(3205,1,3194,'嘉峪关市',620200,620200),(3206,2,3205,'市辖区',620201,620201),(3207,1,3194,'金昌市',620300,620300),(3208,2,3207,'市辖区',620301,620301),(3209,2,3207,'金川区',620302,620302),(3210,2,3207,'永昌县',620321,620321),(3211,1,3194,'白银市',620400,620400),(3212,2,3211,'市辖区',620401,620401),(3213,2,3211,'白银区',620402,620402),(3214,2,3211,'平川区',620403,620403),(3215,2,3211,'靖远县',620421,620421),(3216,2,3211,'会宁县',620422,620422),(3217,2,3211,'景泰县',620423,620423),(3218,1,3194,'天水市',620500,620500),(3219,2,3218,'市辖区',620501,620501),(3220,2,3218,'秦州区',620502,620502),(3221,2,3218,'麦积区',620503,620503),(3222,2,3218,'清水县',620521,620521),(3223,2,3218,'秦安县',620522,620522),(3224,2,3218,'甘谷县',620523,620523),(3225,2,3218,'武山县',620524,620524),(3226,2,3218,'张家川回族自治县',620525,620525),(3227,1,3194,'武威市',620600,620600),(3228,2,3227,'市辖区',620601,620601),(3229,2,3227,'凉州区',620602,620602),(3230,2,3227,'民勤县',620621,620621),(3231,2,3227,'古浪县',620622,620622),(3232,2,3227,'天祝藏族自治县',620623,620623),(3233,1,3194,'张掖市',620700,620700),(3234,2,3233,'市辖区',620701,620701),(3235,2,3233,'甘州区',620702,620702),(3236,2,3233,'肃南裕固族自治县',620721,620721),(3237,2,3233,'民乐县',620722,620722),(3238,2,3233,'临泽县',620723,620723),(3239,2,3233,'高台县',620724,620724),(3240,2,3233,'山丹县',620725,620725),(3241,1,3194,'平凉市',620800,620800),(3242,2,3241,'市辖区',620801,620801),(3243,2,3241,'崆峒区',620802,620802),(3244,2,3241,'泾川县',620821,620821),(3245,2,3241,'灵台县',620822,620822),(3246,2,3241,'崇信县',620823,620823),(3247,2,3241,'华亭县',620824,620824),(3248,2,3241,'庄浪县',620825,620825),(3249,2,3241,'静宁县',620826,620826),(3250,1,3194,'酒泉市',620900,620900),(3251,2,3250,'市辖区',620901,620901),(3252,2,3250,'肃州区',620902,620902),(3253,2,3250,'金塔县',620921,620921),(3254,2,3250,'瓜州县',620922,620922),(3255,2,3250,'肃北蒙古族自治县',620923,620923),(3256,2,3250,'阿克塞哈萨克族自治县',620924,620924),(3257,2,3250,'玉门市',620981,620981),(3258,2,3250,'敦煌市',620982,620982),(3259,1,3194,'庆阳市',621000,621000),(3260,2,3259,'市辖区',621001,621001),(3261,2,3259,'西峰区',621002,621002),(3262,2,3259,'庆城县',621021,621021),(3263,2,3259,'环县',621022,621022),(3264,2,3259,'华池县',621023,621023),(3265,2,3259,'合水县',621024,621024),(3266,2,3259,'正宁县',621025,621025),(3267,2,3259,'宁县',621026,621026),(3268,2,3259,'镇原县',621027,621027),(3269,1,3194,'定西市',621100,621100),(3270,2,3269,'市辖区',621101,621101),(3271,2,3269,'安定区',621102,621102),(3272,2,3269,'通渭县',621121,621121),(3273,2,3269,'陇西县',621122,621122),(3274,2,3269,'渭源县',621123,621123),(3275,2,3269,'临洮县',621124,621124),(3276,2,3269,'漳县',621125,621125),(3277,2,3269,'岷县',621126,621126),(3278,1,3194,'陇南市',621200,621200),(3279,2,3278,'市辖区',621201,621201),(3280,2,3278,'武都区',621202,621202),(3281,2,3278,'成县',621221,621221),(3282,2,3278,'文县',621222,621222),(3283,2,3278,'宕昌县',621223,621223),(3284,2,3278,'康县',621224,621224),(3285,2,3278,'西和县',621225,621225),(3286,2,3278,'礼县',621226,621226),(3287,2,3278,'徽县',621227,621227),(3288,2,3278,'两当县',621228,621228),(3289,1,3194,'临夏回族自治州',622900,622900),(3290,2,3289,'临夏市',622901,622901),(3291,2,3289,'临夏县',622921,622921),(3292,2,3289,'康乐县',622922,622922),(3293,2,3289,'永靖县',622923,622923),(3294,2,3289,'广河县',622924,622924),(3295,2,3289,'和政县',622925,622925),(3296,2,3289,'东乡族自治县',622926,622926),(3297,2,3289,'积石山保安族东乡族撒拉族自治县',622927,622927),(3298,1,3194,'甘南藏族自治州',623000,623000),(3299,2,3298,'合作市',623001,623001),(3300,2,3298,'临潭县',623021,623021),(3301,2,3298,'卓尼县',623022,623022),(3302,2,3298,'舟曲县',623023,623023),(3303,2,3298,'迭部县',623024,623024),(3304,2,3298,'玛曲县',623025,623025),(3305,2,3298,'碌曲县',623026,623026),(3306,2,3298,'夏河县',623027,623027),(3307,0,NULL,'青海省',630000,630000),(3308,1,3307,'西宁市',630100,630100),(3309,2,3308,'市辖区',630101,630101),(3310,2,3308,'城东区',630102,630102),(3311,2,3308,'城中区',630103,630103),(3312,2,3308,'城西区',630104,630104),(3313,2,3308,'城北区',630105,630105),(3314,2,3308,'大通回族土族自治县',630121,630121),(3315,2,3308,'湟中县',630122,630122),(3316,2,3308,'湟源县',630123,630123),(3317,1,3307,'海东市',630200,630200),(3318,2,3317,'乐都区',630202,630202),(3319,2,3317,'平安县',630221,630221),(3320,2,3317,'民和回族土族自治县',630222,630222),(3321,2,3317,'互助土族自治县',630223,630223),(3322,2,3317,'化隆回族自治县',630224,630224),(3323,2,3317,'循化撒拉族自治县',630225,630225),(3324,1,3307,'海北藏族自治州',632200,632200),(3325,2,3324,'门源回族自治县',632221,632221),(3326,2,3324,'祁连县',632222,632222),(3327,2,3324,'海晏县',632223,632223),(3328,2,3324,'刚察县',632224,632224),(3329,1,3307,'黄南藏族自治州',632300,632300),(3330,2,3329,'同仁县',632321,632321),(3331,2,3329,'尖扎县',632322,632322),(3332,2,3329,'泽库县',632323,632323),(3333,2,3329,'河南蒙古族自治县',632324,632324),(3334,1,3307,'海南藏族自治州',632500,632500),(3335,2,3334,'共和县',632521,632521),(3336,2,3334,'同德县',632522,632522),(3337,2,3334,'贵德县',632523,632523),(3338,2,3334,'兴海县',632524,632524),(3339,2,3334,'贵南县',632525,632525),(3340,1,3307,'果洛藏族自治州',632600,632600),(3341,2,3340,'玛沁县',632621,632621),(3342,2,3340,'班玛县',632622,632622),(3343,2,3340,'甘德县',632623,632623),(3344,2,3340,'达日县',632624,632624),(3345,2,3340,'久治县',632625,632625),(3346,2,3340,'玛多县',632626,632626),(3347,1,3307,'玉树藏族自治州',632700,632700),(3348,2,3347,'玉树市',632701,632701),(3349,2,3347,'杂多县',632722,632722),(3350,2,3347,'称多县',632723,632723),(3351,2,3347,'治多县',632724,632724),(3352,2,3347,'囊谦县',632725,632725),(3353,2,3347,'曲麻莱县',632726,632726),(3354,1,3307,'海西蒙古族藏族自治州',632800,632800),(3355,2,3354,'格尔木市',632801,632801),(3356,2,3354,'德令哈市',632802,632802),(3357,2,3354,'乌兰县',632821,632821),(3358,2,3354,'都兰县',632822,632822),(3359,2,3354,'天峻县',632823,632823),(3360,0,NULL,'宁夏回族自治区',640000,640000),(3361,1,3360,'银川市',640100,640100),(3362,2,3361,'市辖区',640101,640101),(3363,2,3361,'兴庆区',640104,640104),(3364,2,3361,'西夏区',640105,640105),(3365,2,3361,'金凤区',640106,640106),(3366,2,3361,'永宁县',640121,640121),(3367,2,3361,'贺兰县',640122,640122),(3368,2,3361,'灵武市',640181,640181),(3369,1,3360,'石嘴山市',640200,640200),(3370,2,3369,'市辖区',640201,640201),(3371,2,3369,'大武口区',640202,640202),(3372,2,3369,'惠农区',640205,640205),(3373,2,3369,'平罗县',640221,640221),(3374,1,3360,'吴忠市',640300,640300),(3375,2,3374,'市辖区',640301,640301),(3376,2,3374,'利通区',640302,640302),(3377,2,3374,'红寺堡区',640303,640303),(3378,2,3374,'盐池县',640323,640323),(3379,2,3374,'同心县',640324,640324),(3380,2,3374,'青铜峡市',640381,640381),(3381,1,3360,'固原市',640400,640400),(3382,2,3381,'市辖区',640401,640401),(3383,2,3381,'原州区',640402,640402),(3384,2,3381,'西吉县',640422,640422),(3385,2,3381,'隆德县',640423,640423),(3386,2,3381,'泾源县',640424,640424),(3387,2,3381,'彭阳县',640425,640425),(3388,1,3360,'中卫市',640500,640500),(3389,2,3388,'市辖区',640501,640501),(3390,2,3388,'沙坡头区',640502,640502),(3391,2,3388,'中宁县',640521,640521),(3392,2,3388,'海原县',640522,640522),(3393,0,NULL,'新疆维吾尔自治区',650000,650000),(3394,1,3393,'乌鲁木齐市',650100,650100),(3395,2,3394,'市辖区',650101,650101),(3396,2,3394,'天山区',650102,650102),(3397,2,3394,'沙依巴克区',650103,650103),(3398,2,3394,'新市区',650104,650104),(3399,2,3394,'水磨沟区',650105,650105),(3400,2,3394,'头屯河区',650106,650106),(3401,2,3394,'达坂城区',650107,650107),(3402,2,3394,'米东区',650109,650109),(3403,2,3394,'乌鲁木齐县',650121,650121),(3404,1,3393,'克拉玛依市',650200,650200),(3405,2,3404,'市辖区',650201,650201),(3406,2,3404,'独山子区',650202,650202),(3407,2,3404,'克拉玛依区',650203,650203),(3408,2,3404,'白碱滩区',650204,650204),(3409,2,3404,'乌尔禾区',650205,650205),(3410,1,3393,'吐鲁番地区',652100,652100),(3411,2,3410,'吐鲁番市',652101,652101),(3412,2,3410,'鄯善县',652122,652122),(3413,2,3410,'托克逊县',652123,652123),(3414,1,3393,'哈密地区',652200,652200),(3415,2,3414,'哈密市',652201,652201),(3416,2,3414,'巴里坤哈萨克自治县',652222,652222),(3417,2,3414,'伊吾县',652223,652223),(3418,1,3393,'昌吉回族自治州',652300,652300),(3419,2,3418,'昌吉市',652301,652301),(3420,2,3418,'阜康市',652302,652302),(3421,2,3418,'呼图壁县',652323,652323),(3422,2,3418,'玛纳斯县',652324,652324),(3423,2,3418,'奇台县',652325,652325),(3424,2,3418,'吉木萨尔县',652327,652327),(3425,2,3418,'木垒哈萨克自治县',652328,652328),(3426,1,3393,'博尔塔拉蒙古自治州',652700,652700),(3427,2,3426,'博乐市',652701,652701),(3428,2,3426,'阿拉山口市',652702,652702),(3429,2,3426,'精河县',652722,652722),(3430,2,3426,'温泉县',652723,652723),(3431,1,3393,'巴音郭楞蒙古自治州',652800,652800),(3432,2,3431,'库尔勒市',652801,652801),(3433,2,3431,'轮台县',652822,652822),(3434,2,3431,'尉犁县',652823,652823),(3435,2,3431,'若羌县',652824,652824),(3436,2,3431,'且末县',652825,652825),(3437,2,3431,'焉耆回族自治县',652826,652826),(3438,2,3431,'和静县',652827,652827),(3439,2,3431,'和硕县',652828,652828),(3440,2,3431,'博湖县',652829,652829),(3441,1,3393,'阿克苏地区',652900,652900),(3442,2,3441,'阿克苏市',652901,652901),(3443,2,3441,'温宿县',652922,652922),(3444,2,3441,'库车县',652923,652923),(3445,2,3441,'沙雅县',652924,652924),(3446,2,3441,'新和县',652925,652925),(3447,2,3441,'拜城县',652926,652926),(3448,2,3441,'乌什县',652927,652927),(3449,2,3441,'阿瓦提县',652928,652928),(3450,2,3441,'柯坪县',652929,652929),(3451,1,3393,'克孜勒苏柯尔克孜自治州',653000,653000),(3452,2,3451,'阿图什市',653001,653001),(3453,2,3451,'阿克陶县',653022,653022),(3454,2,3451,'阿合奇县',653023,653023),(3455,2,3451,'乌恰县',653024,653024),(3456,1,3393,'喀什地区',653100,653100),(3457,2,3456,'喀什市',653101,653101),(3458,2,3456,'疏附县',653121,653121),(3459,2,3456,'疏勒县',653122,653122),(3460,2,3456,'英吉沙县',653123,653123),(3461,2,3456,'泽普县',653124,653124),(3462,2,3456,'莎车县',653125,653125),(3463,2,3456,'叶城县',653126,653126),(3464,2,3456,'麦盖提县',653127,653127),(3465,2,3456,'岳普湖县',653128,653128),(3466,2,3456,'伽师县',653129,653129),(3467,2,3456,'巴楚县',653130,653130),(3468,2,3456,'塔什库尔干塔吉克自治县',653131,653131),(3469,1,3393,'和田地区',653200,653200),(3470,2,3469,'和田市',653201,653201),(3471,2,3469,'和田县',653221,653221),(3472,2,3469,'墨玉县',653222,653222),(3473,2,3469,'皮山县',653223,653223),(3474,2,3469,'洛浦县',653224,653224),(3475,2,3469,'策勒县',653225,653225),(3476,2,3469,'于田县',653226,653226),(3477,2,3469,'民丰县',653227,653227),(3478,1,3393,'伊犁哈萨克自治州',654000,654000),(3479,2,3478,'伊宁市',654002,654002),(3480,2,3478,'奎屯市',654003,654003),(3481,2,3478,'伊宁县',654021,654021),(3482,2,3478,'察布查尔锡伯自治县',654022,654022),(3483,2,3478,'霍城县',654023,654023),(3484,2,3478,'巩留县',654024,654024),(3485,2,3478,'新源县',654025,654025),(3486,2,3478,'昭苏县',654026,654026),(3487,2,3478,'特克斯县',654027,654027),(3488,2,3478,'尼勒克县',654028,654028),(3489,1,3393,'塔城地区',654200,654200),(3490,2,3489,'塔城市',654201,654201),(3491,2,3489,'乌苏市',654202,654202),(3492,2,3489,'额敏县',654221,654221),(3493,2,3489,'沙湾县',654223,654223),(3494,2,3489,'托里县',654224,654224),(3495,2,3489,'裕民县',654225,654225),(3496,2,3489,'和布克赛尔蒙古自治县',654226,654226),(3497,1,3393,'阿勒泰地区',654300,654300),(3498,2,3497,'阿勒泰市',654301,654301),(3499,2,3497,'布尔津县',654321,654321),(3500,2,3497,'富蕴县',654322,654322),(3501,2,3497,'福海县',654323,654323),(3502,2,3497,'哈巴河县',654324,654324),(3503,2,3497,'青河县',654325,654325),(3504,2,3497,'吉木乃县',654326,654326),(3505,1,3393,'自治区直辖县级行政区划',659000,659000),(3506,2,3505,'石河子市',659001,659001),(3507,2,3505,'阿拉尔市',659002,659002),(3508,2,3505,'图木舒克市',659003,659003),(3509,2,3505,'五家渠市',659004,659004),(3510,0,NULL,'台湾省',710000,710000),(3511,0,NULL,'香港特别行政区',810000,810000),(3512,0,NULL,'澳门特别行政区',820000,820000),(3513,2,2271,'市辖区',441901,441901),(3514,2,2272,'市辖区',442001,442001),(3515,2,2444,'市辖区',460301,460301);

#
# Structure for table "sys_message"
#

DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE `sys_message` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `content` varchar(4000) DEFAULT NULL,
  `user_id` int(20) DEFAULT NULL,
  `is_read` bit(1) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `read_time` datetime DEFAULT NULL,
  `message_type` int(2) DEFAULT NULL,
  `batch_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

#
# Data for table "sys_message"
#


#
# Structure for table "sys_notify"
#

DROP TABLE IF EXISTS `sys_notify`;
CREATE TABLE `sys_notify` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `created_time` datetime DEFAULT NULL,
  `topic` varchar(255) DEFAULT NULL,
  `payload` longtext,
  `is_sent` bit(1) DEFAULT NULL,
  `sent_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "sys_notify"
#


#
# Structure for table "sys_setting"
#

DROP TABLE IF EXISTS `sys_setting`;
CREATE TABLE `sys_setting` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `sys_user_id` int(20) DEFAULT NULL,
  `fee_user_id` int(20) DEFAULT NULL,
  `withdraw_fee_rate_script` varchar(1000) DEFAULT NULL,
  `is_dev` bit(1) DEFAULT NULL,
  `grant_user_id` int(20) DEFAULT NULL,
  `buyer_default_coin` decimal(19,2) DEFAULT NULL,
  `bonus_deepth` int(11) DEFAULT NULL,
  `red_packet_quantity` int(20) DEFAULT NULL,
  `adjust_money` decimal(19,2) DEFAULT NULL,
  `gift_coin` decimal(19,2) DEFAULT NULL,
  `gift_packing_coin` decimal(19,2) DEFAULT NULL,
  `gift_logistics_coin` decimal(19,2) DEFAULT NULL,
  `merchant_default_coin` decimal(19,2) DEFAULT NULL,
  `bonus_rate_script` varchar(1000) DEFAULT NULL,
  `basic_coin_script` varchar(1000) DEFAULT NULL,
  `buyer_pay_expire_in_hours` int(11) DEFAULT NULL,
  `merchant_deliver_expire_in_hours` int(11) DEFAULT NULL,
  `merchant_confirm_expire_in_hours` int(11) DEFAULT NULL,
  `can_comment_after_delivered_in_hours` int(11) DEFAULT NULL,
  `buyer_register_grant_coin` decimal(19,2) DEFAULT NULL,
  `buyer_register_ticket_amount` decimal(19,2) DEFAULT NULL,
  `task_item_complete_point` decimal(19,2) DEFAULT NULL,
  `achievement_award_script` varchar(1000) DEFAULT NULL,
  `sign_in_point_script` varchar(1000) DEFAULT NULL,
  `insurance_coin_rate_script` varchar(1000) DEFAULT NULL,
  `max_auto_confirm_count` int(11) DEFAULT NULL,
  `text_comment_coin_script` varchar(1000) DEFAULT NULL,
  `image_comment_coin_script` varchar(1000) DEFAULT NULL,
  `use_portrait_coin_script` varchar(1000) DEFAULT NULL,
  `use_appearance_coin_script` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

#
# Data for table "sys_setting"
#

INSERT INTO `sys_setting` VALUES (1,1,2,'def result\r\nif (userType == UserType.代理) {\r\n\tif (currencyType == CurrencyType.现金) {\r\n\t\tresult = 0.0\r\n\t} else {\r\n\t\tresult = 0.01\r\n\t}\r\n} else {\r\n\tif (currencyType == CurrencyType.现金) {\r\n\t\tresult = 0.009\r\n\t} else {\r\n\t\tresult = 0.02\r\n\t}\r\n}\r\nreturn result',b'1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

#
# Structure for table "sys_short_message"
#

DROP TABLE IF EXISTS `sys_short_message`;
CREATE TABLE `sys_short_message` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `message` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `error_code` int(11) DEFAULT NULL,
  `error_msg` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

#
# Data for table "sys_short_message"
#


#
# Structure for table "sys_site"
#

DROP TABLE IF EXISTS `sys_site`;
CREATE TABLE `sys_site` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `code` varchar(60) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `domain_url` varchar(255) DEFAULT NULL,
  `qq_login_app_key` varchar(255) DEFAULT NULL,
  `qq_login_app_secret` varchar(255) DEFAULT NULL,
  `weixin_login_app_key` varchar(255) DEFAULT NULL,
  `weixin_login_app_secret` varchar(255) DEFAULT NULL,
  `weixin_pay_mch_id` varchar(255) DEFAULT NULL,
  `weixin_pay_secret` varchar(255) DEFAULT NULL,
  `weixin_mp_app_key` varchar(255) DEFAULT NULL,
  `weixin_mp_app_secret` varchar(255) DEFAULT NULL,
  `alipay_bargainor_id` varchar(255) DEFAULT NULL,
  `alipay_bargainor_key` varchar(255) DEFAULT NULL,
  `is_local_pay` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "sys_site"
#


#
# Structure for table "usr_address"
#

DROP TABLE IF EXISTS `usr_address`;
CREATE TABLE `usr_address` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `is_default` bit(1) DEFAULT NULL,
  `area_id` int(20) DEFAULT NULL,
  `realname` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `province` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `district` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "usr_address"
#


#
# Structure for table "usr_appearance"
#

DROP TABLE IF EXISTS `usr_appearance`;
CREATE TABLE `usr_appearance` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `realname` varchar(255) DEFAULT NULL,
  `id_card_number` varchar(255) DEFAULT NULL,
  `id_card_image1` varchar(255) DEFAULT NULL,
  `id_card_image2` varchar(255) DEFAULT NULL,
  `image1` varchar(255) DEFAULT NULL,
  `image2` varchar(255) DEFAULT NULL,
  `image3` varchar(255) DEFAULT NULL,
  `image4` varchar(255) DEFAULT NULL,
  `image5` varchar(255) DEFAULT NULL,
  `image6` varchar(255) DEFAULT NULL,
  `applied_time` datetime DEFAULT NULL,
  `confirm_status` int(2) DEFAULT NULL,
  `confirm_remark` varchar(255) DEFAULT NULL,
  `confirmed_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "usr_appearance"
#


#
# Structure for table "usr_job"
#

DROP TABLE IF EXISTS `usr_job`;
CREATE TABLE `usr_job` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `job_name` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_job_name` (`job_name`)
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4;

#
# Data for table "usr_job"
#

INSERT INTO `usr_job` VALUES (1,'在校学生'),(2,'销售'),(3,'个体经营/零售'),(4,'市场/市场拓展/公关'),(5,'商务/采购/贸易'),(6,'酒店/餐饮/旅游/其他服务'),(7,'客户服务/技术支持'),(8,'计算机/互联网/IT'),(9,'通信技术'),(10,'美术/设计/创意'),(11,'电子/半导体/仪器仪表'),(12,'电气/能源/动力'),(13,'生产/加工/制造'),(14,'工程/机械技工'),(15,'交通/仓储/物流'),(16,'建筑/房地产/装修/物管'),(17,'金融/银行/证券/投资'),(18,'财务/审计/统计'),(19,'保险'),(20,'法律'),(21,'教育/培训'),(22,'医疗/卫生'),(23,'经营管理/高级管理'),(24,'人力资源'),(25,'政府工作人员'),(26,'美容/保健'),(27,'行政/后勤'),(28,'生物/制药/医疗器械'),(29,'化工'),(30,'能源/矿产/地质勘察'),(31,'军人/警察'),(32,'文体/影视/写作/媒体'),(33,'学术/科研'),(34,'翻译'),(35,'农村外出务工人员'),(36,'农林牧渔劳动者'),(37,'自由职业者'),(38,'待业/无业/失业'),(39,'退休'),(40,'其他');

#
# Structure for table "usr_portrait"
#

DROP TABLE IF EXISTS `usr_portrait`;
CREATE TABLE `usr_portrait` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `gender` int(2) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `area_id` int(20) DEFAULT NULL,
  `hometown_area_id` int(20) DEFAULT NULL,
  `consumption_level` int(2) DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  `job_id` int(20) DEFAULT NULL,
  `job_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "usr_portrait"
#


#
# Structure for table "usr_tag"
#

DROP TABLE IF EXISTS `usr_tag`;
CREATE TABLE `usr_tag` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `tag_type` varchar(255) DEFAULT NULL,
  `tag_name` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_tag_name` (`tag_name`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4;

#
# Data for table "usr_tag"
#

INSERT INTO `usr_tag` VALUES (1,'兴趣爱好','旅游'),(2,'兴趣爱好','健身'),(3,'兴趣爱好','音乐'),(6,'兴趣爱好','电影达人'),(7,'兴趣爱好','美剧迷'),(8,'兴趣爱好','韩剧迷'),(9,'兴趣爱好','偶像剧'),(10,'兴趣爱好','ACG'),(11,'兴趣爱好','摄影发烧友'),(12,'兴趣爱好','爱发呆'),(13,'兴趣爱好','吃货'),(14,'兴趣爱好','数码控'),(15,'兴趣爱好','果粉'),(16,'兴趣爱好','养身专家'),(17,'兴趣爱好','爱看直播'),(18,'游戏','电竞达人'),(19,'游戏','魔兽世界'),(20,'游戏','DotA'),(21,'游戏','LOL'),(22,'游戏','手游达人'),(23,'游戏','页游达人'),(36,'状态','奋斗ing'),(37,'状态','恋爱ing'),(38,'状态','赚钱ing'),(39,'状态','减肥ing'),(40,'状态','带娃ing'),(41,'其他','高帅富'),(42,'其他','白富美'),(43,'其他','宅男'),(44,'其他','腐女'),(45,'其他','技术宅'),(46,'其他','胖子'),(47,'其他','胖美眉');

#
# Structure for table "usr_user"
#

DROP TABLE IF EXISTS `usr_user`;
CREATE TABLE `usr_user` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `phone` varchar(11) DEFAULT NULL,
  `password` varchar(60) DEFAULT NULL,
  `nickname` varchar(60) DEFAULT NULL,
  `user_type` int(2) DEFAULT NULL,
  `qq` varchar(11) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `is_frozen` bit(1) DEFAULT NULL,
  `register_time` datetime DEFAULT NULL,
  `register_ip` varchar(255) DEFAULT NULL,
  `inviter_id` int(20) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `user_rank` int(2) DEFAULT NULL,
  `is_info_completed` bit(1) DEFAULT NULL,
  `invite_code` varchar(60) DEFAULT NULL,
  `vip_expired_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_phone` (`phone`),
  UNIQUE KEY `unique_invite_code` (`invite_code`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4;

#
# Data for table "usr_user"
#

INSERT INTO `usr_user` VALUES (1,'11122223333','c4ca4238a0b923820dcc509a6f75849b','中转账户',2,'1241241024','http://image.1024ke.com/avatar/465a78ec-ab3c-4b5c-bb72-106527e729d2',b'1','2016-04-26 12:14:33','12221',1,'1',0,NULL,NULL,NULL),(2,'11122224444','c4ca4238a0b923820dcc509a6f75849b','资金账户',2,'1241241024','http://image.1024ke.com/avatar/465a78ec-ab3c-4b5c-bb72-106527e729d2',b'1','2016-04-29 10:51:16','123123',1,NULL,0,NULL,NULL,NULL),(3,'33330963096','da9c57b938ef5562d98572686b0e129a','发放账户',2,'123','http://image.1024ke.com/avatar/465a78ec-ab3c-4b5c-bb72-106527e729d2',b'1','2016-04-29 10:51:16','127.0.0.1',1,'1',0,b'0',NULL,NULL),(20,'18888888881','96e79218965eb72c92a549dd5a330112','8888',3,'18888888881','http://image.mayishike.com/avatar_default.jpg',b'0','2016-08-22 23:09:13','180.175.162.97',NULL,NULL,3,b'0','MAKHCW',NULL);

#
# Structure for table "usr_user_log"
#

DROP TABLE IF EXISTS `usr_user_log`;
CREATE TABLE `usr_user_log` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `operation` varchar(255) DEFAULT NULL,
  `operator_id` int(20) DEFAULT NULL,
  `operated_time` datetime DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "usr_user_log"
#


#
# Structure for table "usr_user_setting"
#

DROP TABLE IF EXISTS `usr_user_setting`;
CREATE TABLE `usr_user_setting` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `is_receive_task_sms` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "usr_user_setting"
#


#
# Structure for table "usr_weixin_user"
#

DROP TABLE IF EXISTS `usr_weixin_user`;
CREATE TABLE `usr_weixin_user` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `open_id` varchar(60) DEFAULT NULL,
  `union_id` varchar(60) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_open_id` (`open_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

#
# Data for table "usr_weixin_user"
#

