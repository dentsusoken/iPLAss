/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str`;
CREATE TABLE `obj_index_str` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__1`;
CREATE TABLE `obj_index_str__1` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__1_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__1_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__2`;
CREATE TABLE `obj_index_str__2` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__2_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__2_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__3`;
CREATE TABLE `obj_index_str__3` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__3_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__3_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__4`;
CREATE TABLE `obj_index_str__4` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__4_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__4_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__5`;
CREATE TABLE `obj_index_str__5` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__5_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__5_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__6`;
CREATE TABLE `obj_index_str__6` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__6_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__6_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__7`;
CREATE TABLE `obj_index_str__7` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__7_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__7_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__8`;
CREATE TABLE `obj_index_str__8` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__8_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__8_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__9`;
CREATE TABLE `obj_index_str__9` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__9_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__9_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__10`;
CREATE TABLE `obj_index_str__10` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__10_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__10_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__11`;
CREATE TABLE `obj_index_str__11` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__11_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__11_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__12`;
CREATE TABLE `obj_index_str__12` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__12_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__12_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__13`;
CREATE TABLE `obj_index_str__13` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__13_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__13_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__14`;
CREATE TABLE `obj_index_str__14` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__14_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__14_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__15`;
CREATE TABLE `obj_index_str__15` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__15_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__15_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__16`;
CREATE TABLE `obj_index_str__16` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__16_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__16_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__17`;
CREATE TABLE `obj_index_str__17` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__17_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__17_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__18`;
CREATE TABLE `obj_index_str__18` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__18_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__18_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__19`;
CREATE TABLE `obj_index_str__19` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__19_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__19_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__20`;
CREATE TABLE `obj_index_str__20` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__20_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__20_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__21`;
CREATE TABLE `obj_index_str__21` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__21_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__21_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__22`;
CREATE TABLE `obj_index_str__22` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__22_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__22_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__23`;
CREATE TABLE `obj_index_str__23` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__23_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__23_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__24`;
CREATE TABLE `obj_index_str__24` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__24_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__24_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__25`;
CREATE TABLE `obj_index_str__25` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__25_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__25_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__26`;
CREATE TABLE `obj_index_str__26` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__26_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__26_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__27`;
CREATE TABLE `obj_index_str__27` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__27_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__27_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__28`;
CREATE TABLE `obj_index_str__28` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__28_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__28_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__29`;
CREATE TABLE `obj_index_str__29` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__29_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__29_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__30`;
CREATE TABLE `obj_index_str__30` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__30_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__30_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__31`;
CREATE TABLE `obj_index_str__31` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__31_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__31_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__32`;
CREATE TABLE `obj_index_str__32` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__32_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__32_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__33`;
CREATE TABLE `obj_index_str__33` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__33_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__33_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__34`;
CREATE TABLE `obj_index_str__34` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__34_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__34_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__35`;
CREATE TABLE `obj_index_str__35` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__35_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__35_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__36`;
CREATE TABLE `obj_index_str__36` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__36_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__36_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__37`;
CREATE TABLE `obj_index_str__37` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__37_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__37_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__38`;
CREATE TABLE `obj_index_str__38` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__38_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__38_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__39`;
CREATE TABLE `obj_index_str__39` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__39_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__39_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__40`;
CREATE TABLE `obj_index_str__40` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__40_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__40_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__41`;
CREATE TABLE `obj_index_str__41` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__41_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__41_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__42`;
CREATE TABLE `obj_index_str__42` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__42_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__42_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__43`;
CREATE TABLE `obj_index_str__43` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__43_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__43_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__44`;
CREATE TABLE `obj_index_str__44` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__44_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__44_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__45`;
CREATE TABLE `obj_index_str__45` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__45_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__45_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__46`;
CREATE TABLE `obj_index_str__46` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__46_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__46_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__47`;
CREATE TABLE `obj_index_str__47` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__47_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__47_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__48`;
CREATE TABLE `obj_index_str__48` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__48_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__48_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__49`;
CREATE TABLE `obj_index_str__49` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__49_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__49_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__50`;
CREATE TABLE `obj_index_str__50` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__50_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__50_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__51`;
CREATE TABLE `obj_index_str__51` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__51_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__51_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__52`;
CREATE TABLE `obj_index_str__52` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__52_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__52_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__53`;
CREATE TABLE `obj_index_str__53` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__53_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__53_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__54`;
CREATE TABLE `obj_index_str__54` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__54_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__54_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__55`;
CREATE TABLE `obj_index_str__55` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__55_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__55_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__56`;
CREATE TABLE `obj_index_str__56` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__56_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__56_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__57`;
CREATE TABLE `obj_index_str__57` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__57_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__57_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__58`;
CREATE TABLE `obj_index_str__58` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__58_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__58_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__59`;
CREATE TABLE `obj_index_str__59` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__59_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__59_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__60`;
CREATE TABLE `obj_index_str__60` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__60_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__60_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__61`;
CREATE TABLE `obj_index_str__61` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__61_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__61_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__62`;
CREATE TABLE `obj_index_str__62` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__62_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__62_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__63`;
CREATE TABLE `obj_index_str__63` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__63_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__63_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__64`;
CREATE TABLE `obj_index_str__64` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__64_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__64_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__65`;
CREATE TABLE `obj_index_str__65` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__65_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__65_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__66`;
CREATE TABLE `obj_index_str__66` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__66_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__66_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__67`;
CREATE TABLE `obj_index_str__67` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__67_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__67_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__68`;
CREATE TABLE `obj_index_str__68` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__68_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__68_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__69`;
CREATE TABLE `obj_index_str__69` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__69_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__69_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__70`;
CREATE TABLE `obj_index_str__70` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__70_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__70_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__71`;
CREATE TABLE `obj_index_str__71` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__71_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__71_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__72`;
CREATE TABLE `obj_index_str__72` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__72_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__72_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__73`;
CREATE TABLE `obj_index_str__73` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__73_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__73_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__74`;
CREATE TABLE `obj_index_str__74` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__74_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__74_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__75`;
CREATE TABLE `obj_index_str__75` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__75_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__75_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__76`;
CREATE TABLE `obj_index_str__76` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__76_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__76_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__77`;
CREATE TABLE `obj_index_str__77` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__77_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__77_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__78`;
CREATE TABLE `obj_index_str__78` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__78_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__78_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__79`;
CREATE TABLE `obj_index_str__79` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__79_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__79_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__80`;
CREATE TABLE `obj_index_str__80` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__80_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__80_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__81`;
CREATE TABLE `obj_index_str__81` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__81_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__81_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__82`;
CREATE TABLE `obj_index_str__82` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__82_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__82_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__83`;
CREATE TABLE `obj_index_str__83` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__83_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__83_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__84`;
CREATE TABLE `obj_index_str__84` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__84_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__84_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__85`;
CREATE TABLE `obj_index_str__85` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__85_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__85_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__86`;
CREATE TABLE `obj_index_str__86` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__86_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__86_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__87`;
CREATE TABLE `obj_index_str__87` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__87_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__87_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__88`;
CREATE TABLE `obj_index_str__88` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__88_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__88_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__89`;
CREATE TABLE `obj_index_str__89` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__89_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__89_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__90`;
CREATE TABLE `obj_index_str__90` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__90_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__90_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__91`;
CREATE TABLE `obj_index_str__91` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__91_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__91_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__92`;
CREATE TABLE `obj_index_str__92` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__92_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__92_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__93`;
CREATE TABLE `obj_index_str__93` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__93_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__93_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__94`;
CREATE TABLE `obj_index_str__94` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__94_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__94_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__95`;
CREATE TABLE `obj_index_str__95` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__95_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__95_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__96`;
CREATE TABLE `obj_index_str__96` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__96_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__96_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__97`;
CREATE TABLE `obj_index_str__97` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__97_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__97_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__98`;
CREATE TABLE `obj_index_str__98` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__98_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__98_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__99`;
CREATE TABLE `obj_index_str__99` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__99_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__99_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__100`;
CREATE TABLE `obj_index_str__100` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__100_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__100_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__101`;
CREATE TABLE `obj_index_str__101` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__101_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__101_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__102`;
CREATE TABLE `obj_index_str__102` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__102_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__102_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__103`;
CREATE TABLE `obj_index_str__103` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__103_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__103_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__104`;
CREATE TABLE `obj_index_str__104` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__104_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__104_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__105`;
CREATE TABLE `obj_index_str__105` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__105_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__105_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__106`;
CREATE TABLE `obj_index_str__106` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__106_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__106_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__107`;
CREATE TABLE `obj_index_str__107` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__107_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__107_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__108`;
CREATE TABLE `obj_index_str__108` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__108_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__108_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__109`;
CREATE TABLE `obj_index_str__109` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__109_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__109_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__110`;
CREATE TABLE `obj_index_str__110` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__110_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__110_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__111`;
CREATE TABLE `obj_index_str__111` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__111_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__111_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__112`;
CREATE TABLE `obj_index_str__112` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__112_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__112_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__113`;
CREATE TABLE `obj_index_str__113` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__113_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__113_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__114`;
CREATE TABLE `obj_index_str__114` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__114_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__114_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__115`;
CREATE TABLE `obj_index_str__115` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__115_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__115_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__116`;
CREATE TABLE `obj_index_str__116` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__116_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__116_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__117`;
CREATE TABLE `obj_index_str__117` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__117_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__117_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__118`;
CREATE TABLE `obj_index_str__118` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__118_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__118_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__119`;
CREATE TABLE `obj_index_str__119` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__119_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__119_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__120`;
CREATE TABLE `obj_index_str__120` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__120_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__120_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__121`;
CREATE TABLE `obj_index_str__121` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__121_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__121_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__122`;
CREATE TABLE `obj_index_str__122` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__122_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__122_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__123`;
CREATE TABLE `obj_index_str__123` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__123_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__123_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__124`;
CREATE TABLE `obj_index_str__124` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__124_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__124_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__125`;
CREATE TABLE `obj_index_str__125` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__125_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__125_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__126`;
CREATE TABLE `obj_index_str__126` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__126_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__126_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `obj_index_str__127`;
CREATE TABLE `obj_index_str__127` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(603) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__127_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(603)),
  INDEX `obj_index_str__127_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
