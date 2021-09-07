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
  INDEX `obj_ref_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


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
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__1`;
CREATE TABLE `obj_ref__1` (
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
  INDEX `obj_ref__1_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__1_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__1_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__1_0_0,
            SUBPARTITION obj_ref__1_0_1,
            SUBPARTITION obj_ref__1_0_2,
            SUBPARTITION obj_ref__1_0_3,
            SUBPARTITION obj_ref__1_0_4,
            SUBPARTITION obj_ref__1_0_5,
            SUBPARTITION obj_ref__1_0_6,
            SUBPARTITION obj_ref__1_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__2`;
CREATE TABLE `obj_ref__2` (
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
  INDEX `obj_ref__2_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__2_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__2_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__2_0_0,
            SUBPARTITION obj_ref__2_0_1,
            SUBPARTITION obj_ref__2_0_2,
            SUBPARTITION obj_ref__2_0_3,
            SUBPARTITION obj_ref__2_0_4,
            SUBPARTITION obj_ref__2_0_5,
            SUBPARTITION obj_ref__2_0_6,
            SUBPARTITION obj_ref__2_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__3`;
CREATE TABLE `obj_ref__3` (
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
  INDEX `obj_ref__3_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__3_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__3_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__3_0_0,
            SUBPARTITION obj_ref__3_0_1,
            SUBPARTITION obj_ref__3_0_2,
            SUBPARTITION obj_ref__3_0_3,
            SUBPARTITION obj_ref__3_0_4,
            SUBPARTITION obj_ref__3_0_5,
            SUBPARTITION obj_ref__3_0_6,
            SUBPARTITION obj_ref__3_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__4`;
CREATE TABLE `obj_ref__4` (
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
  INDEX `obj_ref__4_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__4_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__4_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__4_0_0,
            SUBPARTITION obj_ref__4_0_1,
            SUBPARTITION obj_ref__4_0_2,
            SUBPARTITION obj_ref__4_0_3,
            SUBPARTITION obj_ref__4_0_4,
            SUBPARTITION obj_ref__4_0_5,
            SUBPARTITION obj_ref__4_0_6,
            SUBPARTITION obj_ref__4_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__5`;
CREATE TABLE `obj_ref__5` (
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
  INDEX `obj_ref__5_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__5_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__5_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__5_0_0,
            SUBPARTITION obj_ref__5_0_1,
            SUBPARTITION obj_ref__5_0_2,
            SUBPARTITION obj_ref__5_0_3,
            SUBPARTITION obj_ref__5_0_4,
            SUBPARTITION obj_ref__5_0_5,
            SUBPARTITION obj_ref__5_0_6,
            SUBPARTITION obj_ref__5_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__6`;
CREATE TABLE `obj_ref__6` (
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
  INDEX `obj_ref__6_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__6_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__6_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__6_0_0,
            SUBPARTITION obj_ref__6_0_1,
            SUBPARTITION obj_ref__6_0_2,
            SUBPARTITION obj_ref__6_0_3,
            SUBPARTITION obj_ref__6_0_4,
            SUBPARTITION obj_ref__6_0_5,
            SUBPARTITION obj_ref__6_0_6,
            SUBPARTITION obj_ref__6_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__7`;
CREATE TABLE `obj_ref__7` (
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
  INDEX `obj_ref__7_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__7_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__7_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__7_0_0,
            SUBPARTITION obj_ref__7_0_1,
            SUBPARTITION obj_ref__7_0_2,
            SUBPARTITION obj_ref__7_0_3,
            SUBPARTITION obj_ref__7_0_4,
            SUBPARTITION obj_ref__7_0_5,
            SUBPARTITION obj_ref__7_0_6,
            SUBPARTITION obj_ref__7_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__8`;
CREATE TABLE `obj_ref__8` (
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
  INDEX `obj_ref__8_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__8_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__8_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__8_0_0,
            SUBPARTITION obj_ref__8_0_1,
            SUBPARTITION obj_ref__8_0_2,
            SUBPARTITION obj_ref__8_0_3,
            SUBPARTITION obj_ref__8_0_4,
            SUBPARTITION obj_ref__8_0_5,
            SUBPARTITION obj_ref__8_0_6,
            SUBPARTITION obj_ref__8_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__9`;
CREATE TABLE `obj_ref__9` (
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
  INDEX `obj_ref__9_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__9_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__9_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__9_0_0,
            SUBPARTITION obj_ref__9_0_1,
            SUBPARTITION obj_ref__9_0_2,
            SUBPARTITION obj_ref__9_0_3,
            SUBPARTITION obj_ref__9_0_4,
            SUBPARTITION obj_ref__9_0_5,
            SUBPARTITION obj_ref__9_0_6,
            SUBPARTITION obj_ref__9_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__10`;
CREATE TABLE `obj_ref__10` (
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
  INDEX `obj_ref__10_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__10_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__10_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__10_0_0,
            SUBPARTITION obj_ref__10_0_1,
            SUBPARTITION obj_ref__10_0_2,
            SUBPARTITION obj_ref__10_0_3,
            SUBPARTITION obj_ref__10_0_4,
            SUBPARTITION obj_ref__10_0_5,
            SUBPARTITION obj_ref__10_0_6,
            SUBPARTITION obj_ref__10_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__11`;
CREATE TABLE `obj_ref__11` (
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
  INDEX `obj_ref__11_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__11_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__11_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__11_0_0,
            SUBPARTITION obj_ref__11_0_1,
            SUBPARTITION obj_ref__11_0_2,
            SUBPARTITION obj_ref__11_0_3,
            SUBPARTITION obj_ref__11_0_4,
            SUBPARTITION obj_ref__11_0_5,
            SUBPARTITION obj_ref__11_0_6,
            SUBPARTITION obj_ref__11_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__12`;
CREATE TABLE `obj_ref__12` (
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
  INDEX `obj_ref__12_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__12_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__12_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__12_0_0,
            SUBPARTITION obj_ref__12_0_1,
            SUBPARTITION obj_ref__12_0_2,
            SUBPARTITION obj_ref__12_0_3,
            SUBPARTITION obj_ref__12_0_4,
            SUBPARTITION obj_ref__12_0_5,
            SUBPARTITION obj_ref__12_0_6,
            SUBPARTITION obj_ref__12_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__13`;
CREATE TABLE `obj_ref__13` (
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
  INDEX `obj_ref__13_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__13_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__13_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__13_0_0,
            SUBPARTITION obj_ref__13_0_1,
            SUBPARTITION obj_ref__13_0_2,
            SUBPARTITION obj_ref__13_0_3,
            SUBPARTITION obj_ref__13_0_4,
            SUBPARTITION obj_ref__13_0_5,
            SUBPARTITION obj_ref__13_0_6,
            SUBPARTITION obj_ref__13_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__14`;
CREATE TABLE `obj_ref__14` (
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
  INDEX `obj_ref__14_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__14_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__14_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__14_0_0,
            SUBPARTITION obj_ref__14_0_1,
            SUBPARTITION obj_ref__14_0_2,
            SUBPARTITION obj_ref__14_0_3,
            SUBPARTITION obj_ref__14_0_4,
            SUBPARTITION obj_ref__14_0_5,
            SUBPARTITION obj_ref__14_0_6,
            SUBPARTITION obj_ref__14_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__15`;
CREATE TABLE `obj_ref__15` (
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
  INDEX `obj_ref__15_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__15_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__15_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__15_0_0,
            SUBPARTITION obj_ref__15_0_1,
            SUBPARTITION obj_ref__15_0_2,
            SUBPARTITION obj_ref__15_0_3,
            SUBPARTITION obj_ref__15_0_4,
            SUBPARTITION obj_ref__15_0_5,
            SUBPARTITION obj_ref__15_0_6,
            SUBPARTITION obj_ref__15_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__16`;
CREATE TABLE `obj_ref__16` (
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
  INDEX `obj_ref__16_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__16_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__16_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__16_0_0,
            SUBPARTITION obj_ref__16_0_1,
            SUBPARTITION obj_ref__16_0_2,
            SUBPARTITION obj_ref__16_0_3,
            SUBPARTITION obj_ref__16_0_4,
            SUBPARTITION obj_ref__16_0_5,
            SUBPARTITION obj_ref__16_0_6,
            SUBPARTITION obj_ref__16_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__17`;
CREATE TABLE `obj_ref__17` (
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
  INDEX `obj_ref__17_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__17_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__17_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__17_0_0,
            SUBPARTITION obj_ref__17_0_1,
            SUBPARTITION obj_ref__17_0_2,
            SUBPARTITION obj_ref__17_0_3,
            SUBPARTITION obj_ref__17_0_4,
            SUBPARTITION obj_ref__17_0_5,
            SUBPARTITION obj_ref__17_0_6,
            SUBPARTITION obj_ref__17_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__18`;
CREATE TABLE `obj_ref__18` (
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
  INDEX `obj_ref__18_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__18_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__18_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__18_0_0,
            SUBPARTITION obj_ref__18_0_1,
            SUBPARTITION obj_ref__18_0_2,
            SUBPARTITION obj_ref__18_0_3,
            SUBPARTITION obj_ref__18_0_4,
            SUBPARTITION obj_ref__18_0_5,
            SUBPARTITION obj_ref__18_0_6,
            SUBPARTITION obj_ref__18_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__19`;
CREATE TABLE `obj_ref__19` (
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
  INDEX `obj_ref__19_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__19_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__19_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__19_0_0,
            SUBPARTITION obj_ref__19_0_1,
            SUBPARTITION obj_ref__19_0_2,
            SUBPARTITION obj_ref__19_0_3,
            SUBPARTITION obj_ref__19_0_4,
            SUBPARTITION obj_ref__19_0_5,
            SUBPARTITION obj_ref__19_0_6,
            SUBPARTITION obj_ref__19_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__20`;
CREATE TABLE `obj_ref__20` (
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
  INDEX `obj_ref__20_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__20_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__20_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__20_0_0,
            SUBPARTITION obj_ref__20_0_1,
            SUBPARTITION obj_ref__20_0_2,
            SUBPARTITION obj_ref__20_0_3,
            SUBPARTITION obj_ref__20_0_4,
            SUBPARTITION obj_ref__20_0_5,
            SUBPARTITION obj_ref__20_0_6,
            SUBPARTITION obj_ref__20_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__21`;
CREATE TABLE `obj_ref__21` (
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
  INDEX `obj_ref__21_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__21_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__21_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__21_0_0,
            SUBPARTITION obj_ref__21_0_1,
            SUBPARTITION obj_ref__21_0_2,
            SUBPARTITION obj_ref__21_0_3,
            SUBPARTITION obj_ref__21_0_4,
            SUBPARTITION obj_ref__21_0_5,
            SUBPARTITION obj_ref__21_0_6,
            SUBPARTITION obj_ref__21_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__22`;
CREATE TABLE `obj_ref__22` (
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
  INDEX `obj_ref__22_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__22_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__22_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__22_0_0,
            SUBPARTITION obj_ref__22_0_1,
            SUBPARTITION obj_ref__22_0_2,
            SUBPARTITION obj_ref__22_0_3,
            SUBPARTITION obj_ref__22_0_4,
            SUBPARTITION obj_ref__22_0_5,
            SUBPARTITION obj_ref__22_0_6,
            SUBPARTITION obj_ref__22_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__23`;
CREATE TABLE `obj_ref__23` (
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
  INDEX `obj_ref__23_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__23_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__23_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__23_0_0,
            SUBPARTITION obj_ref__23_0_1,
            SUBPARTITION obj_ref__23_0_2,
            SUBPARTITION obj_ref__23_0_3,
            SUBPARTITION obj_ref__23_0_4,
            SUBPARTITION obj_ref__23_0_5,
            SUBPARTITION obj_ref__23_0_6,
            SUBPARTITION obj_ref__23_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__24`;
CREATE TABLE `obj_ref__24` (
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
  INDEX `obj_ref__24_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__24_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__24_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__24_0_0,
            SUBPARTITION obj_ref__24_0_1,
            SUBPARTITION obj_ref__24_0_2,
            SUBPARTITION obj_ref__24_0_3,
            SUBPARTITION obj_ref__24_0_4,
            SUBPARTITION obj_ref__24_0_5,
            SUBPARTITION obj_ref__24_0_6,
            SUBPARTITION obj_ref__24_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__25`;
CREATE TABLE `obj_ref__25` (
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
  INDEX `obj_ref__25_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__25_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__25_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__25_0_0,
            SUBPARTITION obj_ref__25_0_1,
            SUBPARTITION obj_ref__25_0_2,
            SUBPARTITION obj_ref__25_0_3,
            SUBPARTITION obj_ref__25_0_4,
            SUBPARTITION obj_ref__25_0_5,
            SUBPARTITION obj_ref__25_0_6,
            SUBPARTITION obj_ref__25_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__26`;
CREATE TABLE `obj_ref__26` (
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
  INDEX `obj_ref__26_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__26_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__26_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__26_0_0,
            SUBPARTITION obj_ref__26_0_1,
            SUBPARTITION obj_ref__26_0_2,
            SUBPARTITION obj_ref__26_0_3,
            SUBPARTITION obj_ref__26_0_4,
            SUBPARTITION obj_ref__26_0_5,
            SUBPARTITION obj_ref__26_0_6,
            SUBPARTITION obj_ref__26_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__27`;
CREATE TABLE `obj_ref__27` (
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
  INDEX `obj_ref__27_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__27_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__27_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__27_0_0,
            SUBPARTITION obj_ref__27_0_1,
            SUBPARTITION obj_ref__27_0_2,
            SUBPARTITION obj_ref__27_0_3,
            SUBPARTITION obj_ref__27_0_4,
            SUBPARTITION obj_ref__27_0_5,
            SUBPARTITION obj_ref__27_0_6,
            SUBPARTITION obj_ref__27_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__28`;
CREATE TABLE `obj_ref__28` (
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
  INDEX `obj_ref__28_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__28_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__28_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__28_0_0,
            SUBPARTITION obj_ref__28_0_1,
            SUBPARTITION obj_ref__28_0_2,
            SUBPARTITION obj_ref__28_0_3,
            SUBPARTITION obj_ref__28_0_4,
            SUBPARTITION obj_ref__28_0_5,
            SUBPARTITION obj_ref__28_0_6,
            SUBPARTITION obj_ref__28_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__29`;
CREATE TABLE `obj_ref__29` (
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
  INDEX `obj_ref__29_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__29_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__29_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__29_0_0,
            SUBPARTITION obj_ref__29_0_1,
            SUBPARTITION obj_ref__29_0_2,
            SUBPARTITION obj_ref__29_0_3,
            SUBPARTITION obj_ref__29_0_4,
            SUBPARTITION obj_ref__29_0_5,
            SUBPARTITION obj_ref__29_0_6,
            SUBPARTITION obj_ref__29_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__30`;
CREATE TABLE `obj_ref__30` (
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
  INDEX `obj_ref__30_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__30_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__30_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__30_0_0,
            SUBPARTITION obj_ref__30_0_1,
            SUBPARTITION obj_ref__30_0_2,
            SUBPARTITION obj_ref__30_0_3,
            SUBPARTITION obj_ref__30_0_4,
            SUBPARTITION obj_ref__30_0_5,
            SUBPARTITION obj_ref__30_0_6,
            SUBPARTITION obj_ref__30_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__31`;
CREATE TABLE `obj_ref__31` (
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
  INDEX `obj_ref__31_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__31_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__31_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__31_0_0,
            SUBPARTITION obj_ref__31_0_1,
            SUBPARTITION obj_ref__31_0_2,
            SUBPARTITION obj_ref__31_0_3,
            SUBPARTITION obj_ref__31_0_4,
            SUBPARTITION obj_ref__31_0_5,
            SUBPARTITION obj_ref__31_0_6,
            SUBPARTITION obj_ref__31_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__32`;
CREATE TABLE `obj_ref__32` (
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
  INDEX `obj_ref__32_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__32_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__32_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__32_0_0,
            SUBPARTITION obj_ref__32_0_1,
            SUBPARTITION obj_ref__32_0_2,
            SUBPARTITION obj_ref__32_0_3,
            SUBPARTITION obj_ref__32_0_4,
            SUBPARTITION obj_ref__32_0_5,
            SUBPARTITION obj_ref__32_0_6,
            SUBPARTITION obj_ref__32_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__33`;
CREATE TABLE `obj_ref__33` (
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
  INDEX `obj_ref__33_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__33_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__33_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__33_0_0,
            SUBPARTITION obj_ref__33_0_1,
            SUBPARTITION obj_ref__33_0_2,
            SUBPARTITION obj_ref__33_0_3,
            SUBPARTITION obj_ref__33_0_4,
            SUBPARTITION obj_ref__33_0_5,
            SUBPARTITION obj_ref__33_0_6,
            SUBPARTITION obj_ref__33_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__34`;
CREATE TABLE `obj_ref__34` (
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
  INDEX `obj_ref__34_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__34_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__34_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__34_0_0,
            SUBPARTITION obj_ref__34_0_1,
            SUBPARTITION obj_ref__34_0_2,
            SUBPARTITION obj_ref__34_0_3,
            SUBPARTITION obj_ref__34_0_4,
            SUBPARTITION obj_ref__34_0_5,
            SUBPARTITION obj_ref__34_0_6,
            SUBPARTITION obj_ref__34_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__35`;
CREATE TABLE `obj_ref__35` (
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
  INDEX `obj_ref__35_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__35_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__35_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__35_0_0,
            SUBPARTITION obj_ref__35_0_1,
            SUBPARTITION obj_ref__35_0_2,
            SUBPARTITION obj_ref__35_0_3,
            SUBPARTITION obj_ref__35_0_4,
            SUBPARTITION obj_ref__35_0_5,
            SUBPARTITION obj_ref__35_0_6,
            SUBPARTITION obj_ref__35_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__36`;
CREATE TABLE `obj_ref__36` (
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
  INDEX `obj_ref__36_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__36_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__36_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__36_0_0,
            SUBPARTITION obj_ref__36_0_1,
            SUBPARTITION obj_ref__36_0_2,
            SUBPARTITION obj_ref__36_0_3,
            SUBPARTITION obj_ref__36_0_4,
            SUBPARTITION obj_ref__36_0_5,
            SUBPARTITION obj_ref__36_0_6,
            SUBPARTITION obj_ref__36_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__37`;
CREATE TABLE `obj_ref__37` (
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
  INDEX `obj_ref__37_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__37_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__37_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__37_0_0,
            SUBPARTITION obj_ref__37_0_1,
            SUBPARTITION obj_ref__37_0_2,
            SUBPARTITION obj_ref__37_0_3,
            SUBPARTITION obj_ref__37_0_4,
            SUBPARTITION obj_ref__37_0_5,
            SUBPARTITION obj_ref__37_0_6,
            SUBPARTITION obj_ref__37_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__38`;
CREATE TABLE `obj_ref__38` (
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
  INDEX `obj_ref__38_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__38_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__38_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__38_0_0,
            SUBPARTITION obj_ref__38_0_1,
            SUBPARTITION obj_ref__38_0_2,
            SUBPARTITION obj_ref__38_0_3,
            SUBPARTITION obj_ref__38_0_4,
            SUBPARTITION obj_ref__38_0_5,
            SUBPARTITION obj_ref__38_0_6,
            SUBPARTITION obj_ref__38_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__39`;
CREATE TABLE `obj_ref__39` (
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
  INDEX `obj_ref__39_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__39_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__39_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__39_0_0,
            SUBPARTITION obj_ref__39_0_1,
            SUBPARTITION obj_ref__39_0_2,
            SUBPARTITION obj_ref__39_0_3,
            SUBPARTITION obj_ref__39_0_4,
            SUBPARTITION obj_ref__39_0_5,
            SUBPARTITION obj_ref__39_0_6,
            SUBPARTITION obj_ref__39_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__40`;
CREATE TABLE `obj_ref__40` (
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
  INDEX `obj_ref__40_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__40_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__40_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__40_0_0,
            SUBPARTITION obj_ref__40_0_1,
            SUBPARTITION obj_ref__40_0_2,
            SUBPARTITION obj_ref__40_0_3,
            SUBPARTITION obj_ref__40_0_4,
            SUBPARTITION obj_ref__40_0_5,
            SUBPARTITION obj_ref__40_0_6,
            SUBPARTITION obj_ref__40_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__41`;
CREATE TABLE `obj_ref__41` (
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
  INDEX `obj_ref__41_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__41_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__41_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__41_0_0,
            SUBPARTITION obj_ref__41_0_1,
            SUBPARTITION obj_ref__41_0_2,
            SUBPARTITION obj_ref__41_0_3,
            SUBPARTITION obj_ref__41_0_4,
            SUBPARTITION obj_ref__41_0_5,
            SUBPARTITION obj_ref__41_0_6,
            SUBPARTITION obj_ref__41_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__42`;
CREATE TABLE `obj_ref__42` (
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
  INDEX `obj_ref__42_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__42_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__42_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__42_0_0,
            SUBPARTITION obj_ref__42_0_1,
            SUBPARTITION obj_ref__42_0_2,
            SUBPARTITION obj_ref__42_0_3,
            SUBPARTITION obj_ref__42_0_4,
            SUBPARTITION obj_ref__42_0_5,
            SUBPARTITION obj_ref__42_0_6,
            SUBPARTITION obj_ref__42_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__43`;
CREATE TABLE `obj_ref__43` (
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
  INDEX `obj_ref__43_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__43_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__43_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__43_0_0,
            SUBPARTITION obj_ref__43_0_1,
            SUBPARTITION obj_ref__43_0_2,
            SUBPARTITION obj_ref__43_0_3,
            SUBPARTITION obj_ref__43_0_4,
            SUBPARTITION obj_ref__43_0_5,
            SUBPARTITION obj_ref__43_0_6,
            SUBPARTITION obj_ref__43_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__44`;
CREATE TABLE `obj_ref__44` (
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
  INDEX `obj_ref__44_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__44_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__44_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__44_0_0,
            SUBPARTITION obj_ref__44_0_1,
            SUBPARTITION obj_ref__44_0_2,
            SUBPARTITION obj_ref__44_0_3,
            SUBPARTITION obj_ref__44_0_4,
            SUBPARTITION obj_ref__44_0_5,
            SUBPARTITION obj_ref__44_0_6,
            SUBPARTITION obj_ref__44_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__45`;
CREATE TABLE `obj_ref__45` (
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
  INDEX `obj_ref__45_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__45_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__45_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__45_0_0,
            SUBPARTITION obj_ref__45_0_1,
            SUBPARTITION obj_ref__45_0_2,
            SUBPARTITION obj_ref__45_0_3,
            SUBPARTITION obj_ref__45_0_4,
            SUBPARTITION obj_ref__45_0_5,
            SUBPARTITION obj_ref__45_0_6,
            SUBPARTITION obj_ref__45_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__46`;
CREATE TABLE `obj_ref__46` (
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
  INDEX `obj_ref__46_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__46_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__46_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__46_0_0,
            SUBPARTITION obj_ref__46_0_1,
            SUBPARTITION obj_ref__46_0_2,
            SUBPARTITION obj_ref__46_0_3,
            SUBPARTITION obj_ref__46_0_4,
            SUBPARTITION obj_ref__46_0_5,
            SUBPARTITION obj_ref__46_0_6,
            SUBPARTITION obj_ref__46_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__47`;
CREATE TABLE `obj_ref__47` (
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
  INDEX `obj_ref__47_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__47_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__47_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__47_0_0,
            SUBPARTITION obj_ref__47_0_1,
            SUBPARTITION obj_ref__47_0_2,
            SUBPARTITION obj_ref__47_0_3,
            SUBPARTITION obj_ref__47_0_4,
            SUBPARTITION obj_ref__47_0_5,
            SUBPARTITION obj_ref__47_0_6,
            SUBPARTITION obj_ref__47_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__48`;
CREATE TABLE `obj_ref__48` (
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
  INDEX `obj_ref__48_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__48_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__48_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__48_0_0,
            SUBPARTITION obj_ref__48_0_1,
            SUBPARTITION obj_ref__48_0_2,
            SUBPARTITION obj_ref__48_0_3,
            SUBPARTITION obj_ref__48_0_4,
            SUBPARTITION obj_ref__48_0_5,
            SUBPARTITION obj_ref__48_0_6,
            SUBPARTITION obj_ref__48_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__49`;
CREATE TABLE `obj_ref__49` (
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
  INDEX `obj_ref__49_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__49_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__49_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__49_0_0,
            SUBPARTITION obj_ref__49_0_1,
            SUBPARTITION obj_ref__49_0_2,
            SUBPARTITION obj_ref__49_0_3,
            SUBPARTITION obj_ref__49_0_4,
            SUBPARTITION obj_ref__49_0_5,
            SUBPARTITION obj_ref__49_0_6,
            SUBPARTITION obj_ref__49_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__50`;
CREATE TABLE `obj_ref__50` (
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
  INDEX `obj_ref__50_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__50_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__50_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__50_0_0,
            SUBPARTITION obj_ref__50_0_1,
            SUBPARTITION obj_ref__50_0_2,
            SUBPARTITION obj_ref__50_0_3,
            SUBPARTITION obj_ref__50_0_4,
            SUBPARTITION obj_ref__50_0_5,
            SUBPARTITION obj_ref__50_0_6,
            SUBPARTITION obj_ref__50_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__51`;
CREATE TABLE `obj_ref__51` (
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
  INDEX `obj_ref__51_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__51_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__51_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__51_0_0,
            SUBPARTITION obj_ref__51_0_1,
            SUBPARTITION obj_ref__51_0_2,
            SUBPARTITION obj_ref__51_0_3,
            SUBPARTITION obj_ref__51_0_4,
            SUBPARTITION obj_ref__51_0_5,
            SUBPARTITION obj_ref__51_0_6,
            SUBPARTITION obj_ref__51_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__52`;
CREATE TABLE `obj_ref__52` (
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
  INDEX `obj_ref__52_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__52_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__52_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__52_0_0,
            SUBPARTITION obj_ref__52_0_1,
            SUBPARTITION obj_ref__52_0_2,
            SUBPARTITION obj_ref__52_0_3,
            SUBPARTITION obj_ref__52_0_4,
            SUBPARTITION obj_ref__52_0_5,
            SUBPARTITION obj_ref__52_0_6,
            SUBPARTITION obj_ref__52_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__53`;
CREATE TABLE `obj_ref__53` (
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
  INDEX `obj_ref__53_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__53_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__53_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__53_0_0,
            SUBPARTITION obj_ref__53_0_1,
            SUBPARTITION obj_ref__53_0_2,
            SUBPARTITION obj_ref__53_0_3,
            SUBPARTITION obj_ref__53_0_4,
            SUBPARTITION obj_ref__53_0_5,
            SUBPARTITION obj_ref__53_0_6,
            SUBPARTITION obj_ref__53_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__54`;
CREATE TABLE `obj_ref__54` (
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
  INDEX `obj_ref__54_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__54_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__54_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__54_0_0,
            SUBPARTITION obj_ref__54_0_1,
            SUBPARTITION obj_ref__54_0_2,
            SUBPARTITION obj_ref__54_0_3,
            SUBPARTITION obj_ref__54_0_4,
            SUBPARTITION obj_ref__54_0_5,
            SUBPARTITION obj_ref__54_0_6,
            SUBPARTITION obj_ref__54_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__55`;
CREATE TABLE `obj_ref__55` (
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
  INDEX `obj_ref__55_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__55_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__55_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__55_0_0,
            SUBPARTITION obj_ref__55_0_1,
            SUBPARTITION obj_ref__55_0_2,
            SUBPARTITION obj_ref__55_0_3,
            SUBPARTITION obj_ref__55_0_4,
            SUBPARTITION obj_ref__55_0_5,
            SUBPARTITION obj_ref__55_0_6,
            SUBPARTITION obj_ref__55_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__56`;
CREATE TABLE `obj_ref__56` (
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
  INDEX `obj_ref__56_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__56_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__56_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__56_0_0,
            SUBPARTITION obj_ref__56_0_1,
            SUBPARTITION obj_ref__56_0_2,
            SUBPARTITION obj_ref__56_0_3,
            SUBPARTITION obj_ref__56_0_4,
            SUBPARTITION obj_ref__56_0_5,
            SUBPARTITION obj_ref__56_0_6,
            SUBPARTITION obj_ref__56_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__57`;
CREATE TABLE `obj_ref__57` (
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
  INDEX `obj_ref__57_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__57_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__57_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__57_0_0,
            SUBPARTITION obj_ref__57_0_1,
            SUBPARTITION obj_ref__57_0_2,
            SUBPARTITION obj_ref__57_0_3,
            SUBPARTITION obj_ref__57_0_4,
            SUBPARTITION obj_ref__57_0_5,
            SUBPARTITION obj_ref__57_0_6,
            SUBPARTITION obj_ref__57_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__58`;
CREATE TABLE `obj_ref__58` (
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
  INDEX `obj_ref__58_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__58_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__58_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__58_0_0,
            SUBPARTITION obj_ref__58_0_1,
            SUBPARTITION obj_ref__58_0_2,
            SUBPARTITION obj_ref__58_0_3,
            SUBPARTITION obj_ref__58_0_4,
            SUBPARTITION obj_ref__58_0_5,
            SUBPARTITION obj_ref__58_0_6,
            SUBPARTITION obj_ref__58_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__59`;
CREATE TABLE `obj_ref__59` (
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
  INDEX `obj_ref__59_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__59_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__59_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__59_0_0,
            SUBPARTITION obj_ref__59_0_1,
            SUBPARTITION obj_ref__59_0_2,
            SUBPARTITION obj_ref__59_0_3,
            SUBPARTITION obj_ref__59_0_4,
            SUBPARTITION obj_ref__59_0_5,
            SUBPARTITION obj_ref__59_0_6,
            SUBPARTITION obj_ref__59_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__60`;
CREATE TABLE `obj_ref__60` (
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
  INDEX `obj_ref__60_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__60_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__60_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__60_0_0,
            SUBPARTITION obj_ref__60_0_1,
            SUBPARTITION obj_ref__60_0_2,
            SUBPARTITION obj_ref__60_0_3,
            SUBPARTITION obj_ref__60_0_4,
            SUBPARTITION obj_ref__60_0_5,
            SUBPARTITION obj_ref__60_0_6,
            SUBPARTITION obj_ref__60_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__61`;
CREATE TABLE `obj_ref__61` (
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
  INDEX `obj_ref__61_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__61_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__61_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__61_0_0,
            SUBPARTITION obj_ref__61_0_1,
            SUBPARTITION obj_ref__61_0_2,
            SUBPARTITION obj_ref__61_0_3,
            SUBPARTITION obj_ref__61_0_4,
            SUBPARTITION obj_ref__61_0_5,
            SUBPARTITION obj_ref__61_0_6,
            SUBPARTITION obj_ref__61_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__62`;
CREATE TABLE `obj_ref__62` (
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
  INDEX `obj_ref__62_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__62_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__62_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__62_0_0,
            SUBPARTITION obj_ref__62_0_1,
            SUBPARTITION obj_ref__62_0_2,
            SUBPARTITION obj_ref__62_0_3,
            SUBPARTITION obj_ref__62_0_4,
            SUBPARTITION obj_ref__62_0_5,
            SUBPARTITION obj_ref__62_0_6,
            SUBPARTITION obj_ref__62_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__63`;
CREATE TABLE `obj_ref__63` (
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
  INDEX `obj_ref__63_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__63_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__63_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__63_0_0,
            SUBPARTITION obj_ref__63_0_1,
            SUBPARTITION obj_ref__63_0_2,
            SUBPARTITION obj_ref__63_0_3,
            SUBPARTITION obj_ref__63_0_4,
            SUBPARTITION obj_ref__63_0_5,
            SUBPARTITION obj_ref__63_0_6,
            SUBPARTITION obj_ref__63_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__64`;
CREATE TABLE `obj_ref__64` (
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
  INDEX `obj_ref__64_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__64_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__64_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__64_0_0,
            SUBPARTITION obj_ref__64_0_1,
            SUBPARTITION obj_ref__64_0_2,
            SUBPARTITION obj_ref__64_0_3,
            SUBPARTITION obj_ref__64_0_4,
            SUBPARTITION obj_ref__64_0_5,
            SUBPARTITION obj_ref__64_0_6,
            SUBPARTITION obj_ref__64_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__65`;
CREATE TABLE `obj_ref__65` (
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
  INDEX `obj_ref__65_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__65_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__65_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__65_0_0,
            SUBPARTITION obj_ref__65_0_1,
            SUBPARTITION obj_ref__65_0_2,
            SUBPARTITION obj_ref__65_0_3,
            SUBPARTITION obj_ref__65_0_4,
            SUBPARTITION obj_ref__65_0_5,
            SUBPARTITION obj_ref__65_0_6,
            SUBPARTITION obj_ref__65_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__66`;
CREATE TABLE `obj_ref__66` (
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
  INDEX `obj_ref__66_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__66_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__66_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__66_0_0,
            SUBPARTITION obj_ref__66_0_1,
            SUBPARTITION obj_ref__66_0_2,
            SUBPARTITION obj_ref__66_0_3,
            SUBPARTITION obj_ref__66_0_4,
            SUBPARTITION obj_ref__66_0_5,
            SUBPARTITION obj_ref__66_0_6,
            SUBPARTITION obj_ref__66_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__67`;
CREATE TABLE `obj_ref__67` (
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
  INDEX `obj_ref__67_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__67_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__67_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__67_0_0,
            SUBPARTITION obj_ref__67_0_1,
            SUBPARTITION obj_ref__67_0_2,
            SUBPARTITION obj_ref__67_0_3,
            SUBPARTITION obj_ref__67_0_4,
            SUBPARTITION obj_ref__67_0_5,
            SUBPARTITION obj_ref__67_0_6,
            SUBPARTITION obj_ref__67_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__68`;
CREATE TABLE `obj_ref__68` (
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
  INDEX `obj_ref__68_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__68_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__68_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__68_0_0,
            SUBPARTITION obj_ref__68_0_1,
            SUBPARTITION obj_ref__68_0_2,
            SUBPARTITION obj_ref__68_0_3,
            SUBPARTITION obj_ref__68_0_4,
            SUBPARTITION obj_ref__68_0_5,
            SUBPARTITION obj_ref__68_0_6,
            SUBPARTITION obj_ref__68_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__69`;
CREATE TABLE `obj_ref__69` (
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
  INDEX `obj_ref__69_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__69_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__69_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__69_0_0,
            SUBPARTITION obj_ref__69_0_1,
            SUBPARTITION obj_ref__69_0_2,
            SUBPARTITION obj_ref__69_0_3,
            SUBPARTITION obj_ref__69_0_4,
            SUBPARTITION obj_ref__69_0_5,
            SUBPARTITION obj_ref__69_0_6,
            SUBPARTITION obj_ref__69_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__70`;
CREATE TABLE `obj_ref__70` (
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
  INDEX `obj_ref__70_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__70_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__70_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__70_0_0,
            SUBPARTITION obj_ref__70_0_1,
            SUBPARTITION obj_ref__70_0_2,
            SUBPARTITION obj_ref__70_0_3,
            SUBPARTITION obj_ref__70_0_4,
            SUBPARTITION obj_ref__70_0_5,
            SUBPARTITION obj_ref__70_0_6,
            SUBPARTITION obj_ref__70_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__71`;
CREATE TABLE `obj_ref__71` (
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
  INDEX `obj_ref__71_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__71_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__71_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__71_0_0,
            SUBPARTITION obj_ref__71_0_1,
            SUBPARTITION obj_ref__71_0_2,
            SUBPARTITION obj_ref__71_0_3,
            SUBPARTITION obj_ref__71_0_4,
            SUBPARTITION obj_ref__71_0_5,
            SUBPARTITION obj_ref__71_0_6,
            SUBPARTITION obj_ref__71_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__72`;
CREATE TABLE `obj_ref__72` (
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
  INDEX `obj_ref__72_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__72_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__72_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__72_0_0,
            SUBPARTITION obj_ref__72_0_1,
            SUBPARTITION obj_ref__72_0_2,
            SUBPARTITION obj_ref__72_0_3,
            SUBPARTITION obj_ref__72_0_4,
            SUBPARTITION obj_ref__72_0_5,
            SUBPARTITION obj_ref__72_0_6,
            SUBPARTITION obj_ref__72_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__73`;
CREATE TABLE `obj_ref__73` (
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
  INDEX `obj_ref__73_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__73_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__73_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__73_0_0,
            SUBPARTITION obj_ref__73_0_1,
            SUBPARTITION obj_ref__73_0_2,
            SUBPARTITION obj_ref__73_0_3,
            SUBPARTITION obj_ref__73_0_4,
            SUBPARTITION obj_ref__73_0_5,
            SUBPARTITION obj_ref__73_0_6,
            SUBPARTITION obj_ref__73_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__74`;
CREATE TABLE `obj_ref__74` (
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
  INDEX `obj_ref__74_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__74_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__74_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__74_0_0,
            SUBPARTITION obj_ref__74_0_1,
            SUBPARTITION obj_ref__74_0_2,
            SUBPARTITION obj_ref__74_0_3,
            SUBPARTITION obj_ref__74_0_4,
            SUBPARTITION obj_ref__74_0_5,
            SUBPARTITION obj_ref__74_0_6,
            SUBPARTITION obj_ref__74_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__75`;
CREATE TABLE `obj_ref__75` (
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
  INDEX `obj_ref__75_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__75_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__75_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__75_0_0,
            SUBPARTITION obj_ref__75_0_1,
            SUBPARTITION obj_ref__75_0_2,
            SUBPARTITION obj_ref__75_0_3,
            SUBPARTITION obj_ref__75_0_4,
            SUBPARTITION obj_ref__75_0_5,
            SUBPARTITION obj_ref__75_0_6,
            SUBPARTITION obj_ref__75_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__76`;
CREATE TABLE `obj_ref__76` (
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
  INDEX `obj_ref__76_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__76_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__76_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__76_0_0,
            SUBPARTITION obj_ref__76_0_1,
            SUBPARTITION obj_ref__76_0_2,
            SUBPARTITION obj_ref__76_0_3,
            SUBPARTITION obj_ref__76_0_4,
            SUBPARTITION obj_ref__76_0_5,
            SUBPARTITION obj_ref__76_0_6,
            SUBPARTITION obj_ref__76_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__77`;
CREATE TABLE `obj_ref__77` (
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
  INDEX `obj_ref__77_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__77_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__77_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__77_0_0,
            SUBPARTITION obj_ref__77_0_1,
            SUBPARTITION obj_ref__77_0_2,
            SUBPARTITION obj_ref__77_0_3,
            SUBPARTITION obj_ref__77_0_4,
            SUBPARTITION obj_ref__77_0_5,
            SUBPARTITION obj_ref__77_0_6,
            SUBPARTITION obj_ref__77_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__78`;
CREATE TABLE `obj_ref__78` (
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
  INDEX `obj_ref__78_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__78_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__78_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__78_0_0,
            SUBPARTITION obj_ref__78_0_1,
            SUBPARTITION obj_ref__78_0_2,
            SUBPARTITION obj_ref__78_0_3,
            SUBPARTITION obj_ref__78_0_4,
            SUBPARTITION obj_ref__78_0_5,
            SUBPARTITION obj_ref__78_0_6,
            SUBPARTITION obj_ref__78_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__79`;
CREATE TABLE `obj_ref__79` (
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
  INDEX `obj_ref__79_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__79_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__79_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__79_0_0,
            SUBPARTITION obj_ref__79_0_1,
            SUBPARTITION obj_ref__79_0_2,
            SUBPARTITION obj_ref__79_0_3,
            SUBPARTITION obj_ref__79_0_4,
            SUBPARTITION obj_ref__79_0_5,
            SUBPARTITION obj_ref__79_0_6,
            SUBPARTITION obj_ref__79_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__80`;
CREATE TABLE `obj_ref__80` (
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
  INDEX `obj_ref__80_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__80_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__80_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__80_0_0,
            SUBPARTITION obj_ref__80_0_1,
            SUBPARTITION obj_ref__80_0_2,
            SUBPARTITION obj_ref__80_0_3,
            SUBPARTITION obj_ref__80_0_4,
            SUBPARTITION obj_ref__80_0_5,
            SUBPARTITION obj_ref__80_0_6,
            SUBPARTITION obj_ref__80_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__81`;
CREATE TABLE `obj_ref__81` (
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
  INDEX `obj_ref__81_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__81_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__81_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__81_0_0,
            SUBPARTITION obj_ref__81_0_1,
            SUBPARTITION obj_ref__81_0_2,
            SUBPARTITION obj_ref__81_0_3,
            SUBPARTITION obj_ref__81_0_4,
            SUBPARTITION obj_ref__81_0_5,
            SUBPARTITION obj_ref__81_0_6,
            SUBPARTITION obj_ref__81_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__82`;
CREATE TABLE `obj_ref__82` (
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
  INDEX `obj_ref__82_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__82_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__82_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__82_0_0,
            SUBPARTITION obj_ref__82_0_1,
            SUBPARTITION obj_ref__82_0_2,
            SUBPARTITION obj_ref__82_0_3,
            SUBPARTITION obj_ref__82_0_4,
            SUBPARTITION obj_ref__82_0_5,
            SUBPARTITION obj_ref__82_0_6,
            SUBPARTITION obj_ref__82_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__83`;
CREATE TABLE `obj_ref__83` (
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
  INDEX `obj_ref__83_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__83_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__83_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__83_0_0,
            SUBPARTITION obj_ref__83_0_1,
            SUBPARTITION obj_ref__83_0_2,
            SUBPARTITION obj_ref__83_0_3,
            SUBPARTITION obj_ref__83_0_4,
            SUBPARTITION obj_ref__83_0_5,
            SUBPARTITION obj_ref__83_0_6,
            SUBPARTITION obj_ref__83_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__84`;
CREATE TABLE `obj_ref__84` (
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
  INDEX `obj_ref__84_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__84_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__84_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__84_0_0,
            SUBPARTITION obj_ref__84_0_1,
            SUBPARTITION obj_ref__84_0_2,
            SUBPARTITION obj_ref__84_0_3,
            SUBPARTITION obj_ref__84_0_4,
            SUBPARTITION obj_ref__84_0_5,
            SUBPARTITION obj_ref__84_0_6,
            SUBPARTITION obj_ref__84_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__85`;
CREATE TABLE `obj_ref__85` (
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
  INDEX `obj_ref__85_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__85_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__85_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__85_0_0,
            SUBPARTITION obj_ref__85_0_1,
            SUBPARTITION obj_ref__85_0_2,
            SUBPARTITION obj_ref__85_0_3,
            SUBPARTITION obj_ref__85_0_4,
            SUBPARTITION obj_ref__85_0_5,
            SUBPARTITION obj_ref__85_0_6,
            SUBPARTITION obj_ref__85_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__86`;
CREATE TABLE `obj_ref__86` (
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
  INDEX `obj_ref__86_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__86_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__86_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__86_0_0,
            SUBPARTITION obj_ref__86_0_1,
            SUBPARTITION obj_ref__86_0_2,
            SUBPARTITION obj_ref__86_0_3,
            SUBPARTITION obj_ref__86_0_4,
            SUBPARTITION obj_ref__86_0_5,
            SUBPARTITION obj_ref__86_0_6,
            SUBPARTITION obj_ref__86_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__87`;
CREATE TABLE `obj_ref__87` (
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
  INDEX `obj_ref__87_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__87_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__87_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__87_0_0,
            SUBPARTITION obj_ref__87_0_1,
            SUBPARTITION obj_ref__87_0_2,
            SUBPARTITION obj_ref__87_0_3,
            SUBPARTITION obj_ref__87_0_4,
            SUBPARTITION obj_ref__87_0_5,
            SUBPARTITION obj_ref__87_0_6,
            SUBPARTITION obj_ref__87_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__88`;
CREATE TABLE `obj_ref__88` (
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
  INDEX `obj_ref__88_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__88_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__88_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__88_0_0,
            SUBPARTITION obj_ref__88_0_1,
            SUBPARTITION obj_ref__88_0_2,
            SUBPARTITION obj_ref__88_0_3,
            SUBPARTITION obj_ref__88_0_4,
            SUBPARTITION obj_ref__88_0_5,
            SUBPARTITION obj_ref__88_0_6,
            SUBPARTITION obj_ref__88_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__89`;
CREATE TABLE `obj_ref__89` (
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
  INDEX `obj_ref__89_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__89_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__89_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__89_0_0,
            SUBPARTITION obj_ref__89_0_1,
            SUBPARTITION obj_ref__89_0_2,
            SUBPARTITION obj_ref__89_0_3,
            SUBPARTITION obj_ref__89_0_4,
            SUBPARTITION obj_ref__89_0_5,
            SUBPARTITION obj_ref__89_0_6,
            SUBPARTITION obj_ref__89_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__90`;
CREATE TABLE `obj_ref__90` (
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
  INDEX `obj_ref__90_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__90_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__90_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__90_0_0,
            SUBPARTITION obj_ref__90_0_1,
            SUBPARTITION obj_ref__90_0_2,
            SUBPARTITION obj_ref__90_0_3,
            SUBPARTITION obj_ref__90_0_4,
            SUBPARTITION obj_ref__90_0_5,
            SUBPARTITION obj_ref__90_0_6,
            SUBPARTITION obj_ref__90_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__91`;
CREATE TABLE `obj_ref__91` (
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
  INDEX `obj_ref__91_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__91_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__91_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__91_0_0,
            SUBPARTITION obj_ref__91_0_1,
            SUBPARTITION obj_ref__91_0_2,
            SUBPARTITION obj_ref__91_0_3,
            SUBPARTITION obj_ref__91_0_4,
            SUBPARTITION obj_ref__91_0_5,
            SUBPARTITION obj_ref__91_0_6,
            SUBPARTITION obj_ref__91_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__92`;
CREATE TABLE `obj_ref__92` (
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
  INDEX `obj_ref__92_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__92_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__92_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__92_0_0,
            SUBPARTITION obj_ref__92_0_1,
            SUBPARTITION obj_ref__92_0_2,
            SUBPARTITION obj_ref__92_0_3,
            SUBPARTITION obj_ref__92_0_4,
            SUBPARTITION obj_ref__92_0_5,
            SUBPARTITION obj_ref__92_0_6,
            SUBPARTITION obj_ref__92_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__93`;
CREATE TABLE `obj_ref__93` (
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
  INDEX `obj_ref__93_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__93_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__93_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__93_0_0,
            SUBPARTITION obj_ref__93_0_1,
            SUBPARTITION obj_ref__93_0_2,
            SUBPARTITION obj_ref__93_0_3,
            SUBPARTITION obj_ref__93_0_4,
            SUBPARTITION obj_ref__93_0_5,
            SUBPARTITION obj_ref__93_0_6,
            SUBPARTITION obj_ref__93_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__94`;
CREATE TABLE `obj_ref__94` (
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
  INDEX `obj_ref__94_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__94_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__94_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__94_0_0,
            SUBPARTITION obj_ref__94_0_1,
            SUBPARTITION obj_ref__94_0_2,
            SUBPARTITION obj_ref__94_0_3,
            SUBPARTITION obj_ref__94_0_4,
            SUBPARTITION obj_ref__94_0_5,
            SUBPARTITION obj_ref__94_0_6,
            SUBPARTITION obj_ref__94_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__95`;
CREATE TABLE `obj_ref__95` (
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
  INDEX `obj_ref__95_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__95_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__95_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__95_0_0,
            SUBPARTITION obj_ref__95_0_1,
            SUBPARTITION obj_ref__95_0_2,
            SUBPARTITION obj_ref__95_0_3,
            SUBPARTITION obj_ref__95_0_4,
            SUBPARTITION obj_ref__95_0_5,
            SUBPARTITION obj_ref__95_0_6,
            SUBPARTITION obj_ref__95_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__96`;
CREATE TABLE `obj_ref__96` (
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
  INDEX `obj_ref__96_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__96_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__96_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__96_0_0,
            SUBPARTITION obj_ref__96_0_1,
            SUBPARTITION obj_ref__96_0_2,
            SUBPARTITION obj_ref__96_0_3,
            SUBPARTITION obj_ref__96_0_4,
            SUBPARTITION obj_ref__96_0_5,
            SUBPARTITION obj_ref__96_0_6,
            SUBPARTITION obj_ref__96_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__97`;
CREATE TABLE `obj_ref__97` (
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
  INDEX `obj_ref__97_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__97_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__97_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__97_0_0,
            SUBPARTITION obj_ref__97_0_1,
            SUBPARTITION obj_ref__97_0_2,
            SUBPARTITION obj_ref__97_0_3,
            SUBPARTITION obj_ref__97_0_4,
            SUBPARTITION obj_ref__97_0_5,
            SUBPARTITION obj_ref__97_0_6,
            SUBPARTITION obj_ref__97_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__98`;
CREATE TABLE `obj_ref__98` (
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
  INDEX `obj_ref__98_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__98_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__98_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__98_0_0,
            SUBPARTITION obj_ref__98_0_1,
            SUBPARTITION obj_ref__98_0_2,
            SUBPARTITION obj_ref__98_0_3,
            SUBPARTITION obj_ref__98_0_4,
            SUBPARTITION obj_ref__98_0_5,
            SUBPARTITION obj_ref__98_0_6,
            SUBPARTITION obj_ref__98_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__99`;
CREATE TABLE `obj_ref__99` (
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
  INDEX `obj_ref__99_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__99_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__99_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__99_0_0,
            SUBPARTITION obj_ref__99_0_1,
            SUBPARTITION obj_ref__99_0_2,
            SUBPARTITION obj_ref__99_0_3,
            SUBPARTITION obj_ref__99_0_4,
            SUBPARTITION obj_ref__99_0_5,
            SUBPARTITION obj_ref__99_0_6,
            SUBPARTITION obj_ref__99_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__100`;
CREATE TABLE `obj_ref__100` (
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
  INDEX `obj_ref__100_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__100_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__100_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__100_0_0,
            SUBPARTITION obj_ref__100_0_1,
            SUBPARTITION obj_ref__100_0_2,
            SUBPARTITION obj_ref__100_0_3,
            SUBPARTITION obj_ref__100_0_4,
            SUBPARTITION obj_ref__100_0_5,
            SUBPARTITION obj_ref__100_0_6,
            SUBPARTITION obj_ref__100_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__101`;
CREATE TABLE `obj_ref__101` (
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
  INDEX `obj_ref__101_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__101_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__101_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__101_0_0,
            SUBPARTITION obj_ref__101_0_1,
            SUBPARTITION obj_ref__101_0_2,
            SUBPARTITION obj_ref__101_0_3,
            SUBPARTITION obj_ref__101_0_4,
            SUBPARTITION obj_ref__101_0_5,
            SUBPARTITION obj_ref__101_0_6,
            SUBPARTITION obj_ref__101_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__102`;
CREATE TABLE `obj_ref__102` (
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
  INDEX `obj_ref__102_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__102_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__102_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__102_0_0,
            SUBPARTITION obj_ref__102_0_1,
            SUBPARTITION obj_ref__102_0_2,
            SUBPARTITION obj_ref__102_0_3,
            SUBPARTITION obj_ref__102_0_4,
            SUBPARTITION obj_ref__102_0_5,
            SUBPARTITION obj_ref__102_0_6,
            SUBPARTITION obj_ref__102_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__103`;
CREATE TABLE `obj_ref__103` (
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
  INDEX `obj_ref__103_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__103_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__103_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__103_0_0,
            SUBPARTITION obj_ref__103_0_1,
            SUBPARTITION obj_ref__103_0_2,
            SUBPARTITION obj_ref__103_0_3,
            SUBPARTITION obj_ref__103_0_4,
            SUBPARTITION obj_ref__103_0_5,
            SUBPARTITION obj_ref__103_0_6,
            SUBPARTITION obj_ref__103_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__104`;
CREATE TABLE `obj_ref__104` (
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
  INDEX `obj_ref__104_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__104_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__104_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__104_0_0,
            SUBPARTITION obj_ref__104_0_1,
            SUBPARTITION obj_ref__104_0_2,
            SUBPARTITION obj_ref__104_0_3,
            SUBPARTITION obj_ref__104_0_4,
            SUBPARTITION obj_ref__104_0_5,
            SUBPARTITION obj_ref__104_0_6,
            SUBPARTITION obj_ref__104_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__105`;
CREATE TABLE `obj_ref__105` (
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
  INDEX `obj_ref__105_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__105_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__105_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__105_0_0,
            SUBPARTITION obj_ref__105_0_1,
            SUBPARTITION obj_ref__105_0_2,
            SUBPARTITION obj_ref__105_0_3,
            SUBPARTITION obj_ref__105_0_4,
            SUBPARTITION obj_ref__105_0_5,
            SUBPARTITION obj_ref__105_0_6,
            SUBPARTITION obj_ref__105_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__106`;
CREATE TABLE `obj_ref__106` (
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
  INDEX `obj_ref__106_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__106_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__106_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__106_0_0,
            SUBPARTITION obj_ref__106_0_1,
            SUBPARTITION obj_ref__106_0_2,
            SUBPARTITION obj_ref__106_0_3,
            SUBPARTITION obj_ref__106_0_4,
            SUBPARTITION obj_ref__106_0_5,
            SUBPARTITION obj_ref__106_0_6,
            SUBPARTITION obj_ref__106_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__107`;
CREATE TABLE `obj_ref__107` (
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
  INDEX `obj_ref__107_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__107_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__107_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__107_0_0,
            SUBPARTITION obj_ref__107_0_1,
            SUBPARTITION obj_ref__107_0_2,
            SUBPARTITION obj_ref__107_0_3,
            SUBPARTITION obj_ref__107_0_4,
            SUBPARTITION obj_ref__107_0_5,
            SUBPARTITION obj_ref__107_0_6,
            SUBPARTITION obj_ref__107_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__108`;
CREATE TABLE `obj_ref__108` (
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
  INDEX `obj_ref__108_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__108_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__108_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__108_0_0,
            SUBPARTITION obj_ref__108_0_1,
            SUBPARTITION obj_ref__108_0_2,
            SUBPARTITION obj_ref__108_0_3,
            SUBPARTITION obj_ref__108_0_4,
            SUBPARTITION obj_ref__108_0_5,
            SUBPARTITION obj_ref__108_0_6,
            SUBPARTITION obj_ref__108_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__109`;
CREATE TABLE `obj_ref__109` (
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
  INDEX `obj_ref__109_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__109_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__109_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__109_0_0,
            SUBPARTITION obj_ref__109_0_1,
            SUBPARTITION obj_ref__109_0_2,
            SUBPARTITION obj_ref__109_0_3,
            SUBPARTITION obj_ref__109_0_4,
            SUBPARTITION obj_ref__109_0_5,
            SUBPARTITION obj_ref__109_0_6,
            SUBPARTITION obj_ref__109_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__110`;
CREATE TABLE `obj_ref__110` (
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
  INDEX `obj_ref__110_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__110_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__110_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__110_0_0,
            SUBPARTITION obj_ref__110_0_1,
            SUBPARTITION obj_ref__110_0_2,
            SUBPARTITION obj_ref__110_0_3,
            SUBPARTITION obj_ref__110_0_4,
            SUBPARTITION obj_ref__110_0_5,
            SUBPARTITION obj_ref__110_0_6,
            SUBPARTITION obj_ref__110_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__111`;
CREATE TABLE `obj_ref__111` (
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
  INDEX `obj_ref__111_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__111_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__111_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__111_0_0,
            SUBPARTITION obj_ref__111_0_1,
            SUBPARTITION obj_ref__111_0_2,
            SUBPARTITION obj_ref__111_0_3,
            SUBPARTITION obj_ref__111_0_4,
            SUBPARTITION obj_ref__111_0_5,
            SUBPARTITION obj_ref__111_0_6,
            SUBPARTITION obj_ref__111_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__112`;
CREATE TABLE `obj_ref__112` (
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
  INDEX `obj_ref__112_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__112_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__112_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__112_0_0,
            SUBPARTITION obj_ref__112_0_1,
            SUBPARTITION obj_ref__112_0_2,
            SUBPARTITION obj_ref__112_0_3,
            SUBPARTITION obj_ref__112_0_4,
            SUBPARTITION obj_ref__112_0_5,
            SUBPARTITION obj_ref__112_0_6,
            SUBPARTITION obj_ref__112_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__113`;
CREATE TABLE `obj_ref__113` (
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
  INDEX `obj_ref__113_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__113_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__113_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__113_0_0,
            SUBPARTITION obj_ref__113_0_1,
            SUBPARTITION obj_ref__113_0_2,
            SUBPARTITION obj_ref__113_0_3,
            SUBPARTITION obj_ref__113_0_4,
            SUBPARTITION obj_ref__113_0_5,
            SUBPARTITION obj_ref__113_0_6,
            SUBPARTITION obj_ref__113_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__114`;
CREATE TABLE `obj_ref__114` (
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
  INDEX `obj_ref__114_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__114_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__114_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__114_0_0,
            SUBPARTITION obj_ref__114_0_1,
            SUBPARTITION obj_ref__114_0_2,
            SUBPARTITION obj_ref__114_0_3,
            SUBPARTITION obj_ref__114_0_4,
            SUBPARTITION obj_ref__114_0_5,
            SUBPARTITION obj_ref__114_0_6,
            SUBPARTITION obj_ref__114_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__115`;
CREATE TABLE `obj_ref__115` (
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
  INDEX `obj_ref__115_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__115_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__115_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__115_0_0,
            SUBPARTITION obj_ref__115_0_1,
            SUBPARTITION obj_ref__115_0_2,
            SUBPARTITION obj_ref__115_0_3,
            SUBPARTITION obj_ref__115_0_4,
            SUBPARTITION obj_ref__115_0_5,
            SUBPARTITION obj_ref__115_0_6,
            SUBPARTITION obj_ref__115_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__116`;
CREATE TABLE `obj_ref__116` (
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
  INDEX `obj_ref__116_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__116_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__116_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__116_0_0,
            SUBPARTITION obj_ref__116_0_1,
            SUBPARTITION obj_ref__116_0_2,
            SUBPARTITION obj_ref__116_0_3,
            SUBPARTITION obj_ref__116_0_4,
            SUBPARTITION obj_ref__116_0_5,
            SUBPARTITION obj_ref__116_0_6,
            SUBPARTITION obj_ref__116_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__117`;
CREATE TABLE `obj_ref__117` (
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
  INDEX `obj_ref__117_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__117_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__117_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__117_0_0,
            SUBPARTITION obj_ref__117_0_1,
            SUBPARTITION obj_ref__117_0_2,
            SUBPARTITION obj_ref__117_0_3,
            SUBPARTITION obj_ref__117_0_4,
            SUBPARTITION obj_ref__117_0_5,
            SUBPARTITION obj_ref__117_0_6,
            SUBPARTITION obj_ref__117_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__118`;
CREATE TABLE `obj_ref__118` (
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
  INDEX `obj_ref__118_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__118_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__118_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__118_0_0,
            SUBPARTITION obj_ref__118_0_1,
            SUBPARTITION obj_ref__118_0_2,
            SUBPARTITION obj_ref__118_0_3,
            SUBPARTITION obj_ref__118_0_4,
            SUBPARTITION obj_ref__118_0_5,
            SUBPARTITION obj_ref__118_0_6,
            SUBPARTITION obj_ref__118_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__119`;
CREATE TABLE `obj_ref__119` (
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
  INDEX `obj_ref__119_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__119_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__119_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__119_0_0,
            SUBPARTITION obj_ref__119_0_1,
            SUBPARTITION obj_ref__119_0_2,
            SUBPARTITION obj_ref__119_0_3,
            SUBPARTITION obj_ref__119_0_4,
            SUBPARTITION obj_ref__119_0_5,
            SUBPARTITION obj_ref__119_0_6,
            SUBPARTITION obj_ref__119_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__120`;
CREATE TABLE `obj_ref__120` (
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
  INDEX `obj_ref__120_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__120_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__120_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__120_0_0,
            SUBPARTITION obj_ref__120_0_1,
            SUBPARTITION obj_ref__120_0_2,
            SUBPARTITION obj_ref__120_0_3,
            SUBPARTITION obj_ref__120_0_4,
            SUBPARTITION obj_ref__120_0_5,
            SUBPARTITION obj_ref__120_0_6,
            SUBPARTITION obj_ref__120_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__121`;
CREATE TABLE `obj_ref__121` (
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
  INDEX `obj_ref__121_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__121_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__121_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__121_0_0,
            SUBPARTITION obj_ref__121_0_1,
            SUBPARTITION obj_ref__121_0_2,
            SUBPARTITION obj_ref__121_0_3,
            SUBPARTITION obj_ref__121_0_4,
            SUBPARTITION obj_ref__121_0_5,
            SUBPARTITION obj_ref__121_0_6,
            SUBPARTITION obj_ref__121_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__122`;
CREATE TABLE `obj_ref__122` (
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
  INDEX `obj_ref__122_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__122_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__122_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__122_0_0,
            SUBPARTITION obj_ref__122_0_1,
            SUBPARTITION obj_ref__122_0_2,
            SUBPARTITION obj_ref__122_0_3,
            SUBPARTITION obj_ref__122_0_4,
            SUBPARTITION obj_ref__122_0_5,
            SUBPARTITION obj_ref__122_0_6,
            SUBPARTITION obj_ref__122_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__123`;
CREATE TABLE `obj_ref__123` (
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
  INDEX `obj_ref__123_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__123_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__123_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__123_0_0,
            SUBPARTITION obj_ref__123_0_1,
            SUBPARTITION obj_ref__123_0_2,
            SUBPARTITION obj_ref__123_0_3,
            SUBPARTITION obj_ref__123_0_4,
            SUBPARTITION obj_ref__123_0_5,
            SUBPARTITION obj_ref__123_0_6,
            SUBPARTITION obj_ref__123_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__124`;
CREATE TABLE `obj_ref__124` (
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
  INDEX `obj_ref__124_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__124_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__124_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__124_0_0,
            SUBPARTITION obj_ref__124_0_1,
            SUBPARTITION obj_ref__124_0_2,
            SUBPARTITION obj_ref__124_0_3,
            SUBPARTITION obj_ref__124_0_4,
            SUBPARTITION obj_ref__124_0_5,
            SUBPARTITION obj_ref__124_0_6,
            SUBPARTITION obj_ref__124_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__125`;
CREATE TABLE `obj_ref__125` (
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
  INDEX `obj_ref__125_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__125_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__125_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__125_0_0,
            SUBPARTITION obj_ref__125_0_1,
            SUBPARTITION obj_ref__125_0_2,
            SUBPARTITION obj_ref__125_0_3,
            SUBPARTITION obj_ref__125_0_4,
            SUBPARTITION obj_ref__125_0_5,
            SUBPARTITION obj_ref__125_0_6,
            SUBPARTITION obj_ref__125_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__126`;
CREATE TABLE `obj_ref__126` (
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
  INDEX `obj_ref__126_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__126_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__126_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__126_0_0,
            SUBPARTITION obj_ref__126_0_1,
            SUBPARTITION obj_ref__126_0_2,
            SUBPARTITION obj_ref__126_0_3,
            SUBPARTITION obj_ref__126_0_4,
            SUBPARTITION obj_ref__126_0_5,
            SUBPARTITION obj_ref__126_0_6,
            SUBPARTITION obj_ref__126_0_7
        )
    )

;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `obj_ref__127`;
CREATE TABLE `obj_ref__127` (
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
  INDEX `obj_ref__127_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__127_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_ref__127_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_ref__127_0_0,
            SUBPARTITION obj_ref__127_0_1,
            SUBPARTITION obj_ref__127_0_2,
            SUBPARTITION obj_ref__127_0_3,
            SUBPARTITION obj_ref__127_0_4,
            SUBPARTITION obj_ref__127_0_5,
            SUBPARTITION obj_ref__127_0_6,
            SUBPARTITION obj_ref__127_0_7
        )
    )

;
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
  INDEX `obj_ref__MTP_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__MTP_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


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
    )

;
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
  INDEX `obj_ref__USER_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__USER_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)


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
    )

;
