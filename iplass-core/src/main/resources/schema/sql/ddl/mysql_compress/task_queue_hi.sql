DROP TABLE IF EXISTS `task_queue_hi`;
CREATE TABLE `task_queue_hi`
(
    `tenant_id` INT(7) NOT NULL,
    `q_id` INT(7) NOT NULL,
    `task_id` BIGINT(16) NOT NULL,
    `v_time` BIGINT(16) NOT NULL,
    `status` CHAR(1) NOT NULL,
    `g_key` VARCHAR(128),
    `vw_id` INT(7),
    `exp_mode` CHAR(1) NOT NULL,
    `res_flg` CHAR(1) NOT NULL,
    `ver` BIGINT(16) NOT NULL,
    `up_date` DATETIME NOT NULL,
    `server_id` VARCHAR(128),
    `re_cnt` INT(7),
    `callable` LONGBLOB,
    `res` LONGBLOB,
    PRIMARY KEY (`q_id`, `tenant_id`, `task_id`)
)
ENGINE=InnoDB COMPRESSION="zlib"

PARTITION BY RANGE (`tenant_id`)
(
    PARTITION task_queue_hi_0 VALUES LESS THAN (1)
)
;
