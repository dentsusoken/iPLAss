/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb" cascade;

create table "obj_ref_rb"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb_index1" on "obj_ref_rb" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__1" cascade;

create table "obj_ref_rb__1"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__1_index1" on "obj_ref_rb__1" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__2" cascade;

create table "obj_ref_rb__2"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__2_index1" on "obj_ref_rb__2" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__3" cascade;

create table "obj_ref_rb__3"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__3_index1" on "obj_ref_rb__3" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__4" cascade;

create table "obj_ref_rb__4"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__4_index1" on "obj_ref_rb__4" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__5" cascade;

create table "obj_ref_rb__5"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__5_index1" on "obj_ref_rb__5" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__6" cascade;

create table "obj_ref_rb__6"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__6_index1" on "obj_ref_rb__6" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__7" cascade;

create table "obj_ref_rb__7"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__7_index1" on "obj_ref_rb__7" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__8" cascade;

create table "obj_ref_rb__8"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__8_index1" on "obj_ref_rb__8" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__9" cascade;

create table "obj_ref_rb__9"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__9_index1" on "obj_ref_rb__9" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__10" cascade;

create table "obj_ref_rb__10"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__10_index1" on "obj_ref_rb__10" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__11" cascade;

create table "obj_ref_rb__11"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__11_index1" on "obj_ref_rb__11" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__12" cascade;

create table "obj_ref_rb__12"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__12_index1" on "obj_ref_rb__12" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__13" cascade;

create table "obj_ref_rb__13"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__13_index1" on "obj_ref_rb__13" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__14" cascade;

create table "obj_ref_rb__14"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__14_index1" on "obj_ref_rb__14" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__15" cascade;

create table "obj_ref_rb__15"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__15_index1" on "obj_ref_rb__15" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__16" cascade;

create table "obj_ref_rb__16"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__16_index1" on "obj_ref_rb__16" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__17" cascade;

create table "obj_ref_rb__17"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__17_index1" on "obj_ref_rb__17" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__18" cascade;

create table "obj_ref_rb__18"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__18_index1" on "obj_ref_rb__18" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__19" cascade;

create table "obj_ref_rb__19"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__19_index1" on "obj_ref_rb__19" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__20" cascade;

create table "obj_ref_rb__20"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__20_index1" on "obj_ref_rb__20" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__21" cascade;

create table "obj_ref_rb__21"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__21_index1" on "obj_ref_rb__21" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__22" cascade;

create table "obj_ref_rb__22"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__22_index1" on "obj_ref_rb__22" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__23" cascade;

create table "obj_ref_rb__23"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__23_index1" on "obj_ref_rb__23" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__24" cascade;

create table "obj_ref_rb__24"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__24_index1" on "obj_ref_rb__24" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__25" cascade;

create table "obj_ref_rb__25"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__25_index1" on "obj_ref_rb__25" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__26" cascade;

create table "obj_ref_rb__26"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__26_index1" on "obj_ref_rb__26" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__27" cascade;

create table "obj_ref_rb__27"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__27_index1" on "obj_ref_rb__27" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__28" cascade;

create table "obj_ref_rb__28"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__28_index1" on "obj_ref_rb__28" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__29" cascade;

create table "obj_ref_rb__29"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__29_index1" on "obj_ref_rb__29" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__30" cascade;

create table "obj_ref_rb__30"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__30_index1" on "obj_ref_rb__30" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__31" cascade;

create table "obj_ref_rb__31"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__31_index1" on "obj_ref_rb__31" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__32" cascade;

create table "obj_ref_rb__32"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__32_index1" on "obj_ref_rb__32" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__33" cascade;

create table "obj_ref_rb__33"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__33_index1" on "obj_ref_rb__33" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__34" cascade;

create table "obj_ref_rb__34"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__34_index1" on "obj_ref_rb__34" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__35" cascade;

create table "obj_ref_rb__35"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__35_index1" on "obj_ref_rb__35" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__36" cascade;

create table "obj_ref_rb__36"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__36_index1" on "obj_ref_rb__36" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__37" cascade;

create table "obj_ref_rb__37"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__37_index1" on "obj_ref_rb__37" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__38" cascade;

create table "obj_ref_rb__38"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__38_index1" on "obj_ref_rb__38" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__39" cascade;

create table "obj_ref_rb__39"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__39_index1" on "obj_ref_rb__39" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__40" cascade;

create table "obj_ref_rb__40"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__40_index1" on "obj_ref_rb__40" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__41" cascade;

create table "obj_ref_rb__41"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__41_index1" on "obj_ref_rb__41" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__42" cascade;

create table "obj_ref_rb__42"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__42_index1" on "obj_ref_rb__42" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__43" cascade;

create table "obj_ref_rb__43"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__43_index1" on "obj_ref_rb__43" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__44" cascade;

create table "obj_ref_rb__44"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__44_index1" on "obj_ref_rb__44" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__45" cascade;

create table "obj_ref_rb__45"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__45_index1" on "obj_ref_rb__45" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__46" cascade;

create table "obj_ref_rb__46"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__46_index1" on "obj_ref_rb__46" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__47" cascade;

create table "obj_ref_rb__47"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__47_index1" on "obj_ref_rb__47" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__48" cascade;

create table "obj_ref_rb__48"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__48_index1" on "obj_ref_rb__48" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__49" cascade;

create table "obj_ref_rb__49"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__49_index1" on "obj_ref_rb__49" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__50" cascade;

create table "obj_ref_rb__50"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__50_index1" on "obj_ref_rb__50" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__51" cascade;

create table "obj_ref_rb__51"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__51_index1" on "obj_ref_rb__51" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__52" cascade;

create table "obj_ref_rb__52"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__52_index1" on "obj_ref_rb__52" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__53" cascade;

create table "obj_ref_rb__53"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__53_index1" on "obj_ref_rb__53" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__54" cascade;

create table "obj_ref_rb__54"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__54_index1" on "obj_ref_rb__54" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__55" cascade;

create table "obj_ref_rb__55"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__55_index1" on "obj_ref_rb__55" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__56" cascade;

create table "obj_ref_rb__56"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__56_index1" on "obj_ref_rb__56" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__57" cascade;

create table "obj_ref_rb__57"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__57_index1" on "obj_ref_rb__57" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__58" cascade;

create table "obj_ref_rb__58"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__58_index1" on "obj_ref_rb__58" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__59" cascade;

create table "obj_ref_rb__59"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__59_index1" on "obj_ref_rb__59" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__60" cascade;

create table "obj_ref_rb__60"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__60_index1" on "obj_ref_rb__60" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__61" cascade;

create table "obj_ref_rb__61"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__61_index1" on "obj_ref_rb__61" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__62" cascade;

create table "obj_ref_rb__62"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__62_index1" on "obj_ref_rb__62" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__63" cascade;

create table "obj_ref_rb__63"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__63_index1" on "obj_ref_rb__63" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__64" cascade;

create table "obj_ref_rb__64"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__64_index1" on "obj_ref_rb__64" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__65" cascade;

create table "obj_ref_rb__65"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__65_index1" on "obj_ref_rb__65" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__66" cascade;

create table "obj_ref_rb__66"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__66_index1" on "obj_ref_rb__66" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__67" cascade;

create table "obj_ref_rb__67"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__67_index1" on "obj_ref_rb__67" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__68" cascade;

create table "obj_ref_rb__68"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__68_index1" on "obj_ref_rb__68" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__69" cascade;

create table "obj_ref_rb__69"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__69_index1" on "obj_ref_rb__69" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__70" cascade;

create table "obj_ref_rb__70"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__70_index1" on "obj_ref_rb__70" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__71" cascade;

create table "obj_ref_rb__71"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__71_index1" on "obj_ref_rb__71" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__72" cascade;

create table "obj_ref_rb__72"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__72_index1" on "obj_ref_rb__72" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__73" cascade;

create table "obj_ref_rb__73"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__73_index1" on "obj_ref_rb__73" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__74" cascade;

create table "obj_ref_rb__74"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__74_index1" on "obj_ref_rb__74" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__75" cascade;

create table "obj_ref_rb__75"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__75_index1" on "obj_ref_rb__75" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__76" cascade;

create table "obj_ref_rb__76"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__76_index1" on "obj_ref_rb__76" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__77" cascade;

create table "obj_ref_rb__77"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__77_index1" on "obj_ref_rb__77" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__78" cascade;

create table "obj_ref_rb__78"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__78_index1" on "obj_ref_rb__78" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__79" cascade;

create table "obj_ref_rb__79"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__79_index1" on "obj_ref_rb__79" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__80" cascade;

create table "obj_ref_rb__80"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__80_index1" on "obj_ref_rb__80" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__81" cascade;

create table "obj_ref_rb__81"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__81_index1" on "obj_ref_rb__81" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__82" cascade;

create table "obj_ref_rb__82"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__82_index1" on "obj_ref_rb__82" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__83" cascade;

create table "obj_ref_rb__83"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__83_index1" on "obj_ref_rb__83" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__84" cascade;

create table "obj_ref_rb__84"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__84_index1" on "obj_ref_rb__84" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__85" cascade;

create table "obj_ref_rb__85"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__85_index1" on "obj_ref_rb__85" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__86" cascade;

create table "obj_ref_rb__86"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__86_index1" on "obj_ref_rb__86" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__87" cascade;

create table "obj_ref_rb__87"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__87_index1" on "obj_ref_rb__87" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__88" cascade;

create table "obj_ref_rb__88"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__88_index1" on "obj_ref_rb__88" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__89" cascade;

create table "obj_ref_rb__89"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__89_index1" on "obj_ref_rb__89" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__90" cascade;

create table "obj_ref_rb__90"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__90_index1" on "obj_ref_rb__90" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__91" cascade;

create table "obj_ref_rb__91"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__91_index1" on "obj_ref_rb__91" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__92" cascade;

create table "obj_ref_rb__92"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__92_index1" on "obj_ref_rb__92" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__93" cascade;

create table "obj_ref_rb__93"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__93_index1" on "obj_ref_rb__93" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__94" cascade;

create table "obj_ref_rb__94"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__94_index1" on "obj_ref_rb__94" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__95" cascade;

create table "obj_ref_rb__95"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__95_index1" on "obj_ref_rb__95" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__96" cascade;

create table "obj_ref_rb__96"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__96_index1" on "obj_ref_rb__96" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__97" cascade;

create table "obj_ref_rb__97"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__97_index1" on "obj_ref_rb__97" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__98" cascade;

create table "obj_ref_rb__98"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__98_index1" on "obj_ref_rb__98" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__99" cascade;

create table "obj_ref_rb__99"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__99_index1" on "obj_ref_rb__99" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__100" cascade;

create table "obj_ref_rb__100"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__100_index1" on "obj_ref_rb__100" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__101" cascade;

create table "obj_ref_rb__101"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__101_index1" on "obj_ref_rb__101" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__102" cascade;

create table "obj_ref_rb__102"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__102_index1" on "obj_ref_rb__102" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__103" cascade;

create table "obj_ref_rb__103"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__103_index1" on "obj_ref_rb__103" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__104" cascade;

create table "obj_ref_rb__104"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__104_index1" on "obj_ref_rb__104" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__105" cascade;

create table "obj_ref_rb__105"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__105_index1" on "obj_ref_rb__105" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__106" cascade;

create table "obj_ref_rb__106"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__106_index1" on "obj_ref_rb__106" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__107" cascade;

create table "obj_ref_rb__107"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__107_index1" on "obj_ref_rb__107" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__108" cascade;

create table "obj_ref_rb__108"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__108_index1" on "obj_ref_rb__108" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__109" cascade;

create table "obj_ref_rb__109"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__109_index1" on "obj_ref_rb__109" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__110" cascade;

create table "obj_ref_rb__110"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__110_index1" on "obj_ref_rb__110" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__111" cascade;

create table "obj_ref_rb__111"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__111_index1" on "obj_ref_rb__111" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__112" cascade;

create table "obj_ref_rb__112"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__112_index1" on "obj_ref_rb__112" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__113" cascade;

create table "obj_ref_rb__113"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__113_index1" on "obj_ref_rb__113" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__114" cascade;

create table "obj_ref_rb__114"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__114_index1" on "obj_ref_rb__114" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__115" cascade;

create table "obj_ref_rb__115"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__115_index1" on "obj_ref_rb__115" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__116" cascade;

create table "obj_ref_rb__116"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__116_index1" on "obj_ref_rb__116" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__117" cascade;

create table "obj_ref_rb__117"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__117_index1" on "obj_ref_rb__117" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__118" cascade;

create table "obj_ref_rb__118"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__118_index1" on "obj_ref_rb__118" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__119" cascade;

create table "obj_ref_rb__119"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__119_index1" on "obj_ref_rb__119" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__120" cascade;

create table "obj_ref_rb__120"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__120_index1" on "obj_ref_rb__120" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__121" cascade;

create table "obj_ref_rb__121"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__121_index1" on "obj_ref_rb__121" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__122" cascade;

create table "obj_ref_rb__122"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__122_index1" on "obj_ref_rb__122" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__123" cascade;

create table "obj_ref_rb__123"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__123_index1" on "obj_ref_rb__123" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__124" cascade;

create table "obj_ref_rb__124"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__124_index1" on "obj_ref_rb__124" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__125" cascade;

create table "obj_ref_rb__125"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__125_index1" on "obj_ref_rb__125" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__126" cascade;

create table "obj_ref_rb__126"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__126_index1" on "obj_ref_rb__126" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__127" cascade;

create table "obj_ref_rb__127"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__127_index1" on "obj_ref_rb__127" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__mtp" cascade;

create table "obj_ref_rb__mtp"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__mtp_index1" on "obj_ref_rb__mtp" ("tenant_id", "obj_def_id", "rb_id");


/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__user" cascade;

create table "obj_ref_rb__user"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "rb_id" numeric(16,0),
    "ref_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "target_obj_def_id" varchar(128) not null,
    "target_obj_id" varchar(64) not null,
    "target_obj_ver" numeric(10,0) default 0 not null
)

;

create index "obj_ref_rb__user_index1" on "obj_ref_rb__user" ("tenant_id", "obj_def_id", "rb_id");


