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
partition by range ("tenant_id")
;
create index "obj_ref_rb_index1" on "obj_ref_rb" ("tenant_id", "obj_def_id", "rb_id");
create table "obj_ref_rb_0" partition of "obj_ref_rb" for values from (0) to (1) partition by hash ("obj_def_id");
create table "obj_ref_rb_0_0" partition of "obj_ref_rb_0" for values with (modulus 8, remainder 0);
create table "obj_ref_rb_0_1" partition of "obj_ref_rb_0" for values with (modulus 8, remainder 1);
create table "obj_ref_rb_0_2" partition of "obj_ref_rb_0" for values with (modulus 8, remainder 2);
create table "obj_ref_rb_0_3" partition of "obj_ref_rb_0" for values with (modulus 8, remainder 3);
create table "obj_ref_rb_0_4" partition of "obj_ref_rb_0" for values with (modulus 8, remainder 4);
create table "obj_ref_rb_0_5" partition of "obj_ref_rb_0" for values with (modulus 8, remainder 5);
create table "obj_ref_rb_0_6" partition of "obj_ref_rb_0" for values with (modulus 8, remainder 6);
create table "obj_ref_rb_0_7" partition of "obj_ref_rb_0" for values with (modulus 8, remainder 7);

/* drop/create OBJ_REF_RB__MTP */
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
partition by range ("tenant_id")
;
create index "obj_ref_rb__mtp_index1" on "obj_ref_rb__mtp" ("tenant_id", "obj_def_id", "rb_id");
create table "obj_ref_rb__mtp_0" partition of "obj_ref_rb__mtp" for values from (0) to (1) partition by hash ("obj_def_id");
create table "obj_ref_rb__mtp_0_0" partition of "obj_ref_rb__mtp_0" for values with (modulus 8, remainder 0);
create table "obj_ref_rb__mtp_0_1" partition of "obj_ref_rb__mtp_0" for values with (modulus 8, remainder 1);
create table "obj_ref_rb__mtp_0_2" partition of "obj_ref_rb__mtp_0" for values with (modulus 8, remainder 2);
create table "obj_ref_rb__mtp_0_3" partition of "obj_ref_rb__mtp_0" for values with (modulus 8, remainder 3);
create table "obj_ref_rb__mtp_0_4" partition of "obj_ref_rb__mtp_0" for values with (modulus 8, remainder 4);
create table "obj_ref_rb__mtp_0_5" partition of "obj_ref_rb__mtp_0" for values with (modulus 8, remainder 5);
create table "obj_ref_rb__mtp_0_6" partition of "obj_ref_rb__mtp_0" for values with (modulus 8, remainder 6);
create table "obj_ref_rb__mtp_0_7" partition of "obj_ref_rb__mtp_0" for values with (modulus 8, remainder 7);

/* drop/create OBJ_REF_RB__USER */
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
partition by range ("tenant_id")
;
create index "obj_ref_rb__user_index1" on "obj_ref_rb__user" ("tenant_id", "obj_def_id", "rb_id");
create table "obj_ref_rb__user_0" partition of "obj_ref_rb__user" for values from (0) to (1) partition by hash ("obj_def_id");
create table "obj_ref_rb__user_0_0" partition of "obj_ref_rb__user_0" for values with (modulus 8, remainder 0);
create table "obj_ref_rb__user_0_1" partition of "obj_ref_rb__user_0" for values with (modulus 8, remainder 1);
create table "obj_ref_rb__user_0_2" partition of "obj_ref_rb__user_0" for values with (modulus 8, remainder 2);
create table "obj_ref_rb__user_0_3" partition of "obj_ref_rb__user_0" for values with (modulus 8, remainder 3);
create table "obj_ref_rb__user_0_4" partition of "obj_ref_rb__user_0" for values with (modulus 8, remainder 4);
create table "obj_ref_rb__user_0_5" partition of "obj_ref_rb__user_0" for values with (modulus 8, remainder 5);
create table "obj_ref_rb__user_0_6" partition of "obj_ref_rb__user_0" for values with (modulus 8, remainder 6);
create table "obj_ref_rb__user_0_7" partition of "obj_ref_rb__user_0" for values with (modulus 8, remainder 7);
