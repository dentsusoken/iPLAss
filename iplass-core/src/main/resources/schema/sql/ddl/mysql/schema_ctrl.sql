DROP TABLE IF EXISTS `SCHEMA_CTRL`;
CREATE TABLE `SCHEMA_CTRL` (
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `obj_def_ver` BIGINT(10) NULL,
  `lock_status` CHAR(1) NULL,
  `cr_data_ver` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`tenant_id`, `obj_def_id`)
)

PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION schema_ctrl_0 VALUES LESS THAN (1) (
            SUBPARTITION schema_ctrl_0_0,
            SUBPARTITION schema_ctrl_0_1,
            SUBPARTITION schema_ctrl_0_2,
            SUBPARTITION schema_ctrl_0_3,
            SUBPARTITION schema_ctrl_0_4,
            SUBPARTITION schema_ctrl_0_5,
            SUBPARTITION schema_ctrl_0_6,
            SUBPARTITION schema_ctrl_0_7
        )
    )
;
