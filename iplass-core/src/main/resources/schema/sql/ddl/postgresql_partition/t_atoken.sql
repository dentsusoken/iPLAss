drop table if exists "t_atoken" cascade;
create table "t_atoken"
(
    "tenant_id" numeric(7) not null,
    "t_type" varchar(32) not null,
    "u_key" varchar(128) not null,
    "series" varchar(128) not null,
    "token" varchar(128) not null,
    "pol_name" varchar(128),
    "s_date" timestamp(3),
    "t_info" bytea,
    constraint "t_atoken_pk" primary key ("tenant_id", "t_type", "series")
)
;
create index "t_atoken_index1" on "t_atoken" ("tenant_id", "t_type", "u_key");
create index "t_atoken_index2" on "t_atoken" ("tenant_id", "u_key");
