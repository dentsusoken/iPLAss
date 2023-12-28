/* drop/create OBJ_REF_RB */
DROP TABLE IF EXISTS `OBJ_REF_RB`;
CREATE TABLE `OBJ_REF_RB` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__1`;
CREATE TABLE `OBJ_REF_RB__1` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__2`;
CREATE TABLE `OBJ_REF_RB__2` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__3`;
CREATE TABLE `OBJ_REF_RB__3` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__4`;
CREATE TABLE `OBJ_REF_RB__4` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__5`;
CREATE TABLE `OBJ_REF_RB__5` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__6`;
CREATE TABLE `OBJ_REF_RB__6` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__7`;
CREATE TABLE `OBJ_REF_RB__7` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__8`;
CREATE TABLE `OBJ_REF_RB__8` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__9`;
CREATE TABLE `OBJ_REF_RB__9` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__10`;
CREATE TABLE `OBJ_REF_RB__10` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__11`;
CREATE TABLE `OBJ_REF_RB__11` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__12`;
CREATE TABLE `OBJ_REF_RB__12` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__13`;
CREATE TABLE `OBJ_REF_RB__13` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__14`;
CREATE TABLE `OBJ_REF_RB__14` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__15`;
CREATE TABLE `OBJ_REF_RB__15` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__16`;
CREATE TABLE `OBJ_REF_RB__16` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__17`;
CREATE TABLE `OBJ_REF_RB__17` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__18`;
CREATE TABLE `OBJ_REF_RB__18` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__19`;
CREATE TABLE `OBJ_REF_RB__19` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__20`;
CREATE TABLE `OBJ_REF_RB__20` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__21`;
CREATE TABLE `OBJ_REF_RB__21` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__22`;
CREATE TABLE `OBJ_REF_RB__22` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__23`;
CREATE TABLE `OBJ_REF_RB__23` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__24`;
CREATE TABLE `OBJ_REF_RB__24` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__25`;
CREATE TABLE `OBJ_REF_RB__25` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__26`;
CREATE TABLE `OBJ_REF_RB__26` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__27`;
CREATE TABLE `OBJ_REF_RB__27` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__28`;
CREATE TABLE `OBJ_REF_RB__28` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__29`;
CREATE TABLE `OBJ_REF_RB__29` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__30`;
CREATE TABLE `OBJ_REF_RB__30` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__31`;
CREATE TABLE `OBJ_REF_RB__31` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__32`;
CREATE TABLE `OBJ_REF_RB__32` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__33`;
CREATE TABLE `OBJ_REF_RB__33` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__34`;
CREATE TABLE `OBJ_REF_RB__34` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__35`;
CREATE TABLE `OBJ_REF_RB__35` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__36`;
CREATE TABLE `OBJ_REF_RB__36` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__37`;
CREATE TABLE `OBJ_REF_RB__37` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__38`;
CREATE TABLE `OBJ_REF_RB__38` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__39`;
CREATE TABLE `OBJ_REF_RB__39` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__40`;
CREATE TABLE `OBJ_REF_RB__40` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__41`;
CREATE TABLE `OBJ_REF_RB__41` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__42`;
CREATE TABLE `OBJ_REF_RB__42` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__43`;
CREATE TABLE `OBJ_REF_RB__43` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__44`;
CREATE TABLE `OBJ_REF_RB__44` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__45`;
CREATE TABLE `OBJ_REF_RB__45` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__46`;
CREATE TABLE `OBJ_REF_RB__46` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__47`;
CREATE TABLE `OBJ_REF_RB__47` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__48`;
CREATE TABLE `OBJ_REF_RB__48` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__49`;
CREATE TABLE `OBJ_REF_RB__49` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__50`;
CREATE TABLE `OBJ_REF_RB__50` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__51`;
CREATE TABLE `OBJ_REF_RB__51` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__52`;
CREATE TABLE `OBJ_REF_RB__52` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__53`;
CREATE TABLE `OBJ_REF_RB__53` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__54`;
CREATE TABLE `OBJ_REF_RB__54` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__55`;
CREATE TABLE `OBJ_REF_RB__55` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__56`;
CREATE TABLE `OBJ_REF_RB__56` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__57`;
CREATE TABLE `OBJ_REF_RB__57` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__58`;
CREATE TABLE `OBJ_REF_RB__58` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__59`;
CREATE TABLE `OBJ_REF_RB__59` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__60`;
CREATE TABLE `OBJ_REF_RB__60` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__61`;
CREATE TABLE `OBJ_REF_RB__61` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__62`;
CREATE TABLE `OBJ_REF_RB__62` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__63`;
CREATE TABLE `OBJ_REF_RB__63` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__64`;
CREATE TABLE `OBJ_REF_RB__64` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__65`;
CREATE TABLE `OBJ_REF_RB__65` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__66`;
CREATE TABLE `OBJ_REF_RB__66` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__67`;
CREATE TABLE `OBJ_REF_RB__67` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__68`;
CREATE TABLE `OBJ_REF_RB__68` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__69`;
CREATE TABLE `OBJ_REF_RB__69` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__70`;
CREATE TABLE `OBJ_REF_RB__70` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__71`;
CREATE TABLE `OBJ_REF_RB__71` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__72`;
CREATE TABLE `OBJ_REF_RB__72` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__73`;
CREATE TABLE `OBJ_REF_RB__73` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__74`;
CREATE TABLE `OBJ_REF_RB__74` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__75`;
CREATE TABLE `OBJ_REF_RB__75` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__76`;
CREATE TABLE `OBJ_REF_RB__76` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__77`;
CREATE TABLE `OBJ_REF_RB__77` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__78`;
CREATE TABLE `OBJ_REF_RB__78` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__79`;
CREATE TABLE `OBJ_REF_RB__79` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__80`;
CREATE TABLE `OBJ_REF_RB__80` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__81`;
CREATE TABLE `OBJ_REF_RB__81` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__82`;
CREATE TABLE `OBJ_REF_RB__82` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__83`;
CREATE TABLE `OBJ_REF_RB__83` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__84`;
CREATE TABLE `OBJ_REF_RB__84` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__85`;
CREATE TABLE `OBJ_REF_RB__85` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__86`;
CREATE TABLE `OBJ_REF_RB__86` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__87`;
CREATE TABLE `OBJ_REF_RB__87` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__88`;
CREATE TABLE `OBJ_REF_RB__88` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__89`;
CREATE TABLE `OBJ_REF_RB__89` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__90`;
CREATE TABLE `OBJ_REF_RB__90` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__91`;
CREATE TABLE `OBJ_REF_RB__91` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__92`;
CREATE TABLE `OBJ_REF_RB__92` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__93`;
CREATE TABLE `OBJ_REF_RB__93` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__94`;
CREATE TABLE `OBJ_REF_RB__94` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__95`;
CREATE TABLE `OBJ_REF_RB__95` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__96`;
CREATE TABLE `OBJ_REF_RB__96` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__97`;
CREATE TABLE `OBJ_REF_RB__97` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__98`;
CREATE TABLE `OBJ_REF_RB__98` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__99`;
CREATE TABLE `OBJ_REF_RB__99` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__100`;
CREATE TABLE `OBJ_REF_RB__100` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__101`;
CREATE TABLE `OBJ_REF_RB__101` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__102`;
CREATE TABLE `OBJ_REF_RB__102` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__103`;
CREATE TABLE `OBJ_REF_RB__103` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__104`;
CREATE TABLE `OBJ_REF_RB__104` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__105`;
CREATE TABLE `OBJ_REF_RB__105` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__106`;
CREATE TABLE `OBJ_REF_RB__106` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__107`;
CREATE TABLE `OBJ_REF_RB__107` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__108`;
CREATE TABLE `OBJ_REF_RB__108` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__109`;
CREATE TABLE `OBJ_REF_RB__109` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__110`;
CREATE TABLE `OBJ_REF_RB__110` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__111`;
CREATE TABLE `OBJ_REF_RB__111` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__112`;
CREATE TABLE `OBJ_REF_RB__112` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__113`;
CREATE TABLE `OBJ_REF_RB__113` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__114`;
CREATE TABLE `OBJ_REF_RB__114` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__115`;
CREATE TABLE `OBJ_REF_RB__115` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__116`;
CREATE TABLE `OBJ_REF_RB__116` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__117`;
CREATE TABLE `OBJ_REF_RB__117` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__118`;
CREATE TABLE `OBJ_REF_RB__118` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__119`;
CREATE TABLE `OBJ_REF_RB__119` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__120`;
CREATE TABLE `OBJ_REF_RB__120` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__121`;
CREATE TABLE `OBJ_REF_RB__121` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__122`;
CREATE TABLE `OBJ_REF_RB__122` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__123`;
CREATE TABLE `OBJ_REF_RB__123` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__124`;
CREATE TABLE `OBJ_REF_RB__124` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__125`;
CREATE TABLE `OBJ_REF_RB__125` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__126`;
CREATE TABLE `OBJ_REF_RB__126` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__127`;
CREATE TABLE `OBJ_REF_RB__127` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__MTP`;
CREATE TABLE `OBJ_REF_RB__MTP` (
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
DROP TABLE IF EXISTS `OBJ_REF_RB__USER`;
CREATE TABLE `OBJ_REF_RB__USER` (
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
