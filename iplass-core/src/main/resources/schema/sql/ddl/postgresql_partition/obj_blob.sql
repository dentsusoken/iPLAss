drop table if exists "obj_blob" cascade;
create table "obj_blob"
(
    "tenant_id" numeric(7,0),
    "lob_id" numeric(16,0),
    "lob_name" varchar(256),
    "lob_type" varchar(256),
    "lob_stat" varchar(1),
    "lob_data_id" numeric(16,0),
    "cre_date" timestamp(3),
    "up_date" timestamp(3),
    "cre_user" varchar(64),
    "up_user" varchar(64),
    "sess_id" varchar(128),
    "obj_def_id" varchar(128),
    "prop_def_id" varchar(128),
    "obj_id" varchar(64),
    "obj_ver" numeric(10,0)
)
partition by range ("tenant_id")
;
create index "obj_blob_index1" on "obj_blob" ("tenant_id", "lob_id");
create table "obj_blob_0" partition of "obj_blob" for values from (0) to (1);
