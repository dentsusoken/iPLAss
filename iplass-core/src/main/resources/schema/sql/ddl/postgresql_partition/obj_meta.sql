drop table if exists "obj_meta" cascade;
create table "obj_meta"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "obj_def_ver" numeric(10,0) not null,
    "obj_def_path" varchar(1024) not null,
    "obj_def_disp_name" varchar(1024),
    "obj_desc" varchar(1024),
    "obj_meta_data" bytea not null,
    "status" varchar(1),
    "sharable" varchar(1),
    "overwritable" varchar(1),
    "cre_user" varchar(64),
    "cre_date" timestamp(3) not null,
    "up_user" varchar(64),
    "up_date" timestamp(3) not null,
     constraint "obj_meta_pk" primary key ("tenant_id", "obj_def_id", "obj_def_ver")
);
create index "obj_meta_index1" on "obj_meta" ("tenant_id", "obj_def_path", "status");
