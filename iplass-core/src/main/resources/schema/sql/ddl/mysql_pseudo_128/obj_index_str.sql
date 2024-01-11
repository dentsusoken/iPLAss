/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR`;
CREATE TABLE `OBJ_INDEX_STR` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__1`;
CREATE TABLE `OBJ_INDEX_STR__1` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__2`;
CREATE TABLE `OBJ_INDEX_STR__2` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__3`;
CREATE TABLE `OBJ_INDEX_STR__3` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__4`;
CREATE TABLE `OBJ_INDEX_STR__4` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__5`;
CREATE TABLE `OBJ_INDEX_STR__5` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__6`;
CREATE TABLE `OBJ_INDEX_STR__6` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__7`;
CREATE TABLE `OBJ_INDEX_STR__7` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__8`;
CREATE TABLE `OBJ_INDEX_STR__8` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__9`;
CREATE TABLE `OBJ_INDEX_STR__9` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__10`;
CREATE TABLE `OBJ_INDEX_STR__10` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__11`;
CREATE TABLE `OBJ_INDEX_STR__11` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__12`;
CREATE TABLE `OBJ_INDEX_STR__12` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__13`;
CREATE TABLE `OBJ_INDEX_STR__13` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__14`;
CREATE TABLE `OBJ_INDEX_STR__14` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__15`;
CREATE TABLE `OBJ_INDEX_STR__15` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__16`;
CREATE TABLE `OBJ_INDEX_STR__16` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__17`;
CREATE TABLE `OBJ_INDEX_STR__17` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__18`;
CREATE TABLE `OBJ_INDEX_STR__18` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__19`;
CREATE TABLE `OBJ_INDEX_STR__19` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__20`;
CREATE TABLE `OBJ_INDEX_STR__20` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__21`;
CREATE TABLE `OBJ_INDEX_STR__21` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__22`;
CREATE TABLE `OBJ_INDEX_STR__22` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__23`;
CREATE TABLE `OBJ_INDEX_STR__23` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__24`;
CREATE TABLE `OBJ_INDEX_STR__24` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__25`;
CREATE TABLE `OBJ_INDEX_STR__25` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__26`;
CREATE TABLE `OBJ_INDEX_STR__26` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__27`;
CREATE TABLE `OBJ_INDEX_STR__27` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__28`;
CREATE TABLE `OBJ_INDEX_STR__28` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__29`;
CREATE TABLE `OBJ_INDEX_STR__29` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__30`;
CREATE TABLE `OBJ_INDEX_STR__30` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__31`;
CREATE TABLE `OBJ_INDEX_STR__31` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__32`;
CREATE TABLE `OBJ_INDEX_STR__32` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__33`;
CREATE TABLE `OBJ_INDEX_STR__33` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__34`;
CREATE TABLE `OBJ_INDEX_STR__34` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__35`;
CREATE TABLE `OBJ_INDEX_STR__35` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__36`;
CREATE TABLE `OBJ_INDEX_STR__36` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__37`;
CREATE TABLE `OBJ_INDEX_STR__37` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__38`;
CREATE TABLE `OBJ_INDEX_STR__38` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__39`;
CREATE TABLE `OBJ_INDEX_STR__39` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__40`;
CREATE TABLE `OBJ_INDEX_STR__40` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__41`;
CREATE TABLE `OBJ_INDEX_STR__41` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__42`;
CREATE TABLE `OBJ_INDEX_STR__42` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__43`;
CREATE TABLE `OBJ_INDEX_STR__43` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__44`;
CREATE TABLE `OBJ_INDEX_STR__44` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__45`;
CREATE TABLE `OBJ_INDEX_STR__45` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__46`;
CREATE TABLE `OBJ_INDEX_STR__46` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__47`;
CREATE TABLE `OBJ_INDEX_STR__47` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__48`;
CREATE TABLE `OBJ_INDEX_STR__48` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__49`;
CREATE TABLE `OBJ_INDEX_STR__49` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__50`;
CREATE TABLE `OBJ_INDEX_STR__50` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__51`;
CREATE TABLE `OBJ_INDEX_STR__51` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__52`;
CREATE TABLE `OBJ_INDEX_STR__52` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__53`;
CREATE TABLE `OBJ_INDEX_STR__53` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__54`;
CREATE TABLE `OBJ_INDEX_STR__54` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__55`;
CREATE TABLE `OBJ_INDEX_STR__55` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__56`;
CREATE TABLE `OBJ_INDEX_STR__56` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__57`;
CREATE TABLE `OBJ_INDEX_STR__57` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__58`;
CREATE TABLE `OBJ_INDEX_STR__58` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__59`;
CREATE TABLE `OBJ_INDEX_STR__59` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__60`;
CREATE TABLE `OBJ_INDEX_STR__60` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__61`;
CREATE TABLE `OBJ_INDEX_STR__61` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__62`;
CREATE TABLE `OBJ_INDEX_STR__62` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__63`;
CREATE TABLE `OBJ_INDEX_STR__63` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__64`;
CREATE TABLE `OBJ_INDEX_STR__64` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__65`;
CREATE TABLE `OBJ_INDEX_STR__65` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__66`;
CREATE TABLE `OBJ_INDEX_STR__66` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__67`;
CREATE TABLE `OBJ_INDEX_STR__67` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__68`;
CREATE TABLE `OBJ_INDEX_STR__68` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__69`;
CREATE TABLE `OBJ_INDEX_STR__69` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__70`;
CREATE TABLE `OBJ_INDEX_STR__70` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__71`;
CREATE TABLE `OBJ_INDEX_STR__71` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__72`;
CREATE TABLE `OBJ_INDEX_STR__72` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__73`;
CREATE TABLE `OBJ_INDEX_STR__73` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__74`;
CREATE TABLE `OBJ_INDEX_STR__74` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__75`;
CREATE TABLE `OBJ_INDEX_STR__75` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__76`;
CREATE TABLE `OBJ_INDEX_STR__76` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__77`;
CREATE TABLE `OBJ_INDEX_STR__77` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__78`;
CREATE TABLE `OBJ_INDEX_STR__78` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__79`;
CREATE TABLE `OBJ_INDEX_STR__79` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__80`;
CREATE TABLE `OBJ_INDEX_STR__80` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__81`;
CREATE TABLE `OBJ_INDEX_STR__81` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__82`;
CREATE TABLE `OBJ_INDEX_STR__82` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__83`;
CREATE TABLE `OBJ_INDEX_STR__83` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__84`;
CREATE TABLE `OBJ_INDEX_STR__84` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__85`;
CREATE TABLE `OBJ_INDEX_STR__85` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__86`;
CREATE TABLE `OBJ_INDEX_STR__86` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__87`;
CREATE TABLE `OBJ_INDEX_STR__87` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__88`;
CREATE TABLE `OBJ_INDEX_STR__88` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__89`;
CREATE TABLE `OBJ_INDEX_STR__89` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__90`;
CREATE TABLE `OBJ_INDEX_STR__90` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__91`;
CREATE TABLE `OBJ_INDEX_STR__91` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__92`;
CREATE TABLE `OBJ_INDEX_STR__92` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__93`;
CREATE TABLE `OBJ_INDEX_STR__93` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__94`;
CREATE TABLE `OBJ_INDEX_STR__94` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__95`;
CREATE TABLE `OBJ_INDEX_STR__95` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__96`;
CREATE TABLE `OBJ_INDEX_STR__96` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__97`;
CREATE TABLE `OBJ_INDEX_STR__97` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__98`;
CREATE TABLE `OBJ_INDEX_STR__98` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__99`;
CREATE TABLE `OBJ_INDEX_STR__99` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__100`;
CREATE TABLE `OBJ_INDEX_STR__100` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__101`;
CREATE TABLE `OBJ_INDEX_STR__101` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__102`;
CREATE TABLE `OBJ_INDEX_STR__102` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__103`;
CREATE TABLE `OBJ_INDEX_STR__103` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__104`;
CREATE TABLE `OBJ_INDEX_STR__104` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__105`;
CREATE TABLE `OBJ_INDEX_STR__105` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__106`;
CREATE TABLE `OBJ_INDEX_STR__106` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__107`;
CREATE TABLE `OBJ_INDEX_STR__107` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__108`;
CREATE TABLE `OBJ_INDEX_STR__108` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__109`;
CREATE TABLE `OBJ_INDEX_STR__109` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__110`;
CREATE TABLE `OBJ_INDEX_STR__110` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__111`;
CREATE TABLE `OBJ_INDEX_STR__111` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__112`;
CREATE TABLE `OBJ_INDEX_STR__112` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__113`;
CREATE TABLE `OBJ_INDEX_STR__113` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__114`;
CREATE TABLE `OBJ_INDEX_STR__114` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__115`;
CREATE TABLE `OBJ_INDEX_STR__115` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__116`;
CREATE TABLE `OBJ_INDEX_STR__116` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__117`;
CREATE TABLE `OBJ_INDEX_STR__117` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__118`;
CREATE TABLE `OBJ_INDEX_STR__118` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__119`;
CREATE TABLE `OBJ_INDEX_STR__119` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__120`;
CREATE TABLE `OBJ_INDEX_STR__120` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__121`;
CREATE TABLE `OBJ_INDEX_STR__121` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__122`;
CREATE TABLE `OBJ_INDEX_STR__122` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__123`;
CREATE TABLE `OBJ_INDEX_STR__123` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__124`;
CREATE TABLE `OBJ_INDEX_STR__124` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__125`;
CREATE TABLE `OBJ_INDEX_STR__125` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__126`;
CREATE TABLE `OBJ_INDEX_STR__126` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_STR__127`;
CREATE TABLE `OBJ_INDEX_STR__127` (
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
