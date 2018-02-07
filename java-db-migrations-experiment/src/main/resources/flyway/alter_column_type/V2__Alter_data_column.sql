alter table table1 alter column data rename to olddata;
alter table table1 add column data int;
update table1 set data = cast(olddata as int);
alter table table1 drop column olddata;
alter table table1 alter column data set not null;
