DROP TABLE IF EXISTS `t_account`;
CREATE TABLE `t_account` (
  `tenant_id` INT(7) NOT NULL,
  `account_id` VARCHAR(128) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `salt` VARCHAR(64) NULL,
  `oid` VARCHAR(128) NOT NULL,
  `last_login_on` DATETIME NULL,
  `login_err_cnt` INT(2) NOT NULL DEFAULT 0,
  `login_err_date` DATETIME NULL,
  `pol_name` VARCHAR(128) NULL,
  `cre_user` VARCHAR(64) NULL,
  `cre_date` DATETIME NULL,
  `up_user` VARCHAR(64) NULL,
  `up_date` DATETIME NULL,
  `last_password_change` DATETIME NULL,
  PRIMARY KEY (`tenant_id`, `account_id`),
  UNIQUE INDEX `t_account_uq` (`tenant_id`, `oid`)
)
ENGINE=InnoDB COMPRESSION="zlib"
;
