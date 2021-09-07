drop table if exists "t_pass_hi" cascade;
create table "t_pass_hi"
(
    "tenant_id" numeric(7) not null,
    "account_id" varchar(128) not null,
    "password" varchar(128) not null,
    "salt" varchar(64),
    "up_date" timestamp(3)
)
;
create index "t_pass_hi_index" on "t_pass_hi" ("tenant_id", "account_id");
