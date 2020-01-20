drop table if exists "lob_store" cascade;
create table "lob_store"
(	"tenant_id" numeric(7,0),
	"lob_data_id" numeric(16,0),
	"cre_date" timestamp(3),
	"ref_count" numeric(10,0),
	"b_data" bytea,
	"lob_size" numeric(16,0),
    constraint "lob_store_pk" primary key ("tenant_id", "lob_data_id")
)
partition by range ("tenant_id")
;
create table "lob_store_0" partition of "lob_store" for values from (0) to (1);
