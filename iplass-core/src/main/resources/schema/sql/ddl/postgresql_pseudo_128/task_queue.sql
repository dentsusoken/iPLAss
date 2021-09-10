drop table if exists "task_queue" cascade;
create table "task_queue"
(
    "tenant_id" numeric(7,0) not null,
    "q_id" numeric(7,0) not null,
    "task_id" numeric(16,0) not null,
    "v_time" numeric(16,0) not null,
    "status" varchar(1) not null,
    "g_key" varchar(128),
    "vw_id" numeric(7,0),
    "exp_mode" varchar(1) not null,
    "res_flg" varchar(1) not null,
    "ver" numeric(16,0) not null,
    "up_date" timestamp(3) not null,
    "server_id" varchar(128),
    "re_cnt" numeric(7,0),
    "callable" bytea,
    "res" bytea,
    constraint "task_queue_pk" primary key ("q_id", "tenant_id", "task_id")
)
;
