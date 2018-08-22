/* drop/create OBJ_REF_RB */
drop table if exists "obj_ref_rb" cascade;
create table "obj_ref_rb"
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
create index "obj_ref_rb_index1" on "obj_ref_rb" ("tenant_id", "obj_def_id", "rb_id");

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
