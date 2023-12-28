DROP TABLE IF EXISTS `CRAWL_LOG`;
CREATE TABLE `CRAWL_LOG` (
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `obj_def_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `cre_date` DATETIME NULL,
  `up_date` DATETIME NULL,
  PRIMARY KEY (`tenant_id`, `obj_def_id`, `obj_def_ver`)
)
;
