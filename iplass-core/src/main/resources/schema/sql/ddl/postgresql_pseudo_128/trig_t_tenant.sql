drop function if exists func_trig_t_tenant() cascade;
create function func_trig_t_tenant() returns trigger as $trig_t_tenant$ 
begin
    if (new.id is null) then
      select nextval('seq_t_tenant_id') into new.id;
    end if;
    return new;
end;
$trig_t_tenant$ language plpgsql;

drop trigger if exists trig_t_tenant on "t_tenant" cascade;
create trigger trig_t_tenant
before insert on "t_tenant"
for each row execute procedure func_trig_t_tenant();
