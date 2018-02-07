create table categories(
  id int primary key auto_increment not null,
  name varchar(256) not null
);

create table items(
  id int primary key auto_increment not null,
  name varchar(256) not null,
  categoryid int not null references categories(id),
  quantity int not null
);
