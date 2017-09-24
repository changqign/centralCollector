CREATE TABLE `log_infos` (
`id`  int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id' ,
`logger_name`  varchar(200) NOT NULL DEFAULT '' COMMENT 'logger名字' ,
`thread_name`  varchar(200) NOT NULL COMMENT '线程名字' ,
`level`  varchar(20) NOT NULL COMMENT '日志级别' ,
`log_time`  bigint(16) NOT NULL COMMENT '日志时间' ,
`data`  text NOT NULL COMMENT '日志数据' ,
`host`  varchar(50) NOT NULL COMMENT '客户端host' ,
`create_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间' ,
`update_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据修改时间' ,
`status`  bit(1) NOT NULL DEFAULT b'1' COMMENT '状态值' ,
PRIMARY KEY (`id`),
INDEX `index_log_time` (`logger_name`) USING BTREE
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8
;
