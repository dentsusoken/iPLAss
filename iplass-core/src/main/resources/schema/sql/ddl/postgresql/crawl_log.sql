drop table if exists "crawl_log" cascade;
create table "crawl_log"
(
    "tenant_id" numeric(7,0) not null,
    "obj_def_id" varchar(128) not null,
    "obj_def_ver" numeric(10,0) not null,
    "cre_date" timestamp(3),
    "up_date" timestamp(3)
);
create index "crawl_log_index1" on "crawl_log" ("tenant_id", "obj_def_id", "obj_def_ver");
