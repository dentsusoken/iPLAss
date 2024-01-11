/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF`;
CREATE TABLE `OBJ_REF` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__1`;
CREATE TABLE `OBJ_REF__1` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__1_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__1_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__2`;
CREATE TABLE `OBJ_REF__2` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__2_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__2_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__3`;
CREATE TABLE `OBJ_REF__3` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__3_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__3_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__4`;
CREATE TABLE `OBJ_REF__4` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__4_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__4_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__5`;
CREATE TABLE `OBJ_REF__5` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__5_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__5_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__6`;
CREATE TABLE `OBJ_REF__6` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__6_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__6_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__7`;
CREATE TABLE `OBJ_REF__7` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__7_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__7_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__8`;
CREATE TABLE `OBJ_REF__8` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__8_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__8_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__9`;
CREATE TABLE `OBJ_REF__9` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__9_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__9_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__10`;
CREATE TABLE `OBJ_REF__10` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__10_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__10_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__11`;
CREATE TABLE `OBJ_REF__11` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__11_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__11_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__12`;
CREATE TABLE `OBJ_REF__12` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__12_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__12_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__13`;
CREATE TABLE `OBJ_REF__13` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__13_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__13_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__14`;
CREATE TABLE `OBJ_REF__14` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__14_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__14_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__15`;
CREATE TABLE `OBJ_REF__15` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__15_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__15_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__16`;
CREATE TABLE `OBJ_REF__16` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__16_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__16_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__17`;
CREATE TABLE `OBJ_REF__17` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__17_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__17_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__18`;
CREATE TABLE `OBJ_REF__18` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__18_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__18_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__19`;
CREATE TABLE `OBJ_REF__19` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__19_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__19_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__20`;
CREATE TABLE `OBJ_REF__20` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__20_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__20_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__21`;
CREATE TABLE `OBJ_REF__21` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__21_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__21_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__22`;
CREATE TABLE `OBJ_REF__22` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__22_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__22_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__23`;
CREATE TABLE `OBJ_REF__23` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__23_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__23_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__24`;
CREATE TABLE `OBJ_REF__24` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__24_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__24_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__25`;
CREATE TABLE `OBJ_REF__25` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__25_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__25_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__26`;
CREATE TABLE `OBJ_REF__26` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__26_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__26_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__27`;
CREATE TABLE `OBJ_REF__27` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__27_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__27_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__28`;
CREATE TABLE `OBJ_REF__28` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__28_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__28_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__29`;
CREATE TABLE `OBJ_REF__29` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__29_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__29_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__30`;
CREATE TABLE `OBJ_REF__30` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__30_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__30_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__31`;
CREATE TABLE `OBJ_REF__31` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__31_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__31_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__32`;
CREATE TABLE `OBJ_REF__32` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__32_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__32_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__33`;
CREATE TABLE `OBJ_REF__33` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__33_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__33_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__34`;
CREATE TABLE `OBJ_REF__34` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__34_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__34_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__35`;
CREATE TABLE `OBJ_REF__35` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__35_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__35_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__36`;
CREATE TABLE `OBJ_REF__36` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__36_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__36_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__37`;
CREATE TABLE `OBJ_REF__37` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__37_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__37_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__38`;
CREATE TABLE `OBJ_REF__38` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__38_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__38_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__39`;
CREATE TABLE `OBJ_REF__39` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__39_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__39_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__40`;
CREATE TABLE `OBJ_REF__40` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__40_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__40_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__41`;
CREATE TABLE `OBJ_REF__41` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__41_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__41_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__42`;
CREATE TABLE `OBJ_REF__42` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__42_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__42_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__43`;
CREATE TABLE `OBJ_REF__43` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__43_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__43_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__44`;
CREATE TABLE `OBJ_REF__44` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__44_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__44_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__45`;
CREATE TABLE `OBJ_REF__45` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__45_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__45_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__46`;
CREATE TABLE `OBJ_REF__46` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__46_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__46_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__47`;
CREATE TABLE `OBJ_REF__47` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__47_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__47_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__48`;
CREATE TABLE `OBJ_REF__48` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__48_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__48_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__49`;
CREATE TABLE `OBJ_REF__49` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__49_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__49_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__50`;
CREATE TABLE `OBJ_REF__50` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__50_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__50_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__51`;
CREATE TABLE `OBJ_REF__51` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__51_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__51_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__52`;
CREATE TABLE `OBJ_REF__52` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__52_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__52_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__53`;
CREATE TABLE `OBJ_REF__53` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__53_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__53_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__54`;
CREATE TABLE `OBJ_REF__54` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__54_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__54_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__55`;
CREATE TABLE `OBJ_REF__55` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__55_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__55_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__56`;
CREATE TABLE `OBJ_REF__56` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__56_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__56_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__57`;
CREATE TABLE `OBJ_REF__57` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__57_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__57_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__58`;
CREATE TABLE `OBJ_REF__58` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__58_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__58_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__59`;
CREATE TABLE `OBJ_REF__59` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__59_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__59_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__60`;
CREATE TABLE `OBJ_REF__60` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__60_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__60_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__61`;
CREATE TABLE `OBJ_REF__61` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__61_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__61_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__62`;
CREATE TABLE `OBJ_REF__62` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__62_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__62_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__63`;
CREATE TABLE `OBJ_REF__63` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__63_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__63_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__64`;
CREATE TABLE `OBJ_REF__64` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__64_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__64_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__65`;
CREATE TABLE `OBJ_REF__65` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__65_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__65_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__66`;
CREATE TABLE `OBJ_REF__66` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__66_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__66_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__67`;
CREATE TABLE `OBJ_REF__67` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__67_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__67_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__68`;
CREATE TABLE `OBJ_REF__68` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__68_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__68_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__69`;
CREATE TABLE `OBJ_REF__69` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__69_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__69_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__70`;
CREATE TABLE `OBJ_REF__70` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__70_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__70_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__71`;
CREATE TABLE `OBJ_REF__71` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__71_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__71_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__72`;
CREATE TABLE `OBJ_REF__72` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__72_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__72_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__73`;
CREATE TABLE `OBJ_REF__73` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__73_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__73_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__74`;
CREATE TABLE `OBJ_REF__74` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__74_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__74_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__75`;
CREATE TABLE `OBJ_REF__75` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__75_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__75_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__76`;
CREATE TABLE `OBJ_REF__76` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__76_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__76_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__77`;
CREATE TABLE `OBJ_REF__77` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__77_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__77_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__78`;
CREATE TABLE `OBJ_REF__78` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__78_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__78_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__79`;
CREATE TABLE `OBJ_REF__79` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__79_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__79_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__80`;
CREATE TABLE `OBJ_REF__80` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__80_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__80_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__81`;
CREATE TABLE `OBJ_REF__81` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__81_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__81_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__82`;
CREATE TABLE `OBJ_REF__82` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__82_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__82_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__83`;
CREATE TABLE `OBJ_REF__83` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__83_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__83_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__84`;
CREATE TABLE `OBJ_REF__84` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__84_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__84_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__85`;
CREATE TABLE `OBJ_REF__85` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__85_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__85_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__86`;
CREATE TABLE `OBJ_REF__86` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__86_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__86_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__87`;
CREATE TABLE `OBJ_REF__87` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__87_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__87_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__88`;
CREATE TABLE `OBJ_REF__88` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__88_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__88_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__89`;
CREATE TABLE `OBJ_REF__89` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__89_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__89_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__90`;
CREATE TABLE `OBJ_REF__90` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__90_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__90_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__91`;
CREATE TABLE `OBJ_REF__91` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__91_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__91_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__92`;
CREATE TABLE `OBJ_REF__92` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__92_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__92_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__93`;
CREATE TABLE `OBJ_REF__93` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__93_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__93_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__94`;
CREATE TABLE `OBJ_REF__94` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__94_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__94_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__95`;
CREATE TABLE `OBJ_REF__95` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__95_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__95_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__96`;
CREATE TABLE `OBJ_REF__96` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__96_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__96_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__97`;
CREATE TABLE `OBJ_REF__97` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__97_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__97_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__98`;
CREATE TABLE `OBJ_REF__98` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__98_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__98_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__99`;
CREATE TABLE `OBJ_REF__99` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__99_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__99_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__100`;
CREATE TABLE `OBJ_REF__100` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__100_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__100_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__101`;
CREATE TABLE `OBJ_REF__101` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__101_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__101_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__102`;
CREATE TABLE `OBJ_REF__102` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__102_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__102_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__103`;
CREATE TABLE `OBJ_REF__103` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__103_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__103_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__104`;
CREATE TABLE `OBJ_REF__104` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__104_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__104_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__105`;
CREATE TABLE `OBJ_REF__105` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__105_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__105_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__106`;
CREATE TABLE `OBJ_REF__106` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__106_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__106_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__107`;
CREATE TABLE `OBJ_REF__107` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__107_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__107_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__108`;
CREATE TABLE `OBJ_REF__108` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__108_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__108_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__109`;
CREATE TABLE `OBJ_REF__109` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__109_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__109_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__110`;
CREATE TABLE `OBJ_REF__110` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__110_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__110_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__111`;
CREATE TABLE `OBJ_REF__111` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__111_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__111_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__112`;
CREATE TABLE `OBJ_REF__112` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__112_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__112_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__113`;
CREATE TABLE `OBJ_REF__113` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__113_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__113_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__114`;
CREATE TABLE `OBJ_REF__114` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__114_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__114_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__115`;
CREATE TABLE `OBJ_REF__115` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__115_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__115_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__116`;
CREATE TABLE `OBJ_REF__116` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__116_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__116_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__117`;
CREATE TABLE `OBJ_REF__117` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__117_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__117_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__118`;
CREATE TABLE `OBJ_REF__118` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__118_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__118_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__119`;
CREATE TABLE `OBJ_REF__119` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__119_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__119_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__120`;
CREATE TABLE `OBJ_REF__120` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__120_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__120_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__121`;
CREATE TABLE `OBJ_REF__121` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__121_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__121_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__122`;
CREATE TABLE `OBJ_REF__122` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__122_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__122_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__123`;
CREATE TABLE `OBJ_REF__123` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__123_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__123_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__124`;
CREATE TABLE `OBJ_REF__124` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__124_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__124_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__125`;
CREATE TABLE `OBJ_REF__125` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__125_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__125_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__126`;
CREATE TABLE `OBJ_REF__126` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__126_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__126_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__127`;
CREATE TABLE `OBJ_REF__127` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__127_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__127_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__MTP`;
CREATE TABLE `OBJ_REF__MTP` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__MTP_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__MTP_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
/* drop/create OBJ_REF */
DROP TABLE IF EXISTS `OBJ_REF__USER`;
CREATE TABLE `OBJ_REF__USER` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref__USER_index1` (`tenant_id`, `obj_def_id`(128), `obj_id`(64), `ref_def_id`(128)),
  INDEX `obj_ref__USER_index2` (`tenant_id`, `target_obj_def_id`(128), `target_obj_id`(64), `ref_def_id`(128))
)



;
