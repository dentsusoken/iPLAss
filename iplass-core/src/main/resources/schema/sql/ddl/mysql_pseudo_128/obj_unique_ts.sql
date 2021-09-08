/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts`;
CREATE TABLE `obj_unique_ts` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__1`;
CREATE TABLE `obj_unique_ts__1` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__1_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__1_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__2`;
CREATE TABLE `obj_unique_ts__2` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__2_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__2_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__3`;
CREATE TABLE `obj_unique_ts__3` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__3_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__3_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__4`;
CREATE TABLE `obj_unique_ts__4` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__4_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__4_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__5`;
CREATE TABLE `obj_unique_ts__5` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__5_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__5_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__6`;
CREATE TABLE `obj_unique_ts__6` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__6_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__6_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__7`;
CREATE TABLE `obj_unique_ts__7` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__7_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__7_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__8`;
CREATE TABLE `obj_unique_ts__8` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__8_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__8_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__9`;
CREATE TABLE `obj_unique_ts__9` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__9_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__9_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__10`;
CREATE TABLE `obj_unique_ts__10` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__10_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__10_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__11`;
CREATE TABLE `obj_unique_ts__11` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__11_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__11_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__12`;
CREATE TABLE `obj_unique_ts__12` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__12_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__12_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__13`;
CREATE TABLE `obj_unique_ts__13` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__13_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__13_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__14`;
CREATE TABLE `obj_unique_ts__14` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__14_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__14_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__15`;
CREATE TABLE `obj_unique_ts__15` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__15_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__15_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__16`;
CREATE TABLE `obj_unique_ts__16` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__16_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__16_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__17`;
CREATE TABLE `obj_unique_ts__17` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__17_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__17_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__18`;
CREATE TABLE `obj_unique_ts__18` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__18_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__18_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__19`;
CREATE TABLE `obj_unique_ts__19` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__19_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__19_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__20`;
CREATE TABLE `obj_unique_ts__20` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__20_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__20_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__21`;
CREATE TABLE `obj_unique_ts__21` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__21_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__21_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__22`;
CREATE TABLE `obj_unique_ts__22` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__22_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__22_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__23`;
CREATE TABLE `obj_unique_ts__23` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__23_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__23_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__24`;
CREATE TABLE `obj_unique_ts__24` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__24_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__24_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__25`;
CREATE TABLE `obj_unique_ts__25` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__25_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__25_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__26`;
CREATE TABLE `obj_unique_ts__26` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__26_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__26_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__27`;
CREATE TABLE `obj_unique_ts__27` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__27_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__27_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__28`;
CREATE TABLE `obj_unique_ts__28` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__28_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__28_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__29`;
CREATE TABLE `obj_unique_ts__29` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__29_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__29_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__30`;
CREATE TABLE `obj_unique_ts__30` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__30_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__30_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__31`;
CREATE TABLE `obj_unique_ts__31` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__31_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__31_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__32`;
CREATE TABLE `obj_unique_ts__32` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__32_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__32_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__33`;
CREATE TABLE `obj_unique_ts__33` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__33_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__33_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__34`;
CREATE TABLE `obj_unique_ts__34` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__34_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__34_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__35`;
CREATE TABLE `obj_unique_ts__35` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__35_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__35_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__36`;
CREATE TABLE `obj_unique_ts__36` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__36_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__36_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__37`;
CREATE TABLE `obj_unique_ts__37` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__37_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__37_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__38`;
CREATE TABLE `obj_unique_ts__38` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__38_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__38_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__39`;
CREATE TABLE `obj_unique_ts__39` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__39_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__39_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__40`;
CREATE TABLE `obj_unique_ts__40` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__40_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__40_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__41`;
CREATE TABLE `obj_unique_ts__41` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__41_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__41_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__42`;
CREATE TABLE `obj_unique_ts__42` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__42_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__42_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__43`;
CREATE TABLE `obj_unique_ts__43` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__43_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__43_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__44`;
CREATE TABLE `obj_unique_ts__44` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__44_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__44_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__45`;
CREATE TABLE `obj_unique_ts__45` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__45_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__45_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__46`;
CREATE TABLE `obj_unique_ts__46` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__46_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__46_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__47`;
CREATE TABLE `obj_unique_ts__47` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__47_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__47_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__48`;
CREATE TABLE `obj_unique_ts__48` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__48_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__48_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__49`;
CREATE TABLE `obj_unique_ts__49` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__49_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__49_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__50`;
CREATE TABLE `obj_unique_ts__50` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__50_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__50_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__51`;
CREATE TABLE `obj_unique_ts__51` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__51_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__51_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__52`;
CREATE TABLE `obj_unique_ts__52` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__52_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__52_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__53`;
CREATE TABLE `obj_unique_ts__53` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__53_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__53_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__54`;
CREATE TABLE `obj_unique_ts__54` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__54_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__54_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__55`;
CREATE TABLE `obj_unique_ts__55` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__55_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__55_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__56`;
CREATE TABLE `obj_unique_ts__56` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__56_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__56_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__57`;
CREATE TABLE `obj_unique_ts__57` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__57_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__57_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__58`;
CREATE TABLE `obj_unique_ts__58` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__58_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__58_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__59`;
CREATE TABLE `obj_unique_ts__59` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__59_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__59_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__60`;
CREATE TABLE `obj_unique_ts__60` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__60_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__60_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__61`;
CREATE TABLE `obj_unique_ts__61` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__61_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__61_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__62`;
CREATE TABLE `obj_unique_ts__62` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__62_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__62_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__63`;
CREATE TABLE `obj_unique_ts__63` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__63_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__63_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__64`;
CREATE TABLE `obj_unique_ts__64` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__64_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__64_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__65`;
CREATE TABLE `obj_unique_ts__65` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__65_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__65_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__66`;
CREATE TABLE `obj_unique_ts__66` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__66_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__66_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__67`;
CREATE TABLE `obj_unique_ts__67` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__67_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__67_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__68`;
CREATE TABLE `obj_unique_ts__68` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__68_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__68_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__69`;
CREATE TABLE `obj_unique_ts__69` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__69_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__69_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__70`;
CREATE TABLE `obj_unique_ts__70` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__70_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__70_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__71`;
CREATE TABLE `obj_unique_ts__71` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__71_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__71_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__72`;
CREATE TABLE `obj_unique_ts__72` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__72_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__72_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__73`;
CREATE TABLE `obj_unique_ts__73` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__73_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__73_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__74`;
CREATE TABLE `obj_unique_ts__74` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__74_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__74_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__75`;
CREATE TABLE `obj_unique_ts__75` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__75_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__75_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__76`;
CREATE TABLE `obj_unique_ts__76` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__76_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__76_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__77`;
CREATE TABLE `obj_unique_ts__77` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__77_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__77_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__78`;
CREATE TABLE `obj_unique_ts__78` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__78_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__78_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__79`;
CREATE TABLE `obj_unique_ts__79` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__79_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__79_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__80`;
CREATE TABLE `obj_unique_ts__80` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__80_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__80_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__81`;
CREATE TABLE `obj_unique_ts__81` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__81_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__81_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__82`;
CREATE TABLE `obj_unique_ts__82` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__82_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__82_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__83`;
CREATE TABLE `obj_unique_ts__83` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__83_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__83_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__84`;
CREATE TABLE `obj_unique_ts__84` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__84_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__84_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__85`;
CREATE TABLE `obj_unique_ts__85` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__85_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__85_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__86`;
CREATE TABLE `obj_unique_ts__86` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__86_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__86_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__87`;
CREATE TABLE `obj_unique_ts__87` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__87_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__87_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__88`;
CREATE TABLE `obj_unique_ts__88` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__88_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__88_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__89`;
CREATE TABLE `obj_unique_ts__89` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__89_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__89_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__90`;
CREATE TABLE `obj_unique_ts__90` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__90_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__90_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__91`;
CREATE TABLE `obj_unique_ts__91` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__91_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__91_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__92`;
CREATE TABLE `obj_unique_ts__92` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__92_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__92_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__93`;
CREATE TABLE `obj_unique_ts__93` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__93_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__93_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__94`;
CREATE TABLE `obj_unique_ts__94` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__94_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__94_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__95`;
CREATE TABLE `obj_unique_ts__95` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__95_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__95_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__96`;
CREATE TABLE `obj_unique_ts__96` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__96_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__96_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__97`;
CREATE TABLE `obj_unique_ts__97` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__97_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__97_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__98`;
CREATE TABLE `obj_unique_ts__98` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__98_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__98_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__99`;
CREATE TABLE `obj_unique_ts__99` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__99_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__99_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__100`;
CREATE TABLE `obj_unique_ts__100` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__100_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__100_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__101`;
CREATE TABLE `obj_unique_ts__101` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__101_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__101_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__102`;
CREATE TABLE `obj_unique_ts__102` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__102_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__102_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__103`;
CREATE TABLE `obj_unique_ts__103` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__103_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__103_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__104`;
CREATE TABLE `obj_unique_ts__104` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__104_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__104_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__105`;
CREATE TABLE `obj_unique_ts__105` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__105_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__105_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__106`;
CREATE TABLE `obj_unique_ts__106` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__106_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__106_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__107`;
CREATE TABLE `obj_unique_ts__107` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__107_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__107_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__108`;
CREATE TABLE `obj_unique_ts__108` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__108_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__108_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__109`;
CREATE TABLE `obj_unique_ts__109` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__109_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__109_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__110`;
CREATE TABLE `obj_unique_ts__110` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__110_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__110_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__111`;
CREATE TABLE `obj_unique_ts__111` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__111_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__111_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__112`;
CREATE TABLE `obj_unique_ts__112` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__112_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__112_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__113`;
CREATE TABLE `obj_unique_ts__113` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__113_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__113_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__114`;
CREATE TABLE `obj_unique_ts__114` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__114_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__114_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__115`;
CREATE TABLE `obj_unique_ts__115` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__115_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__115_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__116`;
CREATE TABLE `obj_unique_ts__116` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__116_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__116_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__117`;
CREATE TABLE `obj_unique_ts__117` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__117_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__117_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__118`;
CREATE TABLE `obj_unique_ts__118` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__118_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__118_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__119`;
CREATE TABLE `obj_unique_ts__119` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__119_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__119_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__120`;
CREATE TABLE `obj_unique_ts__120` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__120_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__120_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__121`;
CREATE TABLE `obj_unique_ts__121` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__121_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__121_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__122`;
CREATE TABLE `obj_unique_ts__122` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__122_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__122_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__123`;
CREATE TABLE `obj_unique_ts__123` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__123_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__123_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__124`;
CREATE TABLE `obj_unique_ts__124` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__124_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__124_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__125`;
CREATE TABLE `obj_unique_ts__125` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__125_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__125_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__126`;
CREATE TABLE `obj_unique_ts__126` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__126_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__126_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_TS */
DROP TABLE IF EXISTS `obj_unique_ts__127`;
CREATE TABLE `obj_unique_ts__127` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_ts__127_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_ts__127_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
