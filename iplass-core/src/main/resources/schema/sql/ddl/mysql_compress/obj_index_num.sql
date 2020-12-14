DROP TABLE IF EXISTS `obj_index_num`;
CREATE TABLE `obj_index_num` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_num_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_num_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
ENGINE=InnoDB COMPRESSION="lz4"

PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_index_num_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_index_num_0_0,
            SUBPARTITION obj_index_num_0_1,
            SUBPARTITION obj_index_num_0_2,
            SUBPARTITION obj_index_num_0_3,
            SUBPARTITION obj_index_num_0_4,
            SUBPARTITION obj_index_num_0_5,
            SUBPARTITION obj_index_num_0_6,
            SUBPARTITION obj_index_num_0_7
        )
    )
;
