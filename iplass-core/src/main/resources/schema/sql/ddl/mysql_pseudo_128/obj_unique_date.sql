/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date`;
CREATE TABLE `obj_unique_date` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__1`;
CREATE TABLE `obj_unique_date__1` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__1_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__1_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__2`;
CREATE TABLE `obj_unique_date__2` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__2_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__2_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__3`;
CREATE TABLE `obj_unique_date__3` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__3_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__3_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__4`;
CREATE TABLE `obj_unique_date__4` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__4_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__4_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__5`;
CREATE TABLE `obj_unique_date__5` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__5_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__5_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__6`;
CREATE TABLE `obj_unique_date__6` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__6_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__6_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__7`;
CREATE TABLE `obj_unique_date__7` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__7_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__7_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__8`;
CREATE TABLE `obj_unique_date__8` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__8_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__8_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__9`;
CREATE TABLE `obj_unique_date__9` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__9_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__9_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__10`;
CREATE TABLE `obj_unique_date__10` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__10_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__10_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__11`;
CREATE TABLE `obj_unique_date__11` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__11_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__11_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__12`;
CREATE TABLE `obj_unique_date__12` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__12_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__12_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__13`;
CREATE TABLE `obj_unique_date__13` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__13_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__13_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__14`;
CREATE TABLE `obj_unique_date__14` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__14_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__14_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__15`;
CREATE TABLE `obj_unique_date__15` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__15_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__15_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__16`;
CREATE TABLE `obj_unique_date__16` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__16_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__16_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__17`;
CREATE TABLE `obj_unique_date__17` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__17_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__17_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__18`;
CREATE TABLE `obj_unique_date__18` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__18_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__18_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__19`;
CREATE TABLE `obj_unique_date__19` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__19_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__19_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__20`;
CREATE TABLE `obj_unique_date__20` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__20_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__20_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__21`;
CREATE TABLE `obj_unique_date__21` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__21_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__21_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__22`;
CREATE TABLE `obj_unique_date__22` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__22_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__22_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__23`;
CREATE TABLE `obj_unique_date__23` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__23_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__23_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__24`;
CREATE TABLE `obj_unique_date__24` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__24_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__24_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__25`;
CREATE TABLE `obj_unique_date__25` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__25_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__25_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__26`;
CREATE TABLE `obj_unique_date__26` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__26_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__26_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__27`;
CREATE TABLE `obj_unique_date__27` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__27_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__27_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__28`;
CREATE TABLE `obj_unique_date__28` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__28_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__28_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__29`;
CREATE TABLE `obj_unique_date__29` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__29_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__29_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__30`;
CREATE TABLE `obj_unique_date__30` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__30_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__30_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__31`;
CREATE TABLE `obj_unique_date__31` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__31_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__31_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__32`;
CREATE TABLE `obj_unique_date__32` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__32_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__32_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__33`;
CREATE TABLE `obj_unique_date__33` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__33_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__33_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__34`;
CREATE TABLE `obj_unique_date__34` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__34_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__34_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__35`;
CREATE TABLE `obj_unique_date__35` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__35_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__35_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__36`;
CREATE TABLE `obj_unique_date__36` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__36_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__36_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__37`;
CREATE TABLE `obj_unique_date__37` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__37_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__37_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__38`;
CREATE TABLE `obj_unique_date__38` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__38_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__38_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__39`;
CREATE TABLE `obj_unique_date__39` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__39_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__39_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__40`;
CREATE TABLE `obj_unique_date__40` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__40_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__40_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__41`;
CREATE TABLE `obj_unique_date__41` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__41_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__41_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__42`;
CREATE TABLE `obj_unique_date__42` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__42_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__42_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__43`;
CREATE TABLE `obj_unique_date__43` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__43_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__43_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__44`;
CREATE TABLE `obj_unique_date__44` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__44_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__44_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__45`;
CREATE TABLE `obj_unique_date__45` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__45_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__45_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__46`;
CREATE TABLE `obj_unique_date__46` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__46_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__46_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__47`;
CREATE TABLE `obj_unique_date__47` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__47_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__47_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__48`;
CREATE TABLE `obj_unique_date__48` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__48_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__48_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__49`;
CREATE TABLE `obj_unique_date__49` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__49_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__49_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__50`;
CREATE TABLE `obj_unique_date__50` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__50_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__50_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__51`;
CREATE TABLE `obj_unique_date__51` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__51_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__51_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__52`;
CREATE TABLE `obj_unique_date__52` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__52_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__52_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__53`;
CREATE TABLE `obj_unique_date__53` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__53_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__53_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__54`;
CREATE TABLE `obj_unique_date__54` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__54_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__54_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__55`;
CREATE TABLE `obj_unique_date__55` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__55_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__55_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__56`;
CREATE TABLE `obj_unique_date__56` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__56_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__56_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__57`;
CREATE TABLE `obj_unique_date__57` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__57_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__57_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__58`;
CREATE TABLE `obj_unique_date__58` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__58_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__58_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__59`;
CREATE TABLE `obj_unique_date__59` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__59_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__59_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__60`;
CREATE TABLE `obj_unique_date__60` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__60_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__60_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__61`;
CREATE TABLE `obj_unique_date__61` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__61_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__61_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__62`;
CREATE TABLE `obj_unique_date__62` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__62_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__62_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__63`;
CREATE TABLE `obj_unique_date__63` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__63_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__63_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__64`;
CREATE TABLE `obj_unique_date__64` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__64_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__64_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__65`;
CREATE TABLE `obj_unique_date__65` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__65_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__65_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__66`;
CREATE TABLE `obj_unique_date__66` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__66_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__66_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__67`;
CREATE TABLE `obj_unique_date__67` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__67_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__67_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__68`;
CREATE TABLE `obj_unique_date__68` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__68_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__68_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__69`;
CREATE TABLE `obj_unique_date__69` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__69_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__69_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__70`;
CREATE TABLE `obj_unique_date__70` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__70_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__70_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__71`;
CREATE TABLE `obj_unique_date__71` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__71_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__71_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__72`;
CREATE TABLE `obj_unique_date__72` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__72_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__72_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__73`;
CREATE TABLE `obj_unique_date__73` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__73_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__73_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__74`;
CREATE TABLE `obj_unique_date__74` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__74_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__74_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__75`;
CREATE TABLE `obj_unique_date__75` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__75_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__75_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__76`;
CREATE TABLE `obj_unique_date__76` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__76_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__76_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__77`;
CREATE TABLE `obj_unique_date__77` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__77_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__77_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__78`;
CREATE TABLE `obj_unique_date__78` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__78_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__78_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__79`;
CREATE TABLE `obj_unique_date__79` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__79_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__79_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__80`;
CREATE TABLE `obj_unique_date__80` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__80_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__80_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__81`;
CREATE TABLE `obj_unique_date__81` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__81_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__81_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__82`;
CREATE TABLE `obj_unique_date__82` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__82_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__82_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__83`;
CREATE TABLE `obj_unique_date__83` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__83_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__83_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__84`;
CREATE TABLE `obj_unique_date__84` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__84_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__84_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__85`;
CREATE TABLE `obj_unique_date__85` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__85_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__85_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__86`;
CREATE TABLE `obj_unique_date__86` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__86_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__86_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__87`;
CREATE TABLE `obj_unique_date__87` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__87_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__87_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__88`;
CREATE TABLE `obj_unique_date__88` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__88_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__88_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__89`;
CREATE TABLE `obj_unique_date__89` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__89_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__89_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__90`;
CREATE TABLE `obj_unique_date__90` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__90_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__90_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__91`;
CREATE TABLE `obj_unique_date__91` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__91_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__91_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__92`;
CREATE TABLE `obj_unique_date__92` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__92_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__92_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__93`;
CREATE TABLE `obj_unique_date__93` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__93_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__93_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__94`;
CREATE TABLE `obj_unique_date__94` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__94_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__94_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__95`;
CREATE TABLE `obj_unique_date__95` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__95_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__95_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__96`;
CREATE TABLE `obj_unique_date__96` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__96_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__96_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__97`;
CREATE TABLE `obj_unique_date__97` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__97_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__97_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__98`;
CREATE TABLE `obj_unique_date__98` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__98_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__98_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__99`;
CREATE TABLE `obj_unique_date__99` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__99_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__99_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__100`;
CREATE TABLE `obj_unique_date__100` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__100_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__100_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__101`;
CREATE TABLE `obj_unique_date__101` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__101_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__101_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__102`;
CREATE TABLE `obj_unique_date__102` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__102_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__102_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__103`;
CREATE TABLE `obj_unique_date__103` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__103_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__103_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__104`;
CREATE TABLE `obj_unique_date__104` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__104_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__104_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__105`;
CREATE TABLE `obj_unique_date__105` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__105_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__105_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__106`;
CREATE TABLE `obj_unique_date__106` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__106_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__106_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__107`;
CREATE TABLE `obj_unique_date__107` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__107_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__107_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__108`;
CREATE TABLE `obj_unique_date__108` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__108_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__108_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__109`;
CREATE TABLE `obj_unique_date__109` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__109_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__109_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__110`;
CREATE TABLE `obj_unique_date__110` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__110_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__110_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__111`;
CREATE TABLE `obj_unique_date__111` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__111_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__111_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__112`;
CREATE TABLE `obj_unique_date__112` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__112_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__112_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__113`;
CREATE TABLE `obj_unique_date__113` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__113_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__113_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__114`;
CREATE TABLE `obj_unique_date__114` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__114_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__114_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__115`;
CREATE TABLE `obj_unique_date__115` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__115_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__115_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__116`;
CREATE TABLE `obj_unique_date__116` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__116_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__116_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__117`;
CREATE TABLE `obj_unique_date__117` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__117_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__117_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__118`;
CREATE TABLE `obj_unique_date__118` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__118_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__118_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__119`;
CREATE TABLE `obj_unique_date__119` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__119_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__119_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__120`;
CREATE TABLE `obj_unique_date__120` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__120_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__120_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__121`;
CREATE TABLE `obj_unique_date__121` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__121_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__121_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__122`;
CREATE TABLE `obj_unique_date__122` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__122_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__122_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__123`;
CREATE TABLE `obj_unique_date__123` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__123_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__123_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__124`;
CREATE TABLE `obj_unique_date__124` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__124_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__124_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__125`;
CREATE TABLE `obj_unique_date__125` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__125_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__125_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__126`;
CREATE TABLE `obj_unique_date__126` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__126_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__126_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_UNIQUE_DATE */
DROP TABLE IF EXISTS `obj_unique_date__127`;
CREATE TABLE `obj_unique_date__127` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `val` DATE NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  UNIQUE INDEX `obj_unique_date__127_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_unique_date__127_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
