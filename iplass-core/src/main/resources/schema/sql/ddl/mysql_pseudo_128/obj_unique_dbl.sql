/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL`;
CREATE TABLE `OBJ_UNIQUE_DBL` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__1`;
CREATE TABLE `OBJ_UNIQUE_DBL__1` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__2`;
CREATE TABLE `OBJ_UNIQUE_DBL__2` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__3`;
CREATE TABLE `OBJ_UNIQUE_DBL__3` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__4`;
CREATE TABLE `OBJ_UNIQUE_DBL__4` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__5`;
CREATE TABLE `OBJ_UNIQUE_DBL__5` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__6`;
CREATE TABLE `OBJ_UNIQUE_DBL__6` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__7`;
CREATE TABLE `OBJ_UNIQUE_DBL__7` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__8`;
CREATE TABLE `OBJ_UNIQUE_DBL__8` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__9`;
CREATE TABLE `OBJ_UNIQUE_DBL__9` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__10`;
CREATE TABLE `OBJ_UNIQUE_DBL__10` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__11`;
CREATE TABLE `OBJ_UNIQUE_DBL__11` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__12`;
CREATE TABLE `OBJ_UNIQUE_DBL__12` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__13`;
CREATE TABLE `OBJ_UNIQUE_DBL__13` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__14`;
CREATE TABLE `OBJ_UNIQUE_DBL__14` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__15`;
CREATE TABLE `OBJ_UNIQUE_DBL__15` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__16`;
CREATE TABLE `OBJ_UNIQUE_DBL__16` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__17`;
CREATE TABLE `OBJ_UNIQUE_DBL__17` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__18`;
CREATE TABLE `OBJ_UNIQUE_DBL__18` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__19`;
CREATE TABLE `OBJ_UNIQUE_DBL__19` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__20`;
CREATE TABLE `OBJ_UNIQUE_DBL__20` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__21`;
CREATE TABLE `OBJ_UNIQUE_DBL__21` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__22`;
CREATE TABLE `OBJ_UNIQUE_DBL__22` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__23`;
CREATE TABLE `OBJ_UNIQUE_DBL__23` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__24`;
CREATE TABLE `OBJ_UNIQUE_DBL__24` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__25`;
CREATE TABLE `OBJ_UNIQUE_DBL__25` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__26`;
CREATE TABLE `OBJ_UNIQUE_DBL__26` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__27`;
CREATE TABLE `OBJ_UNIQUE_DBL__27` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__28`;
CREATE TABLE `OBJ_UNIQUE_DBL__28` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__29`;
CREATE TABLE `OBJ_UNIQUE_DBL__29` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__30`;
CREATE TABLE `OBJ_UNIQUE_DBL__30` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__31`;
CREATE TABLE `OBJ_UNIQUE_DBL__31` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__32`;
CREATE TABLE `OBJ_UNIQUE_DBL__32` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__33`;
CREATE TABLE `OBJ_UNIQUE_DBL__33` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__34`;
CREATE TABLE `OBJ_UNIQUE_DBL__34` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__35`;
CREATE TABLE `OBJ_UNIQUE_DBL__35` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__36`;
CREATE TABLE `OBJ_UNIQUE_DBL__36` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__37`;
CREATE TABLE `OBJ_UNIQUE_DBL__37` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__38`;
CREATE TABLE `OBJ_UNIQUE_DBL__38` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__39`;
CREATE TABLE `OBJ_UNIQUE_DBL__39` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__40`;
CREATE TABLE `OBJ_UNIQUE_DBL__40` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__41`;
CREATE TABLE `OBJ_UNIQUE_DBL__41` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__42`;
CREATE TABLE `OBJ_UNIQUE_DBL__42` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__43`;
CREATE TABLE `OBJ_UNIQUE_DBL__43` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__44`;
CREATE TABLE `OBJ_UNIQUE_DBL__44` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__45`;
CREATE TABLE `OBJ_UNIQUE_DBL__45` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__46`;
CREATE TABLE `OBJ_UNIQUE_DBL__46` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__47`;
CREATE TABLE `OBJ_UNIQUE_DBL__47` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__48`;
CREATE TABLE `OBJ_UNIQUE_DBL__48` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__49`;
CREATE TABLE `OBJ_UNIQUE_DBL__49` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__50`;
CREATE TABLE `OBJ_UNIQUE_DBL__50` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__51`;
CREATE TABLE `OBJ_UNIQUE_DBL__51` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__52`;
CREATE TABLE `OBJ_UNIQUE_DBL__52` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__53`;
CREATE TABLE `OBJ_UNIQUE_DBL__53` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__54`;
CREATE TABLE `OBJ_UNIQUE_DBL__54` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__55`;
CREATE TABLE `OBJ_UNIQUE_DBL__55` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__56`;
CREATE TABLE `OBJ_UNIQUE_DBL__56` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__57`;
CREATE TABLE `OBJ_UNIQUE_DBL__57` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__58`;
CREATE TABLE `OBJ_UNIQUE_DBL__58` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__59`;
CREATE TABLE `OBJ_UNIQUE_DBL__59` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__60`;
CREATE TABLE `OBJ_UNIQUE_DBL__60` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__61`;
CREATE TABLE `OBJ_UNIQUE_DBL__61` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__62`;
CREATE TABLE `OBJ_UNIQUE_DBL__62` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__63`;
CREATE TABLE `OBJ_UNIQUE_DBL__63` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__64`;
CREATE TABLE `OBJ_UNIQUE_DBL__64` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__65`;
CREATE TABLE `OBJ_UNIQUE_DBL__65` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__66`;
CREATE TABLE `OBJ_UNIQUE_DBL__66` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__67`;
CREATE TABLE `OBJ_UNIQUE_DBL__67` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__68`;
CREATE TABLE `OBJ_UNIQUE_DBL__68` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__69`;
CREATE TABLE `OBJ_UNIQUE_DBL__69` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__70`;
CREATE TABLE `OBJ_UNIQUE_DBL__70` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__71`;
CREATE TABLE `OBJ_UNIQUE_DBL__71` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__72`;
CREATE TABLE `OBJ_UNIQUE_DBL__72` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__73`;
CREATE TABLE `OBJ_UNIQUE_DBL__73` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__74`;
CREATE TABLE `OBJ_UNIQUE_DBL__74` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__75`;
CREATE TABLE `OBJ_UNIQUE_DBL__75` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__76`;
CREATE TABLE `OBJ_UNIQUE_DBL__76` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__77`;
CREATE TABLE `OBJ_UNIQUE_DBL__77` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__78`;
CREATE TABLE `OBJ_UNIQUE_DBL__78` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__79`;
CREATE TABLE `OBJ_UNIQUE_DBL__79` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__80`;
CREATE TABLE `OBJ_UNIQUE_DBL__80` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__81`;
CREATE TABLE `OBJ_UNIQUE_DBL__81` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__82`;
CREATE TABLE `OBJ_UNIQUE_DBL__82` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__83`;
CREATE TABLE `OBJ_UNIQUE_DBL__83` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__84`;
CREATE TABLE `OBJ_UNIQUE_DBL__84` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__85`;
CREATE TABLE `OBJ_UNIQUE_DBL__85` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__86`;
CREATE TABLE `OBJ_UNIQUE_DBL__86` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__87`;
CREATE TABLE `OBJ_UNIQUE_DBL__87` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__88`;
CREATE TABLE `OBJ_UNIQUE_DBL__88` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__89`;
CREATE TABLE `OBJ_UNIQUE_DBL__89` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__90`;
CREATE TABLE `OBJ_UNIQUE_DBL__90` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__91`;
CREATE TABLE `OBJ_UNIQUE_DBL__91` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__92`;
CREATE TABLE `OBJ_UNIQUE_DBL__92` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__93`;
CREATE TABLE `OBJ_UNIQUE_DBL__93` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__94`;
CREATE TABLE `OBJ_UNIQUE_DBL__94` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__95`;
CREATE TABLE `OBJ_UNIQUE_DBL__95` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__96`;
CREATE TABLE `OBJ_UNIQUE_DBL__96` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__97`;
CREATE TABLE `OBJ_UNIQUE_DBL__97` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__98`;
CREATE TABLE `OBJ_UNIQUE_DBL__98` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__99`;
CREATE TABLE `OBJ_UNIQUE_DBL__99` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__100`;
CREATE TABLE `OBJ_UNIQUE_DBL__100` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__101`;
CREATE TABLE `OBJ_UNIQUE_DBL__101` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__102`;
CREATE TABLE `OBJ_UNIQUE_DBL__102` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__103`;
CREATE TABLE `OBJ_UNIQUE_DBL__103` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__104`;
CREATE TABLE `OBJ_UNIQUE_DBL__104` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__105`;
CREATE TABLE `OBJ_UNIQUE_DBL__105` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__106`;
CREATE TABLE `OBJ_UNIQUE_DBL__106` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__107`;
CREATE TABLE `OBJ_UNIQUE_DBL__107` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__108`;
CREATE TABLE `OBJ_UNIQUE_DBL__108` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__109`;
CREATE TABLE `OBJ_UNIQUE_DBL__109` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__110`;
CREATE TABLE `OBJ_UNIQUE_DBL__110` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__111`;
CREATE TABLE `OBJ_UNIQUE_DBL__111` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__112`;
CREATE TABLE `OBJ_UNIQUE_DBL__112` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__113`;
CREATE TABLE `OBJ_UNIQUE_DBL__113` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__114`;
CREATE TABLE `OBJ_UNIQUE_DBL__114` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__115`;
CREATE TABLE `OBJ_UNIQUE_DBL__115` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__116`;
CREATE TABLE `OBJ_UNIQUE_DBL__116` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__117`;
CREATE TABLE `OBJ_UNIQUE_DBL__117` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__118`;
CREATE TABLE `OBJ_UNIQUE_DBL__118` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__119`;
CREATE TABLE `OBJ_UNIQUE_DBL__119` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__120`;
CREATE TABLE `OBJ_UNIQUE_DBL__120` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__121`;
CREATE TABLE `OBJ_UNIQUE_DBL__121` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__122`;
CREATE TABLE `OBJ_UNIQUE_DBL__122` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__123`;
CREATE TABLE `OBJ_UNIQUE_DBL__123` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__124`;
CREATE TABLE `OBJ_UNIQUE_DBL__124` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__125`;
CREATE TABLE `OBJ_UNIQUE_DBL__125` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__126`;
CREATE TABLE `OBJ_UNIQUE_DBL__126` (
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



;
/* drop/create OBJ_UNIQUE_DBL */
DROP TABLE IF EXISTS `OBJ_UNIQUE_DBL__127`;
CREATE TABLE `OBJ_UNIQUE_DBL__127` (
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



;
