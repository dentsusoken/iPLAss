/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ`;
CREATE TABLE `OBJ_INDEX_TS__APQ` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__1`;
CREATE TABLE `OBJ_INDEX_TS__APQ__1` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__2`;
CREATE TABLE `OBJ_INDEX_TS__APQ__2` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__2` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__3`;
CREATE TABLE `OBJ_INDEX_TS__APQ__3` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__3` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__3` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__4`;
CREATE TABLE `OBJ_INDEX_TS__APQ__4` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__4` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__4` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__5`;
CREATE TABLE `OBJ_INDEX_TS__APQ__5` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__5` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__5` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__6`;
CREATE TABLE `OBJ_INDEX_TS__APQ__6` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__6` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__6` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__7`;
CREATE TABLE `OBJ_INDEX_TS__APQ__7` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__7` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__7` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__8`;
CREATE TABLE `OBJ_INDEX_TS__APQ__8` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__8` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__8` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__9`;
CREATE TABLE `OBJ_INDEX_TS__APQ__9` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__9` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__9` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__10`;
CREATE TABLE `OBJ_INDEX_TS__APQ__10` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__10` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__10` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__11`;
CREATE TABLE `OBJ_INDEX_TS__APQ__11` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__11` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__11` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__12`;
CREATE TABLE `OBJ_INDEX_TS__APQ__12` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__12` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__12` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__13`;
CREATE TABLE `OBJ_INDEX_TS__APQ__13` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__13` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__13` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__14`;
CREATE TABLE `OBJ_INDEX_TS__APQ__14` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__14` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__14` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__15`;
CREATE TABLE `OBJ_INDEX_TS__APQ__15` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__15` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__15` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__16`;
CREATE TABLE `OBJ_INDEX_TS__APQ__16` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__16` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__16` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__17`;
CREATE TABLE `OBJ_INDEX_TS__APQ__17` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__17` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__17` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__18`;
CREATE TABLE `OBJ_INDEX_TS__APQ__18` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__18` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__18` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__19`;
CREATE TABLE `OBJ_INDEX_TS__APQ__19` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__19` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__19` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__20`;
CREATE TABLE `OBJ_INDEX_TS__APQ__20` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__20` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__20` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__21`;
CREATE TABLE `OBJ_INDEX_TS__APQ__21` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__21` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__21` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__22`;
CREATE TABLE `OBJ_INDEX_TS__APQ__22` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__22` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__22` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__23`;
CREATE TABLE `OBJ_INDEX_TS__APQ__23` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__23` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__23` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__24`;
CREATE TABLE `OBJ_INDEX_TS__APQ__24` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__24` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__24` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__25`;
CREATE TABLE `OBJ_INDEX_TS__APQ__25` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__25` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__25` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__26`;
CREATE TABLE `OBJ_INDEX_TS__APQ__26` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__26` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__26` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__27`;
CREATE TABLE `OBJ_INDEX_TS__APQ__27` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__27` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__27` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__28`;
CREATE TABLE `OBJ_INDEX_TS__APQ__28` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__28` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__28` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__29`;
CREATE TABLE `OBJ_INDEX_TS__APQ__29` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__29` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__29` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__30`;
CREATE TABLE `OBJ_INDEX_TS__APQ__30` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__30` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__30` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__31`;
CREATE TABLE `OBJ_INDEX_TS__APQ__31` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__31` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__31` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__32`;
CREATE TABLE `OBJ_INDEX_TS__APQ__32` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__32` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__32` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__33`;
CREATE TABLE `OBJ_INDEX_TS__APQ__33` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__33` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__33` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__34`;
CREATE TABLE `OBJ_INDEX_TS__APQ__34` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__34` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__34` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__35`;
CREATE TABLE `OBJ_INDEX_TS__APQ__35` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__35` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__35` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__36`;
CREATE TABLE `OBJ_INDEX_TS__APQ__36` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__36` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__36` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__37`;
CREATE TABLE `OBJ_INDEX_TS__APQ__37` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__37` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__37` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__38`;
CREATE TABLE `OBJ_INDEX_TS__APQ__38` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__38` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__38` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__39`;
CREATE TABLE `OBJ_INDEX_TS__APQ__39` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__39` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__39` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__40`;
CREATE TABLE `OBJ_INDEX_TS__APQ__40` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__40` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__40` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__41`;
CREATE TABLE `OBJ_INDEX_TS__APQ__41` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__41` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__41` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__42`;
CREATE TABLE `OBJ_INDEX_TS__APQ__42` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__42` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__42` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__43`;
CREATE TABLE `OBJ_INDEX_TS__APQ__43` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__43` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__43` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__44`;
CREATE TABLE `OBJ_INDEX_TS__APQ__44` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__44` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__44` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__45`;
CREATE TABLE `OBJ_INDEX_TS__APQ__45` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__45` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__45` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__46`;
CREATE TABLE `OBJ_INDEX_TS__APQ__46` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__46` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__46` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__47`;
CREATE TABLE `OBJ_INDEX_TS__APQ__47` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__47` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__47` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__48`;
CREATE TABLE `OBJ_INDEX_TS__APQ__48` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__48` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__48` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__49`;
CREATE TABLE `OBJ_INDEX_TS__APQ__49` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__49` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__49` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__50`;
CREATE TABLE `OBJ_INDEX_TS__APQ__50` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__50` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__50` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__51`;
CREATE TABLE `OBJ_INDEX_TS__APQ__51` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__51` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__51` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__52`;
CREATE TABLE `OBJ_INDEX_TS__APQ__52` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__52` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__52` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__53`;
CREATE TABLE `OBJ_INDEX_TS__APQ__53` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__53` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__53` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__54`;
CREATE TABLE `OBJ_INDEX_TS__APQ__54` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__54` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__54` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__55`;
CREATE TABLE `OBJ_INDEX_TS__APQ__55` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__55` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__55` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__56`;
CREATE TABLE `OBJ_INDEX_TS__APQ__56` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__56` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__56` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__57`;
CREATE TABLE `OBJ_INDEX_TS__APQ__57` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__57` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__57` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__58`;
CREATE TABLE `OBJ_INDEX_TS__APQ__58` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__58` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__58` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__59`;
CREATE TABLE `OBJ_INDEX_TS__APQ__59` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__59` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__59` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__60`;
CREATE TABLE `OBJ_INDEX_TS__APQ__60` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__60` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__60` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__61`;
CREATE TABLE `OBJ_INDEX_TS__APQ__61` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__61` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__61` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__62`;
CREATE TABLE `OBJ_INDEX_TS__APQ__62` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__62` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__62` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__63`;
CREATE TABLE `OBJ_INDEX_TS__APQ__63` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__63` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__63` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__64`;
CREATE TABLE `OBJ_INDEX_TS__APQ__64` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__64` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__64` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__65`;
CREATE TABLE `OBJ_INDEX_TS__APQ__65` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__65` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__65` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__66`;
CREATE TABLE `OBJ_INDEX_TS__APQ__66` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__66` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__66` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__67`;
CREATE TABLE `OBJ_INDEX_TS__APQ__67` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__67` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__67` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__68`;
CREATE TABLE `OBJ_INDEX_TS__APQ__68` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__68` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__68` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__69`;
CREATE TABLE `OBJ_INDEX_TS__APQ__69` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__69` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__69` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__70`;
CREATE TABLE `OBJ_INDEX_TS__APQ__70` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__70` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__70` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__71`;
CREATE TABLE `OBJ_INDEX_TS__APQ__71` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__71` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__71` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__72`;
CREATE TABLE `OBJ_INDEX_TS__APQ__72` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__72` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__72` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__73`;
CREATE TABLE `OBJ_INDEX_TS__APQ__73` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__73` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__73` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__74`;
CREATE TABLE `OBJ_INDEX_TS__APQ__74` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__74` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__74` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__75`;
CREATE TABLE `OBJ_INDEX_TS__APQ__75` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__75` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__75` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__76`;
CREATE TABLE `OBJ_INDEX_TS__APQ__76` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__76` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__76` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__77`;
CREATE TABLE `OBJ_INDEX_TS__APQ__77` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__77` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__77` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__78`;
CREATE TABLE `OBJ_INDEX_TS__APQ__78` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__78` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__78` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__79`;
CREATE TABLE `OBJ_INDEX_TS__APQ__79` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__79` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__79` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__80`;
CREATE TABLE `OBJ_INDEX_TS__APQ__80` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__80` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__80` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__81`;
CREATE TABLE `OBJ_INDEX_TS__APQ__81` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__81` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__81` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__82`;
CREATE TABLE `OBJ_INDEX_TS__APQ__82` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__82` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__82` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__83`;
CREATE TABLE `OBJ_INDEX_TS__APQ__83` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__83` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__83` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__84`;
CREATE TABLE `OBJ_INDEX_TS__APQ__84` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__84` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__84` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__85`;
CREATE TABLE `OBJ_INDEX_TS__APQ__85` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__85` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__85` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__86`;
CREATE TABLE `OBJ_INDEX_TS__APQ__86` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__86` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__86` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__87`;
CREATE TABLE `OBJ_INDEX_TS__APQ__87` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__87` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__87` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__88`;
CREATE TABLE `OBJ_INDEX_TS__APQ__88` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__88` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__88` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__89`;
CREATE TABLE `OBJ_INDEX_TS__APQ__89` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__89` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__89` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__90`;
CREATE TABLE `OBJ_INDEX_TS__APQ__90` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__90` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__90` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__91`;
CREATE TABLE `OBJ_INDEX_TS__APQ__91` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__91` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__91` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__92`;
CREATE TABLE `OBJ_INDEX_TS__APQ__92` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__92` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__92` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__93`;
CREATE TABLE `OBJ_INDEX_TS__APQ__93` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__93` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__93` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__94`;
CREATE TABLE `OBJ_INDEX_TS__APQ__94` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__94` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__94` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__95`;
CREATE TABLE `OBJ_INDEX_TS__APQ__95` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__95` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__95` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__96`;
CREATE TABLE `OBJ_INDEX_TS__APQ__96` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__96` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__96` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__97`;
CREATE TABLE `OBJ_INDEX_TS__APQ__97` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__97` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__97` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__98`;
CREATE TABLE `OBJ_INDEX_TS__APQ__98` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__98` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__98` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__99`;
CREATE TABLE `OBJ_INDEX_TS__APQ__99` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__99` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__99` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__100`;
CREATE TABLE `OBJ_INDEX_TS__APQ__100` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__100` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__100` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__101`;
CREATE TABLE `OBJ_INDEX_TS__APQ__101` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__101` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__101` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__102`;
CREATE TABLE `OBJ_INDEX_TS__APQ__102` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__102` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__102` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__103`;
CREATE TABLE `OBJ_INDEX_TS__APQ__103` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__103` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__103` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__104`;
CREATE TABLE `OBJ_INDEX_TS__APQ__104` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__104` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__104` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__105`;
CREATE TABLE `OBJ_INDEX_TS__APQ__105` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__105` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__105` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__106`;
CREATE TABLE `OBJ_INDEX_TS__APQ__106` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__106` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__106` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__107`;
CREATE TABLE `OBJ_INDEX_TS__APQ__107` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__107` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__107` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__108`;
CREATE TABLE `OBJ_INDEX_TS__APQ__108` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__108` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__108` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__109`;
CREATE TABLE `OBJ_INDEX_TS__APQ__109` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__109` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__109` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__110`;
CREATE TABLE `OBJ_INDEX_TS__APQ__110` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__110` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__110` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__111`;
CREATE TABLE `OBJ_INDEX_TS__APQ__111` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__111` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__111` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__112`;
CREATE TABLE `OBJ_INDEX_TS__APQ__112` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__112` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__112` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__113`;
CREATE TABLE `OBJ_INDEX_TS__APQ__113` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__113` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__113` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__114`;
CREATE TABLE `OBJ_INDEX_TS__APQ__114` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__114` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__114` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__115`;
CREATE TABLE `OBJ_INDEX_TS__APQ__115` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__115` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__115` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__116`;
CREATE TABLE `OBJ_INDEX_TS__APQ__116` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__116` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__116` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__117`;
CREATE TABLE `OBJ_INDEX_TS__APQ__117` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__117` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__117` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__118`;
CREATE TABLE `OBJ_INDEX_TS__APQ__118` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__118` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__118` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__119`;
CREATE TABLE `OBJ_INDEX_TS__APQ__119` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__119` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__119` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__120`;
CREATE TABLE `OBJ_INDEX_TS__APQ__120` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__120` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__120` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__121`;
CREATE TABLE `OBJ_INDEX_TS__APQ__121` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__121` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__121` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__122`;
CREATE TABLE `OBJ_INDEX_TS__APQ__122` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__122` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__122` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__123`;
CREATE TABLE `OBJ_INDEX_TS__APQ__123` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__123` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__123` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__124`;
CREATE TABLE `OBJ_INDEX_TS__APQ__124` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__124` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__124` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__125`;
CREATE TABLE `OBJ_INDEX_TS__APQ__125` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__125` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__125` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__126`;
CREATE TABLE `OBJ_INDEX_TS__APQ__126` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__126` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__126` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS__APQ__127`;
CREATE TABLE `OBJ_INDEX_TS__APQ__127` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__APQ__127` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__APQ__127` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)
;
