/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl`;
CREATE TABLE `obj_unique_dbl` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_dbl_0_0,
            SUBPARTITION obj_unique_dbl_0_1,
            SUBPARTITION obj_unique_dbl_0_2,
            SUBPARTITION obj_unique_dbl_0_3,
            SUBPARTITION obj_unique_dbl_0_4,
            SUBPARTITION obj_unique_dbl_0_5,
            SUBPARTITION obj_unique_dbl_0_6,
            SUBPARTITION obj_unique_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__1`;
CREATE TABLE `obj_unique_dbl__1` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__1_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__1_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__1_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__1_dbl_0_0,
            SUBPARTITION obj_unique__1_dbl_0_1,
            SUBPARTITION obj_unique__1_dbl_0_2,
            SUBPARTITION obj_unique__1_dbl_0_3,
            SUBPARTITION obj_unique__1_dbl_0_4,
            SUBPARTITION obj_unique__1_dbl_0_5,
            SUBPARTITION obj_unique__1_dbl_0_6,
            SUBPARTITION obj_unique__1_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__2`;
CREATE TABLE `obj_unique_dbl__2` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__2_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__2_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__2_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__2_dbl_0_0,
            SUBPARTITION obj_unique__2_dbl_0_1,
            SUBPARTITION obj_unique__2_dbl_0_2,
            SUBPARTITION obj_unique__2_dbl_0_3,
            SUBPARTITION obj_unique__2_dbl_0_4,
            SUBPARTITION obj_unique__2_dbl_0_5,
            SUBPARTITION obj_unique__2_dbl_0_6,
            SUBPARTITION obj_unique__2_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__3`;
CREATE TABLE `obj_unique_dbl__3` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__3_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__3_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__3_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__3_dbl_0_0,
            SUBPARTITION obj_unique__3_dbl_0_1,
            SUBPARTITION obj_unique__3_dbl_0_2,
            SUBPARTITION obj_unique__3_dbl_0_3,
            SUBPARTITION obj_unique__3_dbl_0_4,
            SUBPARTITION obj_unique__3_dbl_0_5,
            SUBPARTITION obj_unique__3_dbl_0_6,
            SUBPARTITION obj_unique__3_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__4`;
CREATE TABLE `obj_unique_dbl__4` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__4_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__4_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__4_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__4_dbl_0_0,
            SUBPARTITION obj_unique__4_dbl_0_1,
            SUBPARTITION obj_unique__4_dbl_0_2,
            SUBPARTITION obj_unique__4_dbl_0_3,
            SUBPARTITION obj_unique__4_dbl_0_4,
            SUBPARTITION obj_unique__4_dbl_0_5,
            SUBPARTITION obj_unique__4_dbl_0_6,
            SUBPARTITION obj_unique__4_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__5`;
CREATE TABLE `obj_unique_dbl__5` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__5_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__5_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__5_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__5_dbl_0_0,
            SUBPARTITION obj_unique__5_dbl_0_1,
            SUBPARTITION obj_unique__5_dbl_0_2,
            SUBPARTITION obj_unique__5_dbl_0_3,
            SUBPARTITION obj_unique__5_dbl_0_4,
            SUBPARTITION obj_unique__5_dbl_0_5,
            SUBPARTITION obj_unique__5_dbl_0_6,
            SUBPARTITION obj_unique__5_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__6`;
CREATE TABLE `obj_unique_dbl__6` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__6_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__6_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__6_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__6_dbl_0_0,
            SUBPARTITION obj_unique__6_dbl_0_1,
            SUBPARTITION obj_unique__6_dbl_0_2,
            SUBPARTITION obj_unique__6_dbl_0_3,
            SUBPARTITION obj_unique__6_dbl_0_4,
            SUBPARTITION obj_unique__6_dbl_0_5,
            SUBPARTITION obj_unique__6_dbl_0_6,
            SUBPARTITION obj_unique__6_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__7`;
CREATE TABLE `obj_unique_dbl__7` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__7_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__7_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__7_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__7_dbl_0_0,
            SUBPARTITION obj_unique__7_dbl_0_1,
            SUBPARTITION obj_unique__7_dbl_0_2,
            SUBPARTITION obj_unique__7_dbl_0_3,
            SUBPARTITION obj_unique__7_dbl_0_4,
            SUBPARTITION obj_unique__7_dbl_0_5,
            SUBPARTITION obj_unique__7_dbl_0_6,
            SUBPARTITION obj_unique__7_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__8`;
CREATE TABLE `obj_unique_dbl__8` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__8_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__8_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__8_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__8_dbl_0_0,
            SUBPARTITION obj_unique__8_dbl_0_1,
            SUBPARTITION obj_unique__8_dbl_0_2,
            SUBPARTITION obj_unique__8_dbl_0_3,
            SUBPARTITION obj_unique__8_dbl_0_4,
            SUBPARTITION obj_unique__8_dbl_0_5,
            SUBPARTITION obj_unique__8_dbl_0_6,
            SUBPARTITION obj_unique__8_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__9`;
CREATE TABLE `obj_unique_dbl__9` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__9_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__9_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__9_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__9_dbl_0_0,
            SUBPARTITION obj_unique__9_dbl_0_1,
            SUBPARTITION obj_unique__9_dbl_0_2,
            SUBPARTITION obj_unique__9_dbl_0_3,
            SUBPARTITION obj_unique__9_dbl_0_4,
            SUBPARTITION obj_unique__9_dbl_0_5,
            SUBPARTITION obj_unique__9_dbl_0_6,
            SUBPARTITION obj_unique__9_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__10`;
CREATE TABLE `obj_unique_dbl__10` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__10_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__10_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__10_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__10_dbl_0_0,
            SUBPARTITION obj_unique__10_dbl_0_1,
            SUBPARTITION obj_unique__10_dbl_0_2,
            SUBPARTITION obj_unique__10_dbl_0_3,
            SUBPARTITION obj_unique__10_dbl_0_4,
            SUBPARTITION obj_unique__10_dbl_0_5,
            SUBPARTITION obj_unique__10_dbl_0_6,
            SUBPARTITION obj_unique__10_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__11`;
CREATE TABLE `obj_unique_dbl__11` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__11_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__11_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__11_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__11_dbl_0_0,
            SUBPARTITION obj_unique__11_dbl_0_1,
            SUBPARTITION obj_unique__11_dbl_0_2,
            SUBPARTITION obj_unique__11_dbl_0_3,
            SUBPARTITION obj_unique__11_dbl_0_4,
            SUBPARTITION obj_unique__11_dbl_0_5,
            SUBPARTITION obj_unique__11_dbl_0_6,
            SUBPARTITION obj_unique__11_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__12`;
CREATE TABLE `obj_unique_dbl__12` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__12_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__12_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__12_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__12_dbl_0_0,
            SUBPARTITION obj_unique__12_dbl_0_1,
            SUBPARTITION obj_unique__12_dbl_0_2,
            SUBPARTITION obj_unique__12_dbl_0_3,
            SUBPARTITION obj_unique__12_dbl_0_4,
            SUBPARTITION obj_unique__12_dbl_0_5,
            SUBPARTITION obj_unique__12_dbl_0_6,
            SUBPARTITION obj_unique__12_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__13`;
CREATE TABLE `obj_unique_dbl__13` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__13_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__13_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__13_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__13_dbl_0_0,
            SUBPARTITION obj_unique__13_dbl_0_1,
            SUBPARTITION obj_unique__13_dbl_0_2,
            SUBPARTITION obj_unique__13_dbl_0_3,
            SUBPARTITION obj_unique__13_dbl_0_4,
            SUBPARTITION obj_unique__13_dbl_0_5,
            SUBPARTITION obj_unique__13_dbl_0_6,
            SUBPARTITION obj_unique__13_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__14`;
CREATE TABLE `obj_unique_dbl__14` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__14_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__14_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__14_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__14_dbl_0_0,
            SUBPARTITION obj_unique__14_dbl_0_1,
            SUBPARTITION obj_unique__14_dbl_0_2,
            SUBPARTITION obj_unique__14_dbl_0_3,
            SUBPARTITION obj_unique__14_dbl_0_4,
            SUBPARTITION obj_unique__14_dbl_0_5,
            SUBPARTITION obj_unique__14_dbl_0_6,
            SUBPARTITION obj_unique__14_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__15`;
CREATE TABLE `obj_unique_dbl__15` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__15_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__15_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__15_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__15_dbl_0_0,
            SUBPARTITION obj_unique__15_dbl_0_1,
            SUBPARTITION obj_unique__15_dbl_0_2,
            SUBPARTITION obj_unique__15_dbl_0_3,
            SUBPARTITION obj_unique__15_dbl_0_4,
            SUBPARTITION obj_unique__15_dbl_0_5,
            SUBPARTITION obj_unique__15_dbl_0_6,
            SUBPARTITION obj_unique__15_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__16`;
CREATE TABLE `obj_unique_dbl__16` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__16_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__16_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__16_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__16_dbl_0_0,
            SUBPARTITION obj_unique__16_dbl_0_1,
            SUBPARTITION obj_unique__16_dbl_0_2,
            SUBPARTITION obj_unique__16_dbl_0_3,
            SUBPARTITION obj_unique__16_dbl_0_4,
            SUBPARTITION obj_unique__16_dbl_0_5,
            SUBPARTITION obj_unique__16_dbl_0_6,
            SUBPARTITION obj_unique__16_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__17`;
CREATE TABLE `obj_unique_dbl__17` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__17_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__17_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__17_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__17_dbl_0_0,
            SUBPARTITION obj_unique__17_dbl_0_1,
            SUBPARTITION obj_unique__17_dbl_0_2,
            SUBPARTITION obj_unique__17_dbl_0_3,
            SUBPARTITION obj_unique__17_dbl_0_4,
            SUBPARTITION obj_unique__17_dbl_0_5,
            SUBPARTITION obj_unique__17_dbl_0_6,
            SUBPARTITION obj_unique__17_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__18`;
CREATE TABLE `obj_unique_dbl__18` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__18_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__18_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__18_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__18_dbl_0_0,
            SUBPARTITION obj_unique__18_dbl_0_1,
            SUBPARTITION obj_unique__18_dbl_0_2,
            SUBPARTITION obj_unique__18_dbl_0_3,
            SUBPARTITION obj_unique__18_dbl_0_4,
            SUBPARTITION obj_unique__18_dbl_0_5,
            SUBPARTITION obj_unique__18_dbl_0_6,
            SUBPARTITION obj_unique__18_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__19`;
CREATE TABLE `obj_unique_dbl__19` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__19_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__19_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__19_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__19_dbl_0_0,
            SUBPARTITION obj_unique__19_dbl_0_1,
            SUBPARTITION obj_unique__19_dbl_0_2,
            SUBPARTITION obj_unique__19_dbl_0_3,
            SUBPARTITION obj_unique__19_dbl_0_4,
            SUBPARTITION obj_unique__19_dbl_0_5,
            SUBPARTITION obj_unique__19_dbl_0_6,
            SUBPARTITION obj_unique__19_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__20`;
CREATE TABLE `obj_unique_dbl__20` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__20_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__20_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__20_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__20_dbl_0_0,
            SUBPARTITION obj_unique__20_dbl_0_1,
            SUBPARTITION obj_unique__20_dbl_0_2,
            SUBPARTITION obj_unique__20_dbl_0_3,
            SUBPARTITION obj_unique__20_dbl_0_4,
            SUBPARTITION obj_unique__20_dbl_0_5,
            SUBPARTITION obj_unique__20_dbl_0_6,
            SUBPARTITION obj_unique__20_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__21`;
CREATE TABLE `obj_unique_dbl__21` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__21_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__21_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__21_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__21_dbl_0_0,
            SUBPARTITION obj_unique__21_dbl_0_1,
            SUBPARTITION obj_unique__21_dbl_0_2,
            SUBPARTITION obj_unique__21_dbl_0_3,
            SUBPARTITION obj_unique__21_dbl_0_4,
            SUBPARTITION obj_unique__21_dbl_0_5,
            SUBPARTITION obj_unique__21_dbl_0_6,
            SUBPARTITION obj_unique__21_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__22`;
CREATE TABLE `obj_unique_dbl__22` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__22_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__22_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__22_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__22_dbl_0_0,
            SUBPARTITION obj_unique__22_dbl_0_1,
            SUBPARTITION obj_unique__22_dbl_0_2,
            SUBPARTITION obj_unique__22_dbl_0_3,
            SUBPARTITION obj_unique__22_dbl_0_4,
            SUBPARTITION obj_unique__22_dbl_0_5,
            SUBPARTITION obj_unique__22_dbl_0_6,
            SUBPARTITION obj_unique__22_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__23`;
CREATE TABLE `obj_unique_dbl__23` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__23_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__23_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__23_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__23_dbl_0_0,
            SUBPARTITION obj_unique__23_dbl_0_1,
            SUBPARTITION obj_unique__23_dbl_0_2,
            SUBPARTITION obj_unique__23_dbl_0_3,
            SUBPARTITION obj_unique__23_dbl_0_4,
            SUBPARTITION obj_unique__23_dbl_0_5,
            SUBPARTITION obj_unique__23_dbl_0_6,
            SUBPARTITION obj_unique__23_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__24`;
CREATE TABLE `obj_unique_dbl__24` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__24_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__24_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__24_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__24_dbl_0_0,
            SUBPARTITION obj_unique__24_dbl_0_1,
            SUBPARTITION obj_unique__24_dbl_0_2,
            SUBPARTITION obj_unique__24_dbl_0_3,
            SUBPARTITION obj_unique__24_dbl_0_4,
            SUBPARTITION obj_unique__24_dbl_0_5,
            SUBPARTITION obj_unique__24_dbl_0_6,
            SUBPARTITION obj_unique__24_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__25`;
CREATE TABLE `obj_unique_dbl__25` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__25_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__25_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__25_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__25_dbl_0_0,
            SUBPARTITION obj_unique__25_dbl_0_1,
            SUBPARTITION obj_unique__25_dbl_0_2,
            SUBPARTITION obj_unique__25_dbl_0_3,
            SUBPARTITION obj_unique__25_dbl_0_4,
            SUBPARTITION obj_unique__25_dbl_0_5,
            SUBPARTITION obj_unique__25_dbl_0_6,
            SUBPARTITION obj_unique__25_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__26`;
CREATE TABLE `obj_unique_dbl__26` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__26_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__26_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__26_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__26_dbl_0_0,
            SUBPARTITION obj_unique__26_dbl_0_1,
            SUBPARTITION obj_unique__26_dbl_0_2,
            SUBPARTITION obj_unique__26_dbl_0_3,
            SUBPARTITION obj_unique__26_dbl_0_4,
            SUBPARTITION obj_unique__26_dbl_0_5,
            SUBPARTITION obj_unique__26_dbl_0_6,
            SUBPARTITION obj_unique__26_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__27`;
CREATE TABLE `obj_unique_dbl__27` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__27_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__27_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__27_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__27_dbl_0_0,
            SUBPARTITION obj_unique__27_dbl_0_1,
            SUBPARTITION obj_unique__27_dbl_0_2,
            SUBPARTITION obj_unique__27_dbl_0_3,
            SUBPARTITION obj_unique__27_dbl_0_4,
            SUBPARTITION obj_unique__27_dbl_0_5,
            SUBPARTITION obj_unique__27_dbl_0_6,
            SUBPARTITION obj_unique__27_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__28`;
CREATE TABLE `obj_unique_dbl__28` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__28_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__28_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__28_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__28_dbl_0_0,
            SUBPARTITION obj_unique__28_dbl_0_1,
            SUBPARTITION obj_unique__28_dbl_0_2,
            SUBPARTITION obj_unique__28_dbl_0_3,
            SUBPARTITION obj_unique__28_dbl_0_4,
            SUBPARTITION obj_unique__28_dbl_0_5,
            SUBPARTITION obj_unique__28_dbl_0_6,
            SUBPARTITION obj_unique__28_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__29`;
CREATE TABLE `obj_unique_dbl__29` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__29_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__29_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__29_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__29_dbl_0_0,
            SUBPARTITION obj_unique__29_dbl_0_1,
            SUBPARTITION obj_unique__29_dbl_0_2,
            SUBPARTITION obj_unique__29_dbl_0_3,
            SUBPARTITION obj_unique__29_dbl_0_4,
            SUBPARTITION obj_unique__29_dbl_0_5,
            SUBPARTITION obj_unique__29_dbl_0_6,
            SUBPARTITION obj_unique__29_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__30`;
CREATE TABLE `obj_unique_dbl__30` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__30_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__30_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__30_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__30_dbl_0_0,
            SUBPARTITION obj_unique__30_dbl_0_1,
            SUBPARTITION obj_unique__30_dbl_0_2,
            SUBPARTITION obj_unique__30_dbl_0_3,
            SUBPARTITION obj_unique__30_dbl_0_4,
            SUBPARTITION obj_unique__30_dbl_0_5,
            SUBPARTITION obj_unique__30_dbl_0_6,
            SUBPARTITION obj_unique__30_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__31`;
CREATE TABLE `obj_unique_dbl__31` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__31_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__31_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__31_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__31_dbl_0_0,
            SUBPARTITION obj_unique__31_dbl_0_1,
            SUBPARTITION obj_unique__31_dbl_0_2,
            SUBPARTITION obj_unique__31_dbl_0_3,
            SUBPARTITION obj_unique__31_dbl_0_4,
            SUBPARTITION obj_unique__31_dbl_0_5,
            SUBPARTITION obj_unique__31_dbl_0_6,
            SUBPARTITION obj_unique__31_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__32`;
CREATE TABLE `obj_unique_dbl__32` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__32_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__32_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__32_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__32_dbl_0_0,
            SUBPARTITION obj_unique__32_dbl_0_1,
            SUBPARTITION obj_unique__32_dbl_0_2,
            SUBPARTITION obj_unique__32_dbl_0_3,
            SUBPARTITION obj_unique__32_dbl_0_4,
            SUBPARTITION obj_unique__32_dbl_0_5,
            SUBPARTITION obj_unique__32_dbl_0_6,
            SUBPARTITION obj_unique__32_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__33`;
CREATE TABLE `obj_unique_dbl__33` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__33_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__33_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__33_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__33_dbl_0_0,
            SUBPARTITION obj_unique__33_dbl_0_1,
            SUBPARTITION obj_unique__33_dbl_0_2,
            SUBPARTITION obj_unique__33_dbl_0_3,
            SUBPARTITION obj_unique__33_dbl_0_4,
            SUBPARTITION obj_unique__33_dbl_0_5,
            SUBPARTITION obj_unique__33_dbl_0_6,
            SUBPARTITION obj_unique__33_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__34`;
CREATE TABLE `obj_unique_dbl__34` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__34_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__34_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__34_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__34_dbl_0_0,
            SUBPARTITION obj_unique__34_dbl_0_1,
            SUBPARTITION obj_unique__34_dbl_0_2,
            SUBPARTITION obj_unique__34_dbl_0_3,
            SUBPARTITION obj_unique__34_dbl_0_4,
            SUBPARTITION obj_unique__34_dbl_0_5,
            SUBPARTITION obj_unique__34_dbl_0_6,
            SUBPARTITION obj_unique__34_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__35`;
CREATE TABLE `obj_unique_dbl__35` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__35_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__35_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__35_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__35_dbl_0_0,
            SUBPARTITION obj_unique__35_dbl_0_1,
            SUBPARTITION obj_unique__35_dbl_0_2,
            SUBPARTITION obj_unique__35_dbl_0_3,
            SUBPARTITION obj_unique__35_dbl_0_4,
            SUBPARTITION obj_unique__35_dbl_0_5,
            SUBPARTITION obj_unique__35_dbl_0_6,
            SUBPARTITION obj_unique__35_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__36`;
CREATE TABLE `obj_unique_dbl__36` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__36_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__36_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__36_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__36_dbl_0_0,
            SUBPARTITION obj_unique__36_dbl_0_1,
            SUBPARTITION obj_unique__36_dbl_0_2,
            SUBPARTITION obj_unique__36_dbl_0_3,
            SUBPARTITION obj_unique__36_dbl_0_4,
            SUBPARTITION obj_unique__36_dbl_0_5,
            SUBPARTITION obj_unique__36_dbl_0_6,
            SUBPARTITION obj_unique__36_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__37`;
CREATE TABLE `obj_unique_dbl__37` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__37_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__37_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__37_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__37_dbl_0_0,
            SUBPARTITION obj_unique__37_dbl_0_1,
            SUBPARTITION obj_unique__37_dbl_0_2,
            SUBPARTITION obj_unique__37_dbl_0_3,
            SUBPARTITION obj_unique__37_dbl_0_4,
            SUBPARTITION obj_unique__37_dbl_0_5,
            SUBPARTITION obj_unique__37_dbl_0_6,
            SUBPARTITION obj_unique__37_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__38`;
CREATE TABLE `obj_unique_dbl__38` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__38_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__38_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__38_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__38_dbl_0_0,
            SUBPARTITION obj_unique__38_dbl_0_1,
            SUBPARTITION obj_unique__38_dbl_0_2,
            SUBPARTITION obj_unique__38_dbl_0_3,
            SUBPARTITION obj_unique__38_dbl_0_4,
            SUBPARTITION obj_unique__38_dbl_0_5,
            SUBPARTITION obj_unique__38_dbl_0_6,
            SUBPARTITION obj_unique__38_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__39`;
CREATE TABLE `obj_unique_dbl__39` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__39_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__39_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__39_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__39_dbl_0_0,
            SUBPARTITION obj_unique__39_dbl_0_1,
            SUBPARTITION obj_unique__39_dbl_0_2,
            SUBPARTITION obj_unique__39_dbl_0_3,
            SUBPARTITION obj_unique__39_dbl_0_4,
            SUBPARTITION obj_unique__39_dbl_0_5,
            SUBPARTITION obj_unique__39_dbl_0_6,
            SUBPARTITION obj_unique__39_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__40`;
CREATE TABLE `obj_unique_dbl__40` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__40_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__40_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__40_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__40_dbl_0_0,
            SUBPARTITION obj_unique__40_dbl_0_1,
            SUBPARTITION obj_unique__40_dbl_0_2,
            SUBPARTITION obj_unique__40_dbl_0_3,
            SUBPARTITION obj_unique__40_dbl_0_4,
            SUBPARTITION obj_unique__40_dbl_0_5,
            SUBPARTITION obj_unique__40_dbl_0_6,
            SUBPARTITION obj_unique__40_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__41`;
CREATE TABLE `obj_unique_dbl__41` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__41_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__41_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__41_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__41_dbl_0_0,
            SUBPARTITION obj_unique__41_dbl_0_1,
            SUBPARTITION obj_unique__41_dbl_0_2,
            SUBPARTITION obj_unique__41_dbl_0_3,
            SUBPARTITION obj_unique__41_dbl_0_4,
            SUBPARTITION obj_unique__41_dbl_0_5,
            SUBPARTITION obj_unique__41_dbl_0_6,
            SUBPARTITION obj_unique__41_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__42`;
CREATE TABLE `obj_unique_dbl__42` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__42_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__42_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__42_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__42_dbl_0_0,
            SUBPARTITION obj_unique__42_dbl_0_1,
            SUBPARTITION obj_unique__42_dbl_0_2,
            SUBPARTITION obj_unique__42_dbl_0_3,
            SUBPARTITION obj_unique__42_dbl_0_4,
            SUBPARTITION obj_unique__42_dbl_0_5,
            SUBPARTITION obj_unique__42_dbl_0_6,
            SUBPARTITION obj_unique__42_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__43`;
CREATE TABLE `obj_unique_dbl__43` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__43_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__43_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__43_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__43_dbl_0_0,
            SUBPARTITION obj_unique__43_dbl_0_1,
            SUBPARTITION obj_unique__43_dbl_0_2,
            SUBPARTITION obj_unique__43_dbl_0_3,
            SUBPARTITION obj_unique__43_dbl_0_4,
            SUBPARTITION obj_unique__43_dbl_0_5,
            SUBPARTITION obj_unique__43_dbl_0_6,
            SUBPARTITION obj_unique__43_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__44`;
CREATE TABLE `obj_unique_dbl__44` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__44_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__44_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__44_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__44_dbl_0_0,
            SUBPARTITION obj_unique__44_dbl_0_1,
            SUBPARTITION obj_unique__44_dbl_0_2,
            SUBPARTITION obj_unique__44_dbl_0_3,
            SUBPARTITION obj_unique__44_dbl_0_4,
            SUBPARTITION obj_unique__44_dbl_0_5,
            SUBPARTITION obj_unique__44_dbl_0_6,
            SUBPARTITION obj_unique__44_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__45`;
CREATE TABLE `obj_unique_dbl__45` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__45_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__45_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__45_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__45_dbl_0_0,
            SUBPARTITION obj_unique__45_dbl_0_1,
            SUBPARTITION obj_unique__45_dbl_0_2,
            SUBPARTITION obj_unique__45_dbl_0_3,
            SUBPARTITION obj_unique__45_dbl_0_4,
            SUBPARTITION obj_unique__45_dbl_0_5,
            SUBPARTITION obj_unique__45_dbl_0_6,
            SUBPARTITION obj_unique__45_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__46`;
CREATE TABLE `obj_unique_dbl__46` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__46_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__46_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__46_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__46_dbl_0_0,
            SUBPARTITION obj_unique__46_dbl_0_1,
            SUBPARTITION obj_unique__46_dbl_0_2,
            SUBPARTITION obj_unique__46_dbl_0_3,
            SUBPARTITION obj_unique__46_dbl_0_4,
            SUBPARTITION obj_unique__46_dbl_0_5,
            SUBPARTITION obj_unique__46_dbl_0_6,
            SUBPARTITION obj_unique__46_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__47`;
CREATE TABLE `obj_unique_dbl__47` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__47_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__47_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__47_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__47_dbl_0_0,
            SUBPARTITION obj_unique__47_dbl_0_1,
            SUBPARTITION obj_unique__47_dbl_0_2,
            SUBPARTITION obj_unique__47_dbl_0_3,
            SUBPARTITION obj_unique__47_dbl_0_4,
            SUBPARTITION obj_unique__47_dbl_0_5,
            SUBPARTITION obj_unique__47_dbl_0_6,
            SUBPARTITION obj_unique__47_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__48`;
CREATE TABLE `obj_unique_dbl__48` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__48_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__48_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__48_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__48_dbl_0_0,
            SUBPARTITION obj_unique__48_dbl_0_1,
            SUBPARTITION obj_unique__48_dbl_0_2,
            SUBPARTITION obj_unique__48_dbl_0_3,
            SUBPARTITION obj_unique__48_dbl_0_4,
            SUBPARTITION obj_unique__48_dbl_0_5,
            SUBPARTITION obj_unique__48_dbl_0_6,
            SUBPARTITION obj_unique__48_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__49`;
CREATE TABLE `obj_unique_dbl__49` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__49_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__49_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__49_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__49_dbl_0_0,
            SUBPARTITION obj_unique__49_dbl_0_1,
            SUBPARTITION obj_unique__49_dbl_0_2,
            SUBPARTITION obj_unique__49_dbl_0_3,
            SUBPARTITION obj_unique__49_dbl_0_4,
            SUBPARTITION obj_unique__49_dbl_0_5,
            SUBPARTITION obj_unique__49_dbl_0_6,
            SUBPARTITION obj_unique__49_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__50`;
CREATE TABLE `obj_unique_dbl__50` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__50_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__50_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__50_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__50_dbl_0_0,
            SUBPARTITION obj_unique__50_dbl_0_1,
            SUBPARTITION obj_unique__50_dbl_0_2,
            SUBPARTITION obj_unique__50_dbl_0_3,
            SUBPARTITION obj_unique__50_dbl_0_4,
            SUBPARTITION obj_unique__50_dbl_0_5,
            SUBPARTITION obj_unique__50_dbl_0_6,
            SUBPARTITION obj_unique__50_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__51`;
CREATE TABLE `obj_unique_dbl__51` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__51_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__51_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__51_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__51_dbl_0_0,
            SUBPARTITION obj_unique__51_dbl_0_1,
            SUBPARTITION obj_unique__51_dbl_0_2,
            SUBPARTITION obj_unique__51_dbl_0_3,
            SUBPARTITION obj_unique__51_dbl_0_4,
            SUBPARTITION obj_unique__51_dbl_0_5,
            SUBPARTITION obj_unique__51_dbl_0_6,
            SUBPARTITION obj_unique__51_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__52`;
CREATE TABLE `obj_unique_dbl__52` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__52_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__52_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__52_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__52_dbl_0_0,
            SUBPARTITION obj_unique__52_dbl_0_1,
            SUBPARTITION obj_unique__52_dbl_0_2,
            SUBPARTITION obj_unique__52_dbl_0_3,
            SUBPARTITION obj_unique__52_dbl_0_4,
            SUBPARTITION obj_unique__52_dbl_0_5,
            SUBPARTITION obj_unique__52_dbl_0_6,
            SUBPARTITION obj_unique__52_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__53`;
CREATE TABLE `obj_unique_dbl__53` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__53_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__53_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__53_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__53_dbl_0_0,
            SUBPARTITION obj_unique__53_dbl_0_1,
            SUBPARTITION obj_unique__53_dbl_0_2,
            SUBPARTITION obj_unique__53_dbl_0_3,
            SUBPARTITION obj_unique__53_dbl_0_4,
            SUBPARTITION obj_unique__53_dbl_0_5,
            SUBPARTITION obj_unique__53_dbl_0_6,
            SUBPARTITION obj_unique__53_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__54`;
CREATE TABLE `obj_unique_dbl__54` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__54_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__54_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__54_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__54_dbl_0_0,
            SUBPARTITION obj_unique__54_dbl_0_1,
            SUBPARTITION obj_unique__54_dbl_0_2,
            SUBPARTITION obj_unique__54_dbl_0_3,
            SUBPARTITION obj_unique__54_dbl_0_4,
            SUBPARTITION obj_unique__54_dbl_0_5,
            SUBPARTITION obj_unique__54_dbl_0_6,
            SUBPARTITION obj_unique__54_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__55`;
CREATE TABLE `obj_unique_dbl__55` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__55_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__55_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__55_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__55_dbl_0_0,
            SUBPARTITION obj_unique__55_dbl_0_1,
            SUBPARTITION obj_unique__55_dbl_0_2,
            SUBPARTITION obj_unique__55_dbl_0_3,
            SUBPARTITION obj_unique__55_dbl_0_4,
            SUBPARTITION obj_unique__55_dbl_0_5,
            SUBPARTITION obj_unique__55_dbl_0_6,
            SUBPARTITION obj_unique__55_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__56`;
CREATE TABLE `obj_unique_dbl__56` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__56_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__56_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__56_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__56_dbl_0_0,
            SUBPARTITION obj_unique__56_dbl_0_1,
            SUBPARTITION obj_unique__56_dbl_0_2,
            SUBPARTITION obj_unique__56_dbl_0_3,
            SUBPARTITION obj_unique__56_dbl_0_4,
            SUBPARTITION obj_unique__56_dbl_0_5,
            SUBPARTITION obj_unique__56_dbl_0_6,
            SUBPARTITION obj_unique__56_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__57`;
CREATE TABLE `obj_unique_dbl__57` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__57_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__57_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__57_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__57_dbl_0_0,
            SUBPARTITION obj_unique__57_dbl_0_1,
            SUBPARTITION obj_unique__57_dbl_0_2,
            SUBPARTITION obj_unique__57_dbl_0_3,
            SUBPARTITION obj_unique__57_dbl_0_4,
            SUBPARTITION obj_unique__57_dbl_0_5,
            SUBPARTITION obj_unique__57_dbl_0_6,
            SUBPARTITION obj_unique__57_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__58`;
CREATE TABLE `obj_unique_dbl__58` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__58_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__58_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__58_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__58_dbl_0_0,
            SUBPARTITION obj_unique__58_dbl_0_1,
            SUBPARTITION obj_unique__58_dbl_0_2,
            SUBPARTITION obj_unique__58_dbl_0_3,
            SUBPARTITION obj_unique__58_dbl_0_4,
            SUBPARTITION obj_unique__58_dbl_0_5,
            SUBPARTITION obj_unique__58_dbl_0_6,
            SUBPARTITION obj_unique__58_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__59`;
CREATE TABLE `obj_unique_dbl__59` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__59_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__59_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__59_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__59_dbl_0_0,
            SUBPARTITION obj_unique__59_dbl_0_1,
            SUBPARTITION obj_unique__59_dbl_0_2,
            SUBPARTITION obj_unique__59_dbl_0_3,
            SUBPARTITION obj_unique__59_dbl_0_4,
            SUBPARTITION obj_unique__59_dbl_0_5,
            SUBPARTITION obj_unique__59_dbl_0_6,
            SUBPARTITION obj_unique__59_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__60`;
CREATE TABLE `obj_unique_dbl__60` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__60_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__60_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__60_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__60_dbl_0_0,
            SUBPARTITION obj_unique__60_dbl_0_1,
            SUBPARTITION obj_unique__60_dbl_0_2,
            SUBPARTITION obj_unique__60_dbl_0_3,
            SUBPARTITION obj_unique__60_dbl_0_4,
            SUBPARTITION obj_unique__60_dbl_0_5,
            SUBPARTITION obj_unique__60_dbl_0_6,
            SUBPARTITION obj_unique__60_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__61`;
CREATE TABLE `obj_unique_dbl__61` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__61_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__61_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__61_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__61_dbl_0_0,
            SUBPARTITION obj_unique__61_dbl_0_1,
            SUBPARTITION obj_unique__61_dbl_0_2,
            SUBPARTITION obj_unique__61_dbl_0_3,
            SUBPARTITION obj_unique__61_dbl_0_4,
            SUBPARTITION obj_unique__61_dbl_0_5,
            SUBPARTITION obj_unique__61_dbl_0_6,
            SUBPARTITION obj_unique__61_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__62`;
CREATE TABLE `obj_unique_dbl__62` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__62_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__62_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__62_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__62_dbl_0_0,
            SUBPARTITION obj_unique__62_dbl_0_1,
            SUBPARTITION obj_unique__62_dbl_0_2,
            SUBPARTITION obj_unique__62_dbl_0_3,
            SUBPARTITION obj_unique__62_dbl_0_4,
            SUBPARTITION obj_unique__62_dbl_0_5,
            SUBPARTITION obj_unique__62_dbl_0_6,
            SUBPARTITION obj_unique__62_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__63`;
CREATE TABLE `obj_unique_dbl__63` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__63_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__63_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__63_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__63_dbl_0_0,
            SUBPARTITION obj_unique__63_dbl_0_1,
            SUBPARTITION obj_unique__63_dbl_0_2,
            SUBPARTITION obj_unique__63_dbl_0_3,
            SUBPARTITION obj_unique__63_dbl_0_4,
            SUBPARTITION obj_unique__63_dbl_0_5,
            SUBPARTITION obj_unique__63_dbl_0_6,
            SUBPARTITION obj_unique__63_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__64`;
CREATE TABLE `obj_unique_dbl__64` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__64_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__64_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__64_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__64_dbl_0_0,
            SUBPARTITION obj_unique__64_dbl_0_1,
            SUBPARTITION obj_unique__64_dbl_0_2,
            SUBPARTITION obj_unique__64_dbl_0_3,
            SUBPARTITION obj_unique__64_dbl_0_4,
            SUBPARTITION obj_unique__64_dbl_0_5,
            SUBPARTITION obj_unique__64_dbl_0_6,
            SUBPARTITION obj_unique__64_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__65`;
CREATE TABLE `obj_unique_dbl__65` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__65_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__65_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__65_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__65_dbl_0_0,
            SUBPARTITION obj_unique__65_dbl_0_1,
            SUBPARTITION obj_unique__65_dbl_0_2,
            SUBPARTITION obj_unique__65_dbl_0_3,
            SUBPARTITION obj_unique__65_dbl_0_4,
            SUBPARTITION obj_unique__65_dbl_0_5,
            SUBPARTITION obj_unique__65_dbl_0_6,
            SUBPARTITION obj_unique__65_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__66`;
CREATE TABLE `obj_unique_dbl__66` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__66_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__66_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__66_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__66_dbl_0_0,
            SUBPARTITION obj_unique__66_dbl_0_1,
            SUBPARTITION obj_unique__66_dbl_0_2,
            SUBPARTITION obj_unique__66_dbl_0_3,
            SUBPARTITION obj_unique__66_dbl_0_4,
            SUBPARTITION obj_unique__66_dbl_0_5,
            SUBPARTITION obj_unique__66_dbl_0_6,
            SUBPARTITION obj_unique__66_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__67`;
CREATE TABLE `obj_unique_dbl__67` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__67_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__67_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__67_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__67_dbl_0_0,
            SUBPARTITION obj_unique__67_dbl_0_1,
            SUBPARTITION obj_unique__67_dbl_0_2,
            SUBPARTITION obj_unique__67_dbl_0_3,
            SUBPARTITION obj_unique__67_dbl_0_4,
            SUBPARTITION obj_unique__67_dbl_0_5,
            SUBPARTITION obj_unique__67_dbl_0_6,
            SUBPARTITION obj_unique__67_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__68`;
CREATE TABLE `obj_unique_dbl__68` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__68_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__68_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__68_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__68_dbl_0_0,
            SUBPARTITION obj_unique__68_dbl_0_1,
            SUBPARTITION obj_unique__68_dbl_0_2,
            SUBPARTITION obj_unique__68_dbl_0_3,
            SUBPARTITION obj_unique__68_dbl_0_4,
            SUBPARTITION obj_unique__68_dbl_0_5,
            SUBPARTITION obj_unique__68_dbl_0_6,
            SUBPARTITION obj_unique__68_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__69`;
CREATE TABLE `obj_unique_dbl__69` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__69_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__69_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__69_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__69_dbl_0_0,
            SUBPARTITION obj_unique__69_dbl_0_1,
            SUBPARTITION obj_unique__69_dbl_0_2,
            SUBPARTITION obj_unique__69_dbl_0_3,
            SUBPARTITION obj_unique__69_dbl_0_4,
            SUBPARTITION obj_unique__69_dbl_0_5,
            SUBPARTITION obj_unique__69_dbl_0_6,
            SUBPARTITION obj_unique__69_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__70`;
CREATE TABLE `obj_unique_dbl__70` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__70_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__70_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__70_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__70_dbl_0_0,
            SUBPARTITION obj_unique__70_dbl_0_1,
            SUBPARTITION obj_unique__70_dbl_0_2,
            SUBPARTITION obj_unique__70_dbl_0_3,
            SUBPARTITION obj_unique__70_dbl_0_4,
            SUBPARTITION obj_unique__70_dbl_0_5,
            SUBPARTITION obj_unique__70_dbl_0_6,
            SUBPARTITION obj_unique__70_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__71`;
CREATE TABLE `obj_unique_dbl__71` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__71_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__71_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__71_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__71_dbl_0_0,
            SUBPARTITION obj_unique__71_dbl_0_1,
            SUBPARTITION obj_unique__71_dbl_0_2,
            SUBPARTITION obj_unique__71_dbl_0_3,
            SUBPARTITION obj_unique__71_dbl_0_4,
            SUBPARTITION obj_unique__71_dbl_0_5,
            SUBPARTITION obj_unique__71_dbl_0_6,
            SUBPARTITION obj_unique__71_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__72`;
CREATE TABLE `obj_unique_dbl__72` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__72_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__72_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__72_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__72_dbl_0_0,
            SUBPARTITION obj_unique__72_dbl_0_1,
            SUBPARTITION obj_unique__72_dbl_0_2,
            SUBPARTITION obj_unique__72_dbl_0_3,
            SUBPARTITION obj_unique__72_dbl_0_4,
            SUBPARTITION obj_unique__72_dbl_0_5,
            SUBPARTITION obj_unique__72_dbl_0_6,
            SUBPARTITION obj_unique__72_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__73`;
CREATE TABLE `obj_unique_dbl__73` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__73_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__73_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__73_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__73_dbl_0_0,
            SUBPARTITION obj_unique__73_dbl_0_1,
            SUBPARTITION obj_unique__73_dbl_0_2,
            SUBPARTITION obj_unique__73_dbl_0_3,
            SUBPARTITION obj_unique__73_dbl_0_4,
            SUBPARTITION obj_unique__73_dbl_0_5,
            SUBPARTITION obj_unique__73_dbl_0_6,
            SUBPARTITION obj_unique__73_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__74`;
CREATE TABLE `obj_unique_dbl__74` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__74_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__74_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__74_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__74_dbl_0_0,
            SUBPARTITION obj_unique__74_dbl_0_1,
            SUBPARTITION obj_unique__74_dbl_0_2,
            SUBPARTITION obj_unique__74_dbl_0_3,
            SUBPARTITION obj_unique__74_dbl_0_4,
            SUBPARTITION obj_unique__74_dbl_0_5,
            SUBPARTITION obj_unique__74_dbl_0_6,
            SUBPARTITION obj_unique__74_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__75`;
CREATE TABLE `obj_unique_dbl__75` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__75_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__75_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__75_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__75_dbl_0_0,
            SUBPARTITION obj_unique__75_dbl_0_1,
            SUBPARTITION obj_unique__75_dbl_0_2,
            SUBPARTITION obj_unique__75_dbl_0_3,
            SUBPARTITION obj_unique__75_dbl_0_4,
            SUBPARTITION obj_unique__75_dbl_0_5,
            SUBPARTITION obj_unique__75_dbl_0_6,
            SUBPARTITION obj_unique__75_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__76`;
CREATE TABLE `obj_unique_dbl__76` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__76_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__76_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__76_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__76_dbl_0_0,
            SUBPARTITION obj_unique__76_dbl_0_1,
            SUBPARTITION obj_unique__76_dbl_0_2,
            SUBPARTITION obj_unique__76_dbl_0_3,
            SUBPARTITION obj_unique__76_dbl_0_4,
            SUBPARTITION obj_unique__76_dbl_0_5,
            SUBPARTITION obj_unique__76_dbl_0_6,
            SUBPARTITION obj_unique__76_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__77`;
CREATE TABLE `obj_unique_dbl__77` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__77_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__77_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__77_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__77_dbl_0_0,
            SUBPARTITION obj_unique__77_dbl_0_1,
            SUBPARTITION obj_unique__77_dbl_0_2,
            SUBPARTITION obj_unique__77_dbl_0_3,
            SUBPARTITION obj_unique__77_dbl_0_4,
            SUBPARTITION obj_unique__77_dbl_0_5,
            SUBPARTITION obj_unique__77_dbl_0_6,
            SUBPARTITION obj_unique__77_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__78`;
CREATE TABLE `obj_unique_dbl__78` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__78_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__78_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__78_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__78_dbl_0_0,
            SUBPARTITION obj_unique__78_dbl_0_1,
            SUBPARTITION obj_unique__78_dbl_0_2,
            SUBPARTITION obj_unique__78_dbl_0_3,
            SUBPARTITION obj_unique__78_dbl_0_4,
            SUBPARTITION obj_unique__78_dbl_0_5,
            SUBPARTITION obj_unique__78_dbl_0_6,
            SUBPARTITION obj_unique__78_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__79`;
CREATE TABLE `obj_unique_dbl__79` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__79_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__79_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__79_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__79_dbl_0_0,
            SUBPARTITION obj_unique__79_dbl_0_1,
            SUBPARTITION obj_unique__79_dbl_0_2,
            SUBPARTITION obj_unique__79_dbl_0_3,
            SUBPARTITION obj_unique__79_dbl_0_4,
            SUBPARTITION obj_unique__79_dbl_0_5,
            SUBPARTITION obj_unique__79_dbl_0_6,
            SUBPARTITION obj_unique__79_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__80`;
CREATE TABLE `obj_unique_dbl__80` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__80_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__80_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__80_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__80_dbl_0_0,
            SUBPARTITION obj_unique__80_dbl_0_1,
            SUBPARTITION obj_unique__80_dbl_0_2,
            SUBPARTITION obj_unique__80_dbl_0_3,
            SUBPARTITION obj_unique__80_dbl_0_4,
            SUBPARTITION obj_unique__80_dbl_0_5,
            SUBPARTITION obj_unique__80_dbl_0_6,
            SUBPARTITION obj_unique__80_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__81`;
CREATE TABLE `obj_unique_dbl__81` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__81_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__81_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__81_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__81_dbl_0_0,
            SUBPARTITION obj_unique__81_dbl_0_1,
            SUBPARTITION obj_unique__81_dbl_0_2,
            SUBPARTITION obj_unique__81_dbl_0_3,
            SUBPARTITION obj_unique__81_dbl_0_4,
            SUBPARTITION obj_unique__81_dbl_0_5,
            SUBPARTITION obj_unique__81_dbl_0_6,
            SUBPARTITION obj_unique__81_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__82`;
CREATE TABLE `obj_unique_dbl__82` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__82_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__82_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__82_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__82_dbl_0_0,
            SUBPARTITION obj_unique__82_dbl_0_1,
            SUBPARTITION obj_unique__82_dbl_0_2,
            SUBPARTITION obj_unique__82_dbl_0_3,
            SUBPARTITION obj_unique__82_dbl_0_4,
            SUBPARTITION obj_unique__82_dbl_0_5,
            SUBPARTITION obj_unique__82_dbl_0_6,
            SUBPARTITION obj_unique__82_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__83`;
CREATE TABLE `obj_unique_dbl__83` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__83_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__83_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__83_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__83_dbl_0_0,
            SUBPARTITION obj_unique__83_dbl_0_1,
            SUBPARTITION obj_unique__83_dbl_0_2,
            SUBPARTITION obj_unique__83_dbl_0_3,
            SUBPARTITION obj_unique__83_dbl_0_4,
            SUBPARTITION obj_unique__83_dbl_0_5,
            SUBPARTITION obj_unique__83_dbl_0_6,
            SUBPARTITION obj_unique__83_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__84`;
CREATE TABLE `obj_unique_dbl__84` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__84_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__84_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__84_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__84_dbl_0_0,
            SUBPARTITION obj_unique__84_dbl_0_1,
            SUBPARTITION obj_unique__84_dbl_0_2,
            SUBPARTITION obj_unique__84_dbl_0_3,
            SUBPARTITION obj_unique__84_dbl_0_4,
            SUBPARTITION obj_unique__84_dbl_0_5,
            SUBPARTITION obj_unique__84_dbl_0_6,
            SUBPARTITION obj_unique__84_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__85`;
CREATE TABLE `obj_unique_dbl__85` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__85_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__85_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__85_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__85_dbl_0_0,
            SUBPARTITION obj_unique__85_dbl_0_1,
            SUBPARTITION obj_unique__85_dbl_0_2,
            SUBPARTITION obj_unique__85_dbl_0_3,
            SUBPARTITION obj_unique__85_dbl_0_4,
            SUBPARTITION obj_unique__85_dbl_0_5,
            SUBPARTITION obj_unique__85_dbl_0_6,
            SUBPARTITION obj_unique__85_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__86`;
CREATE TABLE `obj_unique_dbl__86` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__86_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__86_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__86_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__86_dbl_0_0,
            SUBPARTITION obj_unique__86_dbl_0_1,
            SUBPARTITION obj_unique__86_dbl_0_2,
            SUBPARTITION obj_unique__86_dbl_0_3,
            SUBPARTITION obj_unique__86_dbl_0_4,
            SUBPARTITION obj_unique__86_dbl_0_5,
            SUBPARTITION obj_unique__86_dbl_0_6,
            SUBPARTITION obj_unique__86_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__87`;
CREATE TABLE `obj_unique_dbl__87` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__87_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__87_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__87_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__87_dbl_0_0,
            SUBPARTITION obj_unique__87_dbl_0_1,
            SUBPARTITION obj_unique__87_dbl_0_2,
            SUBPARTITION obj_unique__87_dbl_0_3,
            SUBPARTITION obj_unique__87_dbl_0_4,
            SUBPARTITION obj_unique__87_dbl_0_5,
            SUBPARTITION obj_unique__87_dbl_0_6,
            SUBPARTITION obj_unique__87_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__88`;
CREATE TABLE `obj_unique_dbl__88` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__88_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__88_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__88_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__88_dbl_0_0,
            SUBPARTITION obj_unique__88_dbl_0_1,
            SUBPARTITION obj_unique__88_dbl_0_2,
            SUBPARTITION obj_unique__88_dbl_0_3,
            SUBPARTITION obj_unique__88_dbl_0_4,
            SUBPARTITION obj_unique__88_dbl_0_5,
            SUBPARTITION obj_unique__88_dbl_0_6,
            SUBPARTITION obj_unique__88_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__89`;
CREATE TABLE `obj_unique_dbl__89` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__89_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__89_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__89_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__89_dbl_0_0,
            SUBPARTITION obj_unique__89_dbl_0_1,
            SUBPARTITION obj_unique__89_dbl_0_2,
            SUBPARTITION obj_unique__89_dbl_0_3,
            SUBPARTITION obj_unique__89_dbl_0_4,
            SUBPARTITION obj_unique__89_dbl_0_5,
            SUBPARTITION obj_unique__89_dbl_0_6,
            SUBPARTITION obj_unique__89_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__90`;
CREATE TABLE `obj_unique_dbl__90` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__90_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__90_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__90_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__90_dbl_0_0,
            SUBPARTITION obj_unique__90_dbl_0_1,
            SUBPARTITION obj_unique__90_dbl_0_2,
            SUBPARTITION obj_unique__90_dbl_0_3,
            SUBPARTITION obj_unique__90_dbl_0_4,
            SUBPARTITION obj_unique__90_dbl_0_5,
            SUBPARTITION obj_unique__90_dbl_0_6,
            SUBPARTITION obj_unique__90_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__91`;
CREATE TABLE `obj_unique_dbl__91` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__91_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__91_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__91_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__91_dbl_0_0,
            SUBPARTITION obj_unique__91_dbl_0_1,
            SUBPARTITION obj_unique__91_dbl_0_2,
            SUBPARTITION obj_unique__91_dbl_0_3,
            SUBPARTITION obj_unique__91_dbl_0_4,
            SUBPARTITION obj_unique__91_dbl_0_5,
            SUBPARTITION obj_unique__91_dbl_0_6,
            SUBPARTITION obj_unique__91_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__92`;
CREATE TABLE `obj_unique_dbl__92` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__92_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__92_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__92_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__92_dbl_0_0,
            SUBPARTITION obj_unique__92_dbl_0_1,
            SUBPARTITION obj_unique__92_dbl_0_2,
            SUBPARTITION obj_unique__92_dbl_0_3,
            SUBPARTITION obj_unique__92_dbl_0_4,
            SUBPARTITION obj_unique__92_dbl_0_5,
            SUBPARTITION obj_unique__92_dbl_0_6,
            SUBPARTITION obj_unique__92_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__93`;
CREATE TABLE `obj_unique_dbl__93` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__93_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__93_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__93_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__93_dbl_0_0,
            SUBPARTITION obj_unique__93_dbl_0_1,
            SUBPARTITION obj_unique__93_dbl_0_2,
            SUBPARTITION obj_unique__93_dbl_0_3,
            SUBPARTITION obj_unique__93_dbl_0_4,
            SUBPARTITION obj_unique__93_dbl_0_5,
            SUBPARTITION obj_unique__93_dbl_0_6,
            SUBPARTITION obj_unique__93_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__94`;
CREATE TABLE `obj_unique_dbl__94` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__94_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__94_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__94_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__94_dbl_0_0,
            SUBPARTITION obj_unique__94_dbl_0_1,
            SUBPARTITION obj_unique__94_dbl_0_2,
            SUBPARTITION obj_unique__94_dbl_0_3,
            SUBPARTITION obj_unique__94_dbl_0_4,
            SUBPARTITION obj_unique__94_dbl_0_5,
            SUBPARTITION obj_unique__94_dbl_0_6,
            SUBPARTITION obj_unique__94_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__95`;
CREATE TABLE `obj_unique_dbl__95` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__95_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__95_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__95_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__95_dbl_0_0,
            SUBPARTITION obj_unique__95_dbl_0_1,
            SUBPARTITION obj_unique__95_dbl_0_2,
            SUBPARTITION obj_unique__95_dbl_0_3,
            SUBPARTITION obj_unique__95_dbl_0_4,
            SUBPARTITION obj_unique__95_dbl_0_5,
            SUBPARTITION obj_unique__95_dbl_0_6,
            SUBPARTITION obj_unique__95_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__96`;
CREATE TABLE `obj_unique_dbl__96` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__96_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__96_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__96_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__96_dbl_0_0,
            SUBPARTITION obj_unique__96_dbl_0_1,
            SUBPARTITION obj_unique__96_dbl_0_2,
            SUBPARTITION obj_unique__96_dbl_0_3,
            SUBPARTITION obj_unique__96_dbl_0_4,
            SUBPARTITION obj_unique__96_dbl_0_5,
            SUBPARTITION obj_unique__96_dbl_0_6,
            SUBPARTITION obj_unique__96_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__97`;
CREATE TABLE `obj_unique_dbl__97` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__97_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__97_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__97_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__97_dbl_0_0,
            SUBPARTITION obj_unique__97_dbl_0_1,
            SUBPARTITION obj_unique__97_dbl_0_2,
            SUBPARTITION obj_unique__97_dbl_0_3,
            SUBPARTITION obj_unique__97_dbl_0_4,
            SUBPARTITION obj_unique__97_dbl_0_5,
            SUBPARTITION obj_unique__97_dbl_0_6,
            SUBPARTITION obj_unique__97_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__98`;
CREATE TABLE `obj_unique_dbl__98` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__98_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__98_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__98_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__98_dbl_0_0,
            SUBPARTITION obj_unique__98_dbl_0_1,
            SUBPARTITION obj_unique__98_dbl_0_2,
            SUBPARTITION obj_unique__98_dbl_0_3,
            SUBPARTITION obj_unique__98_dbl_0_4,
            SUBPARTITION obj_unique__98_dbl_0_5,
            SUBPARTITION obj_unique__98_dbl_0_6,
            SUBPARTITION obj_unique__98_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__99`;
CREATE TABLE `obj_unique_dbl__99` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__99_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__99_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__99_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__99_dbl_0_0,
            SUBPARTITION obj_unique__99_dbl_0_1,
            SUBPARTITION obj_unique__99_dbl_0_2,
            SUBPARTITION obj_unique__99_dbl_0_3,
            SUBPARTITION obj_unique__99_dbl_0_4,
            SUBPARTITION obj_unique__99_dbl_0_5,
            SUBPARTITION obj_unique__99_dbl_0_6,
            SUBPARTITION obj_unique__99_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__100`;
CREATE TABLE `obj_unique_dbl__100` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__100_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__100_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__100_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__100_dbl_0_0,
            SUBPARTITION obj_unique__100_dbl_0_1,
            SUBPARTITION obj_unique__100_dbl_0_2,
            SUBPARTITION obj_unique__100_dbl_0_3,
            SUBPARTITION obj_unique__100_dbl_0_4,
            SUBPARTITION obj_unique__100_dbl_0_5,
            SUBPARTITION obj_unique__100_dbl_0_6,
            SUBPARTITION obj_unique__100_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__101`;
CREATE TABLE `obj_unique_dbl__101` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__101_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__101_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__101_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__101_dbl_0_0,
            SUBPARTITION obj_unique__101_dbl_0_1,
            SUBPARTITION obj_unique__101_dbl_0_2,
            SUBPARTITION obj_unique__101_dbl_0_3,
            SUBPARTITION obj_unique__101_dbl_0_4,
            SUBPARTITION obj_unique__101_dbl_0_5,
            SUBPARTITION obj_unique__101_dbl_0_6,
            SUBPARTITION obj_unique__101_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__102`;
CREATE TABLE `obj_unique_dbl__102` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__102_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__102_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__102_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__102_dbl_0_0,
            SUBPARTITION obj_unique__102_dbl_0_1,
            SUBPARTITION obj_unique__102_dbl_0_2,
            SUBPARTITION obj_unique__102_dbl_0_3,
            SUBPARTITION obj_unique__102_dbl_0_4,
            SUBPARTITION obj_unique__102_dbl_0_5,
            SUBPARTITION obj_unique__102_dbl_0_6,
            SUBPARTITION obj_unique__102_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__103`;
CREATE TABLE `obj_unique_dbl__103` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__103_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__103_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__103_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__103_dbl_0_0,
            SUBPARTITION obj_unique__103_dbl_0_1,
            SUBPARTITION obj_unique__103_dbl_0_2,
            SUBPARTITION obj_unique__103_dbl_0_3,
            SUBPARTITION obj_unique__103_dbl_0_4,
            SUBPARTITION obj_unique__103_dbl_0_5,
            SUBPARTITION obj_unique__103_dbl_0_6,
            SUBPARTITION obj_unique__103_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__104`;
CREATE TABLE `obj_unique_dbl__104` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__104_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__104_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__104_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__104_dbl_0_0,
            SUBPARTITION obj_unique__104_dbl_0_1,
            SUBPARTITION obj_unique__104_dbl_0_2,
            SUBPARTITION obj_unique__104_dbl_0_3,
            SUBPARTITION obj_unique__104_dbl_0_4,
            SUBPARTITION obj_unique__104_dbl_0_5,
            SUBPARTITION obj_unique__104_dbl_0_6,
            SUBPARTITION obj_unique__104_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__105`;
CREATE TABLE `obj_unique_dbl__105` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__105_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__105_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__105_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__105_dbl_0_0,
            SUBPARTITION obj_unique__105_dbl_0_1,
            SUBPARTITION obj_unique__105_dbl_0_2,
            SUBPARTITION obj_unique__105_dbl_0_3,
            SUBPARTITION obj_unique__105_dbl_0_4,
            SUBPARTITION obj_unique__105_dbl_0_5,
            SUBPARTITION obj_unique__105_dbl_0_6,
            SUBPARTITION obj_unique__105_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__106`;
CREATE TABLE `obj_unique_dbl__106` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__106_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__106_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__106_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__106_dbl_0_0,
            SUBPARTITION obj_unique__106_dbl_0_1,
            SUBPARTITION obj_unique__106_dbl_0_2,
            SUBPARTITION obj_unique__106_dbl_0_3,
            SUBPARTITION obj_unique__106_dbl_0_4,
            SUBPARTITION obj_unique__106_dbl_0_5,
            SUBPARTITION obj_unique__106_dbl_0_6,
            SUBPARTITION obj_unique__106_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__107`;
CREATE TABLE `obj_unique_dbl__107` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__107_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__107_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__107_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__107_dbl_0_0,
            SUBPARTITION obj_unique__107_dbl_0_1,
            SUBPARTITION obj_unique__107_dbl_0_2,
            SUBPARTITION obj_unique__107_dbl_0_3,
            SUBPARTITION obj_unique__107_dbl_0_4,
            SUBPARTITION obj_unique__107_dbl_0_5,
            SUBPARTITION obj_unique__107_dbl_0_6,
            SUBPARTITION obj_unique__107_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__108`;
CREATE TABLE `obj_unique_dbl__108` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__108_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__108_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__108_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__108_dbl_0_0,
            SUBPARTITION obj_unique__108_dbl_0_1,
            SUBPARTITION obj_unique__108_dbl_0_2,
            SUBPARTITION obj_unique__108_dbl_0_3,
            SUBPARTITION obj_unique__108_dbl_0_4,
            SUBPARTITION obj_unique__108_dbl_0_5,
            SUBPARTITION obj_unique__108_dbl_0_6,
            SUBPARTITION obj_unique__108_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__109`;
CREATE TABLE `obj_unique_dbl__109` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__109_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__109_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__109_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__109_dbl_0_0,
            SUBPARTITION obj_unique__109_dbl_0_1,
            SUBPARTITION obj_unique__109_dbl_0_2,
            SUBPARTITION obj_unique__109_dbl_0_3,
            SUBPARTITION obj_unique__109_dbl_0_4,
            SUBPARTITION obj_unique__109_dbl_0_5,
            SUBPARTITION obj_unique__109_dbl_0_6,
            SUBPARTITION obj_unique__109_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__110`;
CREATE TABLE `obj_unique_dbl__110` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__110_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__110_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__110_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__110_dbl_0_0,
            SUBPARTITION obj_unique__110_dbl_0_1,
            SUBPARTITION obj_unique__110_dbl_0_2,
            SUBPARTITION obj_unique__110_dbl_0_3,
            SUBPARTITION obj_unique__110_dbl_0_4,
            SUBPARTITION obj_unique__110_dbl_0_5,
            SUBPARTITION obj_unique__110_dbl_0_6,
            SUBPARTITION obj_unique__110_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__111`;
CREATE TABLE `obj_unique_dbl__111` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__111_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__111_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__111_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__111_dbl_0_0,
            SUBPARTITION obj_unique__111_dbl_0_1,
            SUBPARTITION obj_unique__111_dbl_0_2,
            SUBPARTITION obj_unique__111_dbl_0_3,
            SUBPARTITION obj_unique__111_dbl_0_4,
            SUBPARTITION obj_unique__111_dbl_0_5,
            SUBPARTITION obj_unique__111_dbl_0_6,
            SUBPARTITION obj_unique__111_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__112`;
CREATE TABLE `obj_unique_dbl__112` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__112_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__112_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__112_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__112_dbl_0_0,
            SUBPARTITION obj_unique__112_dbl_0_1,
            SUBPARTITION obj_unique__112_dbl_0_2,
            SUBPARTITION obj_unique__112_dbl_0_3,
            SUBPARTITION obj_unique__112_dbl_0_4,
            SUBPARTITION obj_unique__112_dbl_0_5,
            SUBPARTITION obj_unique__112_dbl_0_6,
            SUBPARTITION obj_unique__112_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__113`;
CREATE TABLE `obj_unique_dbl__113` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__113_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__113_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__113_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__113_dbl_0_0,
            SUBPARTITION obj_unique__113_dbl_0_1,
            SUBPARTITION obj_unique__113_dbl_0_2,
            SUBPARTITION obj_unique__113_dbl_0_3,
            SUBPARTITION obj_unique__113_dbl_0_4,
            SUBPARTITION obj_unique__113_dbl_0_5,
            SUBPARTITION obj_unique__113_dbl_0_6,
            SUBPARTITION obj_unique__113_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__114`;
CREATE TABLE `obj_unique_dbl__114` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__114_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__114_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__114_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__114_dbl_0_0,
            SUBPARTITION obj_unique__114_dbl_0_1,
            SUBPARTITION obj_unique__114_dbl_0_2,
            SUBPARTITION obj_unique__114_dbl_0_3,
            SUBPARTITION obj_unique__114_dbl_0_4,
            SUBPARTITION obj_unique__114_dbl_0_5,
            SUBPARTITION obj_unique__114_dbl_0_6,
            SUBPARTITION obj_unique__114_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__115`;
CREATE TABLE `obj_unique_dbl__115` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__115_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__115_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__115_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__115_dbl_0_0,
            SUBPARTITION obj_unique__115_dbl_0_1,
            SUBPARTITION obj_unique__115_dbl_0_2,
            SUBPARTITION obj_unique__115_dbl_0_3,
            SUBPARTITION obj_unique__115_dbl_0_4,
            SUBPARTITION obj_unique__115_dbl_0_5,
            SUBPARTITION obj_unique__115_dbl_0_6,
            SUBPARTITION obj_unique__115_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__116`;
CREATE TABLE `obj_unique_dbl__116` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__116_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__116_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__116_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__116_dbl_0_0,
            SUBPARTITION obj_unique__116_dbl_0_1,
            SUBPARTITION obj_unique__116_dbl_0_2,
            SUBPARTITION obj_unique__116_dbl_0_3,
            SUBPARTITION obj_unique__116_dbl_0_4,
            SUBPARTITION obj_unique__116_dbl_0_5,
            SUBPARTITION obj_unique__116_dbl_0_6,
            SUBPARTITION obj_unique__116_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__117`;
CREATE TABLE `obj_unique_dbl__117` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__117_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__117_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__117_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__117_dbl_0_0,
            SUBPARTITION obj_unique__117_dbl_0_1,
            SUBPARTITION obj_unique__117_dbl_0_2,
            SUBPARTITION obj_unique__117_dbl_0_3,
            SUBPARTITION obj_unique__117_dbl_0_4,
            SUBPARTITION obj_unique__117_dbl_0_5,
            SUBPARTITION obj_unique__117_dbl_0_6,
            SUBPARTITION obj_unique__117_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__118`;
CREATE TABLE `obj_unique_dbl__118` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__118_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__118_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__118_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__118_dbl_0_0,
            SUBPARTITION obj_unique__118_dbl_0_1,
            SUBPARTITION obj_unique__118_dbl_0_2,
            SUBPARTITION obj_unique__118_dbl_0_3,
            SUBPARTITION obj_unique__118_dbl_0_4,
            SUBPARTITION obj_unique__118_dbl_0_5,
            SUBPARTITION obj_unique__118_dbl_0_6,
            SUBPARTITION obj_unique__118_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__119`;
CREATE TABLE `obj_unique_dbl__119` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__119_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__119_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__119_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__119_dbl_0_0,
            SUBPARTITION obj_unique__119_dbl_0_1,
            SUBPARTITION obj_unique__119_dbl_0_2,
            SUBPARTITION obj_unique__119_dbl_0_3,
            SUBPARTITION obj_unique__119_dbl_0_4,
            SUBPARTITION obj_unique__119_dbl_0_5,
            SUBPARTITION obj_unique__119_dbl_0_6,
            SUBPARTITION obj_unique__119_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__120`;
CREATE TABLE `obj_unique_dbl__120` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__120_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__120_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__120_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__120_dbl_0_0,
            SUBPARTITION obj_unique__120_dbl_0_1,
            SUBPARTITION obj_unique__120_dbl_0_2,
            SUBPARTITION obj_unique__120_dbl_0_3,
            SUBPARTITION obj_unique__120_dbl_0_4,
            SUBPARTITION obj_unique__120_dbl_0_5,
            SUBPARTITION obj_unique__120_dbl_0_6,
            SUBPARTITION obj_unique__120_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__121`;
CREATE TABLE `obj_unique_dbl__121` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__121_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__121_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__121_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__121_dbl_0_0,
            SUBPARTITION obj_unique__121_dbl_0_1,
            SUBPARTITION obj_unique__121_dbl_0_2,
            SUBPARTITION obj_unique__121_dbl_0_3,
            SUBPARTITION obj_unique__121_dbl_0_4,
            SUBPARTITION obj_unique__121_dbl_0_5,
            SUBPARTITION obj_unique__121_dbl_0_6,
            SUBPARTITION obj_unique__121_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__122`;
CREATE TABLE `obj_unique_dbl__122` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__122_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__122_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__122_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__122_dbl_0_0,
            SUBPARTITION obj_unique__122_dbl_0_1,
            SUBPARTITION obj_unique__122_dbl_0_2,
            SUBPARTITION obj_unique__122_dbl_0_3,
            SUBPARTITION obj_unique__122_dbl_0_4,
            SUBPARTITION obj_unique__122_dbl_0_5,
            SUBPARTITION obj_unique__122_dbl_0_6,
            SUBPARTITION obj_unique__122_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__123`;
CREATE TABLE `obj_unique_dbl__123` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__123_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__123_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__123_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__123_dbl_0_0,
            SUBPARTITION obj_unique__123_dbl_0_1,
            SUBPARTITION obj_unique__123_dbl_0_2,
            SUBPARTITION obj_unique__123_dbl_0_3,
            SUBPARTITION obj_unique__123_dbl_0_4,
            SUBPARTITION obj_unique__123_dbl_0_5,
            SUBPARTITION obj_unique__123_dbl_0_6,
            SUBPARTITION obj_unique__123_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__124`;
CREATE TABLE `obj_unique_dbl__124` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__124_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__124_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__124_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__124_dbl_0_0,
            SUBPARTITION obj_unique__124_dbl_0_1,
            SUBPARTITION obj_unique__124_dbl_0_2,
            SUBPARTITION obj_unique__124_dbl_0_3,
            SUBPARTITION obj_unique__124_dbl_0_4,
            SUBPARTITION obj_unique__124_dbl_0_5,
            SUBPARTITION obj_unique__124_dbl_0_6,
            SUBPARTITION obj_unique__124_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__125`;
CREATE TABLE `obj_unique_dbl__125` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__125_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__125_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__125_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__125_dbl_0_0,
            SUBPARTITION obj_unique__125_dbl_0_1,
            SUBPARTITION obj_unique__125_dbl_0_2,
            SUBPARTITION obj_unique__125_dbl_0_3,
            SUBPARTITION obj_unique__125_dbl_0_4,
            SUBPARTITION obj_unique__125_dbl_0_5,
            SUBPARTITION obj_unique__125_dbl_0_6,
            SUBPARTITION obj_unique__125_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__126`;
CREATE TABLE `obj_unique_dbl__126` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__126_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__126_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__126_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__126_dbl_0_0,
            SUBPARTITION obj_unique__126_dbl_0_1,
            SUBPARTITION obj_unique__126_dbl_0_2,
            SUBPARTITION obj_unique__126_dbl_0_3,
            SUBPARTITION obj_unique__126_dbl_0_4,
            SUBPARTITION obj_unique__126_dbl_0_5,
            SUBPARTITION obj_unique__126_dbl_0_6,
            SUBPARTITION obj_unique__126_dbl_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `obj_unique_dbl__127`;
CREATE TABLE `obj_unique_dbl__127` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DOUBLE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_dbl__127_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_dbl__127_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_dbl__127_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique__127_dbl_0_0,
            SUBPARTITION obj_unique__127_dbl_0_1,
            SUBPARTITION obj_unique__127_dbl_0_2,
            SUBPARTITION obj_unique__127_dbl_0_3,
            SUBPARTITION obj_unique__127_dbl_0_4,
            SUBPARTITION obj_unique__127_dbl_0_5,
            SUBPARTITION obj_unique__127_dbl_0_6,
            SUBPARTITION obj_unique__127_dbl_0_7
        )
    )

;
