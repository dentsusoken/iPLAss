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
