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

create index "obj_ref_index1" on "obj_ref" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref_index2" on "obj_ref" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__1_index1" on "obj_ref__1" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__1_index2" on "obj_ref__1" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__2_index1" on "obj_ref__2" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__2_index2" on "obj_ref__2" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__3_index1" on "obj_ref__3" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__3_index2" on "obj_ref__3" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__4_index1" on "obj_ref__4" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__4_index2" on "obj_ref__4" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__5_index1" on "obj_ref__5" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__5_index2" on "obj_ref__5" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__6_index1" on "obj_ref__6" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__6_index2" on "obj_ref__6" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__7_index1" on "obj_ref__7" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__7_index2" on "obj_ref__7" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__8_index1" on "obj_ref__8" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__8_index2" on "obj_ref__8" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__9_index1" on "obj_ref__9" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__9_index2" on "obj_ref__9" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__10_index1" on "obj_ref__10" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__10_index2" on "obj_ref__10" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__11_index1" on "obj_ref__11" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__11_index2" on "obj_ref__11" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__12_index1" on "obj_ref__12" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__12_index2" on "obj_ref__12" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__13_index1" on "obj_ref__13" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__13_index2" on "obj_ref__13" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__14_index1" on "obj_ref__14" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__14_index2" on "obj_ref__14" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__15_index1" on "obj_ref__15" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__15_index2" on "obj_ref__15" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__16_index1" on "obj_ref__16" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__16_index2" on "obj_ref__16" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__17_index1" on "obj_ref__17" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__17_index2" on "obj_ref__17" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__18_index1" on "obj_ref__18" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__18_index2" on "obj_ref__18" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__19_index1" on "obj_ref__19" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__19_index2" on "obj_ref__19" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__20_index1" on "obj_ref__20" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__20_index2" on "obj_ref__20" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__21_index1" on "obj_ref__21" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__21_index2" on "obj_ref__21" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__22_index1" on "obj_ref__22" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__22_index2" on "obj_ref__22" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__23_index1" on "obj_ref__23" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__23_index2" on "obj_ref__23" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__24_index1" on "obj_ref__24" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__24_index2" on "obj_ref__24" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__25_index1" on "obj_ref__25" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__25_index2" on "obj_ref__25" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__26_index1" on "obj_ref__26" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__26_index2" on "obj_ref__26" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__27_index1" on "obj_ref__27" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__27_index2" on "obj_ref__27" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__28_index1" on "obj_ref__28" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__28_index2" on "obj_ref__28" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__29_index1" on "obj_ref__29" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__29_index2" on "obj_ref__29" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__30_index1" on "obj_ref__30" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__30_index2" on "obj_ref__30" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__31_index1" on "obj_ref__31" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__31_index2" on "obj_ref__31" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__32" cascade;

create table "obj_ref__32"
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

create index "obj_ref__32_index1" on "obj_ref__32" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__32_index2" on "obj_ref__32" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__33" cascade;

create table "obj_ref__33"
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

create index "obj_ref__33_index1" on "obj_ref__33" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__33_index2" on "obj_ref__33" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__34" cascade;

create table "obj_ref__34"
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

create index "obj_ref__34_index1" on "obj_ref__34" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__34_index2" on "obj_ref__34" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__35" cascade;

create table "obj_ref__35"
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

create index "obj_ref__35_index1" on "obj_ref__35" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__35_index2" on "obj_ref__35" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__36" cascade;

create table "obj_ref__36"
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

create index "obj_ref__36_index1" on "obj_ref__36" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__36_index2" on "obj_ref__36" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__37" cascade;

create table "obj_ref__37"
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

create index "obj_ref__37_index1" on "obj_ref__37" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__37_index2" on "obj_ref__37" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__38" cascade;

create table "obj_ref__38"
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

create index "obj_ref__38_index1" on "obj_ref__38" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__38_index2" on "obj_ref__38" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__39" cascade;

create table "obj_ref__39"
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

create index "obj_ref__39_index1" on "obj_ref__39" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__39_index2" on "obj_ref__39" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__40" cascade;

create table "obj_ref__40"
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

create index "obj_ref__40_index1" on "obj_ref__40" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__40_index2" on "obj_ref__40" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__41" cascade;

create table "obj_ref__41"
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

create index "obj_ref__41_index1" on "obj_ref__41" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__41_index2" on "obj_ref__41" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__42" cascade;

create table "obj_ref__42"
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

create index "obj_ref__42_index1" on "obj_ref__42" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__42_index2" on "obj_ref__42" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__43" cascade;

create table "obj_ref__43"
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

create index "obj_ref__43_index1" on "obj_ref__43" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__43_index2" on "obj_ref__43" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__44" cascade;

create table "obj_ref__44"
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

create index "obj_ref__44_index1" on "obj_ref__44" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__44_index2" on "obj_ref__44" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__45" cascade;

create table "obj_ref__45"
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

create index "obj_ref__45_index1" on "obj_ref__45" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__45_index2" on "obj_ref__45" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__46" cascade;

create table "obj_ref__46"
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

create index "obj_ref__46_index1" on "obj_ref__46" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__46_index2" on "obj_ref__46" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__47" cascade;

create table "obj_ref__47"
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

create index "obj_ref__47_index1" on "obj_ref__47" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__47_index2" on "obj_ref__47" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__48" cascade;

create table "obj_ref__48"
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

create index "obj_ref__48_index1" on "obj_ref__48" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__48_index2" on "obj_ref__48" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__49" cascade;

create table "obj_ref__49"
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

create index "obj_ref__49_index1" on "obj_ref__49" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__49_index2" on "obj_ref__49" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__50" cascade;

create table "obj_ref__50"
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

create index "obj_ref__50_index1" on "obj_ref__50" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__50_index2" on "obj_ref__50" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__51" cascade;

create table "obj_ref__51"
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

create index "obj_ref__51_index1" on "obj_ref__51" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__51_index2" on "obj_ref__51" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__52" cascade;

create table "obj_ref__52"
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

create index "obj_ref__52_index1" on "obj_ref__52" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__52_index2" on "obj_ref__52" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__53" cascade;

create table "obj_ref__53"
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

create index "obj_ref__53_index1" on "obj_ref__53" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__53_index2" on "obj_ref__53" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__54" cascade;

create table "obj_ref__54"
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

create index "obj_ref__54_index1" on "obj_ref__54" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__54_index2" on "obj_ref__54" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__55" cascade;

create table "obj_ref__55"
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

create index "obj_ref__55_index1" on "obj_ref__55" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__55_index2" on "obj_ref__55" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__56" cascade;

create table "obj_ref__56"
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

create index "obj_ref__56_index1" on "obj_ref__56" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__56_index2" on "obj_ref__56" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__57" cascade;

create table "obj_ref__57"
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

create index "obj_ref__57_index1" on "obj_ref__57" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__57_index2" on "obj_ref__57" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__58" cascade;

create table "obj_ref__58"
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

create index "obj_ref__58_index1" on "obj_ref__58" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__58_index2" on "obj_ref__58" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__59" cascade;

create table "obj_ref__59"
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

create index "obj_ref__59_index1" on "obj_ref__59" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__59_index2" on "obj_ref__59" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__60" cascade;

create table "obj_ref__60"
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

create index "obj_ref__60_index1" on "obj_ref__60" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__60_index2" on "obj_ref__60" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__61" cascade;

create table "obj_ref__61"
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

create index "obj_ref__61_index1" on "obj_ref__61" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__61_index2" on "obj_ref__61" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__62" cascade;

create table "obj_ref__62"
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

create index "obj_ref__62_index1" on "obj_ref__62" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__62_index2" on "obj_ref__62" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__63" cascade;

create table "obj_ref__63"
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

create index "obj_ref__63_index1" on "obj_ref__63" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__63_index2" on "obj_ref__63" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__64" cascade;

create table "obj_ref__64"
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

create index "obj_ref__64_index1" on "obj_ref__64" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__64_index2" on "obj_ref__64" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__65" cascade;

create table "obj_ref__65"
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

create index "obj_ref__65_index1" on "obj_ref__65" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__65_index2" on "obj_ref__65" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__66" cascade;

create table "obj_ref__66"
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

create index "obj_ref__66_index1" on "obj_ref__66" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__66_index2" on "obj_ref__66" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__67" cascade;

create table "obj_ref__67"
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

create index "obj_ref__67_index1" on "obj_ref__67" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__67_index2" on "obj_ref__67" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__68" cascade;

create table "obj_ref__68"
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

create index "obj_ref__68_index1" on "obj_ref__68" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__68_index2" on "obj_ref__68" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__69" cascade;

create table "obj_ref__69"
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

create index "obj_ref__69_index1" on "obj_ref__69" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__69_index2" on "obj_ref__69" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__70" cascade;

create table "obj_ref__70"
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

create index "obj_ref__70_index1" on "obj_ref__70" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__70_index2" on "obj_ref__70" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__71" cascade;

create table "obj_ref__71"
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

create index "obj_ref__71_index1" on "obj_ref__71" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__71_index2" on "obj_ref__71" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__72" cascade;

create table "obj_ref__72"
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

create index "obj_ref__72_index1" on "obj_ref__72" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__72_index2" on "obj_ref__72" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__73" cascade;

create table "obj_ref__73"
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

create index "obj_ref__73_index1" on "obj_ref__73" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__73_index2" on "obj_ref__73" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__74" cascade;

create table "obj_ref__74"
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

create index "obj_ref__74_index1" on "obj_ref__74" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__74_index2" on "obj_ref__74" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__75" cascade;

create table "obj_ref__75"
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

create index "obj_ref__75_index1" on "obj_ref__75" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__75_index2" on "obj_ref__75" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__76" cascade;

create table "obj_ref__76"
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

create index "obj_ref__76_index1" on "obj_ref__76" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__76_index2" on "obj_ref__76" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__77" cascade;

create table "obj_ref__77"
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

create index "obj_ref__77_index1" on "obj_ref__77" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__77_index2" on "obj_ref__77" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__78" cascade;

create table "obj_ref__78"
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

create index "obj_ref__78_index1" on "obj_ref__78" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__78_index2" on "obj_ref__78" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__79" cascade;

create table "obj_ref__79"
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

create index "obj_ref__79_index1" on "obj_ref__79" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__79_index2" on "obj_ref__79" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__80" cascade;

create table "obj_ref__80"
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

create index "obj_ref__80_index1" on "obj_ref__80" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__80_index2" on "obj_ref__80" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__81" cascade;

create table "obj_ref__81"
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

create index "obj_ref__81_index1" on "obj_ref__81" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__81_index2" on "obj_ref__81" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__82" cascade;

create table "obj_ref__82"
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

create index "obj_ref__82_index1" on "obj_ref__82" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__82_index2" on "obj_ref__82" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__83" cascade;

create table "obj_ref__83"
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

create index "obj_ref__83_index1" on "obj_ref__83" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__83_index2" on "obj_ref__83" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__84" cascade;

create table "obj_ref__84"
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

create index "obj_ref__84_index1" on "obj_ref__84" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__84_index2" on "obj_ref__84" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__85" cascade;

create table "obj_ref__85"
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

create index "obj_ref__85_index1" on "obj_ref__85" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__85_index2" on "obj_ref__85" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__86" cascade;

create table "obj_ref__86"
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

create index "obj_ref__86_index1" on "obj_ref__86" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__86_index2" on "obj_ref__86" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__87" cascade;

create table "obj_ref__87"
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

create index "obj_ref__87_index1" on "obj_ref__87" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__87_index2" on "obj_ref__87" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__88" cascade;

create table "obj_ref__88"
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

create index "obj_ref__88_index1" on "obj_ref__88" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__88_index2" on "obj_ref__88" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__89" cascade;

create table "obj_ref__89"
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

create index "obj_ref__89_index1" on "obj_ref__89" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__89_index2" on "obj_ref__89" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__90" cascade;

create table "obj_ref__90"
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

create index "obj_ref__90_index1" on "obj_ref__90" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__90_index2" on "obj_ref__90" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__91" cascade;

create table "obj_ref__91"
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

create index "obj_ref__91_index1" on "obj_ref__91" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__91_index2" on "obj_ref__91" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__92" cascade;

create table "obj_ref__92"
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

create index "obj_ref__92_index1" on "obj_ref__92" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__92_index2" on "obj_ref__92" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__93" cascade;

create table "obj_ref__93"
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

create index "obj_ref__93_index1" on "obj_ref__93" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__93_index2" on "obj_ref__93" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__94" cascade;

create table "obj_ref__94"
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

create index "obj_ref__94_index1" on "obj_ref__94" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__94_index2" on "obj_ref__94" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__95" cascade;

create table "obj_ref__95"
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

create index "obj_ref__95_index1" on "obj_ref__95" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__95_index2" on "obj_ref__95" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__96" cascade;

create table "obj_ref__96"
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

create index "obj_ref__96_index1" on "obj_ref__96" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__96_index2" on "obj_ref__96" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__97" cascade;

create table "obj_ref__97"
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

create index "obj_ref__97_index1" on "obj_ref__97" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__97_index2" on "obj_ref__97" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__98" cascade;

create table "obj_ref__98"
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

create index "obj_ref__98_index1" on "obj_ref__98" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__98_index2" on "obj_ref__98" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__99" cascade;

create table "obj_ref__99"
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

create index "obj_ref__99_index1" on "obj_ref__99" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__99_index2" on "obj_ref__99" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__100" cascade;

create table "obj_ref__100"
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

create index "obj_ref__100_index1" on "obj_ref__100" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__100_index2" on "obj_ref__100" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__101" cascade;

create table "obj_ref__101"
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

create index "obj_ref__101_index1" on "obj_ref__101" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__101_index2" on "obj_ref__101" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__102" cascade;

create table "obj_ref__102"
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

create index "obj_ref__102_index1" on "obj_ref__102" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__102_index2" on "obj_ref__102" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__103" cascade;

create table "obj_ref__103"
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

create index "obj_ref__103_index1" on "obj_ref__103" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__103_index2" on "obj_ref__103" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__104" cascade;

create table "obj_ref__104"
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

create index "obj_ref__104_index1" on "obj_ref__104" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__104_index2" on "obj_ref__104" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__105" cascade;

create table "obj_ref__105"
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

create index "obj_ref__105_index1" on "obj_ref__105" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__105_index2" on "obj_ref__105" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__106" cascade;

create table "obj_ref__106"
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

create index "obj_ref__106_index1" on "obj_ref__106" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__106_index2" on "obj_ref__106" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__107" cascade;

create table "obj_ref__107"
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

create index "obj_ref__107_index1" on "obj_ref__107" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__107_index2" on "obj_ref__107" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__108" cascade;

create table "obj_ref__108"
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

create index "obj_ref__108_index1" on "obj_ref__108" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__108_index2" on "obj_ref__108" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__109" cascade;

create table "obj_ref__109"
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

create index "obj_ref__109_index1" on "obj_ref__109" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__109_index2" on "obj_ref__109" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__110" cascade;

create table "obj_ref__110"
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

create index "obj_ref__110_index1" on "obj_ref__110" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__110_index2" on "obj_ref__110" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__111" cascade;

create table "obj_ref__111"
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

create index "obj_ref__111_index1" on "obj_ref__111" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__111_index2" on "obj_ref__111" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__112" cascade;

create table "obj_ref__112"
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

create index "obj_ref__112_index1" on "obj_ref__112" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__112_index2" on "obj_ref__112" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__113" cascade;

create table "obj_ref__113"
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

create index "obj_ref__113_index1" on "obj_ref__113" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__113_index2" on "obj_ref__113" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__114" cascade;

create table "obj_ref__114"
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

create index "obj_ref__114_index1" on "obj_ref__114" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__114_index2" on "obj_ref__114" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__115" cascade;

create table "obj_ref__115"
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

create index "obj_ref__115_index1" on "obj_ref__115" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__115_index2" on "obj_ref__115" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__116" cascade;

create table "obj_ref__116"
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

create index "obj_ref__116_index1" on "obj_ref__116" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__116_index2" on "obj_ref__116" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__117" cascade;

create table "obj_ref__117"
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

create index "obj_ref__117_index1" on "obj_ref__117" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__117_index2" on "obj_ref__117" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__118" cascade;

create table "obj_ref__118"
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

create index "obj_ref__118_index1" on "obj_ref__118" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__118_index2" on "obj_ref__118" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__119" cascade;

create table "obj_ref__119"
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

create index "obj_ref__119_index1" on "obj_ref__119" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__119_index2" on "obj_ref__119" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__120" cascade;

create table "obj_ref__120"
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

create index "obj_ref__120_index1" on "obj_ref__120" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__120_index2" on "obj_ref__120" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__121" cascade;

create table "obj_ref__121"
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

create index "obj_ref__121_index1" on "obj_ref__121" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__121_index2" on "obj_ref__121" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__122" cascade;

create table "obj_ref__122"
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

create index "obj_ref__122_index1" on "obj_ref__122" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__122_index2" on "obj_ref__122" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__123" cascade;

create table "obj_ref__123"
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

create index "obj_ref__123_index1" on "obj_ref__123" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__123_index2" on "obj_ref__123" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__124" cascade;

create table "obj_ref__124"
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

create index "obj_ref__124_index1" on "obj_ref__124" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__124_index2" on "obj_ref__124" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__125" cascade;

create table "obj_ref__125"
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

create index "obj_ref__125_index1" on "obj_ref__125" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__125_index2" on "obj_ref__125" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__126" cascade;

create table "obj_ref__126"
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

create index "obj_ref__126_index1" on "obj_ref__126" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__126_index2" on "obj_ref__126" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


/* drop/create OBJ_REF */
drop table if exists "obj_ref__127" cascade;

create table "obj_ref__127"
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

create index "obj_ref__127_index1" on "obj_ref__127" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__127_index2" on "obj_ref__127" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__mtp_index1" on "obj_ref__mtp" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__mtp_index2" on "obj_ref__mtp" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


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

create index "obj_ref__user_index1" on "obj_ref__user" ("tenant_id", "obj_def_id", "obj_id", "ref_def_id");
create index "obj_ref__user_index2" on "obj_ref__user" ("tenant_id", "target_obj_def_id", "target_obj_id", "ref_def_id");


