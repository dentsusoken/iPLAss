/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref`;
CREATE TABLE `obj_ref` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64)),
  INDEX `obj_ref_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64))
)
ENGINE = INNODB ROW_FORMAT=DYNAMIC

PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_0_0,
            SUBPARTITION obj_ref_0_1,
            SUBPARTITION obj_ref_0_2,
            SUBPARTITION obj_ref_0_3,
            SUBPARTITION obj_ref_0_4,
            SUBPARTITION obj_ref_0_5,
            SUBPARTITION obj_ref_0_6,
            SUBPARTITION obj_ref_0_7
        )
    );


/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__MTP`;
CREATE TABLE `obj_ref__MTP` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__MTP_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64)),
  INDEX `obj_ref__MTP_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64))
)
ENGINE = INNODB ROW_FORMAT=DYNAMIC

PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__MTP_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__MTP_0_0,
            SUBPARTITION obj_ref__MTP_0_1,
            SUBPARTITION obj_ref__MTP_0_2,
            SUBPARTITION obj_ref__MTP_0_3,
            SUBPARTITION obj_ref__MTP_0_4,
            SUBPARTITION obj_ref__MTP_0_5,
            SUBPARTITION obj_ref__MTP_0_6,
            SUBPARTITION obj_ref__MTP_0_7
        )
    );


/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__USER`;
CREATE TABLE `obj_ref__USER` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__USER_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64)),
  INDEX `obj_ref__USER_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64))
)
ENGINE = INNODB ROW_FORMAT=DYNAMIC

PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__USER_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__USER_0_0,
            SUBPARTITION obj_ref__USER_0_1,
            SUBPARTITION obj_ref__USER_0_2,
            SUBPARTITION obj_ref__USER_0_3,
            SUBPARTITION obj_ref__USER_0_4,
            SUBPARTITION obj_ref__USER_0_5,
            SUBPARTITION obj_ref__USER_0_6,
            SUBPARTITION obj_ref__USER_0_7
        )
    );


