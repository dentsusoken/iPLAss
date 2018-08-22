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
drop table if exists "obj_ref_rb__mtp" cascade;
create table "obj_ref_rb__mtp"
(
    "rb_id" numeric(16,0),
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
create index "obj_ref_rb__mtp_index1" on "obj_ref_rb__mtp" ("tenant_id", "obj_def_id", "rb_id");

/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb__user" cascade;
create table "obj_ref_rb__user"
(
    "rb_id" numeric(16,0),
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
create index "obj_ref_rb__user_index1" on "obj_ref_rb__user" ("tenant_id", "obj_def_id", "rb_id");
