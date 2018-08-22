drop table if exists "obj_unique_dbl" cascade;
create table "obj_unique_dbl"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" double precision
)
;
create unique index "obj_unique_dbl_index1" on "obj_unique_dbl" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_dbl_index2" on "obj_unique_dbl" ("tenant_id", "obj_def_id", "obj_id");
