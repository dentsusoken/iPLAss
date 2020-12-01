DROP TABLE IF EXISTS `t_tenant`;
CREATE TABLE `t_tenant` (
  `id` INT(7) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(256) NOT NULL,
  `description` VARCHAR(4000) NULL,
  `host_name` VARCHAR(256) NULL,
  `url` VARCHAR(256) NOT NULL,
  `yuko_date_from` DATETIME NOT NULL,
  `yuko_date_to` DATETIME NOT NULL,
  `cre_user` VARCHAR(64) NULL,
  `cre_date` DATETIME NULL,
  `up_user` VARCHAR(64) NULL,
  `up_date` DATETIME NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`url`),
  INDEX `t_tenant_index1` (`id`, `host_name`(256), `url`(256), `yuko_date_from`, `yuko_date_to`),
  INDEX `t_tenant_index2` (`id`, `up_date`)
)
ENGINE=InnoDB COMPRESSION="zlib"
;
