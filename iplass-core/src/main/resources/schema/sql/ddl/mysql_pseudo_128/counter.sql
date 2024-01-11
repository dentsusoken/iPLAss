DROP TABLE IF EXISTS `COUNTER`;
CREATE TABLE `COUNTER` (
  `tenant_id` INT(7) NOT NULL,
  `cnt_name` VARCHAR(128) NOT NULL,
  `inc_unit_key` VARCHAR(128) NOT NULL,
  `cnt_val` BIGINT(18) NULL,
  PRIMARY KEY (`tenant_id`, `cnt_name`(128), `inc_unit_key`(128))
)

PARTITION BY RANGE (`tenant_id`)
(
    PARTITION counter_0 VALUES LESS THAN (1)
)
;
