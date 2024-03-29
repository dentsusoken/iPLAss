DROP TABLE IF EXISTS `T_PASS_HI`;
CREATE TABLE `T_PASS_HI` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `account_id` VARCHAR(128) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `salt` VARCHAR(64) NULL,
  `up_date` DATETIME NULL,
  PRIMARY KEY (`r_id`),
  INDEX `t_pass_hi_index` (`tenant_id`, `account_id`)
)
ENGINE=InnoDB COMPRESSION="zlib"
;
