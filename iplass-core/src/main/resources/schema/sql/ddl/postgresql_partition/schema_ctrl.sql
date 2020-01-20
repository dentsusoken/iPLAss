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
partition by range ("tenant_id")
;
create table "schema_ctrl_0" partition of "schema_ctrl" for values from (0) to (1) partition by hash ("obj_def_id");
create table "schema_ctrl_0_0" partition of "schema_ctrl_0" for values with (modulus 8, remainder 0);
create table "schema_ctrl_0_1" partition of "schema_ctrl_0" for values with (modulus 8, remainder 1);
create table "schema_ctrl_0_2" partition of "schema_ctrl_0" for values with (modulus 8, remainder 2);
create table "schema_ctrl_0_3" partition of "schema_ctrl_0" for values with (modulus 8, remainder 3);
create table "schema_ctrl_0_4" partition of "schema_ctrl_0" for values with (modulus 8, remainder 4);
create table "schema_ctrl_0_5" partition of "schema_ctrl_0" for values with (modulus 8, remainder 5);
create table "schema_ctrl_0_6" partition of "schema_ctrl_0" for values with (modulus 8, remainder 6);
create table "schema_ctrl_0_7" partition of "schema_ctrl_0" for values with (modulus 8, remainder 7);
