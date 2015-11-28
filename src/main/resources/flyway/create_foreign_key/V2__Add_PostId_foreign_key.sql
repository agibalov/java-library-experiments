alter table comments add column PostId int not null;

alter table comments add foreign key (PostId) references posts(id);