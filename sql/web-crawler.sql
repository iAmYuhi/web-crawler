DROP TABLE IF EXISTS tb_consult;
CREATE TABLE `tb_consult` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `text` text NOT NULL COMMENT '快讯内容',
  `url` varchar(1000) DEFAULT NULL COMMENT '外链url',
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '快讯时间',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '软删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='快讯';

DROP TABLE IF EXISTS tb_currency;
CREATE TABLE `tb_currency` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `currency_name` varchar(200) NOT NULL DEFAULT '' COMMENT '货币名',
  `currency_img` varchar(500) NOT NULL DEFAULT '' COMMENT '货币图片',
  `market_price` varchar(100) NOT NULL DEFAULT '0' COMMENT '流通市值',
  `price` varchar(100) NOT NULL DEFAULT '0' COMMENT '货币价格',
  `market_num` varchar(100) NOT NULL DEFAULT '0' COMMENT '流通数量',
  `markey_rate` varchar(50) NOT NULL DEFAULT '0' COMMENT '流通率',
  `turnover24h` varchar(100) NOT NULL DEFAULT '0' COMMENT '24小时成交额',
  `rose1h` varchar(50) DEFAULT '' COMMENT '涨幅(1h)',
  `rose24h` varchar(50) DEFAULT '' COMMENT '涨幅(24h)',
  `rose7d` varchar(50) DEFAULT '' COMMENT '涨幅(7d)',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '软删除',
  `high24h` varchar(50) NOT NULL DEFAULT '' COMMENT '24h最高',
  `low24h` varchar(50) NOT NULL DEFAULT '' COMMENT '24h最低',
  `describe` text COMMENT '介绍详情',
  `currency_url` varchar(100) NOT NULL DEFAULT '' COMMENT '跳转url',
  PRIMARY KEY (`id`),
  KEY `uk_currency_url` (`currency_url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='货币列表';

DROP TABLE IF EXISTS tb_news;
CREATE TABLE `tb_news` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `logo` varchar(1000) DEFAULT NULL COMMENT '展示logo',
  `title` varchar(500) NOT NULL DEFAULT '' COMMENT '标题',
  `intro` text COMMENT '简介',
  `content` text COMMENT '详情',
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` int(11) NOT NULL DEFAULT '0' COMMENT '软删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;