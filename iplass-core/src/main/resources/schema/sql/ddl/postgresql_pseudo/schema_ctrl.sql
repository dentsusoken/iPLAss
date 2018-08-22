drop table if exists "schema_ctrl" cascade;
create table "schema_ctrl"
(
    "tenant_id" numeric(7,0),
    "obj_def_id" varchar(128),
    "obj_def_ver" numeric(10,0),
    "lock_status" varchar(1),
    "cr_data_ver" numeric(10,0),
    constraint "schema_ctrl_pk" primary key ("tenant_id", "obj_def_id")
)
;
