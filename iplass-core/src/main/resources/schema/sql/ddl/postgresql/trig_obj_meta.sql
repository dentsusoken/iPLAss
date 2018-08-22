drop function if exists func_trig_obj_meta() cascade;
create function func_trig_obj_meta() returns trigger as $trig_obj_meta$ 
begin
    perform lo_unlink(old.obj_meta_data);
    return old;
end;
$trig_obj_meta$ language plpgsql;

drop trigger if exists trig_obj_meta on "obj_meta" cascade;
create trigger trig_obj_meta
before update of obj_meta_data or delete on "obj_meta"
for each row execute procedure func_trig_obj_meta();
