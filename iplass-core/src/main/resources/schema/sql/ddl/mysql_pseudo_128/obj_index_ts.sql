/* drop/create OBJ_INDEX_TS */
DROP TABLE IF EXISTS `OBJ_INDEX_TS`;
CREATE TABLE `OBJ_INDEX_TS` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__1`;
CREATE TABLE `OBJ_INDEX_TS__1` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__2`;
CREATE TABLE `OBJ_INDEX_TS__2` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__3`;
CREATE TABLE `OBJ_INDEX_TS__3` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__4`;
CREATE TABLE `OBJ_INDEX_TS__4` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__5`;
CREATE TABLE `OBJ_INDEX_TS__5` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__6`;
CREATE TABLE `OBJ_INDEX_TS__6` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__7`;
CREATE TABLE `OBJ_INDEX_TS__7` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__8`;
CREATE TABLE `OBJ_INDEX_TS__8` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__9`;
CREATE TABLE `OBJ_INDEX_TS__9` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__10`;
CREATE TABLE `OBJ_INDEX_TS__10` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__11`;
CREATE TABLE `OBJ_INDEX_TS__11` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__12`;
CREATE TABLE `OBJ_INDEX_TS__12` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__13`;
CREATE TABLE `OBJ_INDEX_TS__13` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__14`;
CREATE TABLE `OBJ_INDEX_TS__14` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__15`;
CREATE TABLE `OBJ_INDEX_TS__15` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__16`;
CREATE TABLE `OBJ_INDEX_TS__16` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__17`;
CREATE TABLE `OBJ_INDEX_TS__17` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__18`;
CREATE TABLE `OBJ_INDEX_TS__18` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__19`;
CREATE TABLE `OBJ_INDEX_TS__19` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__20`;
CREATE TABLE `OBJ_INDEX_TS__20` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__21`;
CREATE TABLE `OBJ_INDEX_TS__21` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__22`;
CREATE TABLE `OBJ_INDEX_TS__22` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__23`;
CREATE TABLE `OBJ_INDEX_TS__23` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__24`;
CREATE TABLE `OBJ_INDEX_TS__24` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__25`;
CREATE TABLE `OBJ_INDEX_TS__25` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__26`;
CREATE TABLE `OBJ_INDEX_TS__26` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__27`;
CREATE TABLE `OBJ_INDEX_TS__27` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__28`;
CREATE TABLE `OBJ_INDEX_TS__28` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__29`;
CREATE TABLE `OBJ_INDEX_TS__29` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__30`;
CREATE TABLE `OBJ_INDEX_TS__30` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__31`;
CREATE TABLE `OBJ_INDEX_TS__31` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__32`;
CREATE TABLE `OBJ_INDEX_TS__32` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__33`;
CREATE TABLE `OBJ_INDEX_TS__33` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__34`;
CREATE TABLE `OBJ_INDEX_TS__34` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__35`;
CREATE TABLE `OBJ_INDEX_TS__35` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__36`;
CREATE TABLE `OBJ_INDEX_TS__36` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__37`;
CREATE TABLE `OBJ_INDEX_TS__37` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__38`;
CREATE TABLE `OBJ_INDEX_TS__38` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__39`;
CREATE TABLE `OBJ_INDEX_TS__39` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__40`;
CREATE TABLE `OBJ_INDEX_TS__40` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__41`;
CREATE TABLE `OBJ_INDEX_TS__41` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__42`;
CREATE TABLE `OBJ_INDEX_TS__42` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__43`;
CREATE TABLE `OBJ_INDEX_TS__43` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__44`;
CREATE TABLE `OBJ_INDEX_TS__44` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__45`;
CREATE TABLE `OBJ_INDEX_TS__45` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__46`;
CREATE TABLE `OBJ_INDEX_TS__46` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__47`;
CREATE TABLE `OBJ_INDEX_TS__47` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__48`;
CREATE TABLE `OBJ_INDEX_TS__48` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__49`;
CREATE TABLE `OBJ_INDEX_TS__49` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__50`;
CREATE TABLE `OBJ_INDEX_TS__50` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__51`;
CREATE TABLE `OBJ_INDEX_TS__51` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__52`;
CREATE TABLE `OBJ_INDEX_TS__52` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__53`;
CREATE TABLE `OBJ_INDEX_TS__53` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__54`;
CREATE TABLE `OBJ_INDEX_TS__54` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__55`;
CREATE TABLE `OBJ_INDEX_TS__55` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__56`;
CREATE TABLE `OBJ_INDEX_TS__56` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__57`;
CREATE TABLE `OBJ_INDEX_TS__57` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__58`;
CREATE TABLE `OBJ_INDEX_TS__58` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__59`;
CREATE TABLE `OBJ_INDEX_TS__59` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__60`;
CREATE TABLE `OBJ_INDEX_TS__60` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__61`;
CREATE TABLE `OBJ_INDEX_TS__61` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__62`;
CREATE TABLE `OBJ_INDEX_TS__62` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__63`;
CREATE TABLE `OBJ_INDEX_TS__63` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__64`;
CREATE TABLE `OBJ_INDEX_TS__64` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__65`;
CREATE TABLE `OBJ_INDEX_TS__65` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__66`;
CREATE TABLE `OBJ_INDEX_TS__66` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__67`;
CREATE TABLE `OBJ_INDEX_TS__67` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__68`;
CREATE TABLE `OBJ_INDEX_TS__68` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__69`;
CREATE TABLE `OBJ_INDEX_TS__69` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__70`;
CREATE TABLE `OBJ_INDEX_TS__70` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__71`;
CREATE TABLE `OBJ_INDEX_TS__71` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__72`;
CREATE TABLE `OBJ_INDEX_TS__72` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__73`;
CREATE TABLE `OBJ_INDEX_TS__73` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__74`;
CREATE TABLE `OBJ_INDEX_TS__74` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__75`;
CREATE TABLE `OBJ_INDEX_TS__75` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__76`;
CREATE TABLE `OBJ_INDEX_TS__76` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__77`;
CREATE TABLE `OBJ_INDEX_TS__77` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__78`;
CREATE TABLE `OBJ_INDEX_TS__78` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__79`;
CREATE TABLE `OBJ_INDEX_TS__79` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__80`;
CREATE TABLE `OBJ_INDEX_TS__80` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__81`;
CREATE TABLE `OBJ_INDEX_TS__81` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__82`;
CREATE TABLE `OBJ_INDEX_TS__82` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__83`;
CREATE TABLE `OBJ_INDEX_TS__83` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__84`;
CREATE TABLE `OBJ_INDEX_TS__84` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__85`;
CREATE TABLE `OBJ_INDEX_TS__85` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__86`;
CREATE TABLE `OBJ_INDEX_TS__86` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__87`;
CREATE TABLE `OBJ_INDEX_TS__87` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__88`;
CREATE TABLE `OBJ_INDEX_TS__88` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__89`;
CREATE TABLE `OBJ_INDEX_TS__89` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__90`;
CREATE TABLE `OBJ_INDEX_TS__90` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__91`;
CREATE TABLE `OBJ_INDEX_TS__91` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__92`;
CREATE TABLE `OBJ_INDEX_TS__92` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__93`;
CREATE TABLE `OBJ_INDEX_TS__93` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__94`;
CREATE TABLE `OBJ_INDEX_TS__94` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__95`;
CREATE TABLE `OBJ_INDEX_TS__95` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__96`;
CREATE TABLE `OBJ_INDEX_TS__96` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__97`;
CREATE TABLE `OBJ_INDEX_TS__97` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__98`;
CREATE TABLE `OBJ_INDEX_TS__98` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__99`;
CREATE TABLE `OBJ_INDEX_TS__99` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__100`;
CREATE TABLE `OBJ_INDEX_TS__100` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__101`;
CREATE TABLE `OBJ_INDEX_TS__101` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__102`;
CREATE TABLE `OBJ_INDEX_TS__102` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__103`;
CREATE TABLE `OBJ_INDEX_TS__103` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__104`;
CREATE TABLE `OBJ_INDEX_TS__104` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__105`;
CREATE TABLE `OBJ_INDEX_TS__105` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__106`;
CREATE TABLE `OBJ_INDEX_TS__106` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__107`;
CREATE TABLE `OBJ_INDEX_TS__107` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__108`;
CREATE TABLE `OBJ_INDEX_TS__108` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__109`;
CREATE TABLE `OBJ_INDEX_TS__109` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__110`;
CREATE TABLE `OBJ_INDEX_TS__110` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__111`;
CREATE TABLE `OBJ_INDEX_TS__111` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__112`;
CREATE TABLE `OBJ_INDEX_TS__112` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__113`;
CREATE TABLE `OBJ_INDEX_TS__113` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__114`;
CREATE TABLE `OBJ_INDEX_TS__114` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__115`;
CREATE TABLE `OBJ_INDEX_TS__115` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__116`;
CREATE TABLE `OBJ_INDEX_TS__116` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__117`;
CREATE TABLE `OBJ_INDEX_TS__117` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__118`;
CREATE TABLE `OBJ_INDEX_TS__118` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__119`;
CREATE TABLE `OBJ_INDEX_TS__119` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__120`;
CREATE TABLE `OBJ_INDEX_TS__120` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__121`;
CREATE TABLE `OBJ_INDEX_TS__121` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__122`;
CREATE TABLE `OBJ_INDEX_TS__122` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__123`;
CREATE TABLE `OBJ_INDEX_TS__123` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__124`;
CREATE TABLE `OBJ_INDEX_TS__124` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__125`;
CREATE TABLE `OBJ_INDEX_TS__125` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__126`;
CREATE TABLE `OBJ_INDEX_TS__126` (
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
DROP TABLE IF EXISTS `OBJ_INDEX_TS__127`;
CREATE TABLE `OBJ_INDEX_TS__127` (
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
