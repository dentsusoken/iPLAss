drop sequence if exists seq_obj_id;
drop sequence if exists seq_lob_id;
drop sequence if exists seq_rb_id;
drop sequence if exists seq_log_id;
drop sequence if exists seq_t_tenant_id;

create sequence seq_obj_id minvalue 1 maxvalue 9999999999999999 increment 1 start with 1;
create sequence seq_lob_id minvalue 1 maxvalue 9999999999999999 increment 1 start with 1;
create sequence seq_rb_id minvalue 1 maxvalue 9999999999999999 increment 1 start with 1;
create sequence seq_log_id minvalue 1 maxvalue 9999999999999999 increment 1 start with 1;
create sequence seq_t_tenant_id minvalue 1 maxvalue 9999999 increment 1 start with 1 ;
