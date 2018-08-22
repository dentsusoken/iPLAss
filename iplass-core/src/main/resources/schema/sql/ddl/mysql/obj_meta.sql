DROP TABLE IF EXISTS `obj_meta`;
CREATE TABLE `obj_meta` (
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `obj_def_ver` BIGINT(10) NOT NULL,
  `obj_def_path` VARBINARY(1024) NOT NULL,
  `obj_def_disp_name` VARCHAR(1024) NULL,
  `obj_desc` VARCHAR(1024) NULL,
  `obj_meta_data` LONGBLOB NOT NULL,
  `status` VARCHAR(1) NULL,
  `sharable` VARCHAR(1) NULL,
  `overwritable` VARCHAR(1) NULL,
  `cre_user` VARCHAR(64) NULL,
  `cre_date` DATETIME NOT NULL,
  `up_user` VARCHAR(64) NULL,
  `up_date` DATETIME NOT NULL,
  PRIMARY KEY (`tenant_id`, `obj_def_id`, `obj_def_ver`),
  INDEX `obj_meta_index1` (`tenant_id`, `obj_def_path`, `status`)
)
ENGINE = INNODB ROW_FORMAT=COMPRESSED
;
