/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb`;
CREATE TABLE `obj_ref_rb` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__1`;
CREATE TABLE `obj_ref_rb__1` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__1_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__2`;
CREATE TABLE `obj_ref_rb__2` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__2_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__3`;
CREATE TABLE `obj_ref_rb__3` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__3_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__4`;
CREATE TABLE `obj_ref_rb__4` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__4_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__5`;
CREATE TABLE `obj_ref_rb__5` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__5_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__6`;
CREATE TABLE `obj_ref_rb__6` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__6_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__7`;
CREATE TABLE `obj_ref_rb__7` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__7_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__8`;
CREATE TABLE `obj_ref_rb__8` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__8_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__9`;
CREATE TABLE `obj_ref_rb__9` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__9_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__10`;
CREATE TABLE `obj_ref_rb__10` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__10_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__11`;
CREATE TABLE `obj_ref_rb__11` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__11_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__12`;
CREATE TABLE `obj_ref_rb__12` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__12_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__13`;
CREATE TABLE `obj_ref_rb__13` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__13_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__14`;
CREATE TABLE `obj_ref_rb__14` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__14_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__15`;
CREATE TABLE `obj_ref_rb__15` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__15_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__16`;
CREATE TABLE `obj_ref_rb__16` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__16_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__17`;
CREATE TABLE `obj_ref_rb__17` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__17_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__18`;
CREATE TABLE `obj_ref_rb__18` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__18_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__19`;
CREATE TABLE `obj_ref_rb__19` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__19_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__20`;
CREATE TABLE `obj_ref_rb__20` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__20_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__21`;
CREATE TABLE `obj_ref_rb__21` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__21_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__22`;
CREATE TABLE `obj_ref_rb__22` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__22_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__23`;
CREATE TABLE `obj_ref_rb__23` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__23_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__24`;
CREATE TABLE `obj_ref_rb__24` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__24_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__25`;
CREATE TABLE `obj_ref_rb__25` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__25_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__26`;
CREATE TABLE `obj_ref_rb__26` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__26_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__27`;
CREATE TABLE `obj_ref_rb__27` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__27_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__28`;
CREATE TABLE `obj_ref_rb__28` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__28_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__29`;
CREATE TABLE `obj_ref_rb__29` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__29_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__30`;
CREATE TABLE `obj_ref_rb__30` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__30_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__31`;
CREATE TABLE `obj_ref_rb__31` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__31_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__32`;
CREATE TABLE `obj_ref_rb__32` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__32_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__33`;
CREATE TABLE `obj_ref_rb__33` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__33_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__34`;
CREATE TABLE `obj_ref_rb__34` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__34_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__35`;
CREATE TABLE `obj_ref_rb__35` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__35_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__36`;
CREATE TABLE `obj_ref_rb__36` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__36_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__37`;
CREATE TABLE `obj_ref_rb__37` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__37_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__38`;
CREATE TABLE `obj_ref_rb__38` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__38_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__39`;
CREATE TABLE `obj_ref_rb__39` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__39_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__40`;
CREATE TABLE `obj_ref_rb__40` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__40_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__41`;
CREATE TABLE `obj_ref_rb__41` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__41_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__42`;
CREATE TABLE `obj_ref_rb__42` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__42_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__43`;
CREATE TABLE `obj_ref_rb__43` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__43_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__44`;
CREATE TABLE `obj_ref_rb__44` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__44_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__45`;
CREATE TABLE `obj_ref_rb__45` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__45_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__46`;
CREATE TABLE `obj_ref_rb__46` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__46_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__47`;
CREATE TABLE `obj_ref_rb__47` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__47_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__48`;
CREATE TABLE `obj_ref_rb__48` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__48_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__49`;
CREATE TABLE `obj_ref_rb__49` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__49_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__50`;
CREATE TABLE `obj_ref_rb__50` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__50_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__51`;
CREATE TABLE `obj_ref_rb__51` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__51_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__52`;
CREATE TABLE `obj_ref_rb__52` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__52_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__53`;
CREATE TABLE `obj_ref_rb__53` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__53_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__54`;
CREATE TABLE `obj_ref_rb__54` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__54_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__55`;
CREATE TABLE `obj_ref_rb__55` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__55_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__56`;
CREATE TABLE `obj_ref_rb__56` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__56_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__57`;
CREATE TABLE `obj_ref_rb__57` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__57_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__58`;
CREATE TABLE `obj_ref_rb__58` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__58_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__59`;
CREATE TABLE `obj_ref_rb__59` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__59_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__60`;
CREATE TABLE `obj_ref_rb__60` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__60_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__61`;
CREATE TABLE `obj_ref_rb__61` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__61_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__62`;
CREATE TABLE `obj_ref_rb__62` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__62_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__63`;
CREATE TABLE `obj_ref_rb__63` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__63_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__64`;
CREATE TABLE `obj_ref_rb__64` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__64_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__65`;
CREATE TABLE `obj_ref_rb__65` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__65_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__66`;
CREATE TABLE `obj_ref_rb__66` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__66_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__67`;
CREATE TABLE `obj_ref_rb__67` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__67_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__68`;
CREATE TABLE `obj_ref_rb__68` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__68_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__69`;
CREATE TABLE `obj_ref_rb__69` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__69_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__70`;
CREATE TABLE `obj_ref_rb__70` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__70_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__71`;
CREATE TABLE `obj_ref_rb__71` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__71_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__72`;
CREATE TABLE `obj_ref_rb__72` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__72_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__73`;
CREATE TABLE `obj_ref_rb__73` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__73_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__74`;
CREATE TABLE `obj_ref_rb__74` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__74_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__75`;
CREATE TABLE `obj_ref_rb__75` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__75_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__76`;
CREATE TABLE `obj_ref_rb__76` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__76_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__77`;
CREATE TABLE `obj_ref_rb__77` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__77_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__78`;
CREATE TABLE `obj_ref_rb__78` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__78_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__79`;
CREATE TABLE `obj_ref_rb__79` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__79_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__80`;
CREATE TABLE `obj_ref_rb__80` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__80_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__81`;
CREATE TABLE `obj_ref_rb__81` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__81_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__82`;
CREATE TABLE `obj_ref_rb__82` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__82_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__83`;
CREATE TABLE `obj_ref_rb__83` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__83_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__84`;
CREATE TABLE `obj_ref_rb__84` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__84_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__85`;
CREATE TABLE `obj_ref_rb__85` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__85_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__86`;
CREATE TABLE `obj_ref_rb__86` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__86_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__87`;
CREATE TABLE `obj_ref_rb__87` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__87_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__88`;
CREATE TABLE `obj_ref_rb__88` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__88_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__89`;
CREATE TABLE `obj_ref_rb__89` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__89_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__90`;
CREATE TABLE `obj_ref_rb__90` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__90_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__91`;
CREATE TABLE `obj_ref_rb__91` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__91_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__92`;
CREATE TABLE `obj_ref_rb__92` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__92_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__93`;
CREATE TABLE `obj_ref_rb__93` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__93_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__94`;
CREATE TABLE `obj_ref_rb__94` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__94_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__95`;
CREATE TABLE `obj_ref_rb__95` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__95_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__96`;
CREATE TABLE `obj_ref_rb__96` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__96_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__97`;
CREATE TABLE `obj_ref_rb__97` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__97_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__98`;
CREATE TABLE `obj_ref_rb__98` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__98_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__99`;
CREATE TABLE `obj_ref_rb__99` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__99_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__100`;
CREATE TABLE `obj_ref_rb__100` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__100_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__101`;
CREATE TABLE `obj_ref_rb__101` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__101_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__102`;
CREATE TABLE `obj_ref_rb__102` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__102_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__103`;
CREATE TABLE `obj_ref_rb__103` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__103_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__104`;
CREATE TABLE `obj_ref_rb__104` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__104_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__105`;
CREATE TABLE `obj_ref_rb__105` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__105_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__106`;
CREATE TABLE `obj_ref_rb__106` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__106_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__107`;
CREATE TABLE `obj_ref_rb__107` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__107_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__108`;
CREATE TABLE `obj_ref_rb__108` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__108_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__109`;
CREATE TABLE `obj_ref_rb__109` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__109_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__110`;
CREATE TABLE `obj_ref_rb__110` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__110_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__111`;
CREATE TABLE `obj_ref_rb__111` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__111_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__112`;
CREATE TABLE `obj_ref_rb__112` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__112_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__113`;
CREATE TABLE `obj_ref_rb__113` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__113_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__114`;
CREATE TABLE `obj_ref_rb__114` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__114_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__115`;
CREATE TABLE `obj_ref_rb__115` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__115_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__116`;
CREATE TABLE `obj_ref_rb__116` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__116_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__117`;
CREATE TABLE `obj_ref_rb__117` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__117_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__118`;
CREATE TABLE `obj_ref_rb__118` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__118_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__119`;
CREATE TABLE `obj_ref_rb__119` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__119_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__120`;
CREATE TABLE `obj_ref_rb__120` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__120_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__121`;
CREATE TABLE `obj_ref_rb__121` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__121_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__122`;
CREATE TABLE `obj_ref_rb__122` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__122_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__123`;
CREATE TABLE `obj_ref_rb__123` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__123_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__124`;
CREATE TABLE `obj_ref_rb__124` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__124_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__125`;
CREATE TABLE `obj_ref_rb__125` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__125_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__126`;
CREATE TABLE `obj_ref_rb__126` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__126_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__127`;
CREATE TABLE `obj_ref_rb__127` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__127_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__MTP`;
CREATE TABLE `obj_ref_rb__MTP` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__MTP_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `obj_ref_rb__USER`;
CREATE TABLE `obj_ref_rb__USER` (
  `r_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rb_id` BIGINT(16) NULL,
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ref_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `target_obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `target_obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`, `tenant_id`, `obj_def_id`),
  INDEX `obj_ref_rb__USER_index1` (`tenant_id`, `obj_def_id`(128), `rb_id`)
)



;
