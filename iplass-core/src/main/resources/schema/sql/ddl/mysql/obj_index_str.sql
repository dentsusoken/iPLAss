DROP TABLE IF EXISTS `OBJ_INDEX_STR`;
CREATE TABLE `OBJ_INDEX_STR` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_index_str_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_index_str_0_0,
            SUBPARTITION obj_index_str_0_1,
            SUBPARTITION obj_index_str_0_2,
            SUBPARTITION obj_index_str_0_3,
            SUBPARTITION obj_index_str_0_4,
            SUBPARTITION obj_index_str_0_5,
            SUBPARTITION obj_index_str_0_6,
            SUBPARTITION obj_index_str_0_7
        )
    )
;
