drop table if exists "cache_store" cascade;
create table "cache_store"
(
    "ns" varchar(256) not null,
    "c_key" varchar(512) not null,
    "c_val" bytea,
    "ver" numeric(19,0),
    "cre_time" numeric(19,0),
    "inv_time" numeric(19,0),
    "ci_0" varchar(512),
    "ci_1" varchar(512),
    "ci_2" varchar(512),
    constraint "cache_store_pk" primary key ("ns", "c_key")
)
;
create index "cache_store_index0" on "cache_store" ("ns", "ci_0");
create index "cache_store_index1" on "cache_store" ("ns", "ci_1");
create index "cache_store_index2" on "cache_store" ("ns", "ci_2");
