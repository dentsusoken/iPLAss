DROP TABLE IF EXISTS `lob_store`;
CREATE TABLE `lob_store` (
  `tenant_id` INT(7) NOT NULL,
  `lob_data_id` BIGINT(16) NOT NULL,
  `cre_date` DATETIME NULL,
  `ref_count` BIGINT(10) NULL,
  `b_data` LONGBLOB NULL,
  `lob_size` BIGINT(16) NULL,
  PRIMARY KEY `lob_store_pk` (`tenant_id`, `lob_data_id`)
)
ENGINE=InnoDB COMPRESSION="none"

PARTITION BY RANGE (`tenant_id`)
(
    PARTITION lob_store_0 VALUES LESS THAN (1)
)
;
