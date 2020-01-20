drop table if exists "obj_blob_rb" cascade;
create table "obj_blob_rb"
(
    "tenant_id" numeric(7,0),
    "rb_id" numeric(16,0),
    "lob_id" numeric(16,0)
)
partition by range ("tenant_id")
;
create index "obj_blob_rb_index1" on "obj_blob_rb" ("tenant_id", "rb_id");
create table "obj_blob_rb_0" partition of "obj_blob_rb" for values from (0) to (1);
