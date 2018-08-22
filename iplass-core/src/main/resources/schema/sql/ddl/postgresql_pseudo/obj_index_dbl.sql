/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl" cascade;
create table "obj_index_dbl"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl_index1" on "obj_index_dbl" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl_index2" on "obj_index_dbl" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__1" cascade;
create table "obj_index_dbl__1"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__1_index1" on "obj_index_dbl__1" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__1_index2" on "obj_index_dbl__1" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__2" cascade;
create table "obj_index_dbl__2"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__2_index1" on "obj_index_dbl__2" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__2_index2" on "obj_index_dbl__2" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__3" cascade;
create table "obj_index_dbl__3"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__3_index1" on "obj_index_dbl__3" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__3_index2" on "obj_index_dbl__3" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__4" cascade;
create table "obj_index_dbl__4"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__4_index1" on "obj_index_dbl__4" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__4_index2" on "obj_index_dbl__4" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__5" cascade;
create table "obj_index_dbl__5"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__5_index1" on "obj_index_dbl__5" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__5_index2" on "obj_index_dbl__5" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__6" cascade;
create table "obj_index_dbl__6"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__6_index1" on "obj_index_dbl__6" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__6_index2" on "obj_index_dbl__6" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__7" cascade;
create table "obj_index_dbl__7"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__7_index1" on "obj_index_dbl__7" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__7_index2" on "obj_index_dbl__7" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__8" cascade;
create table "obj_index_dbl__8"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__8_index1" on "obj_index_dbl__8" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__8_index2" on "obj_index_dbl__8" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__9" cascade;
create table "obj_index_dbl__9"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__9_index1" on "obj_index_dbl__9" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__9_index2" on "obj_index_dbl__9" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__10" cascade;
create table "obj_index_dbl__10"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__10_index1" on "obj_index_dbl__10" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__10_index2" on "obj_index_dbl__10" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__11" cascade;
create table "obj_index_dbl__11"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__11_index1" on "obj_index_dbl__11" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__11_index2" on "obj_index_dbl__11" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__12" cascade;
create table "obj_index_dbl__12"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__12_index1" on "obj_index_dbl__12" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__12_index2" on "obj_index_dbl__12" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__13" cascade;
create table "obj_index_dbl__13"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__13_index1" on "obj_index_dbl__13" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__13_index2" on "obj_index_dbl__13" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__14" cascade;
create table "obj_index_dbl__14"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__14_index1" on "obj_index_dbl__14" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__14_index2" on "obj_index_dbl__14" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__15" cascade;
create table "obj_index_dbl__15"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__15_index1" on "obj_index_dbl__15" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__15_index2" on "obj_index_dbl__15" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__16" cascade;
create table "obj_index_dbl__16"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__16_index1" on "obj_index_dbl__16" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__16_index2" on "obj_index_dbl__16" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__17" cascade;
create table "obj_index_dbl__17"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__17_index1" on "obj_index_dbl__17" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__17_index2" on "obj_index_dbl__17" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__18" cascade;
create table "obj_index_dbl__18"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__18_index1" on "obj_index_dbl__18" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__18_index2" on "obj_index_dbl__18" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__19" cascade;
create table "obj_index_dbl__19"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__19_index1" on "obj_index_dbl__19" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__19_index2" on "obj_index_dbl__19" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__20" cascade;
create table "obj_index_dbl__20"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__20_index1" on "obj_index_dbl__20" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__20_index2" on "obj_index_dbl__20" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__21" cascade;
create table "obj_index_dbl__21"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__21_index1" on "obj_index_dbl__21" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__21_index2" on "obj_index_dbl__21" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__22" cascade;
create table "obj_index_dbl__22"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__22_index1" on "obj_index_dbl__22" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__22_index2" on "obj_index_dbl__22" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__23" cascade;
create table "obj_index_dbl__23"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__23_index1" on "obj_index_dbl__23" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__23_index2" on "obj_index_dbl__23" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__24" cascade;
create table "obj_index_dbl__24"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__24_index1" on "obj_index_dbl__24" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__24_index2" on "obj_index_dbl__24" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__25" cascade;
create table "obj_index_dbl__25"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__25_index1" on "obj_index_dbl__25" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__25_index2" on "obj_index_dbl__25" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__26" cascade;
create table "obj_index_dbl__26"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__26_index1" on "obj_index_dbl__26" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__26_index2" on "obj_index_dbl__26" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__27" cascade;
create table "obj_index_dbl__27"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__27_index1" on "obj_index_dbl__27" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__27_index2" on "obj_index_dbl__27" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__28" cascade;
create table "obj_index_dbl__28"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__28_index1" on "obj_index_dbl__28" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__28_index2" on "obj_index_dbl__28" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__29" cascade;
create table "obj_index_dbl__29"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__29_index1" on "obj_index_dbl__29" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__29_index2" on "obj_index_dbl__29" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__30" cascade;
create table "obj_index_dbl__30"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__30_index1" on "obj_index_dbl__30" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__30_index2" on "obj_index_dbl__30" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__31" cascade;
create table "obj_index_dbl__31"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)
;
create index "obj_index_dbl__31_index1" on "obj_index_dbl__31" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__31_index2" on "obj_index_dbl__31" ("tenant_id", "obj_def_id", "obj_id");
