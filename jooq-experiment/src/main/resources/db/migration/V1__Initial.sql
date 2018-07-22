create table Users(
    id varchar(255) not null,
    name varchar(255) not null,
    createdAt timestamp not null,
    postCount int not null,
    commentCount int not null,
    primary key (id)
);

create table Posts(
    id varchar(255) not null,
    authorId varchar(255) not null,
    title varchar(255) not null,
    text varchar(255) not null,
    createdAt timestamp not null,
    commentCount int not null,
    primary key (id),
    foreign key (authorId) references Users(id)
);

create table Comments(
    id varchar(255) not null,
    postId varchar(255) not null,
    authorId varchar(255) not null,
    text varchar(255) not null,
    createdAt timestamp not null,
    primary key (id),
    foreign key (postId) references Posts(id),
    foreign key (authorId) references Users(id)
);
