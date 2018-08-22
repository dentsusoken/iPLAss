drop table if exists "counter" cascade;
create table "counter"
(
    "tenant_id" numeric(7,0) not null,
    "cnt_name" varchar(128) not null,
    "inc_unit_key" varchar(128) not null,
    "cnt_val" numeric(18,0),
    constraint "counter_pk" primary key ("tenant_id", "cnt_name", "inc_unit_key")
)
;
