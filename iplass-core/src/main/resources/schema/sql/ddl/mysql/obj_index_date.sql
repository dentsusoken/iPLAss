DROP TABLE IF EXISTS `OBJ_INDEX_DATE`;
CREATE TABLE `OBJ_INDEX_DATE` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_date_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_date_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_index_date_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_index_date_0_0,
            SUBPARTITION obj_index_date_0_1,
            SUBPARTITION obj_index_date_0_2,
            SUBPARTITION obj_index_date_0_3,
            SUBPARTITION obj_index_date_0_4,
            SUBPARTITION obj_index_date_0_5,
            SUBPARTITION obj_index_date_0_6,
            SUBPARTITION obj_index_date_0_7
        )
    )
;
