drop table if exists "obj_unique_dbl" cascade;
create table "obj_unique_dbl"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" double precision
)
partition by range ("tenant_id")
;
create unique index "obj_unique_dbl_index1" on "obj_unique_dbl" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_dbl_index2" on "obj_unique_dbl" ("tenant_id", "obj_def_id", "obj_id");
create table "obj_unique_dbl_0" partition of "obj_unique_dbl" for values from (0) to (1) partition by hash ("obj_def_id");
create table "obj_unique_dbl_0_0" partition of "obj_unique_dbl_0" for values with (modulus 8, remainder 0);
create table "obj_unique_dbl_0_1" partition of "obj_unique_dbl_0" for values with (modulus 8, remainder 1);
create table "obj_unique_dbl_0_2" partition of "obj_unique_dbl_0" for values with (modulus 8, remainder 2);
create table "obj_unique_dbl_0_3" partition of "obj_unique_dbl_0" for values with (modulus 8, remainder 3);
create table "obj_unique_dbl_0_4" partition of "obj_unique_dbl_0" for values with (modulus 8, remainder 4);
create table "obj_unique_dbl_0_5" partition of "obj_unique_dbl_0" for values with (modulus 8, remainder 5);
create table "obj_unique_dbl_0_6" partition of "obj_unique_dbl_0" for values with (modulus 8, remainder 6);
create table "obj_unique_dbl_0_7" partition of "obj_unique_dbl_0" for values with (modulus 8, remainder 7);
