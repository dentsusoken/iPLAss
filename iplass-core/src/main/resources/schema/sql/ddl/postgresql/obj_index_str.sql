drop table if exists "obj_index_str" cascade;
create table "obj_index_str"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "col_name" varchar(36) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "val" varchar(4000)
)
;
create index "obj_index_str_index1" on "obj_index_str" ("tenant_id", "obj_def_id", "col_name", "val");
create index "obj_index_str_index2" on "obj_index_str" ("tenant_id", "obj_def_id", "obj_id");
