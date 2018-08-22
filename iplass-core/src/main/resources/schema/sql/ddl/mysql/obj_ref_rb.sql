/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb`;
CREATE TABLE `obj_ref_rb` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)
ENGINE = INNODB ROW_FORMAT=COMPRESSED

PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb_0_0,
            SUBPARTITION obj_ref_rb_0_1,
            SUBPARTITION obj_ref_rb_0_2,
            SUBPARTITION obj_ref_rb_0_3,
            SUBPARTITION obj_ref_rb_0_4,
            SUBPARTITION obj_ref_rb_0_5,
            SUBPARTITION obj_ref_rb_0_6,
            SUBPARTITION obj_ref_rb_0_7
        )
    );


/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__MTP`;
CREATE TABLE `obj_ref_rb__MTP` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__MTP_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)
ENGINE = INNODB ROW_FORMAT=COMPRESSED

PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__MTP_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__MTP_0_0,
            SUBPARTITION obj_ref_rb__MTP_0_1,
            SUBPARTITION obj_ref_rb__MTP_0_2,
            SUBPARTITION obj_ref_rb__MTP_0_3,
            SUBPARTITION obj_ref_rb__MTP_0_4,
            SUBPARTITION obj_ref_rb__MTP_0_5,
            SUBPARTITION obj_ref_rb__MTP_0_6,
            SUBPARTITION obj_ref_rb__MTP_0_7
        )
    );


/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__USER`;
CREATE TABLE `obj_ref_rb__USER` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__USER_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)
ENGINE = INNODB ROW_FORMAT=COMPRESSED

PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__USER_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__USER_0_0,
            SUBPARTITION obj_ref_rb__USER_0_1,
            SUBPARTITION obj_ref_rb__USER_0_2,
            SUBPARTITION obj_ref_rb__USER_0_3,
            SUBPARTITION obj_ref_rb__USER_0_4,
            SUBPARTITION obj_ref_rb__USER_0_5,
            SUBPARTITION obj_ref_rb__USER_0_6,
            SUBPARTITION obj_ref_rb__USER_0_7
        )
    );


