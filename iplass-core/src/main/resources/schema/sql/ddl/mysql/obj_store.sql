/* drop/create OBJ_STORE */
DROP TABLE IF EXISTS `obj_store`;
CREATE TABLE `obj_store` (
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `pg_no` INT(2) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `obj_def_ver` BIGINT(10) NULL,
  `status` CHAR(1) NULL,
  `obj_name` VARCHAR(256) NULL,
  `obj_desc` TEXT NULL,
  `cre_date` DATETIME(3) NULL,
  `up_date` DATETIME(3) NULL,
  `s_date` DATETIME(3) NULL,
  `e_date` DATETIME(3) NULL,
  `lock_user` VARCHAR(64) NULL,
  `cre_user` VARCHAR(64) NULL,
  `up_user` VARCHAR(64) NULL
  ,`USTR_1` TEXT
  ,`USTR_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`UNUM_1` DECIMAL(22, 0)
  ,`UNUM_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`UTS_1` DATETIME(3)
  ,`UTS_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`UDBL_1` DOUBLE
  ,`UDBL_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`USTR_2` TEXT
  ,`USTR_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`UNUM_2` DECIMAL(22, 0)
  ,`UNUM_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`UTS_2` DATETIME(3)
  ,`UTS_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`UDBL_2` DOUBLE
  ,`UDBL_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ISTR_1` TEXT
  ,`ISTR_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`INUM_1` DECIMAL(22, 0)
  ,`INUM_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ITS_1` DATETIME(3)
  ,`ITS_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`IDBL_1` DOUBLE
  ,`IDBL_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ISTR_2` TEXT
  ,`ISTR_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`INUM_2` DECIMAL(22, 0)
  ,`INUM_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ITS_2` DATETIME(3)
  ,`ITS_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`IDBL_2` DOUBLE
  ,`IDBL_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ISTR_3` TEXT
  ,`ISTR_3_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ISTR_4` TEXT
  ,`ISTR_4_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`INUM_3` DECIMAL(22, 0)
  ,`INUM_3_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ITS_3` DATETIME(3)
  ,`ITS_3_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`IDBL_3` DOUBLE
  ,`IDBL_3_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ISTR_5` TEXT
  ,`ISTR_5_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`INUM_4` DECIMAL(22, 0)
  ,`INUM_4_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ITS_4` DATETIME(3)
  ,`ITS_4_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`IDBL_4` DOUBLE
  ,`IDBL_4_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`STR_1` TEXT
  ,`STR_2` TEXT
  ,`NUM_1` DECIMAL(22, 0)
  ,`TS_1` DATETIME(3)
  ,`STR_3` TEXT
  ,`STR_4` TEXT
  ,`NUM_2` DECIMAL(22, 0)
  ,`TS_2` DATETIME(3)
  ,`DBL_1` DOUBLE
  ,`STR_5` TEXT
  ,`STR_6` TEXT
  ,`NUM_3` DECIMAL(22, 0)
  ,`TS_3` DATETIME(3)
  ,`STR_7` TEXT
  ,`STR_8` TEXT
  ,`NUM_4` DECIMAL(22, 0)
  ,`TS_4` DATETIME(3)
  ,`DBL_2` DOUBLE
  ,`STR_9` TEXT
  ,`STR_10` TEXT
  ,`NUM_5` DECIMAL(22, 0)
  ,`TS_5` DATETIME(3)
  ,`STR_11` TEXT
  ,`STR_12` TEXT
  ,`NUM_6` DECIMAL(22, 0)
  ,`TS_6` DATETIME(3)
  ,`DBL_3` DOUBLE
  ,`STR_13` TEXT
  ,`STR_14` TEXT
  ,`NUM_7` DECIMAL(22, 0)
  ,`TS_7` DATETIME(3)
  ,`STR_15` TEXT
  ,`STR_16` TEXT
  ,`NUM_8` DECIMAL(22, 0)
  ,`TS_8` DATETIME(3)
  ,`DBL_4` DOUBLE
  ,`STR_17` TEXT
  ,`STR_18` TEXT
  ,`NUM_9` DECIMAL(22, 0)
  ,`TS_9` DATETIME(3)
  ,`STR_19` TEXT
  ,`STR_20` TEXT
  ,`NUM_10` DECIMAL(22, 0)
  ,`TS_10` DATETIME(3)
  ,`DBL_5` DOUBLE
  ,`STR_21` TEXT
  ,`STR_22` TEXT
  ,`NUM_11` DECIMAL(22, 0)
  ,`TS_11` DATETIME(3)
  ,`STR_23` TEXT
  ,`STR_24` TEXT
  ,`NUM_12` DECIMAL(22, 0)
  ,`TS_12` DATETIME(3)
  ,`DBL_6` DOUBLE
  ,`STR_25` TEXT
  ,`STR_26` TEXT
  ,`NUM_13` DECIMAL(22, 0)
  ,`TS_13` DATETIME(3)
  ,`STR_27` TEXT
  ,`STR_28` TEXT
  ,`NUM_14` DECIMAL(22, 0)
  ,`TS_14` DATETIME(3)
  ,`DBL_7` DOUBLE
  ,`STR_29` TEXT
  ,`STR_30` TEXT
  ,`NUM_15` DECIMAL(22, 0)
  ,`TS_15` DATETIME(3)
  ,`STR_31` TEXT
  ,`STR_32` TEXT
  ,`NUM_16` DECIMAL(22, 0)
  ,`TS_16` DATETIME(3)
  ,`DBL_8` DOUBLE
  ,`STR_33` TEXT
  ,`STR_34` TEXT
  ,`NUM_17` DECIMAL(22, 0)
  ,`TS_17` DATETIME(3)
  ,`STR_35` TEXT
  ,`STR_36` TEXT
  ,`NUM_18` DECIMAL(22, 0)
  ,`TS_18` DATETIME(3)
  ,`DBL_9` DOUBLE
  ,`STR_37` TEXT
  ,`STR_38` TEXT
  ,`NUM_19` DECIMAL(22, 0)
  ,`TS_19` DATETIME(3)
  ,`STR_39` TEXT
  ,`STR_40` TEXT
  ,`NUM_20` DECIMAL(22, 0)
  ,`TS_20` DATETIME(3)
  ,`DBL_10` DOUBLE
  ,`STR_41` TEXT
  ,`STR_42` TEXT
  ,`NUM_21` DECIMAL(22, 0)
  ,`TS_21` DATETIME(3)
  ,`STR_43` TEXT
  ,`STR_44` TEXT
  ,`NUM_22` DECIMAL(22, 0)
  ,`TS_22` DATETIME(3)
  ,`DBL_11` DOUBLE
  ,`STR_45` TEXT
  ,`STR_46` TEXT
  ,`NUM_23` DECIMAL(22, 0)
  ,`TS_23` DATETIME(3)
  ,`STR_47` TEXT
  ,`STR_48` TEXT
  ,`NUM_24` DECIMAL(22, 0)
  ,`TS_24` DATETIME(3)
  ,`DBL_12` DOUBLE
  ,`STR_49` TEXT
  ,`STR_50` TEXT
  ,`NUM_25` DECIMAL(22, 0)
  ,`TS_25` DATETIME(3)
  ,`STR_51` TEXT
  ,`STR_52` TEXT
  ,`NUM_26` DECIMAL(22, 0)
  ,`TS_26` DATETIME(3)
  ,`DBL_13` DOUBLE
  ,`STR_53` TEXT
  ,`STR_54` TEXT
  ,`NUM_27` DECIMAL(22, 0)
  ,`TS_27` DATETIME(3)
  ,`STR_55` TEXT
  ,`STR_56` TEXT
  ,`NUM_28` DECIMAL(22, 0)
  ,`TS_28` DATETIME(3)
  ,`DBL_14` DOUBLE
  ,`STR_57` TEXT
  ,`STR_58` TEXT
  ,`NUM_29` DECIMAL(22, 0)
  ,`TS_29` DATETIME(3)
  ,`STR_59` TEXT
  ,`STR_60` TEXT
  ,`NUM_30` DECIMAL(22, 0)
  ,`TS_30` DATETIME(3)
  ,`DBL_15` DOUBLE
  ,`STR_61` TEXT
  ,`STR_62` TEXT
  ,`NUM_31` DECIMAL(22, 0)
  ,`TS_31` DATETIME(3)
  ,`STR_63` TEXT
  ,`STR_64` TEXT
  ,`NUM_32` DECIMAL(22, 0)
  ,`TS_32` DATETIME(3)
  ,`DBL_16` DOUBLE
  
  ,PRIMARY KEY (`tenant_id`, `obj_def_id`, `obj_id`, `obj_ver`, `pg_no`)
  ,UNIQUE INDEX `obj_store_USTR_unique_1` (`USTR_1_TD`(139), `USTR_1`(255), `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store_UNUM_unique_1` (`UNUM_1_TD`(139), `UNUM_1`, `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store_UTS_unique_1` (`UTS_1_TD`(139), `UTS_1`, `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store_UDBL_unique_1` (`UDBL_1_TD`(139), `UDBL_1`, `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store_USTR_unique_2` (`USTR_2_TD`(139), `USTR_2`(255), `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store_UNUM_unique_2` (`UNUM_2_TD`(139), `UNUM_2`, `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store_UTS_unique_2` (`UTS_2_TD`(139), `UTS_2`, `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store_UDBL_unique_2` (`UDBL_2_TD`(139), `UDBL_2`, `tenant_id`, `obj_def_id`(128))
  ,INDEX `obj_store_ISTR_index_1` (`ISTR_1`(255), `ISTR_1_TD`(139))
  ,INDEX `obj_store_INUM_index_1` (`INUM_1`, `INUM_1_TD`(139))
  ,INDEX `obj_store_ITS_index_1` (`ITS_1`, `ITS_1_TD`(139))
  ,INDEX `obj_store_IDBL_index_1` (`IDBL_1`, `IDBL_1_TD`(139))
  ,INDEX `obj_store_ISTR_index_2` (`ISTR_2`(255), `ISTR_2_TD`(139))
  ,INDEX `obj_store_INUM_index_2` (`INUM_2`, `INUM_2_TD`(139))
  ,INDEX `obj_store_ITS_index_2` (`ITS_2`, `ITS_2_TD`(139))
  ,INDEX `obj_store_IDBL_index_2` (`IDBL_2`, `IDBL_2_TD`(139))
  ,INDEX `obj_store_ISTR_index_3` (`ISTR_3`(255), `ISTR_3_TD`(139))
  ,INDEX `obj_store_ISTR_index_4` (`ISTR_4`(255), `ISTR_4_TD`(139))
  ,INDEX `obj_store_INUM_index_3` (`INUM_3`, `INUM_3_TD`(139))
  ,INDEX `obj_store_ITS_index_3` (`ITS_3`, `ITS_3_TD`(139))
  ,INDEX `obj_store_IDBL_index_3` (`IDBL_3`, `IDBL_3_TD`(139))
  ,INDEX `obj_store_ISTR_index_5` (`ISTR_5`(255), `ISTR_5_TD`(139))
  ,INDEX `obj_store_INUM_index_4` (`INUM_4`, `INUM_4_TD`(139))
  ,INDEX `obj_store_ITS_index_4` (`ITS_4`, `ITS_4_TD`(139))
  ,INDEX `obj_store_IDBL_index_4` (`IDBL_4`, `IDBL_4_TD`(139))
  ,INDEX `obj_store_cre_user_index` (`cre_user`(64), `obj_def_id`(128), `tenant_id`)
)
ENGINE = INNODB ROW_FORMAT=DYNAMIC

PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_store_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_store_0_0,
            SUBPARTITION obj_store_0_1,
            SUBPARTITION obj_store_0_2,
            SUBPARTITION obj_store_0_3,
            SUBPARTITION obj_store_0_4,
            SUBPARTITION obj_store_0_5,
            SUBPARTITION obj_store_0_6,
            SUBPARTITION obj_store_0_7
        )
    );


/* drop/create OBJ_STORE */
DROP TABLE IF EXISTS `obj_store__MTP`;
CREATE TABLE `obj_store__MTP` (
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `pg_no` INT(2) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `obj_def_ver` BIGINT(10) NULL,
  `status` CHAR(1) NULL,
  `obj_name` VARCHAR(256) NULL,
  `obj_desc` TEXT NULL,
  `cre_date` DATETIME(3) NULL,
  `up_date` DATETIME(3) NULL,
  `s_date` DATETIME(3) NULL,
  `e_date` DATETIME(3) NULL,
  `lock_user` VARCHAR(64) NULL,
  `cre_user` VARCHAR(64) NULL,
  `up_user` VARCHAR(64) NULL
  ,`USTR_1` TEXT
  ,`USTR_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`UNUM_1` DECIMAL(22, 0)
  ,`UNUM_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`UTS_1` DATETIME(3)
  ,`UTS_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`UDBL_1` DOUBLE
  ,`UDBL_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`USTR_2` TEXT
  ,`USTR_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`UNUM_2` DECIMAL(22, 0)
  ,`UNUM_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`UTS_2` DATETIME(3)
  ,`UTS_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`UDBL_2` DOUBLE
  ,`UDBL_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ISTR_1` TEXT
  ,`ISTR_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`INUM_1` DECIMAL(22, 0)
  ,`INUM_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ITS_1` DATETIME(3)
  ,`ITS_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`IDBL_1` DOUBLE
  ,`IDBL_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ISTR_2` TEXT
  ,`ISTR_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`INUM_2` DECIMAL(22, 0)
  ,`INUM_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ITS_2` DATETIME(3)
  ,`ITS_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`IDBL_2` DOUBLE
  ,`IDBL_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ISTR_3` TEXT
  ,`ISTR_3_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ISTR_4` TEXT
  ,`ISTR_4_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`INUM_3` DECIMAL(22, 0)
  ,`INUM_3_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ITS_3` DATETIME(3)
  ,`ITS_3_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`IDBL_3` DOUBLE
  ,`IDBL_3_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ISTR_5` TEXT
  ,`ISTR_5_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`INUM_4` DECIMAL(22, 0)
  ,`INUM_4_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ITS_4` DATETIME(3)
  ,`ITS_4_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`IDBL_4` DOUBLE
  ,`IDBL_4_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`STR_1` TEXT
  ,`STR_2` TEXT
  ,`NUM_1` DECIMAL(22, 0)
  ,`TS_1` DATETIME(3)
  ,`STR_3` TEXT
  ,`STR_4` TEXT
  ,`NUM_2` DECIMAL(22, 0)
  ,`TS_2` DATETIME(3)
  ,`DBL_1` DOUBLE
  ,`STR_5` TEXT
  ,`STR_6` TEXT
  ,`NUM_3` DECIMAL(22, 0)
  ,`TS_3` DATETIME(3)
  ,`STR_7` TEXT
  ,`STR_8` TEXT
  ,`NUM_4` DECIMAL(22, 0)
  ,`TS_4` DATETIME(3)
  ,`DBL_2` DOUBLE
  ,`STR_9` TEXT
  ,`STR_10` TEXT
  ,`NUM_5` DECIMAL(22, 0)
  ,`TS_5` DATETIME(3)
  ,`STR_11` TEXT
  ,`STR_12` TEXT
  ,`NUM_6` DECIMAL(22, 0)
  ,`TS_6` DATETIME(3)
  ,`DBL_3` DOUBLE
  ,`STR_13` TEXT
  ,`STR_14` TEXT
  ,`NUM_7` DECIMAL(22, 0)
  ,`TS_7` DATETIME(3)
  ,`STR_15` TEXT
  ,`STR_16` TEXT
  ,`NUM_8` DECIMAL(22, 0)
  ,`TS_8` DATETIME(3)
  ,`DBL_4` DOUBLE
  ,`STR_17` TEXT
  ,`STR_18` TEXT
  ,`NUM_9` DECIMAL(22, 0)
  ,`TS_9` DATETIME(3)
  ,`STR_19` TEXT
  ,`STR_20` TEXT
  ,`NUM_10` DECIMAL(22, 0)
  ,`TS_10` DATETIME(3)
  ,`DBL_5` DOUBLE
  ,`STR_21` TEXT
  ,`STR_22` TEXT
  ,`NUM_11` DECIMAL(22, 0)
  ,`TS_11` DATETIME(3)
  ,`STR_23` TEXT
  ,`STR_24` TEXT
  ,`NUM_12` DECIMAL(22, 0)
  ,`TS_12` DATETIME(3)
  ,`DBL_6` DOUBLE
  ,`STR_25` TEXT
  ,`STR_26` TEXT
  ,`NUM_13` DECIMAL(22, 0)
  ,`TS_13` DATETIME(3)
  ,`STR_27` TEXT
  ,`STR_28` TEXT
  ,`NUM_14` DECIMAL(22, 0)
  ,`TS_14` DATETIME(3)
  ,`DBL_7` DOUBLE
  ,`STR_29` TEXT
  ,`STR_30` TEXT
  ,`NUM_15` DECIMAL(22, 0)
  ,`TS_15` DATETIME(3)
  ,`STR_31` TEXT
  ,`STR_32` TEXT
  ,`NUM_16` DECIMAL(22, 0)
  ,`TS_16` DATETIME(3)
  ,`DBL_8` DOUBLE
  ,`STR_33` TEXT
  ,`STR_34` TEXT
  ,`NUM_17` DECIMAL(22, 0)
  ,`TS_17` DATETIME(3)
  ,`STR_35` TEXT
  ,`STR_36` TEXT
  ,`NUM_18` DECIMAL(22, 0)
  ,`TS_18` DATETIME(3)
  ,`DBL_9` DOUBLE
  ,`STR_37` TEXT
  ,`STR_38` TEXT
  ,`NUM_19` DECIMAL(22, 0)
  ,`TS_19` DATETIME(3)
  ,`STR_39` TEXT
  ,`STR_40` TEXT
  ,`NUM_20` DECIMAL(22, 0)
  ,`TS_20` DATETIME(3)
  ,`DBL_10` DOUBLE
  ,`STR_41` TEXT
  ,`STR_42` TEXT
  ,`NUM_21` DECIMAL(22, 0)
  ,`TS_21` DATETIME(3)
  ,`STR_43` TEXT
  ,`STR_44` TEXT
  ,`NUM_22` DECIMAL(22, 0)
  ,`TS_22` DATETIME(3)
  ,`DBL_11` DOUBLE
  ,`STR_45` TEXT
  ,`STR_46` TEXT
  ,`NUM_23` DECIMAL(22, 0)
  ,`TS_23` DATETIME(3)
  ,`STR_47` TEXT
  ,`STR_48` TEXT
  ,`NUM_24` DECIMAL(22, 0)
  ,`TS_24` DATETIME(3)
  ,`DBL_12` DOUBLE
  ,`STR_49` TEXT
  ,`STR_50` TEXT
  ,`NUM_25` DECIMAL(22, 0)
  ,`TS_25` DATETIME(3)
  ,`STR_51` TEXT
  ,`STR_52` TEXT
  ,`NUM_26` DECIMAL(22, 0)
  ,`TS_26` DATETIME(3)
  ,`DBL_13` DOUBLE
  ,`STR_53` TEXT
  ,`STR_54` TEXT
  ,`NUM_27` DECIMAL(22, 0)
  ,`TS_27` DATETIME(3)
  ,`STR_55` TEXT
  ,`STR_56` TEXT
  ,`NUM_28` DECIMAL(22, 0)
  ,`TS_28` DATETIME(3)
  ,`DBL_14` DOUBLE
  ,`STR_57` TEXT
  ,`STR_58` TEXT
  ,`NUM_29` DECIMAL(22, 0)
  ,`TS_29` DATETIME(3)
  ,`STR_59` TEXT
  ,`STR_60` TEXT
  ,`NUM_30` DECIMAL(22, 0)
  ,`TS_30` DATETIME(3)
  ,`DBL_15` DOUBLE
  ,`STR_61` TEXT
  ,`STR_62` TEXT
  ,`NUM_31` DECIMAL(22, 0)
  ,`TS_31` DATETIME(3)
  ,`STR_63` TEXT
  ,`STR_64` TEXT
  ,`NUM_32` DECIMAL(22, 0)
  ,`TS_32` DATETIME(3)
  ,`DBL_16` DOUBLE
  
  ,PRIMARY KEY (`tenant_id`, `obj_def_id`, `obj_id`, `obj_ver`, `pg_no`)
  ,UNIQUE INDEX `obj_store__MTP_USTR_unique_1` (`USTR_1_TD`(139), `USTR_1`(255), `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store__MTP_UNUM_unique_1` (`UNUM_1_TD`(139), `UNUM_1`, `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store__MTP_UTS_unique_1` (`UTS_1_TD`(139), `UTS_1`, `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store__MTP_UDBL_unique_1` (`UDBL_1_TD`(139), `UDBL_1`, `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store__MTP_USTR_unique_2` (`USTR_2_TD`(139), `USTR_2`(255), `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store__MTP_UNUM_unique_2` (`UNUM_2_TD`(139), `UNUM_2`, `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store__MTP_UTS_unique_2` (`UTS_2_TD`(139), `UTS_2`, `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store__MTP_UDBL_unique_2` (`UDBL_2_TD`(139), `UDBL_2`, `tenant_id`, `obj_def_id`(128))
  ,INDEX `obj_store__MTP_ISTR_index_1` (`ISTR_1`(255), `ISTR_1_TD`(139))
  ,INDEX `obj_store__MTP_INUM_index_1` (`INUM_1`, `INUM_1_TD`(139))
  ,INDEX `obj_store__MTP_ITS_index_1` (`ITS_1`, `ITS_1_TD`(139))
  ,INDEX `obj_store__MTP_IDBL_index_1` (`IDBL_1`, `IDBL_1_TD`(139))
  ,INDEX `obj_store__MTP_ISTR_index_2` (`ISTR_2`(255), `ISTR_2_TD`(139))
  ,INDEX `obj_store__MTP_INUM_index_2` (`INUM_2`, `INUM_2_TD`(139))
  ,INDEX `obj_store__MTP_ITS_index_2` (`ITS_2`, `ITS_2_TD`(139))
  ,INDEX `obj_store__MTP_IDBL_index_2` (`IDBL_2`, `IDBL_2_TD`(139))
  ,INDEX `obj_store__MTP_ISTR_index_3` (`ISTR_3`(255), `ISTR_3_TD`(139))
  ,INDEX `obj_store__MTP_ISTR_index_4` (`ISTR_4`(255), `ISTR_4_TD`(139))
  ,INDEX `obj_store__MTP_INUM_index_3` (`INUM_3`, `INUM_3_TD`(139))
  ,INDEX `obj_store__MTP_ITS_index_3` (`ITS_3`, `ITS_3_TD`(139))
  ,INDEX `obj_store__MTP_IDBL_index_3` (`IDBL_3`, `IDBL_3_TD`(139))
  ,INDEX `obj_store__MTP_ISTR_index_5` (`ISTR_5`(255), `ISTR_5_TD`(139))
  ,INDEX `obj_store__MTP_INUM_index_4` (`INUM_4`, `INUM_4_TD`(139))
  ,INDEX `obj_store__MTP_ITS_index_4` (`ITS_4`, `ITS_4_TD`(139))
  ,INDEX `obj_store__MTP_IDBL_index_4` (`IDBL_4`, `IDBL_4_TD`(139))
  ,INDEX `obj_store__MTP_cre_user_index` (`cre_user`(64), `obj_def_id`(128), `tenant_id`)
)
ENGINE = INNODB ROW_FORMAT=DYNAMIC

PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_store__MTP_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_store__MTP_0_0,
            SUBPARTITION obj_store__MTP_0_1,
            SUBPARTITION obj_store__MTP_0_2,
            SUBPARTITION obj_store__MTP_0_3,
            SUBPARTITION obj_store__MTP_0_4,
            SUBPARTITION obj_store__MTP_0_5,
            SUBPARTITION obj_store__MTP_0_6,
            SUBPARTITION obj_store__MTP_0_7
        )
    );


/* drop/create OBJ_STORE */
DROP TABLE IF EXISTS `obj_store__USER`;
CREATE TABLE `obj_store__USER` (
  `tenant_id` INT(7) NOT NULL,
  `obj_def_id` VARCHAR(128) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `pg_no` INT(2) NOT NULL,
  `obj_id` VARCHAR(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `obj_ver` BIGINT(10) NOT NULL DEFAULT 0,
  `obj_def_ver` BIGINT(10) NULL,
  `status` CHAR(1) NULL,
  `obj_name` VARCHAR(256) NULL,
  `obj_desc` TEXT NULL,
  `cre_date` DATETIME(3) NULL,
  `up_date` DATETIME(3) NULL,
  `s_date` DATETIME(3) NULL,
  `e_date` DATETIME(3) NULL,
  `lock_user` VARCHAR(64) NULL,
  `cre_user` VARCHAR(64) NULL,
  `up_user` VARCHAR(64) NULL
  ,`USTR_1` TEXT
  ,`USTR_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`UNUM_1` DECIMAL(22, 0)
  ,`UNUM_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`UTS_1` DATETIME(3)
  ,`UTS_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`UDBL_1` DOUBLE
  ,`UDBL_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`USTR_2` TEXT
  ,`USTR_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`UNUM_2` DECIMAL(22, 0)
  ,`UNUM_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`UTS_2` DATETIME(3)
  ,`UTS_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`UDBL_2` DOUBLE
  ,`UDBL_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ISTR_1` TEXT
  ,`ISTR_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`INUM_1` DECIMAL(22, 0)
  ,`INUM_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ITS_1` DATETIME(3)
  ,`ITS_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`IDBL_1` DOUBLE
  ,`IDBL_1_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ISTR_2` TEXT
  ,`ISTR_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`INUM_2` DECIMAL(22, 0)
  ,`INUM_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ITS_2` DATETIME(3)
  ,`ITS_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`IDBL_2` DOUBLE
  ,`IDBL_2_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ISTR_3` TEXT
  ,`ISTR_3_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ISTR_4` TEXT
  ,`ISTR_4_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`INUM_3` DECIMAL(22, 0)
  ,`INUM_3_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ITS_3` DATETIME(3)
  ,`ITS_3_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`IDBL_3` DOUBLE
  ,`IDBL_3_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ISTR_5` TEXT
  ,`ISTR_5_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`INUM_4` DECIMAL(22, 0)
  ,`INUM_4_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`ITS_4` DATETIME(3)
  ,`ITS_4_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`IDBL_4` DOUBLE
  ,`IDBL_4_TD` VARCHAR(139) CHARACTER SET latin1 COLLATE latin1_bin
  ,`STR_1` TEXT
  ,`STR_2` TEXT
  ,`NUM_1` DECIMAL(22, 0)
  ,`TS_1` DATETIME(3)
  ,`STR_3` TEXT
  ,`STR_4` TEXT
  ,`NUM_2` DECIMAL(22, 0)
  ,`TS_2` DATETIME(3)
  ,`DBL_1` DOUBLE
  ,`STR_5` TEXT
  ,`STR_6` TEXT
  ,`NUM_3` DECIMAL(22, 0)
  ,`TS_3` DATETIME(3)
  ,`STR_7` TEXT
  ,`STR_8` TEXT
  ,`NUM_4` DECIMAL(22, 0)
  ,`TS_4` DATETIME(3)
  ,`DBL_2` DOUBLE
  ,`STR_9` TEXT
  ,`STR_10` TEXT
  ,`NUM_5` DECIMAL(22, 0)
  ,`TS_5` DATETIME(3)
  ,`STR_11` TEXT
  ,`STR_12` TEXT
  ,`NUM_6` DECIMAL(22, 0)
  ,`TS_6` DATETIME(3)
  ,`DBL_3` DOUBLE
  ,`STR_13` TEXT
  ,`STR_14` TEXT
  ,`NUM_7` DECIMAL(22, 0)
  ,`TS_7` DATETIME(3)
  ,`STR_15` TEXT
  ,`STR_16` TEXT
  ,`NUM_8` DECIMAL(22, 0)
  ,`TS_8` DATETIME(3)
  ,`DBL_4` DOUBLE
  ,`STR_17` TEXT
  ,`STR_18` TEXT
  ,`NUM_9` DECIMAL(22, 0)
  ,`TS_9` DATETIME(3)
  ,`STR_19` TEXT
  ,`STR_20` TEXT
  ,`NUM_10` DECIMAL(22, 0)
  ,`TS_10` DATETIME(3)
  ,`DBL_5` DOUBLE
  ,`STR_21` TEXT
  ,`STR_22` TEXT
  ,`NUM_11` DECIMAL(22, 0)
  ,`TS_11` DATETIME(3)
  ,`STR_23` TEXT
  ,`STR_24` TEXT
  ,`NUM_12` DECIMAL(22, 0)
  ,`TS_12` DATETIME(3)
  ,`DBL_6` DOUBLE
  ,`STR_25` TEXT
  ,`STR_26` TEXT
  ,`NUM_13` DECIMAL(22, 0)
  ,`TS_13` DATETIME(3)
  ,`STR_27` TEXT
  ,`STR_28` TEXT
  ,`NUM_14` DECIMAL(22, 0)
  ,`TS_14` DATETIME(3)
  ,`DBL_7` DOUBLE
  ,`STR_29` TEXT
  ,`STR_30` TEXT
  ,`NUM_15` DECIMAL(22, 0)
  ,`TS_15` DATETIME(3)
  ,`STR_31` TEXT
  ,`STR_32` TEXT
  ,`NUM_16` DECIMAL(22, 0)
  ,`TS_16` DATETIME(3)
  ,`DBL_8` DOUBLE
  ,`STR_33` TEXT
  ,`STR_34` TEXT
  ,`NUM_17` DECIMAL(22, 0)
  ,`TS_17` DATETIME(3)
  ,`STR_35` TEXT
  ,`STR_36` TEXT
  ,`NUM_18` DECIMAL(22, 0)
  ,`TS_18` DATETIME(3)
  ,`DBL_9` DOUBLE
  ,`STR_37` TEXT
  ,`STR_38` TEXT
  ,`NUM_19` DECIMAL(22, 0)
  ,`TS_19` DATETIME(3)
  ,`STR_39` TEXT
  ,`STR_40` TEXT
  ,`NUM_20` DECIMAL(22, 0)
  ,`TS_20` DATETIME(3)
  ,`DBL_10` DOUBLE
  ,`STR_41` TEXT
  ,`STR_42` TEXT
  ,`NUM_21` DECIMAL(22, 0)
  ,`TS_21` DATETIME(3)
  ,`STR_43` TEXT
  ,`STR_44` TEXT
  ,`NUM_22` DECIMAL(22, 0)
  ,`TS_22` DATETIME(3)
  ,`DBL_11` DOUBLE
  ,`STR_45` TEXT
  ,`STR_46` TEXT
  ,`NUM_23` DECIMAL(22, 0)
  ,`TS_23` DATETIME(3)
  ,`STR_47` TEXT
  ,`STR_48` TEXT
  ,`NUM_24` DECIMAL(22, 0)
  ,`TS_24` DATETIME(3)
  ,`DBL_12` DOUBLE
  ,`STR_49` TEXT
  ,`STR_50` TEXT
  ,`NUM_25` DECIMAL(22, 0)
  ,`TS_25` DATETIME(3)
  ,`STR_51` TEXT
  ,`STR_52` TEXT
  ,`NUM_26` DECIMAL(22, 0)
  ,`TS_26` DATETIME(3)
  ,`DBL_13` DOUBLE
  ,`STR_53` TEXT
  ,`STR_54` TEXT
  ,`NUM_27` DECIMAL(22, 0)
  ,`TS_27` DATETIME(3)
  ,`STR_55` TEXT
  ,`STR_56` TEXT
  ,`NUM_28` DECIMAL(22, 0)
  ,`TS_28` DATETIME(3)
  ,`DBL_14` DOUBLE
  ,`STR_57` TEXT
  ,`STR_58` TEXT
  ,`NUM_29` DECIMAL(22, 0)
  ,`TS_29` DATETIME(3)
  ,`STR_59` TEXT
  ,`STR_60` TEXT
  ,`NUM_30` DECIMAL(22, 0)
  ,`TS_30` DATETIME(3)
  ,`DBL_15` DOUBLE
  ,`STR_61` TEXT
  ,`STR_62` TEXT
  ,`NUM_31` DECIMAL(22, 0)
  ,`TS_31` DATETIME(3)
  ,`STR_63` TEXT
  ,`STR_64` TEXT
  ,`NUM_32` DECIMAL(22, 0)
  ,`TS_32` DATETIME(3)
  ,`DBL_16` DOUBLE
  
  ,PRIMARY KEY (`tenant_id`, `obj_def_id`, `obj_id`, `obj_ver`, `pg_no`)
  ,UNIQUE INDEX `obj_store__USER_USTR_unique_1` (`USTR_1_TD`(139), `USTR_1`(255), `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store__USER_UNUM_unique_1` (`UNUM_1_TD`(139), `UNUM_1`, `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store__USER_UTS_unique_1` (`UTS_1_TD`(139), `UTS_1`, `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store__USER_UDBL_unique_1` (`UDBL_1_TD`(139), `UDBL_1`, `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store__USER_USTR_unique_2` (`USTR_2_TD`(139), `USTR_2`(255), `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store__USER_UNUM_unique_2` (`UNUM_2_TD`(139), `UNUM_2`, `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store__USER_UTS_unique_2` (`UTS_2_TD`(139), `UTS_2`, `tenant_id`, `obj_def_id`(128))
  ,UNIQUE INDEX `obj_store__USER_UDBL_unique_2` (`UDBL_2_TD`(139), `UDBL_2`, `tenant_id`, `obj_def_id`(128))
  ,INDEX `obj_store__USER_ISTR_index_1` (`ISTR_1`(255), `ISTR_1_TD`(139))
  ,INDEX `obj_store__USER_INUM_index_1` (`INUM_1`, `INUM_1_TD`(139))
  ,INDEX `obj_store__USER_ITS_index_1` (`ITS_1`, `ITS_1_TD`(139))
  ,INDEX `obj_store__USER_IDBL_index_1` (`IDBL_1`, `IDBL_1_TD`(139))
  ,INDEX `obj_store__USER_ISTR_index_2` (`ISTR_2`(255), `ISTR_2_TD`(139))
  ,INDEX `obj_store__USER_INUM_index_2` (`INUM_2`, `INUM_2_TD`(139))
  ,INDEX `obj_store__USER_ITS_index_2` (`ITS_2`, `ITS_2_TD`(139))
  ,INDEX `obj_store__USER_IDBL_index_2` (`IDBL_2`, `IDBL_2_TD`(139))
  ,INDEX `obj_store__USER_ISTR_index_3` (`ISTR_3`(255), `ISTR_3_TD`(139))
  ,INDEX `obj_store__USER_ISTR_index_4` (`ISTR_4`(255), `ISTR_4_TD`(139))
  ,INDEX `obj_store__USER_INUM_index_3` (`INUM_3`, `INUM_3_TD`(139))
  ,INDEX `obj_store__USER_ITS_index_3` (`ITS_3`, `ITS_3_TD`(139))
  ,INDEX `obj_store__USER_IDBL_index_3` (`IDBL_3`, `IDBL_3_TD`(139))
  ,INDEX `obj_store__USER_ISTR_index_5` (`ISTR_5`(255), `ISTR_5_TD`(139))
  ,INDEX `obj_store__USER_INUM_index_4` (`INUM_4`, `INUM_4_TD`(139))
  ,INDEX `obj_store__USER_ITS_index_4` (`ITS_4`, `ITS_4_TD`(139))
  ,INDEX `obj_store__USER_IDBL_index_4` (`IDBL_4`, `IDBL_4_TD`(139))
  ,INDEX `obj_store__USER_cre_user_index` (`cre_user`(64), `obj_def_id`(128), `tenant_id`)
)
ENGINE = INNODB ROW_FORMAT=DYNAMIC

PARTITION BY RANGE( `tenant_id` )
    SUBPARTITION BY LINEAR KEY( `obj_def_id` ) (
        PARTITION obj_store__USER_0 VALUES LESS THAN (1) (
            SUBPARTITION obj_store__USER_0_0,
            SUBPARTITION obj_store__USER_0_1,
            SUBPARTITION obj_store__USER_0_2,
            SUBPARTITION obj_store__USER_0_3,
            SUBPARTITION obj_store__USER_0_4,
            SUBPARTITION obj_store__USER_0_5,
            SUBPARTITION obj_store__USER_0_6,
            SUBPARTITION obj_store__USER_0_7
        )
    );


