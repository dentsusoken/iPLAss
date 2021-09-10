drop table if exists "delete_log" cascade;
create table "delete_log"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "obj_id" varchar(64) not null,
    "obj_ver" numeric(10,0) default 0 not null,
    "status" varchar(1),
    "cre_date" timestamp(3),
    "up_date" timestamp(3)
);
