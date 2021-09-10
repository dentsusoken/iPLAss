/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str" cascade;

create table "obj_index_str"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str_index1" on "obj_index_str" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str_index2" on "obj_index_str" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__1" cascade;

create table "obj_index_str__1"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__1_index1" on "obj_index_str__1" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__1_index2" on "obj_index_str__1" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__2" cascade;

create table "obj_index_str__2"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__2_index1" on "obj_index_str__2" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__2_index2" on "obj_index_str__2" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__3" cascade;

create table "obj_index_str__3"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__3_index1" on "obj_index_str__3" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__3_index2" on "obj_index_str__3" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__4" cascade;

create table "obj_index_str__4"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__4_index1" on "obj_index_str__4" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__4_index2" on "obj_index_str__4" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__5" cascade;

create table "obj_index_str__5"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__5_index1" on "obj_index_str__5" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__5_index2" on "obj_index_str__5" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__6" cascade;

create table "obj_index_str__6"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__6_index1" on "obj_index_str__6" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__6_index2" on "obj_index_str__6" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__7" cascade;

create table "obj_index_str__7"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__7_index1" on "obj_index_str__7" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__7_index2" on "obj_index_str__7" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__8" cascade;

create table "obj_index_str__8"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__8_index1" on "obj_index_str__8" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__8_index2" on "obj_index_str__8" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__9" cascade;

create table "obj_index_str__9"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__9_index1" on "obj_index_str__9" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__9_index2" on "obj_index_str__9" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__10" cascade;

create table "obj_index_str__10"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__10_index1" on "obj_index_str__10" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__10_index2" on "obj_index_str__10" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__11" cascade;

create table "obj_index_str__11"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__11_index1" on "obj_index_str__11" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__11_index2" on "obj_index_str__11" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__12" cascade;

create table "obj_index_str__12"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__12_index1" on "obj_index_str__12" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__12_index2" on "obj_index_str__12" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__13" cascade;

create table "obj_index_str__13"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__13_index1" on "obj_index_str__13" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__13_index2" on "obj_index_str__13" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__14" cascade;

create table "obj_index_str__14"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__14_index1" on "obj_index_str__14" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__14_index2" on "obj_index_str__14" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__15" cascade;

create table "obj_index_str__15"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__15_index1" on "obj_index_str__15" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__15_index2" on "obj_index_str__15" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__16" cascade;

create table "obj_index_str__16"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__16_index1" on "obj_index_str__16" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__16_index2" on "obj_index_str__16" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__17" cascade;

create table "obj_index_str__17"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__17_index1" on "obj_index_str__17" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__17_index2" on "obj_index_str__17" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__18" cascade;

create table "obj_index_str__18"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__18_index1" on "obj_index_str__18" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__18_index2" on "obj_index_str__18" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__19" cascade;

create table "obj_index_str__19"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__19_index1" on "obj_index_str__19" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__19_index2" on "obj_index_str__19" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__20" cascade;

create table "obj_index_str__20"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__20_index1" on "obj_index_str__20" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__20_index2" on "obj_index_str__20" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__21" cascade;

create table "obj_index_str__21"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__21_index1" on "obj_index_str__21" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__21_index2" on "obj_index_str__21" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__22" cascade;

create table "obj_index_str__22"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__22_index1" on "obj_index_str__22" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__22_index2" on "obj_index_str__22" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__23" cascade;

create table "obj_index_str__23"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__23_index1" on "obj_index_str__23" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__23_index2" on "obj_index_str__23" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__24" cascade;

create table "obj_index_str__24"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__24_index1" on "obj_index_str__24" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__24_index2" on "obj_index_str__24" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__25" cascade;

create table "obj_index_str__25"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__25_index1" on "obj_index_str__25" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__25_index2" on "obj_index_str__25" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__26" cascade;

create table "obj_index_str__26"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__26_index1" on "obj_index_str__26" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__26_index2" on "obj_index_str__26" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__27" cascade;

create table "obj_index_str__27"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__27_index1" on "obj_index_str__27" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__27_index2" on "obj_index_str__27" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__28" cascade;

create table "obj_index_str__28"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__28_index1" on "obj_index_str__28" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__28_index2" on "obj_index_str__28" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__29" cascade;

create table "obj_index_str__29"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__29_index1" on "obj_index_str__29" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__29_index2" on "obj_index_str__29" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__30" cascade;

create table "obj_index_str__30"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__30_index1" on "obj_index_str__30" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__30_index2" on "obj_index_str__30" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__31" cascade;

create table "obj_index_str__31"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__31_index1" on "obj_index_str__31" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__31_index2" on "obj_index_str__31" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__32" cascade;

create table "obj_index_str__32"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__32_index1" on "obj_index_str__32" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__32_index2" on "obj_index_str__32" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__33" cascade;

create table "obj_index_str__33"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__33_index1" on "obj_index_str__33" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__33_index2" on "obj_index_str__33" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__34" cascade;

create table "obj_index_str__34"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__34_index1" on "obj_index_str__34" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__34_index2" on "obj_index_str__34" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__35" cascade;

create table "obj_index_str__35"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__35_index1" on "obj_index_str__35" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__35_index2" on "obj_index_str__35" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__36" cascade;

create table "obj_index_str__36"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__36_index1" on "obj_index_str__36" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__36_index2" on "obj_index_str__36" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__37" cascade;

create table "obj_index_str__37"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__37_index1" on "obj_index_str__37" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__37_index2" on "obj_index_str__37" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__38" cascade;

create table "obj_index_str__38"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__38_index1" on "obj_index_str__38" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__38_index2" on "obj_index_str__38" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__39" cascade;

create table "obj_index_str__39"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__39_index1" on "obj_index_str__39" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__39_index2" on "obj_index_str__39" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__40" cascade;

create table "obj_index_str__40"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__40_index1" on "obj_index_str__40" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__40_index2" on "obj_index_str__40" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__41" cascade;

create table "obj_index_str__41"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__41_index1" on "obj_index_str__41" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__41_index2" on "obj_index_str__41" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__42" cascade;

create table "obj_index_str__42"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__42_index1" on "obj_index_str__42" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__42_index2" on "obj_index_str__42" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__43" cascade;

create table "obj_index_str__43"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__43_index1" on "obj_index_str__43" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__43_index2" on "obj_index_str__43" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__44" cascade;

create table "obj_index_str__44"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__44_index1" on "obj_index_str__44" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__44_index2" on "obj_index_str__44" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__45" cascade;

create table "obj_index_str__45"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__45_index1" on "obj_index_str__45" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__45_index2" on "obj_index_str__45" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__46" cascade;

create table "obj_index_str__46"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__46_index1" on "obj_index_str__46" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__46_index2" on "obj_index_str__46" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__47" cascade;

create table "obj_index_str__47"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__47_index1" on "obj_index_str__47" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__47_index2" on "obj_index_str__47" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__48" cascade;

create table "obj_index_str__48"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__48_index1" on "obj_index_str__48" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__48_index2" on "obj_index_str__48" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__49" cascade;

create table "obj_index_str__49"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__49_index1" on "obj_index_str__49" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__49_index2" on "obj_index_str__49" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__50" cascade;

create table "obj_index_str__50"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__50_index1" on "obj_index_str__50" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__50_index2" on "obj_index_str__50" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__51" cascade;

create table "obj_index_str__51"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__51_index1" on "obj_index_str__51" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__51_index2" on "obj_index_str__51" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__52" cascade;

create table "obj_index_str__52"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__52_index1" on "obj_index_str__52" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__52_index2" on "obj_index_str__52" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__53" cascade;

create table "obj_index_str__53"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__53_index1" on "obj_index_str__53" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__53_index2" on "obj_index_str__53" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__54" cascade;

create table "obj_index_str__54"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__54_index1" on "obj_index_str__54" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__54_index2" on "obj_index_str__54" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__55" cascade;

create table "obj_index_str__55"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__55_index1" on "obj_index_str__55" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__55_index2" on "obj_index_str__55" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__56" cascade;

create table "obj_index_str__56"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__56_index1" on "obj_index_str__56" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__56_index2" on "obj_index_str__56" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__57" cascade;

create table "obj_index_str__57"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__57_index1" on "obj_index_str__57" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__57_index2" on "obj_index_str__57" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__58" cascade;

create table "obj_index_str__58"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__58_index1" on "obj_index_str__58" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__58_index2" on "obj_index_str__58" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__59" cascade;

create table "obj_index_str__59"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__59_index1" on "obj_index_str__59" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__59_index2" on "obj_index_str__59" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__60" cascade;

create table "obj_index_str__60"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__60_index1" on "obj_index_str__60" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__60_index2" on "obj_index_str__60" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__61" cascade;

create table "obj_index_str__61"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__61_index1" on "obj_index_str__61" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__61_index2" on "obj_index_str__61" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__62" cascade;

create table "obj_index_str__62"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__62_index1" on "obj_index_str__62" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__62_index2" on "obj_index_str__62" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__63" cascade;

create table "obj_index_str__63"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__63_index1" on "obj_index_str__63" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__63_index2" on "obj_index_str__63" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__64" cascade;

create table "obj_index_str__64"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__64_index1" on "obj_index_str__64" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__64_index2" on "obj_index_str__64" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__65" cascade;

create table "obj_index_str__65"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__65_index1" on "obj_index_str__65" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__65_index2" on "obj_index_str__65" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__66" cascade;

create table "obj_index_str__66"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__66_index1" on "obj_index_str__66" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__66_index2" on "obj_index_str__66" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__67" cascade;

create table "obj_index_str__67"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__67_index1" on "obj_index_str__67" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__67_index2" on "obj_index_str__67" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__68" cascade;

create table "obj_index_str__68"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__68_index1" on "obj_index_str__68" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__68_index2" on "obj_index_str__68" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__69" cascade;

create table "obj_index_str__69"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__69_index1" on "obj_index_str__69" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__69_index2" on "obj_index_str__69" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__70" cascade;

create table "obj_index_str__70"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__70_index1" on "obj_index_str__70" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__70_index2" on "obj_index_str__70" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__71" cascade;

create table "obj_index_str__71"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__71_index1" on "obj_index_str__71" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__71_index2" on "obj_index_str__71" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__72" cascade;

create table "obj_index_str__72"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__72_index1" on "obj_index_str__72" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__72_index2" on "obj_index_str__72" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__73" cascade;

create table "obj_index_str__73"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__73_index1" on "obj_index_str__73" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__73_index2" on "obj_index_str__73" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__74" cascade;

create table "obj_index_str__74"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__74_index1" on "obj_index_str__74" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__74_index2" on "obj_index_str__74" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__75" cascade;

create table "obj_index_str__75"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__75_index1" on "obj_index_str__75" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__75_index2" on "obj_index_str__75" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__76" cascade;

create table "obj_index_str__76"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__76_index1" on "obj_index_str__76" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__76_index2" on "obj_index_str__76" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__77" cascade;

create table "obj_index_str__77"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__77_index1" on "obj_index_str__77" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__77_index2" on "obj_index_str__77" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__78" cascade;

create table "obj_index_str__78"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__78_index1" on "obj_index_str__78" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__78_index2" on "obj_index_str__78" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__79" cascade;

create table "obj_index_str__79"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__79_index1" on "obj_index_str__79" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__79_index2" on "obj_index_str__79" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__80" cascade;

create table "obj_index_str__80"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__80_index1" on "obj_index_str__80" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__80_index2" on "obj_index_str__80" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__81" cascade;

create table "obj_index_str__81"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__81_index1" on "obj_index_str__81" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__81_index2" on "obj_index_str__81" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__82" cascade;

create table "obj_index_str__82"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__82_index1" on "obj_index_str__82" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__82_index2" on "obj_index_str__82" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__83" cascade;

create table "obj_index_str__83"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__83_index1" on "obj_index_str__83" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__83_index2" on "obj_index_str__83" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__84" cascade;

create table "obj_index_str__84"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__84_index1" on "obj_index_str__84" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__84_index2" on "obj_index_str__84" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__85" cascade;

create table "obj_index_str__85"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__85_index1" on "obj_index_str__85" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__85_index2" on "obj_index_str__85" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__86" cascade;

create table "obj_index_str__86"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__86_index1" on "obj_index_str__86" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__86_index2" on "obj_index_str__86" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__87" cascade;

create table "obj_index_str__87"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__87_index1" on "obj_index_str__87" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__87_index2" on "obj_index_str__87" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__88" cascade;

create table "obj_index_str__88"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__88_index1" on "obj_index_str__88" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__88_index2" on "obj_index_str__88" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__89" cascade;

create table "obj_index_str__89"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__89_index1" on "obj_index_str__89" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__89_index2" on "obj_index_str__89" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__90" cascade;

create table "obj_index_str__90"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__90_index1" on "obj_index_str__90" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__90_index2" on "obj_index_str__90" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__91" cascade;

create table "obj_index_str__91"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__91_index1" on "obj_index_str__91" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__91_index2" on "obj_index_str__91" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__92" cascade;

create table "obj_index_str__92"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__92_index1" on "obj_index_str__92" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__92_index2" on "obj_index_str__92" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__93" cascade;

create table "obj_index_str__93"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__93_index1" on "obj_index_str__93" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__93_index2" on "obj_index_str__93" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__94" cascade;

create table "obj_index_str__94"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__94_index1" on "obj_index_str__94" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__94_index2" on "obj_index_str__94" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__95" cascade;

create table "obj_index_str__95"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__95_index1" on "obj_index_str__95" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__95_index2" on "obj_index_str__95" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__96" cascade;

create table "obj_index_str__96"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__96_index1" on "obj_index_str__96" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__96_index2" on "obj_index_str__96" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__97" cascade;

create table "obj_index_str__97"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__97_index1" on "obj_index_str__97" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__97_index2" on "obj_index_str__97" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__98" cascade;

create table "obj_index_str__98"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__98_index1" on "obj_index_str__98" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__98_index2" on "obj_index_str__98" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__99" cascade;

create table "obj_index_str__99"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__99_index1" on "obj_index_str__99" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__99_index2" on "obj_index_str__99" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__100" cascade;

create table "obj_index_str__100"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__100_index1" on "obj_index_str__100" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__100_index2" on "obj_index_str__100" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__101" cascade;

create table "obj_index_str__101"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__101_index1" on "obj_index_str__101" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__101_index2" on "obj_index_str__101" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__102" cascade;

create table "obj_index_str__102"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__102_index1" on "obj_index_str__102" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__102_index2" on "obj_index_str__102" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__103" cascade;

create table "obj_index_str__103"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__103_index1" on "obj_index_str__103" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__103_index2" on "obj_index_str__103" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__104" cascade;

create table "obj_index_str__104"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__104_index1" on "obj_index_str__104" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__104_index2" on "obj_index_str__104" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__105" cascade;

create table "obj_index_str__105"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__105_index1" on "obj_index_str__105" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__105_index2" on "obj_index_str__105" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__106" cascade;

create table "obj_index_str__106"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__106_index1" on "obj_index_str__106" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__106_index2" on "obj_index_str__106" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__107" cascade;

create table "obj_index_str__107"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__107_index1" on "obj_index_str__107" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__107_index2" on "obj_index_str__107" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__108" cascade;

create table "obj_index_str__108"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__108_index1" on "obj_index_str__108" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__108_index2" on "obj_index_str__108" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__109" cascade;

create table "obj_index_str__109"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__109_index1" on "obj_index_str__109" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__109_index2" on "obj_index_str__109" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__110" cascade;

create table "obj_index_str__110"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__110_index1" on "obj_index_str__110" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__110_index2" on "obj_index_str__110" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__111" cascade;

create table "obj_index_str__111"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__111_index1" on "obj_index_str__111" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__111_index2" on "obj_index_str__111" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__112" cascade;

create table "obj_index_str__112"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__112_index1" on "obj_index_str__112" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__112_index2" on "obj_index_str__112" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__113" cascade;

create table "obj_index_str__113"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__113_index1" on "obj_index_str__113" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__113_index2" on "obj_index_str__113" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__114" cascade;

create table "obj_index_str__114"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__114_index1" on "obj_index_str__114" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__114_index2" on "obj_index_str__114" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__115" cascade;

create table "obj_index_str__115"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__115_index1" on "obj_index_str__115" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__115_index2" on "obj_index_str__115" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__116" cascade;

create table "obj_index_str__116"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__116_index1" on "obj_index_str__116" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__116_index2" on "obj_index_str__116" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__117" cascade;

create table "obj_index_str__117"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__117_index1" on "obj_index_str__117" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__117_index2" on "obj_index_str__117" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__118" cascade;

create table "obj_index_str__118"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__118_index1" on "obj_index_str__118" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__118_index2" on "obj_index_str__118" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__119" cascade;

create table "obj_index_str__119"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__119_index1" on "obj_index_str__119" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__119_index2" on "obj_index_str__119" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__120" cascade;

create table "obj_index_str__120"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__120_index1" on "obj_index_str__120" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__120_index2" on "obj_index_str__120" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__121" cascade;

create table "obj_index_str__121"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__121_index1" on "obj_index_str__121" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__121_index2" on "obj_index_str__121" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__122" cascade;

create table "obj_index_str__122"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__122_index1" on "obj_index_str__122" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__122_index2" on "obj_index_str__122" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__123" cascade;

create table "obj_index_str__123"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__123_index1" on "obj_index_str__123" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__123_index2" on "obj_index_str__123" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__124" cascade;

create table "obj_index_str__124"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__124_index1" on "obj_index_str__124" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__124_index2" on "obj_index_str__124" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__125" cascade;

create table "obj_index_str__125"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__125_index1" on "obj_index_str__125" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__125_index2" on "obj_index_str__125" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__126" cascade;

create table "obj_index_str__126"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__126_index1" on "obj_index_str__126" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__126_index2" on "obj_index_str__126" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_STR */
drop table if exists "obj_index_str__127" cascade;

create table "obj_index_str__127"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)

;

create index "obj_index_str__127_index1" on "obj_index_str__127" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str__127_index2" on "obj_index_str__127" ("tenant_id", "obj_def_id", "obj_id");
