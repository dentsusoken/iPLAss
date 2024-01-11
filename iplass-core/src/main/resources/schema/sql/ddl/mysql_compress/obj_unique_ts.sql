DROP TABLE IF EXISTS `OBJ_UNIQUE_TS`;
CREATE TABLE `OBJ_UNIQUE_TS` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
ENGINE=InnoDB COMPRESSION="lz4"

PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_ts_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_ts_0_0,
            SUBPARTITION obj_unique_ts_0_1,
            SUBPARTITION obj_unique_ts_0_2,
            SUBPARTITION obj_unique_ts_0_3,
            SUBPARTITION obj_unique_ts_0_4,
            SUBPARTITION obj_unique_ts_0_5,
            SUBPARTITION obj_unique_ts_0_6,
            SUBPARTITION obj_unique_ts_0_7
        )
    )
;
