DROP TABLE IF EXISTS `obj_blob_rb`;
CREATE TABLE `obj_blob_rb` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `rb_id` BIGINT(16) NULL,
  `lob_id` BIGINT(16) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`),
  INDEX `obj_blob_rb_index1` (`tenant_id`, `rb_id`)
)
ENGINE=InnoDB COMPRESSION="zlib"

PARTITION BY RANGE (`tenant_id`)
(
    PARTITION obj_blob_rb_0 VALUES LESS THAN (1)
)
;
