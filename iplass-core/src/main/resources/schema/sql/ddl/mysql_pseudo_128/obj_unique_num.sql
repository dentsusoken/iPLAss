/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num`;
CREATE TABLE `obj_unique_num` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num_0_0,
            SUBPARTITION obj_unique_num_0_1,
            SUBPARTITION obj_unique_num_0_2,
            SUBPARTITION obj_unique_num_0_3,
            SUBPARTITION obj_unique_num_0_4,
            SUBPARTITION obj_unique_num_0_5,
            SUBPARTITION obj_unique_num_0_6,
            SUBPARTITION obj_unique_num_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__1`;
CREATE TABLE `obj_unique_num__1` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__1_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__1_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__1_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__1_0_0,
            SUBPARTITION obj_unique_num__1_0_1,
            SUBPARTITION obj_unique_num__1_0_2,
            SUBPARTITION obj_unique_num__1_0_3,
            SUBPARTITION obj_unique_num__1_0_4,
            SUBPARTITION obj_unique_num__1_0_5,
            SUBPARTITION obj_unique_num__1_0_6,
            SUBPARTITION obj_unique_num__1_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__2`;
CREATE TABLE `obj_unique_num__2` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__2_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__2_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__2_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__2_0_0,
            SUBPARTITION obj_unique_num__2_0_1,
            SUBPARTITION obj_unique_num__2_0_2,
            SUBPARTITION obj_unique_num__2_0_3,
            SUBPARTITION obj_unique_num__2_0_4,
            SUBPARTITION obj_unique_num__2_0_5,
            SUBPARTITION obj_unique_num__2_0_6,
            SUBPARTITION obj_unique_num__2_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__3`;
CREATE TABLE `obj_unique_num__3` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__3_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__3_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__3_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__3_0_0,
            SUBPARTITION obj_unique_num__3_0_1,
            SUBPARTITION obj_unique_num__3_0_2,
            SUBPARTITION obj_unique_num__3_0_3,
            SUBPARTITION obj_unique_num__3_0_4,
            SUBPARTITION obj_unique_num__3_0_5,
            SUBPARTITION obj_unique_num__3_0_6,
            SUBPARTITION obj_unique_num__3_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__4`;
CREATE TABLE `obj_unique_num__4` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__4_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__4_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__4_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__4_0_0,
            SUBPARTITION obj_unique_num__4_0_1,
            SUBPARTITION obj_unique_num__4_0_2,
            SUBPARTITION obj_unique_num__4_0_3,
            SUBPARTITION obj_unique_num__4_0_4,
            SUBPARTITION obj_unique_num__4_0_5,
            SUBPARTITION obj_unique_num__4_0_6,
            SUBPARTITION obj_unique_num__4_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__5`;
CREATE TABLE `obj_unique_num__5` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__5_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__5_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__5_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__5_0_0,
            SUBPARTITION obj_unique_num__5_0_1,
            SUBPARTITION obj_unique_num__5_0_2,
            SUBPARTITION obj_unique_num__5_0_3,
            SUBPARTITION obj_unique_num__5_0_4,
            SUBPARTITION obj_unique_num__5_0_5,
            SUBPARTITION obj_unique_num__5_0_6,
            SUBPARTITION obj_unique_num__5_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__6`;
CREATE TABLE `obj_unique_num__6` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__6_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__6_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__6_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__6_0_0,
            SUBPARTITION obj_unique_num__6_0_1,
            SUBPARTITION obj_unique_num__6_0_2,
            SUBPARTITION obj_unique_num__6_0_3,
            SUBPARTITION obj_unique_num__6_0_4,
            SUBPARTITION obj_unique_num__6_0_5,
            SUBPARTITION obj_unique_num__6_0_6,
            SUBPARTITION obj_unique_num__6_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__7`;
CREATE TABLE `obj_unique_num__7` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__7_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__7_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__7_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__7_0_0,
            SUBPARTITION obj_unique_num__7_0_1,
            SUBPARTITION obj_unique_num__7_0_2,
            SUBPARTITION obj_unique_num__7_0_3,
            SUBPARTITION obj_unique_num__7_0_4,
            SUBPARTITION obj_unique_num__7_0_5,
            SUBPARTITION obj_unique_num__7_0_6,
            SUBPARTITION obj_unique_num__7_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__8`;
CREATE TABLE `obj_unique_num__8` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__8_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__8_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__8_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__8_0_0,
            SUBPARTITION obj_unique_num__8_0_1,
            SUBPARTITION obj_unique_num__8_0_2,
            SUBPARTITION obj_unique_num__8_0_3,
            SUBPARTITION obj_unique_num__8_0_4,
            SUBPARTITION obj_unique_num__8_0_5,
            SUBPARTITION obj_unique_num__8_0_6,
            SUBPARTITION obj_unique_num__8_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__9`;
CREATE TABLE `obj_unique_num__9` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__9_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__9_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__9_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__9_0_0,
            SUBPARTITION obj_unique_num__9_0_1,
            SUBPARTITION obj_unique_num__9_0_2,
            SUBPARTITION obj_unique_num__9_0_3,
            SUBPARTITION obj_unique_num__9_0_4,
            SUBPARTITION obj_unique_num__9_0_5,
            SUBPARTITION obj_unique_num__9_0_6,
            SUBPARTITION obj_unique_num__9_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__10`;
CREATE TABLE `obj_unique_num__10` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__10_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__10_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__10_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__10_0_0,
            SUBPARTITION obj_unique_num__10_0_1,
            SUBPARTITION obj_unique_num__10_0_2,
            SUBPARTITION obj_unique_num__10_0_3,
            SUBPARTITION obj_unique_num__10_0_4,
            SUBPARTITION obj_unique_num__10_0_5,
            SUBPARTITION obj_unique_num__10_0_6,
            SUBPARTITION obj_unique_num__10_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__11`;
CREATE TABLE `obj_unique_num__11` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__11_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__11_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__11_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__11_0_0,
            SUBPARTITION obj_unique_num__11_0_1,
            SUBPARTITION obj_unique_num__11_0_2,
            SUBPARTITION obj_unique_num__11_0_3,
            SUBPARTITION obj_unique_num__11_0_4,
            SUBPARTITION obj_unique_num__11_0_5,
            SUBPARTITION obj_unique_num__11_0_6,
            SUBPARTITION obj_unique_num__11_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__12`;
CREATE TABLE `obj_unique_num__12` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__12_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__12_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__12_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__12_0_0,
            SUBPARTITION obj_unique_num__12_0_1,
            SUBPARTITION obj_unique_num__12_0_2,
            SUBPARTITION obj_unique_num__12_0_3,
            SUBPARTITION obj_unique_num__12_0_4,
            SUBPARTITION obj_unique_num__12_0_5,
            SUBPARTITION obj_unique_num__12_0_6,
            SUBPARTITION obj_unique_num__12_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__13`;
CREATE TABLE `obj_unique_num__13` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__13_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__13_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__13_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__13_0_0,
            SUBPARTITION obj_unique_num__13_0_1,
            SUBPARTITION obj_unique_num__13_0_2,
            SUBPARTITION obj_unique_num__13_0_3,
            SUBPARTITION obj_unique_num__13_0_4,
            SUBPARTITION obj_unique_num__13_0_5,
            SUBPARTITION obj_unique_num__13_0_6,
            SUBPARTITION obj_unique_num__13_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__14`;
CREATE TABLE `obj_unique_num__14` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__14_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__14_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__14_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__14_0_0,
            SUBPARTITION obj_unique_num__14_0_1,
            SUBPARTITION obj_unique_num__14_0_2,
            SUBPARTITION obj_unique_num__14_0_3,
            SUBPARTITION obj_unique_num__14_0_4,
            SUBPARTITION obj_unique_num__14_0_5,
            SUBPARTITION obj_unique_num__14_0_6,
            SUBPARTITION obj_unique_num__14_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__15`;
CREATE TABLE `obj_unique_num__15` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__15_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__15_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__15_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__15_0_0,
            SUBPARTITION obj_unique_num__15_0_1,
            SUBPARTITION obj_unique_num__15_0_2,
            SUBPARTITION obj_unique_num__15_0_3,
            SUBPARTITION obj_unique_num__15_0_4,
            SUBPARTITION obj_unique_num__15_0_5,
            SUBPARTITION obj_unique_num__15_0_6,
            SUBPARTITION obj_unique_num__15_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__16`;
CREATE TABLE `obj_unique_num__16` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__16_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__16_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__16_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__16_0_0,
            SUBPARTITION obj_unique_num__16_0_1,
            SUBPARTITION obj_unique_num__16_0_2,
            SUBPARTITION obj_unique_num__16_0_3,
            SUBPARTITION obj_unique_num__16_0_4,
            SUBPARTITION obj_unique_num__16_0_5,
            SUBPARTITION obj_unique_num__16_0_6,
            SUBPARTITION obj_unique_num__16_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__17`;
CREATE TABLE `obj_unique_num__17` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__17_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__17_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__17_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__17_0_0,
            SUBPARTITION obj_unique_num__17_0_1,
            SUBPARTITION obj_unique_num__17_0_2,
            SUBPARTITION obj_unique_num__17_0_3,
            SUBPARTITION obj_unique_num__17_0_4,
            SUBPARTITION obj_unique_num__17_0_5,
            SUBPARTITION obj_unique_num__17_0_6,
            SUBPARTITION obj_unique_num__17_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__18`;
CREATE TABLE `obj_unique_num__18` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__18_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__18_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__18_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__18_0_0,
            SUBPARTITION obj_unique_num__18_0_1,
            SUBPARTITION obj_unique_num__18_0_2,
            SUBPARTITION obj_unique_num__18_0_3,
            SUBPARTITION obj_unique_num__18_0_4,
            SUBPARTITION obj_unique_num__18_0_5,
            SUBPARTITION obj_unique_num__18_0_6,
            SUBPARTITION obj_unique_num__18_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__19`;
CREATE TABLE `obj_unique_num__19` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__19_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__19_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__19_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__19_0_0,
            SUBPARTITION obj_unique_num__19_0_1,
            SUBPARTITION obj_unique_num__19_0_2,
            SUBPARTITION obj_unique_num__19_0_3,
            SUBPARTITION obj_unique_num__19_0_4,
            SUBPARTITION obj_unique_num__19_0_5,
            SUBPARTITION obj_unique_num__19_0_6,
            SUBPARTITION obj_unique_num__19_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__20`;
CREATE TABLE `obj_unique_num__20` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__20_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__20_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__20_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__20_0_0,
            SUBPARTITION obj_unique_num__20_0_1,
            SUBPARTITION obj_unique_num__20_0_2,
            SUBPARTITION obj_unique_num__20_0_3,
            SUBPARTITION obj_unique_num__20_0_4,
            SUBPARTITION obj_unique_num__20_0_5,
            SUBPARTITION obj_unique_num__20_0_6,
            SUBPARTITION obj_unique_num__20_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__21`;
CREATE TABLE `obj_unique_num__21` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__21_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__21_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__21_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__21_0_0,
            SUBPARTITION obj_unique_num__21_0_1,
            SUBPARTITION obj_unique_num__21_0_2,
            SUBPARTITION obj_unique_num__21_0_3,
            SUBPARTITION obj_unique_num__21_0_4,
            SUBPARTITION obj_unique_num__21_0_5,
            SUBPARTITION obj_unique_num__21_0_6,
            SUBPARTITION obj_unique_num__21_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__22`;
CREATE TABLE `obj_unique_num__22` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__22_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__22_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__22_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__22_0_0,
            SUBPARTITION obj_unique_num__22_0_1,
            SUBPARTITION obj_unique_num__22_0_2,
            SUBPARTITION obj_unique_num__22_0_3,
            SUBPARTITION obj_unique_num__22_0_4,
            SUBPARTITION obj_unique_num__22_0_5,
            SUBPARTITION obj_unique_num__22_0_6,
            SUBPARTITION obj_unique_num__22_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__23`;
CREATE TABLE `obj_unique_num__23` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__23_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__23_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__23_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__23_0_0,
            SUBPARTITION obj_unique_num__23_0_1,
            SUBPARTITION obj_unique_num__23_0_2,
            SUBPARTITION obj_unique_num__23_0_3,
            SUBPARTITION obj_unique_num__23_0_4,
            SUBPARTITION obj_unique_num__23_0_5,
            SUBPARTITION obj_unique_num__23_0_6,
            SUBPARTITION obj_unique_num__23_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__24`;
CREATE TABLE `obj_unique_num__24` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__24_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__24_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__24_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__24_0_0,
            SUBPARTITION obj_unique_num__24_0_1,
            SUBPARTITION obj_unique_num__24_0_2,
            SUBPARTITION obj_unique_num__24_0_3,
            SUBPARTITION obj_unique_num__24_0_4,
            SUBPARTITION obj_unique_num__24_0_5,
            SUBPARTITION obj_unique_num__24_0_6,
            SUBPARTITION obj_unique_num__24_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__25`;
CREATE TABLE `obj_unique_num__25` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__25_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__25_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__25_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__25_0_0,
            SUBPARTITION obj_unique_num__25_0_1,
            SUBPARTITION obj_unique_num__25_0_2,
            SUBPARTITION obj_unique_num__25_0_3,
            SUBPARTITION obj_unique_num__25_0_4,
            SUBPARTITION obj_unique_num__25_0_5,
            SUBPARTITION obj_unique_num__25_0_6,
            SUBPARTITION obj_unique_num__25_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__26`;
CREATE TABLE `obj_unique_num__26` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__26_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__26_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__26_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__26_0_0,
            SUBPARTITION obj_unique_num__26_0_1,
            SUBPARTITION obj_unique_num__26_0_2,
            SUBPARTITION obj_unique_num__26_0_3,
            SUBPARTITION obj_unique_num__26_0_4,
            SUBPARTITION obj_unique_num__26_0_5,
            SUBPARTITION obj_unique_num__26_0_6,
            SUBPARTITION obj_unique_num__26_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__27`;
CREATE TABLE `obj_unique_num__27` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__27_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__27_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__27_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__27_0_0,
            SUBPARTITION obj_unique_num__27_0_1,
            SUBPARTITION obj_unique_num__27_0_2,
            SUBPARTITION obj_unique_num__27_0_3,
            SUBPARTITION obj_unique_num__27_0_4,
            SUBPARTITION obj_unique_num__27_0_5,
            SUBPARTITION obj_unique_num__27_0_6,
            SUBPARTITION obj_unique_num__27_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__28`;
CREATE TABLE `obj_unique_num__28` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__28_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__28_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__28_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__28_0_0,
            SUBPARTITION obj_unique_num__28_0_1,
            SUBPARTITION obj_unique_num__28_0_2,
            SUBPARTITION obj_unique_num__28_0_3,
            SUBPARTITION obj_unique_num__28_0_4,
            SUBPARTITION obj_unique_num__28_0_5,
            SUBPARTITION obj_unique_num__28_0_6,
            SUBPARTITION obj_unique_num__28_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__29`;
CREATE TABLE `obj_unique_num__29` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__29_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__29_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__29_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__29_0_0,
            SUBPARTITION obj_unique_num__29_0_1,
            SUBPARTITION obj_unique_num__29_0_2,
            SUBPARTITION obj_unique_num__29_0_3,
            SUBPARTITION obj_unique_num__29_0_4,
            SUBPARTITION obj_unique_num__29_0_5,
            SUBPARTITION obj_unique_num__29_0_6,
            SUBPARTITION obj_unique_num__29_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__30`;
CREATE TABLE `obj_unique_num__30` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__30_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__30_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__30_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__30_0_0,
            SUBPARTITION obj_unique_num__30_0_1,
            SUBPARTITION obj_unique_num__30_0_2,
            SUBPARTITION obj_unique_num__30_0_3,
            SUBPARTITION obj_unique_num__30_0_4,
            SUBPARTITION obj_unique_num__30_0_5,
            SUBPARTITION obj_unique_num__30_0_6,
            SUBPARTITION obj_unique_num__30_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__31`;
CREATE TABLE `obj_unique_num__31` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__31_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__31_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__31_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__31_0_0,
            SUBPARTITION obj_unique_num__31_0_1,
            SUBPARTITION obj_unique_num__31_0_2,
            SUBPARTITION obj_unique_num__31_0_3,
            SUBPARTITION obj_unique_num__31_0_4,
            SUBPARTITION obj_unique_num__31_0_5,
            SUBPARTITION obj_unique_num__31_0_6,
            SUBPARTITION obj_unique_num__31_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__32`;
CREATE TABLE `obj_unique_num__32` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__32_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__32_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__32_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__32_0_0,
            SUBPARTITION obj_unique_num__32_0_1,
            SUBPARTITION obj_unique_num__32_0_2,
            SUBPARTITION obj_unique_num__32_0_3,
            SUBPARTITION obj_unique_num__32_0_4,
            SUBPARTITION obj_unique_num__32_0_5,
            SUBPARTITION obj_unique_num__32_0_6,
            SUBPARTITION obj_unique_num__32_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__33`;
CREATE TABLE `obj_unique_num__33` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__33_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__33_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__33_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__33_0_0,
            SUBPARTITION obj_unique_num__33_0_1,
            SUBPARTITION obj_unique_num__33_0_2,
            SUBPARTITION obj_unique_num__33_0_3,
            SUBPARTITION obj_unique_num__33_0_4,
            SUBPARTITION obj_unique_num__33_0_5,
            SUBPARTITION obj_unique_num__33_0_6,
            SUBPARTITION obj_unique_num__33_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__34`;
CREATE TABLE `obj_unique_num__34` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__34_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__34_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__34_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__34_0_0,
            SUBPARTITION obj_unique_num__34_0_1,
            SUBPARTITION obj_unique_num__34_0_2,
            SUBPARTITION obj_unique_num__34_0_3,
            SUBPARTITION obj_unique_num__34_0_4,
            SUBPARTITION obj_unique_num__34_0_5,
            SUBPARTITION obj_unique_num__34_0_6,
            SUBPARTITION obj_unique_num__34_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__35`;
CREATE TABLE `obj_unique_num__35` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__35_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__35_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__35_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__35_0_0,
            SUBPARTITION obj_unique_num__35_0_1,
            SUBPARTITION obj_unique_num__35_0_2,
            SUBPARTITION obj_unique_num__35_0_3,
            SUBPARTITION obj_unique_num__35_0_4,
            SUBPARTITION obj_unique_num__35_0_5,
            SUBPARTITION obj_unique_num__35_0_6,
            SUBPARTITION obj_unique_num__35_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__36`;
CREATE TABLE `obj_unique_num__36` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__36_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__36_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__36_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__36_0_0,
            SUBPARTITION obj_unique_num__36_0_1,
            SUBPARTITION obj_unique_num__36_0_2,
            SUBPARTITION obj_unique_num__36_0_3,
            SUBPARTITION obj_unique_num__36_0_4,
            SUBPARTITION obj_unique_num__36_0_5,
            SUBPARTITION obj_unique_num__36_0_6,
            SUBPARTITION obj_unique_num__36_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__37`;
CREATE TABLE `obj_unique_num__37` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__37_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__37_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__37_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__37_0_0,
            SUBPARTITION obj_unique_num__37_0_1,
            SUBPARTITION obj_unique_num__37_0_2,
            SUBPARTITION obj_unique_num__37_0_3,
            SUBPARTITION obj_unique_num__37_0_4,
            SUBPARTITION obj_unique_num__37_0_5,
            SUBPARTITION obj_unique_num__37_0_6,
            SUBPARTITION obj_unique_num__37_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__38`;
CREATE TABLE `obj_unique_num__38` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__38_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__38_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__38_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__38_0_0,
            SUBPARTITION obj_unique_num__38_0_1,
            SUBPARTITION obj_unique_num__38_0_2,
            SUBPARTITION obj_unique_num__38_0_3,
            SUBPARTITION obj_unique_num__38_0_4,
            SUBPARTITION obj_unique_num__38_0_5,
            SUBPARTITION obj_unique_num__38_0_6,
            SUBPARTITION obj_unique_num__38_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__39`;
CREATE TABLE `obj_unique_num__39` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__39_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__39_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__39_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__39_0_0,
            SUBPARTITION obj_unique_num__39_0_1,
            SUBPARTITION obj_unique_num__39_0_2,
            SUBPARTITION obj_unique_num__39_0_3,
            SUBPARTITION obj_unique_num__39_0_4,
            SUBPARTITION obj_unique_num__39_0_5,
            SUBPARTITION obj_unique_num__39_0_6,
            SUBPARTITION obj_unique_num__39_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__40`;
CREATE TABLE `obj_unique_num__40` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__40_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__40_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__40_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__40_0_0,
            SUBPARTITION obj_unique_num__40_0_1,
            SUBPARTITION obj_unique_num__40_0_2,
            SUBPARTITION obj_unique_num__40_0_3,
            SUBPARTITION obj_unique_num__40_0_4,
            SUBPARTITION obj_unique_num__40_0_5,
            SUBPARTITION obj_unique_num__40_0_6,
            SUBPARTITION obj_unique_num__40_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__41`;
CREATE TABLE `obj_unique_num__41` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__41_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__41_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__41_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__41_0_0,
            SUBPARTITION obj_unique_num__41_0_1,
            SUBPARTITION obj_unique_num__41_0_2,
            SUBPARTITION obj_unique_num__41_0_3,
            SUBPARTITION obj_unique_num__41_0_4,
            SUBPARTITION obj_unique_num__41_0_5,
            SUBPARTITION obj_unique_num__41_0_6,
            SUBPARTITION obj_unique_num__41_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__42`;
CREATE TABLE `obj_unique_num__42` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__42_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__42_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__42_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__42_0_0,
            SUBPARTITION obj_unique_num__42_0_1,
            SUBPARTITION obj_unique_num__42_0_2,
            SUBPARTITION obj_unique_num__42_0_3,
            SUBPARTITION obj_unique_num__42_0_4,
            SUBPARTITION obj_unique_num__42_0_5,
            SUBPARTITION obj_unique_num__42_0_6,
            SUBPARTITION obj_unique_num__42_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__43`;
CREATE TABLE `obj_unique_num__43` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__43_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__43_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__43_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__43_0_0,
            SUBPARTITION obj_unique_num__43_0_1,
            SUBPARTITION obj_unique_num__43_0_2,
            SUBPARTITION obj_unique_num__43_0_3,
            SUBPARTITION obj_unique_num__43_0_4,
            SUBPARTITION obj_unique_num__43_0_5,
            SUBPARTITION obj_unique_num__43_0_6,
            SUBPARTITION obj_unique_num__43_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__44`;
CREATE TABLE `obj_unique_num__44` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__44_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__44_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__44_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__44_0_0,
            SUBPARTITION obj_unique_num__44_0_1,
            SUBPARTITION obj_unique_num__44_0_2,
            SUBPARTITION obj_unique_num__44_0_3,
            SUBPARTITION obj_unique_num__44_0_4,
            SUBPARTITION obj_unique_num__44_0_5,
            SUBPARTITION obj_unique_num__44_0_6,
            SUBPARTITION obj_unique_num__44_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__45`;
CREATE TABLE `obj_unique_num__45` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__45_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__45_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__45_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__45_0_0,
            SUBPARTITION obj_unique_num__45_0_1,
            SUBPARTITION obj_unique_num__45_0_2,
            SUBPARTITION obj_unique_num__45_0_3,
            SUBPARTITION obj_unique_num__45_0_4,
            SUBPARTITION obj_unique_num__45_0_5,
            SUBPARTITION obj_unique_num__45_0_6,
            SUBPARTITION obj_unique_num__45_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__46`;
CREATE TABLE `obj_unique_num__46` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__46_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__46_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__46_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__46_0_0,
            SUBPARTITION obj_unique_num__46_0_1,
            SUBPARTITION obj_unique_num__46_0_2,
            SUBPARTITION obj_unique_num__46_0_3,
            SUBPARTITION obj_unique_num__46_0_4,
            SUBPARTITION obj_unique_num__46_0_5,
            SUBPARTITION obj_unique_num__46_0_6,
            SUBPARTITION obj_unique_num__46_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__47`;
CREATE TABLE `obj_unique_num__47` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__47_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__47_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__47_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__47_0_0,
            SUBPARTITION obj_unique_num__47_0_1,
            SUBPARTITION obj_unique_num__47_0_2,
            SUBPARTITION obj_unique_num__47_0_3,
            SUBPARTITION obj_unique_num__47_0_4,
            SUBPARTITION obj_unique_num__47_0_5,
            SUBPARTITION obj_unique_num__47_0_6,
            SUBPARTITION obj_unique_num__47_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__48`;
CREATE TABLE `obj_unique_num__48` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__48_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__48_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__48_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__48_0_0,
            SUBPARTITION obj_unique_num__48_0_1,
            SUBPARTITION obj_unique_num__48_0_2,
            SUBPARTITION obj_unique_num__48_0_3,
            SUBPARTITION obj_unique_num__48_0_4,
            SUBPARTITION obj_unique_num__48_0_5,
            SUBPARTITION obj_unique_num__48_0_6,
            SUBPARTITION obj_unique_num__48_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__49`;
CREATE TABLE `obj_unique_num__49` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__49_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__49_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__49_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__49_0_0,
            SUBPARTITION obj_unique_num__49_0_1,
            SUBPARTITION obj_unique_num__49_0_2,
            SUBPARTITION obj_unique_num__49_0_3,
            SUBPARTITION obj_unique_num__49_0_4,
            SUBPARTITION obj_unique_num__49_0_5,
            SUBPARTITION obj_unique_num__49_0_6,
            SUBPARTITION obj_unique_num__49_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__50`;
CREATE TABLE `obj_unique_num__50` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__50_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__50_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__50_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__50_0_0,
            SUBPARTITION obj_unique_num__50_0_1,
            SUBPARTITION obj_unique_num__50_0_2,
            SUBPARTITION obj_unique_num__50_0_3,
            SUBPARTITION obj_unique_num__50_0_4,
            SUBPARTITION obj_unique_num__50_0_5,
            SUBPARTITION obj_unique_num__50_0_6,
            SUBPARTITION obj_unique_num__50_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__51`;
CREATE TABLE `obj_unique_num__51` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__51_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__51_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__51_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__51_0_0,
            SUBPARTITION obj_unique_num__51_0_1,
            SUBPARTITION obj_unique_num__51_0_2,
            SUBPARTITION obj_unique_num__51_0_3,
            SUBPARTITION obj_unique_num__51_0_4,
            SUBPARTITION obj_unique_num__51_0_5,
            SUBPARTITION obj_unique_num__51_0_6,
            SUBPARTITION obj_unique_num__51_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__52`;
CREATE TABLE `obj_unique_num__52` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__52_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__52_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__52_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__52_0_0,
            SUBPARTITION obj_unique_num__52_0_1,
            SUBPARTITION obj_unique_num__52_0_2,
            SUBPARTITION obj_unique_num__52_0_3,
            SUBPARTITION obj_unique_num__52_0_4,
            SUBPARTITION obj_unique_num__52_0_5,
            SUBPARTITION obj_unique_num__52_0_6,
            SUBPARTITION obj_unique_num__52_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__53`;
CREATE TABLE `obj_unique_num__53` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__53_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__53_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__53_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__53_0_0,
            SUBPARTITION obj_unique_num__53_0_1,
            SUBPARTITION obj_unique_num__53_0_2,
            SUBPARTITION obj_unique_num__53_0_3,
            SUBPARTITION obj_unique_num__53_0_4,
            SUBPARTITION obj_unique_num__53_0_5,
            SUBPARTITION obj_unique_num__53_0_6,
            SUBPARTITION obj_unique_num__53_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__54`;
CREATE TABLE `obj_unique_num__54` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__54_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__54_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__54_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__54_0_0,
            SUBPARTITION obj_unique_num__54_0_1,
            SUBPARTITION obj_unique_num__54_0_2,
            SUBPARTITION obj_unique_num__54_0_3,
            SUBPARTITION obj_unique_num__54_0_4,
            SUBPARTITION obj_unique_num__54_0_5,
            SUBPARTITION obj_unique_num__54_0_6,
            SUBPARTITION obj_unique_num__54_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__55`;
CREATE TABLE `obj_unique_num__55` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__55_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__55_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__55_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__55_0_0,
            SUBPARTITION obj_unique_num__55_0_1,
            SUBPARTITION obj_unique_num__55_0_2,
            SUBPARTITION obj_unique_num__55_0_3,
            SUBPARTITION obj_unique_num__55_0_4,
            SUBPARTITION obj_unique_num__55_0_5,
            SUBPARTITION obj_unique_num__55_0_6,
            SUBPARTITION obj_unique_num__55_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__56`;
CREATE TABLE `obj_unique_num__56` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__56_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__56_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__56_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__56_0_0,
            SUBPARTITION obj_unique_num__56_0_1,
            SUBPARTITION obj_unique_num__56_0_2,
            SUBPARTITION obj_unique_num__56_0_3,
            SUBPARTITION obj_unique_num__56_0_4,
            SUBPARTITION obj_unique_num__56_0_5,
            SUBPARTITION obj_unique_num__56_0_6,
            SUBPARTITION obj_unique_num__56_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__57`;
CREATE TABLE `obj_unique_num__57` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__57_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__57_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__57_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__57_0_0,
            SUBPARTITION obj_unique_num__57_0_1,
            SUBPARTITION obj_unique_num__57_0_2,
            SUBPARTITION obj_unique_num__57_0_3,
            SUBPARTITION obj_unique_num__57_0_4,
            SUBPARTITION obj_unique_num__57_0_5,
            SUBPARTITION obj_unique_num__57_0_6,
            SUBPARTITION obj_unique_num__57_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__58`;
CREATE TABLE `obj_unique_num__58` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__58_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__58_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__58_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__58_0_0,
            SUBPARTITION obj_unique_num__58_0_1,
            SUBPARTITION obj_unique_num__58_0_2,
            SUBPARTITION obj_unique_num__58_0_3,
            SUBPARTITION obj_unique_num__58_0_4,
            SUBPARTITION obj_unique_num__58_0_5,
            SUBPARTITION obj_unique_num__58_0_6,
            SUBPARTITION obj_unique_num__58_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__59`;
CREATE TABLE `obj_unique_num__59` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__59_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__59_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__59_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__59_0_0,
            SUBPARTITION obj_unique_num__59_0_1,
            SUBPARTITION obj_unique_num__59_0_2,
            SUBPARTITION obj_unique_num__59_0_3,
            SUBPARTITION obj_unique_num__59_0_4,
            SUBPARTITION obj_unique_num__59_0_5,
            SUBPARTITION obj_unique_num__59_0_6,
            SUBPARTITION obj_unique_num__59_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__60`;
CREATE TABLE `obj_unique_num__60` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__60_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__60_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__60_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__60_0_0,
            SUBPARTITION obj_unique_num__60_0_1,
            SUBPARTITION obj_unique_num__60_0_2,
            SUBPARTITION obj_unique_num__60_0_3,
            SUBPARTITION obj_unique_num__60_0_4,
            SUBPARTITION obj_unique_num__60_0_5,
            SUBPARTITION obj_unique_num__60_0_6,
            SUBPARTITION obj_unique_num__60_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__61`;
CREATE TABLE `obj_unique_num__61` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__61_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__61_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__61_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__61_0_0,
            SUBPARTITION obj_unique_num__61_0_1,
            SUBPARTITION obj_unique_num__61_0_2,
            SUBPARTITION obj_unique_num__61_0_3,
            SUBPARTITION obj_unique_num__61_0_4,
            SUBPARTITION obj_unique_num__61_0_5,
            SUBPARTITION obj_unique_num__61_0_6,
            SUBPARTITION obj_unique_num__61_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__62`;
CREATE TABLE `obj_unique_num__62` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__62_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__62_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__62_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__62_0_0,
            SUBPARTITION obj_unique_num__62_0_1,
            SUBPARTITION obj_unique_num__62_0_2,
            SUBPARTITION obj_unique_num__62_0_3,
            SUBPARTITION obj_unique_num__62_0_4,
            SUBPARTITION obj_unique_num__62_0_5,
            SUBPARTITION obj_unique_num__62_0_6,
            SUBPARTITION obj_unique_num__62_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__63`;
CREATE TABLE `obj_unique_num__63` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__63_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__63_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__63_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__63_0_0,
            SUBPARTITION obj_unique_num__63_0_1,
            SUBPARTITION obj_unique_num__63_0_2,
            SUBPARTITION obj_unique_num__63_0_3,
            SUBPARTITION obj_unique_num__63_0_4,
            SUBPARTITION obj_unique_num__63_0_5,
            SUBPARTITION obj_unique_num__63_0_6,
            SUBPARTITION obj_unique_num__63_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__64`;
CREATE TABLE `obj_unique_num__64` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__64_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__64_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__64_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__64_0_0,
            SUBPARTITION obj_unique_num__64_0_1,
            SUBPARTITION obj_unique_num__64_0_2,
            SUBPARTITION obj_unique_num__64_0_3,
            SUBPARTITION obj_unique_num__64_0_4,
            SUBPARTITION obj_unique_num__64_0_5,
            SUBPARTITION obj_unique_num__64_0_6,
            SUBPARTITION obj_unique_num__64_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__65`;
CREATE TABLE `obj_unique_num__65` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__65_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__65_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__65_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__65_0_0,
            SUBPARTITION obj_unique_num__65_0_1,
            SUBPARTITION obj_unique_num__65_0_2,
            SUBPARTITION obj_unique_num__65_0_3,
            SUBPARTITION obj_unique_num__65_0_4,
            SUBPARTITION obj_unique_num__65_0_5,
            SUBPARTITION obj_unique_num__65_0_6,
            SUBPARTITION obj_unique_num__65_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__66`;
CREATE TABLE `obj_unique_num__66` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__66_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__66_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__66_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__66_0_0,
            SUBPARTITION obj_unique_num__66_0_1,
            SUBPARTITION obj_unique_num__66_0_2,
            SUBPARTITION obj_unique_num__66_0_3,
            SUBPARTITION obj_unique_num__66_0_4,
            SUBPARTITION obj_unique_num__66_0_5,
            SUBPARTITION obj_unique_num__66_0_6,
            SUBPARTITION obj_unique_num__66_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__67`;
CREATE TABLE `obj_unique_num__67` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__67_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__67_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__67_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__67_0_0,
            SUBPARTITION obj_unique_num__67_0_1,
            SUBPARTITION obj_unique_num__67_0_2,
            SUBPARTITION obj_unique_num__67_0_3,
            SUBPARTITION obj_unique_num__67_0_4,
            SUBPARTITION obj_unique_num__67_0_5,
            SUBPARTITION obj_unique_num__67_0_6,
            SUBPARTITION obj_unique_num__67_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__68`;
CREATE TABLE `obj_unique_num__68` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__68_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__68_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__68_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__68_0_0,
            SUBPARTITION obj_unique_num__68_0_1,
            SUBPARTITION obj_unique_num__68_0_2,
            SUBPARTITION obj_unique_num__68_0_3,
            SUBPARTITION obj_unique_num__68_0_4,
            SUBPARTITION obj_unique_num__68_0_5,
            SUBPARTITION obj_unique_num__68_0_6,
            SUBPARTITION obj_unique_num__68_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__69`;
CREATE TABLE `obj_unique_num__69` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__69_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__69_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__69_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__69_0_0,
            SUBPARTITION obj_unique_num__69_0_1,
            SUBPARTITION obj_unique_num__69_0_2,
            SUBPARTITION obj_unique_num__69_0_3,
            SUBPARTITION obj_unique_num__69_0_4,
            SUBPARTITION obj_unique_num__69_0_5,
            SUBPARTITION obj_unique_num__69_0_6,
            SUBPARTITION obj_unique_num__69_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__70`;
CREATE TABLE `obj_unique_num__70` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__70_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__70_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__70_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__70_0_0,
            SUBPARTITION obj_unique_num__70_0_1,
            SUBPARTITION obj_unique_num__70_0_2,
            SUBPARTITION obj_unique_num__70_0_3,
            SUBPARTITION obj_unique_num__70_0_4,
            SUBPARTITION obj_unique_num__70_0_5,
            SUBPARTITION obj_unique_num__70_0_6,
            SUBPARTITION obj_unique_num__70_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__71`;
CREATE TABLE `obj_unique_num__71` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__71_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__71_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__71_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__71_0_0,
            SUBPARTITION obj_unique_num__71_0_1,
            SUBPARTITION obj_unique_num__71_0_2,
            SUBPARTITION obj_unique_num__71_0_3,
            SUBPARTITION obj_unique_num__71_0_4,
            SUBPARTITION obj_unique_num__71_0_5,
            SUBPARTITION obj_unique_num__71_0_6,
            SUBPARTITION obj_unique_num__71_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__72`;
CREATE TABLE `obj_unique_num__72` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__72_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__72_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__72_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__72_0_0,
            SUBPARTITION obj_unique_num__72_0_1,
            SUBPARTITION obj_unique_num__72_0_2,
            SUBPARTITION obj_unique_num__72_0_3,
            SUBPARTITION obj_unique_num__72_0_4,
            SUBPARTITION obj_unique_num__72_0_5,
            SUBPARTITION obj_unique_num__72_0_6,
            SUBPARTITION obj_unique_num__72_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__73`;
CREATE TABLE `obj_unique_num__73` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__73_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__73_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__73_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__73_0_0,
            SUBPARTITION obj_unique_num__73_0_1,
            SUBPARTITION obj_unique_num__73_0_2,
            SUBPARTITION obj_unique_num__73_0_3,
            SUBPARTITION obj_unique_num__73_0_4,
            SUBPARTITION obj_unique_num__73_0_5,
            SUBPARTITION obj_unique_num__73_0_6,
            SUBPARTITION obj_unique_num__73_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__74`;
CREATE TABLE `obj_unique_num__74` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__74_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__74_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__74_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__74_0_0,
            SUBPARTITION obj_unique_num__74_0_1,
            SUBPARTITION obj_unique_num__74_0_2,
            SUBPARTITION obj_unique_num__74_0_3,
            SUBPARTITION obj_unique_num__74_0_4,
            SUBPARTITION obj_unique_num__74_0_5,
            SUBPARTITION obj_unique_num__74_0_6,
            SUBPARTITION obj_unique_num__74_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__75`;
CREATE TABLE `obj_unique_num__75` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__75_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__75_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__75_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__75_0_0,
            SUBPARTITION obj_unique_num__75_0_1,
            SUBPARTITION obj_unique_num__75_0_2,
            SUBPARTITION obj_unique_num__75_0_3,
            SUBPARTITION obj_unique_num__75_0_4,
            SUBPARTITION obj_unique_num__75_0_5,
            SUBPARTITION obj_unique_num__75_0_6,
            SUBPARTITION obj_unique_num__75_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__76`;
CREATE TABLE `obj_unique_num__76` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__76_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__76_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__76_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__76_0_0,
            SUBPARTITION obj_unique_num__76_0_1,
            SUBPARTITION obj_unique_num__76_0_2,
            SUBPARTITION obj_unique_num__76_0_3,
            SUBPARTITION obj_unique_num__76_0_4,
            SUBPARTITION obj_unique_num__76_0_5,
            SUBPARTITION obj_unique_num__76_0_6,
            SUBPARTITION obj_unique_num__76_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__77`;
CREATE TABLE `obj_unique_num__77` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__77_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__77_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__77_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__77_0_0,
            SUBPARTITION obj_unique_num__77_0_1,
            SUBPARTITION obj_unique_num__77_0_2,
            SUBPARTITION obj_unique_num__77_0_3,
            SUBPARTITION obj_unique_num__77_0_4,
            SUBPARTITION obj_unique_num__77_0_5,
            SUBPARTITION obj_unique_num__77_0_6,
            SUBPARTITION obj_unique_num__77_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__78`;
CREATE TABLE `obj_unique_num__78` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__78_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__78_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__78_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__78_0_0,
            SUBPARTITION obj_unique_num__78_0_1,
            SUBPARTITION obj_unique_num__78_0_2,
            SUBPARTITION obj_unique_num__78_0_3,
            SUBPARTITION obj_unique_num__78_0_4,
            SUBPARTITION obj_unique_num__78_0_5,
            SUBPARTITION obj_unique_num__78_0_6,
            SUBPARTITION obj_unique_num__78_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__79`;
CREATE TABLE `obj_unique_num__79` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__79_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__79_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__79_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__79_0_0,
            SUBPARTITION obj_unique_num__79_0_1,
            SUBPARTITION obj_unique_num__79_0_2,
            SUBPARTITION obj_unique_num__79_0_3,
            SUBPARTITION obj_unique_num__79_0_4,
            SUBPARTITION obj_unique_num__79_0_5,
            SUBPARTITION obj_unique_num__79_0_6,
            SUBPARTITION obj_unique_num__79_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__80`;
CREATE TABLE `obj_unique_num__80` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__80_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__80_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__80_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__80_0_0,
            SUBPARTITION obj_unique_num__80_0_1,
            SUBPARTITION obj_unique_num__80_0_2,
            SUBPARTITION obj_unique_num__80_0_3,
            SUBPARTITION obj_unique_num__80_0_4,
            SUBPARTITION obj_unique_num__80_0_5,
            SUBPARTITION obj_unique_num__80_0_6,
            SUBPARTITION obj_unique_num__80_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__81`;
CREATE TABLE `obj_unique_num__81` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__81_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__81_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__81_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__81_0_0,
            SUBPARTITION obj_unique_num__81_0_1,
            SUBPARTITION obj_unique_num__81_0_2,
            SUBPARTITION obj_unique_num__81_0_3,
            SUBPARTITION obj_unique_num__81_0_4,
            SUBPARTITION obj_unique_num__81_0_5,
            SUBPARTITION obj_unique_num__81_0_6,
            SUBPARTITION obj_unique_num__81_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__82`;
CREATE TABLE `obj_unique_num__82` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__82_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__82_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__82_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__82_0_0,
            SUBPARTITION obj_unique_num__82_0_1,
            SUBPARTITION obj_unique_num__82_0_2,
            SUBPARTITION obj_unique_num__82_0_3,
            SUBPARTITION obj_unique_num__82_0_4,
            SUBPARTITION obj_unique_num__82_0_5,
            SUBPARTITION obj_unique_num__82_0_6,
            SUBPARTITION obj_unique_num__82_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__83`;
CREATE TABLE `obj_unique_num__83` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__83_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__83_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__83_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__83_0_0,
            SUBPARTITION obj_unique_num__83_0_1,
            SUBPARTITION obj_unique_num__83_0_2,
            SUBPARTITION obj_unique_num__83_0_3,
            SUBPARTITION obj_unique_num__83_0_4,
            SUBPARTITION obj_unique_num__83_0_5,
            SUBPARTITION obj_unique_num__83_0_6,
            SUBPARTITION obj_unique_num__83_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__84`;
CREATE TABLE `obj_unique_num__84` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__84_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__84_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__84_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__84_0_0,
            SUBPARTITION obj_unique_num__84_0_1,
            SUBPARTITION obj_unique_num__84_0_2,
            SUBPARTITION obj_unique_num__84_0_3,
            SUBPARTITION obj_unique_num__84_0_4,
            SUBPARTITION obj_unique_num__84_0_5,
            SUBPARTITION obj_unique_num__84_0_6,
            SUBPARTITION obj_unique_num__84_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__85`;
CREATE TABLE `obj_unique_num__85` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__85_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__85_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__85_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__85_0_0,
            SUBPARTITION obj_unique_num__85_0_1,
            SUBPARTITION obj_unique_num__85_0_2,
            SUBPARTITION obj_unique_num__85_0_3,
            SUBPARTITION obj_unique_num__85_0_4,
            SUBPARTITION obj_unique_num__85_0_5,
            SUBPARTITION obj_unique_num__85_0_6,
            SUBPARTITION obj_unique_num__85_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__86`;
CREATE TABLE `obj_unique_num__86` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__86_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__86_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__86_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__86_0_0,
            SUBPARTITION obj_unique_num__86_0_1,
            SUBPARTITION obj_unique_num__86_0_2,
            SUBPARTITION obj_unique_num__86_0_3,
            SUBPARTITION obj_unique_num__86_0_4,
            SUBPARTITION obj_unique_num__86_0_5,
            SUBPARTITION obj_unique_num__86_0_6,
            SUBPARTITION obj_unique_num__86_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__87`;
CREATE TABLE `obj_unique_num__87` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__87_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__87_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__87_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__87_0_0,
            SUBPARTITION obj_unique_num__87_0_1,
            SUBPARTITION obj_unique_num__87_0_2,
            SUBPARTITION obj_unique_num__87_0_3,
            SUBPARTITION obj_unique_num__87_0_4,
            SUBPARTITION obj_unique_num__87_0_5,
            SUBPARTITION obj_unique_num__87_0_6,
            SUBPARTITION obj_unique_num__87_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__88`;
CREATE TABLE `obj_unique_num__88` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__88_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__88_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__88_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__88_0_0,
            SUBPARTITION obj_unique_num__88_0_1,
            SUBPARTITION obj_unique_num__88_0_2,
            SUBPARTITION obj_unique_num__88_0_3,
            SUBPARTITION obj_unique_num__88_0_4,
            SUBPARTITION obj_unique_num__88_0_5,
            SUBPARTITION obj_unique_num__88_0_6,
            SUBPARTITION obj_unique_num__88_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__89`;
CREATE TABLE `obj_unique_num__89` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__89_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__89_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__89_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__89_0_0,
            SUBPARTITION obj_unique_num__89_0_1,
            SUBPARTITION obj_unique_num__89_0_2,
            SUBPARTITION obj_unique_num__89_0_3,
            SUBPARTITION obj_unique_num__89_0_4,
            SUBPARTITION obj_unique_num__89_0_5,
            SUBPARTITION obj_unique_num__89_0_6,
            SUBPARTITION obj_unique_num__89_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__90`;
CREATE TABLE `obj_unique_num__90` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__90_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__90_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__90_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__90_0_0,
            SUBPARTITION obj_unique_num__90_0_1,
            SUBPARTITION obj_unique_num__90_0_2,
            SUBPARTITION obj_unique_num__90_0_3,
            SUBPARTITION obj_unique_num__90_0_4,
            SUBPARTITION obj_unique_num__90_0_5,
            SUBPARTITION obj_unique_num__90_0_6,
            SUBPARTITION obj_unique_num__90_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__91`;
CREATE TABLE `obj_unique_num__91` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__91_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__91_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__91_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__91_0_0,
            SUBPARTITION obj_unique_num__91_0_1,
            SUBPARTITION obj_unique_num__91_0_2,
            SUBPARTITION obj_unique_num__91_0_3,
            SUBPARTITION obj_unique_num__91_0_4,
            SUBPARTITION obj_unique_num__91_0_5,
            SUBPARTITION obj_unique_num__91_0_6,
            SUBPARTITION obj_unique_num__91_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__92`;
CREATE TABLE `obj_unique_num__92` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__92_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__92_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__92_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__92_0_0,
            SUBPARTITION obj_unique_num__92_0_1,
            SUBPARTITION obj_unique_num__92_0_2,
            SUBPARTITION obj_unique_num__92_0_3,
            SUBPARTITION obj_unique_num__92_0_4,
            SUBPARTITION obj_unique_num__92_0_5,
            SUBPARTITION obj_unique_num__92_0_6,
            SUBPARTITION obj_unique_num__92_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__93`;
CREATE TABLE `obj_unique_num__93` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__93_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__93_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__93_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__93_0_0,
            SUBPARTITION obj_unique_num__93_0_1,
            SUBPARTITION obj_unique_num__93_0_2,
            SUBPARTITION obj_unique_num__93_0_3,
            SUBPARTITION obj_unique_num__93_0_4,
            SUBPARTITION obj_unique_num__93_0_5,
            SUBPARTITION obj_unique_num__93_0_6,
            SUBPARTITION obj_unique_num__93_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__94`;
CREATE TABLE `obj_unique_num__94` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__94_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__94_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__94_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__94_0_0,
            SUBPARTITION obj_unique_num__94_0_1,
            SUBPARTITION obj_unique_num__94_0_2,
            SUBPARTITION obj_unique_num__94_0_3,
            SUBPARTITION obj_unique_num__94_0_4,
            SUBPARTITION obj_unique_num__94_0_5,
            SUBPARTITION obj_unique_num__94_0_6,
            SUBPARTITION obj_unique_num__94_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__95`;
CREATE TABLE `obj_unique_num__95` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__95_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__95_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__95_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__95_0_0,
            SUBPARTITION obj_unique_num__95_0_1,
            SUBPARTITION obj_unique_num__95_0_2,
            SUBPARTITION obj_unique_num__95_0_3,
            SUBPARTITION obj_unique_num__95_0_4,
            SUBPARTITION obj_unique_num__95_0_5,
            SUBPARTITION obj_unique_num__95_0_6,
            SUBPARTITION obj_unique_num__95_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__96`;
CREATE TABLE `obj_unique_num__96` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__96_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__96_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__96_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__96_0_0,
            SUBPARTITION obj_unique_num__96_0_1,
            SUBPARTITION obj_unique_num__96_0_2,
            SUBPARTITION obj_unique_num__96_0_3,
            SUBPARTITION obj_unique_num__96_0_4,
            SUBPARTITION obj_unique_num__96_0_5,
            SUBPARTITION obj_unique_num__96_0_6,
            SUBPARTITION obj_unique_num__96_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__97`;
CREATE TABLE `obj_unique_num__97` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__97_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__97_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__97_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__97_0_0,
            SUBPARTITION obj_unique_num__97_0_1,
            SUBPARTITION obj_unique_num__97_0_2,
            SUBPARTITION obj_unique_num__97_0_3,
            SUBPARTITION obj_unique_num__97_0_4,
            SUBPARTITION obj_unique_num__97_0_5,
            SUBPARTITION obj_unique_num__97_0_6,
            SUBPARTITION obj_unique_num__97_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__98`;
CREATE TABLE `obj_unique_num__98` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__98_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__98_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__98_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__98_0_0,
            SUBPARTITION obj_unique_num__98_0_1,
            SUBPARTITION obj_unique_num__98_0_2,
            SUBPARTITION obj_unique_num__98_0_3,
            SUBPARTITION obj_unique_num__98_0_4,
            SUBPARTITION obj_unique_num__98_0_5,
            SUBPARTITION obj_unique_num__98_0_6,
            SUBPARTITION obj_unique_num__98_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__99`;
CREATE TABLE `obj_unique_num__99` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__99_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__99_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__99_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__99_0_0,
            SUBPARTITION obj_unique_num__99_0_1,
            SUBPARTITION obj_unique_num__99_0_2,
            SUBPARTITION obj_unique_num__99_0_3,
            SUBPARTITION obj_unique_num__99_0_4,
            SUBPARTITION obj_unique_num__99_0_5,
            SUBPARTITION obj_unique_num__99_0_6,
            SUBPARTITION obj_unique_num__99_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__100`;
CREATE TABLE `obj_unique_num__100` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__100_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__100_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__100_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__100_0_0,
            SUBPARTITION obj_unique_num__100_0_1,
            SUBPARTITION obj_unique_num__100_0_2,
            SUBPARTITION obj_unique_num__100_0_3,
            SUBPARTITION obj_unique_num__100_0_4,
            SUBPARTITION obj_unique_num__100_0_5,
            SUBPARTITION obj_unique_num__100_0_6,
            SUBPARTITION obj_unique_num__100_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__101`;
CREATE TABLE `obj_unique_num__101` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__101_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__101_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__101_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__101_0_0,
            SUBPARTITION obj_unique_num__101_0_1,
            SUBPARTITION obj_unique_num__101_0_2,
            SUBPARTITION obj_unique_num__101_0_3,
            SUBPARTITION obj_unique_num__101_0_4,
            SUBPARTITION obj_unique_num__101_0_5,
            SUBPARTITION obj_unique_num__101_0_6,
            SUBPARTITION obj_unique_num__101_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__102`;
CREATE TABLE `obj_unique_num__102` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__102_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__102_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__102_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__102_0_0,
            SUBPARTITION obj_unique_num__102_0_1,
            SUBPARTITION obj_unique_num__102_0_2,
            SUBPARTITION obj_unique_num__102_0_3,
            SUBPARTITION obj_unique_num__102_0_4,
            SUBPARTITION obj_unique_num__102_0_5,
            SUBPARTITION obj_unique_num__102_0_6,
            SUBPARTITION obj_unique_num__102_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__103`;
CREATE TABLE `obj_unique_num__103` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__103_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__103_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__103_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__103_0_0,
            SUBPARTITION obj_unique_num__103_0_1,
            SUBPARTITION obj_unique_num__103_0_2,
            SUBPARTITION obj_unique_num__103_0_3,
            SUBPARTITION obj_unique_num__103_0_4,
            SUBPARTITION obj_unique_num__103_0_5,
            SUBPARTITION obj_unique_num__103_0_6,
            SUBPARTITION obj_unique_num__103_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__104`;
CREATE TABLE `obj_unique_num__104` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__104_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__104_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__104_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__104_0_0,
            SUBPARTITION obj_unique_num__104_0_1,
            SUBPARTITION obj_unique_num__104_0_2,
            SUBPARTITION obj_unique_num__104_0_3,
            SUBPARTITION obj_unique_num__104_0_4,
            SUBPARTITION obj_unique_num__104_0_5,
            SUBPARTITION obj_unique_num__104_0_6,
            SUBPARTITION obj_unique_num__104_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__105`;
CREATE TABLE `obj_unique_num__105` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__105_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__105_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__105_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__105_0_0,
            SUBPARTITION obj_unique_num__105_0_1,
            SUBPARTITION obj_unique_num__105_0_2,
            SUBPARTITION obj_unique_num__105_0_3,
            SUBPARTITION obj_unique_num__105_0_4,
            SUBPARTITION obj_unique_num__105_0_5,
            SUBPARTITION obj_unique_num__105_0_6,
            SUBPARTITION obj_unique_num__105_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__106`;
CREATE TABLE `obj_unique_num__106` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__106_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__106_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__106_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__106_0_0,
            SUBPARTITION obj_unique_num__106_0_1,
            SUBPARTITION obj_unique_num__106_0_2,
            SUBPARTITION obj_unique_num__106_0_3,
            SUBPARTITION obj_unique_num__106_0_4,
            SUBPARTITION obj_unique_num__106_0_5,
            SUBPARTITION obj_unique_num__106_0_6,
            SUBPARTITION obj_unique_num__106_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__107`;
CREATE TABLE `obj_unique_num__107` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__107_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__107_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__107_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__107_0_0,
            SUBPARTITION obj_unique_num__107_0_1,
            SUBPARTITION obj_unique_num__107_0_2,
            SUBPARTITION obj_unique_num__107_0_3,
            SUBPARTITION obj_unique_num__107_0_4,
            SUBPARTITION obj_unique_num__107_0_5,
            SUBPARTITION obj_unique_num__107_0_6,
            SUBPARTITION obj_unique_num__107_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__108`;
CREATE TABLE `obj_unique_num__108` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__108_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__108_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__108_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__108_0_0,
            SUBPARTITION obj_unique_num__108_0_1,
            SUBPARTITION obj_unique_num__108_0_2,
            SUBPARTITION obj_unique_num__108_0_3,
            SUBPARTITION obj_unique_num__108_0_4,
            SUBPARTITION obj_unique_num__108_0_5,
            SUBPARTITION obj_unique_num__108_0_6,
            SUBPARTITION obj_unique_num__108_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__109`;
CREATE TABLE `obj_unique_num__109` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__109_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__109_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__109_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__109_0_0,
            SUBPARTITION obj_unique_num__109_0_1,
            SUBPARTITION obj_unique_num__109_0_2,
            SUBPARTITION obj_unique_num__109_0_3,
            SUBPARTITION obj_unique_num__109_0_4,
            SUBPARTITION obj_unique_num__109_0_5,
            SUBPARTITION obj_unique_num__109_0_6,
            SUBPARTITION obj_unique_num__109_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__110`;
CREATE TABLE `obj_unique_num__110` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__110_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__110_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__110_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__110_0_0,
            SUBPARTITION obj_unique_num__110_0_1,
            SUBPARTITION obj_unique_num__110_0_2,
            SUBPARTITION obj_unique_num__110_0_3,
            SUBPARTITION obj_unique_num__110_0_4,
            SUBPARTITION obj_unique_num__110_0_5,
            SUBPARTITION obj_unique_num__110_0_6,
            SUBPARTITION obj_unique_num__110_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__111`;
CREATE TABLE `obj_unique_num__111` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__111_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__111_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__111_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__111_0_0,
            SUBPARTITION obj_unique_num__111_0_1,
            SUBPARTITION obj_unique_num__111_0_2,
            SUBPARTITION obj_unique_num__111_0_3,
            SUBPARTITION obj_unique_num__111_0_4,
            SUBPARTITION obj_unique_num__111_0_5,
            SUBPARTITION obj_unique_num__111_0_6,
            SUBPARTITION obj_unique_num__111_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__112`;
CREATE TABLE `obj_unique_num__112` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__112_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__112_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__112_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__112_0_0,
            SUBPARTITION obj_unique_num__112_0_1,
            SUBPARTITION obj_unique_num__112_0_2,
            SUBPARTITION obj_unique_num__112_0_3,
            SUBPARTITION obj_unique_num__112_0_4,
            SUBPARTITION obj_unique_num__112_0_5,
            SUBPARTITION obj_unique_num__112_0_6,
            SUBPARTITION obj_unique_num__112_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__113`;
CREATE TABLE `obj_unique_num__113` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__113_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__113_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__113_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__113_0_0,
            SUBPARTITION obj_unique_num__113_0_1,
            SUBPARTITION obj_unique_num__113_0_2,
            SUBPARTITION obj_unique_num__113_0_3,
            SUBPARTITION obj_unique_num__113_0_4,
            SUBPARTITION obj_unique_num__113_0_5,
            SUBPARTITION obj_unique_num__113_0_6,
            SUBPARTITION obj_unique_num__113_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__114`;
CREATE TABLE `obj_unique_num__114` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__114_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__114_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__114_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__114_0_0,
            SUBPARTITION obj_unique_num__114_0_1,
            SUBPARTITION obj_unique_num__114_0_2,
            SUBPARTITION obj_unique_num__114_0_3,
            SUBPARTITION obj_unique_num__114_0_4,
            SUBPARTITION obj_unique_num__114_0_5,
            SUBPARTITION obj_unique_num__114_0_6,
            SUBPARTITION obj_unique_num__114_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__115`;
CREATE TABLE `obj_unique_num__115` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__115_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__115_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__115_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__115_0_0,
            SUBPARTITION obj_unique_num__115_0_1,
            SUBPARTITION obj_unique_num__115_0_2,
            SUBPARTITION obj_unique_num__115_0_3,
            SUBPARTITION obj_unique_num__115_0_4,
            SUBPARTITION obj_unique_num__115_0_5,
            SUBPARTITION obj_unique_num__115_0_6,
            SUBPARTITION obj_unique_num__115_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__116`;
CREATE TABLE `obj_unique_num__116` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__116_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__116_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__116_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__116_0_0,
            SUBPARTITION obj_unique_num__116_0_1,
            SUBPARTITION obj_unique_num__116_0_2,
            SUBPARTITION obj_unique_num__116_0_3,
            SUBPARTITION obj_unique_num__116_0_4,
            SUBPARTITION obj_unique_num__116_0_5,
            SUBPARTITION obj_unique_num__116_0_6,
            SUBPARTITION obj_unique_num__116_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__117`;
CREATE TABLE `obj_unique_num__117` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__117_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__117_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__117_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__117_0_0,
            SUBPARTITION obj_unique_num__117_0_1,
            SUBPARTITION obj_unique_num__117_0_2,
            SUBPARTITION obj_unique_num__117_0_3,
            SUBPARTITION obj_unique_num__117_0_4,
            SUBPARTITION obj_unique_num__117_0_5,
            SUBPARTITION obj_unique_num__117_0_6,
            SUBPARTITION obj_unique_num__117_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__118`;
CREATE TABLE `obj_unique_num__118` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__118_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__118_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__118_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__118_0_0,
            SUBPARTITION obj_unique_num__118_0_1,
            SUBPARTITION obj_unique_num__118_0_2,
            SUBPARTITION obj_unique_num__118_0_3,
            SUBPARTITION obj_unique_num__118_0_4,
            SUBPARTITION obj_unique_num__118_0_5,
            SUBPARTITION obj_unique_num__118_0_6,
            SUBPARTITION obj_unique_num__118_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__119`;
CREATE TABLE `obj_unique_num__119` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__119_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__119_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__119_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__119_0_0,
            SUBPARTITION obj_unique_num__119_0_1,
            SUBPARTITION obj_unique_num__119_0_2,
            SUBPARTITION obj_unique_num__119_0_3,
            SUBPARTITION obj_unique_num__119_0_4,
            SUBPARTITION obj_unique_num__119_0_5,
            SUBPARTITION obj_unique_num__119_0_6,
            SUBPARTITION obj_unique_num__119_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__120`;
CREATE TABLE `obj_unique_num__120` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__120_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__120_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__120_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__120_0_0,
            SUBPARTITION obj_unique_num__120_0_1,
            SUBPARTITION obj_unique_num__120_0_2,
            SUBPARTITION obj_unique_num__120_0_3,
            SUBPARTITION obj_unique_num__120_0_4,
            SUBPARTITION obj_unique_num__120_0_5,
            SUBPARTITION obj_unique_num__120_0_6,
            SUBPARTITION obj_unique_num__120_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__121`;
CREATE TABLE `obj_unique_num__121` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__121_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__121_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__121_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__121_0_0,
            SUBPARTITION obj_unique_num__121_0_1,
            SUBPARTITION obj_unique_num__121_0_2,
            SUBPARTITION obj_unique_num__121_0_3,
            SUBPARTITION obj_unique_num__121_0_4,
            SUBPARTITION obj_unique_num__121_0_5,
            SUBPARTITION obj_unique_num__121_0_6,
            SUBPARTITION obj_unique_num__121_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__122`;
CREATE TABLE `obj_unique_num__122` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__122_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__122_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__122_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__122_0_0,
            SUBPARTITION obj_unique_num__122_0_1,
            SUBPARTITION obj_unique_num__122_0_2,
            SUBPARTITION obj_unique_num__122_0_3,
            SUBPARTITION obj_unique_num__122_0_4,
            SUBPARTITION obj_unique_num__122_0_5,
            SUBPARTITION obj_unique_num__122_0_6,
            SUBPARTITION obj_unique_num__122_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__123`;
CREATE TABLE `obj_unique_num__123` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__123_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__123_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__123_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__123_0_0,
            SUBPARTITION obj_unique_num__123_0_1,
            SUBPARTITION obj_unique_num__123_0_2,
            SUBPARTITION obj_unique_num__123_0_3,
            SUBPARTITION obj_unique_num__123_0_4,
            SUBPARTITION obj_unique_num__123_0_5,
            SUBPARTITION obj_unique_num__123_0_6,
            SUBPARTITION obj_unique_num__123_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__124`;
CREATE TABLE `obj_unique_num__124` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__124_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__124_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__124_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__124_0_0,
            SUBPARTITION obj_unique_num__124_0_1,
            SUBPARTITION obj_unique_num__124_0_2,
            SUBPARTITION obj_unique_num__124_0_3,
            SUBPARTITION obj_unique_num__124_0_4,
            SUBPARTITION obj_unique_num__124_0_5,
            SUBPARTITION obj_unique_num__124_0_6,
            SUBPARTITION obj_unique_num__124_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__125`;
CREATE TABLE `obj_unique_num__125` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__125_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__125_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__125_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__125_0_0,
            SUBPARTITION obj_unique_num__125_0_1,
            SUBPARTITION obj_unique_num__125_0_2,
            SUBPARTITION obj_unique_num__125_0_3,
            SUBPARTITION obj_unique_num__125_0_4,
            SUBPARTITION obj_unique_num__125_0_5,
            SUBPARTITION obj_unique_num__125_0_6,
            SUBPARTITION obj_unique_num__125_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__126`;
CREATE TABLE `obj_unique_num__126` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__126_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__126_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__126_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__126_0_0,
            SUBPARTITION obj_unique_num__126_0_1,
            SUBPARTITION obj_unique_num__126_0_2,
            SUBPARTITION obj_unique_num__126_0_3,
            SUBPARTITION obj_unique_num__126_0_4,
            SUBPARTITION obj_unique_num__126_0_5,
            SUBPARTITION obj_unique_num__126_0_6,
            SUBPARTITION obj_unique_num__126_0_7
        )
    )

;
/* drop/create OBJ_UNIQUE_NUM */
DROP TABLE IF EXISTS `obj_unique_num__127`;
CREATE TABLE `obj_unique_num__127` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DECIMAL(22, 0) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_num__127_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_num__127_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)

ENGINE=InnoDB COMPRESSION="lz4"


PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_unique_num__127_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_unique_num__127_0_0,
            SUBPARTITION obj_unique_num__127_0_1,
            SUBPARTITION obj_unique_num__127_0_2,
            SUBPARTITION obj_unique_num__127_0_3,
            SUBPARTITION obj_unique_num__127_0_4,
            SUBPARTITION obj_unique_num__127_0_5,
            SUBPARTITION obj_unique_num__127_0_6,
            SUBPARTITION obj_unique_num__127_0_7
        )
    )

;
