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

ENGINE=InnoDB COMPRESSION="zlib"


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
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__1`;
CREATE TABLE `obj_ref_rb__1` (
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
  INDEX `obj_ref_rb__1_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__1_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__1_0_0,
            SUBPARTITION obj_ref_rb__1_0_1,
            SUBPARTITION obj_ref_rb__1_0_2,
            SUBPARTITION obj_ref_rb__1_0_3,
            SUBPARTITION obj_ref_rb__1_0_4,
            SUBPARTITION obj_ref_rb__1_0_5,
            SUBPARTITION obj_ref_rb__1_0_6,
            SUBPARTITION obj_ref_rb__1_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__2`;
CREATE TABLE `obj_ref_rb__2` (
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
  INDEX `obj_ref_rb__2_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__2_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__2_0_0,
            SUBPARTITION obj_ref_rb__2_0_1,
            SUBPARTITION obj_ref_rb__2_0_2,
            SUBPARTITION obj_ref_rb__2_0_3,
            SUBPARTITION obj_ref_rb__2_0_4,
            SUBPARTITION obj_ref_rb__2_0_5,
            SUBPARTITION obj_ref_rb__2_0_6,
            SUBPARTITION obj_ref_rb__2_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__3`;
CREATE TABLE `obj_ref_rb__3` (
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
  INDEX `obj_ref_rb__3_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__3_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__3_0_0,
            SUBPARTITION obj_ref_rb__3_0_1,
            SUBPARTITION obj_ref_rb__3_0_2,
            SUBPARTITION obj_ref_rb__3_0_3,
            SUBPARTITION obj_ref_rb__3_0_4,
            SUBPARTITION obj_ref_rb__3_0_5,
            SUBPARTITION obj_ref_rb__3_0_6,
            SUBPARTITION obj_ref_rb__3_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__4`;
CREATE TABLE `obj_ref_rb__4` (
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
  INDEX `obj_ref_rb__4_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__4_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__4_0_0,
            SUBPARTITION obj_ref_rb__4_0_1,
            SUBPARTITION obj_ref_rb__4_0_2,
            SUBPARTITION obj_ref_rb__4_0_3,
            SUBPARTITION obj_ref_rb__4_0_4,
            SUBPARTITION obj_ref_rb__4_0_5,
            SUBPARTITION obj_ref_rb__4_0_6,
            SUBPARTITION obj_ref_rb__4_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__5`;
CREATE TABLE `obj_ref_rb__5` (
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
  INDEX `obj_ref_rb__5_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__5_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__5_0_0,
            SUBPARTITION obj_ref_rb__5_0_1,
            SUBPARTITION obj_ref_rb__5_0_2,
            SUBPARTITION obj_ref_rb__5_0_3,
            SUBPARTITION obj_ref_rb__5_0_4,
            SUBPARTITION obj_ref_rb__5_0_5,
            SUBPARTITION obj_ref_rb__5_0_6,
            SUBPARTITION obj_ref_rb__5_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__6`;
CREATE TABLE `obj_ref_rb__6` (
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
  INDEX `obj_ref_rb__6_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__6_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__6_0_0,
            SUBPARTITION obj_ref_rb__6_0_1,
            SUBPARTITION obj_ref_rb__6_0_2,
            SUBPARTITION obj_ref_rb__6_0_3,
            SUBPARTITION obj_ref_rb__6_0_4,
            SUBPARTITION obj_ref_rb__6_0_5,
            SUBPARTITION obj_ref_rb__6_0_6,
            SUBPARTITION obj_ref_rb__6_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__7`;
CREATE TABLE `obj_ref_rb__7` (
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
  INDEX `obj_ref_rb__7_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__7_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__7_0_0,
            SUBPARTITION obj_ref_rb__7_0_1,
            SUBPARTITION obj_ref_rb__7_0_2,
            SUBPARTITION obj_ref_rb__7_0_3,
            SUBPARTITION obj_ref_rb__7_0_4,
            SUBPARTITION obj_ref_rb__7_0_5,
            SUBPARTITION obj_ref_rb__7_0_6,
            SUBPARTITION obj_ref_rb__7_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__8`;
CREATE TABLE `obj_ref_rb__8` (
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
  INDEX `obj_ref_rb__8_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__8_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__8_0_0,
            SUBPARTITION obj_ref_rb__8_0_1,
            SUBPARTITION obj_ref_rb__8_0_2,
            SUBPARTITION obj_ref_rb__8_0_3,
            SUBPARTITION obj_ref_rb__8_0_4,
            SUBPARTITION obj_ref_rb__8_0_5,
            SUBPARTITION obj_ref_rb__8_0_6,
            SUBPARTITION obj_ref_rb__8_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__9`;
CREATE TABLE `obj_ref_rb__9` (
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
  INDEX `obj_ref_rb__9_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__9_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__9_0_0,
            SUBPARTITION obj_ref_rb__9_0_1,
            SUBPARTITION obj_ref_rb__9_0_2,
            SUBPARTITION obj_ref_rb__9_0_3,
            SUBPARTITION obj_ref_rb__9_0_4,
            SUBPARTITION obj_ref_rb__9_0_5,
            SUBPARTITION obj_ref_rb__9_0_6,
            SUBPARTITION obj_ref_rb__9_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__10`;
CREATE TABLE `obj_ref_rb__10` (
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
  INDEX `obj_ref_rb__10_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__10_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__10_0_0,
            SUBPARTITION obj_ref_rb__10_0_1,
            SUBPARTITION obj_ref_rb__10_0_2,
            SUBPARTITION obj_ref_rb__10_0_3,
            SUBPARTITION obj_ref_rb__10_0_4,
            SUBPARTITION obj_ref_rb__10_0_5,
            SUBPARTITION obj_ref_rb__10_0_6,
            SUBPARTITION obj_ref_rb__10_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__11`;
CREATE TABLE `obj_ref_rb__11` (
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
  INDEX `obj_ref_rb__11_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__11_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__11_0_0,
            SUBPARTITION obj_ref_rb__11_0_1,
            SUBPARTITION obj_ref_rb__11_0_2,
            SUBPARTITION obj_ref_rb__11_0_3,
            SUBPARTITION obj_ref_rb__11_0_4,
            SUBPARTITION obj_ref_rb__11_0_5,
            SUBPARTITION obj_ref_rb__11_0_6,
            SUBPARTITION obj_ref_rb__11_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__12`;
CREATE TABLE `obj_ref_rb__12` (
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
  INDEX `obj_ref_rb__12_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__12_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__12_0_0,
            SUBPARTITION obj_ref_rb__12_0_1,
            SUBPARTITION obj_ref_rb__12_0_2,
            SUBPARTITION obj_ref_rb__12_0_3,
            SUBPARTITION obj_ref_rb__12_0_4,
            SUBPARTITION obj_ref_rb__12_0_5,
            SUBPARTITION obj_ref_rb__12_0_6,
            SUBPARTITION obj_ref_rb__12_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__13`;
CREATE TABLE `obj_ref_rb__13` (
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
  INDEX `obj_ref_rb__13_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__13_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__13_0_0,
            SUBPARTITION obj_ref_rb__13_0_1,
            SUBPARTITION obj_ref_rb__13_0_2,
            SUBPARTITION obj_ref_rb__13_0_3,
            SUBPARTITION obj_ref_rb__13_0_4,
            SUBPARTITION obj_ref_rb__13_0_5,
            SUBPARTITION obj_ref_rb__13_0_6,
            SUBPARTITION obj_ref_rb__13_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__14`;
CREATE TABLE `obj_ref_rb__14` (
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
  INDEX `obj_ref_rb__14_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__14_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__14_0_0,
            SUBPARTITION obj_ref_rb__14_0_1,
            SUBPARTITION obj_ref_rb__14_0_2,
            SUBPARTITION obj_ref_rb__14_0_3,
            SUBPARTITION obj_ref_rb__14_0_4,
            SUBPARTITION obj_ref_rb__14_0_5,
            SUBPARTITION obj_ref_rb__14_0_6,
            SUBPARTITION obj_ref_rb__14_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__15`;
CREATE TABLE `obj_ref_rb__15` (
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
  INDEX `obj_ref_rb__15_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__15_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__15_0_0,
            SUBPARTITION obj_ref_rb__15_0_1,
            SUBPARTITION obj_ref_rb__15_0_2,
            SUBPARTITION obj_ref_rb__15_0_3,
            SUBPARTITION obj_ref_rb__15_0_4,
            SUBPARTITION obj_ref_rb__15_0_5,
            SUBPARTITION obj_ref_rb__15_0_6,
            SUBPARTITION obj_ref_rb__15_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__16`;
CREATE TABLE `obj_ref_rb__16` (
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
  INDEX `obj_ref_rb__16_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__16_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__16_0_0,
            SUBPARTITION obj_ref_rb__16_0_1,
            SUBPARTITION obj_ref_rb__16_0_2,
            SUBPARTITION obj_ref_rb__16_0_3,
            SUBPARTITION obj_ref_rb__16_0_4,
            SUBPARTITION obj_ref_rb__16_0_5,
            SUBPARTITION obj_ref_rb__16_0_6,
            SUBPARTITION obj_ref_rb__16_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__17`;
CREATE TABLE `obj_ref_rb__17` (
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
  INDEX `obj_ref_rb__17_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__17_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__17_0_0,
            SUBPARTITION obj_ref_rb__17_0_1,
            SUBPARTITION obj_ref_rb__17_0_2,
            SUBPARTITION obj_ref_rb__17_0_3,
            SUBPARTITION obj_ref_rb__17_0_4,
            SUBPARTITION obj_ref_rb__17_0_5,
            SUBPARTITION obj_ref_rb__17_0_6,
            SUBPARTITION obj_ref_rb__17_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__18`;
CREATE TABLE `obj_ref_rb__18` (
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
  INDEX `obj_ref_rb__18_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__18_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__18_0_0,
            SUBPARTITION obj_ref_rb__18_0_1,
            SUBPARTITION obj_ref_rb__18_0_2,
            SUBPARTITION obj_ref_rb__18_0_3,
            SUBPARTITION obj_ref_rb__18_0_4,
            SUBPARTITION obj_ref_rb__18_0_5,
            SUBPARTITION obj_ref_rb__18_0_6,
            SUBPARTITION obj_ref_rb__18_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__19`;
CREATE TABLE `obj_ref_rb__19` (
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
  INDEX `obj_ref_rb__19_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__19_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__19_0_0,
            SUBPARTITION obj_ref_rb__19_0_1,
            SUBPARTITION obj_ref_rb__19_0_2,
            SUBPARTITION obj_ref_rb__19_0_3,
            SUBPARTITION obj_ref_rb__19_0_4,
            SUBPARTITION obj_ref_rb__19_0_5,
            SUBPARTITION obj_ref_rb__19_0_6,
            SUBPARTITION obj_ref_rb__19_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__20`;
CREATE TABLE `obj_ref_rb__20` (
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
  INDEX `obj_ref_rb__20_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__20_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__20_0_0,
            SUBPARTITION obj_ref_rb__20_0_1,
            SUBPARTITION obj_ref_rb__20_0_2,
            SUBPARTITION obj_ref_rb__20_0_3,
            SUBPARTITION obj_ref_rb__20_0_4,
            SUBPARTITION obj_ref_rb__20_0_5,
            SUBPARTITION obj_ref_rb__20_0_6,
            SUBPARTITION obj_ref_rb__20_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__21`;
CREATE TABLE `obj_ref_rb__21` (
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
  INDEX `obj_ref_rb__21_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__21_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__21_0_0,
            SUBPARTITION obj_ref_rb__21_0_1,
            SUBPARTITION obj_ref_rb__21_0_2,
            SUBPARTITION obj_ref_rb__21_0_3,
            SUBPARTITION obj_ref_rb__21_0_4,
            SUBPARTITION obj_ref_rb__21_0_5,
            SUBPARTITION obj_ref_rb__21_0_6,
            SUBPARTITION obj_ref_rb__21_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__22`;
CREATE TABLE `obj_ref_rb__22` (
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
  INDEX `obj_ref_rb__22_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__22_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__22_0_0,
            SUBPARTITION obj_ref_rb__22_0_1,
            SUBPARTITION obj_ref_rb__22_0_2,
            SUBPARTITION obj_ref_rb__22_0_3,
            SUBPARTITION obj_ref_rb__22_0_4,
            SUBPARTITION obj_ref_rb__22_0_5,
            SUBPARTITION obj_ref_rb__22_0_6,
            SUBPARTITION obj_ref_rb__22_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__23`;
CREATE TABLE `obj_ref_rb__23` (
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
  INDEX `obj_ref_rb__23_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__23_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__23_0_0,
            SUBPARTITION obj_ref_rb__23_0_1,
            SUBPARTITION obj_ref_rb__23_0_2,
            SUBPARTITION obj_ref_rb__23_0_3,
            SUBPARTITION obj_ref_rb__23_0_4,
            SUBPARTITION obj_ref_rb__23_0_5,
            SUBPARTITION obj_ref_rb__23_0_6,
            SUBPARTITION obj_ref_rb__23_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__24`;
CREATE TABLE `obj_ref_rb__24` (
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
  INDEX `obj_ref_rb__24_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__24_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__24_0_0,
            SUBPARTITION obj_ref_rb__24_0_1,
            SUBPARTITION obj_ref_rb__24_0_2,
            SUBPARTITION obj_ref_rb__24_0_3,
            SUBPARTITION obj_ref_rb__24_0_4,
            SUBPARTITION obj_ref_rb__24_0_5,
            SUBPARTITION obj_ref_rb__24_0_6,
            SUBPARTITION obj_ref_rb__24_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__25`;
CREATE TABLE `obj_ref_rb__25` (
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
  INDEX `obj_ref_rb__25_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__25_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__25_0_0,
            SUBPARTITION obj_ref_rb__25_0_1,
            SUBPARTITION obj_ref_rb__25_0_2,
            SUBPARTITION obj_ref_rb__25_0_3,
            SUBPARTITION obj_ref_rb__25_0_4,
            SUBPARTITION obj_ref_rb__25_0_5,
            SUBPARTITION obj_ref_rb__25_0_6,
            SUBPARTITION obj_ref_rb__25_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__26`;
CREATE TABLE `obj_ref_rb__26` (
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
  INDEX `obj_ref_rb__26_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__26_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__26_0_0,
            SUBPARTITION obj_ref_rb__26_0_1,
            SUBPARTITION obj_ref_rb__26_0_2,
            SUBPARTITION obj_ref_rb__26_0_3,
            SUBPARTITION obj_ref_rb__26_0_4,
            SUBPARTITION obj_ref_rb__26_0_5,
            SUBPARTITION obj_ref_rb__26_0_6,
            SUBPARTITION obj_ref_rb__26_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__27`;
CREATE TABLE `obj_ref_rb__27` (
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
  INDEX `obj_ref_rb__27_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__27_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__27_0_0,
            SUBPARTITION obj_ref_rb__27_0_1,
            SUBPARTITION obj_ref_rb__27_0_2,
            SUBPARTITION obj_ref_rb__27_0_3,
            SUBPARTITION obj_ref_rb__27_0_4,
            SUBPARTITION obj_ref_rb__27_0_5,
            SUBPARTITION obj_ref_rb__27_0_6,
            SUBPARTITION obj_ref_rb__27_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__28`;
CREATE TABLE `obj_ref_rb__28` (
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
  INDEX `obj_ref_rb__28_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__28_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__28_0_0,
            SUBPARTITION obj_ref_rb__28_0_1,
            SUBPARTITION obj_ref_rb__28_0_2,
            SUBPARTITION obj_ref_rb__28_0_3,
            SUBPARTITION obj_ref_rb__28_0_4,
            SUBPARTITION obj_ref_rb__28_0_5,
            SUBPARTITION obj_ref_rb__28_0_6,
            SUBPARTITION obj_ref_rb__28_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__29`;
CREATE TABLE `obj_ref_rb__29` (
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
  INDEX `obj_ref_rb__29_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__29_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__29_0_0,
            SUBPARTITION obj_ref_rb__29_0_1,
            SUBPARTITION obj_ref_rb__29_0_2,
            SUBPARTITION obj_ref_rb__29_0_3,
            SUBPARTITION obj_ref_rb__29_0_4,
            SUBPARTITION obj_ref_rb__29_0_5,
            SUBPARTITION obj_ref_rb__29_0_6,
            SUBPARTITION obj_ref_rb__29_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__30`;
CREATE TABLE `obj_ref_rb__30` (
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
  INDEX `obj_ref_rb__30_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__30_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__30_0_0,
            SUBPARTITION obj_ref_rb__30_0_1,
            SUBPARTITION obj_ref_rb__30_0_2,
            SUBPARTITION obj_ref_rb__30_0_3,
            SUBPARTITION obj_ref_rb__30_0_4,
            SUBPARTITION obj_ref_rb__30_0_5,
            SUBPARTITION obj_ref_rb__30_0_6,
            SUBPARTITION obj_ref_rb__30_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__31`;
CREATE TABLE `obj_ref_rb__31` (
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
  INDEX `obj_ref_rb__31_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__31_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__31_0_0,
            SUBPARTITION obj_ref_rb__31_0_1,
            SUBPARTITION obj_ref_rb__31_0_2,
            SUBPARTITION obj_ref_rb__31_0_3,
            SUBPARTITION obj_ref_rb__31_0_4,
            SUBPARTITION obj_ref_rb__31_0_5,
            SUBPARTITION obj_ref_rb__31_0_6,
            SUBPARTITION obj_ref_rb__31_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__32`;
CREATE TABLE `obj_ref_rb__32` (
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
  INDEX `obj_ref_rb__32_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__32_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__32_0_0,
            SUBPARTITION obj_ref_rb__32_0_1,
            SUBPARTITION obj_ref_rb__32_0_2,
            SUBPARTITION obj_ref_rb__32_0_3,
            SUBPARTITION obj_ref_rb__32_0_4,
            SUBPARTITION obj_ref_rb__32_0_5,
            SUBPARTITION obj_ref_rb__32_0_6,
            SUBPARTITION obj_ref_rb__32_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__33`;
CREATE TABLE `obj_ref_rb__33` (
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
  INDEX `obj_ref_rb__33_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__33_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__33_0_0,
            SUBPARTITION obj_ref_rb__33_0_1,
            SUBPARTITION obj_ref_rb__33_0_2,
            SUBPARTITION obj_ref_rb__33_0_3,
            SUBPARTITION obj_ref_rb__33_0_4,
            SUBPARTITION obj_ref_rb__33_0_5,
            SUBPARTITION obj_ref_rb__33_0_6,
            SUBPARTITION obj_ref_rb__33_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__34`;
CREATE TABLE `obj_ref_rb__34` (
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
  INDEX `obj_ref_rb__34_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__34_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__34_0_0,
            SUBPARTITION obj_ref_rb__34_0_1,
            SUBPARTITION obj_ref_rb__34_0_2,
            SUBPARTITION obj_ref_rb__34_0_3,
            SUBPARTITION obj_ref_rb__34_0_4,
            SUBPARTITION obj_ref_rb__34_0_5,
            SUBPARTITION obj_ref_rb__34_0_6,
            SUBPARTITION obj_ref_rb__34_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__35`;
CREATE TABLE `obj_ref_rb__35` (
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
  INDEX `obj_ref_rb__35_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__35_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__35_0_0,
            SUBPARTITION obj_ref_rb__35_0_1,
            SUBPARTITION obj_ref_rb__35_0_2,
            SUBPARTITION obj_ref_rb__35_0_3,
            SUBPARTITION obj_ref_rb__35_0_4,
            SUBPARTITION obj_ref_rb__35_0_5,
            SUBPARTITION obj_ref_rb__35_0_6,
            SUBPARTITION obj_ref_rb__35_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__36`;
CREATE TABLE `obj_ref_rb__36` (
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
  INDEX `obj_ref_rb__36_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__36_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__36_0_0,
            SUBPARTITION obj_ref_rb__36_0_1,
            SUBPARTITION obj_ref_rb__36_0_2,
            SUBPARTITION obj_ref_rb__36_0_3,
            SUBPARTITION obj_ref_rb__36_0_4,
            SUBPARTITION obj_ref_rb__36_0_5,
            SUBPARTITION obj_ref_rb__36_0_6,
            SUBPARTITION obj_ref_rb__36_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__37`;
CREATE TABLE `obj_ref_rb__37` (
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
  INDEX `obj_ref_rb__37_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__37_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__37_0_0,
            SUBPARTITION obj_ref_rb__37_0_1,
            SUBPARTITION obj_ref_rb__37_0_2,
            SUBPARTITION obj_ref_rb__37_0_3,
            SUBPARTITION obj_ref_rb__37_0_4,
            SUBPARTITION obj_ref_rb__37_0_5,
            SUBPARTITION obj_ref_rb__37_0_6,
            SUBPARTITION obj_ref_rb__37_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__38`;
CREATE TABLE `obj_ref_rb__38` (
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
  INDEX `obj_ref_rb__38_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__38_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__38_0_0,
            SUBPARTITION obj_ref_rb__38_0_1,
            SUBPARTITION obj_ref_rb__38_0_2,
            SUBPARTITION obj_ref_rb__38_0_3,
            SUBPARTITION obj_ref_rb__38_0_4,
            SUBPARTITION obj_ref_rb__38_0_5,
            SUBPARTITION obj_ref_rb__38_0_6,
            SUBPARTITION obj_ref_rb__38_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__39`;
CREATE TABLE `obj_ref_rb__39` (
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
  INDEX `obj_ref_rb__39_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__39_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__39_0_0,
            SUBPARTITION obj_ref_rb__39_0_1,
            SUBPARTITION obj_ref_rb__39_0_2,
            SUBPARTITION obj_ref_rb__39_0_3,
            SUBPARTITION obj_ref_rb__39_0_4,
            SUBPARTITION obj_ref_rb__39_0_5,
            SUBPARTITION obj_ref_rb__39_0_6,
            SUBPARTITION obj_ref_rb__39_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__40`;
CREATE TABLE `obj_ref_rb__40` (
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
  INDEX `obj_ref_rb__40_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__40_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__40_0_0,
            SUBPARTITION obj_ref_rb__40_0_1,
            SUBPARTITION obj_ref_rb__40_0_2,
            SUBPARTITION obj_ref_rb__40_0_3,
            SUBPARTITION obj_ref_rb__40_0_4,
            SUBPARTITION obj_ref_rb__40_0_5,
            SUBPARTITION obj_ref_rb__40_0_6,
            SUBPARTITION obj_ref_rb__40_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__41`;
CREATE TABLE `obj_ref_rb__41` (
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
  INDEX `obj_ref_rb__41_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__41_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__41_0_0,
            SUBPARTITION obj_ref_rb__41_0_1,
            SUBPARTITION obj_ref_rb__41_0_2,
            SUBPARTITION obj_ref_rb__41_0_3,
            SUBPARTITION obj_ref_rb__41_0_4,
            SUBPARTITION obj_ref_rb__41_0_5,
            SUBPARTITION obj_ref_rb__41_0_6,
            SUBPARTITION obj_ref_rb__41_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__42`;
CREATE TABLE `obj_ref_rb__42` (
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
  INDEX `obj_ref_rb__42_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__42_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__42_0_0,
            SUBPARTITION obj_ref_rb__42_0_1,
            SUBPARTITION obj_ref_rb__42_0_2,
            SUBPARTITION obj_ref_rb__42_0_3,
            SUBPARTITION obj_ref_rb__42_0_4,
            SUBPARTITION obj_ref_rb__42_0_5,
            SUBPARTITION obj_ref_rb__42_0_6,
            SUBPARTITION obj_ref_rb__42_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__43`;
CREATE TABLE `obj_ref_rb__43` (
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
  INDEX `obj_ref_rb__43_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__43_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__43_0_0,
            SUBPARTITION obj_ref_rb__43_0_1,
            SUBPARTITION obj_ref_rb__43_0_2,
            SUBPARTITION obj_ref_rb__43_0_3,
            SUBPARTITION obj_ref_rb__43_0_4,
            SUBPARTITION obj_ref_rb__43_0_5,
            SUBPARTITION obj_ref_rb__43_0_6,
            SUBPARTITION obj_ref_rb__43_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__44`;
CREATE TABLE `obj_ref_rb__44` (
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
  INDEX `obj_ref_rb__44_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__44_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__44_0_0,
            SUBPARTITION obj_ref_rb__44_0_1,
            SUBPARTITION obj_ref_rb__44_0_2,
            SUBPARTITION obj_ref_rb__44_0_3,
            SUBPARTITION obj_ref_rb__44_0_4,
            SUBPARTITION obj_ref_rb__44_0_5,
            SUBPARTITION obj_ref_rb__44_0_6,
            SUBPARTITION obj_ref_rb__44_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__45`;
CREATE TABLE `obj_ref_rb__45` (
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
  INDEX `obj_ref_rb__45_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__45_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__45_0_0,
            SUBPARTITION obj_ref_rb__45_0_1,
            SUBPARTITION obj_ref_rb__45_0_2,
            SUBPARTITION obj_ref_rb__45_0_3,
            SUBPARTITION obj_ref_rb__45_0_4,
            SUBPARTITION obj_ref_rb__45_0_5,
            SUBPARTITION obj_ref_rb__45_0_6,
            SUBPARTITION obj_ref_rb__45_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__46`;
CREATE TABLE `obj_ref_rb__46` (
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
  INDEX `obj_ref_rb__46_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__46_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__46_0_0,
            SUBPARTITION obj_ref_rb__46_0_1,
            SUBPARTITION obj_ref_rb__46_0_2,
            SUBPARTITION obj_ref_rb__46_0_3,
            SUBPARTITION obj_ref_rb__46_0_4,
            SUBPARTITION obj_ref_rb__46_0_5,
            SUBPARTITION obj_ref_rb__46_0_6,
            SUBPARTITION obj_ref_rb__46_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__47`;
CREATE TABLE `obj_ref_rb__47` (
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
  INDEX `obj_ref_rb__47_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__47_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__47_0_0,
            SUBPARTITION obj_ref_rb__47_0_1,
            SUBPARTITION obj_ref_rb__47_0_2,
            SUBPARTITION obj_ref_rb__47_0_3,
            SUBPARTITION obj_ref_rb__47_0_4,
            SUBPARTITION obj_ref_rb__47_0_5,
            SUBPARTITION obj_ref_rb__47_0_6,
            SUBPARTITION obj_ref_rb__47_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__48`;
CREATE TABLE `obj_ref_rb__48` (
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
  INDEX `obj_ref_rb__48_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__48_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__48_0_0,
            SUBPARTITION obj_ref_rb__48_0_1,
            SUBPARTITION obj_ref_rb__48_0_2,
            SUBPARTITION obj_ref_rb__48_0_3,
            SUBPARTITION obj_ref_rb__48_0_4,
            SUBPARTITION obj_ref_rb__48_0_5,
            SUBPARTITION obj_ref_rb__48_0_6,
            SUBPARTITION obj_ref_rb__48_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__49`;
CREATE TABLE `obj_ref_rb__49` (
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
  INDEX `obj_ref_rb__49_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__49_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__49_0_0,
            SUBPARTITION obj_ref_rb__49_0_1,
            SUBPARTITION obj_ref_rb__49_0_2,
            SUBPARTITION obj_ref_rb__49_0_3,
            SUBPARTITION obj_ref_rb__49_0_4,
            SUBPARTITION obj_ref_rb__49_0_5,
            SUBPARTITION obj_ref_rb__49_0_6,
            SUBPARTITION obj_ref_rb__49_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__50`;
CREATE TABLE `obj_ref_rb__50` (
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
  INDEX `obj_ref_rb__50_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__50_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__50_0_0,
            SUBPARTITION obj_ref_rb__50_0_1,
            SUBPARTITION obj_ref_rb__50_0_2,
            SUBPARTITION obj_ref_rb__50_0_3,
            SUBPARTITION obj_ref_rb__50_0_4,
            SUBPARTITION obj_ref_rb__50_0_5,
            SUBPARTITION obj_ref_rb__50_0_6,
            SUBPARTITION obj_ref_rb__50_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__51`;
CREATE TABLE `obj_ref_rb__51` (
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
  INDEX `obj_ref_rb__51_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__51_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__51_0_0,
            SUBPARTITION obj_ref_rb__51_0_1,
            SUBPARTITION obj_ref_rb__51_0_2,
            SUBPARTITION obj_ref_rb__51_0_3,
            SUBPARTITION obj_ref_rb__51_0_4,
            SUBPARTITION obj_ref_rb__51_0_5,
            SUBPARTITION obj_ref_rb__51_0_6,
            SUBPARTITION obj_ref_rb__51_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__52`;
CREATE TABLE `obj_ref_rb__52` (
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
  INDEX `obj_ref_rb__52_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__52_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__52_0_0,
            SUBPARTITION obj_ref_rb__52_0_1,
            SUBPARTITION obj_ref_rb__52_0_2,
            SUBPARTITION obj_ref_rb__52_0_3,
            SUBPARTITION obj_ref_rb__52_0_4,
            SUBPARTITION obj_ref_rb__52_0_5,
            SUBPARTITION obj_ref_rb__52_0_6,
            SUBPARTITION obj_ref_rb__52_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__53`;
CREATE TABLE `obj_ref_rb__53` (
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
  INDEX `obj_ref_rb__53_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__53_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__53_0_0,
            SUBPARTITION obj_ref_rb__53_0_1,
            SUBPARTITION obj_ref_rb__53_0_2,
            SUBPARTITION obj_ref_rb__53_0_3,
            SUBPARTITION obj_ref_rb__53_0_4,
            SUBPARTITION obj_ref_rb__53_0_5,
            SUBPARTITION obj_ref_rb__53_0_6,
            SUBPARTITION obj_ref_rb__53_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__54`;
CREATE TABLE `obj_ref_rb__54` (
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
  INDEX `obj_ref_rb__54_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__54_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__54_0_0,
            SUBPARTITION obj_ref_rb__54_0_1,
            SUBPARTITION obj_ref_rb__54_0_2,
            SUBPARTITION obj_ref_rb__54_0_3,
            SUBPARTITION obj_ref_rb__54_0_4,
            SUBPARTITION obj_ref_rb__54_0_5,
            SUBPARTITION obj_ref_rb__54_0_6,
            SUBPARTITION obj_ref_rb__54_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__55`;
CREATE TABLE `obj_ref_rb__55` (
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
  INDEX `obj_ref_rb__55_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__55_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__55_0_0,
            SUBPARTITION obj_ref_rb__55_0_1,
            SUBPARTITION obj_ref_rb__55_0_2,
            SUBPARTITION obj_ref_rb__55_0_3,
            SUBPARTITION obj_ref_rb__55_0_4,
            SUBPARTITION obj_ref_rb__55_0_5,
            SUBPARTITION obj_ref_rb__55_0_6,
            SUBPARTITION obj_ref_rb__55_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__56`;
CREATE TABLE `obj_ref_rb__56` (
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
  INDEX `obj_ref_rb__56_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__56_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__56_0_0,
            SUBPARTITION obj_ref_rb__56_0_1,
            SUBPARTITION obj_ref_rb__56_0_2,
            SUBPARTITION obj_ref_rb__56_0_3,
            SUBPARTITION obj_ref_rb__56_0_4,
            SUBPARTITION obj_ref_rb__56_0_5,
            SUBPARTITION obj_ref_rb__56_0_6,
            SUBPARTITION obj_ref_rb__56_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__57`;
CREATE TABLE `obj_ref_rb__57` (
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
  INDEX `obj_ref_rb__57_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__57_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__57_0_0,
            SUBPARTITION obj_ref_rb__57_0_1,
            SUBPARTITION obj_ref_rb__57_0_2,
            SUBPARTITION obj_ref_rb__57_0_3,
            SUBPARTITION obj_ref_rb__57_0_4,
            SUBPARTITION obj_ref_rb__57_0_5,
            SUBPARTITION obj_ref_rb__57_0_6,
            SUBPARTITION obj_ref_rb__57_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__58`;
CREATE TABLE `obj_ref_rb__58` (
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
  INDEX `obj_ref_rb__58_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__58_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__58_0_0,
            SUBPARTITION obj_ref_rb__58_0_1,
            SUBPARTITION obj_ref_rb__58_0_2,
            SUBPARTITION obj_ref_rb__58_0_3,
            SUBPARTITION obj_ref_rb__58_0_4,
            SUBPARTITION obj_ref_rb__58_0_5,
            SUBPARTITION obj_ref_rb__58_0_6,
            SUBPARTITION obj_ref_rb__58_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__59`;
CREATE TABLE `obj_ref_rb__59` (
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
  INDEX `obj_ref_rb__59_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__59_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__59_0_0,
            SUBPARTITION obj_ref_rb__59_0_1,
            SUBPARTITION obj_ref_rb__59_0_2,
            SUBPARTITION obj_ref_rb__59_0_3,
            SUBPARTITION obj_ref_rb__59_0_4,
            SUBPARTITION obj_ref_rb__59_0_5,
            SUBPARTITION obj_ref_rb__59_0_6,
            SUBPARTITION obj_ref_rb__59_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__60`;
CREATE TABLE `obj_ref_rb__60` (
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
  INDEX `obj_ref_rb__60_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__60_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__60_0_0,
            SUBPARTITION obj_ref_rb__60_0_1,
            SUBPARTITION obj_ref_rb__60_0_2,
            SUBPARTITION obj_ref_rb__60_0_3,
            SUBPARTITION obj_ref_rb__60_0_4,
            SUBPARTITION obj_ref_rb__60_0_5,
            SUBPARTITION obj_ref_rb__60_0_6,
            SUBPARTITION obj_ref_rb__60_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__61`;
CREATE TABLE `obj_ref_rb__61` (
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
  INDEX `obj_ref_rb__61_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__61_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__61_0_0,
            SUBPARTITION obj_ref_rb__61_0_1,
            SUBPARTITION obj_ref_rb__61_0_2,
            SUBPARTITION obj_ref_rb__61_0_3,
            SUBPARTITION obj_ref_rb__61_0_4,
            SUBPARTITION obj_ref_rb__61_0_5,
            SUBPARTITION obj_ref_rb__61_0_6,
            SUBPARTITION obj_ref_rb__61_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__62`;
CREATE TABLE `obj_ref_rb__62` (
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
  INDEX `obj_ref_rb__62_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__62_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__62_0_0,
            SUBPARTITION obj_ref_rb__62_0_1,
            SUBPARTITION obj_ref_rb__62_0_2,
            SUBPARTITION obj_ref_rb__62_0_3,
            SUBPARTITION obj_ref_rb__62_0_4,
            SUBPARTITION obj_ref_rb__62_0_5,
            SUBPARTITION obj_ref_rb__62_0_6,
            SUBPARTITION obj_ref_rb__62_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__63`;
CREATE TABLE `obj_ref_rb__63` (
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
  INDEX `obj_ref_rb__63_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__63_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__63_0_0,
            SUBPARTITION obj_ref_rb__63_0_1,
            SUBPARTITION obj_ref_rb__63_0_2,
            SUBPARTITION obj_ref_rb__63_0_3,
            SUBPARTITION obj_ref_rb__63_0_4,
            SUBPARTITION obj_ref_rb__63_0_5,
            SUBPARTITION obj_ref_rb__63_0_6,
            SUBPARTITION obj_ref_rb__63_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__64`;
CREATE TABLE `obj_ref_rb__64` (
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
  INDEX `obj_ref_rb__64_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__64_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__64_0_0,
            SUBPARTITION obj_ref_rb__64_0_1,
            SUBPARTITION obj_ref_rb__64_0_2,
            SUBPARTITION obj_ref_rb__64_0_3,
            SUBPARTITION obj_ref_rb__64_0_4,
            SUBPARTITION obj_ref_rb__64_0_5,
            SUBPARTITION obj_ref_rb__64_0_6,
            SUBPARTITION obj_ref_rb__64_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__65`;
CREATE TABLE `obj_ref_rb__65` (
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
  INDEX `obj_ref_rb__65_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__65_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__65_0_0,
            SUBPARTITION obj_ref_rb__65_0_1,
            SUBPARTITION obj_ref_rb__65_0_2,
            SUBPARTITION obj_ref_rb__65_0_3,
            SUBPARTITION obj_ref_rb__65_0_4,
            SUBPARTITION obj_ref_rb__65_0_5,
            SUBPARTITION obj_ref_rb__65_0_6,
            SUBPARTITION obj_ref_rb__65_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__66`;
CREATE TABLE `obj_ref_rb__66` (
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
  INDEX `obj_ref_rb__66_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__66_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__66_0_0,
            SUBPARTITION obj_ref_rb__66_0_1,
            SUBPARTITION obj_ref_rb__66_0_2,
            SUBPARTITION obj_ref_rb__66_0_3,
            SUBPARTITION obj_ref_rb__66_0_4,
            SUBPARTITION obj_ref_rb__66_0_5,
            SUBPARTITION obj_ref_rb__66_0_6,
            SUBPARTITION obj_ref_rb__66_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__67`;
CREATE TABLE `obj_ref_rb__67` (
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
  INDEX `obj_ref_rb__67_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__67_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__67_0_0,
            SUBPARTITION obj_ref_rb__67_0_1,
            SUBPARTITION obj_ref_rb__67_0_2,
            SUBPARTITION obj_ref_rb__67_0_3,
            SUBPARTITION obj_ref_rb__67_0_4,
            SUBPARTITION obj_ref_rb__67_0_5,
            SUBPARTITION obj_ref_rb__67_0_6,
            SUBPARTITION obj_ref_rb__67_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__68`;
CREATE TABLE `obj_ref_rb__68` (
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
  INDEX `obj_ref_rb__68_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__68_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__68_0_0,
            SUBPARTITION obj_ref_rb__68_0_1,
            SUBPARTITION obj_ref_rb__68_0_2,
            SUBPARTITION obj_ref_rb__68_0_3,
            SUBPARTITION obj_ref_rb__68_0_4,
            SUBPARTITION obj_ref_rb__68_0_5,
            SUBPARTITION obj_ref_rb__68_0_6,
            SUBPARTITION obj_ref_rb__68_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__69`;
CREATE TABLE `obj_ref_rb__69` (
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
  INDEX `obj_ref_rb__69_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__69_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__69_0_0,
            SUBPARTITION obj_ref_rb__69_0_1,
            SUBPARTITION obj_ref_rb__69_0_2,
            SUBPARTITION obj_ref_rb__69_0_3,
            SUBPARTITION obj_ref_rb__69_0_4,
            SUBPARTITION obj_ref_rb__69_0_5,
            SUBPARTITION obj_ref_rb__69_0_6,
            SUBPARTITION obj_ref_rb__69_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__70`;
CREATE TABLE `obj_ref_rb__70` (
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
  INDEX `obj_ref_rb__70_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__70_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__70_0_0,
            SUBPARTITION obj_ref_rb__70_0_1,
            SUBPARTITION obj_ref_rb__70_0_2,
            SUBPARTITION obj_ref_rb__70_0_3,
            SUBPARTITION obj_ref_rb__70_0_4,
            SUBPARTITION obj_ref_rb__70_0_5,
            SUBPARTITION obj_ref_rb__70_0_6,
            SUBPARTITION obj_ref_rb__70_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__71`;
CREATE TABLE `obj_ref_rb__71` (
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
  INDEX `obj_ref_rb__71_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__71_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__71_0_0,
            SUBPARTITION obj_ref_rb__71_0_1,
            SUBPARTITION obj_ref_rb__71_0_2,
            SUBPARTITION obj_ref_rb__71_0_3,
            SUBPARTITION obj_ref_rb__71_0_4,
            SUBPARTITION obj_ref_rb__71_0_5,
            SUBPARTITION obj_ref_rb__71_0_6,
            SUBPARTITION obj_ref_rb__71_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__72`;
CREATE TABLE `obj_ref_rb__72` (
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
  INDEX `obj_ref_rb__72_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__72_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__72_0_0,
            SUBPARTITION obj_ref_rb__72_0_1,
            SUBPARTITION obj_ref_rb__72_0_2,
            SUBPARTITION obj_ref_rb__72_0_3,
            SUBPARTITION obj_ref_rb__72_0_4,
            SUBPARTITION obj_ref_rb__72_0_5,
            SUBPARTITION obj_ref_rb__72_0_6,
            SUBPARTITION obj_ref_rb__72_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__73`;
CREATE TABLE `obj_ref_rb__73` (
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
  INDEX `obj_ref_rb__73_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__73_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__73_0_0,
            SUBPARTITION obj_ref_rb__73_0_1,
            SUBPARTITION obj_ref_rb__73_0_2,
            SUBPARTITION obj_ref_rb__73_0_3,
            SUBPARTITION obj_ref_rb__73_0_4,
            SUBPARTITION obj_ref_rb__73_0_5,
            SUBPARTITION obj_ref_rb__73_0_6,
            SUBPARTITION obj_ref_rb__73_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__74`;
CREATE TABLE `obj_ref_rb__74` (
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
  INDEX `obj_ref_rb__74_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__74_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__74_0_0,
            SUBPARTITION obj_ref_rb__74_0_1,
            SUBPARTITION obj_ref_rb__74_0_2,
            SUBPARTITION obj_ref_rb__74_0_3,
            SUBPARTITION obj_ref_rb__74_0_4,
            SUBPARTITION obj_ref_rb__74_0_5,
            SUBPARTITION obj_ref_rb__74_0_6,
            SUBPARTITION obj_ref_rb__74_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__75`;
CREATE TABLE `obj_ref_rb__75` (
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
  INDEX `obj_ref_rb__75_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__75_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__75_0_0,
            SUBPARTITION obj_ref_rb__75_0_1,
            SUBPARTITION obj_ref_rb__75_0_2,
            SUBPARTITION obj_ref_rb__75_0_3,
            SUBPARTITION obj_ref_rb__75_0_4,
            SUBPARTITION obj_ref_rb__75_0_5,
            SUBPARTITION obj_ref_rb__75_0_6,
            SUBPARTITION obj_ref_rb__75_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__76`;
CREATE TABLE `obj_ref_rb__76` (
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
  INDEX `obj_ref_rb__76_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__76_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__76_0_0,
            SUBPARTITION obj_ref_rb__76_0_1,
            SUBPARTITION obj_ref_rb__76_0_2,
            SUBPARTITION obj_ref_rb__76_0_3,
            SUBPARTITION obj_ref_rb__76_0_4,
            SUBPARTITION obj_ref_rb__76_0_5,
            SUBPARTITION obj_ref_rb__76_0_6,
            SUBPARTITION obj_ref_rb__76_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__77`;
CREATE TABLE `obj_ref_rb__77` (
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
  INDEX `obj_ref_rb__77_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__77_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__77_0_0,
            SUBPARTITION obj_ref_rb__77_0_1,
            SUBPARTITION obj_ref_rb__77_0_2,
            SUBPARTITION obj_ref_rb__77_0_3,
            SUBPARTITION obj_ref_rb__77_0_4,
            SUBPARTITION obj_ref_rb__77_0_5,
            SUBPARTITION obj_ref_rb__77_0_6,
            SUBPARTITION obj_ref_rb__77_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__78`;
CREATE TABLE `obj_ref_rb__78` (
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
  INDEX `obj_ref_rb__78_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__78_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__78_0_0,
            SUBPARTITION obj_ref_rb__78_0_1,
            SUBPARTITION obj_ref_rb__78_0_2,
            SUBPARTITION obj_ref_rb__78_0_3,
            SUBPARTITION obj_ref_rb__78_0_4,
            SUBPARTITION obj_ref_rb__78_0_5,
            SUBPARTITION obj_ref_rb__78_0_6,
            SUBPARTITION obj_ref_rb__78_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__79`;
CREATE TABLE `obj_ref_rb__79` (
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
  INDEX `obj_ref_rb__79_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__79_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__79_0_0,
            SUBPARTITION obj_ref_rb__79_0_1,
            SUBPARTITION obj_ref_rb__79_0_2,
            SUBPARTITION obj_ref_rb__79_0_3,
            SUBPARTITION obj_ref_rb__79_0_4,
            SUBPARTITION obj_ref_rb__79_0_5,
            SUBPARTITION obj_ref_rb__79_0_6,
            SUBPARTITION obj_ref_rb__79_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__80`;
CREATE TABLE `obj_ref_rb__80` (
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
  INDEX `obj_ref_rb__80_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__80_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__80_0_0,
            SUBPARTITION obj_ref_rb__80_0_1,
            SUBPARTITION obj_ref_rb__80_0_2,
            SUBPARTITION obj_ref_rb__80_0_3,
            SUBPARTITION obj_ref_rb__80_0_4,
            SUBPARTITION obj_ref_rb__80_0_5,
            SUBPARTITION obj_ref_rb__80_0_6,
            SUBPARTITION obj_ref_rb__80_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__81`;
CREATE TABLE `obj_ref_rb__81` (
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
  INDEX `obj_ref_rb__81_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__81_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__81_0_0,
            SUBPARTITION obj_ref_rb__81_0_1,
            SUBPARTITION obj_ref_rb__81_0_2,
            SUBPARTITION obj_ref_rb__81_0_3,
            SUBPARTITION obj_ref_rb__81_0_4,
            SUBPARTITION obj_ref_rb__81_0_5,
            SUBPARTITION obj_ref_rb__81_0_6,
            SUBPARTITION obj_ref_rb__81_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__82`;
CREATE TABLE `obj_ref_rb__82` (
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
  INDEX `obj_ref_rb__82_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__82_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__82_0_0,
            SUBPARTITION obj_ref_rb__82_0_1,
            SUBPARTITION obj_ref_rb__82_0_2,
            SUBPARTITION obj_ref_rb__82_0_3,
            SUBPARTITION obj_ref_rb__82_0_4,
            SUBPARTITION obj_ref_rb__82_0_5,
            SUBPARTITION obj_ref_rb__82_0_6,
            SUBPARTITION obj_ref_rb__82_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__83`;
CREATE TABLE `obj_ref_rb__83` (
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
  INDEX `obj_ref_rb__83_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__83_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__83_0_0,
            SUBPARTITION obj_ref_rb__83_0_1,
            SUBPARTITION obj_ref_rb__83_0_2,
            SUBPARTITION obj_ref_rb__83_0_3,
            SUBPARTITION obj_ref_rb__83_0_4,
            SUBPARTITION obj_ref_rb__83_0_5,
            SUBPARTITION obj_ref_rb__83_0_6,
            SUBPARTITION obj_ref_rb__83_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__84`;
CREATE TABLE `obj_ref_rb__84` (
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
  INDEX `obj_ref_rb__84_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__84_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__84_0_0,
            SUBPARTITION obj_ref_rb__84_0_1,
            SUBPARTITION obj_ref_rb__84_0_2,
            SUBPARTITION obj_ref_rb__84_0_3,
            SUBPARTITION obj_ref_rb__84_0_4,
            SUBPARTITION obj_ref_rb__84_0_5,
            SUBPARTITION obj_ref_rb__84_0_6,
            SUBPARTITION obj_ref_rb__84_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__85`;
CREATE TABLE `obj_ref_rb__85` (
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
  INDEX `obj_ref_rb__85_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__85_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__85_0_0,
            SUBPARTITION obj_ref_rb__85_0_1,
            SUBPARTITION obj_ref_rb__85_0_2,
            SUBPARTITION obj_ref_rb__85_0_3,
            SUBPARTITION obj_ref_rb__85_0_4,
            SUBPARTITION obj_ref_rb__85_0_5,
            SUBPARTITION obj_ref_rb__85_0_6,
            SUBPARTITION obj_ref_rb__85_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__86`;
CREATE TABLE `obj_ref_rb__86` (
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
  INDEX `obj_ref_rb__86_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__86_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__86_0_0,
            SUBPARTITION obj_ref_rb__86_0_1,
            SUBPARTITION obj_ref_rb__86_0_2,
            SUBPARTITION obj_ref_rb__86_0_3,
            SUBPARTITION obj_ref_rb__86_0_4,
            SUBPARTITION obj_ref_rb__86_0_5,
            SUBPARTITION obj_ref_rb__86_0_6,
            SUBPARTITION obj_ref_rb__86_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__87`;
CREATE TABLE `obj_ref_rb__87` (
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
  INDEX `obj_ref_rb__87_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__87_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__87_0_0,
            SUBPARTITION obj_ref_rb__87_0_1,
            SUBPARTITION obj_ref_rb__87_0_2,
            SUBPARTITION obj_ref_rb__87_0_3,
            SUBPARTITION obj_ref_rb__87_0_4,
            SUBPARTITION obj_ref_rb__87_0_5,
            SUBPARTITION obj_ref_rb__87_0_6,
            SUBPARTITION obj_ref_rb__87_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__88`;
CREATE TABLE `obj_ref_rb__88` (
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
  INDEX `obj_ref_rb__88_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__88_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__88_0_0,
            SUBPARTITION obj_ref_rb__88_0_1,
            SUBPARTITION obj_ref_rb__88_0_2,
            SUBPARTITION obj_ref_rb__88_0_3,
            SUBPARTITION obj_ref_rb__88_0_4,
            SUBPARTITION obj_ref_rb__88_0_5,
            SUBPARTITION obj_ref_rb__88_0_6,
            SUBPARTITION obj_ref_rb__88_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__89`;
CREATE TABLE `obj_ref_rb__89` (
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
  INDEX `obj_ref_rb__89_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__89_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__89_0_0,
            SUBPARTITION obj_ref_rb__89_0_1,
            SUBPARTITION obj_ref_rb__89_0_2,
            SUBPARTITION obj_ref_rb__89_0_3,
            SUBPARTITION obj_ref_rb__89_0_4,
            SUBPARTITION obj_ref_rb__89_0_5,
            SUBPARTITION obj_ref_rb__89_0_6,
            SUBPARTITION obj_ref_rb__89_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__90`;
CREATE TABLE `obj_ref_rb__90` (
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
  INDEX `obj_ref_rb__90_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__90_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__90_0_0,
            SUBPARTITION obj_ref_rb__90_0_1,
            SUBPARTITION obj_ref_rb__90_0_2,
            SUBPARTITION obj_ref_rb__90_0_3,
            SUBPARTITION obj_ref_rb__90_0_4,
            SUBPARTITION obj_ref_rb__90_0_5,
            SUBPARTITION obj_ref_rb__90_0_6,
            SUBPARTITION obj_ref_rb__90_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__91`;
CREATE TABLE `obj_ref_rb__91` (
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
  INDEX `obj_ref_rb__91_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__91_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__91_0_0,
            SUBPARTITION obj_ref_rb__91_0_1,
            SUBPARTITION obj_ref_rb__91_0_2,
            SUBPARTITION obj_ref_rb__91_0_3,
            SUBPARTITION obj_ref_rb__91_0_4,
            SUBPARTITION obj_ref_rb__91_0_5,
            SUBPARTITION obj_ref_rb__91_0_6,
            SUBPARTITION obj_ref_rb__91_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__92`;
CREATE TABLE `obj_ref_rb__92` (
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
  INDEX `obj_ref_rb__92_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__92_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__92_0_0,
            SUBPARTITION obj_ref_rb__92_0_1,
            SUBPARTITION obj_ref_rb__92_0_2,
            SUBPARTITION obj_ref_rb__92_0_3,
            SUBPARTITION obj_ref_rb__92_0_4,
            SUBPARTITION obj_ref_rb__92_0_5,
            SUBPARTITION obj_ref_rb__92_0_6,
            SUBPARTITION obj_ref_rb__92_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__93`;
CREATE TABLE `obj_ref_rb__93` (
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
  INDEX `obj_ref_rb__93_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__93_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__93_0_0,
            SUBPARTITION obj_ref_rb__93_0_1,
            SUBPARTITION obj_ref_rb__93_0_2,
            SUBPARTITION obj_ref_rb__93_0_3,
            SUBPARTITION obj_ref_rb__93_0_4,
            SUBPARTITION obj_ref_rb__93_0_5,
            SUBPARTITION obj_ref_rb__93_0_6,
            SUBPARTITION obj_ref_rb__93_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__94`;
CREATE TABLE `obj_ref_rb__94` (
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
  INDEX `obj_ref_rb__94_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__94_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__94_0_0,
            SUBPARTITION obj_ref_rb__94_0_1,
            SUBPARTITION obj_ref_rb__94_0_2,
            SUBPARTITION obj_ref_rb__94_0_3,
            SUBPARTITION obj_ref_rb__94_0_4,
            SUBPARTITION obj_ref_rb__94_0_5,
            SUBPARTITION obj_ref_rb__94_0_6,
            SUBPARTITION obj_ref_rb__94_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__95`;
CREATE TABLE `obj_ref_rb__95` (
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
  INDEX `obj_ref_rb__95_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__95_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__95_0_0,
            SUBPARTITION obj_ref_rb__95_0_1,
            SUBPARTITION obj_ref_rb__95_0_2,
            SUBPARTITION obj_ref_rb__95_0_3,
            SUBPARTITION obj_ref_rb__95_0_4,
            SUBPARTITION obj_ref_rb__95_0_5,
            SUBPARTITION obj_ref_rb__95_0_6,
            SUBPARTITION obj_ref_rb__95_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__96`;
CREATE TABLE `obj_ref_rb__96` (
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
  INDEX `obj_ref_rb__96_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__96_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__96_0_0,
            SUBPARTITION obj_ref_rb__96_0_1,
            SUBPARTITION obj_ref_rb__96_0_2,
            SUBPARTITION obj_ref_rb__96_0_3,
            SUBPARTITION obj_ref_rb__96_0_4,
            SUBPARTITION obj_ref_rb__96_0_5,
            SUBPARTITION obj_ref_rb__96_0_6,
            SUBPARTITION obj_ref_rb__96_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__97`;
CREATE TABLE `obj_ref_rb__97` (
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
  INDEX `obj_ref_rb__97_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__97_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__97_0_0,
            SUBPARTITION obj_ref_rb__97_0_1,
            SUBPARTITION obj_ref_rb__97_0_2,
            SUBPARTITION obj_ref_rb__97_0_3,
            SUBPARTITION obj_ref_rb__97_0_4,
            SUBPARTITION obj_ref_rb__97_0_5,
            SUBPARTITION obj_ref_rb__97_0_6,
            SUBPARTITION obj_ref_rb__97_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__98`;
CREATE TABLE `obj_ref_rb__98` (
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
  INDEX `obj_ref_rb__98_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__98_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__98_0_0,
            SUBPARTITION obj_ref_rb__98_0_1,
            SUBPARTITION obj_ref_rb__98_0_2,
            SUBPARTITION obj_ref_rb__98_0_3,
            SUBPARTITION obj_ref_rb__98_0_4,
            SUBPARTITION obj_ref_rb__98_0_5,
            SUBPARTITION obj_ref_rb__98_0_6,
            SUBPARTITION obj_ref_rb__98_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__99`;
CREATE TABLE `obj_ref_rb__99` (
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
  INDEX `obj_ref_rb__99_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__99_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__99_0_0,
            SUBPARTITION obj_ref_rb__99_0_1,
            SUBPARTITION obj_ref_rb__99_0_2,
            SUBPARTITION obj_ref_rb__99_0_3,
            SUBPARTITION obj_ref_rb__99_0_4,
            SUBPARTITION obj_ref_rb__99_0_5,
            SUBPARTITION obj_ref_rb__99_0_6,
            SUBPARTITION obj_ref_rb__99_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__100`;
CREATE TABLE `obj_ref_rb__100` (
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
  INDEX `obj_ref_rb__100_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__100_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__100_0_0,
            SUBPARTITION obj_ref_rb__100_0_1,
            SUBPARTITION obj_ref_rb__100_0_2,
            SUBPARTITION obj_ref_rb__100_0_3,
            SUBPARTITION obj_ref_rb__100_0_4,
            SUBPARTITION obj_ref_rb__100_0_5,
            SUBPARTITION obj_ref_rb__100_0_6,
            SUBPARTITION obj_ref_rb__100_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__101`;
CREATE TABLE `obj_ref_rb__101` (
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
  INDEX `obj_ref_rb__101_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__101_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__101_0_0,
            SUBPARTITION obj_ref_rb__101_0_1,
            SUBPARTITION obj_ref_rb__101_0_2,
            SUBPARTITION obj_ref_rb__101_0_3,
            SUBPARTITION obj_ref_rb__101_0_4,
            SUBPARTITION obj_ref_rb__101_0_5,
            SUBPARTITION obj_ref_rb__101_0_6,
            SUBPARTITION obj_ref_rb__101_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__102`;
CREATE TABLE `obj_ref_rb__102` (
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
  INDEX `obj_ref_rb__102_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__102_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__102_0_0,
            SUBPARTITION obj_ref_rb__102_0_1,
            SUBPARTITION obj_ref_rb__102_0_2,
            SUBPARTITION obj_ref_rb__102_0_3,
            SUBPARTITION obj_ref_rb__102_0_4,
            SUBPARTITION obj_ref_rb__102_0_5,
            SUBPARTITION obj_ref_rb__102_0_6,
            SUBPARTITION obj_ref_rb__102_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__103`;
CREATE TABLE `obj_ref_rb__103` (
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
  INDEX `obj_ref_rb__103_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__103_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__103_0_0,
            SUBPARTITION obj_ref_rb__103_0_1,
            SUBPARTITION obj_ref_rb__103_0_2,
            SUBPARTITION obj_ref_rb__103_0_3,
            SUBPARTITION obj_ref_rb__103_0_4,
            SUBPARTITION obj_ref_rb__103_0_5,
            SUBPARTITION obj_ref_rb__103_0_6,
            SUBPARTITION obj_ref_rb__103_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__104`;
CREATE TABLE `obj_ref_rb__104` (
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
  INDEX `obj_ref_rb__104_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__104_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__104_0_0,
            SUBPARTITION obj_ref_rb__104_0_1,
            SUBPARTITION obj_ref_rb__104_0_2,
            SUBPARTITION obj_ref_rb__104_0_3,
            SUBPARTITION obj_ref_rb__104_0_4,
            SUBPARTITION obj_ref_rb__104_0_5,
            SUBPARTITION obj_ref_rb__104_0_6,
            SUBPARTITION obj_ref_rb__104_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__105`;
CREATE TABLE `obj_ref_rb__105` (
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
  INDEX `obj_ref_rb__105_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__105_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__105_0_0,
            SUBPARTITION obj_ref_rb__105_0_1,
            SUBPARTITION obj_ref_rb__105_0_2,
            SUBPARTITION obj_ref_rb__105_0_3,
            SUBPARTITION obj_ref_rb__105_0_4,
            SUBPARTITION obj_ref_rb__105_0_5,
            SUBPARTITION obj_ref_rb__105_0_6,
            SUBPARTITION obj_ref_rb__105_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__106`;
CREATE TABLE `obj_ref_rb__106` (
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
  INDEX `obj_ref_rb__106_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__106_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__106_0_0,
            SUBPARTITION obj_ref_rb__106_0_1,
            SUBPARTITION obj_ref_rb__106_0_2,
            SUBPARTITION obj_ref_rb__106_0_3,
            SUBPARTITION obj_ref_rb__106_0_4,
            SUBPARTITION obj_ref_rb__106_0_5,
            SUBPARTITION obj_ref_rb__106_0_6,
            SUBPARTITION obj_ref_rb__106_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__107`;
CREATE TABLE `obj_ref_rb__107` (
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
  INDEX `obj_ref_rb__107_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__107_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__107_0_0,
            SUBPARTITION obj_ref_rb__107_0_1,
            SUBPARTITION obj_ref_rb__107_0_2,
            SUBPARTITION obj_ref_rb__107_0_3,
            SUBPARTITION obj_ref_rb__107_0_4,
            SUBPARTITION obj_ref_rb__107_0_5,
            SUBPARTITION obj_ref_rb__107_0_6,
            SUBPARTITION obj_ref_rb__107_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__108`;
CREATE TABLE `obj_ref_rb__108` (
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
  INDEX `obj_ref_rb__108_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__108_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__108_0_0,
            SUBPARTITION obj_ref_rb__108_0_1,
            SUBPARTITION obj_ref_rb__108_0_2,
            SUBPARTITION obj_ref_rb__108_0_3,
            SUBPARTITION obj_ref_rb__108_0_4,
            SUBPARTITION obj_ref_rb__108_0_5,
            SUBPARTITION obj_ref_rb__108_0_6,
            SUBPARTITION obj_ref_rb__108_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__109`;
CREATE TABLE `obj_ref_rb__109` (
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
  INDEX `obj_ref_rb__109_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__109_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__109_0_0,
            SUBPARTITION obj_ref_rb__109_0_1,
            SUBPARTITION obj_ref_rb__109_0_2,
            SUBPARTITION obj_ref_rb__109_0_3,
            SUBPARTITION obj_ref_rb__109_0_4,
            SUBPARTITION obj_ref_rb__109_0_5,
            SUBPARTITION obj_ref_rb__109_0_6,
            SUBPARTITION obj_ref_rb__109_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__110`;
CREATE TABLE `obj_ref_rb__110` (
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
  INDEX `obj_ref_rb__110_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__110_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__110_0_0,
            SUBPARTITION obj_ref_rb__110_0_1,
            SUBPARTITION obj_ref_rb__110_0_2,
            SUBPARTITION obj_ref_rb__110_0_3,
            SUBPARTITION obj_ref_rb__110_0_4,
            SUBPARTITION obj_ref_rb__110_0_5,
            SUBPARTITION obj_ref_rb__110_0_6,
            SUBPARTITION obj_ref_rb__110_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__111`;
CREATE TABLE `obj_ref_rb__111` (
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
  INDEX `obj_ref_rb__111_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__111_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__111_0_0,
            SUBPARTITION obj_ref_rb__111_0_1,
            SUBPARTITION obj_ref_rb__111_0_2,
            SUBPARTITION obj_ref_rb__111_0_3,
            SUBPARTITION obj_ref_rb__111_0_4,
            SUBPARTITION obj_ref_rb__111_0_5,
            SUBPARTITION obj_ref_rb__111_0_6,
            SUBPARTITION obj_ref_rb__111_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__112`;
CREATE TABLE `obj_ref_rb__112` (
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
  INDEX `obj_ref_rb__112_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__112_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__112_0_0,
            SUBPARTITION obj_ref_rb__112_0_1,
            SUBPARTITION obj_ref_rb__112_0_2,
            SUBPARTITION obj_ref_rb__112_0_3,
            SUBPARTITION obj_ref_rb__112_0_4,
            SUBPARTITION obj_ref_rb__112_0_5,
            SUBPARTITION obj_ref_rb__112_0_6,
            SUBPARTITION obj_ref_rb__112_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__113`;
CREATE TABLE `obj_ref_rb__113` (
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
  INDEX `obj_ref_rb__113_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__113_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__113_0_0,
            SUBPARTITION obj_ref_rb__113_0_1,
            SUBPARTITION obj_ref_rb__113_0_2,
            SUBPARTITION obj_ref_rb__113_0_3,
            SUBPARTITION obj_ref_rb__113_0_4,
            SUBPARTITION obj_ref_rb__113_0_5,
            SUBPARTITION obj_ref_rb__113_0_6,
            SUBPARTITION obj_ref_rb__113_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__114`;
CREATE TABLE `obj_ref_rb__114` (
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
  INDEX `obj_ref_rb__114_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__114_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__114_0_0,
            SUBPARTITION obj_ref_rb__114_0_1,
            SUBPARTITION obj_ref_rb__114_0_2,
            SUBPARTITION obj_ref_rb__114_0_3,
            SUBPARTITION obj_ref_rb__114_0_4,
            SUBPARTITION obj_ref_rb__114_0_5,
            SUBPARTITION obj_ref_rb__114_0_6,
            SUBPARTITION obj_ref_rb__114_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__115`;
CREATE TABLE `obj_ref_rb__115` (
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
  INDEX `obj_ref_rb__115_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__115_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__115_0_0,
            SUBPARTITION obj_ref_rb__115_0_1,
            SUBPARTITION obj_ref_rb__115_0_2,
            SUBPARTITION obj_ref_rb__115_0_3,
            SUBPARTITION obj_ref_rb__115_0_4,
            SUBPARTITION obj_ref_rb__115_0_5,
            SUBPARTITION obj_ref_rb__115_0_6,
            SUBPARTITION obj_ref_rb__115_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__116`;
CREATE TABLE `obj_ref_rb__116` (
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
  INDEX `obj_ref_rb__116_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__116_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__116_0_0,
            SUBPARTITION obj_ref_rb__116_0_1,
            SUBPARTITION obj_ref_rb__116_0_2,
            SUBPARTITION obj_ref_rb__116_0_3,
            SUBPARTITION obj_ref_rb__116_0_4,
            SUBPARTITION obj_ref_rb__116_0_5,
            SUBPARTITION obj_ref_rb__116_0_6,
            SUBPARTITION obj_ref_rb__116_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__117`;
CREATE TABLE `obj_ref_rb__117` (
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
  INDEX `obj_ref_rb__117_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__117_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__117_0_0,
            SUBPARTITION obj_ref_rb__117_0_1,
            SUBPARTITION obj_ref_rb__117_0_2,
            SUBPARTITION obj_ref_rb__117_0_3,
            SUBPARTITION obj_ref_rb__117_0_4,
            SUBPARTITION obj_ref_rb__117_0_5,
            SUBPARTITION obj_ref_rb__117_0_6,
            SUBPARTITION obj_ref_rb__117_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__118`;
CREATE TABLE `obj_ref_rb__118` (
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
  INDEX `obj_ref_rb__118_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__118_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__118_0_0,
            SUBPARTITION obj_ref_rb__118_0_1,
            SUBPARTITION obj_ref_rb__118_0_2,
            SUBPARTITION obj_ref_rb__118_0_3,
            SUBPARTITION obj_ref_rb__118_0_4,
            SUBPARTITION obj_ref_rb__118_0_5,
            SUBPARTITION obj_ref_rb__118_0_6,
            SUBPARTITION obj_ref_rb__118_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__119`;
CREATE TABLE `obj_ref_rb__119` (
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
  INDEX `obj_ref_rb__119_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__119_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__119_0_0,
            SUBPARTITION obj_ref_rb__119_0_1,
            SUBPARTITION obj_ref_rb__119_0_2,
            SUBPARTITION obj_ref_rb__119_0_3,
            SUBPARTITION obj_ref_rb__119_0_4,
            SUBPARTITION obj_ref_rb__119_0_5,
            SUBPARTITION obj_ref_rb__119_0_6,
            SUBPARTITION obj_ref_rb__119_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__120`;
CREATE TABLE `obj_ref_rb__120` (
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
  INDEX `obj_ref_rb__120_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__120_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__120_0_0,
            SUBPARTITION obj_ref_rb__120_0_1,
            SUBPARTITION obj_ref_rb__120_0_2,
            SUBPARTITION obj_ref_rb__120_0_3,
            SUBPARTITION obj_ref_rb__120_0_4,
            SUBPARTITION obj_ref_rb__120_0_5,
            SUBPARTITION obj_ref_rb__120_0_6,
            SUBPARTITION obj_ref_rb__120_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__121`;
CREATE TABLE `obj_ref_rb__121` (
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
  INDEX `obj_ref_rb__121_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__121_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__121_0_0,
            SUBPARTITION obj_ref_rb__121_0_1,
            SUBPARTITION obj_ref_rb__121_0_2,
            SUBPARTITION obj_ref_rb__121_0_3,
            SUBPARTITION obj_ref_rb__121_0_4,
            SUBPARTITION obj_ref_rb__121_0_5,
            SUBPARTITION obj_ref_rb__121_0_6,
            SUBPARTITION obj_ref_rb__121_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__122`;
CREATE TABLE `obj_ref_rb__122` (
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
  INDEX `obj_ref_rb__122_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__122_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__122_0_0,
            SUBPARTITION obj_ref_rb__122_0_1,
            SUBPARTITION obj_ref_rb__122_0_2,
            SUBPARTITION obj_ref_rb__122_0_3,
            SUBPARTITION obj_ref_rb__122_0_4,
            SUBPARTITION obj_ref_rb__122_0_5,
            SUBPARTITION obj_ref_rb__122_0_6,
            SUBPARTITION obj_ref_rb__122_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__123`;
CREATE TABLE `obj_ref_rb__123` (
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
  INDEX `obj_ref_rb__123_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__123_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__123_0_0,
            SUBPARTITION obj_ref_rb__123_0_1,
            SUBPARTITION obj_ref_rb__123_0_2,
            SUBPARTITION obj_ref_rb__123_0_3,
            SUBPARTITION obj_ref_rb__123_0_4,
            SUBPARTITION obj_ref_rb__123_0_5,
            SUBPARTITION obj_ref_rb__123_0_6,
            SUBPARTITION obj_ref_rb__123_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__124`;
CREATE TABLE `obj_ref_rb__124` (
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
  INDEX `obj_ref_rb__124_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__124_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__124_0_0,
            SUBPARTITION obj_ref_rb__124_0_1,
            SUBPARTITION obj_ref_rb__124_0_2,
            SUBPARTITION obj_ref_rb__124_0_3,
            SUBPARTITION obj_ref_rb__124_0_4,
            SUBPARTITION obj_ref_rb__124_0_5,
            SUBPARTITION obj_ref_rb__124_0_6,
            SUBPARTITION obj_ref_rb__124_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__125`;
CREATE TABLE `obj_ref_rb__125` (
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
  INDEX `obj_ref_rb__125_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__125_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__125_0_0,
            SUBPARTITION obj_ref_rb__125_0_1,
            SUBPARTITION obj_ref_rb__125_0_2,
            SUBPARTITION obj_ref_rb__125_0_3,
            SUBPARTITION obj_ref_rb__125_0_4,
            SUBPARTITION obj_ref_rb__125_0_5,
            SUBPARTITION obj_ref_rb__125_0_6,
            SUBPARTITION obj_ref_rb__125_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__126`;
CREATE TABLE `obj_ref_rb__126` (
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
  INDEX `obj_ref_rb__126_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__126_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__126_0_0,
            SUBPARTITION obj_ref_rb__126_0_1,
            SUBPARTITION obj_ref_rb__126_0_2,
            SUBPARTITION obj_ref_rb__126_0_3,
            SUBPARTITION obj_ref_rb__126_0_4,
            SUBPARTITION obj_ref_rb__126_0_5,
            SUBPARTITION obj_ref_rb__126_0_6,
            SUBPARTITION obj_ref_rb__126_0_7
        )
    )

;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__127`;
CREATE TABLE `obj_ref_rb__127` (
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
  INDEX `obj_ref_rb__127_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)

ENGINE=InnoDB COMPRESSION="zlib"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref_rb__127_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref_rb__127_0_0,
            SUBPARTITION obj_ref_rb__127_0_1,
            SUBPARTITION obj_ref_rb__127_0_2,
            SUBPARTITION obj_ref_rb__127_0_3,
            SUBPARTITION obj_ref_rb__127_0_4,
            SUBPARTITION obj_ref_rb__127_0_5,
            SUBPARTITION obj_ref_rb__127_0_6,
            SUBPARTITION obj_ref_rb__127_0_7
        )
    )

;
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

ENGINE=InnoDB COMPRESSION="zlib"


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
    )

;
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

ENGINE=InnoDB COMPRESSION="zlib"


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
    )

;
