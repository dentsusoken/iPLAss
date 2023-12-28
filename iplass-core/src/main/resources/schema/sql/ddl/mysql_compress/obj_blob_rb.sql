DROP TABLE IF EXISTS `OBJ_BLOB_RB`;
CREATE TABLE `OBJ_BLOB_RB` (
  `tenant_id` INT(7) NOT NULL,
  `rb_id` BIGINT(16) NOT NULL,
  `lob_id` BIGINT(16) NOT NULL,
  PRIMARY KEY (`tenant_id`, `lob_id`),
  INDEX `obj_blob_rb_index1` (`tenant_id`, `rb_id`)
)
ENGINE=InnoDB COMPRESSION="zlib"

PARTITION BY RANGE (`tenant_id`)
(
    PARTITION obj_blob_rb_0 VALUES LESS THAN (1)
)
;
