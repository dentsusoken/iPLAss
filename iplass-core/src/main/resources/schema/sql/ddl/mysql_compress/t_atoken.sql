DROP TABLE IF EXISTS `T_ATOKEN`;
CREATE TABLE `T_ATOKEN` (
  `tenant_id` INT(7) NOT NULL,
  `t_type` VARCHAR(32) NOT NULL,
  `u_key` VARCHAR(128) NOT NULL,
  `series` VARCHAR(256) NOT NULL,
  `token` VARCHAR(256) NULL,
  `pol_name` VARCHAR(128) NULL,
  `s_date` DATETIME NULL,
  `t_info` LONGBLOB NULL,
  PRIMARY KEY (`tenant_id`, `t_type`, `series`),
  INDEX `t_atoken_index1` (`tenant_id`, `t_type`, `u_key`),
  INDEX `t_atoken_index2` (`tenant_id`, `u_key`)
)
ENGINE=InnoDB COMPRESSION="none"
;
