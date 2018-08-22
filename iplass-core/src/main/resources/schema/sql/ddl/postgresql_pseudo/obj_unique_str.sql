/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str" cascade;
create table "obj_unique_str"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str_index1" on "obj_unique_str" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str_index2" on "obj_unique_str" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__1" cascade;
create table "obj_unique_str__1"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__1_index1" on "obj_unique_str__1" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__1_index2" on "obj_unique_str__1" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__2" cascade;
create table "obj_unique_str__2"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__2_index1" on "obj_unique_str__2" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__2_index2" on "obj_unique_str__2" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__3" cascade;
create table "obj_unique_str__3"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__3_index1" on "obj_unique_str__3" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__3_index2" on "obj_unique_str__3" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__4" cascade;
create table "obj_unique_str__4"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__4_index1" on "obj_unique_str__4" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__4_index2" on "obj_unique_str__4" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__5" cascade;
create table "obj_unique_str__5"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__5_index1" on "obj_unique_str__5" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__5_index2" on "obj_unique_str__5" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__6" cascade;
create table "obj_unique_str__6"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__6_index1" on "obj_unique_str__6" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__6_index2" on "obj_unique_str__6" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__7" cascade;
create table "obj_unique_str__7"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__7_index1" on "obj_unique_str__7" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__7_index2" on "obj_unique_str__7" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__8" cascade;
create table "obj_unique_str__8"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__8_index1" on "obj_unique_str__8" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__8_index2" on "obj_unique_str__8" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__9" cascade;
create table "obj_unique_str__9"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__9_index1" on "obj_unique_str__9" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__9_index2" on "obj_unique_str__9" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__10" cascade;
create table "obj_unique_str__10"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__10_index1" on "obj_unique_str__10" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__10_index2" on "obj_unique_str__10" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__11" cascade;
create table "obj_unique_str__11"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__11_index1" on "obj_unique_str__11" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__11_index2" on "obj_unique_str__11" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__12" cascade;
create table "obj_unique_str__12"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__12_index1" on "obj_unique_str__12" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__12_index2" on "obj_unique_str__12" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__13" cascade;
create table "obj_unique_str__13"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__13_index1" on "obj_unique_str__13" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__13_index2" on "obj_unique_str__13" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__14" cascade;
create table "obj_unique_str__14"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__14_index1" on "obj_unique_str__14" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__14_index2" on "obj_unique_str__14" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__15" cascade;
create table "obj_unique_str__15"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__15_index1" on "obj_unique_str__15" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__15_index2" on "obj_unique_str__15" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__16" cascade;
create table "obj_unique_str__16"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__16_index1" on "obj_unique_str__16" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__16_index2" on "obj_unique_str__16" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__17" cascade;
create table "obj_unique_str__17"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__17_index1" on "obj_unique_str__17" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__17_index2" on "obj_unique_str__17" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__18" cascade;
create table "obj_unique_str__18"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__18_index1" on "obj_unique_str__18" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__18_index2" on "obj_unique_str__18" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__19" cascade;
create table "obj_unique_str__19"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__19_index1" on "obj_unique_str__19" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__19_index2" on "obj_unique_str__19" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__20" cascade;
create table "obj_unique_str__20"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__20_index1" on "obj_unique_str__20" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__20_index2" on "obj_unique_str__20" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__21" cascade;
create table "obj_unique_str__21"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__21_index1" on "obj_unique_str__21" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__21_index2" on "obj_unique_str__21" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__22" cascade;
create table "obj_unique_str__22"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__22_index1" on "obj_unique_str__22" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__22_index2" on "obj_unique_str__22" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__23" cascade;
create table "obj_unique_str__23"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__23_index1" on "obj_unique_str__23" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__23_index2" on "obj_unique_str__23" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__24" cascade;
create table "obj_unique_str__24"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__24_index1" on "obj_unique_str__24" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__24_index2" on "obj_unique_str__24" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__25" cascade;
create table "obj_unique_str__25"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__25_index1" on "obj_unique_str__25" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__25_index2" on "obj_unique_str__25" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__26" cascade;
create table "obj_unique_str__26"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__26_index1" on "obj_unique_str__26" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__26_index2" on "obj_unique_str__26" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__27" cascade;
create table "obj_unique_str__27"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__27_index1" on "obj_unique_str__27" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__27_index2" on "obj_unique_str__27" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__28" cascade;
create table "obj_unique_str__28"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__28_index1" on "obj_unique_str__28" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__28_index2" on "obj_unique_str__28" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__29" cascade;
create table "obj_unique_str__29"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__29_index1" on "obj_unique_str__29" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__29_index2" on "obj_unique_str__29" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__30" cascade;
create table "obj_unique_str__30"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__30_index1" on "obj_unique_str__30" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__30_index2" on "obj_unique_str__30" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_STR */
drop table if exists "obj_unique_str__31" cascade;
create table "obj_unique_str__31"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str__31_index1" on "obj_unique_str__31" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str__31_index2" on "obj_unique_str__31" ("tenant_id", "obj_def_id", "obj_id");
