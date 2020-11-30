DROP TABLE IF EXISTS `delete_log`;
CREATE TABLE `delete_log` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `status` VARCHAR(1) NOT NULL,
  `cre_date` DATETIME NULL,
  `up_date` DATETIME NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`)
)
ENGINE=InnoDB COMPRESSION="zlib"

PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION delete_log_0 VALUES LESS THAN (1) (
            SUBPARTITION delete_log_0_0,
            SUBPARTITION delete_log_0_1,
            SUBPARTITION delete_log_0_2,
            SUBPARTITION delete_log_0_3,
            SUBPARTITION delete_log_0_4,
            SUBPARTITION delete_log_0_5,
            SUBPARTITION delete_log_0_6,
            SUBPARTITION delete_log_0_7
        )
    )
;
