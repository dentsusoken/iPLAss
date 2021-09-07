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


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__32" cascade;

create table "obj_index_dbl__32"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__32_index1" on "obj_index_dbl__32" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__32_index2" on "obj_index_dbl__32" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__33" cascade;

create table "obj_index_dbl__33"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__33_index1" on "obj_index_dbl__33" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__33_index2" on "obj_index_dbl__33" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__34" cascade;

create table "obj_index_dbl__34"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__34_index1" on "obj_index_dbl__34" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__34_index2" on "obj_index_dbl__34" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__35" cascade;

create table "obj_index_dbl__35"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__35_index1" on "obj_index_dbl__35" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__35_index2" on "obj_index_dbl__35" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__36" cascade;

create table "obj_index_dbl__36"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__36_index1" on "obj_index_dbl__36" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__36_index2" on "obj_index_dbl__36" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__37" cascade;

create table "obj_index_dbl__37"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__37_index1" on "obj_index_dbl__37" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__37_index2" on "obj_index_dbl__37" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__38" cascade;

create table "obj_index_dbl__38"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__38_index1" on "obj_index_dbl__38" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__38_index2" on "obj_index_dbl__38" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__39" cascade;

create table "obj_index_dbl__39"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__39_index1" on "obj_index_dbl__39" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__39_index2" on "obj_index_dbl__39" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__40" cascade;

create table "obj_index_dbl__40"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__40_index1" on "obj_index_dbl__40" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__40_index2" on "obj_index_dbl__40" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__41" cascade;

create table "obj_index_dbl__41"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__41_index1" on "obj_index_dbl__41" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__41_index2" on "obj_index_dbl__41" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__42" cascade;

create table "obj_index_dbl__42"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__42_index1" on "obj_index_dbl__42" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__42_index2" on "obj_index_dbl__42" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__43" cascade;

create table "obj_index_dbl__43"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__43_index1" on "obj_index_dbl__43" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__43_index2" on "obj_index_dbl__43" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__44" cascade;

create table "obj_index_dbl__44"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__44_index1" on "obj_index_dbl__44" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__44_index2" on "obj_index_dbl__44" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__45" cascade;

create table "obj_index_dbl__45"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__45_index1" on "obj_index_dbl__45" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__45_index2" on "obj_index_dbl__45" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__46" cascade;

create table "obj_index_dbl__46"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__46_index1" on "obj_index_dbl__46" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__46_index2" on "obj_index_dbl__46" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__47" cascade;

create table "obj_index_dbl__47"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__47_index1" on "obj_index_dbl__47" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__47_index2" on "obj_index_dbl__47" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__48" cascade;

create table "obj_index_dbl__48"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__48_index1" on "obj_index_dbl__48" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__48_index2" on "obj_index_dbl__48" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__49" cascade;

create table "obj_index_dbl__49"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__49_index1" on "obj_index_dbl__49" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__49_index2" on "obj_index_dbl__49" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__50" cascade;

create table "obj_index_dbl__50"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__50_index1" on "obj_index_dbl__50" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__50_index2" on "obj_index_dbl__50" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__51" cascade;

create table "obj_index_dbl__51"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__51_index1" on "obj_index_dbl__51" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__51_index2" on "obj_index_dbl__51" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__52" cascade;

create table "obj_index_dbl__52"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__52_index1" on "obj_index_dbl__52" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__52_index2" on "obj_index_dbl__52" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__53" cascade;

create table "obj_index_dbl__53"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__53_index1" on "obj_index_dbl__53" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__53_index2" on "obj_index_dbl__53" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__54" cascade;

create table "obj_index_dbl__54"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__54_index1" on "obj_index_dbl__54" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__54_index2" on "obj_index_dbl__54" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__55" cascade;

create table "obj_index_dbl__55"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__55_index1" on "obj_index_dbl__55" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__55_index2" on "obj_index_dbl__55" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__56" cascade;

create table "obj_index_dbl__56"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__56_index1" on "obj_index_dbl__56" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__56_index2" on "obj_index_dbl__56" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__57" cascade;

create table "obj_index_dbl__57"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__57_index1" on "obj_index_dbl__57" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__57_index2" on "obj_index_dbl__57" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__58" cascade;

create table "obj_index_dbl__58"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__58_index1" on "obj_index_dbl__58" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__58_index2" on "obj_index_dbl__58" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__59" cascade;

create table "obj_index_dbl__59"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__59_index1" on "obj_index_dbl__59" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__59_index2" on "obj_index_dbl__59" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__60" cascade;

create table "obj_index_dbl__60"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__60_index1" on "obj_index_dbl__60" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__60_index2" on "obj_index_dbl__60" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__61" cascade;

create table "obj_index_dbl__61"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__61_index1" on "obj_index_dbl__61" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__61_index2" on "obj_index_dbl__61" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__62" cascade;

create table "obj_index_dbl__62"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__62_index1" on "obj_index_dbl__62" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__62_index2" on "obj_index_dbl__62" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__63" cascade;

create table "obj_index_dbl__63"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__63_index1" on "obj_index_dbl__63" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__63_index2" on "obj_index_dbl__63" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__64" cascade;

create table "obj_index_dbl__64"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__64_index1" on "obj_index_dbl__64" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__64_index2" on "obj_index_dbl__64" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__65" cascade;

create table "obj_index_dbl__65"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__65_index1" on "obj_index_dbl__65" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__65_index2" on "obj_index_dbl__65" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__66" cascade;

create table "obj_index_dbl__66"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__66_index1" on "obj_index_dbl__66" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__66_index2" on "obj_index_dbl__66" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__67" cascade;

create table "obj_index_dbl__67"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__67_index1" on "obj_index_dbl__67" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__67_index2" on "obj_index_dbl__67" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__68" cascade;

create table "obj_index_dbl__68"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__68_index1" on "obj_index_dbl__68" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__68_index2" on "obj_index_dbl__68" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__69" cascade;

create table "obj_index_dbl__69"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__69_index1" on "obj_index_dbl__69" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__69_index2" on "obj_index_dbl__69" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__70" cascade;

create table "obj_index_dbl__70"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__70_index1" on "obj_index_dbl__70" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__70_index2" on "obj_index_dbl__70" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__71" cascade;

create table "obj_index_dbl__71"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__71_index1" on "obj_index_dbl__71" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__71_index2" on "obj_index_dbl__71" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__72" cascade;

create table "obj_index_dbl__72"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__72_index1" on "obj_index_dbl__72" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__72_index2" on "obj_index_dbl__72" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__73" cascade;

create table "obj_index_dbl__73"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__73_index1" on "obj_index_dbl__73" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__73_index2" on "obj_index_dbl__73" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__74" cascade;

create table "obj_index_dbl__74"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__74_index1" on "obj_index_dbl__74" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__74_index2" on "obj_index_dbl__74" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__75" cascade;

create table "obj_index_dbl__75"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__75_index1" on "obj_index_dbl__75" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__75_index2" on "obj_index_dbl__75" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__76" cascade;

create table "obj_index_dbl__76"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__76_index1" on "obj_index_dbl__76" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__76_index2" on "obj_index_dbl__76" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__77" cascade;

create table "obj_index_dbl__77"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__77_index1" on "obj_index_dbl__77" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__77_index2" on "obj_index_dbl__77" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__78" cascade;

create table "obj_index_dbl__78"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__78_index1" on "obj_index_dbl__78" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__78_index2" on "obj_index_dbl__78" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__79" cascade;

create table "obj_index_dbl__79"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__79_index1" on "obj_index_dbl__79" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__79_index2" on "obj_index_dbl__79" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__80" cascade;

create table "obj_index_dbl__80"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__80_index1" on "obj_index_dbl__80" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__80_index2" on "obj_index_dbl__80" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__81" cascade;

create table "obj_index_dbl__81"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__81_index1" on "obj_index_dbl__81" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__81_index2" on "obj_index_dbl__81" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__82" cascade;

create table "obj_index_dbl__82"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__82_index1" on "obj_index_dbl__82" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__82_index2" on "obj_index_dbl__82" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__83" cascade;

create table "obj_index_dbl__83"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__83_index1" on "obj_index_dbl__83" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__83_index2" on "obj_index_dbl__83" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__84" cascade;

create table "obj_index_dbl__84"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__84_index1" on "obj_index_dbl__84" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__84_index2" on "obj_index_dbl__84" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__85" cascade;

create table "obj_index_dbl__85"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__85_index1" on "obj_index_dbl__85" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__85_index2" on "obj_index_dbl__85" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__86" cascade;

create table "obj_index_dbl__86"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__86_index1" on "obj_index_dbl__86" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__86_index2" on "obj_index_dbl__86" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__87" cascade;

create table "obj_index_dbl__87"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__87_index1" on "obj_index_dbl__87" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__87_index2" on "obj_index_dbl__87" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__88" cascade;

create table "obj_index_dbl__88"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__88_index1" on "obj_index_dbl__88" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__88_index2" on "obj_index_dbl__88" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__89" cascade;

create table "obj_index_dbl__89"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__89_index1" on "obj_index_dbl__89" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__89_index2" on "obj_index_dbl__89" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__90" cascade;

create table "obj_index_dbl__90"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__90_index1" on "obj_index_dbl__90" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__90_index2" on "obj_index_dbl__90" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__91" cascade;

create table "obj_index_dbl__91"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__91_index1" on "obj_index_dbl__91" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__91_index2" on "obj_index_dbl__91" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__92" cascade;

create table "obj_index_dbl__92"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__92_index1" on "obj_index_dbl__92" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__92_index2" on "obj_index_dbl__92" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__93" cascade;

create table "obj_index_dbl__93"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__93_index1" on "obj_index_dbl__93" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__93_index2" on "obj_index_dbl__93" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__94" cascade;

create table "obj_index_dbl__94"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__94_index1" on "obj_index_dbl__94" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__94_index2" on "obj_index_dbl__94" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__95" cascade;

create table "obj_index_dbl__95"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__95_index1" on "obj_index_dbl__95" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__95_index2" on "obj_index_dbl__95" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__96" cascade;

create table "obj_index_dbl__96"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__96_index1" on "obj_index_dbl__96" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__96_index2" on "obj_index_dbl__96" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__97" cascade;

create table "obj_index_dbl__97"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__97_index1" on "obj_index_dbl__97" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__97_index2" on "obj_index_dbl__97" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__98" cascade;

create table "obj_index_dbl__98"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__98_index1" on "obj_index_dbl__98" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__98_index2" on "obj_index_dbl__98" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__99" cascade;

create table "obj_index_dbl__99"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__99_index1" on "obj_index_dbl__99" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__99_index2" on "obj_index_dbl__99" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__100" cascade;

create table "obj_index_dbl__100"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__100_index1" on "obj_index_dbl__100" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__100_index2" on "obj_index_dbl__100" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__101" cascade;

create table "obj_index_dbl__101"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__101_index1" on "obj_index_dbl__101" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__101_index2" on "obj_index_dbl__101" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__102" cascade;

create table "obj_index_dbl__102"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__102_index1" on "obj_index_dbl__102" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__102_index2" on "obj_index_dbl__102" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__103" cascade;

create table "obj_index_dbl__103"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__103_index1" on "obj_index_dbl__103" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__103_index2" on "obj_index_dbl__103" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__104" cascade;

create table "obj_index_dbl__104"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__104_index1" on "obj_index_dbl__104" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__104_index2" on "obj_index_dbl__104" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__105" cascade;

create table "obj_index_dbl__105"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__105_index1" on "obj_index_dbl__105" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__105_index2" on "obj_index_dbl__105" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__106" cascade;

create table "obj_index_dbl__106"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__106_index1" on "obj_index_dbl__106" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__106_index2" on "obj_index_dbl__106" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__107" cascade;

create table "obj_index_dbl__107"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__107_index1" on "obj_index_dbl__107" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__107_index2" on "obj_index_dbl__107" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__108" cascade;

create table "obj_index_dbl__108"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__108_index1" on "obj_index_dbl__108" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__108_index2" on "obj_index_dbl__108" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__109" cascade;

create table "obj_index_dbl__109"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__109_index1" on "obj_index_dbl__109" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__109_index2" on "obj_index_dbl__109" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__110" cascade;

create table "obj_index_dbl__110"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__110_index1" on "obj_index_dbl__110" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__110_index2" on "obj_index_dbl__110" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__111" cascade;

create table "obj_index_dbl__111"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__111_index1" on "obj_index_dbl__111" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__111_index2" on "obj_index_dbl__111" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__112" cascade;

create table "obj_index_dbl__112"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__112_index1" on "obj_index_dbl__112" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__112_index2" on "obj_index_dbl__112" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__113" cascade;

create table "obj_index_dbl__113"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__113_index1" on "obj_index_dbl__113" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__113_index2" on "obj_index_dbl__113" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__114" cascade;

create table "obj_index_dbl__114"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__114_index1" on "obj_index_dbl__114" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__114_index2" on "obj_index_dbl__114" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__115" cascade;

create table "obj_index_dbl__115"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__115_index1" on "obj_index_dbl__115" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__115_index2" on "obj_index_dbl__115" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__116" cascade;

create table "obj_index_dbl__116"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__116_index1" on "obj_index_dbl__116" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__116_index2" on "obj_index_dbl__116" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__117" cascade;

create table "obj_index_dbl__117"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__117_index1" on "obj_index_dbl__117" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__117_index2" on "obj_index_dbl__117" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__118" cascade;

create table "obj_index_dbl__118"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__118_index1" on "obj_index_dbl__118" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__118_index2" on "obj_index_dbl__118" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__119" cascade;

create table "obj_index_dbl__119"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__119_index1" on "obj_index_dbl__119" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__119_index2" on "obj_index_dbl__119" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__120" cascade;

create table "obj_index_dbl__120"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__120_index1" on "obj_index_dbl__120" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__120_index2" on "obj_index_dbl__120" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__121" cascade;

create table "obj_index_dbl__121"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__121_index1" on "obj_index_dbl__121" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__121_index2" on "obj_index_dbl__121" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__122" cascade;

create table "obj_index_dbl__122"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__122_index1" on "obj_index_dbl__122" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__122_index2" on "obj_index_dbl__122" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__123" cascade;

create table "obj_index_dbl__123"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__123_index1" on "obj_index_dbl__123" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__123_index2" on "obj_index_dbl__123" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__124" cascade;

create table "obj_index_dbl__124"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__124_index1" on "obj_index_dbl__124" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__124_index2" on "obj_index_dbl__124" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__125" cascade;

create table "obj_index_dbl__125"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__125_index1" on "obj_index_dbl__125" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__125_index2" on "obj_index_dbl__125" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__126" cascade;

create table "obj_index_dbl__126"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__126_index1" on "obj_index_dbl__126" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__126_index2" on "obj_index_dbl__126" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_DBL */
drop table if exists "obj_index_dbl__127" cascade;

create table "obj_index_dbl__127"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" double precision
)

;

create index "obj_index_dbl__127_index1" on "obj_index_dbl__127" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_dbl__127_index2" on "obj_index_dbl__127" ("tenant_id", "obj_def_id", "obj_id");
