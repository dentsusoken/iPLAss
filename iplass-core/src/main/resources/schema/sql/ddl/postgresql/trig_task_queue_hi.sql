drop function if exists func_trig_task_queue_hi() cascade;
create function func_trig_task_queue_hi() returns trigger as $trig_task_queue_hi$ 
begin
    perform lo_unlink(old.callable);
    perform lo_unlink(old.res);
    return old;
end;
$trig_task_queue_hi$ language plpgsql;

drop trigger if exists trig_task_queue_hi on "task_queue_hi" cascade;
create trigger trig_task_queue_hi
before update of callable, res or delete on "task_queue_hi"
for each row execute procedure func_trig_task_queue_hi();
