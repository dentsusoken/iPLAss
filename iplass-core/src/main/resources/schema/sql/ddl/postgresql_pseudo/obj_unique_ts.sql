/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts" cascade;
create table "obj_unique_ts"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts_index1" on "obj_unique_ts" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts_index2" on "obj_unique_ts" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__1" cascade;
create table "obj_unique_ts__1"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__1_index1" on "obj_unique_ts__1" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__1_index2" on "obj_unique_ts__1" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__2" cascade;
create table "obj_unique_ts__2"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__2_index1" on "obj_unique_ts__2" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__2_index2" on "obj_unique_ts__2" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__3" cascade;
create table "obj_unique_ts__3"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__3_index1" on "obj_unique_ts__3" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__3_index2" on "obj_unique_ts__3" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__4" cascade;
create table "obj_unique_ts__4"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__4_index1" on "obj_unique_ts__4" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__4_index2" on "obj_unique_ts__4" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__5" cascade;
create table "obj_unique_ts__5"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__5_index1" on "obj_unique_ts__5" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__5_index2" on "obj_unique_ts__5" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__6" cascade;
create table "obj_unique_ts__6"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__6_index1" on "obj_unique_ts__6" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__6_index2" on "obj_unique_ts__6" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__7" cascade;
create table "obj_unique_ts__7"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__7_index1" on "obj_unique_ts__7" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__7_index2" on "obj_unique_ts__7" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__8" cascade;
create table "obj_unique_ts__8"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__8_index1" on "obj_unique_ts__8" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__8_index2" on "obj_unique_ts__8" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__9" cascade;
create table "obj_unique_ts__9"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__9_index1" on "obj_unique_ts__9" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__9_index2" on "obj_unique_ts__9" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__10" cascade;
create table "obj_unique_ts__10"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__10_index1" on "obj_unique_ts__10" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__10_index2" on "obj_unique_ts__10" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__11" cascade;
create table "obj_unique_ts__11"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__11_index1" on "obj_unique_ts__11" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__11_index2" on "obj_unique_ts__11" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__12" cascade;
create table "obj_unique_ts__12"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__12_index1" on "obj_unique_ts__12" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__12_index2" on "obj_unique_ts__12" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__13" cascade;
create table "obj_unique_ts__13"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__13_index1" on "obj_unique_ts__13" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__13_index2" on "obj_unique_ts__13" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__14" cascade;
create table "obj_unique_ts__14"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__14_index1" on "obj_unique_ts__14" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__14_index2" on "obj_unique_ts__14" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__15" cascade;
create table "obj_unique_ts__15"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__15_index1" on "obj_unique_ts__15" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__15_index2" on "obj_unique_ts__15" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__16" cascade;
create table "obj_unique_ts__16"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__16_index1" on "obj_unique_ts__16" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__16_index2" on "obj_unique_ts__16" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__17" cascade;
create table "obj_unique_ts__17"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__17_index1" on "obj_unique_ts__17" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__17_index2" on "obj_unique_ts__17" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__18" cascade;
create table "obj_unique_ts__18"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__18_index1" on "obj_unique_ts__18" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__18_index2" on "obj_unique_ts__18" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__19" cascade;
create table "obj_unique_ts__19"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__19_index1" on "obj_unique_ts__19" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__19_index2" on "obj_unique_ts__19" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__20" cascade;
create table "obj_unique_ts__20"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__20_index1" on "obj_unique_ts__20" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__20_index2" on "obj_unique_ts__20" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__21" cascade;
create table "obj_unique_ts__21"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__21_index1" on "obj_unique_ts__21" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__21_index2" on "obj_unique_ts__21" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__22" cascade;
create table "obj_unique_ts__22"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__22_index1" on "obj_unique_ts__22" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__22_index2" on "obj_unique_ts__22" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__23" cascade;
create table "obj_unique_ts__23"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__23_index1" on "obj_unique_ts__23" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__23_index2" on "obj_unique_ts__23" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__24" cascade;
create table "obj_unique_ts__24"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__24_index1" on "obj_unique_ts__24" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__24_index2" on "obj_unique_ts__24" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__25" cascade;
create table "obj_unique_ts__25"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__25_index1" on "obj_unique_ts__25" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__25_index2" on "obj_unique_ts__25" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__26" cascade;
create table "obj_unique_ts__26"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__26_index1" on "obj_unique_ts__26" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__26_index2" on "obj_unique_ts__26" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__27" cascade;
create table "obj_unique_ts__27"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__27_index1" on "obj_unique_ts__27" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__27_index2" on "obj_unique_ts__27" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__28" cascade;
create table "obj_unique_ts__28"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__28_index1" on "obj_unique_ts__28" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__28_index2" on "obj_unique_ts__28" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__29" cascade;
create table "obj_unique_ts__29"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__29_index1" on "obj_unique_ts__29" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__29_index2" on "obj_unique_ts__29" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__30" cascade;
create table "obj_unique_ts__30"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__30_index1" on "obj_unique_ts__30" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__30_index2" on "obj_unique_ts__30" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_TS */
drop table if exists "obj_unique_ts__31" cascade;
create table "obj_unique_ts__31"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp(3)
)
;
create unique index "obj_unique_ts__31_index1" on "obj_unique_ts__31" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_ts__31_index2" on "obj_unique_ts__31" ("tenant_id", "obj_def_id", "obj_id");
