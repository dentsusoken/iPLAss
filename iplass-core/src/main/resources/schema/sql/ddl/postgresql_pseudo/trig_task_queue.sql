drop function if exists func_trig_task_queue() cascade;
create function func_trig_task_queue() returns trigger as $trig_task_queue$ 
declare
    result integer;
begin
    select into result count(*) from task_queue_hi where callable=old.callable;
    IF result = 0 THEN
        perform lo_unlink(old.callable);
    END IF;
    select into result count(*) from task_queue_hi where res=old.res;
    IF result = 0 THEN
        perform lo_unlink(old.res);
    END IF;
    return old;
end;
$trig_task_queue$ language plpgsql;

drop trigger if exists trig_task_queue on "task_queue" cascade;
create trigger trig_task_queue
after update of callable or delete on "task_queue"
for each row execute procedure func_trig_task_queue();
