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


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__32" cascade;

create table "obj_index_num__32"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__32_index1" on "obj_index_num__32" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__32_index2" on "obj_index_num__32" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__33" cascade;

create table "obj_index_num__33"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__33_index1" on "obj_index_num__33" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__33_index2" on "obj_index_num__33" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__34" cascade;

create table "obj_index_num__34"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__34_index1" on "obj_index_num__34" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__34_index2" on "obj_index_num__34" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__35" cascade;

create table "obj_index_num__35"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__35_index1" on "obj_index_num__35" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__35_index2" on "obj_index_num__35" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__36" cascade;

create table "obj_index_num__36"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__36_index1" on "obj_index_num__36" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__36_index2" on "obj_index_num__36" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__37" cascade;

create table "obj_index_num__37"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__37_index1" on "obj_index_num__37" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__37_index2" on "obj_index_num__37" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__38" cascade;

create table "obj_index_num__38"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__38_index1" on "obj_index_num__38" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__38_index2" on "obj_index_num__38" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__39" cascade;

create table "obj_index_num__39"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__39_index1" on "obj_index_num__39" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__39_index2" on "obj_index_num__39" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__40" cascade;

create table "obj_index_num__40"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__40_index1" on "obj_index_num__40" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__40_index2" on "obj_index_num__40" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__41" cascade;

create table "obj_index_num__41"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__41_index1" on "obj_index_num__41" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__41_index2" on "obj_index_num__41" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__42" cascade;

create table "obj_index_num__42"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__42_index1" on "obj_index_num__42" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__42_index2" on "obj_index_num__42" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__43" cascade;

create table "obj_index_num__43"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__43_index1" on "obj_index_num__43" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__43_index2" on "obj_index_num__43" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__44" cascade;

create table "obj_index_num__44"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__44_index1" on "obj_index_num__44" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__44_index2" on "obj_index_num__44" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__45" cascade;

create table "obj_index_num__45"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__45_index1" on "obj_index_num__45" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__45_index2" on "obj_index_num__45" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__46" cascade;

create table "obj_index_num__46"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__46_index1" on "obj_index_num__46" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__46_index2" on "obj_index_num__46" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__47" cascade;

create table "obj_index_num__47"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__47_index1" on "obj_index_num__47" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__47_index2" on "obj_index_num__47" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__48" cascade;

create table "obj_index_num__48"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__48_index1" on "obj_index_num__48" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__48_index2" on "obj_index_num__48" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__49" cascade;

create table "obj_index_num__49"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__49_index1" on "obj_index_num__49" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__49_index2" on "obj_index_num__49" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__50" cascade;

create table "obj_index_num__50"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__50_index1" on "obj_index_num__50" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__50_index2" on "obj_index_num__50" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__51" cascade;

create table "obj_index_num__51"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__51_index1" on "obj_index_num__51" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__51_index2" on "obj_index_num__51" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__52" cascade;

create table "obj_index_num__52"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__52_index1" on "obj_index_num__52" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__52_index2" on "obj_index_num__52" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__53" cascade;

create table "obj_index_num__53"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__53_index1" on "obj_index_num__53" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__53_index2" on "obj_index_num__53" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__54" cascade;

create table "obj_index_num__54"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__54_index1" on "obj_index_num__54" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__54_index2" on "obj_index_num__54" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__55" cascade;

create table "obj_index_num__55"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__55_index1" on "obj_index_num__55" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__55_index2" on "obj_index_num__55" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__56" cascade;

create table "obj_index_num__56"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__56_index1" on "obj_index_num__56" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__56_index2" on "obj_index_num__56" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__57" cascade;

create table "obj_index_num__57"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__57_index1" on "obj_index_num__57" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__57_index2" on "obj_index_num__57" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__58" cascade;

create table "obj_index_num__58"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__58_index1" on "obj_index_num__58" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__58_index2" on "obj_index_num__58" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__59" cascade;

create table "obj_index_num__59"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__59_index1" on "obj_index_num__59" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__59_index2" on "obj_index_num__59" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__60" cascade;

create table "obj_index_num__60"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__60_index1" on "obj_index_num__60" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__60_index2" on "obj_index_num__60" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__61" cascade;

create table "obj_index_num__61"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__61_index1" on "obj_index_num__61" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__61_index2" on "obj_index_num__61" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__62" cascade;

create table "obj_index_num__62"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__62_index1" on "obj_index_num__62" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__62_index2" on "obj_index_num__62" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__63" cascade;

create table "obj_index_num__63"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__63_index1" on "obj_index_num__63" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__63_index2" on "obj_index_num__63" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__64" cascade;

create table "obj_index_num__64"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__64_index1" on "obj_index_num__64" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__64_index2" on "obj_index_num__64" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__65" cascade;

create table "obj_index_num__65"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__65_index1" on "obj_index_num__65" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__65_index2" on "obj_index_num__65" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__66" cascade;

create table "obj_index_num__66"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__66_index1" on "obj_index_num__66" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__66_index2" on "obj_index_num__66" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__67" cascade;

create table "obj_index_num__67"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__67_index1" on "obj_index_num__67" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__67_index2" on "obj_index_num__67" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__68" cascade;

create table "obj_index_num__68"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__68_index1" on "obj_index_num__68" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__68_index2" on "obj_index_num__68" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__69" cascade;

create table "obj_index_num__69"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__69_index1" on "obj_index_num__69" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__69_index2" on "obj_index_num__69" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__70" cascade;

create table "obj_index_num__70"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__70_index1" on "obj_index_num__70" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__70_index2" on "obj_index_num__70" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__71" cascade;

create table "obj_index_num__71"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__71_index1" on "obj_index_num__71" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__71_index2" on "obj_index_num__71" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__72" cascade;

create table "obj_index_num__72"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__72_index1" on "obj_index_num__72" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__72_index2" on "obj_index_num__72" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__73" cascade;

create table "obj_index_num__73"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__73_index1" on "obj_index_num__73" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__73_index2" on "obj_index_num__73" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__74" cascade;

create table "obj_index_num__74"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__74_index1" on "obj_index_num__74" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__74_index2" on "obj_index_num__74" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__75" cascade;

create table "obj_index_num__75"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__75_index1" on "obj_index_num__75" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__75_index2" on "obj_index_num__75" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__76" cascade;

create table "obj_index_num__76"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__76_index1" on "obj_index_num__76" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__76_index2" on "obj_index_num__76" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__77" cascade;

create table "obj_index_num__77"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__77_index1" on "obj_index_num__77" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__77_index2" on "obj_index_num__77" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__78" cascade;

create table "obj_index_num__78"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__78_index1" on "obj_index_num__78" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__78_index2" on "obj_index_num__78" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__79" cascade;

create table "obj_index_num__79"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__79_index1" on "obj_index_num__79" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__79_index2" on "obj_index_num__79" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__80" cascade;

create table "obj_index_num__80"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__80_index1" on "obj_index_num__80" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__80_index2" on "obj_index_num__80" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__81" cascade;

create table "obj_index_num__81"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__81_index1" on "obj_index_num__81" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__81_index2" on "obj_index_num__81" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__82" cascade;

create table "obj_index_num__82"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__82_index1" on "obj_index_num__82" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__82_index2" on "obj_index_num__82" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__83" cascade;

create table "obj_index_num__83"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__83_index1" on "obj_index_num__83" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__83_index2" on "obj_index_num__83" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__84" cascade;

create table "obj_index_num__84"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__84_index1" on "obj_index_num__84" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__84_index2" on "obj_index_num__84" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__85" cascade;

create table "obj_index_num__85"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__85_index1" on "obj_index_num__85" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__85_index2" on "obj_index_num__85" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__86" cascade;

create table "obj_index_num__86"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__86_index1" on "obj_index_num__86" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__86_index2" on "obj_index_num__86" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__87" cascade;

create table "obj_index_num__87"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__87_index1" on "obj_index_num__87" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__87_index2" on "obj_index_num__87" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__88" cascade;

create table "obj_index_num__88"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__88_index1" on "obj_index_num__88" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__88_index2" on "obj_index_num__88" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__89" cascade;

create table "obj_index_num__89"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__89_index1" on "obj_index_num__89" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__89_index2" on "obj_index_num__89" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__90" cascade;

create table "obj_index_num__90"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__90_index1" on "obj_index_num__90" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__90_index2" on "obj_index_num__90" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__91" cascade;

create table "obj_index_num__91"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__91_index1" on "obj_index_num__91" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__91_index2" on "obj_index_num__91" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__92" cascade;

create table "obj_index_num__92"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__92_index1" on "obj_index_num__92" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__92_index2" on "obj_index_num__92" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__93" cascade;

create table "obj_index_num__93"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__93_index1" on "obj_index_num__93" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__93_index2" on "obj_index_num__93" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__94" cascade;

create table "obj_index_num__94"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__94_index1" on "obj_index_num__94" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__94_index2" on "obj_index_num__94" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__95" cascade;

create table "obj_index_num__95"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__95_index1" on "obj_index_num__95" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__95_index2" on "obj_index_num__95" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__96" cascade;

create table "obj_index_num__96"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__96_index1" on "obj_index_num__96" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__96_index2" on "obj_index_num__96" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__97" cascade;

create table "obj_index_num__97"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__97_index1" on "obj_index_num__97" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__97_index2" on "obj_index_num__97" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__98" cascade;

create table "obj_index_num__98"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__98_index1" on "obj_index_num__98" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__98_index2" on "obj_index_num__98" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__99" cascade;

create table "obj_index_num__99"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__99_index1" on "obj_index_num__99" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__99_index2" on "obj_index_num__99" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__100" cascade;

create table "obj_index_num__100"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__100_index1" on "obj_index_num__100" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__100_index2" on "obj_index_num__100" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__101" cascade;

create table "obj_index_num__101"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__101_index1" on "obj_index_num__101" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__101_index2" on "obj_index_num__101" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__102" cascade;

create table "obj_index_num__102"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__102_index1" on "obj_index_num__102" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__102_index2" on "obj_index_num__102" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__103" cascade;

create table "obj_index_num__103"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__103_index1" on "obj_index_num__103" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__103_index2" on "obj_index_num__103" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__104" cascade;

create table "obj_index_num__104"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__104_index1" on "obj_index_num__104" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__104_index2" on "obj_index_num__104" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__105" cascade;

create table "obj_index_num__105"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__105_index1" on "obj_index_num__105" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__105_index2" on "obj_index_num__105" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__106" cascade;

create table "obj_index_num__106"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__106_index1" on "obj_index_num__106" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__106_index2" on "obj_index_num__106" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__107" cascade;

create table "obj_index_num__107"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__107_index1" on "obj_index_num__107" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__107_index2" on "obj_index_num__107" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__108" cascade;

create table "obj_index_num__108"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__108_index1" on "obj_index_num__108" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__108_index2" on "obj_index_num__108" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__109" cascade;

create table "obj_index_num__109"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__109_index1" on "obj_index_num__109" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__109_index2" on "obj_index_num__109" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__110" cascade;

create table "obj_index_num__110"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__110_index1" on "obj_index_num__110" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__110_index2" on "obj_index_num__110" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__111" cascade;

create table "obj_index_num__111"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__111_index1" on "obj_index_num__111" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__111_index2" on "obj_index_num__111" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__112" cascade;

create table "obj_index_num__112"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__112_index1" on "obj_index_num__112" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__112_index2" on "obj_index_num__112" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__113" cascade;

create table "obj_index_num__113"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__113_index1" on "obj_index_num__113" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__113_index2" on "obj_index_num__113" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__114" cascade;

create table "obj_index_num__114"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__114_index1" on "obj_index_num__114" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__114_index2" on "obj_index_num__114" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__115" cascade;

create table "obj_index_num__115"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__115_index1" on "obj_index_num__115" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__115_index2" on "obj_index_num__115" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__116" cascade;

create table "obj_index_num__116"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__116_index1" on "obj_index_num__116" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__116_index2" on "obj_index_num__116" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__117" cascade;

create table "obj_index_num__117"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__117_index1" on "obj_index_num__117" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__117_index2" on "obj_index_num__117" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__118" cascade;

create table "obj_index_num__118"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__118_index1" on "obj_index_num__118" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__118_index2" on "obj_index_num__118" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__119" cascade;

create table "obj_index_num__119"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__119_index1" on "obj_index_num__119" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__119_index2" on "obj_index_num__119" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__120" cascade;

create table "obj_index_num__120"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__120_index1" on "obj_index_num__120" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__120_index2" on "obj_index_num__120" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__121" cascade;

create table "obj_index_num__121"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__121_index1" on "obj_index_num__121" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__121_index2" on "obj_index_num__121" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__122" cascade;

create table "obj_index_num__122"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__122_index1" on "obj_index_num__122" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__122_index2" on "obj_index_num__122" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__123" cascade;

create table "obj_index_num__123"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__123_index1" on "obj_index_num__123" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__123_index2" on "obj_index_num__123" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__124" cascade;

create table "obj_index_num__124"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__124_index1" on "obj_index_num__124" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__124_index2" on "obj_index_num__124" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__125" cascade;

create table "obj_index_num__125"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__125_index1" on "obj_index_num__125" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__125_index2" on "obj_index_num__125" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__126" cascade;

create table "obj_index_num__126"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__126_index1" on "obj_index_num__126" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__126_index2" on "obj_index_num__126" ("tenant_id", "obj_def_id", "obj_id");


/* drop/create OBJ_INDEX_NUM */
drop table if exists "obj_index_num__127" cascade;

create table "obj_index_num__127"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" numeric
)

;

create index "obj_index_num__127_index1" on "obj_index_num__127" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_num__127_index2" on "obj_index_num__127" ("tenant_id", "obj_def_id", "obj_id");
