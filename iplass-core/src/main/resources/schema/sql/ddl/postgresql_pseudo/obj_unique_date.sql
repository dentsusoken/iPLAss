/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date" cascade;
create table "obj_unique_date"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date_index1" on "obj_unique_date" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date_index2" on "obj_unique_date" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__1" cascade;
create table "obj_unique_date__1"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__1_index1" on "obj_unique_date__1" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__1_index2" on "obj_unique_date__1" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__2" cascade;
create table "obj_unique_date__2"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__2_index1" on "obj_unique_date__2" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__2_index2" on "obj_unique_date__2" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__3" cascade;
create table "obj_unique_date__3"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__3_index1" on "obj_unique_date__3" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__3_index2" on "obj_unique_date__3" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__4" cascade;
create table "obj_unique_date__4"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__4_index1" on "obj_unique_date__4" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__4_index2" on "obj_unique_date__4" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__5" cascade;
create table "obj_unique_date__5"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__5_index1" on "obj_unique_date__5" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__5_index2" on "obj_unique_date__5" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__6" cascade;
create table "obj_unique_date__6"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__6_index1" on "obj_unique_date__6" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__6_index2" on "obj_unique_date__6" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__7" cascade;
create table "obj_unique_date__7"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__7_index1" on "obj_unique_date__7" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__7_index2" on "obj_unique_date__7" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__8" cascade;
create table "obj_unique_date__8"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__8_index1" on "obj_unique_date__8" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__8_index2" on "obj_unique_date__8" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__9" cascade;
create table "obj_unique_date__9"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__9_index1" on "obj_unique_date__9" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__9_index2" on "obj_unique_date__9" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__10" cascade;
create table "obj_unique_date__10"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__10_index1" on "obj_unique_date__10" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__10_index2" on "obj_unique_date__10" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__11" cascade;
create table "obj_unique_date__11"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__11_index1" on "obj_unique_date__11" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__11_index2" on "obj_unique_date__11" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__12" cascade;
create table "obj_unique_date__12"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__12_index1" on "obj_unique_date__12" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__12_index2" on "obj_unique_date__12" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__13" cascade;
create table "obj_unique_date__13"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__13_index1" on "obj_unique_date__13" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__13_index2" on "obj_unique_date__13" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__14" cascade;
create table "obj_unique_date__14"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__14_index1" on "obj_unique_date__14" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__14_index2" on "obj_unique_date__14" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__15" cascade;
create table "obj_unique_date__15"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__15_index1" on "obj_unique_date__15" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__15_index2" on "obj_unique_date__15" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__16" cascade;
create table "obj_unique_date__16"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__16_index1" on "obj_unique_date__16" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__16_index2" on "obj_unique_date__16" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__17" cascade;
create table "obj_unique_date__17"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__17_index1" on "obj_unique_date__17" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__17_index2" on "obj_unique_date__17" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__18" cascade;
create table "obj_unique_date__18"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__18_index1" on "obj_unique_date__18" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__18_index2" on "obj_unique_date__18" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__19" cascade;
create table "obj_unique_date__19"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__19_index1" on "obj_unique_date__19" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__19_index2" on "obj_unique_date__19" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__20" cascade;
create table "obj_unique_date__20"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__20_index1" on "obj_unique_date__20" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__20_index2" on "obj_unique_date__20" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__21" cascade;
create table "obj_unique_date__21"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__21_index1" on "obj_unique_date__21" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__21_index2" on "obj_unique_date__21" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__22" cascade;
create table "obj_unique_date__22"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__22_index1" on "obj_unique_date__22" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__22_index2" on "obj_unique_date__22" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__23" cascade;
create table "obj_unique_date__23"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__23_index1" on "obj_unique_date__23" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__23_index2" on "obj_unique_date__23" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__24" cascade;
create table "obj_unique_date__24"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__24_index1" on "obj_unique_date__24" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__24_index2" on "obj_unique_date__24" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__25" cascade;
create table "obj_unique_date__25"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__25_index1" on "obj_unique_date__25" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__25_index2" on "obj_unique_date__25" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__26" cascade;
create table "obj_unique_date__26"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__26_index1" on "obj_unique_date__26" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__26_index2" on "obj_unique_date__26" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__27" cascade;
create table "obj_unique_date__27"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__27_index1" on "obj_unique_date__27" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__27_index2" on "obj_unique_date__27" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__28" cascade;
create table "obj_unique_date__28"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__28_index1" on "obj_unique_date__28" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__28_index2" on "obj_unique_date__28" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__29" cascade;
create table "obj_unique_date__29"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__29_index1" on "obj_unique_date__29" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__29_index2" on "obj_unique_date__29" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__30" cascade;
create table "obj_unique_date__30"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__30_index1" on "obj_unique_date__30" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__30_index2" on "obj_unique_date__30" ("tenant_id", "obj_def_id", "obj_id");

/* drop/create OBJ_UNIQUE_DATE */
drop table if exists "obj_unique_date__31" cascade;
create table "obj_unique_date__31"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" timestamp
)
;
create unique index "obj_unique_date__31_index1" on "obj_unique_date__31" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_date__31_index2" on "obj_unique_date__31" ("tenant_id", "obj_def_id", "obj_id");
