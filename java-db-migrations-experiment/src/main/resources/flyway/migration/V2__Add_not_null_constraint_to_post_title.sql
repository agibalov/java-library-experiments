update Posts
set title = ''
where title is null;

alter table Posts
alter column title set not null;

