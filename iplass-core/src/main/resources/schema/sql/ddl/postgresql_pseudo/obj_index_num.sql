/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num" cascade;
create table "obj_index_num"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num_index1" on "obj_index_num" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num_index2" on "obj_index_num" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__1" cascade;
create table "obj_index_num__1"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__1_index1" on "obj_index_num__1" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__1_index2" on "obj_index_num__1" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__2" cascade;
create table "obj_index_num__2"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__2_index1" on "obj_index_num__2" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__2_index2" on "obj_index_num__2" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__3" cascade;
create table "obj_index_num__3"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__3_index1" on "obj_index_num__3" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__3_index2" on "obj_index_num__3" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__4" cascade;
create table "obj_index_num__4"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__4_index1" on "obj_index_num__4" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__4_index2" on "obj_index_num__4" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__5" cascade;
create table "obj_index_num__5"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__5_index1" on "obj_index_num__5" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__5_index2" on "obj_index_num__5" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__6" cascade;
create table "obj_index_num__6"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__6_index1" on "obj_index_num__6" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__6_index2" on "obj_index_num__6" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__7" cascade;
create table "obj_index_num__7"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__7_index1" on "obj_index_num__7" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__7_index2" on "obj_index_num__7" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__8" cascade;
create table "obj_index_num__8"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__8_index1" on "obj_index_num__8" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__8_index2" on "obj_index_num__8" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__9" cascade;
create table "obj_index_num__9"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__9_index1" on "obj_index_num__9" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__9_index2" on "obj_index_num__9" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__10" cascade;
create table "obj_index_num__10"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__10_index1" on "obj_index_num__10" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__10_index2" on "obj_index_num__10" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__11" cascade;
create table "obj_index_num__11"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__11_index1" on "obj_index_num__11" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__11_index2" on "obj_index_num__11" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__12" cascade;
create table "obj_index_num__12"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__12_index1" on "obj_index_num__12" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__12_index2" on "obj_index_num__12" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__13" cascade;
create table "obj_index_num__13"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__13_index1" on "obj_index_num__13" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__13_index2" on "obj_index_num__13" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__14" cascade;
create table "obj_index_num__14"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__14_index1" on "obj_index_num__14" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__14_index2" on "obj_index_num__14" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__15" cascade;
create table "obj_index_num__15"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__15_index1" on "obj_index_num__15" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__15_index2" on "obj_index_num__15" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__16" cascade;
create table "obj_index_num__16"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__16_index1" on "obj_index_num__16" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__16_index2" on "obj_index_num__16" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__17" cascade;
create table "obj_index_num__17"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__17_index1" on "obj_index_num__17" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__17_index2" on "obj_index_num__17" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__18" cascade;
create table "obj_index_num__18"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__18_index1" on "obj_index_num__18" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__18_index2" on "obj_index_num__18" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__19" cascade;
create table "obj_index_num__19"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__19_index1" on "obj_index_num__19" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__19_index2" on "obj_index_num__19" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__20" cascade;
create table "obj_index_num__20"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__20_index1" on "obj_index_num__20" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__20_index2" on "obj_index_num__20" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__21" cascade;
create table "obj_index_num__21"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__21_index1" on "obj_index_num__21" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__21_index2" on "obj_index_num__21" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__22" cascade;
create table "obj_index_num__22"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__22_index1" on "obj_index_num__22" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__22_index2" on "obj_index_num__22" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__23" cascade;
create table "obj_index_num__23"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__23_index1" on "obj_index_num__23" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__23_index2" on "obj_index_num__23" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__24" cascade;
create table "obj_index_num__24"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__24_index1" on "obj_index_num__24" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__24_index2" on "obj_index_num__24" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__25" cascade;
create table "obj_index_num__25"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__25_index1" on "obj_index_num__25" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__25_index2" on "obj_index_num__25" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__26" cascade;
create table "obj_index_num__26"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__26_index1" on "obj_index_num__26" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__26_index2" on "obj_index_num__26" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__27" cascade;
create table "obj_index_num__27"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__27_index1" on "obj_index_num__27" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__27_index2" on "obj_index_num__27" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__28" cascade;
create table "obj_index_num__28"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__28_index1" on "obj_index_num__28" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__28_index2" on "obj_index_num__28" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__29" cascade;
create table "obj_index_num__29"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__29_index1" on "obj_index_num__29" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__29_index2" on "obj_index_num__29" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__30" cascade;
create table "obj_index_num__30"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__30_index1" on "obj_index_num__30" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__30_index2" on "obj_index_num__30" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__31" cascade;
create table "obj_index_num__31"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)
;
create index "obj_index_num__31_index1" on "obj_index_num__31" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__31_index2" on "obj_index_num__31" ("tenant_id", "obj_def_id", "obj_id");
