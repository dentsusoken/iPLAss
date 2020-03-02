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
)
partition by range ("tenant_id")
;
create table "delete_log_0" partition of "delete_log" for values from (0) to (1) partition by hash ("obj_def_id");
create table "delete_log_0_0" partition of "delete_log_0" for values with (modulus 8, remainder 0);
create table "delete_log_0_1" partition of "delete_log_0" for values with (modulus 8, remainder 1);
create table "delete_log_0_2" partition of "delete_log_0" for values with (modulus 8, remainder 2);
create table "delete_log_0_3" partition of "delete_log_0" for values with (modulus 8, remainder 3);
create table "delete_log_0_4" partition of "delete_log_0" for values with (modulus 8, remainder 4);
create table "delete_log_0_5" partition of "delete_log_0" for values with (modulus 8, remainder 5);
create table "delete_log_0_6" partition of "delete_log_0" for values with (modulus 8, remainder 6);
create table "delete_log_0_7" partition of "delete_log_0" for values with (modulus 8, remainder 7);
