DROP TABLE IF EXISTS `obj_blob`;
CREATE TABLE `obj_blob` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `lob_id` BIGINT(16) NULL ,
  `lob_name` VARCHAR(256) NULL,
  `lob_type` VARCHAR(256) NULL,
  `lob_stat` CHAR(1) NULL,
  `lob_data_id` BIGINT(16) NULL,
  `cre_date` DATETIME NULL,
  `up_date` DATETIME NULL,
  `cre_user` VARCHAR(64) NULL,
  `up_user` VARCHAR(64) NULL,
  `sess_id` VARCHAR(128) NULL,
  `obj_def_id` VARCHAR(128) NULL,
  `prop_def_id` VARCHAR(128) NULL,
  `obj_id` VARCHAR(64) NULL,
  `obj_ver` BIGINT(10) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`),
  INDEX `obj_blob_index1` (`tenant_id`, `lob_id`)
)
ENGINE=InnoDB COMPRESSION="none"

PARTITION BY RANGE (`tenant_id`)
(
    PARTITION obj_blob_0 VALUES LESS THAN (1)
)
;
