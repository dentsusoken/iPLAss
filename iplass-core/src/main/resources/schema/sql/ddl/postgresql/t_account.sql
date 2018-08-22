drop table if exists "t_account" cascade;
create table "t_account"
(
    "tenant_id" numeric(7) not null,
    "account_id" varchar(128) not null,
    "password" varchar(128) not null,
    "salt" varchar(64),
    "oid" varchar(128) not null,
    "last_login_on" timestamp(3),
    "last_password_change" timestamp,
    "login_err_cnt" numeric(2,0) default 0 not null,
    "login_err_date" timestamp(6),
    "pol_name" varchar(128),
    "cre_user" varchar(64),
    "cre_date" timestamp(3),
    "up_user" varchar(64),
    "up_date" timestamp(3),
    constraint "t_account_pk" primary key ("tenant_id", "account_id"),
    constraint "t_account_uq" unique ("tenant_id", "oid")
)
;
