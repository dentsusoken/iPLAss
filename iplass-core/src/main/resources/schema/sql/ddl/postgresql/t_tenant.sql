drop table if exists "t_tenant" cascade;
create table "t_tenant"
(
    "id" numeric(7,0) not null,
    "name" varchar(256) not null,
    "description" varchar(4000),
    "host_name" varchar(256),
    "url" varchar(256) not null,
    "yuko_date_from" timestamp not null,
    "yuko_date_to" timestamp not null,
    "cre_user" varchar(64),
    "cre_date" timestamp(3),
    "up_user" varchar(64),
    "up_date" timestamp(3),
     constraint "t_tenant_pk" primary key ("url")
)
;
create index "t_tenant_index1" on "t_tenant" ("id");
