create table Schools(
    id varchar(64) not null primary key,
    name varchar(256) not null
);

create table Students(
    id varchar(64) not null primary key,
    schoolId varchar(64) not null,
    name varchar(256) not null,
    foreign key (schoolId) references Schools(id)
);
