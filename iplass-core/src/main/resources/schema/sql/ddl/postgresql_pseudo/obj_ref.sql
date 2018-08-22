/* drop/create OBJ_REF */
drop table if exists "obj_ref" cascade;
create table "obj_ref"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref_index1" on "obj_ref" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref_index2" on "obj_ref" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__1" cascade;
create table "obj_ref__1"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__1_index1" on "obj_ref__1" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__1_index2" on "obj_ref__1" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__2" cascade;
create table "obj_ref__2"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__2_index1" on "obj_ref__2" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__2_index2" on "obj_ref__2" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__3" cascade;
create table "obj_ref__3"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__3_index1" on "obj_ref__3" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__3_index2" on "obj_ref__3" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__4" cascade;
create table "obj_ref__4"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__4_index1" on "obj_ref__4" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__4_index2" on "obj_ref__4" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__5" cascade;
create table "obj_ref__5"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__5_index1" on "obj_ref__5" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__5_index2" on "obj_ref__5" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__6" cascade;
create table "obj_ref__6"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__6_index1" on "obj_ref__6" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__6_index2" on "obj_ref__6" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__7" cascade;
create table "obj_ref__7"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__7_index1" on "obj_ref__7" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__7_index2" on "obj_ref__7" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__8" cascade;
create table "obj_ref__8"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__8_index1" on "obj_ref__8" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__8_index2" on "obj_ref__8" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__9" cascade;
create table "obj_ref__9"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__9_index1" on "obj_ref__9" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__9_index2" on "obj_ref__9" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__10" cascade;
create table "obj_ref__10"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__10_index1" on "obj_ref__10" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__10_index2" on "obj_ref__10" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__11" cascade;
create table "obj_ref__11"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__11_index1" on "obj_ref__11" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__11_index2" on "obj_ref__11" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__12" cascade;
create table "obj_ref__12"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__12_index1" on "obj_ref__12" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__12_index2" on "obj_ref__12" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__13" cascade;
create table "obj_ref__13"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__13_index1" on "obj_ref__13" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__13_index2" on "obj_ref__13" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__14" cascade;
create table "obj_ref__14"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__14_index1" on "obj_ref__14" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__14_index2" on "obj_ref__14" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__15" cascade;
create table "obj_ref__15"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__15_index1" on "obj_ref__15" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__15_index2" on "obj_ref__15" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__16" cascade;
create table "obj_ref__16"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__16_index1" on "obj_ref__16" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__16_index2" on "obj_ref__16" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__17" cascade;
create table "obj_ref__17"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__17_index1" on "obj_ref__17" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__17_index2" on "obj_ref__17" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__18" cascade;
create table "obj_ref__18"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__18_index1" on "obj_ref__18" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__18_index2" on "obj_ref__18" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__19" cascade;
create table "obj_ref__19"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__19_index1" on "obj_ref__19" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__19_index2" on "obj_ref__19" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__20" cascade;
create table "obj_ref__20"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__20_index1" on "obj_ref__20" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__20_index2" on "obj_ref__20" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__21" cascade;
create table "obj_ref__21"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__21_index1" on "obj_ref__21" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__21_index2" on "obj_ref__21" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__22" cascade;
create table "obj_ref__22"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__22_index1" on "obj_ref__22" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__22_index2" on "obj_ref__22" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__23" cascade;
create table "obj_ref__23"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__23_index1" on "obj_ref__23" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__23_index2" on "obj_ref__23" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__24" cascade;
create table "obj_ref__24"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__24_index1" on "obj_ref__24" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__24_index2" on "obj_ref__24" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__25" cascade;
create table "obj_ref__25"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__25_index1" on "obj_ref__25" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__25_index2" on "obj_ref__25" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__26" cascade;
create table "obj_ref__26"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__26_index1" on "obj_ref__26" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__26_index2" on "obj_ref__26" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__27" cascade;
create table "obj_ref__27"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__27_index1" on "obj_ref__27" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__27_index2" on "obj_ref__27" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__28" cascade;
create table "obj_ref__28"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__28_index1" on "obj_ref__28" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__28_index2" on "obj_ref__28" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__29" cascade;
create table "obj_ref__29"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__29_index1" on "obj_ref__29" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__29_index2" on "obj_ref__29" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__30" cascade;
create table "obj_ref__30"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__30_index1" on "obj_ref__30" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__30_index2" on "obj_ref__30" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__31" cascade;
create table "obj_ref__31"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__31_index1" on "obj_ref__31" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__31_index2" on "obj_ref__31" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__mtp" cascade;
create table "obj_ref__mtp"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__mtp_index1" on "obj_ref__mtp" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__mtp_index2" on "obj_ref__mtp" ("tenant_id", "target_obj_def_id", "target_obj_id");

/* drop/create OBJ_REF */
drop table if exists "obj_ref__user" cascade;
create table "obj_ref__user"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)
;
create index "obj_ref__user_index1" on "obj_ref__user" ("tenant_id", "obj_def_id", "obj_id");
create index "obj_ref__user_index2" on "obj_ref__user" ("tenant_id", "target_obj_def_id", "target_obj_id");
