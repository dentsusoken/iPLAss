drop table if exists "obj_unique_str" cascade;
create table "obj_unique_str"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "val" varchar(4000)
)
;
create unique index "obj_unique_str_index1" on "obj_unique_str" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_unique_str_index2" on "obj_unique_str" ("tenant_id", "obj_def_id", "obj_id");
