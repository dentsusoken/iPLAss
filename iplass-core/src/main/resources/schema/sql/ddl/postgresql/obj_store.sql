/* drop/create OBJ_STORE */
drop table if exists "obj_store" cascade;
create table "obj_store"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "pg_no" numeric(2,0) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "obj_def_ver" numeric(10,0),
    "status" char(1),
    "obj_name" varchar(256),
    "obj_desc" varchar(1024),
    "cre_date" timestamp(3),
    "up_date" timestamp(3),
    "s_date" timestamp(3),
    "e_date" timestamp(3),
    "lock_user" varchar(64),
    "cre_user" varchar(64),
    "up_user" varchar(64),
    "ustr_1" varchar(4000),
    "ustr_1_td" varchar(139),
    "unum_1" numeric,
    "unum_1_td" varchar(139),
    "uts_1" timestamp(3),
    "uts_1_td" varchar(139),
    "udbl_1" double precision,
    "udbl_1_td" varchar(139),
    "ustr_2" varchar(4000),
    "ustr_2_td" varchar(139),
    "unum_2" numeric,
    "unum_2_td" varchar(139),
    "uts_2" timestamp(3),
    "uts_2_td" varchar(139),
    "udbl_2" double precision,
    "udbl_2_td" varchar(139),
    "istr_1" varchar(4000),
    "istr_1_td" varchar(139),
    "istr_2" varchar(4000),
    "istr_2_td" varchar(139),
    "inum_1" numeric,
    "inum_1_td" varchar(139),
    "its_1" timestamp(3),
    "its_1_td" varchar(139),
    "idbl_1" double precision,
    "idbl_1_td" varchar(139),
    "istr_3" varchar(4000),
    "istr_3_td" varchar(139),
    "istr_4" varchar(4000),
    "istr_4_td" varchar(139),
    "inum_2" numeric,
    "inum_2_td" varchar(139),
    "its_2" timestamp(3),
    "its_2_td" varchar(139),
    "idbl_2" double precision,
    "idbl_2_td" varchar(139),
    "istr_5" varchar(4000),
    "istr_5_td" varchar(139),
    "istr_6" varchar(4000),
    "istr_6_td" varchar(139),
    "inum_3" numeric,
    "inum_3_td" varchar(139),
    "its_3" timestamp(3),
    "its_3_td" varchar(139),
    "idbl_3" double precision,
    "idbl_3_td" varchar(139),
    "istr_7" varchar(4000),
    "istr_7_td" varchar(139),
    "istr_8" varchar(4000),
    "istr_8_td" varchar(139),
    "inum_4" numeric,
    "inum_4_td" varchar(139),
    "its_4" timestamp(3),
    "its_4_td" varchar(139),
    "idbl_4" double precision,
    "idbl_4_td" varchar(139),
    "str_1" varchar(4000),
    "str_2" varchar(4000),
    "str_3" varchar(4000),
    "str_4" varchar(4000),
    "num_1" numeric,
    "ts_1" timestamp(3),
    "dbl_1" double precision,
    "str_5" varchar(4000),
    "str_6" varchar(4000),
    "str_7" varchar(4000),
    "str_8" varchar(4000),
    "num_2" numeric,
    "ts_2" timestamp(3),
    "dbl_2" double precision,
    "str_9" varchar(4000),
    "str_10" varchar(4000),
    "str_11" varchar(4000),
    "str_12" varchar(4000),
    "num_3" numeric,
    "ts_3" timestamp(3),
    "dbl_3" double precision,
    "str_13" varchar(4000),
    "str_14" varchar(4000),
    "str_15" varchar(4000),
    "str_16" varchar(4000),
    "num_4" numeric,
    "ts_4" timestamp(3),
    "dbl_4" double precision,
    "str_17" varchar(4000),
    "str_18" varchar(4000),
    "str_19" varchar(4000),
    "str_20" varchar(4000),
    "num_5" numeric,
    "ts_5" timestamp(3),
    "dbl_5" double precision,
    "str_21" varchar(4000),
    "str_22" varchar(4000),
    "str_23" varchar(4000),
    "str_24" varchar(4000),
    "num_6" numeric,
    "ts_6" timestamp(3),
    "dbl_6" double precision,
    "str_25" varchar(4000),
    "str_26" varchar(4000),
    "str_27" varchar(4000),
    "str_28" varchar(4000),
    "num_7" numeric,
    "ts_7" timestamp(3),
    "dbl_7" double precision,
    "str_29" varchar(4000),
    "str_30" varchar(4000),
    "str_31" varchar(4000),
    "str_32" varchar(4000),
    "num_8" numeric,
    "ts_8" timestamp(3),
    "dbl_8" double precision,
    "str_33" varchar(4000),
    "str_34" varchar(4000),
    "str_35" varchar(4000),
    "str_36" varchar(4000),
    "num_9" numeric,
    "ts_9" timestamp(3),
    "dbl_9" double precision,
    "str_37" varchar(4000),
    "str_38" varchar(4000),
    "str_39" varchar(4000),
    "str_40" varchar(4000),
    "num_10" numeric,
    "ts_10" timestamp(3),
    "dbl_10" double precision,
    "str_41" varchar(4000),
    "str_42" varchar(4000),
    "str_43" varchar(4000),
    "str_44" varchar(4000),
    "num_11" numeric,
    "ts_11" timestamp(3),
    "dbl_11" double precision,
    "str_45" varchar(4000),
    "str_46" varchar(4000),
    "str_47" varchar(4000),
    "str_48" varchar(4000),
    "num_12" numeric,
    "ts_12" timestamp(3),
    "dbl_12" double precision,
    "str_49" varchar(4000),
    "str_50" varchar(4000),
    "str_51" varchar(4000),
    "str_52" varchar(4000),
    "num_13" numeric,
    "ts_13" timestamp(3),
    "dbl_13" double precision,
    "str_53" varchar(4000),
    "str_54" varchar(4000),
    "str_55" varchar(4000),
    "str_56" varchar(4000),
    "num_14" numeric,
    "ts_14" timestamp(3),
    "dbl_14" double precision,
    "str_57" varchar(4000),
    "str_58" varchar(4000),
    "str_59" varchar(4000),
    "str_60" varchar(4000),
    "num_15" numeric,
    "ts_15" timestamp(3),
    "dbl_15" double precision,
    "str_61" varchar(4000),
    "str_62" varchar(4000),
    "str_63" varchar(4000),
    "str_64" varchar(4000),
    "num_16" numeric,
    "ts_16" timestamp(3),
    "dbl_16" double precision,
    "str_65" varchar(4000),
    "str_66" varchar(4000),
    "str_67" varchar(4000),
    "str_68" varchar(4000),
    "num_17" numeric,
    "ts_17" timestamp(3),
    "dbl_17" double precision,
    "str_69" varchar(4000),
    "str_70" varchar(4000),
    "str_71" varchar(4000),
    "str_72" varchar(4000),
    "num_18" numeric,
    "ts_18" timestamp(3),
    "dbl_18" double precision,
    "str_73" varchar(4000),
    "str_74" varchar(4000),
    "str_75" varchar(4000),
    "str_76" varchar(4000),
    "num_19" numeric,
    "ts_19" timestamp(3),
    "dbl_19" double precision,
    "str_77" varchar(4000),
    "str_78" varchar(4000),
    "str_79" varchar(4000),
    "str_80" varchar(4000),
    "num_20" numeric,
    "ts_20" timestamp(3),
    "dbl_20" double precision,
    "str_81" varchar(4000),
    "str_82" varchar(4000),
    "str_83" varchar(4000),
    "str_84" varchar(4000),
    "num_21" numeric,
    "ts_21" timestamp(3),
    "dbl_21" double precision,
    "str_85" varchar(4000),
    "str_86" varchar(4000),
    "str_87" varchar(4000),
    "str_88" varchar(4000),
    "num_22" numeric,
    "ts_22" timestamp(3),
    "dbl_22" double precision,
    "str_89" varchar(4000),
    "str_90" varchar(4000),
    "str_91" varchar(4000),
    "str_92" varchar(4000),
    "num_23" numeric,
    "ts_23" timestamp(3),
    "dbl_23" double precision,
    "str_93" varchar(4000),
    "str_94" varchar(4000),
    "str_95" varchar(4000),
    "str_96" varchar(4000),
    "num_24" numeric,
    "ts_24" timestamp(3),
    "dbl_24" double precision,
    "str_97" varchar(4000),
    "str_98" varchar(4000),
    "str_99" varchar(4000),
    "str_100" varchar(4000),
    "num_25" numeric,
    "ts_25" timestamp(3),
    "dbl_25" double precision,
    "str_101" varchar(4000),
    "str_102" varchar(4000),
    "str_103" varchar(4000),
    "str_104" varchar(4000),
    "num_26" numeric,
    "ts_26" timestamp(3),
    "dbl_26" double precision,
    "str_105" varchar(4000),
    "str_106" varchar(4000),
    "str_107" varchar(4000),
    "str_108" varchar(4000),
    "num_27" numeric,
    "ts_27" timestamp(3),
    "dbl_27" double precision,
    "str_109" varchar(4000),
    "str_110" varchar(4000),
    "str_111" varchar(4000),
    "str_112" varchar(4000),
    "num_28" numeric,
    "ts_28" timestamp(3),
    "dbl_28" double precision,
    "str_113" varchar(4000),
    "str_114" varchar(4000),
    "str_115" varchar(4000),
    "str_116" varchar(4000),
    "num_29" numeric,
    "ts_29" timestamp(3),
    "dbl_29" double precision,
    "str_117" varchar(4000),
    "str_118" varchar(4000),
    "str_119" varchar(4000),
    "str_120" varchar(4000),
    "num_30" numeric,
    "ts_30" timestamp(3),
    "dbl_30" double precision,
    "str_121" varchar(4000),
    "str_122" varchar(4000),
    "str_123" varchar(4000),
    "str_124" varchar(4000),
    "num_31" numeric,
    "ts_31" timestamp(3),
    "dbl_31" double precision,
    "str_125" varchar(4000),
    "str_126" varchar(4000),
    "str_127" varchar(4000),
    "str_128" varchar(4000),
    "num_32" numeric,
    "ts_32" timestamp(3),
    "dbl_32" double precision,
    constraint "obj_store_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store_ustr_unique_1" on "obj_store" ("ustr_1_td", "ustr_1");
create unique index "obj_store_unum_unique_1" on "obj_store" ("unum_1_td", "unum_1");
create unique index "obj_store_uts_unique_1" on "obj_store" ("uts_1_td", "uts_1");
create unique index "obj_store_udbl_unique_1" on "obj_store" ("udbl_1_td", "udbl_1");
create unique index "obj_store_ustr_unique_2" on "obj_store" ("ustr_2_td", "ustr_2");
create unique index "obj_store_unum_unique_2" on "obj_store" ("unum_2_td", "unum_2");
create unique index "obj_store_uts_unique_2" on "obj_store" ("uts_2_td", "uts_2");
create unique index "obj_store_udbl_unique_2" on "obj_store" ("udbl_2_td", "udbl_2");
create index "obj_store_istr_index_1" on "obj_store" ("istr_1", "istr_1_td");
create index "obj_store_istr_index_2" on "obj_store" ("istr_2", "istr_2_td");
create index "obj_store_inum_index_1" on "obj_store" ("inum_1", "inum_1_td");
create index "obj_store_its_index_1" on "obj_store" ("its_1", "its_1_td");
create index "obj_store_idbl_index_1" on "obj_store" ("idbl_1", "idbl_1_td");
create index "obj_store_istr_index_3" on "obj_store" ("istr_3", "istr_3_td");
create index "obj_store_istr_index_4" on "obj_store" ("istr_4", "istr_4_td");
create index "obj_store_inum_index_2" on "obj_store" ("inum_2", "inum_2_td");
create index "obj_store_its_index_2" on "obj_store" ("its_2", "its_2_td");
create index "obj_store_idbl_index_2" on "obj_store" ("idbl_2", "idbl_2_td");
create index "obj_store_istr_index_5" on "obj_store" ("istr_5", "istr_5_td");
create index "obj_store_istr_index_6" on "obj_store" ("istr_6", "istr_6_td");
create index "obj_store_inum_index_3" on "obj_store" ("inum_3", "inum_3_td");
create index "obj_store_its_index_3" on "obj_store" ("its_3", "its_3_td");
create index "obj_store_idbl_index_3" on "obj_store" ("idbl_3", "idbl_3_td");
create index "obj_store_istr_index_7" on "obj_store" ("istr_7", "istr_7_td");
create index "obj_store_istr_index_8" on "obj_store" ("istr_8", "istr_8_td");
create index "obj_store_inum_index_4" on "obj_store" ("inum_4", "inum_4_td");
create index "obj_store_its_index_4" on "obj_store" ("its_4", "its_4_td");
create index "obj_store_idbl_index_4" on "obj_store" ("idbl_4", "idbl_4_td");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__mtp" cascade;
create table "obj_store__mtp"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "pg_no" numeric(2,0) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "obj_def_ver" numeric(10,0),
    "status" char(1),
    "obj_name" varchar(256),
    "obj_desc" varchar(1024),
    "cre_date" timestamp(3),
    "up_date" timestamp(3),
    "s_date" timestamp(3),
    "e_date" timestamp(3),
    "lock_user" varchar(64),
    "cre_user" varchar(64),
    "up_user" varchar(64),
    "ustr_1" varchar(4000),
    "ustr_1_td" varchar(139),
    "unum_1" numeric,
    "unum_1_td" varchar(139),
    "uts_1" timestamp(3),
    "uts_1_td" varchar(139),
    "udbl_1" double precision,
    "udbl_1_td" varchar(139),
    "ustr_2" varchar(4000),
    "ustr_2_td" varchar(139),
    "unum_2" numeric,
    "unum_2_td" varchar(139),
    "uts_2" timestamp(3),
    "uts_2_td" varchar(139),
    "udbl_2" double precision,
    "udbl_2_td" varchar(139),
    "istr_1" varchar(4000),
    "istr_1_td" varchar(139),
    "istr_2" varchar(4000),
    "istr_2_td" varchar(139),
    "inum_1" numeric,
    "inum_1_td" varchar(139),
    "its_1" timestamp(3),
    "its_1_td" varchar(139),
    "idbl_1" double precision,
    "idbl_1_td" varchar(139),
    "istr_3" varchar(4000),
    "istr_3_td" varchar(139),
    "istr_4" varchar(4000),
    "istr_4_td" varchar(139),
    "inum_2" numeric,
    "inum_2_td" varchar(139),
    "its_2" timestamp(3),
    "its_2_td" varchar(139),
    "idbl_2" double precision,
    "idbl_2_td" varchar(139),
    "istr_5" varchar(4000),
    "istr_5_td" varchar(139),
    "istr_6" varchar(4000),
    "istr_6_td" varchar(139),
    "inum_3" numeric,
    "inum_3_td" varchar(139),
    "its_3" timestamp(3),
    "its_3_td" varchar(139),
    "idbl_3" double precision,
    "idbl_3_td" varchar(139),
    "istr_7" varchar(4000),
    "istr_7_td" varchar(139),
    "istr_8" varchar(4000),
    "istr_8_td" varchar(139),
    "inum_4" numeric,
    "inum_4_td" varchar(139),
    "its_4" timestamp(3),
    "its_4_td" varchar(139),
    "idbl_4" double precision,
    "idbl_4_td" varchar(139),
    "str_1" varchar(4000),
    "str_2" varchar(4000),
    "str_3" varchar(4000),
    "str_4" varchar(4000),
    "num_1" numeric,
    "ts_1" timestamp(3),
    "dbl_1" double precision,
    "str_5" varchar(4000),
    "str_6" varchar(4000),
    "str_7" varchar(4000),
    "str_8" varchar(4000),
    "num_2" numeric,
    "ts_2" timestamp(3),
    "dbl_2" double precision,
    "str_9" varchar(4000),
    "str_10" varchar(4000),
    "str_11" varchar(4000),
    "str_12" varchar(4000),
    "num_3" numeric,
    "ts_3" timestamp(3),
    "dbl_3" double precision,
    "str_13" varchar(4000),
    "str_14" varchar(4000),
    "str_15" varchar(4000),
    "str_16" varchar(4000),
    "num_4" numeric,
    "ts_4" timestamp(3),
    "dbl_4" double precision,
    "str_17" varchar(4000),
    "str_18" varchar(4000),
    "str_19" varchar(4000),
    "str_20" varchar(4000),
    "num_5" numeric,
    "ts_5" timestamp(3),
    "dbl_5" double precision,
    "str_21" varchar(4000),
    "str_22" varchar(4000),
    "str_23" varchar(4000),
    "str_24" varchar(4000),
    "num_6" numeric,
    "ts_6" timestamp(3),
    "dbl_6" double precision,
    "str_25" varchar(4000),
    "str_26" varchar(4000),
    "str_27" varchar(4000),
    "str_28" varchar(4000),
    "num_7" numeric,
    "ts_7" timestamp(3),
    "dbl_7" double precision,
    "str_29" varchar(4000),
    "str_30" varchar(4000),
    "str_31" varchar(4000),
    "str_32" varchar(4000),
    "num_8" numeric,
    "ts_8" timestamp(3),
    "dbl_8" double precision,
    "str_33" varchar(4000),
    "str_34" varchar(4000),
    "str_35" varchar(4000),
    "str_36" varchar(4000),
    "num_9" numeric,
    "ts_9" timestamp(3),
    "dbl_9" double precision,
    "str_37" varchar(4000),
    "str_38" varchar(4000),
    "str_39" varchar(4000),
    "str_40" varchar(4000),
    "num_10" numeric,
    "ts_10" timestamp(3),
    "dbl_10" double precision,
    "str_41" varchar(4000),
    "str_42" varchar(4000),
    "str_43" varchar(4000),
    "str_44" varchar(4000),
    "num_11" numeric,
    "ts_11" timestamp(3),
    "dbl_11" double precision,
    "str_45" varchar(4000),
    "str_46" varchar(4000),
    "str_47" varchar(4000),
    "str_48" varchar(4000),
    "num_12" numeric,
    "ts_12" timestamp(3),
    "dbl_12" double precision,
    "str_49" varchar(4000),
    "str_50" varchar(4000),
    "str_51" varchar(4000),
    "str_52" varchar(4000),
    "num_13" numeric,
    "ts_13" timestamp(3),
    "dbl_13" double precision,
    "str_53" varchar(4000),
    "str_54" varchar(4000),
    "str_55" varchar(4000),
    "str_56" varchar(4000),
    "num_14" numeric,
    "ts_14" timestamp(3),
    "dbl_14" double precision,
    "str_57" varchar(4000),
    "str_58" varchar(4000),
    "str_59" varchar(4000),
    "str_60" varchar(4000),
    "num_15" numeric,
    "ts_15" timestamp(3),
    "dbl_15" double precision,
    "str_61" varchar(4000),
    "str_62" varchar(4000),
    "str_63" varchar(4000),
    "str_64" varchar(4000),
    "num_16" numeric,
    "ts_16" timestamp(3),
    "dbl_16" double precision,
    "str_65" varchar(4000),
    "str_66" varchar(4000),
    "str_67" varchar(4000),
    "str_68" varchar(4000),
    "num_17" numeric,
    "ts_17" timestamp(3),
    "dbl_17" double precision,
    "str_69" varchar(4000),
    "str_70" varchar(4000),
    "str_71" varchar(4000),
    "str_72" varchar(4000),
    "num_18" numeric,
    "ts_18" timestamp(3),
    "dbl_18" double precision,
    "str_73" varchar(4000),
    "str_74" varchar(4000),
    "str_75" varchar(4000),
    "str_76" varchar(4000),
    "num_19" numeric,
    "ts_19" timestamp(3),
    "dbl_19" double precision,
    "str_77" varchar(4000),
    "str_78" varchar(4000),
    "str_79" varchar(4000),
    "str_80" varchar(4000),
    "num_20" numeric,
    "ts_20" timestamp(3),
    "dbl_20" double precision,
    "str_81" varchar(4000),
    "str_82" varchar(4000),
    "str_83" varchar(4000),
    "str_84" varchar(4000),
    "num_21" numeric,
    "ts_21" timestamp(3),
    "dbl_21" double precision,
    "str_85" varchar(4000),
    "str_86" varchar(4000),
    "str_87" varchar(4000),
    "str_88" varchar(4000),
    "num_22" numeric,
    "ts_22" timestamp(3),
    "dbl_22" double precision,
    "str_89" varchar(4000),
    "str_90" varchar(4000),
    "str_91" varchar(4000),
    "str_92" varchar(4000),
    "num_23" numeric,
    "ts_23" timestamp(3),
    "dbl_23" double precision,
    "str_93" varchar(4000),
    "str_94" varchar(4000),
    "str_95" varchar(4000),
    "str_96" varchar(4000),
    "num_24" numeric,
    "ts_24" timestamp(3),
    "dbl_24" double precision,
    "str_97" varchar(4000),
    "str_98" varchar(4000),
    "str_99" varchar(4000),
    "str_100" varchar(4000),
    "num_25" numeric,
    "ts_25" timestamp(3),
    "dbl_25" double precision,
    "str_101" varchar(4000),
    "str_102" varchar(4000),
    "str_103" varchar(4000),
    "str_104" varchar(4000),
    "num_26" numeric,
    "ts_26" timestamp(3),
    "dbl_26" double precision,
    "str_105" varchar(4000),
    "str_106" varchar(4000),
    "str_107" varchar(4000),
    "str_108" varchar(4000),
    "num_27" numeric,
    "ts_27" timestamp(3),
    "dbl_27" double precision,
    "str_109" varchar(4000),
    "str_110" varchar(4000),
    "str_111" varchar(4000),
    "str_112" varchar(4000),
    "num_28" numeric,
    "ts_28" timestamp(3),
    "dbl_28" double precision,
    "str_113" varchar(4000),
    "str_114" varchar(4000),
    "str_115" varchar(4000),
    "str_116" varchar(4000),
    "num_29" numeric,
    "ts_29" timestamp(3),
    "dbl_29" double precision,
    "str_117" varchar(4000),
    "str_118" varchar(4000),
    "str_119" varchar(4000),
    "str_120" varchar(4000),
    "num_30" numeric,
    "ts_30" timestamp(3),
    "dbl_30" double precision,
    "str_121" varchar(4000),
    "str_122" varchar(4000),
    "str_123" varchar(4000),
    "str_124" varchar(4000),
    "num_31" numeric,
    "ts_31" timestamp(3),
    "dbl_31" double precision,
    "str_125" varchar(4000),
    "str_126" varchar(4000),
    "str_127" varchar(4000),
    "str_128" varchar(4000),
    "num_32" numeric,
    "ts_32" timestamp(3),
    "dbl_32" double precision,
    constraint "obj_store__mtp_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__mtp_ustr_unique_1" on "obj_store__mtp" ("ustr_1_td", "ustr_1");
create unique index "obj_store__mtp_unum_unique_1" on "obj_store__mtp" ("unum_1_td", "unum_1");
create unique index "obj_store__mtp_uts_unique_1" on "obj_store__mtp" ("uts_1_td", "uts_1");
create unique index "obj_store__mtp_udbl_unique_1" on "obj_store__mtp" ("udbl_1_td", "udbl_1");
create unique index "obj_store__mtp_ustr_unique_2" on "obj_store__mtp" ("ustr_2_td", "ustr_2");
create unique index "obj_store__mtp_unum_unique_2" on "obj_store__mtp" ("unum_2_td", "unum_2");
create unique index "obj_store__mtp_uts_unique_2" on "obj_store__mtp" ("uts_2_td", "uts_2");
create unique index "obj_store__mtp_udbl_unique_2" on "obj_store__mtp" ("udbl_2_td", "udbl_2");
create index "obj_store__mtp_istr_index_1" on "obj_store__mtp" ("istr_1", "istr_1_td");
create index "obj_store__mtp_istr_index_2" on "obj_store__mtp" ("istr_2", "istr_2_td");
create index "obj_store__mtp_inum_index_1" on "obj_store__mtp" ("inum_1", "inum_1_td");
create index "obj_store__mtp_its_index_1" on "obj_store__mtp" ("its_1", "its_1_td");
create index "obj_store__mtp_idbl_index_1" on "obj_store__mtp" ("idbl_1", "idbl_1_td");
create index "obj_store__mtp_istr_index_3" on "obj_store__mtp" ("istr_3", "istr_3_td");
create index "obj_store__mtp_istr_index_4" on "obj_store__mtp" ("istr_4", "istr_4_td");
create index "obj_store__mtp_inum_index_2" on "obj_store__mtp" ("inum_2", "inum_2_td");
create index "obj_store__mtp_its_index_2" on "obj_store__mtp" ("its_2", "its_2_td");
create index "obj_store__mtp_idbl_index_2" on "obj_store__mtp" ("idbl_2", "idbl_2_td");
create index "obj_store__mtp_istr_index_5" on "obj_store__mtp" ("istr_5", "istr_5_td");
create index "obj_store__mtp_istr_index_6" on "obj_store__mtp" ("istr_6", "istr_6_td");
create index "obj_store__mtp_inum_index_3" on "obj_store__mtp" ("inum_3", "inum_3_td");
create index "obj_store__mtp_its_index_3" on "obj_store__mtp" ("its_3", "its_3_td");
create index "obj_store__mtp_idbl_index_3" on "obj_store__mtp" ("idbl_3", "idbl_3_td");
create index "obj_store__mtp_istr_index_7" on "obj_store__mtp" ("istr_7", "istr_7_td");
create index "obj_store__mtp_istr_index_8" on "obj_store__mtp" ("istr_8", "istr_8_td");
create index "obj_store__mtp_inum_index_4" on "obj_store__mtp" ("inum_4", "inum_4_td");
create index "obj_store__mtp_its_index_4" on "obj_store__mtp" ("its_4", "its_4_td");
create index "obj_store__mtp_idbl_index_4" on "obj_store__mtp" ("idbl_4", "idbl_4_td");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__user" cascade;
create table "obj_store__user"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "pg_no" numeric(2,0) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "obj_def_ver" numeric(10,0),
    "status" char(1),
    "obj_name" varchar(256),
    "obj_desc" varchar(1024),
    "cre_date" timestamp(3),
    "up_date" timestamp(3),
    "s_date" timestamp(3),
    "e_date" timestamp(3),
    "lock_user" varchar(64),
    "cre_user" varchar(64),
    "up_user" varchar(64),
    "ustr_1" varchar(4000),
    "ustr_1_td" varchar(139),
    "unum_1" numeric,
    "unum_1_td" varchar(139),
    "uts_1" timestamp(3),
    "uts_1_td" varchar(139),
    "udbl_1" double precision,
    "udbl_1_td" varchar(139),
    "ustr_2" varchar(4000),
    "ustr_2_td" varchar(139),
    "unum_2" numeric,
    "unum_2_td" varchar(139),
    "uts_2" timestamp(3),
    "uts_2_td" varchar(139),
    "udbl_2" double precision,
    "udbl_2_td" varchar(139),
    "istr_1" varchar(4000),
    "istr_1_td" varchar(139),
    "istr_2" varchar(4000),
    "istr_2_td" varchar(139),
    "inum_1" numeric,
    "inum_1_td" varchar(139),
    "its_1" timestamp(3),
    "its_1_td" varchar(139),
    "idbl_1" double precision,
    "idbl_1_td" varchar(139),
    "istr_3" varchar(4000),
    "istr_3_td" varchar(139),
    "istr_4" varchar(4000),
    "istr_4_td" varchar(139),
    "inum_2" numeric,
    "inum_2_td" varchar(139),
    "its_2" timestamp(3),
    "its_2_td" varchar(139),
    "idbl_2" double precision,
    "idbl_2_td" varchar(139),
    "istr_5" varchar(4000),
    "istr_5_td" varchar(139),
    "istr_6" varchar(4000),
    "istr_6_td" varchar(139),
    "inum_3" numeric,
    "inum_3_td" varchar(139),
    "its_3" timestamp(3),
    "its_3_td" varchar(139),
    "idbl_3" double precision,
    "idbl_3_td" varchar(139),
    "istr_7" varchar(4000),
    "istr_7_td" varchar(139),
    "istr_8" varchar(4000),
    "istr_8_td" varchar(139),
    "inum_4" numeric,
    "inum_4_td" varchar(139),
    "its_4" timestamp(3),
    "its_4_td" varchar(139),
    "idbl_4" double precision,
    "idbl_4_td" varchar(139),
    "str_1" varchar(4000),
    "str_2" varchar(4000),
    "str_3" varchar(4000),
    "str_4" varchar(4000),
    "num_1" numeric,
    "ts_1" timestamp(3),
    "dbl_1" double precision,
    "str_5" varchar(4000),
    "str_6" varchar(4000),
    "str_7" varchar(4000),
    "str_8" varchar(4000),
    "num_2" numeric,
    "ts_2" timestamp(3),
    "dbl_2" double precision,
    "str_9" varchar(4000),
    "str_10" varchar(4000),
    "str_11" varchar(4000),
    "str_12" varchar(4000),
    "num_3" numeric,
    "ts_3" timestamp(3),
    "dbl_3" double precision,
    "str_13" varchar(4000),
    "str_14" varchar(4000),
    "str_15" varchar(4000),
    "str_16" varchar(4000),
    "num_4" numeric,
    "ts_4" timestamp(3),
    "dbl_4" double precision,
    "str_17" varchar(4000),
    "str_18" varchar(4000),
    "str_19" varchar(4000),
    "str_20" varchar(4000),
    "num_5" numeric,
    "ts_5" timestamp(3),
    "dbl_5" double precision,
    "str_21" varchar(4000),
    "str_22" varchar(4000),
    "str_23" varchar(4000),
    "str_24" varchar(4000),
    "num_6" numeric,
    "ts_6" timestamp(3),
    "dbl_6" double precision,
    "str_25" varchar(4000),
    "str_26" varchar(4000),
    "str_27" varchar(4000),
    "str_28" varchar(4000),
    "num_7" numeric,
    "ts_7" timestamp(3),
    "dbl_7" double precision,
    "str_29" varchar(4000),
    "str_30" varchar(4000),
    "str_31" varchar(4000),
    "str_32" varchar(4000),
    "num_8" numeric,
    "ts_8" timestamp(3),
    "dbl_8" double precision,
    "str_33" varchar(4000),
    "str_34" varchar(4000),
    "str_35" varchar(4000),
    "str_36" varchar(4000),
    "num_9" numeric,
    "ts_9" timestamp(3),
    "dbl_9" double precision,
    "str_37" varchar(4000),
    "str_38" varchar(4000),
    "str_39" varchar(4000),
    "str_40" varchar(4000),
    "num_10" numeric,
    "ts_10" timestamp(3),
    "dbl_10" double precision,
    "str_41" varchar(4000),
    "str_42" varchar(4000),
    "str_43" varchar(4000),
    "str_44" varchar(4000),
    "num_11" numeric,
    "ts_11" timestamp(3),
    "dbl_11" double precision,
    "str_45" varchar(4000),
    "str_46" varchar(4000),
    "str_47" varchar(4000),
    "str_48" varchar(4000),
    "num_12" numeric,
    "ts_12" timestamp(3),
    "dbl_12" double precision,
    "str_49" varchar(4000),
    "str_50" varchar(4000),
    "str_51" varchar(4000),
    "str_52" varchar(4000),
    "num_13" numeric,
    "ts_13" timestamp(3),
    "dbl_13" double precision,
    "str_53" varchar(4000),
    "str_54" varchar(4000),
    "str_55" varchar(4000),
    "str_56" varchar(4000),
    "num_14" numeric,
    "ts_14" timestamp(3),
    "dbl_14" double precision,
    "str_57" varchar(4000),
    "str_58" varchar(4000),
    "str_59" varchar(4000),
    "str_60" varchar(4000),
    "num_15" numeric,
    "ts_15" timestamp(3),
    "dbl_15" double precision,
    "str_61" varchar(4000),
    "str_62" varchar(4000),
    "str_63" varchar(4000),
    "str_64" varchar(4000),
    "num_16" numeric,
    "ts_16" timestamp(3),
    "dbl_16" double precision,
    "str_65" varchar(4000),
    "str_66" varchar(4000),
    "str_67" varchar(4000),
    "str_68" varchar(4000),
    "num_17" numeric,
    "ts_17" timestamp(3),
    "dbl_17" double precision,
    "str_69" varchar(4000),
    "str_70" varchar(4000),
    "str_71" varchar(4000),
    "str_72" varchar(4000),
    "num_18" numeric,
    "ts_18" timestamp(3),
    "dbl_18" double precision,
    "str_73" varchar(4000),
    "str_74" varchar(4000),
    "str_75" varchar(4000),
    "str_76" varchar(4000),
    "num_19" numeric,
    "ts_19" timestamp(3),
    "dbl_19" double precision,
    "str_77" varchar(4000),
    "str_78" varchar(4000),
    "str_79" varchar(4000),
    "str_80" varchar(4000),
    "num_20" numeric,
    "ts_20" timestamp(3),
    "dbl_20" double precision,
    "str_81" varchar(4000),
    "str_82" varchar(4000),
    "str_83" varchar(4000),
    "str_84" varchar(4000),
    "num_21" numeric,
    "ts_21" timestamp(3),
    "dbl_21" double precision,
    "str_85" varchar(4000),
    "str_86" varchar(4000),
    "str_87" varchar(4000),
    "str_88" varchar(4000),
    "num_22" numeric,
    "ts_22" timestamp(3),
    "dbl_22" double precision,
    "str_89" varchar(4000),
    "str_90" varchar(4000),
    "str_91" varchar(4000),
    "str_92" varchar(4000),
    "num_23" numeric,
    "ts_23" timestamp(3),
    "dbl_23" double precision,
    "str_93" varchar(4000),
    "str_94" varchar(4000),
    "str_95" varchar(4000),
    "str_96" varchar(4000),
    "num_24" numeric,
    "ts_24" timestamp(3),
    "dbl_24" double precision,
    "str_97" varchar(4000),
    "str_98" varchar(4000),
    "str_99" varchar(4000),
    "str_100" varchar(4000),
    "num_25" numeric,
    "ts_25" timestamp(3),
    "dbl_25" double precision,
    "str_101" varchar(4000),
    "str_102" varchar(4000),
    "str_103" varchar(4000),
    "str_104" varchar(4000),
    "num_26" numeric,
    "ts_26" timestamp(3),
    "dbl_26" double precision,
    "str_105" varchar(4000),
    "str_106" varchar(4000),
    "str_107" varchar(4000),
    "str_108" varchar(4000),
    "num_27" numeric,
    "ts_27" timestamp(3),
    "dbl_27" double precision,
    "str_109" varchar(4000),
    "str_110" varchar(4000),
    "str_111" varchar(4000),
    "str_112" varchar(4000),
    "num_28" numeric,
    "ts_28" timestamp(3),
    "dbl_28" double precision,
    "str_113" varchar(4000),
    "str_114" varchar(4000),
    "str_115" varchar(4000),
    "str_116" varchar(4000),
    "num_29" numeric,
    "ts_29" timestamp(3),
    "dbl_29" double precision,
    "str_117" varchar(4000),
    "str_118" varchar(4000),
    "str_119" varchar(4000),
    "str_120" varchar(4000),
    "num_30" numeric,
    "ts_30" timestamp(3),
    "dbl_30" double precision,
    "str_121" varchar(4000),
    "str_122" varchar(4000),
    "str_123" varchar(4000),
    "str_124" varchar(4000),
    "num_31" numeric,
    "ts_31" timestamp(3),
    "dbl_31" double precision,
    "str_125" varchar(4000),
    "str_126" varchar(4000),
    "str_127" varchar(4000),
    "str_128" varchar(4000),
    "num_32" numeric,
    "ts_32" timestamp(3),
    "dbl_32" double precision,
    constraint "obj_store__user_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__user_ustr_unique_1" on "obj_store__user" ("ustr_1_td", "ustr_1");
create unique index "obj_store__user_unum_unique_1" on "obj_store__user" ("unum_1_td", "unum_1");
create unique index "obj_store__user_uts_unique_1" on "obj_store__user" ("uts_1_td", "uts_1");
create unique index "obj_store__user_udbl_unique_1" on "obj_store__user" ("udbl_1_td", "udbl_1");
create unique index "obj_store__user_ustr_unique_2" on "obj_store__user" ("ustr_2_td", "ustr_2");
create unique index "obj_store__user_unum_unique_2" on "obj_store__user" ("unum_2_td", "unum_2");
create unique index "obj_store__user_uts_unique_2" on "obj_store__user" ("uts_2_td", "uts_2");
create unique index "obj_store__user_udbl_unique_2" on "obj_store__user" ("udbl_2_td", "udbl_2");
create index "obj_store__user_istr_index_1" on "obj_store__user" ("istr_1", "istr_1_td");
create index "obj_store__user_istr_index_2" on "obj_store__user" ("istr_2", "istr_2_td");
create index "obj_store__user_inum_index_1" on "obj_store__user" ("inum_1", "inum_1_td");
create index "obj_store__user_its_index_1" on "obj_store__user" ("its_1", "its_1_td");
create index "obj_store__user_idbl_index_1" on "obj_store__user" ("idbl_1", "idbl_1_td");
create index "obj_store__user_istr_index_3" on "obj_store__user" ("istr_3", "istr_3_td");
create index "obj_store__user_istr_index_4" on "obj_store__user" ("istr_4", "istr_4_td");
create index "obj_store__user_inum_index_2" on "obj_store__user" ("inum_2", "inum_2_td");
create index "obj_store__user_its_index_2" on "obj_store__user" ("its_2", "its_2_td");
create index "obj_store__user_idbl_index_2" on "obj_store__user" ("idbl_2", "idbl_2_td");
create index "obj_store__user_istr_index_5" on "obj_store__user" ("istr_5", "istr_5_td");
create index "obj_store__user_istr_index_6" on "obj_store__user" ("istr_6", "istr_6_td");
create index "obj_store__user_inum_index_3" on "obj_store__user" ("inum_3", "inum_3_td");
create index "obj_store__user_its_index_3" on "obj_store__user" ("its_3", "its_3_td");
create index "obj_store__user_idbl_index_3" on "obj_store__user" ("idbl_3", "idbl_3_td");
create index "obj_store__user_istr_index_7" on "obj_store__user" ("istr_7", "istr_7_td");
create index "obj_store__user_istr_index_8" on "obj_store__user" ("istr_8", "istr_8_td");
create index "obj_store__user_inum_index_4" on "obj_store__user" ("inum_4", "inum_4_td");
create index "obj_store__user_its_index_4" on "obj_store__user" ("its_4", "its_4_td");
create index "obj_store__user_idbl_index_4" on "obj_store__user" ("idbl_4", "idbl_4_td");
