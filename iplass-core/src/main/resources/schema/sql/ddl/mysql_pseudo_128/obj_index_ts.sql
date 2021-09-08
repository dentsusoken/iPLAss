/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts`;
CREATE TABLE `obj_index_ts` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__1`;
CREATE TABLE `obj_index_ts__1` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__1` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__2`;
CREATE TABLE `obj_index_ts__2` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__2` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__2` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__3`;
CREATE TABLE `obj_index_ts__3` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__3` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__3` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__4`;
CREATE TABLE `obj_index_ts__4` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__4` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__4` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__5`;
CREATE TABLE `obj_index_ts__5` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__5` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__5` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__6`;
CREATE TABLE `obj_index_ts__6` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__6` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__6` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__7`;
CREATE TABLE `obj_index_ts__7` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__7` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__7` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__8`;
CREATE TABLE `obj_index_ts__8` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__8` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__8` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__9`;
CREATE TABLE `obj_index_ts__9` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__9` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__9` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__10`;
CREATE TABLE `obj_index_ts__10` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__10` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__10` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__11`;
CREATE TABLE `obj_index_ts__11` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__11` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__11` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__12`;
CREATE TABLE `obj_index_ts__12` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__12` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__12` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__13`;
CREATE TABLE `obj_index_ts__13` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__13` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__13` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__14`;
CREATE TABLE `obj_index_ts__14` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__14` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__14` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__15`;
CREATE TABLE `obj_index_ts__15` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__15` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__15` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__16`;
CREATE TABLE `obj_index_ts__16` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__16` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__16` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__17`;
CREATE TABLE `obj_index_ts__17` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__17` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__17` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__18`;
CREATE TABLE `obj_index_ts__18` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__18` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__18` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__19`;
CREATE TABLE `obj_index_ts__19` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__19` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__19` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__20`;
CREATE TABLE `obj_index_ts__20` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__20` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__20` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__21`;
CREATE TABLE `obj_index_ts__21` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__21` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__21` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__22`;
CREATE TABLE `obj_index_ts__22` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__22` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__22` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__23`;
CREATE TABLE `obj_index_ts__23` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__23` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__23` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__24`;
CREATE TABLE `obj_index_ts__24` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__24` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__24` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__25`;
CREATE TABLE `obj_index_ts__25` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__25` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__25` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__26`;
CREATE TABLE `obj_index_ts__26` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__26` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__26` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__27`;
CREATE TABLE `obj_index_ts__27` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__27` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__27` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__28`;
CREATE TABLE `obj_index_ts__28` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__28` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__28` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__29`;
CREATE TABLE `obj_index_ts__29` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__29` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__29` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__30`;
CREATE TABLE `obj_index_ts__30` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__30` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__30` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__31`;
CREATE TABLE `obj_index_ts__31` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__31` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__31` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__32`;
CREATE TABLE `obj_index_ts__32` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__32` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__32` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__33`;
CREATE TABLE `obj_index_ts__33` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__33` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__33` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__34`;
CREATE TABLE `obj_index_ts__34` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__34` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__34` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__35`;
CREATE TABLE `obj_index_ts__35` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__35` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__35` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__36`;
CREATE TABLE `obj_index_ts__36` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__36` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__36` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__37`;
CREATE TABLE `obj_index_ts__37` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__37` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__37` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__38`;
CREATE TABLE `obj_index_ts__38` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__38` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__38` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__39`;
CREATE TABLE `obj_index_ts__39` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__39` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__39` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__40`;
CREATE TABLE `obj_index_ts__40` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__40` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__40` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__41`;
CREATE TABLE `obj_index_ts__41` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__41` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__41` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__42`;
CREATE TABLE `obj_index_ts__42` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__42` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__42` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__43`;
CREATE TABLE `obj_index_ts__43` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__43` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__43` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__44`;
CREATE TABLE `obj_index_ts__44` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__44` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__44` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__45`;
CREATE TABLE `obj_index_ts__45` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__45` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__45` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__46`;
CREATE TABLE `obj_index_ts__46` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__46` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__46` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__47`;
CREATE TABLE `obj_index_ts__47` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__47` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__47` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__48`;
CREATE TABLE `obj_index_ts__48` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__48` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__48` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__49`;
CREATE TABLE `obj_index_ts__49` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__49` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__49` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__50`;
CREATE TABLE `obj_index_ts__50` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__50` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__50` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__51`;
CREATE TABLE `obj_index_ts__51` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__51` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__51` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__52`;
CREATE TABLE `obj_index_ts__52` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__52` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__52` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__53`;
CREATE TABLE `obj_index_ts__53` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__53` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__53` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__54`;
CREATE TABLE `obj_index_ts__54` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__54` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__54` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__55`;
CREATE TABLE `obj_index_ts__55` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__55` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__55` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__56`;
CREATE TABLE `obj_index_ts__56` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__56` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__56` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__57`;
CREATE TABLE `obj_index_ts__57` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__57` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__57` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__58`;
CREATE TABLE `obj_index_ts__58` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__58` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__58` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__59`;
CREATE TABLE `obj_index_ts__59` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__59` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__59` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__60`;
CREATE TABLE `obj_index_ts__60` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__60` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__60` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__61`;
CREATE TABLE `obj_index_ts__61` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__61` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__61` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__62`;
CREATE TABLE `obj_index_ts__62` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__62` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__62` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__63`;
CREATE TABLE `obj_index_ts__63` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__63` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__63` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__64`;
CREATE TABLE `obj_index_ts__64` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__64` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__64` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__65`;
CREATE TABLE `obj_index_ts__65` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__65` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__65` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__66`;
CREATE TABLE `obj_index_ts__66` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__66` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__66` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__67`;
CREATE TABLE `obj_index_ts__67` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__67` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__67` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__68`;
CREATE TABLE `obj_index_ts__68` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__68` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__68` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__69`;
CREATE TABLE `obj_index_ts__69` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__69` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__69` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__70`;
CREATE TABLE `obj_index_ts__70` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__70` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__70` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__71`;
CREATE TABLE `obj_index_ts__71` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__71` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__71` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__72`;
CREATE TABLE `obj_index_ts__72` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__72` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__72` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__73`;
CREATE TABLE `obj_index_ts__73` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__73` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__73` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__74`;
CREATE TABLE `obj_index_ts__74` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__74` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__74` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__75`;
CREATE TABLE `obj_index_ts__75` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__75` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__75` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__76`;
CREATE TABLE `obj_index_ts__76` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__76` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__76` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__77`;
CREATE TABLE `obj_index_ts__77` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__77` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__77` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__78`;
CREATE TABLE `obj_index_ts__78` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__78` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__78` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__79`;
CREATE TABLE `obj_index_ts__79` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__79` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__79` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__80`;
CREATE TABLE `obj_index_ts__80` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__80` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__80` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__81`;
CREATE TABLE `obj_index_ts__81` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__81` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__81` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__82`;
CREATE TABLE `obj_index_ts__82` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__82` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__82` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__83`;
CREATE TABLE `obj_index_ts__83` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__83` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__83` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__84`;
CREATE TABLE `obj_index_ts__84` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__84` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__84` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__85`;
CREATE TABLE `obj_index_ts__85` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__85` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__85` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__86`;
CREATE TABLE `obj_index_ts__86` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__86` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__86` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__87`;
CREATE TABLE `obj_index_ts__87` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__87` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__87` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__88`;
CREATE TABLE `obj_index_ts__88` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__88` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__88` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__89`;
CREATE TABLE `obj_index_ts__89` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__89` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__89` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__90`;
CREATE TABLE `obj_index_ts__90` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__90` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__90` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__91`;
CREATE TABLE `obj_index_ts__91` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__91` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__91` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__92`;
CREATE TABLE `obj_index_ts__92` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__92` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__92` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__93`;
CREATE TABLE `obj_index_ts__93` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__93` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__93` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__94`;
CREATE TABLE `obj_index_ts__94` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__94` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__94` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__95`;
CREATE TABLE `obj_index_ts__95` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__95` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__95` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__96`;
CREATE TABLE `obj_index_ts__96` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__96` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__96` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__97`;
CREATE TABLE `obj_index_ts__97` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__97` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__97` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__98`;
CREATE TABLE `obj_index_ts__98` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__98` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__98` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__99`;
CREATE TABLE `obj_index_ts__99` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__99` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__99` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__100`;
CREATE TABLE `obj_index_ts__100` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__100` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__100` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__101`;
CREATE TABLE `obj_index_ts__101` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__101` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__101` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__102`;
CREATE TABLE `obj_index_ts__102` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__102` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__102` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__103`;
CREATE TABLE `obj_index_ts__103` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__103` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__103` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__104`;
CREATE TABLE `obj_index_ts__104` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__104` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__104` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__105`;
CREATE TABLE `obj_index_ts__105` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__105` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__105` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__106`;
CREATE TABLE `obj_index_ts__106` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__106` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__106` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__107`;
CREATE TABLE `obj_index_ts__107` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__107` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__107` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__108`;
CREATE TABLE `obj_index_ts__108` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__108` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__108` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__109`;
CREATE TABLE `obj_index_ts__109` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__109` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__109` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__110`;
CREATE TABLE `obj_index_ts__110` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__110` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__110` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__111`;
CREATE TABLE `obj_index_ts__111` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__111` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__111` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__112`;
CREATE TABLE `obj_index_ts__112` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__112` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__112` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__113`;
CREATE TABLE `obj_index_ts__113` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__113` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__113` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__114`;
CREATE TABLE `obj_index_ts__114` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__114` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__114` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__115`;
CREATE TABLE `obj_index_ts__115` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__115` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__115` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__116`;
CREATE TABLE `obj_index_ts__116` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__116` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__116` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__117`;
CREATE TABLE `obj_index_ts__117` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__117` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__117` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__118`;
CREATE TABLE `obj_index_ts__118` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__118` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__118` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__119`;
CREATE TABLE `obj_index_ts__119` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__119` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__119` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__120`;
CREATE TABLE `obj_index_ts__120` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__120` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__120` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__121`;
CREATE TABLE `obj_index_ts__121` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__121` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__121` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__122`;
CREATE TABLE `obj_index_ts__122` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__122` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__122` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__123`;
CREATE TABLE `obj_index_ts__123` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__123` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__123` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__124`;
CREATE TABLE `obj_index_ts__124` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__124` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__124` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__125`;
CREATE TABLE `obj_index_ts__125` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__125` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__125` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__126`;
CREATE TABLE `obj_index_ts__126` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__126` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__126` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `obj_index_ts__127`;
CREATE TABLE `obj_index_ts__127` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) NOT NULL,
  `col_name` VARCHAR(36) NOT NULL,
  `obj_id` VARCHAR(64) NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `val` DATETIME(3) NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_index_ts_index1__127` (`tenant_id`, `obj_def_id`(128), `col_name`(36), `val`),
  INDEX `obj_index_ts_index2__127` (`tenant_id`, `obj_def_id`(128), `obj_id`(64))
)



;
