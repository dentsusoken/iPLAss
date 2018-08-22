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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
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
create index "obj_store_istr_index_1" on "obj_store" ("istr_1_td", "istr_1");
create index "obj_store_istr_index_2" on "obj_store" ("istr_2_td", "istr_2");
create index "obj_store_inum_index_1" on "obj_store" ("inum_1_td", "inum_1");
create index "obj_store_its_index_1" on "obj_store" ("its_1_td", "its_1");
create index "obj_store_idbl_index_1" on "obj_store" ("idbl_1_td", "idbl_1");
create index "obj_store_istr_index_3" on "obj_store" ("istr_3_td", "istr_3");
create index "obj_store_istr_index_4" on "obj_store" ("istr_4_td", "istr_4");
create index "obj_store_inum_index_2" on "obj_store" ("inum_2_td", "inum_2");
create index "obj_store_its_index_2" on "obj_store" ("its_2_td", "its_2");
create index "obj_store_idbl_index_2" on "obj_store" ("idbl_2_td", "idbl_2");
create index "obj_store_istr_index_5" on "obj_store" ("istr_5_td", "istr_5");
create index "obj_store_istr_index_6" on "obj_store" ("istr_6_td", "istr_6");
create index "obj_store_inum_index_3" on "obj_store" ("inum_3_td", "inum_3");
create index "obj_store_its_index_3" on "obj_store" ("its_3_td", "its_3");
create index "obj_store_idbl_index_3" on "obj_store" ("idbl_3_td", "idbl_3");
create index "obj_store_istr_index_7" on "obj_store" ("istr_7_td", "istr_7");
create index "obj_store_istr_index_8" on "obj_store" ("istr_8_td", "istr_8");
create index "obj_store_inum_index_4" on "obj_store" ("inum_4_td", "inum_4");
create index "obj_store_its_index_4" on "obj_store" ("its_4_td", "its_4");
create index "obj_store_idbl_index_4" on "obj_store" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__1" cascade;
create table "obj_store__1"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__1_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__1_ustr_unique_1" on "obj_store__1" ("ustr_1_td", "ustr_1");
create unique index "obj_store__1_unum_unique_1" on "obj_store__1" ("unum_1_td", "unum_1");
create unique index "obj_store__1_uts_unique_1" on "obj_store__1" ("uts_1_td", "uts_1");
create unique index "obj_store__1_udbl_unique_1" on "obj_store__1" ("udbl_1_td", "udbl_1");
create unique index "obj_store__1_ustr_unique_2" on "obj_store__1" ("ustr_2_td", "ustr_2");
create unique index "obj_store__1_unum_unique_2" on "obj_store__1" ("unum_2_td", "unum_2");
create unique index "obj_store__1_uts_unique_2" on "obj_store__1" ("uts_2_td", "uts_2");
create unique index "obj_store__1_udbl_unique_2" on "obj_store__1" ("udbl_2_td", "udbl_2");
create index "obj_store__1_istr_index_1" on "obj_store__1" ("istr_1_td", "istr_1");
create index "obj_store__1_istr_index_2" on "obj_store__1" ("istr_2_td", "istr_2");
create index "obj_store__1_inum_index_1" on "obj_store__1" ("inum_1_td", "inum_1");
create index "obj_store__1_its_index_1" on "obj_store__1" ("its_1_td", "its_1");
create index "obj_store__1_idbl_index_1" on "obj_store__1" ("idbl_1_td", "idbl_1");
create index "obj_store__1_istr_index_3" on "obj_store__1" ("istr_3_td", "istr_3");
create index "obj_store__1_istr_index_4" on "obj_store__1" ("istr_4_td", "istr_4");
create index "obj_store__1_inum_index_2" on "obj_store__1" ("inum_2_td", "inum_2");
create index "obj_store__1_its_index_2" on "obj_store__1" ("its_2_td", "its_2");
create index "obj_store__1_idbl_index_2" on "obj_store__1" ("idbl_2_td", "idbl_2");
create index "obj_store__1_istr_index_5" on "obj_store__1" ("istr_5_td", "istr_5");
create index "obj_store__1_istr_index_6" on "obj_store__1" ("istr_6_td", "istr_6");
create index "obj_store__1_inum_index_3" on "obj_store__1" ("inum_3_td", "inum_3");
create index "obj_store__1_its_index_3" on "obj_store__1" ("its_3_td", "its_3");
create index "obj_store__1_idbl_index_3" on "obj_store__1" ("idbl_3_td", "idbl_3");
create index "obj_store__1_istr_index_7" on "obj_store__1" ("istr_7_td", "istr_7");
create index "obj_store__1_istr_index_8" on "obj_store__1" ("istr_8_td", "istr_8");
create index "obj_store__1_inum_index_4" on "obj_store__1" ("inum_4_td", "inum_4");
create index "obj_store__1_its_index_4" on "obj_store__1" ("its_4_td", "its_4");
create index "obj_store__1_idbl_index_4" on "obj_store__1" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__2" cascade;
create table "obj_store__2"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__2_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__2_ustr_unique_1" on "obj_store__2" ("ustr_1_td", "ustr_1");
create unique index "obj_store__2_unum_unique_1" on "obj_store__2" ("unum_1_td", "unum_1");
create unique index "obj_store__2_uts_unique_1" on "obj_store__2" ("uts_1_td", "uts_1");
create unique index "obj_store__2_udbl_unique_1" on "obj_store__2" ("udbl_1_td", "udbl_1");
create unique index "obj_store__2_ustr_unique_2" on "obj_store__2" ("ustr_2_td", "ustr_2");
create unique index "obj_store__2_unum_unique_2" on "obj_store__2" ("unum_2_td", "unum_2");
create unique index "obj_store__2_uts_unique_2" on "obj_store__2" ("uts_2_td", "uts_2");
create unique index "obj_store__2_udbl_unique_2" on "obj_store__2" ("udbl_2_td", "udbl_2");
create index "obj_store__2_istr_index_1" on "obj_store__2" ("istr_1_td", "istr_1");
create index "obj_store__2_istr_index_2" on "obj_store__2" ("istr_2_td", "istr_2");
create index "obj_store__2_inum_index_1" on "obj_store__2" ("inum_1_td", "inum_1");
create index "obj_store__2_its_index_1" on "obj_store__2" ("its_1_td", "its_1");
create index "obj_store__2_idbl_index_1" on "obj_store__2" ("idbl_1_td", "idbl_1");
create index "obj_store__2_istr_index_3" on "obj_store__2" ("istr_3_td", "istr_3");
create index "obj_store__2_istr_index_4" on "obj_store__2" ("istr_4_td", "istr_4");
create index "obj_store__2_inum_index_2" on "obj_store__2" ("inum_2_td", "inum_2");
create index "obj_store__2_its_index_2" on "obj_store__2" ("its_2_td", "its_2");
create index "obj_store__2_idbl_index_2" on "obj_store__2" ("idbl_2_td", "idbl_2");
create index "obj_store__2_istr_index_5" on "obj_store__2" ("istr_5_td", "istr_5");
create index "obj_store__2_istr_index_6" on "obj_store__2" ("istr_6_td", "istr_6");
create index "obj_store__2_inum_index_3" on "obj_store__2" ("inum_3_td", "inum_3");
create index "obj_store__2_its_index_3" on "obj_store__2" ("its_3_td", "its_3");
create index "obj_store__2_idbl_index_3" on "obj_store__2" ("idbl_3_td", "idbl_3");
create index "obj_store__2_istr_index_7" on "obj_store__2" ("istr_7_td", "istr_7");
create index "obj_store__2_istr_index_8" on "obj_store__2" ("istr_8_td", "istr_8");
create index "obj_store__2_inum_index_4" on "obj_store__2" ("inum_4_td", "inum_4");
create index "obj_store__2_its_index_4" on "obj_store__2" ("its_4_td", "its_4");
create index "obj_store__2_idbl_index_4" on "obj_store__2" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__3" cascade;
create table "obj_store__3"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__3_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__3_ustr_unique_1" on "obj_store__3" ("ustr_1_td", "ustr_1");
create unique index "obj_store__3_unum_unique_1" on "obj_store__3" ("unum_1_td", "unum_1");
create unique index "obj_store__3_uts_unique_1" on "obj_store__3" ("uts_1_td", "uts_1");
create unique index "obj_store__3_udbl_unique_1" on "obj_store__3" ("udbl_1_td", "udbl_1");
create unique index "obj_store__3_ustr_unique_2" on "obj_store__3" ("ustr_2_td", "ustr_2");
create unique index "obj_store__3_unum_unique_2" on "obj_store__3" ("unum_2_td", "unum_2");
create unique index "obj_store__3_uts_unique_2" on "obj_store__3" ("uts_2_td", "uts_2");
create unique index "obj_store__3_udbl_unique_2" on "obj_store__3" ("udbl_2_td", "udbl_2");
create index "obj_store__3_istr_index_1" on "obj_store__3" ("istr_1_td", "istr_1");
create index "obj_store__3_istr_index_2" on "obj_store__3" ("istr_2_td", "istr_2");
create index "obj_store__3_inum_index_1" on "obj_store__3" ("inum_1_td", "inum_1");
create index "obj_store__3_its_index_1" on "obj_store__3" ("its_1_td", "its_1");
create index "obj_store__3_idbl_index_1" on "obj_store__3" ("idbl_1_td", "idbl_1");
create index "obj_store__3_istr_index_3" on "obj_store__3" ("istr_3_td", "istr_3");
create index "obj_store__3_istr_index_4" on "obj_store__3" ("istr_4_td", "istr_4");
create index "obj_store__3_inum_index_2" on "obj_store__3" ("inum_2_td", "inum_2");
create index "obj_store__3_its_index_2" on "obj_store__3" ("its_2_td", "its_2");
create index "obj_store__3_idbl_index_2" on "obj_store__3" ("idbl_2_td", "idbl_2");
create index "obj_store__3_istr_index_5" on "obj_store__3" ("istr_5_td", "istr_5");
create index "obj_store__3_istr_index_6" on "obj_store__3" ("istr_6_td", "istr_6");
create index "obj_store__3_inum_index_3" on "obj_store__3" ("inum_3_td", "inum_3");
create index "obj_store__3_its_index_3" on "obj_store__3" ("its_3_td", "its_3");
create index "obj_store__3_idbl_index_3" on "obj_store__3" ("idbl_3_td", "idbl_3");
create index "obj_store__3_istr_index_7" on "obj_store__3" ("istr_7_td", "istr_7");
create index "obj_store__3_istr_index_8" on "obj_store__3" ("istr_8_td", "istr_8");
create index "obj_store__3_inum_index_4" on "obj_store__3" ("inum_4_td", "inum_4");
create index "obj_store__3_its_index_4" on "obj_store__3" ("its_4_td", "its_4");
create index "obj_store__3_idbl_index_4" on "obj_store__3" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__4" cascade;
create table "obj_store__4"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__4_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__4_ustr_unique_1" on "obj_store__4" ("ustr_1_td", "ustr_1");
create unique index "obj_store__4_unum_unique_1" on "obj_store__4" ("unum_1_td", "unum_1");
create unique index "obj_store__4_uts_unique_1" on "obj_store__4" ("uts_1_td", "uts_1");
create unique index "obj_store__4_udbl_unique_1" on "obj_store__4" ("udbl_1_td", "udbl_1");
create unique index "obj_store__4_ustr_unique_2" on "obj_store__4" ("ustr_2_td", "ustr_2");
create unique index "obj_store__4_unum_unique_2" on "obj_store__4" ("unum_2_td", "unum_2");
create unique index "obj_store__4_uts_unique_2" on "obj_store__4" ("uts_2_td", "uts_2");
create unique index "obj_store__4_udbl_unique_2" on "obj_store__4" ("udbl_2_td", "udbl_2");
create index "obj_store__4_istr_index_1" on "obj_store__4" ("istr_1_td", "istr_1");
create index "obj_store__4_istr_index_2" on "obj_store__4" ("istr_2_td", "istr_2");
create index "obj_store__4_inum_index_1" on "obj_store__4" ("inum_1_td", "inum_1");
create index "obj_store__4_its_index_1" on "obj_store__4" ("its_1_td", "its_1");
create index "obj_store__4_idbl_index_1" on "obj_store__4" ("idbl_1_td", "idbl_1");
create index "obj_store__4_istr_index_3" on "obj_store__4" ("istr_3_td", "istr_3");
create index "obj_store__4_istr_index_4" on "obj_store__4" ("istr_4_td", "istr_4");
create index "obj_store__4_inum_index_2" on "obj_store__4" ("inum_2_td", "inum_2");
create index "obj_store__4_its_index_2" on "obj_store__4" ("its_2_td", "its_2");
create index "obj_store__4_idbl_index_2" on "obj_store__4" ("idbl_2_td", "idbl_2");
create index "obj_store__4_istr_index_5" on "obj_store__4" ("istr_5_td", "istr_5");
create index "obj_store__4_istr_index_6" on "obj_store__4" ("istr_6_td", "istr_6");
create index "obj_store__4_inum_index_3" on "obj_store__4" ("inum_3_td", "inum_3");
create index "obj_store__4_its_index_3" on "obj_store__4" ("its_3_td", "its_3");
create index "obj_store__4_idbl_index_3" on "obj_store__4" ("idbl_3_td", "idbl_3");
create index "obj_store__4_istr_index_7" on "obj_store__4" ("istr_7_td", "istr_7");
create index "obj_store__4_istr_index_8" on "obj_store__4" ("istr_8_td", "istr_8");
create index "obj_store__4_inum_index_4" on "obj_store__4" ("inum_4_td", "inum_4");
create index "obj_store__4_its_index_4" on "obj_store__4" ("its_4_td", "its_4");
create index "obj_store__4_idbl_index_4" on "obj_store__4" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__5" cascade;
create table "obj_store__5"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__5_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__5_ustr_unique_1" on "obj_store__5" ("ustr_1_td", "ustr_1");
create unique index "obj_store__5_unum_unique_1" on "obj_store__5" ("unum_1_td", "unum_1");
create unique index "obj_store__5_uts_unique_1" on "obj_store__5" ("uts_1_td", "uts_1");
create unique index "obj_store__5_udbl_unique_1" on "obj_store__5" ("udbl_1_td", "udbl_1");
create unique index "obj_store__5_ustr_unique_2" on "obj_store__5" ("ustr_2_td", "ustr_2");
create unique index "obj_store__5_unum_unique_2" on "obj_store__5" ("unum_2_td", "unum_2");
create unique index "obj_store__5_uts_unique_2" on "obj_store__5" ("uts_2_td", "uts_2");
create unique index "obj_store__5_udbl_unique_2" on "obj_store__5" ("udbl_2_td", "udbl_2");
create index "obj_store__5_istr_index_1" on "obj_store__5" ("istr_1_td", "istr_1");
create index "obj_store__5_istr_index_2" on "obj_store__5" ("istr_2_td", "istr_2");
create index "obj_store__5_inum_index_1" on "obj_store__5" ("inum_1_td", "inum_1");
create index "obj_store__5_its_index_1" on "obj_store__5" ("its_1_td", "its_1");
create index "obj_store__5_idbl_index_1" on "obj_store__5" ("idbl_1_td", "idbl_1");
create index "obj_store__5_istr_index_3" on "obj_store__5" ("istr_3_td", "istr_3");
create index "obj_store__5_istr_index_4" on "obj_store__5" ("istr_4_td", "istr_4");
create index "obj_store__5_inum_index_2" on "obj_store__5" ("inum_2_td", "inum_2");
create index "obj_store__5_its_index_2" on "obj_store__5" ("its_2_td", "its_2");
create index "obj_store__5_idbl_index_2" on "obj_store__5" ("idbl_2_td", "idbl_2");
create index "obj_store__5_istr_index_5" on "obj_store__5" ("istr_5_td", "istr_5");
create index "obj_store__5_istr_index_6" on "obj_store__5" ("istr_6_td", "istr_6");
create index "obj_store__5_inum_index_3" on "obj_store__5" ("inum_3_td", "inum_3");
create index "obj_store__5_its_index_3" on "obj_store__5" ("its_3_td", "its_3");
create index "obj_store__5_idbl_index_3" on "obj_store__5" ("idbl_3_td", "idbl_3");
create index "obj_store__5_istr_index_7" on "obj_store__5" ("istr_7_td", "istr_7");
create index "obj_store__5_istr_index_8" on "obj_store__5" ("istr_8_td", "istr_8");
create index "obj_store__5_inum_index_4" on "obj_store__5" ("inum_4_td", "inum_4");
create index "obj_store__5_its_index_4" on "obj_store__5" ("its_4_td", "its_4");
create index "obj_store__5_idbl_index_4" on "obj_store__5" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__6" cascade;
create table "obj_store__6"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__6_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__6_ustr_unique_1" on "obj_store__6" ("ustr_1_td", "ustr_1");
create unique index "obj_store__6_unum_unique_1" on "obj_store__6" ("unum_1_td", "unum_1");
create unique index "obj_store__6_uts_unique_1" on "obj_store__6" ("uts_1_td", "uts_1");
create unique index "obj_store__6_udbl_unique_1" on "obj_store__6" ("udbl_1_td", "udbl_1");
create unique index "obj_store__6_ustr_unique_2" on "obj_store__6" ("ustr_2_td", "ustr_2");
create unique index "obj_store__6_unum_unique_2" on "obj_store__6" ("unum_2_td", "unum_2");
create unique index "obj_store__6_uts_unique_2" on "obj_store__6" ("uts_2_td", "uts_2");
create unique index "obj_store__6_udbl_unique_2" on "obj_store__6" ("udbl_2_td", "udbl_2");
create index "obj_store__6_istr_index_1" on "obj_store__6" ("istr_1_td", "istr_1");
create index "obj_store__6_istr_index_2" on "obj_store__6" ("istr_2_td", "istr_2");
create index "obj_store__6_inum_index_1" on "obj_store__6" ("inum_1_td", "inum_1");
create index "obj_store__6_its_index_1" on "obj_store__6" ("its_1_td", "its_1");
create index "obj_store__6_idbl_index_1" on "obj_store__6" ("idbl_1_td", "idbl_1");
create index "obj_store__6_istr_index_3" on "obj_store__6" ("istr_3_td", "istr_3");
create index "obj_store__6_istr_index_4" on "obj_store__6" ("istr_4_td", "istr_4");
create index "obj_store__6_inum_index_2" on "obj_store__6" ("inum_2_td", "inum_2");
create index "obj_store__6_its_index_2" on "obj_store__6" ("its_2_td", "its_2");
create index "obj_store__6_idbl_index_2" on "obj_store__6" ("idbl_2_td", "idbl_2");
create index "obj_store__6_istr_index_5" on "obj_store__6" ("istr_5_td", "istr_5");
create index "obj_store__6_istr_index_6" on "obj_store__6" ("istr_6_td", "istr_6");
create index "obj_store__6_inum_index_3" on "obj_store__6" ("inum_3_td", "inum_3");
create index "obj_store__6_its_index_3" on "obj_store__6" ("its_3_td", "its_3");
create index "obj_store__6_idbl_index_3" on "obj_store__6" ("idbl_3_td", "idbl_3");
create index "obj_store__6_istr_index_7" on "obj_store__6" ("istr_7_td", "istr_7");
create index "obj_store__6_istr_index_8" on "obj_store__6" ("istr_8_td", "istr_8");
create index "obj_store__6_inum_index_4" on "obj_store__6" ("inum_4_td", "inum_4");
create index "obj_store__6_its_index_4" on "obj_store__6" ("its_4_td", "its_4");
create index "obj_store__6_idbl_index_4" on "obj_store__6" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__7" cascade;
create table "obj_store__7"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__7_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__7_ustr_unique_1" on "obj_store__7" ("ustr_1_td", "ustr_1");
create unique index "obj_store__7_unum_unique_1" on "obj_store__7" ("unum_1_td", "unum_1");
create unique index "obj_store__7_uts_unique_1" on "obj_store__7" ("uts_1_td", "uts_1");
create unique index "obj_store__7_udbl_unique_1" on "obj_store__7" ("udbl_1_td", "udbl_1");
create unique index "obj_store__7_ustr_unique_2" on "obj_store__7" ("ustr_2_td", "ustr_2");
create unique index "obj_store__7_unum_unique_2" on "obj_store__7" ("unum_2_td", "unum_2");
create unique index "obj_store__7_uts_unique_2" on "obj_store__7" ("uts_2_td", "uts_2");
create unique index "obj_store__7_udbl_unique_2" on "obj_store__7" ("udbl_2_td", "udbl_2");
create index "obj_store__7_istr_index_1" on "obj_store__7" ("istr_1_td", "istr_1");
create index "obj_store__7_istr_index_2" on "obj_store__7" ("istr_2_td", "istr_2");
create index "obj_store__7_inum_index_1" on "obj_store__7" ("inum_1_td", "inum_1");
create index "obj_store__7_its_index_1" on "obj_store__7" ("its_1_td", "its_1");
create index "obj_store__7_idbl_index_1" on "obj_store__7" ("idbl_1_td", "idbl_1");
create index "obj_store__7_istr_index_3" on "obj_store__7" ("istr_3_td", "istr_3");
create index "obj_store__7_istr_index_4" on "obj_store__7" ("istr_4_td", "istr_4");
create index "obj_store__7_inum_index_2" on "obj_store__7" ("inum_2_td", "inum_2");
create index "obj_store__7_its_index_2" on "obj_store__7" ("its_2_td", "its_2");
create index "obj_store__7_idbl_index_2" on "obj_store__7" ("idbl_2_td", "idbl_2");
create index "obj_store__7_istr_index_5" on "obj_store__7" ("istr_5_td", "istr_5");
create index "obj_store__7_istr_index_6" on "obj_store__7" ("istr_6_td", "istr_6");
create index "obj_store__7_inum_index_3" on "obj_store__7" ("inum_3_td", "inum_3");
create index "obj_store__7_its_index_3" on "obj_store__7" ("its_3_td", "its_3");
create index "obj_store__7_idbl_index_3" on "obj_store__7" ("idbl_3_td", "idbl_3");
create index "obj_store__7_istr_index_7" on "obj_store__7" ("istr_7_td", "istr_7");
create index "obj_store__7_istr_index_8" on "obj_store__7" ("istr_8_td", "istr_8");
create index "obj_store__7_inum_index_4" on "obj_store__7" ("inum_4_td", "inum_4");
create index "obj_store__7_its_index_4" on "obj_store__7" ("its_4_td", "its_4");
create index "obj_store__7_idbl_index_4" on "obj_store__7" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__8" cascade;
create table "obj_store__8"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__8_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__8_ustr_unique_1" on "obj_store__8" ("ustr_1_td", "ustr_1");
create unique index "obj_store__8_unum_unique_1" on "obj_store__8" ("unum_1_td", "unum_1");
create unique index "obj_store__8_uts_unique_1" on "obj_store__8" ("uts_1_td", "uts_1");
create unique index "obj_store__8_udbl_unique_1" on "obj_store__8" ("udbl_1_td", "udbl_1");
create unique index "obj_store__8_ustr_unique_2" on "obj_store__8" ("ustr_2_td", "ustr_2");
create unique index "obj_store__8_unum_unique_2" on "obj_store__8" ("unum_2_td", "unum_2");
create unique index "obj_store__8_uts_unique_2" on "obj_store__8" ("uts_2_td", "uts_2");
create unique index "obj_store__8_udbl_unique_2" on "obj_store__8" ("udbl_2_td", "udbl_2");
create index "obj_store__8_istr_index_1" on "obj_store__8" ("istr_1_td", "istr_1");
create index "obj_store__8_istr_index_2" on "obj_store__8" ("istr_2_td", "istr_2");
create index "obj_store__8_inum_index_1" on "obj_store__8" ("inum_1_td", "inum_1");
create index "obj_store__8_its_index_1" on "obj_store__8" ("its_1_td", "its_1");
create index "obj_store__8_idbl_index_1" on "obj_store__8" ("idbl_1_td", "idbl_1");
create index "obj_store__8_istr_index_3" on "obj_store__8" ("istr_3_td", "istr_3");
create index "obj_store__8_istr_index_4" on "obj_store__8" ("istr_4_td", "istr_4");
create index "obj_store__8_inum_index_2" on "obj_store__8" ("inum_2_td", "inum_2");
create index "obj_store__8_its_index_2" on "obj_store__8" ("its_2_td", "its_2");
create index "obj_store__8_idbl_index_2" on "obj_store__8" ("idbl_2_td", "idbl_2");
create index "obj_store__8_istr_index_5" on "obj_store__8" ("istr_5_td", "istr_5");
create index "obj_store__8_istr_index_6" on "obj_store__8" ("istr_6_td", "istr_6");
create index "obj_store__8_inum_index_3" on "obj_store__8" ("inum_3_td", "inum_3");
create index "obj_store__8_its_index_3" on "obj_store__8" ("its_3_td", "its_3");
create index "obj_store__8_idbl_index_3" on "obj_store__8" ("idbl_3_td", "idbl_3");
create index "obj_store__8_istr_index_7" on "obj_store__8" ("istr_7_td", "istr_7");
create index "obj_store__8_istr_index_8" on "obj_store__8" ("istr_8_td", "istr_8");
create index "obj_store__8_inum_index_4" on "obj_store__8" ("inum_4_td", "inum_4");
create index "obj_store__8_its_index_4" on "obj_store__8" ("its_4_td", "its_4");
create index "obj_store__8_idbl_index_4" on "obj_store__8" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__9" cascade;
create table "obj_store__9"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__9_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__9_ustr_unique_1" on "obj_store__9" ("ustr_1_td", "ustr_1");
create unique index "obj_store__9_unum_unique_1" on "obj_store__9" ("unum_1_td", "unum_1");
create unique index "obj_store__9_uts_unique_1" on "obj_store__9" ("uts_1_td", "uts_1");
create unique index "obj_store__9_udbl_unique_1" on "obj_store__9" ("udbl_1_td", "udbl_1");
create unique index "obj_store__9_ustr_unique_2" on "obj_store__9" ("ustr_2_td", "ustr_2");
create unique index "obj_store__9_unum_unique_2" on "obj_store__9" ("unum_2_td", "unum_2");
create unique index "obj_store__9_uts_unique_2" on "obj_store__9" ("uts_2_td", "uts_2");
create unique index "obj_store__9_udbl_unique_2" on "obj_store__9" ("udbl_2_td", "udbl_2");
create index "obj_store__9_istr_index_1" on "obj_store__9" ("istr_1_td", "istr_1");
create index "obj_store__9_istr_index_2" on "obj_store__9" ("istr_2_td", "istr_2");
create index "obj_store__9_inum_index_1" on "obj_store__9" ("inum_1_td", "inum_1");
create index "obj_store__9_its_index_1" on "obj_store__9" ("its_1_td", "its_1");
create index "obj_store__9_idbl_index_1" on "obj_store__9" ("idbl_1_td", "idbl_1");
create index "obj_store__9_istr_index_3" on "obj_store__9" ("istr_3_td", "istr_3");
create index "obj_store__9_istr_index_4" on "obj_store__9" ("istr_4_td", "istr_4");
create index "obj_store__9_inum_index_2" on "obj_store__9" ("inum_2_td", "inum_2");
create index "obj_store__9_its_index_2" on "obj_store__9" ("its_2_td", "its_2");
create index "obj_store__9_idbl_index_2" on "obj_store__9" ("idbl_2_td", "idbl_2");
create index "obj_store__9_istr_index_5" on "obj_store__9" ("istr_5_td", "istr_5");
create index "obj_store__9_istr_index_6" on "obj_store__9" ("istr_6_td", "istr_6");
create index "obj_store__9_inum_index_3" on "obj_store__9" ("inum_3_td", "inum_3");
create index "obj_store__9_its_index_3" on "obj_store__9" ("its_3_td", "its_3");
create index "obj_store__9_idbl_index_3" on "obj_store__9" ("idbl_3_td", "idbl_3");
create index "obj_store__9_istr_index_7" on "obj_store__9" ("istr_7_td", "istr_7");
create index "obj_store__9_istr_index_8" on "obj_store__9" ("istr_8_td", "istr_8");
create index "obj_store__9_inum_index_4" on "obj_store__9" ("inum_4_td", "inum_4");
create index "obj_store__9_its_index_4" on "obj_store__9" ("its_4_td", "its_4");
create index "obj_store__9_idbl_index_4" on "obj_store__9" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__10" cascade;
create table "obj_store__10"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__10_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__10_ustr_unique_1" on "obj_store__10" ("ustr_1_td", "ustr_1");
create unique index "obj_store__10_unum_unique_1" on "obj_store__10" ("unum_1_td", "unum_1");
create unique index "obj_store__10_uts_unique_1" on "obj_store__10" ("uts_1_td", "uts_1");
create unique index "obj_store__10_udbl_unique_1" on "obj_store__10" ("udbl_1_td", "udbl_1");
create unique index "obj_store__10_ustr_unique_2" on "obj_store__10" ("ustr_2_td", "ustr_2");
create unique index "obj_store__10_unum_unique_2" on "obj_store__10" ("unum_2_td", "unum_2");
create unique index "obj_store__10_uts_unique_2" on "obj_store__10" ("uts_2_td", "uts_2");
create unique index "obj_store__10_udbl_unique_2" on "obj_store__10" ("udbl_2_td", "udbl_2");
create index "obj_store__10_istr_index_1" on "obj_store__10" ("istr_1_td", "istr_1");
create index "obj_store__10_istr_index_2" on "obj_store__10" ("istr_2_td", "istr_2");
create index "obj_store__10_inum_index_1" on "obj_store__10" ("inum_1_td", "inum_1");
create index "obj_store__10_its_index_1" on "obj_store__10" ("its_1_td", "its_1");
create index "obj_store__10_idbl_index_1" on "obj_store__10" ("idbl_1_td", "idbl_1");
create index "obj_store__10_istr_index_3" on "obj_store__10" ("istr_3_td", "istr_3");
create index "obj_store__10_istr_index_4" on "obj_store__10" ("istr_4_td", "istr_4");
create index "obj_store__10_inum_index_2" on "obj_store__10" ("inum_2_td", "inum_2");
create index "obj_store__10_its_index_2" on "obj_store__10" ("its_2_td", "its_2");
create index "obj_store__10_idbl_index_2" on "obj_store__10" ("idbl_2_td", "idbl_2");
create index "obj_store__10_istr_index_5" on "obj_store__10" ("istr_5_td", "istr_5");
create index "obj_store__10_istr_index_6" on "obj_store__10" ("istr_6_td", "istr_6");
create index "obj_store__10_inum_index_3" on "obj_store__10" ("inum_3_td", "inum_3");
create index "obj_store__10_its_index_3" on "obj_store__10" ("its_3_td", "its_3");
create index "obj_store__10_idbl_index_3" on "obj_store__10" ("idbl_3_td", "idbl_3");
create index "obj_store__10_istr_index_7" on "obj_store__10" ("istr_7_td", "istr_7");
create index "obj_store__10_istr_index_8" on "obj_store__10" ("istr_8_td", "istr_8");
create index "obj_store__10_inum_index_4" on "obj_store__10" ("inum_4_td", "inum_4");
create index "obj_store__10_its_index_4" on "obj_store__10" ("its_4_td", "its_4");
create index "obj_store__10_idbl_index_4" on "obj_store__10" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__11" cascade;
create table "obj_store__11"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__11_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__11_ustr_unique_1" on "obj_store__11" ("ustr_1_td", "ustr_1");
create unique index "obj_store__11_unum_unique_1" on "obj_store__11" ("unum_1_td", "unum_1");
create unique index "obj_store__11_uts_unique_1" on "obj_store__11" ("uts_1_td", "uts_1");
create unique index "obj_store__11_udbl_unique_1" on "obj_store__11" ("udbl_1_td", "udbl_1");
create unique index "obj_store__11_ustr_unique_2" on "obj_store__11" ("ustr_2_td", "ustr_2");
create unique index "obj_store__11_unum_unique_2" on "obj_store__11" ("unum_2_td", "unum_2");
create unique index "obj_store__11_uts_unique_2" on "obj_store__11" ("uts_2_td", "uts_2");
create unique index "obj_store__11_udbl_unique_2" on "obj_store__11" ("udbl_2_td", "udbl_2");
create index "obj_store__11_istr_index_1" on "obj_store__11" ("istr_1_td", "istr_1");
create index "obj_store__11_istr_index_2" on "obj_store__11" ("istr_2_td", "istr_2");
create index "obj_store__11_inum_index_1" on "obj_store__11" ("inum_1_td", "inum_1");
create index "obj_store__11_its_index_1" on "obj_store__11" ("its_1_td", "its_1");
create index "obj_store__11_idbl_index_1" on "obj_store__11" ("idbl_1_td", "idbl_1");
create index "obj_store__11_istr_index_3" on "obj_store__11" ("istr_3_td", "istr_3");
create index "obj_store__11_istr_index_4" on "obj_store__11" ("istr_4_td", "istr_4");
create index "obj_store__11_inum_index_2" on "obj_store__11" ("inum_2_td", "inum_2");
create index "obj_store__11_its_index_2" on "obj_store__11" ("its_2_td", "its_2");
create index "obj_store__11_idbl_index_2" on "obj_store__11" ("idbl_2_td", "idbl_2");
create index "obj_store__11_istr_index_5" on "obj_store__11" ("istr_5_td", "istr_5");
create index "obj_store__11_istr_index_6" on "obj_store__11" ("istr_6_td", "istr_6");
create index "obj_store__11_inum_index_3" on "obj_store__11" ("inum_3_td", "inum_3");
create index "obj_store__11_its_index_3" on "obj_store__11" ("its_3_td", "its_3");
create index "obj_store__11_idbl_index_3" on "obj_store__11" ("idbl_3_td", "idbl_3");
create index "obj_store__11_istr_index_7" on "obj_store__11" ("istr_7_td", "istr_7");
create index "obj_store__11_istr_index_8" on "obj_store__11" ("istr_8_td", "istr_8");
create index "obj_store__11_inum_index_4" on "obj_store__11" ("inum_4_td", "inum_4");
create index "obj_store__11_its_index_4" on "obj_store__11" ("its_4_td", "its_4");
create index "obj_store__11_idbl_index_4" on "obj_store__11" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__12" cascade;
create table "obj_store__12"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__12_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__12_ustr_unique_1" on "obj_store__12" ("ustr_1_td", "ustr_1");
create unique index "obj_store__12_unum_unique_1" on "obj_store__12" ("unum_1_td", "unum_1");
create unique index "obj_store__12_uts_unique_1" on "obj_store__12" ("uts_1_td", "uts_1");
create unique index "obj_store__12_udbl_unique_1" on "obj_store__12" ("udbl_1_td", "udbl_1");
create unique index "obj_store__12_ustr_unique_2" on "obj_store__12" ("ustr_2_td", "ustr_2");
create unique index "obj_store__12_unum_unique_2" on "obj_store__12" ("unum_2_td", "unum_2");
create unique index "obj_store__12_uts_unique_2" on "obj_store__12" ("uts_2_td", "uts_2");
create unique index "obj_store__12_udbl_unique_2" on "obj_store__12" ("udbl_2_td", "udbl_2");
create index "obj_store__12_istr_index_1" on "obj_store__12" ("istr_1_td", "istr_1");
create index "obj_store__12_istr_index_2" on "obj_store__12" ("istr_2_td", "istr_2");
create index "obj_store__12_inum_index_1" on "obj_store__12" ("inum_1_td", "inum_1");
create index "obj_store__12_its_index_1" on "obj_store__12" ("its_1_td", "its_1");
create index "obj_store__12_idbl_index_1" on "obj_store__12" ("idbl_1_td", "idbl_1");
create index "obj_store__12_istr_index_3" on "obj_store__12" ("istr_3_td", "istr_3");
create index "obj_store__12_istr_index_4" on "obj_store__12" ("istr_4_td", "istr_4");
create index "obj_store__12_inum_index_2" on "obj_store__12" ("inum_2_td", "inum_2");
create index "obj_store__12_its_index_2" on "obj_store__12" ("its_2_td", "its_2");
create index "obj_store__12_idbl_index_2" on "obj_store__12" ("idbl_2_td", "idbl_2");
create index "obj_store__12_istr_index_5" on "obj_store__12" ("istr_5_td", "istr_5");
create index "obj_store__12_istr_index_6" on "obj_store__12" ("istr_6_td", "istr_6");
create index "obj_store__12_inum_index_3" on "obj_store__12" ("inum_3_td", "inum_3");
create index "obj_store__12_its_index_3" on "obj_store__12" ("its_3_td", "its_3");
create index "obj_store__12_idbl_index_3" on "obj_store__12" ("idbl_3_td", "idbl_3");
create index "obj_store__12_istr_index_7" on "obj_store__12" ("istr_7_td", "istr_7");
create index "obj_store__12_istr_index_8" on "obj_store__12" ("istr_8_td", "istr_8");
create index "obj_store__12_inum_index_4" on "obj_store__12" ("inum_4_td", "inum_4");
create index "obj_store__12_its_index_4" on "obj_store__12" ("its_4_td", "its_4");
create index "obj_store__12_idbl_index_4" on "obj_store__12" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__13" cascade;
create table "obj_store__13"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__13_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__13_ustr_unique_1" on "obj_store__13" ("ustr_1_td", "ustr_1");
create unique index "obj_store__13_unum_unique_1" on "obj_store__13" ("unum_1_td", "unum_1");
create unique index "obj_store__13_uts_unique_1" on "obj_store__13" ("uts_1_td", "uts_1");
create unique index "obj_store__13_udbl_unique_1" on "obj_store__13" ("udbl_1_td", "udbl_1");
create unique index "obj_store__13_ustr_unique_2" on "obj_store__13" ("ustr_2_td", "ustr_2");
create unique index "obj_store__13_unum_unique_2" on "obj_store__13" ("unum_2_td", "unum_2");
create unique index "obj_store__13_uts_unique_2" on "obj_store__13" ("uts_2_td", "uts_2");
create unique index "obj_store__13_udbl_unique_2" on "obj_store__13" ("udbl_2_td", "udbl_2");
create index "obj_store__13_istr_index_1" on "obj_store__13" ("istr_1_td", "istr_1");
create index "obj_store__13_istr_index_2" on "obj_store__13" ("istr_2_td", "istr_2");
create index "obj_store__13_inum_index_1" on "obj_store__13" ("inum_1_td", "inum_1");
create index "obj_store__13_its_index_1" on "obj_store__13" ("its_1_td", "its_1");
create index "obj_store__13_idbl_index_1" on "obj_store__13" ("idbl_1_td", "idbl_1");
create index "obj_store__13_istr_index_3" on "obj_store__13" ("istr_3_td", "istr_3");
create index "obj_store__13_istr_index_4" on "obj_store__13" ("istr_4_td", "istr_4");
create index "obj_store__13_inum_index_2" on "obj_store__13" ("inum_2_td", "inum_2");
create index "obj_store__13_its_index_2" on "obj_store__13" ("its_2_td", "its_2");
create index "obj_store__13_idbl_index_2" on "obj_store__13" ("idbl_2_td", "idbl_2");
create index "obj_store__13_istr_index_5" on "obj_store__13" ("istr_5_td", "istr_5");
create index "obj_store__13_istr_index_6" on "obj_store__13" ("istr_6_td", "istr_6");
create index "obj_store__13_inum_index_3" on "obj_store__13" ("inum_3_td", "inum_3");
create index "obj_store__13_its_index_3" on "obj_store__13" ("its_3_td", "its_3");
create index "obj_store__13_idbl_index_3" on "obj_store__13" ("idbl_3_td", "idbl_3");
create index "obj_store__13_istr_index_7" on "obj_store__13" ("istr_7_td", "istr_7");
create index "obj_store__13_istr_index_8" on "obj_store__13" ("istr_8_td", "istr_8");
create index "obj_store__13_inum_index_4" on "obj_store__13" ("inum_4_td", "inum_4");
create index "obj_store__13_its_index_4" on "obj_store__13" ("its_4_td", "its_4");
create index "obj_store__13_idbl_index_4" on "obj_store__13" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__14" cascade;
create table "obj_store__14"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__14_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__14_ustr_unique_1" on "obj_store__14" ("ustr_1_td", "ustr_1");
create unique index "obj_store__14_unum_unique_1" on "obj_store__14" ("unum_1_td", "unum_1");
create unique index "obj_store__14_uts_unique_1" on "obj_store__14" ("uts_1_td", "uts_1");
create unique index "obj_store__14_udbl_unique_1" on "obj_store__14" ("udbl_1_td", "udbl_1");
create unique index "obj_store__14_ustr_unique_2" on "obj_store__14" ("ustr_2_td", "ustr_2");
create unique index "obj_store__14_unum_unique_2" on "obj_store__14" ("unum_2_td", "unum_2");
create unique index "obj_store__14_uts_unique_2" on "obj_store__14" ("uts_2_td", "uts_2");
create unique index "obj_store__14_udbl_unique_2" on "obj_store__14" ("udbl_2_td", "udbl_2");
create index "obj_store__14_istr_index_1" on "obj_store__14" ("istr_1_td", "istr_1");
create index "obj_store__14_istr_index_2" on "obj_store__14" ("istr_2_td", "istr_2");
create index "obj_store__14_inum_index_1" on "obj_store__14" ("inum_1_td", "inum_1");
create index "obj_store__14_its_index_1" on "obj_store__14" ("its_1_td", "its_1");
create index "obj_store__14_idbl_index_1" on "obj_store__14" ("idbl_1_td", "idbl_1");
create index "obj_store__14_istr_index_3" on "obj_store__14" ("istr_3_td", "istr_3");
create index "obj_store__14_istr_index_4" on "obj_store__14" ("istr_4_td", "istr_4");
create index "obj_store__14_inum_index_2" on "obj_store__14" ("inum_2_td", "inum_2");
create index "obj_store__14_its_index_2" on "obj_store__14" ("its_2_td", "its_2");
create index "obj_store__14_idbl_index_2" on "obj_store__14" ("idbl_2_td", "idbl_2");
create index "obj_store__14_istr_index_5" on "obj_store__14" ("istr_5_td", "istr_5");
create index "obj_store__14_istr_index_6" on "obj_store__14" ("istr_6_td", "istr_6");
create index "obj_store__14_inum_index_3" on "obj_store__14" ("inum_3_td", "inum_3");
create index "obj_store__14_its_index_3" on "obj_store__14" ("its_3_td", "its_3");
create index "obj_store__14_idbl_index_3" on "obj_store__14" ("idbl_3_td", "idbl_3");
create index "obj_store__14_istr_index_7" on "obj_store__14" ("istr_7_td", "istr_7");
create index "obj_store__14_istr_index_8" on "obj_store__14" ("istr_8_td", "istr_8");
create index "obj_store__14_inum_index_4" on "obj_store__14" ("inum_4_td", "inum_4");
create index "obj_store__14_its_index_4" on "obj_store__14" ("its_4_td", "its_4");
create index "obj_store__14_idbl_index_4" on "obj_store__14" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__15" cascade;
create table "obj_store__15"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__15_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__15_ustr_unique_1" on "obj_store__15" ("ustr_1_td", "ustr_1");
create unique index "obj_store__15_unum_unique_1" on "obj_store__15" ("unum_1_td", "unum_1");
create unique index "obj_store__15_uts_unique_1" on "obj_store__15" ("uts_1_td", "uts_1");
create unique index "obj_store__15_udbl_unique_1" on "obj_store__15" ("udbl_1_td", "udbl_1");
create unique index "obj_store__15_ustr_unique_2" on "obj_store__15" ("ustr_2_td", "ustr_2");
create unique index "obj_store__15_unum_unique_2" on "obj_store__15" ("unum_2_td", "unum_2");
create unique index "obj_store__15_uts_unique_2" on "obj_store__15" ("uts_2_td", "uts_2");
create unique index "obj_store__15_udbl_unique_2" on "obj_store__15" ("udbl_2_td", "udbl_2");
create index "obj_store__15_istr_index_1" on "obj_store__15" ("istr_1_td", "istr_1");
create index "obj_store__15_istr_index_2" on "obj_store__15" ("istr_2_td", "istr_2");
create index "obj_store__15_inum_index_1" on "obj_store__15" ("inum_1_td", "inum_1");
create index "obj_store__15_its_index_1" on "obj_store__15" ("its_1_td", "its_1");
create index "obj_store__15_idbl_index_1" on "obj_store__15" ("idbl_1_td", "idbl_1");
create index "obj_store__15_istr_index_3" on "obj_store__15" ("istr_3_td", "istr_3");
create index "obj_store__15_istr_index_4" on "obj_store__15" ("istr_4_td", "istr_4");
create index "obj_store__15_inum_index_2" on "obj_store__15" ("inum_2_td", "inum_2");
create index "obj_store__15_its_index_2" on "obj_store__15" ("its_2_td", "its_2");
create index "obj_store__15_idbl_index_2" on "obj_store__15" ("idbl_2_td", "idbl_2");
create index "obj_store__15_istr_index_5" on "obj_store__15" ("istr_5_td", "istr_5");
create index "obj_store__15_istr_index_6" on "obj_store__15" ("istr_6_td", "istr_6");
create index "obj_store__15_inum_index_3" on "obj_store__15" ("inum_3_td", "inum_3");
create index "obj_store__15_its_index_3" on "obj_store__15" ("its_3_td", "its_3");
create index "obj_store__15_idbl_index_3" on "obj_store__15" ("idbl_3_td", "idbl_3");
create index "obj_store__15_istr_index_7" on "obj_store__15" ("istr_7_td", "istr_7");
create index "obj_store__15_istr_index_8" on "obj_store__15" ("istr_8_td", "istr_8");
create index "obj_store__15_inum_index_4" on "obj_store__15" ("inum_4_td", "inum_4");
create index "obj_store__15_its_index_4" on "obj_store__15" ("its_4_td", "its_4");
create index "obj_store__15_idbl_index_4" on "obj_store__15" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__16" cascade;
create table "obj_store__16"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__16_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__16_ustr_unique_1" on "obj_store__16" ("ustr_1_td", "ustr_1");
create unique index "obj_store__16_unum_unique_1" on "obj_store__16" ("unum_1_td", "unum_1");
create unique index "obj_store__16_uts_unique_1" on "obj_store__16" ("uts_1_td", "uts_1");
create unique index "obj_store__16_udbl_unique_1" on "obj_store__16" ("udbl_1_td", "udbl_1");
create unique index "obj_store__16_ustr_unique_2" on "obj_store__16" ("ustr_2_td", "ustr_2");
create unique index "obj_store__16_unum_unique_2" on "obj_store__16" ("unum_2_td", "unum_2");
create unique index "obj_store__16_uts_unique_2" on "obj_store__16" ("uts_2_td", "uts_2");
create unique index "obj_store__16_udbl_unique_2" on "obj_store__16" ("udbl_2_td", "udbl_2");
create index "obj_store__16_istr_index_1" on "obj_store__16" ("istr_1_td", "istr_1");
create index "obj_store__16_istr_index_2" on "obj_store__16" ("istr_2_td", "istr_2");
create index "obj_store__16_inum_index_1" on "obj_store__16" ("inum_1_td", "inum_1");
create index "obj_store__16_its_index_1" on "obj_store__16" ("its_1_td", "its_1");
create index "obj_store__16_idbl_index_1" on "obj_store__16" ("idbl_1_td", "idbl_1");
create index "obj_store__16_istr_index_3" on "obj_store__16" ("istr_3_td", "istr_3");
create index "obj_store__16_istr_index_4" on "obj_store__16" ("istr_4_td", "istr_4");
create index "obj_store__16_inum_index_2" on "obj_store__16" ("inum_2_td", "inum_2");
create index "obj_store__16_its_index_2" on "obj_store__16" ("its_2_td", "its_2");
create index "obj_store__16_idbl_index_2" on "obj_store__16" ("idbl_2_td", "idbl_2");
create index "obj_store__16_istr_index_5" on "obj_store__16" ("istr_5_td", "istr_5");
create index "obj_store__16_istr_index_6" on "obj_store__16" ("istr_6_td", "istr_6");
create index "obj_store__16_inum_index_3" on "obj_store__16" ("inum_3_td", "inum_3");
create index "obj_store__16_its_index_3" on "obj_store__16" ("its_3_td", "its_3");
create index "obj_store__16_idbl_index_3" on "obj_store__16" ("idbl_3_td", "idbl_3");
create index "obj_store__16_istr_index_7" on "obj_store__16" ("istr_7_td", "istr_7");
create index "obj_store__16_istr_index_8" on "obj_store__16" ("istr_8_td", "istr_8");
create index "obj_store__16_inum_index_4" on "obj_store__16" ("inum_4_td", "inum_4");
create index "obj_store__16_its_index_4" on "obj_store__16" ("its_4_td", "its_4");
create index "obj_store__16_idbl_index_4" on "obj_store__16" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__17" cascade;
create table "obj_store__17"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__17_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__17_ustr_unique_1" on "obj_store__17" ("ustr_1_td", "ustr_1");
create unique index "obj_store__17_unum_unique_1" on "obj_store__17" ("unum_1_td", "unum_1");
create unique index "obj_store__17_uts_unique_1" on "obj_store__17" ("uts_1_td", "uts_1");
create unique index "obj_store__17_udbl_unique_1" on "obj_store__17" ("udbl_1_td", "udbl_1");
create unique index "obj_store__17_ustr_unique_2" on "obj_store__17" ("ustr_2_td", "ustr_2");
create unique index "obj_store__17_unum_unique_2" on "obj_store__17" ("unum_2_td", "unum_2");
create unique index "obj_store__17_uts_unique_2" on "obj_store__17" ("uts_2_td", "uts_2");
create unique index "obj_store__17_udbl_unique_2" on "obj_store__17" ("udbl_2_td", "udbl_2");
create index "obj_store__17_istr_index_1" on "obj_store__17" ("istr_1_td", "istr_1");
create index "obj_store__17_istr_index_2" on "obj_store__17" ("istr_2_td", "istr_2");
create index "obj_store__17_inum_index_1" on "obj_store__17" ("inum_1_td", "inum_1");
create index "obj_store__17_its_index_1" on "obj_store__17" ("its_1_td", "its_1");
create index "obj_store__17_idbl_index_1" on "obj_store__17" ("idbl_1_td", "idbl_1");
create index "obj_store__17_istr_index_3" on "obj_store__17" ("istr_3_td", "istr_3");
create index "obj_store__17_istr_index_4" on "obj_store__17" ("istr_4_td", "istr_4");
create index "obj_store__17_inum_index_2" on "obj_store__17" ("inum_2_td", "inum_2");
create index "obj_store__17_its_index_2" on "obj_store__17" ("its_2_td", "its_2");
create index "obj_store__17_idbl_index_2" on "obj_store__17" ("idbl_2_td", "idbl_2");
create index "obj_store__17_istr_index_5" on "obj_store__17" ("istr_5_td", "istr_5");
create index "obj_store__17_istr_index_6" on "obj_store__17" ("istr_6_td", "istr_6");
create index "obj_store__17_inum_index_3" on "obj_store__17" ("inum_3_td", "inum_3");
create index "obj_store__17_its_index_3" on "obj_store__17" ("its_3_td", "its_3");
create index "obj_store__17_idbl_index_3" on "obj_store__17" ("idbl_3_td", "idbl_3");
create index "obj_store__17_istr_index_7" on "obj_store__17" ("istr_7_td", "istr_7");
create index "obj_store__17_istr_index_8" on "obj_store__17" ("istr_8_td", "istr_8");
create index "obj_store__17_inum_index_4" on "obj_store__17" ("inum_4_td", "inum_4");
create index "obj_store__17_its_index_4" on "obj_store__17" ("its_4_td", "its_4");
create index "obj_store__17_idbl_index_4" on "obj_store__17" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__18" cascade;
create table "obj_store__18"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__18_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__18_ustr_unique_1" on "obj_store__18" ("ustr_1_td", "ustr_1");
create unique index "obj_store__18_unum_unique_1" on "obj_store__18" ("unum_1_td", "unum_1");
create unique index "obj_store__18_uts_unique_1" on "obj_store__18" ("uts_1_td", "uts_1");
create unique index "obj_store__18_udbl_unique_1" on "obj_store__18" ("udbl_1_td", "udbl_1");
create unique index "obj_store__18_ustr_unique_2" on "obj_store__18" ("ustr_2_td", "ustr_2");
create unique index "obj_store__18_unum_unique_2" on "obj_store__18" ("unum_2_td", "unum_2");
create unique index "obj_store__18_uts_unique_2" on "obj_store__18" ("uts_2_td", "uts_2");
create unique index "obj_store__18_udbl_unique_2" on "obj_store__18" ("udbl_2_td", "udbl_2");
create index "obj_store__18_istr_index_1" on "obj_store__18" ("istr_1_td", "istr_1");
create index "obj_store__18_istr_index_2" on "obj_store__18" ("istr_2_td", "istr_2");
create index "obj_store__18_inum_index_1" on "obj_store__18" ("inum_1_td", "inum_1");
create index "obj_store__18_its_index_1" on "obj_store__18" ("its_1_td", "its_1");
create index "obj_store__18_idbl_index_1" on "obj_store__18" ("idbl_1_td", "idbl_1");
create index "obj_store__18_istr_index_3" on "obj_store__18" ("istr_3_td", "istr_3");
create index "obj_store__18_istr_index_4" on "obj_store__18" ("istr_4_td", "istr_4");
create index "obj_store__18_inum_index_2" on "obj_store__18" ("inum_2_td", "inum_2");
create index "obj_store__18_its_index_2" on "obj_store__18" ("its_2_td", "its_2");
create index "obj_store__18_idbl_index_2" on "obj_store__18" ("idbl_2_td", "idbl_2");
create index "obj_store__18_istr_index_5" on "obj_store__18" ("istr_5_td", "istr_5");
create index "obj_store__18_istr_index_6" on "obj_store__18" ("istr_6_td", "istr_6");
create index "obj_store__18_inum_index_3" on "obj_store__18" ("inum_3_td", "inum_3");
create index "obj_store__18_its_index_3" on "obj_store__18" ("its_3_td", "its_3");
create index "obj_store__18_idbl_index_3" on "obj_store__18" ("idbl_3_td", "idbl_3");
create index "obj_store__18_istr_index_7" on "obj_store__18" ("istr_7_td", "istr_7");
create index "obj_store__18_istr_index_8" on "obj_store__18" ("istr_8_td", "istr_8");
create index "obj_store__18_inum_index_4" on "obj_store__18" ("inum_4_td", "inum_4");
create index "obj_store__18_its_index_4" on "obj_store__18" ("its_4_td", "its_4");
create index "obj_store__18_idbl_index_4" on "obj_store__18" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__19" cascade;
create table "obj_store__19"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__19_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__19_ustr_unique_1" on "obj_store__19" ("ustr_1_td", "ustr_1");
create unique index "obj_store__19_unum_unique_1" on "obj_store__19" ("unum_1_td", "unum_1");
create unique index "obj_store__19_uts_unique_1" on "obj_store__19" ("uts_1_td", "uts_1");
create unique index "obj_store__19_udbl_unique_1" on "obj_store__19" ("udbl_1_td", "udbl_1");
create unique index "obj_store__19_ustr_unique_2" on "obj_store__19" ("ustr_2_td", "ustr_2");
create unique index "obj_store__19_unum_unique_2" on "obj_store__19" ("unum_2_td", "unum_2");
create unique index "obj_store__19_uts_unique_2" on "obj_store__19" ("uts_2_td", "uts_2");
create unique index "obj_store__19_udbl_unique_2" on "obj_store__19" ("udbl_2_td", "udbl_2");
create index "obj_store__19_istr_index_1" on "obj_store__19" ("istr_1_td", "istr_1");
create index "obj_store__19_istr_index_2" on "obj_store__19" ("istr_2_td", "istr_2");
create index "obj_store__19_inum_index_1" on "obj_store__19" ("inum_1_td", "inum_1");
create index "obj_store__19_its_index_1" on "obj_store__19" ("its_1_td", "its_1");
create index "obj_store__19_idbl_index_1" on "obj_store__19" ("idbl_1_td", "idbl_1");
create index "obj_store__19_istr_index_3" on "obj_store__19" ("istr_3_td", "istr_3");
create index "obj_store__19_istr_index_4" on "obj_store__19" ("istr_4_td", "istr_4");
create index "obj_store__19_inum_index_2" on "obj_store__19" ("inum_2_td", "inum_2");
create index "obj_store__19_its_index_2" on "obj_store__19" ("its_2_td", "its_2");
create index "obj_store__19_idbl_index_2" on "obj_store__19" ("idbl_2_td", "idbl_2");
create index "obj_store__19_istr_index_5" on "obj_store__19" ("istr_5_td", "istr_5");
create index "obj_store__19_istr_index_6" on "obj_store__19" ("istr_6_td", "istr_6");
create index "obj_store__19_inum_index_3" on "obj_store__19" ("inum_3_td", "inum_3");
create index "obj_store__19_its_index_3" on "obj_store__19" ("its_3_td", "its_3");
create index "obj_store__19_idbl_index_3" on "obj_store__19" ("idbl_3_td", "idbl_3");
create index "obj_store__19_istr_index_7" on "obj_store__19" ("istr_7_td", "istr_7");
create index "obj_store__19_istr_index_8" on "obj_store__19" ("istr_8_td", "istr_8");
create index "obj_store__19_inum_index_4" on "obj_store__19" ("inum_4_td", "inum_4");
create index "obj_store__19_its_index_4" on "obj_store__19" ("its_4_td", "its_4");
create index "obj_store__19_idbl_index_4" on "obj_store__19" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__20" cascade;
create table "obj_store__20"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__20_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__20_ustr_unique_1" on "obj_store__20" ("ustr_1_td", "ustr_1");
create unique index "obj_store__20_unum_unique_1" on "obj_store__20" ("unum_1_td", "unum_1");
create unique index "obj_store__20_uts_unique_1" on "obj_store__20" ("uts_1_td", "uts_1");
create unique index "obj_store__20_udbl_unique_1" on "obj_store__20" ("udbl_1_td", "udbl_1");
create unique index "obj_store__20_ustr_unique_2" on "obj_store__20" ("ustr_2_td", "ustr_2");
create unique index "obj_store__20_unum_unique_2" on "obj_store__20" ("unum_2_td", "unum_2");
create unique index "obj_store__20_uts_unique_2" on "obj_store__20" ("uts_2_td", "uts_2");
create unique index "obj_store__20_udbl_unique_2" on "obj_store__20" ("udbl_2_td", "udbl_2");
create index "obj_store__20_istr_index_1" on "obj_store__20" ("istr_1_td", "istr_1");
create index "obj_store__20_istr_index_2" on "obj_store__20" ("istr_2_td", "istr_2");
create index "obj_store__20_inum_index_1" on "obj_store__20" ("inum_1_td", "inum_1");
create index "obj_store__20_its_index_1" on "obj_store__20" ("its_1_td", "its_1");
create index "obj_store__20_idbl_index_1" on "obj_store__20" ("idbl_1_td", "idbl_1");
create index "obj_store__20_istr_index_3" on "obj_store__20" ("istr_3_td", "istr_3");
create index "obj_store__20_istr_index_4" on "obj_store__20" ("istr_4_td", "istr_4");
create index "obj_store__20_inum_index_2" on "obj_store__20" ("inum_2_td", "inum_2");
create index "obj_store__20_its_index_2" on "obj_store__20" ("its_2_td", "its_2");
create index "obj_store__20_idbl_index_2" on "obj_store__20" ("idbl_2_td", "idbl_2");
create index "obj_store__20_istr_index_5" on "obj_store__20" ("istr_5_td", "istr_5");
create index "obj_store__20_istr_index_6" on "obj_store__20" ("istr_6_td", "istr_6");
create index "obj_store__20_inum_index_3" on "obj_store__20" ("inum_3_td", "inum_3");
create index "obj_store__20_its_index_3" on "obj_store__20" ("its_3_td", "its_3");
create index "obj_store__20_idbl_index_3" on "obj_store__20" ("idbl_3_td", "idbl_3");
create index "obj_store__20_istr_index_7" on "obj_store__20" ("istr_7_td", "istr_7");
create index "obj_store__20_istr_index_8" on "obj_store__20" ("istr_8_td", "istr_8");
create index "obj_store__20_inum_index_4" on "obj_store__20" ("inum_4_td", "inum_4");
create index "obj_store__20_its_index_4" on "obj_store__20" ("its_4_td", "its_4");
create index "obj_store__20_idbl_index_4" on "obj_store__20" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__21" cascade;
create table "obj_store__21"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__21_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__21_ustr_unique_1" on "obj_store__21" ("ustr_1_td", "ustr_1");
create unique index "obj_store__21_unum_unique_1" on "obj_store__21" ("unum_1_td", "unum_1");
create unique index "obj_store__21_uts_unique_1" on "obj_store__21" ("uts_1_td", "uts_1");
create unique index "obj_store__21_udbl_unique_1" on "obj_store__21" ("udbl_1_td", "udbl_1");
create unique index "obj_store__21_ustr_unique_2" on "obj_store__21" ("ustr_2_td", "ustr_2");
create unique index "obj_store__21_unum_unique_2" on "obj_store__21" ("unum_2_td", "unum_2");
create unique index "obj_store__21_uts_unique_2" on "obj_store__21" ("uts_2_td", "uts_2");
create unique index "obj_store__21_udbl_unique_2" on "obj_store__21" ("udbl_2_td", "udbl_2");
create index "obj_store__21_istr_index_1" on "obj_store__21" ("istr_1_td", "istr_1");
create index "obj_store__21_istr_index_2" on "obj_store__21" ("istr_2_td", "istr_2");
create index "obj_store__21_inum_index_1" on "obj_store__21" ("inum_1_td", "inum_1");
create index "obj_store__21_its_index_1" on "obj_store__21" ("its_1_td", "its_1");
create index "obj_store__21_idbl_index_1" on "obj_store__21" ("idbl_1_td", "idbl_1");
create index "obj_store__21_istr_index_3" on "obj_store__21" ("istr_3_td", "istr_3");
create index "obj_store__21_istr_index_4" on "obj_store__21" ("istr_4_td", "istr_4");
create index "obj_store__21_inum_index_2" on "obj_store__21" ("inum_2_td", "inum_2");
create index "obj_store__21_its_index_2" on "obj_store__21" ("its_2_td", "its_2");
create index "obj_store__21_idbl_index_2" on "obj_store__21" ("idbl_2_td", "idbl_2");
create index "obj_store__21_istr_index_5" on "obj_store__21" ("istr_5_td", "istr_5");
create index "obj_store__21_istr_index_6" on "obj_store__21" ("istr_6_td", "istr_6");
create index "obj_store__21_inum_index_3" on "obj_store__21" ("inum_3_td", "inum_3");
create index "obj_store__21_its_index_3" on "obj_store__21" ("its_3_td", "its_3");
create index "obj_store__21_idbl_index_3" on "obj_store__21" ("idbl_3_td", "idbl_3");
create index "obj_store__21_istr_index_7" on "obj_store__21" ("istr_7_td", "istr_7");
create index "obj_store__21_istr_index_8" on "obj_store__21" ("istr_8_td", "istr_8");
create index "obj_store__21_inum_index_4" on "obj_store__21" ("inum_4_td", "inum_4");
create index "obj_store__21_its_index_4" on "obj_store__21" ("its_4_td", "its_4");
create index "obj_store__21_idbl_index_4" on "obj_store__21" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__22" cascade;
create table "obj_store__22"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__22_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__22_ustr_unique_1" on "obj_store__22" ("ustr_1_td", "ustr_1");
create unique index "obj_store__22_unum_unique_1" on "obj_store__22" ("unum_1_td", "unum_1");
create unique index "obj_store__22_uts_unique_1" on "obj_store__22" ("uts_1_td", "uts_1");
create unique index "obj_store__22_udbl_unique_1" on "obj_store__22" ("udbl_1_td", "udbl_1");
create unique index "obj_store__22_ustr_unique_2" on "obj_store__22" ("ustr_2_td", "ustr_2");
create unique index "obj_store__22_unum_unique_2" on "obj_store__22" ("unum_2_td", "unum_2");
create unique index "obj_store__22_uts_unique_2" on "obj_store__22" ("uts_2_td", "uts_2");
create unique index "obj_store__22_udbl_unique_2" on "obj_store__22" ("udbl_2_td", "udbl_2");
create index "obj_store__22_istr_index_1" on "obj_store__22" ("istr_1_td", "istr_1");
create index "obj_store__22_istr_index_2" on "obj_store__22" ("istr_2_td", "istr_2");
create index "obj_store__22_inum_index_1" on "obj_store__22" ("inum_1_td", "inum_1");
create index "obj_store__22_its_index_1" on "obj_store__22" ("its_1_td", "its_1");
create index "obj_store__22_idbl_index_1" on "obj_store__22" ("idbl_1_td", "idbl_1");
create index "obj_store__22_istr_index_3" on "obj_store__22" ("istr_3_td", "istr_3");
create index "obj_store__22_istr_index_4" on "obj_store__22" ("istr_4_td", "istr_4");
create index "obj_store__22_inum_index_2" on "obj_store__22" ("inum_2_td", "inum_2");
create index "obj_store__22_its_index_2" on "obj_store__22" ("its_2_td", "its_2");
create index "obj_store__22_idbl_index_2" on "obj_store__22" ("idbl_2_td", "idbl_2");
create index "obj_store__22_istr_index_5" on "obj_store__22" ("istr_5_td", "istr_5");
create index "obj_store__22_istr_index_6" on "obj_store__22" ("istr_6_td", "istr_6");
create index "obj_store__22_inum_index_3" on "obj_store__22" ("inum_3_td", "inum_3");
create index "obj_store__22_its_index_3" on "obj_store__22" ("its_3_td", "its_3");
create index "obj_store__22_idbl_index_3" on "obj_store__22" ("idbl_3_td", "idbl_3");
create index "obj_store__22_istr_index_7" on "obj_store__22" ("istr_7_td", "istr_7");
create index "obj_store__22_istr_index_8" on "obj_store__22" ("istr_8_td", "istr_8");
create index "obj_store__22_inum_index_4" on "obj_store__22" ("inum_4_td", "inum_4");
create index "obj_store__22_its_index_4" on "obj_store__22" ("its_4_td", "its_4");
create index "obj_store__22_idbl_index_4" on "obj_store__22" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__23" cascade;
create table "obj_store__23"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__23_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__23_ustr_unique_1" on "obj_store__23" ("ustr_1_td", "ustr_1");
create unique index "obj_store__23_unum_unique_1" on "obj_store__23" ("unum_1_td", "unum_1");
create unique index "obj_store__23_uts_unique_1" on "obj_store__23" ("uts_1_td", "uts_1");
create unique index "obj_store__23_udbl_unique_1" on "obj_store__23" ("udbl_1_td", "udbl_1");
create unique index "obj_store__23_ustr_unique_2" on "obj_store__23" ("ustr_2_td", "ustr_2");
create unique index "obj_store__23_unum_unique_2" on "obj_store__23" ("unum_2_td", "unum_2");
create unique index "obj_store__23_uts_unique_2" on "obj_store__23" ("uts_2_td", "uts_2");
create unique index "obj_store__23_udbl_unique_2" on "obj_store__23" ("udbl_2_td", "udbl_2");
create index "obj_store__23_istr_index_1" on "obj_store__23" ("istr_1_td", "istr_1");
create index "obj_store__23_istr_index_2" on "obj_store__23" ("istr_2_td", "istr_2");
create index "obj_store__23_inum_index_1" on "obj_store__23" ("inum_1_td", "inum_1");
create index "obj_store__23_its_index_1" on "obj_store__23" ("its_1_td", "its_1");
create index "obj_store__23_idbl_index_1" on "obj_store__23" ("idbl_1_td", "idbl_1");
create index "obj_store__23_istr_index_3" on "obj_store__23" ("istr_3_td", "istr_3");
create index "obj_store__23_istr_index_4" on "obj_store__23" ("istr_4_td", "istr_4");
create index "obj_store__23_inum_index_2" on "obj_store__23" ("inum_2_td", "inum_2");
create index "obj_store__23_its_index_2" on "obj_store__23" ("its_2_td", "its_2");
create index "obj_store__23_idbl_index_2" on "obj_store__23" ("idbl_2_td", "idbl_2");
create index "obj_store__23_istr_index_5" on "obj_store__23" ("istr_5_td", "istr_5");
create index "obj_store__23_istr_index_6" on "obj_store__23" ("istr_6_td", "istr_6");
create index "obj_store__23_inum_index_3" on "obj_store__23" ("inum_3_td", "inum_3");
create index "obj_store__23_its_index_3" on "obj_store__23" ("its_3_td", "its_3");
create index "obj_store__23_idbl_index_3" on "obj_store__23" ("idbl_3_td", "idbl_3");
create index "obj_store__23_istr_index_7" on "obj_store__23" ("istr_7_td", "istr_7");
create index "obj_store__23_istr_index_8" on "obj_store__23" ("istr_8_td", "istr_8");
create index "obj_store__23_inum_index_4" on "obj_store__23" ("inum_4_td", "inum_4");
create index "obj_store__23_its_index_4" on "obj_store__23" ("its_4_td", "its_4");
create index "obj_store__23_idbl_index_4" on "obj_store__23" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__24" cascade;
create table "obj_store__24"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__24_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__24_ustr_unique_1" on "obj_store__24" ("ustr_1_td", "ustr_1");
create unique index "obj_store__24_unum_unique_1" on "obj_store__24" ("unum_1_td", "unum_1");
create unique index "obj_store__24_uts_unique_1" on "obj_store__24" ("uts_1_td", "uts_1");
create unique index "obj_store__24_udbl_unique_1" on "obj_store__24" ("udbl_1_td", "udbl_1");
create unique index "obj_store__24_ustr_unique_2" on "obj_store__24" ("ustr_2_td", "ustr_2");
create unique index "obj_store__24_unum_unique_2" on "obj_store__24" ("unum_2_td", "unum_2");
create unique index "obj_store__24_uts_unique_2" on "obj_store__24" ("uts_2_td", "uts_2");
create unique index "obj_store__24_udbl_unique_2" on "obj_store__24" ("udbl_2_td", "udbl_2");
create index "obj_store__24_istr_index_1" on "obj_store__24" ("istr_1_td", "istr_1");
create index "obj_store__24_istr_index_2" on "obj_store__24" ("istr_2_td", "istr_2");
create index "obj_store__24_inum_index_1" on "obj_store__24" ("inum_1_td", "inum_1");
create index "obj_store__24_its_index_1" on "obj_store__24" ("its_1_td", "its_1");
create index "obj_store__24_idbl_index_1" on "obj_store__24" ("idbl_1_td", "idbl_1");
create index "obj_store__24_istr_index_3" on "obj_store__24" ("istr_3_td", "istr_3");
create index "obj_store__24_istr_index_4" on "obj_store__24" ("istr_4_td", "istr_4");
create index "obj_store__24_inum_index_2" on "obj_store__24" ("inum_2_td", "inum_2");
create index "obj_store__24_its_index_2" on "obj_store__24" ("its_2_td", "its_2");
create index "obj_store__24_idbl_index_2" on "obj_store__24" ("idbl_2_td", "idbl_2");
create index "obj_store__24_istr_index_5" on "obj_store__24" ("istr_5_td", "istr_5");
create index "obj_store__24_istr_index_6" on "obj_store__24" ("istr_6_td", "istr_6");
create index "obj_store__24_inum_index_3" on "obj_store__24" ("inum_3_td", "inum_3");
create index "obj_store__24_its_index_3" on "obj_store__24" ("its_3_td", "its_3");
create index "obj_store__24_idbl_index_3" on "obj_store__24" ("idbl_3_td", "idbl_3");
create index "obj_store__24_istr_index_7" on "obj_store__24" ("istr_7_td", "istr_7");
create index "obj_store__24_istr_index_8" on "obj_store__24" ("istr_8_td", "istr_8");
create index "obj_store__24_inum_index_4" on "obj_store__24" ("inum_4_td", "inum_4");
create index "obj_store__24_its_index_4" on "obj_store__24" ("its_4_td", "its_4");
create index "obj_store__24_idbl_index_4" on "obj_store__24" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__25" cascade;
create table "obj_store__25"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__25_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__25_ustr_unique_1" on "obj_store__25" ("ustr_1_td", "ustr_1");
create unique index "obj_store__25_unum_unique_1" on "obj_store__25" ("unum_1_td", "unum_1");
create unique index "obj_store__25_uts_unique_1" on "obj_store__25" ("uts_1_td", "uts_1");
create unique index "obj_store__25_udbl_unique_1" on "obj_store__25" ("udbl_1_td", "udbl_1");
create unique index "obj_store__25_ustr_unique_2" on "obj_store__25" ("ustr_2_td", "ustr_2");
create unique index "obj_store__25_unum_unique_2" on "obj_store__25" ("unum_2_td", "unum_2");
create unique index "obj_store__25_uts_unique_2" on "obj_store__25" ("uts_2_td", "uts_2");
create unique index "obj_store__25_udbl_unique_2" on "obj_store__25" ("udbl_2_td", "udbl_2");
create index "obj_store__25_istr_index_1" on "obj_store__25" ("istr_1_td", "istr_1");
create index "obj_store__25_istr_index_2" on "obj_store__25" ("istr_2_td", "istr_2");
create index "obj_store__25_inum_index_1" on "obj_store__25" ("inum_1_td", "inum_1");
create index "obj_store__25_its_index_1" on "obj_store__25" ("its_1_td", "its_1");
create index "obj_store__25_idbl_index_1" on "obj_store__25" ("idbl_1_td", "idbl_1");
create index "obj_store__25_istr_index_3" on "obj_store__25" ("istr_3_td", "istr_3");
create index "obj_store__25_istr_index_4" on "obj_store__25" ("istr_4_td", "istr_4");
create index "obj_store__25_inum_index_2" on "obj_store__25" ("inum_2_td", "inum_2");
create index "obj_store__25_its_index_2" on "obj_store__25" ("its_2_td", "its_2");
create index "obj_store__25_idbl_index_2" on "obj_store__25" ("idbl_2_td", "idbl_2");
create index "obj_store__25_istr_index_5" on "obj_store__25" ("istr_5_td", "istr_5");
create index "obj_store__25_istr_index_6" on "obj_store__25" ("istr_6_td", "istr_6");
create index "obj_store__25_inum_index_3" on "obj_store__25" ("inum_3_td", "inum_3");
create index "obj_store__25_its_index_3" on "obj_store__25" ("its_3_td", "its_3");
create index "obj_store__25_idbl_index_3" on "obj_store__25" ("idbl_3_td", "idbl_3");
create index "obj_store__25_istr_index_7" on "obj_store__25" ("istr_7_td", "istr_7");
create index "obj_store__25_istr_index_8" on "obj_store__25" ("istr_8_td", "istr_8");
create index "obj_store__25_inum_index_4" on "obj_store__25" ("inum_4_td", "inum_4");
create index "obj_store__25_its_index_4" on "obj_store__25" ("its_4_td", "its_4");
create index "obj_store__25_idbl_index_4" on "obj_store__25" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__26" cascade;
create table "obj_store__26"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__26_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__26_ustr_unique_1" on "obj_store__26" ("ustr_1_td", "ustr_1");
create unique index "obj_store__26_unum_unique_1" on "obj_store__26" ("unum_1_td", "unum_1");
create unique index "obj_store__26_uts_unique_1" on "obj_store__26" ("uts_1_td", "uts_1");
create unique index "obj_store__26_udbl_unique_1" on "obj_store__26" ("udbl_1_td", "udbl_1");
create unique index "obj_store__26_ustr_unique_2" on "obj_store__26" ("ustr_2_td", "ustr_2");
create unique index "obj_store__26_unum_unique_2" on "obj_store__26" ("unum_2_td", "unum_2");
create unique index "obj_store__26_uts_unique_2" on "obj_store__26" ("uts_2_td", "uts_2");
create unique index "obj_store__26_udbl_unique_2" on "obj_store__26" ("udbl_2_td", "udbl_2");
create index "obj_store__26_istr_index_1" on "obj_store__26" ("istr_1_td", "istr_1");
create index "obj_store__26_istr_index_2" on "obj_store__26" ("istr_2_td", "istr_2");
create index "obj_store__26_inum_index_1" on "obj_store__26" ("inum_1_td", "inum_1");
create index "obj_store__26_its_index_1" on "obj_store__26" ("its_1_td", "its_1");
create index "obj_store__26_idbl_index_1" on "obj_store__26" ("idbl_1_td", "idbl_1");
create index "obj_store__26_istr_index_3" on "obj_store__26" ("istr_3_td", "istr_3");
create index "obj_store__26_istr_index_4" on "obj_store__26" ("istr_4_td", "istr_4");
create index "obj_store__26_inum_index_2" on "obj_store__26" ("inum_2_td", "inum_2");
create index "obj_store__26_its_index_2" on "obj_store__26" ("its_2_td", "its_2");
create index "obj_store__26_idbl_index_2" on "obj_store__26" ("idbl_2_td", "idbl_2");
create index "obj_store__26_istr_index_5" on "obj_store__26" ("istr_5_td", "istr_5");
create index "obj_store__26_istr_index_6" on "obj_store__26" ("istr_6_td", "istr_6");
create index "obj_store__26_inum_index_3" on "obj_store__26" ("inum_3_td", "inum_3");
create index "obj_store__26_its_index_3" on "obj_store__26" ("its_3_td", "its_3");
create index "obj_store__26_idbl_index_3" on "obj_store__26" ("idbl_3_td", "idbl_3");
create index "obj_store__26_istr_index_7" on "obj_store__26" ("istr_7_td", "istr_7");
create index "obj_store__26_istr_index_8" on "obj_store__26" ("istr_8_td", "istr_8");
create index "obj_store__26_inum_index_4" on "obj_store__26" ("inum_4_td", "inum_4");
create index "obj_store__26_its_index_4" on "obj_store__26" ("its_4_td", "its_4");
create index "obj_store__26_idbl_index_4" on "obj_store__26" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__27" cascade;
create table "obj_store__27"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__27_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__27_ustr_unique_1" on "obj_store__27" ("ustr_1_td", "ustr_1");
create unique index "obj_store__27_unum_unique_1" on "obj_store__27" ("unum_1_td", "unum_1");
create unique index "obj_store__27_uts_unique_1" on "obj_store__27" ("uts_1_td", "uts_1");
create unique index "obj_store__27_udbl_unique_1" on "obj_store__27" ("udbl_1_td", "udbl_1");
create unique index "obj_store__27_ustr_unique_2" on "obj_store__27" ("ustr_2_td", "ustr_2");
create unique index "obj_store__27_unum_unique_2" on "obj_store__27" ("unum_2_td", "unum_2");
create unique index "obj_store__27_uts_unique_2" on "obj_store__27" ("uts_2_td", "uts_2");
create unique index "obj_store__27_udbl_unique_2" on "obj_store__27" ("udbl_2_td", "udbl_2");
create index "obj_store__27_istr_index_1" on "obj_store__27" ("istr_1_td", "istr_1");
create index "obj_store__27_istr_index_2" on "obj_store__27" ("istr_2_td", "istr_2");
create index "obj_store__27_inum_index_1" on "obj_store__27" ("inum_1_td", "inum_1");
create index "obj_store__27_its_index_1" on "obj_store__27" ("its_1_td", "its_1");
create index "obj_store__27_idbl_index_1" on "obj_store__27" ("idbl_1_td", "idbl_1");
create index "obj_store__27_istr_index_3" on "obj_store__27" ("istr_3_td", "istr_3");
create index "obj_store__27_istr_index_4" on "obj_store__27" ("istr_4_td", "istr_4");
create index "obj_store__27_inum_index_2" on "obj_store__27" ("inum_2_td", "inum_2");
create index "obj_store__27_its_index_2" on "obj_store__27" ("its_2_td", "its_2");
create index "obj_store__27_idbl_index_2" on "obj_store__27" ("idbl_2_td", "idbl_2");
create index "obj_store__27_istr_index_5" on "obj_store__27" ("istr_5_td", "istr_5");
create index "obj_store__27_istr_index_6" on "obj_store__27" ("istr_6_td", "istr_6");
create index "obj_store__27_inum_index_3" on "obj_store__27" ("inum_3_td", "inum_3");
create index "obj_store__27_its_index_3" on "obj_store__27" ("its_3_td", "its_3");
create index "obj_store__27_idbl_index_3" on "obj_store__27" ("idbl_3_td", "idbl_3");
create index "obj_store__27_istr_index_7" on "obj_store__27" ("istr_7_td", "istr_7");
create index "obj_store__27_istr_index_8" on "obj_store__27" ("istr_8_td", "istr_8");
create index "obj_store__27_inum_index_4" on "obj_store__27" ("inum_4_td", "inum_4");
create index "obj_store__27_its_index_4" on "obj_store__27" ("its_4_td", "its_4");
create index "obj_store__27_idbl_index_4" on "obj_store__27" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__28" cascade;
create table "obj_store__28"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__28_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__28_ustr_unique_1" on "obj_store__28" ("ustr_1_td", "ustr_1");
create unique index "obj_store__28_unum_unique_1" on "obj_store__28" ("unum_1_td", "unum_1");
create unique index "obj_store__28_uts_unique_1" on "obj_store__28" ("uts_1_td", "uts_1");
create unique index "obj_store__28_udbl_unique_1" on "obj_store__28" ("udbl_1_td", "udbl_1");
create unique index "obj_store__28_ustr_unique_2" on "obj_store__28" ("ustr_2_td", "ustr_2");
create unique index "obj_store__28_unum_unique_2" on "obj_store__28" ("unum_2_td", "unum_2");
create unique index "obj_store__28_uts_unique_2" on "obj_store__28" ("uts_2_td", "uts_2");
create unique index "obj_store__28_udbl_unique_2" on "obj_store__28" ("udbl_2_td", "udbl_2");
create index "obj_store__28_istr_index_1" on "obj_store__28" ("istr_1_td", "istr_1");
create index "obj_store__28_istr_index_2" on "obj_store__28" ("istr_2_td", "istr_2");
create index "obj_store__28_inum_index_1" on "obj_store__28" ("inum_1_td", "inum_1");
create index "obj_store__28_its_index_1" on "obj_store__28" ("its_1_td", "its_1");
create index "obj_store__28_idbl_index_1" on "obj_store__28" ("idbl_1_td", "idbl_1");
create index "obj_store__28_istr_index_3" on "obj_store__28" ("istr_3_td", "istr_3");
create index "obj_store__28_istr_index_4" on "obj_store__28" ("istr_4_td", "istr_4");
create index "obj_store__28_inum_index_2" on "obj_store__28" ("inum_2_td", "inum_2");
create index "obj_store__28_its_index_2" on "obj_store__28" ("its_2_td", "its_2");
create index "obj_store__28_idbl_index_2" on "obj_store__28" ("idbl_2_td", "idbl_2");
create index "obj_store__28_istr_index_5" on "obj_store__28" ("istr_5_td", "istr_5");
create index "obj_store__28_istr_index_6" on "obj_store__28" ("istr_6_td", "istr_6");
create index "obj_store__28_inum_index_3" on "obj_store__28" ("inum_3_td", "inum_3");
create index "obj_store__28_its_index_3" on "obj_store__28" ("its_3_td", "its_3");
create index "obj_store__28_idbl_index_3" on "obj_store__28" ("idbl_3_td", "idbl_3");
create index "obj_store__28_istr_index_7" on "obj_store__28" ("istr_7_td", "istr_7");
create index "obj_store__28_istr_index_8" on "obj_store__28" ("istr_8_td", "istr_8");
create index "obj_store__28_inum_index_4" on "obj_store__28" ("inum_4_td", "inum_4");
create index "obj_store__28_its_index_4" on "obj_store__28" ("its_4_td", "its_4");
create index "obj_store__28_idbl_index_4" on "obj_store__28" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__29" cascade;
create table "obj_store__29"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__29_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__29_ustr_unique_1" on "obj_store__29" ("ustr_1_td", "ustr_1");
create unique index "obj_store__29_unum_unique_1" on "obj_store__29" ("unum_1_td", "unum_1");
create unique index "obj_store__29_uts_unique_1" on "obj_store__29" ("uts_1_td", "uts_1");
create unique index "obj_store__29_udbl_unique_1" on "obj_store__29" ("udbl_1_td", "udbl_1");
create unique index "obj_store__29_ustr_unique_2" on "obj_store__29" ("ustr_2_td", "ustr_2");
create unique index "obj_store__29_unum_unique_2" on "obj_store__29" ("unum_2_td", "unum_2");
create unique index "obj_store__29_uts_unique_2" on "obj_store__29" ("uts_2_td", "uts_2");
create unique index "obj_store__29_udbl_unique_2" on "obj_store__29" ("udbl_2_td", "udbl_2");
create index "obj_store__29_istr_index_1" on "obj_store__29" ("istr_1_td", "istr_1");
create index "obj_store__29_istr_index_2" on "obj_store__29" ("istr_2_td", "istr_2");
create index "obj_store__29_inum_index_1" on "obj_store__29" ("inum_1_td", "inum_1");
create index "obj_store__29_its_index_1" on "obj_store__29" ("its_1_td", "its_1");
create index "obj_store__29_idbl_index_1" on "obj_store__29" ("idbl_1_td", "idbl_1");
create index "obj_store__29_istr_index_3" on "obj_store__29" ("istr_3_td", "istr_3");
create index "obj_store__29_istr_index_4" on "obj_store__29" ("istr_4_td", "istr_4");
create index "obj_store__29_inum_index_2" on "obj_store__29" ("inum_2_td", "inum_2");
create index "obj_store__29_its_index_2" on "obj_store__29" ("its_2_td", "its_2");
create index "obj_store__29_idbl_index_2" on "obj_store__29" ("idbl_2_td", "idbl_2");
create index "obj_store__29_istr_index_5" on "obj_store__29" ("istr_5_td", "istr_5");
create index "obj_store__29_istr_index_6" on "obj_store__29" ("istr_6_td", "istr_6");
create index "obj_store__29_inum_index_3" on "obj_store__29" ("inum_3_td", "inum_3");
create index "obj_store__29_its_index_3" on "obj_store__29" ("its_3_td", "its_3");
create index "obj_store__29_idbl_index_3" on "obj_store__29" ("idbl_3_td", "idbl_3");
create index "obj_store__29_istr_index_7" on "obj_store__29" ("istr_7_td", "istr_7");
create index "obj_store__29_istr_index_8" on "obj_store__29" ("istr_8_td", "istr_8");
create index "obj_store__29_inum_index_4" on "obj_store__29" ("inum_4_td", "inum_4");
create index "obj_store__29_its_index_4" on "obj_store__29" ("its_4_td", "its_4");
create index "obj_store__29_idbl_index_4" on "obj_store__29" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__30" cascade;
create table "obj_store__30"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__30_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__30_ustr_unique_1" on "obj_store__30" ("ustr_1_td", "ustr_1");
create unique index "obj_store__30_unum_unique_1" on "obj_store__30" ("unum_1_td", "unum_1");
create unique index "obj_store__30_uts_unique_1" on "obj_store__30" ("uts_1_td", "uts_1");
create unique index "obj_store__30_udbl_unique_1" on "obj_store__30" ("udbl_1_td", "udbl_1");
create unique index "obj_store__30_ustr_unique_2" on "obj_store__30" ("ustr_2_td", "ustr_2");
create unique index "obj_store__30_unum_unique_2" on "obj_store__30" ("unum_2_td", "unum_2");
create unique index "obj_store__30_uts_unique_2" on "obj_store__30" ("uts_2_td", "uts_2");
create unique index "obj_store__30_udbl_unique_2" on "obj_store__30" ("udbl_2_td", "udbl_2");
create index "obj_store__30_istr_index_1" on "obj_store__30" ("istr_1_td", "istr_1");
create index "obj_store__30_istr_index_2" on "obj_store__30" ("istr_2_td", "istr_2");
create index "obj_store__30_inum_index_1" on "obj_store__30" ("inum_1_td", "inum_1");
create index "obj_store__30_its_index_1" on "obj_store__30" ("its_1_td", "its_1");
create index "obj_store__30_idbl_index_1" on "obj_store__30" ("idbl_1_td", "idbl_1");
create index "obj_store__30_istr_index_3" on "obj_store__30" ("istr_3_td", "istr_3");
create index "obj_store__30_istr_index_4" on "obj_store__30" ("istr_4_td", "istr_4");
create index "obj_store__30_inum_index_2" on "obj_store__30" ("inum_2_td", "inum_2");
create index "obj_store__30_its_index_2" on "obj_store__30" ("its_2_td", "its_2");
create index "obj_store__30_idbl_index_2" on "obj_store__30" ("idbl_2_td", "idbl_2");
create index "obj_store__30_istr_index_5" on "obj_store__30" ("istr_5_td", "istr_5");
create index "obj_store__30_istr_index_6" on "obj_store__30" ("istr_6_td", "istr_6");
create index "obj_store__30_inum_index_3" on "obj_store__30" ("inum_3_td", "inum_3");
create index "obj_store__30_its_index_3" on "obj_store__30" ("its_3_td", "its_3");
create index "obj_store__30_idbl_index_3" on "obj_store__30" ("idbl_3_td", "idbl_3");
create index "obj_store__30_istr_index_7" on "obj_store__30" ("istr_7_td", "istr_7");
create index "obj_store__30_istr_index_8" on "obj_store__30" ("istr_8_td", "istr_8");
create index "obj_store__30_inum_index_4" on "obj_store__30" ("inum_4_td", "inum_4");
create index "obj_store__30_its_index_4" on "obj_store__30" ("its_4_td", "its_4");
create index "obj_store__30_idbl_index_4" on "obj_store__30" ("idbl_4_td", "idbl_4");

/* drop/create OBJ_STORE */
drop table if exists "obj_store__31" cascade;
create table "obj_store__31"
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
    "up_user" varchar(64)
    ,"ustr_1" varchar(4000)
    ,"ustr_1_td" varchar(139)
    ,"unum_1" numeric
    ,"unum_1_td" varchar(139)
    ,"uts_1" timestamp(3)
    ,"uts_1_td" varchar(139)
    ,"udbl_1" double precision
    ,"udbl_1_td" varchar(139)
    ,"ustr_2" varchar(4000)
    ,"ustr_2_td" varchar(139)
    ,"unum_2" numeric
    ,"unum_2_td" varchar(139)
    ,"uts_2" timestamp(3)
    ,"uts_2_td" varchar(139)
    ,"udbl_2" double precision
    ,"udbl_2_td" varchar(139)
    ,"istr_1" varchar(4000)
    ,"istr_1_td" varchar(139)
    ,"istr_2" varchar(4000)
    ,"istr_2_td" varchar(139)
    ,"inum_1" numeric
    ,"inum_1_td" varchar(139)
    ,"its_1" timestamp(3)
    ,"its_1_td" varchar(139)
    ,"idbl_1" double precision
    ,"idbl_1_td" varchar(139)
    ,"istr_3" varchar(4000)
    ,"istr_3_td" varchar(139)
    ,"istr_4" varchar(4000)
    ,"istr_4_td" varchar(139)
    ,"inum_2" numeric
    ,"inum_2_td" varchar(139)
    ,"its_2" timestamp(3)
    ,"its_2_td" varchar(139)
    ,"idbl_2" double precision
    ,"idbl_2_td" varchar(139)
    ,"istr_5" varchar(4000)
    ,"istr_5_td" varchar(139)
    ,"istr_6" varchar(4000)
    ,"istr_6_td" varchar(139)
    ,"inum_3" numeric
    ,"inum_3_td" varchar(139)
    ,"its_3" timestamp(3)
    ,"its_3_td" varchar(139)
    ,"idbl_3" double precision
    ,"idbl_3_td" varchar(139)
    ,"istr_7" varchar(4000)
    ,"istr_7_td" varchar(139)
    ,"istr_8" varchar(4000)
    ,"istr_8_td" varchar(139)
    ,"inum_4" numeric
    ,"inum_4_td" varchar(139)
    ,"its_4" timestamp(3)
    ,"its_4_td" varchar(139)
    ,"idbl_4" double precision
    ,"idbl_4_td" varchar(139)
    ,"str_1" varchar(4000)
    ,"str_2" varchar(4000)
    ,"str_3" varchar(4000)
    ,"str_4" varchar(4000)
    ,"num_1" numeric
    ,"ts_1" timestamp(3)
    ,"dbl_1" double precision
    ,"str_5" varchar(4000)
    ,"str_6" varchar(4000)
    ,"str_7" varchar(4000)
    ,"str_8" varchar(4000)
    ,"num_2" numeric
    ,"ts_2" timestamp(3)
    ,"dbl_2" double precision
    ,"str_9" varchar(4000)
    ,"str_10" varchar(4000)
    ,"str_11" varchar(4000)
    ,"str_12" varchar(4000)
    ,"num_3" numeric
    ,"ts_3" timestamp(3)
    ,"dbl_3" double precision
    ,"str_13" varchar(4000)
    ,"str_14" varchar(4000)
    ,"str_15" varchar(4000)
    ,"str_16" varchar(4000)
    ,"num_4" numeric
    ,"ts_4" timestamp(3)
    ,"dbl_4" double precision
    ,"str_17" varchar(4000)
    ,"str_18" varchar(4000)
    ,"str_19" varchar(4000)
    ,"str_20" varchar(4000)
    ,"num_5" numeric
    ,"ts_5" timestamp(3)
    ,"dbl_5" double precision
    ,"str_21" varchar(4000)
    ,"str_22" varchar(4000)
    ,"str_23" varchar(4000)
    ,"str_24" varchar(4000)
    ,"num_6" numeric
    ,"ts_6" timestamp(3)
    ,"dbl_6" double precision
    ,"str_25" varchar(4000)
    ,"str_26" varchar(4000)
    ,"str_27" varchar(4000)
    ,"str_28" varchar(4000)
    ,"num_7" numeric
    ,"ts_7" timestamp(3)
    ,"dbl_7" double precision
    ,"str_29" varchar(4000)
    ,"str_30" varchar(4000)
    ,"str_31" varchar(4000)
    ,"str_32" varchar(4000)
    ,"num_8" numeric
    ,"ts_8" timestamp(3)
    ,"dbl_8" double precision
    ,"str_33" varchar(4000)
    ,"str_34" varchar(4000)
    ,"str_35" varchar(4000)
    ,"str_36" varchar(4000)
    ,"num_9" numeric
    ,"ts_9" timestamp(3)
    ,"dbl_9" double precision
    ,"str_37" varchar(4000)
    ,"str_38" varchar(4000)
    ,"str_39" varchar(4000)
    ,"str_40" varchar(4000)
    ,"num_10" numeric
    ,"ts_10" timestamp(3)
    ,"dbl_10" double precision
    ,"str_41" varchar(4000)
    ,"str_42" varchar(4000)
    ,"str_43" varchar(4000)
    ,"str_44" varchar(4000)
    ,"num_11" numeric
    ,"ts_11" timestamp(3)
    ,"dbl_11" double precision
    ,"str_45" varchar(4000)
    ,"str_46" varchar(4000)
    ,"str_47" varchar(4000)
    ,"str_48" varchar(4000)
    ,"num_12" numeric
    ,"ts_12" timestamp(3)
    ,"dbl_12" double precision
    ,"str_49" varchar(4000)
    ,"str_50" varchar(4000)
    ,"str_51" varchar(4000)
    ,"str_52" varchar(4000)
    ,"num_13" numeric
    ,"ts_13" timestamp(3)
    ,"dbl_13" double precision
    ,"str_53" varchar(4000)
    ,"str_54" varchar(4000)
    ,"str_55" varchar(4000)
    ,"str_56" varchar(4000)
    ,"num_14" numeric
    ,"ts_14" timestamp(3)
    ,"dbl_14" double precision
    ,"str_57" varchar(4000)
    ,"str_58" varchar(4000)
    ,"str_59" varchar(4000)
    ,"str_60" varchar(4000)
    ,"num_15" numeric
    ,"ts_15" timestamp(3)
    ,"dbl_15" double precision
    ,"str_61" varchar(4000)
    ,"str_62" varchar(4000)
    ,"str_63" varchar(4000)
    ,"str_64" varchar(4000)
    ,"num_16" numeric
    ,"ts_16" timestamp(3)
    ,"dbl_16" double precision
    ,"str_65" varchar(4000)
    ,"str_66" varchar(4000)
    ,"str_67" varchar(4000)
    ,"str_68" varchar(4000)
    ,"num_17" numeric
    ,"ts_17" timestamp(3)
    ,"dbl_17" double precision
    ,"str_69" varchar(4000)
    ,"str_70" varchar(4000)
    ,"str_71" varchar(4000)
    ,"str_72" varchar(4000)
    ,"num_18" numeric
    ,"ts_18" timestamp(3)
    ,"dbl_18" double precision
    ,"str_73" varchar(4000)
    ,"str_74" varchar(4000)
    ,"str_75" varchar(4000)
    ,"str_76" varchar(4000)
    ,"num_19" numeric
    ,"ts_19" timestamp(3)
    ,"dbl_19" double precision
    ,"str_77" varchar(4000)
    ,"str_78" varchar(4000)
    ,"str_79" varchar(4000)
    ,"str_80" varchar(4000)
    ,"num_20" numeric
    ,"ts_20" timestamp(3)
    ,"dbl_20" double precision
    ,"str_81" varchar(4000)
    ,"str_82" varchar(4000)
    ,"str_83" varchar(4000)
    ,"str_84" varchar(4000)
    ,"num_21" numeric
    ,"ts_21" timestamp(3)
    ,"dbl_21" double precision
    ,"str_85" varchar(4000)
    ,"str_86" varchar(4000)
    ,"str_87" varchar(4000)
    ,"str_88" varchar(4000)
    ,"num_22" numeric
    ,"ts_22" timestamp(3)
    ,"dbl_22" double precision
    ,"str_89" varchar(4000)
    ,"str_90" varchar(4000)
    ,"str_91" varchar(4000)
    ,"str_92" varchar(4000)
    ,"num_23" numeric
    ,"ts_23" timestamp(3)
    ,"dbl_23" double precision
    ,"str_93" varchar(4000)
    ,"str_94" varchar(4000)
    ,"str_95" varchar(4000)
    ,"str_96" varchar(4000)
    ,"num_24" numeric
    ,"ts_24" timestamp(3)
    ,"dbl_24" double precision
    ,"str_97" varchar(4000)
    ,"str_98" varchar(4000)
    ,"str_99" varchar(4000)
    ,"str_100" varchar(4000)
    ,"num_25" numeric
    ,"ts_25" timestamp(3)
    ,"dbl_25" double precision
    ,"str_101" varchar(4000)
    ,"str_102" varchar(4000)
    ,"str_103" varchar(4000)
    ,"str_104" varchar(4000)
    ,"num_26" numeric
    ,"ts_26" timestamp(3)
    ,"dbl_26" double precision
    ,"str_105" varchar(4000)
    ,"str_106" varchar(4000)
    ,"str_107" varchar(4000)
    ,"str_108" varchar(4000)
    ,"num_27" numeric
    ,"ts_27" timestamp(3)
    ,"dbl_27" double precision
    ,"str_109" varchar(4000)
    ,"str_110" varchar(4000)
    ,"str_111" varchar(4000)
    ,"str_112" varchar(4000)
    ,"num_28" numeric
    ,"ts_28" timestamp(3)
    ,"dbl_28" double precision
    ,"str_113" varchar(4000)
    ,"str_114" varchar(4000)
    ,"str_115" varchar(4000)
    ,"str_116" varchar(4000)
    ,"num_29" numeric
    ,"ts_29" timestamp(3)
    ,"dbl_29" double precision
    ,"str_117" varchar(4000)
    ,"str_118" varchar(4000)
    ,"str_119" varchar(4000)
    ,"str_120" varchar(4000)
    ,"num_30" numeric
    ,"ts_30" timestamp(3)
    ,"dbl_30" double precision
    ,"str_121" varchar(4000)
    ,"str_122" varchar(4000)
    ,"str_123" varchar(4000)
    ,"str_124" varchar(4000)
    ,"num_31" numeric
    ,"ts_31" timestamp(3)
    ,"dbl_31" double precision
    ,"str_125" varchar(4000)
    ,"str_126" varchar(4000)
    ,"str_127" varchar(4000)
    ,"str_128" varchar(4000)
    ,"num_32" numeric
    ,"ts_32" timestamp(3)
    ,"dbl_32" double precision
    ,constraint "obj_store__31_pk" primary key ("tenant_id", "obj_def_id", "obj_id", "obj_ver", "pg_no")
)
;
create unique index "obj_store__31_ustr_unique_1" on "obj_store__31" ("ustr_1_td", "ustr_1");
create unique index "obj_store__31_unum_unique_1" on "obj_store__31" ("unum_1_td", "unum_1");
create unique index "obj_store__31_uts_unique_1" on "obj_store__31" ("uts_1_td", "uts_1");
create unique index "obj_store__31_udbl_unique_1" on "obj_store__31" ("udbl_1_td", "udbl_1");
create unique index "obj_store__31_ustr_unique_2" on "obj_store__31" ("ustr_2_td", "ustr_2");
create unique index "obj_store__31_unum_unique_2" on "obj_store__31" ("unum_2_td", "unum_2");
create unique index "obj_store__31_uts_unique_2" on "obj_store__31" ("uts_2_td", "uts_2");
create unique index "obj_store__31_udbl_unique_2" on "obj_store__31" ("udbl_2_td", "udbl_2");
create index "obj_store__31_istr_index_1" on "obj_store__31" ("istr_1_td", "istr_1");
create index "obj_store__31_istr_index_2" on "obj_store__31" ("istr_2_td", "istr_2");
create index "obj_store__31_inum_index_1" on "obj_store__31" ("inum_1_td", "inum_1");
create index "obj_store__31_its_index_1" on "obj_store__31" ("its_1_td", "its_1");
create index "obj_store__31_idbl_index_1" on "obj_store__31" ("idbl_1_td", "idbl_1");
create index "obj_store__31_istr_index_3" on "obj_store__31" ("istr_3_td", "istr_3");
create index "obj_store__31_istr_index_4" on "obj_store__31" ("istr_4_td", "istr_4");
create index "obj_store__31_inum_index_2" on "obj_store__31" ("inum_2_td", "inum_2");
create index "obj_store__31_its_index_2" on "obj_store__31" ("its_2_td", "its_2");
create index "obj_store__31_idbl_index_2" on "obj_store__31" ("idbl_2_td", "idbl_2");
create index "obj_store__31_istr_index_5" on "obj_store__31" ("istr_5_td", "istr_5");
create index "obj_store__31_istr_index_6" on "obj_store__31" ("istr_6_td", "istr_6");
create index "obj_store__31_inum_index_3" on "obj_store__31" ("inum_3_td", "inum_3");
create index "obj_store__31_its_index_3" on "obj_store__31" ("its_3_td", "its_3");
create index "obj_store__31_idbl_index_3" on "obj_store__31" ("idbl_3_td", "idbl_3");
create index "obj_store__31_istr_index_7" on "obj_store__31" ("istr_7_td", "istr_7");
create index "obj_store__31_istr_index_8" on "obj_store__31" ("istr_8_td", "istr_8");
create index "obj_store__31_inum_index_4" on "obj_store__31" ("inum_4_td", "inum_4");
create index "obj_store__31_its_index_4" on "obj_store__31" ("its_4_td", "its_4");
create index "obj_store__31_idbl_index_4" on "obj_store__31" ("idbl_4_td", "idbl_4");

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
