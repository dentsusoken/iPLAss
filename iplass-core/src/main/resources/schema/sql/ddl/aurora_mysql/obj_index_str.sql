/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ`;
CREATE TABLE `OBJ_INDEX_STR__APQ` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__1`;
CREATE TABLE `OBJ_INDEX_STR__APQ__1` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__1_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__1_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__2`;
CREATE TABLE `OBJ_INDEX_STR__APQ__2` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__2_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__2_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__3`;
CREATE TABLE `OBJ_INDEX_STR__APQ__3` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__3_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__3_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__4`;
CREATE TABLE `OBJ_INDEX_STR__APQ__4` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__4_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__4_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__5`;
CREATE TABLE `OBJ_INDEX_STR__APQ__5` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__5_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__5_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__6`;
CREATE TABLE `OBJ_INDEX_STR__APQ__6` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__6_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__6_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__7`;
CREATE TABLE `OBJ_INDEX_STR__APQ__7` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__7_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__7_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__8`;
CREATE TABLE `OBJ_INDEX_STR__APQ__8` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__8_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__8_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__9`;
CREATE TABLE `OBJ_INDEX_STR__APQ__9` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__9_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__9_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__10`;
CREATE TABLE `OBJ_INDEX_STR__APQ__10` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__10_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__10_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__11`;
CREATE TABLE `OBJ_INDEX_STR__APQ__11` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__11_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__11_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__12`;
CREATE TABLE `OBJ_INDEX_STR__APQ__12` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__12_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__12_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__13`;
CREATE TABLE `OBJ_INDEX_STR__APQ__13` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__13_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__13_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__14`;
CREATE TABLE `OBJ_INDEX_STR__APQ__14` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__14_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__14_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__15`;
CREATE TABLE `OBJ_INDEX_STR__APQ__15` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__15_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__15_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__16`;
CREATE TABLE `OBJ_INDEX_STR__APQ__16` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__16_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__16_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__17`;
CREATE TABLE `OBJ_INDEX_STR__APQ__17` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__17_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__17_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__18`;
CREATE TABLE `OBJ_INDEX_STR__APQ__18` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__18_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__18_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__19`;
CREATE TABLE `OBJ_INDEX_STR__APQ__19` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__19_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__19_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__20`;
CREATE TABLE `OBJ_INDEX_STR__APQ__20` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__20_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__20_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__21`;
CREATE TABLE `OBJ_INDEX_STR__APQ__21` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__21_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__21_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__22`;
CREATE TABLE `OBJ_INDEX_STR__APQ__22` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__22_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__22_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__23`;
CREATE TABLE `OBJ_INDEX_STR__APQ__23` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__23_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__23_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__24`;
CREATE TABLE `OBJ_INDEX_STR__APQ__24` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__24_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__24_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__25`;
CREATE TABLE `OBJ_INDEX_STR__APQ__25` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__25_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__25_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__26`;
CREATE TABLE `OBJ_INDEX_STR__APQ__26` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__26_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__26_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__27`;
CREATE TABLE `OBJ_INDEX_STR__APQ__27` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__27_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__27_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__28`;
CREATE TABLE `OBJ_INDEX_STR__APQ__28` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__28_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__28_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__29`;
CREATE TABLE `OBJ_INDEX_STR__APQ__29` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__29_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__29_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__30`;
CREATE TABLE `OBJ_INDEX_STR__APQ__30` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__30_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__30_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__31`;
CREATE TABLE `OBJ_INDEX_STR__APQ__31` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__31_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__31_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__32`;
CREATE TABLE `OBJ_INDEX_STR__APQ__32` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__32_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__32_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__33`;
CREATE TABLE `OBJ_INDEX_STR__APQ__33` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__33_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__33_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__34`;
CREATE TABLE `OBJ_INDEX_STR__APQ__34` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__34_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__34_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__35`;
CREATE TABLE `OBJ_INDEX_STR__APQ__35` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__35_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__35_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__36`;
CREATE TABLE `OBJ_INDEX_STR__APQ__36` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__36_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__36_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__37`;
CREATE TABLE `OBJ_INDEX_STR__APQ__37` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__37_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__37_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__38`;
CREATE TABLE `OBJ_INDEX_STR__APQ__38` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__38_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__38_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__39`;
CREATE TABLE `OBJ_INDEX_STR__APQ__39` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__39_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__39_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__40`;
CREATE TABLE `OBJ_INDEX_STR__APQ__40` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__40_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__40_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__41`;
CREATE TABLE `OBJ_INDEX_STR__APQ__41` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__41_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__41_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__42`;
CREATE TABLE `OBJ_INDEX_STR__APQ__42` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__42_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__42_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__43`;
CREATE TABLE `OBJ_INDEX_STR__APQ__43` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__43_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__43_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__44`;
CREATE TABLE `OBJ_INDEX_STR__APQ__44` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__44_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__44_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__45`;
CREATE TABLE `OBJ_INDEX_STR__APQ__45` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__45_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__45_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__46`;
CREATE TABLE `OBJ_INDEX_STR__APQ__46` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__46_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__46_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__47`;
CREATE TABLE `OBJ_INDEX_STR__APQ__47` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__47_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__47_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__48`;
CREATE TABLE `OBJ_INDEX_STR__APQ__48` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__48_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__48_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__49`;
CREATE TABLE `OBJ_INDEX_STR__APQ__49` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__49_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__49_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__50`;
CREATE TABLE `OBJ_INDEX_STR__APQ__50` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__50_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__50_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__51`;
CREATE TABLE `OBJ_INDEX_STR__APQ__51` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__51_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__51_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__52`;
CREATE TABLE `OBJ_INDEX_STR__APQ__52` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__52_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__52_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__53`;
CREATE TABLE `OBJ_INDEX_STR__APQ__53` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__53_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__53_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__54`;
CREATE TABLE `OBJ_INDEX_STR__APQ__54` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__54_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__54_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__55`;
CREATE TABLE `OBJ_INDEX_STR__APQ__55` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__55_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__55_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__56`;
CREATE TABLE `OBJ_INDEX_STR__APQ__56` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__56_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__56_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__57`;
CREATE TABLE `OBJ_INDEX_STR__APQ__57` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__57_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__57_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__58`;
CREATE TABLE `OBJ_INDEX_STR__APQ__58` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__58_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__58_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__59`;
CREATE TABLE `OBJ_INDEX_STR__APQ__59` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__59_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__59_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__60`;
CREATE TABLE `OBJ_INDEX_STR__APQ__60` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__60_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__60_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__61`;
CREATE TABLE `OBJ_INDEX_STR__APQ__61` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__61_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__61_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__62`;
CREATE TABLE `OBJ_INDEX_STR__APQ__62` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__62_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__62_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__63`;
CREATE TABLE `OBJ_INDEX_STR__APQ__63` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__63_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__63_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__64`;
CREATE TABLE `OBJ_INDEX_STR__APQ__64` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__64_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__64_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__65`;
CREATE TABLE `OBJ_INDEX_STR__APQ__65` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__65_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__65_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__66`;
CREATE TABLE `OBJ_INDEX_STR__APQ__66` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__66_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__66_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__67`;
CREATE TABLE `OBJ_INDEX_STR__APQ__67` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__67_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__67_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__68`;
CREATE TABLE `OBJ_INDEX_STR__APQ__68` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__68_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__68_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__69`;
CREATE TABLE `OBJ_INDEX_STR__APQ__69` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__69_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__69_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__70`;
CREATE TABLE `OBJ_INDEX_STR__APQ__70` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__70_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__70_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__71`;
CREATE TABLE `OBJ_INDEX_STR__APQ__71` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__71_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__71_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__72`;
CREATE TABLE `OBJ_INDEX_STR__APQ__72` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__72_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__72_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__73`;
CREATE TABLE `OBJ_INDEX_STR__APQ__73` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__73_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__73_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__74`;
CREATE TABLE `OBJ_INDEX_STR__APQ__74` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__74_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__74_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__75`;
CREATE TABLE `OBJ_INDEX_STR__APQ__75` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__75_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__75_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__76`;
CREATE TABLE `OBJ_INDEX_STR__APQ__76` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__76_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__76_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__77`;
CREATE TABLE `OBJ_INDEX_STR__APQ__77` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__77_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__77_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__78`;
CREATE TABLE `OBJ_INDEX_STR__APQ__78` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__78_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__78_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__79`;
CREATE TABLE `OBJ_INDEX_STR__APQ__79` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__79_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__79_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__80`;
CREATE TABLE `OBJ_INDEX_STR__APQ__80` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__80_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__80_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__81`;
CREATE TABLE `OBJ_INDEX_STR__APQ__81` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__81_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__81_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__82`;
CREATE TABLE `OBJ_INDEX_STR__APQ__82` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__82_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__82_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__83`;
CREATE TABLE `OBJ_INDEX_STR__APQ__83` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__83_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__83_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__84`;
CREATE TABLE `OBJ_INDEX_STR__APQ__84` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__84_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__84_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__85`;
CREATE TABLE `OBJ_INDEX_STR__APQ__85` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__85_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__85_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__86`;
CREATE TABLE `OBJ_INDEX_STR__APQ__86` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__86_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__86_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__87`;
CREATE TABLE `OBJ_INDEX_STR__APQ__87` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__87_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__87_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__88`;
CREATE TABLE `OBJ_INDEX_STR__APQ__88` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__88_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__88_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__89`;
CREATE TABLE `OBJ_INDEX_STR__APQ__89` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__89_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__89_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__90`;
CREATE TABLE `OBJ_INDEX_STR__APQ__90` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__90_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__90_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__91`;
CREATE TABLE `OBJ_INDEX_STR__APQ__91` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__91_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__91_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__92`;
CREATE TABLE `OBJ_INDEX_STR__APQ__92` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__92_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__92_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__93`;
CREATE TABLE `OBJ_INDEX_STR__APQ__93` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__93_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__93_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__94`;
CREATE TABLE `OBJ_INDEX_STR__APQ__94` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__94_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__94_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__95`;
CREATE TABLE `OBJ_INDEX_STR__APQ__95` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__95_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__95_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__96`;
CREATE TABLE `OBJ_INDEX_STR__APQ__96` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__96_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__96_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__97`;
CREATE TABLE `OBJ_INDEX_STR__APQ__97` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__97_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__97_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__98`;
CREATE TABLE `OBJ_INDEX_STR__APQ__98` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__98_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__98_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__99`;
CREATE TABLE `OBJ_INDEX_STR__APQ__99` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__99_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__99_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__100`;
CREATE TABLE `OBJ_INDEX_STR__APQ__100` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__100_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__100_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__101`;
CREATE TABLE `OBJ_INDEX_STR__APQ__101` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__101_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__101_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__102`;
CREATE TABLE `OBJ_INDEX_STR__APQ__102` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__102_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__102_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__103`;
CREATE TABLE `OBJ_INDEX_STR__APQ__103` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__103_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__103_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__104`;
CREATE TABLE `OBJ_INDEX_STR__APQ__104` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__104_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__104_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__105`;
CREATE TABLE `OBJ_INDEX_STR__APQ__105` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__105_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__105_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__106`;
CREATE TABLE `OBJ_INDEX_STR__APQ__106` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__106_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__106_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__107`;
CREATE TABLE `OBJ_INDEX_STR__APQ__107` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__107_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__107_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__108`;
CREATE TABLE `OBJ_INDEX_STR__APQ__108` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__108_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__108_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__109`;
CREATE TABLE `OBJ_INDEX_STR__APQ__109` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__109_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__109_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__110`;
CREATE TABLE `OBJ_INDEX_STR__APQ__110` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__110_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__110_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__111`;
CREATE TABLE `OBJ_INDEX_STR__APQ__111` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__111_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__111_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__112`;
CREATE TABLE `OBJ_INDEX_STR__APQ__112` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__112_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__112_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__113`;
CREATE TABLE `OBJ_INDEX_STR__APQ__113` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__113_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__113_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__114`;
CREATE TABLE `OBJ_INDEX_STR__APQ__114` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__114_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__114_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__115`;
CREATE TABLE `OBJ_INDEX_STR__APQ__115` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__115_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__115_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__116`;
CREATE TABLE `OBJ_INDEX_STR__APQ__116` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__116_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__116_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__117`;
CREATE TABLE `OBJ_INDEX_STR__APQ__117` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__117_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__117_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__118`;
CREATE TABLE `OBJ_INDEX_STR__APQ__118` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__118_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__118_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__119`;
CREATE TABLE `OBJ_INDEX_STR__APQ__119` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__119_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__119_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__120`;
CREATE TABLE `OBJ_INDEX_STR__APQ__120` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__120_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__120_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__121`;
CREATE TABLE `OBJ_INDEX_STR__APQ__121` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__121_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__121_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__122`;
CREATE TABLE `OBJ_INDEX_STR__APQ__122` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__122_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__122_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__123`;
CREATE TABLE `OBJ_INDEX_STR__APQ__123` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__123_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__123_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__124`;
CREATE TABLE `OBJ_INDEX_STR__APQ__124` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__124_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__124_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__125`;
CREATE TABLE `OBJ_INDEX_STR__APQ__125` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__125_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__125_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__126`;
CREATE TABLE `OBJ_INDEX_STR__APQ__126` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__126_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__126_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_STR */
DROP TABLE IF EXISTS `OBJ_INDEX_STR__APQ__127`;
CREATE TABLE `OBJ_INDEX_STR__APQ__127` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` VARCHAR(192) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_str__APQ__127_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`(192)),
  INDEX `obj_index_str__APQ__127_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
