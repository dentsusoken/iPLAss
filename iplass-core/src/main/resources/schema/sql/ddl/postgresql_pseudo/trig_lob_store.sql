drop function if exists func_trig_lob_store() cascade;
create function func_trig_lob_store() returns trigger as $trig_lob_store$
begin
    perform lo_unlink(old.b_data);
    return old;
end;
$trig_lob_store$ language plpgsql;

drop trigger if exists trig_lob_store on "obj_meta" cascade;
create trigger trig_lob_store
before delete on "lob_store"
for each row execute procedure func_trig_lob_store();
